package com.example.androidexample;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class resetPasswordActivity extends AppCompatActivity {

    private Button backButton;
    private EditText passwordInput;
    private Button changePasswordButton;
    private TextView messageText;
    private String userEmail;



    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.resetpassword);

        userEmail = getIntent().getStringExtra("email");

        backButton = findViewById(R.id.backButton);
        passwordInput = findViewById(R.id.newPasswordInput);
        changePasswordButton = findViewById(R.id.changePassword);
        messageText = findViewById(R.id.messageText);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(resetPasswordActivity.this, loginActivity.class);
                startActivity(intent);
            }
        });

        changePasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newPassword = passwordInput.getText().toString().trim();

                // Check to make sure password has minimum requirements
                if (!isValidPassword(newPassword)) {
                    showAlertDialog("Error", "Password must be at least 8 characters long, contain 1 uppercase letter, 1 number, and 1 special character");
                    return;
                }
                String url = "http://coms-3090-046.class.las.iastate.edu:8080/login/resetPassword?email=" + userEmail + "&newPassword=" + newPassword;

                RequestQueue queue = Volley.newRequestQueue(resetPasswordActivity.this);


                StringRequest putRequest = new StringRequest(Request.Method.PUT, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                //Toast.makeText(resetPasswordActivity.this, "Password successfully updated.", Toast.LENGTH_SHORT).show();

                                // Delay 2 seconds
                                new android.os.Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        Intent intent = new Intent(resetPasswordActivity.this, loginActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                }, 2500);
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                // Handle error responses
                                if (error.networkResponse != null && error.networkResponse.statusCode == 404) {
                                    messageText.setText("No user exists with this email.");
                                } else {
                                    messageText.setText("An error occurred. Please try again.");
                                }
                            }
                        });
                queue.add(putRequest);
            }
        });

    }


    private boolean isValidPassword(String passwordFilled) {
        // Password must be at least 8 characters long, contain an uppercase letter, a number, and a special character
        return passwordFilled.length() >= 8
                && passwordFilled.matches(".*[A-Z].*")        // At least one uppercase letter
                && passwordFilled.matches(".*[0-9].*")        // At least one digit
                && passwordFilled.matches(".*[!@#\\$%^&*].*"); // At least one special character
    }

    private void showAlertDialog(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(resetPasswordActivity.this);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton("OK", (dialog, which) -> dialog.dismiss());
        builder.show();
    }
}


