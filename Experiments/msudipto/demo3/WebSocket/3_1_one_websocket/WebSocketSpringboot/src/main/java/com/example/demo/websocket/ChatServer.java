package com.example.demo.websocket;

import java.io.IOException;
import java.util.Hashtable;
import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import jakarta.websocket.OnClose;
import jakarta.websocket.OnError;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Represents a WebSocket chat server for handling real-time communication
 * between users. Each user connects to the server using their unique
 * username.
 *
 * This class is annotated with Spring's `@ServerEndpoint` and `@Component`
 * annotations, making it a WebSocket endpoint that can handle WebSocket
 * connections at the "/chat/{username}" endpoint.
 *
 * Example URL: ws://localhost:8080/chat/username
 *
 * The server provides functionality for broadcasting messages to all connected
 * users and sending messages to specific users.
 **/

@ServerEndpoint("/chat/{username}/{token}")
@Component
public class ChatServer {

    // Store all socket session and their corresponding username
    private static Map<Session, String> sessionUsernameMap = new Hashtable<>();
    private static Map<String, Session> usernameSessionMap = new Hashtable<>();

    // Server side logger
    private final Logger logger = LoggerFactory.getLogger(ChatServer.class);

    // Scheduled Executor for sending heartbeats
    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    /**
     * This method is called when a new WebSocket connection is established.
     *
     * @param session  represents the WebSocket session for the connected user.
     * @param username username specified in the path parameter.
     * @param token    authentication token for the user.
     **/

    @OnOpen
    public void onOpen(Session session, @PathParam("username") String username, @PathParam("token") String token) throws IOException {
        // Authenticate the user
        if (!isValidToken(token)) {
            session.getBasicRemote().sendText("Invalid authentication token. Connection rejected.");
            session.close();
            return;
        }

        // Log the connection
        logger.info("[onOpen] Connection established for user: " + username);

        // Handle duplicate usernames
        if (usernameSessionMap.containsKey(username)) {
            session.getBasicRemote().sendText("Username already exists. Please choose a different one.");
            session.close();
            return;
        }

        // Add user to the session map
        sessionUsernameMap.put(session, username);
        usernameSessionMap.put(username, session);

        // Start sending heartbeat pings
        startHeartbeat(session);

        // Send a welcome message to the new user
        sendMessageToUser(username, "Welcome to the chat server, " + username);

        // Broadcast to other users
        broadcast("User " + username + " has joined the chat.");
    }

    /**
     * Handles incoming WebSocket messages from a client.
     *
     * @param session The WebSocket session representing the client's connection.
     * @param message The message received from the client.
     **/

    @OnMessage
    public void onMessage(Session session, String message) throws IOException {
        String username = sessionUsernameMap.get(session);
        logger.info("[onMessage] " + username + ": " + message);

        if (message.equalsIgnoreCase("ping")) {
            session.getBasicRemote().sendText("pong"); // Respond to pings for heartbeat
            return;
        }

        if (message.startsWith("@")) {
            // Handle direct message
            String[] splitMessage = message.split("\\s+", 2);
            String recipient = splitMessage[0].substring(1);  // Extract username
            String actualMessage = splitMessage.length > 1 ? splitMessage[1] : "";

            if (usernameSessionMap.containsKey(recipient)) {
                sendMessageToUser(recipient, "[DM from " + username + "]: " + actualMessage);
                sendMessageToUser(username, "[DM to " + recipient + "]: " + actualMessage);
            } else {
                sendMessageToUser(username, "User " + recipient + " not found.");
            }
        } else {
            // Broadcast to all users
            broadcast(username + ": " + message);
        }
    }

    /**
     * Handles the closure of a WebSocket connection.
     *
     * @param session The WebSocket session that is being closed.
     **/

    @OnClose
    public void onClose(Session session) throws IOException {
        String username = sessionUsernameMap.remove(session);
        usernameSessionMap.remove(username);

        // Log the closure
        logger.info("[onClose] Connection closed for user: " + username);

        // Notify other users
        broadcast("User " + username + " has left the chat.");
    }

    /**
     * Handles WebSocket errors.
     *
     * @param session   The WebSocket session where the error occurred.
     * @param throwable The Throwable representing the error condition.
     **/

    @OnError
    public void onError(Session session, Throwable throwable) {
        String username = sessionUsernameMap.get(session);
        logger.error("[onError] Error for user " + username + ": " + throwable.getMessage());
    }

    /**
     * Sends a message to a specific user.
     *
     * @param username The username of the recipient.
     * @param message  The message to be sent.
     **/

    private void sendMessageToUser(String username, String message) {
        try {
            Session session = usernameSessionMap.get(username);
            if (session != null) {
                session.getBasicRemote().sendText(message);
            }
        } catch (IOException e) {
            logger.error("[sendMessageToUser] Error sending message to " + username + ": " + e.getMessage());
        }
    }

    /**
     * Broadcasts a message to all connected users.
     *
     * @param message The message to be broadcasted.
     **/

    private void broadcast(String message) {
        sessionUsernameMap.forEach((session, username) -> {
            try {
                session.getBasicRemote().sendText(message);
            } catch (IOException e) {
                logger.error("[broadcast] Error broadcasting message: " + e.getMessage());
            }
        });
    }

    /**
     * Returns the list of currently connected users.
     *
     * @return List of connected usernames.
     **/

    public static String getConnectedUsers() {
        return String.join(", ", usernameSessionMap.keySet());
    }

    /**
     * Validates the user's authentication token.
     *
     * @param token The token to be validated.
     * @return true if the token is valid, false otherwise.
     **/

    private boolean isValidToken(String token) {
        // Placeholder for token validation logic
        return "valid_token".equals(token);  // Replace with real validation
    }

    /**
     * Starts the heartbeat mechanism to ensure live connections.
     *
     * @param session The session to which pings will be sent.
     **/

    private void startHeartbeat(Session session) {
        scheduler.scheduleAtFixedRate(() -> {
            try {
                if (session.isOpen()) {
                    session.getBasicRemote().sendText("ping");
                }
            } catch (IOException e) {
                logger.error("[startHeartbeat] Error sending ping: " + e.getMessage());
            }
        }, 0, 30, TimeUnit.SECONDS); // Ping every 30 seconds
    }
}