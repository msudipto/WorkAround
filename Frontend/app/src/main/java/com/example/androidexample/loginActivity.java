package com.example.androidexample;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


/**
 * This page is what a user would see when they open the app and login with their credentials. They will login with their credentials.They can also reset password or create account from this page
 */
public class loginActivity extends AppCompatActivity {

    private TextView messageText;
    private EditText usernameInput;
    private EditText passwordInput;
    private Button submitButton;
    private ImageButton showPassword;
    private Button forgotPasswordButton;
    private Button newUserButton;

    boolean isPasswordVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        /* Initialize UI Elements */
        submitButton = findViewById(R.id.submitButton);
        usernameInput = findViewById(R.id.usernameInput);
        passwordInput = findViewById(R.id.passwordInput);
        messageText = findViewById(R.id.mainMessage);

        forgotPasswordButton = findViewById(R.id.forgotButton);
        newUserButton = findViewById(R.id.newUserJoin);

        showPassword = findViewById(R.id.showPassword);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the user's username/password inputs, trim to remove whitespace
                String username = usernameInput.getText().toString().trim();
                String password = passwordInput.getText().toString().trim();

                // Check if fields are filled, then attempt login
                if (!username.isEmpty() && !password.isEmpty()) {
                    String url = "http://coms-3090-046.class.las.iastate.edu:8080/login?username=" + username + "&password=" + password;

                    Log.d("Logging in", url);
                    StringRequest loginRequest = new StringRequest(Request.Method.GET, url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    if (response.equals("Login successful")) {
                                        // If login is successful, fetch user profile
                                        fetchUserProfile(username);
                                    } else {
                                        messageText.setText(response); // Display error message from the login API
                                    }
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    messageText.setText("Login failed. Please try again.");
                                    Log.e("Volley", error.toString());
                                }
                            });

                    // Add the login request to the Volley request queue
                    Volley.newRequestQueue(loginActivity.this).add(loginRequest);
                } else {
                    messageText.setText("Please enter both username and password.");
                }
            }
        });

        showPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isPasswordVisible) {
                    passwordInput.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    showPassword.setImageResource(R.drawable.eyehide);
                } else {
                    passwordInput.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    showPassword.setImageResource(R.drawable.eyeshow);
                }
                isPasswordVisible = !isPasswordVisible;
            }
        });

        forgotPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(loginActivity.this, forgotpasswordActivity.class);
                startActivity(intent);
            }
        });

        newUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(loginActivity.this, joinNowActivity.class);
                startActivity(intent);
            }
        });
    }

    // Method to check userType to send to right page
    private void fetchUserProfile(final String username) {
        // URL to fetch user profile using the username
        String url = "http://coms-3090-046.class.las.iastate.edu:8080/api/userprofile/username/" + username;

        // Make a GET request to fetch the user profile
        JsonObjectRequest profileRequest = new JsonObjectRequest(Request.Method.GET, url,
                null, // No body needed for GET request
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            // Extract user details from the response
                            int userId = response.getInt("userId");
                            String fullName = response.getString("fullName");
                            String userType = response.getString("userType");

                            // Save these details in SharedPreferences
                            SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putInt("userId", userId);
                            editor.putString("username", username);
                            editor.putString("userType", userType);
                            editor.putString("fullName", fullName);
                            editor.apply();

                            // Redirect to appropriate activity based on user type
                            Intent intent;
                            switch (userType) {
                                case "ADMIN":
                                    intent = new Intent(loginActivity.this, adminActivity.class);
                                    break;
                                case "EMPLOYER":
                                    intent = new Intent(loginActivity.this, employerActivity.class);
                                    break;
                                case "EMPLOYEE":
                                    intent = new Intent(loginActivity.this, employeeActivity.class);
                                    break;
                                default:
                                    messageText.setText("Unknown user type");
                                    return;
                            }
                            startActivity(intent);

                        } catch (JSONException e) {
                            messageText.setText("Error parsing user profile.");
                            Log.e("Profile Error", "JSON parsing error", e);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        messageText.setText("Failed to fetch user profile.");
                        Log.e("Profile Error", error.toString());
                    }
                });

        // Add the profile request to the Volley request queue
        Volley.newRequestQueue(loginActivity.this).add(profileRequest);
    }
}
