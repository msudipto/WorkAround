package coms309.websocket;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import coms309.entity.Chat;
import coms309.repository.ChatRepository;
import coms309.controller.ImageController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Controller;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnError;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;

@Controller
@ServerEndpoint(value = "/chat/{chatType}/{username}")
public class ChatSocket {

    private static ChatRepository msgRepo;
    private static ImageController imageController;
    private static final Map<String, Map<String, Session>> chatSessions = new ConcurrentHashMap<>();

    @Autowired
    public void setChatRepository(ChatRepository repository) {
        msgRepo = repository;
    }

    @Autowired
    public void setImageController(ImageController controller) {
        imageController = controller;
    }

    @OnOpen
    public void onOpen(Session session, @PathParam("chatType") String chatType, @PathParam("username") String username) throws IOException {
        chatSessions.putIfAbsent(chatType, new ConcurrentHashMap<>());
        chatSessions.get(chatType).put(username, session);
        broadcastMessage(chatType, "Server", username + " has joined the chat.");
    }

    @OnMessage
    public void onMessage(String message, @PathParam("chatType") String chatType, @PathParam("username") String username) {
        if (message.startsWith("/image ")) {
            String imagePath = message.substring(7);
            handleImageUpload(chatType, username, imagePath);
        } else {
            Chat chatMessage = new Chat(username, message);
            msgRepo.save(chatMessage);
            broadcastMessage(chatType, username, message);
        }
    }

    @OnClose
    public void onClose(Session session, @PathParam("chatType") String chatType, @PathParam("username") String username) throws IOException {
        chatSessions.getOrDefault(chatType, new ConcurrentHashMap<>()).remove(username);
        broadcastMessage(chatType, "Server", username + " has left the chat.");
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        throwable.printStackTrace();
    }

    private void broadcastMessage(String chatType, String sender, String message) {
        chatSessions.getOrDefault(chatType, new ConcurrentHashMap<>()).values().forEach(session -> {
            try {
                session.getBasicRemote().sendText(sender + ": " + message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void handleImageUpload(String chatType, String username, String imagePath) {
        try {
            InputStreamResource imageResource = imageController.loadImageAsResource(imagePath);
            String message = "[Image uploaded by " + username + "]";
            broadcastMessage(chatType, username, message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
