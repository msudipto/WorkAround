package com.cs309.websocket3.chat;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import jakarta.websocket.OnClose;
import jakarta.websocket.OnError;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller  // Required for Spring Boot endpoint
@ServerEndpoint(value = "/chat/{username}")  // WebSocket URL
public class ChatSocket {

	private static MessageRepository msgRepo;

	@Autowired
	public void setMessageRepository(MessageRepository repo) {
		msgRepo = repo;  // Setting the static variable
	}

	// Thread-safe maps for session and username management
	private static final Map<Session, String> sessionUsernameMap = new ConcurrentHashMap<>();
	private static final Map<String, Session> usernameSessionMap = new ConcurrentHashMap<>();

	private final Logger logger = LoggerFactory.getLogger(ChatSocket.class);

	@OnOpen
	public void onOpen(Session session, @PathParam("username") String username) throws IOException {
		logger.info("User {} connected", username);

		// Store connecting user information
		sessionUsernameMap.put(session, username);
		usernameSessionMap.put(username, session);

		// Send chat history to the newly connected user
		sendMessageToParticularUser(username, getChatHistory());

		// Broadcast that a new user joined
		broadcast("User: " + username + " has joined the chat.");
	}

	@OnMessage
	public void onMessage(Session session, String message) throws IOException {
		String username = sessionUsernameMap.get(session);
		logger.info("Message received from {}: {}", username, message);

		if (message.startsWith("@")) {
			// Direct message handling
			String destUsername = message.split(" ")[0].substring(1);
			String directMessage = "[DM] " + username + ": " + message.substring(destUsername.length() + 2);
			sendMessageToParticularUser(destUsername, directMessage);
			sendMessageToParticularUser(username, directMessage);
		} else {
			// Broadcast message
			broadcast(username + ": " + message);
		}

		// Save chat history to repository
		msgRepo.save(new Message(username, message));
	}

	@OnClose
	public void onClose(Session session) throws IOException {
		String username = sessionUsernameMap.remove(session);
		usernameSessionMap.remove(username);
		logger.info("User {} disconnected", username);

		// Broadcast that the user disconnected
		broadcast(username + " disconnected");
	}

	@OnError
	public void onError(Session session, Throwable throwable) {
		String username = sessionUsernameMap.get(session);
		logger.error("Error occurred for user {}: {}", username, throwable.getMessage());
		throwable.printStackTrace();
	}

	private void sendMessageToParticularUser(String username, String message) {
		try {
			Session session = usernameSessionMap.get(username);
			if (session != null && session.isOpen()) {
				session.getBasicRemote().sendText(message);
			}
		} catch (IOException e) {
			logger.error("Failed to send message to {}: {}", username, e.getMessage());
		}
	}

	private void broadcast(String message) {
		sessionUsernameMap.keySet().parallelStream().forEach(session -> {
			try {
				session.getBasicRemote().sendText(message);
			} catch (IOException e) {
				logger.error("Failed to broadcast message: {}", e.getMessage());
			}
		});
	}

	private String getChatHistory() {
		List<Message> messages = msgRepo.findAll();
		StringBuilder sb = new StringBuilder();
		if (messages != null && !messages.isEmpty()) {
			messages.forEach(message -> sb.append(message.getUserName())
					.append(": ")
					.append(message.getContent())
					.append("\n"));
		}
		return sb.toString();
	}

	// New Feature: List active users
	public String listActiveUsers() {
		return String.join(", ", usernameSessionMap.keySet());
	}

	// New Feature: Clear chat history
	public void clearChatHistory() {
		msgRepo.deleteAll();
		logger.info("Chat history cleared.");
	}
}