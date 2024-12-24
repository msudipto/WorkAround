package com.example.androidexample;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class chatActivity extends AppCompatActivity {

    private TextView chatTitle;
    private RecyclerView recyclerViewMessages;
    private EditText messageInput;
    private Button sendButton;
    private String loggedInUsername;

    private messageAdapter messageAdapter;
    private List<String> messageList;

    private chatWebSocketClient chatWebSocketClient;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chatactivity);

        Toolbar toolbar = findViewById(R.id.chat_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("");

        chatTitle = findViewById(R.id.chatTitle);
        recyclerViewMessages = findViewById(R.id.recyclerView_messages);
        messageInput = findViewById(R.id.messageInput);
        sendButton = findViewById(R.id.sendButton);

        messageInput.requestFocus();

        // Retrieve data from SharedPreferences instead of Intent
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        loggedInUsername = sharedPreferences.getString("username", null);
        String name = sharedPreferences.getString("name", "");
        boolean isGroup = sharedPreferences.getBoolean("isGroup", false);
        username = sharedPreferences.getString("username", "");  // Get username
        String chatId = sharedPreferences.getString("chatId", "");

        chatTitle.setText(isGroup ? "Group: " + name : name);

        // Initialize message list and adapter
        messageList = new ArrayList<>();
        messageAdapter = new messageAdapter(this, messageList, true);
        recyclerViewMessages.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewMessages.setAdapter(messageAdapter);

        // Initialize and start WebSocket connection
        String chatType = isGroup ? "group" : "individual";
        chatWebSocketClient = new chatWebSocketClient();
        chatWebSocketClient.startWebSocket(chatType, username);  // Pass username to WebSocket

        // Listen for incoming messages from WebSocket
        chatWebSocketClient.setMessageListener(new chatWebSocketClient.MessageListener() {
            @Override
            public void onMessageReceived(String message) {
                runOnUiThread(() -> {
                    // Only add the message if it isn't sent by the user (to avoid duplication)
                    if (!message.startsWith(username + ": ")) {
                        messageList.add(message);
                        messageAdapter.notifyItemInserted(messageList.size() - 1);
                        recyclerViewMessages.scrollToPosition(messageList.size() - 1);
                    }
                });
            }
        });

        // Set up send button to send messages over WebSocket
        sendButton.setOnClickListener(v -> {
            String message = messageInput.getText().toString().trim();
            if (!message.isEmpty()) {
                String formattedMessage = message;
                chatWebSocketClient.sendMessage(formattedMessage);
                messageList.add(formattedMessage);
                messageAdapter.notifyItemInserted(messageList.size() - 1);
                recyclerViewMessages.scrollToPosition(messageList.size() - 1);
                messageInput.setText("");
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (chatWebSocketClient != null) {
            chatWebSocketClient.closeWebSocket();
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(chatActivity.this, messageActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}









