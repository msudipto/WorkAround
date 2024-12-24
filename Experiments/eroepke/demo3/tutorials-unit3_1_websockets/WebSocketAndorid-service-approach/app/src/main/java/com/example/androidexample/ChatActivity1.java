package com.example.androidexample;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class ChatActivity1 extends AppCompatActivity {

    private Button sendBtn, backMainBtn;
    private EditText msgEtx;
    private TextView msgTv, typingStatusTv, otherUserTypingStatusTv;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat1);

        /* initialize UI elements */
        sendBtn = findViewById(R.id.sendBtn);
        backMainBtn = findViewById(R.id.backMainBtn);
        msgEtx = findViewById(R.id.msgEdt);
        msgTv = findViewById(R.id.tx1);
        typingStatusTv = findViewById(R.id.typingStatus);  // Your typing status
        otherUserTypingStatusTv = findViewById(R.id.otherUserTypingStatus);  // Other user's typing status

        /* send button listener */
        sendBtn.setOnClickListener(v -> {
            Intent intent = new Intent("SendWebSocketMessage");
            intent.putExtra("key", "chat1");
            intent.putExtra("message", msgEtx.getText().toString());
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
        });

        /* back button listener */
        backMainBtn.setOnClickListener(view -> {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        });

        // Typing status listener for you
        msgEtx.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Intent intent = new Intent("TypingStatusUpdate");
                intent.putExtra("key", "chat1");
                intent.putExtra("typing", charSequence.length() > 0);
                LocalBroadcastManager.getInstance(ChatActivity1.this).sendBroadcast(intent);
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });
    }

    // Broadcast receiver for messages and typing status
    private final BroadcastReceiver messageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String key = intent.getStringExtra("key");

            if ("chat1".equals(key)) {
                if (intent.hasExtra("message")) {
                    String message = intent.getStringExtra("message");
                    runOnUiThread(() -> {
                        String s = msgTv.getText().toString();
                        msgTv.setText(s + "\n" + message);
                    });
                }

                if (intent.hasExtra("typing")) {
                    boolean isTyping = intent.getBooleanExtra("typing", false);
                    runOnUiThread(() -> setTypingStatus(typingStatusTv, isTyping));
                }

                if (intent.hasExtra("otherUserTyping")) {
                    boolean isOtherUserTyping = intent.getBooleanExtra("otherUserTyping", false);
                    runOnUiThread(() -> setTypingStatus(otherUserTypingStatusTv, isOtherUserTyping));
                }
            }
        }
    };

    // Method to animate typing status
    private void setTypingStatus(TextView typingStatus, boolean isTyping) {
        if (isTyping) {
            typingStatus.setText("User is typing...");
            typingStatus.startAnimation(getFadeAnimation());
        } else {
            typingStatus.setText("");
            typingStatus.clearAnimation();
        }
    }

    // Method to create fade animation
    private Animation getFadeAnimation() {
        AlphaAnimation fade = new AlphaAnimation(0.0f, 1.0f);
        fade.setDuration(500);  // Duration of fade-in
        fade.setRepeatMode(Animation.REVERSE);
        fade.setRepeatCount(Animation.INFINITE);  // Repeat indefinitely
        return fade;
    }

    @Override
    protected void onStart() {
        super.onStart();
        LocalBroadcastManager.getInstance(this).registerReceiver(messageReceiver,
                new IntentFilter("WebSocketMessageReceived"));
    }

    @Override
    protected void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(messageReceiver);
    }
}

