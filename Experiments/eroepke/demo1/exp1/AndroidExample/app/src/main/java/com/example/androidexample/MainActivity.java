package com.example.androidexample;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {


        private TextView messageText;
        private EditText usernameInput;
        private EditText passwordInput;
        private Button submitButton;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            /* Initialize UI elements */
            messageText = findViewById(R.id.main_msg_txt);
            usernameInput = findViewById(R.id.username_input);
            passwordInput = findViewById(R.id.password_input);
            submitButton = findViewById(R.id.submit_btn);

            /* Set default text */
            messageText.setText("Please enter your credentials");

            /* Handle button click */
            submitButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String username = usernameInput.getText().toString();
                    String password = passwordInput.getText().toString();

                    // Simple check for demonstration purposes
                    if (username.equals("admin") && password.equals("password123")) {
                        messageText.setText("Welcome, " + username + "!");
                    } else {
                        messageText.setText("Invalid credentials, please try again.");
                    }
                }
            });
        }
    }
