package com.example.androidexample;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.java_websocket.handshake.ServerHandshake;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity implements WebSocketListener {

    private Button sendBtn, searchBtn;
    private EditText msgEtx, searchEdt;
    private TextView msgTv;
    private List<String> allMessages = new ArrayList<>(); // Store all messages
    private List<String> filteredMessages = new ArrayList<>(); // Store filtered messages for display

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        /* Initialize UI elements */
        sendBtn = findViewById(R.id.sendBtn);
        msgEtx = findViewById(R.id.msgEdt);
        msgTv = findViewById(R.id.tx1);
        searchEdt = findViewById(R.id.searchEdt);
        searchBtn = findViewById(R.id.searchBtn);

        /* Connect this activity to the WebSocket instance */
        WebSocketManager.getInstance().setWebSocketListener(ChatActivity.this);

        /* Send button listener */
        sendBtn.setOnClickListener(v -> {
            String message = msgEtx.getText().toString();
            if (!message.isEmpty()) {
                WebSocketManager.getInstance().sendMessage(message);
                updateChat("You: " + message);
                msgEtx.setText("");
            }
        });

        /* Search button listener */
        searchBtn.setOnClickListener(v -> filterMessages(searchEdt.getText().toString()));

        /* Optional: Real-time search listener */
        searchEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterMessages(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    /* Update chat text view with new messages */
    private void updateChat(String message) {
        allMessages.add(message); // Store message in allMessages list
        filterMessages(searchEdt.getText().toString()); // Refresh display based on search input
    }

    /* Filter messages based on search input */
    private void filterMessages(String query) {
        filteredMessages.clear();
        for (String message : allMessages) {
            if (message.toLowerCase().contains(query.toLowerCase())) {
                filteredMessages.add(message);
            }
        }
        displayMessages();
    }

    /* Display messages (either all or filtered) in TextView */
    private void displayMessages() {
        StringBuilder displayText = new StringBuilder();
        for (String message : filteredMessages.isEmpty() ? allMessages : filteredMessages) {
            displayText.append(message).append("\n");
        }
        msgTv.setText(displayText.toString().trim());
    }

    @Override
    public void onWebSocketMessage(String message) {
        runOnUiThread(() -> updateChat("Server: " + message));
    }

    @Override
    public void onWebSocketClose(int code, String reason, boolean remote) {
        String closedBy = remote ? "Server" : "You";
        runOnUiThread(() -> updateChat("---\nConnection closed by " + closedBy + "\nReason: " + reason + "\n---"));
    }

    @Override
    public void onWebSocketOpen(ServerHandshake handshakedata) {
        runOnUiThread(() -> updateChat("---\nConnected to server!\n---"));
    }

    @Override
    public void onWebSocketError(Exception ex) {
        runOnUiThread(() -> updateChat("---\nError: " + ex.getMessage() + "\n---"));
    }
}