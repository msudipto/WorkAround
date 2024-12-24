package com.example.androidexample;

import okhttp3.*;
import java.io.IOException;

public class chatWebSocketClient {

    private WebSocket webSocket;
    private MessageListener messageListener;

    // Define the interface for message callback
    public interface MessageListener {
        void onMessageReceived(String message);
    }

    // Method to set the message listener
    public void setMessageListener(MessageListener listener) {
        this.messageListener = listener;
    }

    public void startWebSocket(String chatType, String username) {
        OkHttpClient client = new OkHttpClient();
        String url = "ws://coms-3090-046.class.las.iastate.edu:8080/chat/individual/" + username;
        Request request = new Request.Builder().url(url).build();

        webSocket = client.newWebSocket(request, new WebSocketListener() {
            @Override
            public void onOpen(WebSocket webSocket, Response response) {
                // Connection opened
            }

            @Override
            public void onMessage(WebSocket webSocket, String text) {
                // Pass received message to listener
                if (messageListener != null) {
                    messageListener.onMessageReceived(text);
                }
            }

            @Override
            public void onFailure(WebSocket webSocket, Throwable t, Response response) {
                t.printStackTrace();
            }

            @Override
            public void onClosed(WebSocket webSocket, int code, String reason) {
                // WebSocket closed
            }
        });
    }

    public void sendMessage(String message) {
        if (webSocket != null) {
            webSocket.send(message);
        }
    }

    public void closeWebSocket() {
        if (webSocket != null) {
            webSocket.close(1000, "Closing connection");
        }
    }
}




