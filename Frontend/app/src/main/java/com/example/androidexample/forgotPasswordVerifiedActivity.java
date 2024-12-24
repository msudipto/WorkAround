package com.example.androidexample;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class forgotPasswordVerifiedActivity extends AppCompatActivity {

    private TextView getEmail;
    private Button backHome;
    private Button resendEmail;
    private TextView messageText;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgotpasswordverified);

        getEmail = findViewById(R.id.emailPlace);
        backHome = findViewById(R.id.backHome);
        resendEmail = findViewById(R.id.resendButton);
        messageText = findViewById(R.id.checkAgain);

        String email = getIntent().getStringExtra("userEmail");
        getEmail.setText(email);


        backHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(forgotPasswordVerifiedActivity.this, loginActivity.class);
                startActivity(intent);
            }
        });

        resendEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //RESEND EMAIL TO RESET PASSWORD

                messageText.setText("Check Email for new Password Reset");

            }
        });

    }
}