package com.example.androidexample;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.java_websocket.handshake.ServerHandshake;

public class MainActivity extends AppCompatActivity implements WebSocketListener {

    private Button connectBtn;
    private EditText serverEtx, usernameEtx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* initialize UI elements */
        connectBtn = findViewById(R.id.connectBtn);
        serverEtx = findViewById(R.id.serverEdt);
        usernameEtx = findViewById(R.id.unameEdt);

        /* connect button listener */
        connectBtn.setOnClickListener(view -> {
            String serverUrl = serverEtx.getText().toString() + usernameEtx.getText().toString();

            // Establish WebSocket connection and set listener
            WebSocketManager.getInstance().connectWebSocket(serverUrl);
            WebSocketManager.getInstance().setWebSocketListener(MainActivity.this);

            // Go to chat activity
            Intent intent = new Intent(this, ChatActivity.class);
            startActivity(intent);
        });
    }

    @Override
    public void onWebSocketOpen(ServerHandshake handshakedata) {
        // Display a toast when WebSocket connection opens
        runOnUiThread(() -> Toast.makeText(this, "Connected to server!", Toast.LENGTH_SHORT).show());
    }

    @Override
    public void onWebSocketMessage(String message) {
        // Display received message in a Toast
        runOnUiThread(() -> Toast.makeText(this, "New message: " + message, Toast.LENGTH_SHORT).show());
    }

    @Override
    public void onWebSocketClose(int code, String reason, boolean remote) {
        // Show a Toast message when WebSocket connection closes
        runOnUiThread(() -> Toast.makeText(this, "Disconnected: " + reason, Toast.LENGTH_SHORT).show());
    }

    @Override
    public void onWebSocketError(Exception ex) {
        // Show a Toast message when WebSocket encounters an error
        runOnUiThread(() -> Toast.makeText(this, "Connection error: " + ex.getMessage(), Toast.LENGTH_SHORT).show());
    }
}

