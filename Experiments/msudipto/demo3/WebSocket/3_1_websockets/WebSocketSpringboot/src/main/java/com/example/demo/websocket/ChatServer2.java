package com.example.demo.websocket;

import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * WebSocket chat server for real-time communication between users.
 *
 * This server handles user authentication, broadcasting messages,
 * sending private messages, and managing user commands.
 *
 * Example URL: ws://localhost:8080/chat/username
 **/

@ServerEndpoint("/chat/2/{username}")
@Component
public class ChatServer2 {

    // Maps for session-username management
    private static final Map<Session, String> sessionUsernameMap = new ConcurrentHashMap<>();
    private static final Map<String, Session> usernameSessionMap = new ConcurrentHashMap<>();
    private static final Set<String> authenticatedUsers = ConcurrentHashMap.newKeySet();

    private final Logger logger = LoggerFactory.getLogger(ChatServer2.class);

    // Simulated user authentication
    private static final Map<String, String> userCredentials = Map.of(
            "user1", "password1",
            "user2", "password2",
            "admin", "admin123"
    );

    /**
     * On new connection, authenticate user and add to session map.
     **/

    @OnOpen
    public void onOpen(Session session, @PathParam("username") String username) throws IOException {
        logger.info("[onOpen] Attempt to connect by: " + username);

        // Authenticate user (simple demo)
        if (!userCredentials.containsKey(username)) {
            session.getBasicRemote().sendText("Invalid username!");
            session.close(new CloseReason(CloseReason.CloseCodes.CANNOT_ACCEPT, "Invalid username"));
            return;
        }

        // Check if the username is already in use
        if (usernameSessionMap.containsKey(username)) {
            session.getBasicRemote().sendText("Username already in use!");
            session.close(new CloseReason(CloseReason.CloseCodes.CANNOT_ACCEPT, "Username already in use"));
            return;
        }

        // Add to session map
        sessionUsernameMap.put(session, username);
        usernameSessionMap.put(username, session);
        authenticatedUsers.add(username);

        session.setMaxIdleTimeout(300000); // Set timeout to 5 minutes

        sendMessageToUser(username, "Welcome to the chat server, " + username);
        broadcast("User " + username + " has joined the chat!");
    }

    /**
     * Handles incoming messages.
     **/

    @OnMessage
    public void onMessage(Session session, String message) throws IOException {
        String username = sessionUsernameMap.get(session);
        logger.info("[onMessage] " + username + ": " + message);

        // Command handling
        if (message.startsWith("@")) {
            handleCommand(username, message);
        } else {
            // Broadcast message to all
            broadcast(username + ": " + message);
        }
    }

    /**
     * Handle command messages.
     **/

    private void handleCommand(String username, String message) {
        String[] splitMessage = message.split("\\s+", 2);
        String command = splitMessage[0].substring(1); // Remove '@'

        switch (command.toLowerCase()) {
            case "list":
                listUsers(username);
                break;
            case "exit":
                disconnectUser(username);
                break;
            default:
                sendMessageToUser(username, "Unknown command: " + command);
        }
    }

    /**
     * List all online users.
     **/

    private void listUsers(String username) {
        String userList = String.join(", ", authenticatedUsers);
        sendMessageToUser(username, "Online users: " + userList);
    }

    /**
     * Disconnect a user.
     **/

    private void disconnectUser(String username) {
        try {
            Session session = usernameSessionMap.get(username);
            if (session != null) {
                session.close(new CloseReason(CloseReason.CloseCodes.NORMAL_CLOSURE, "User requested disconnect"));
            }
        } catch (IOException e) {
            logger.error("[disconnectUser] Error: " + e.getMessage());
        }
    }

    /**
     * On connection close, remove user from session map.
     **/

    @OnClose
    public void onClose(Session session, CloseReason reason) {
        String username = sessionUsernameMap.remove(session);
        if (username != null) {
            usernameSessionMap.remove(username);
            authenticatedUsers.remove(username);
            logger.info("[onClose] " + username + " disconnected. Reason: " + reason.getReasonPhrase());
            broadcast("User " + username + " has left the chat!");
        }
    }

    /**
     * On error, log the error message.
     **/

    @OnError
    public void onError(Session session, Throwable throwable) {
        String username = sessionUsernameMap.get(session);
        if (username != null) {
            logger.error("[onError] " + username + ": " + throwable.getMessage());
            sendMessageToUser(username, "Error: " + throwable.getMessage());
        }
    }

    /**
     * Send a message to a specific user.
     **/

    private void sendMessageToUser(String username, String message) {
        Session session = usernameSessionMap.get(username);
        if (session != null) {
            try {
                session.getBasicRemote().sendText(message);
            } catch (IOException e) {
                logger.error("[sendMessageToUser] Error: " + e.getMessage());
            }
        }
    }

    /**
     * Broadcast message to all users.
     **/

    private void broadcast(String message) {
        sessionUsernameMap.forEach((session, username) -> {
            try {
                session.getBasicRemote().sendText(message);
            } catch (IOException e) {
                logger.error("[broadcast] Error: " + e.getMessage());
            }
        });
    }
}