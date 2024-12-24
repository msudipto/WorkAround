package com.example.androidexample;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class payCheckSearchActivity extends AppCompatActivity {

    private EditText usernameInput;
    private Button searchUsers;
    private Button modifyUsers;
    private TextView noUserFoundText;
    private RequestQueue requestQueue;
    private String username;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.paychecksearch);

        usernameInput = findViewById(R.id.usernameInput);
        searchUsers = findViewById(R.id.searchUsers);
        modifyUsers = findViewById(R.id.modifyUsers);
        noUserFoundText = findViewById(R.id.noUserMessage);
        requestQueue = Volley.newRequestQueue(this);

        // Set onClickListener for the search button
        searchUsers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchForUser();
            }
        });

        // Set onClickListener for the modify button
        modifyUsers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = usernameInput.getText().toString().trim();
                if (username.isEmpty()) {
                    Toast.makeText(payCheckSearchActivity.this, "Please enter a username to modify", Toast.LENGTH_SHORT).show();
                } else {
                    // Check if the user exists before allowing to modify pay
                    checkUserExistsAndModify(username);
                }
            }
        });
    }

    private void searchForUser() {
        username = usernameInput.getText().toString().trim();
        if (username.isEmpty()) {
            Toast.makeText(this, "Please enter a username to search", Toast.LENGTH_SHORT).show();
            return;
        }

        String url = "http://coms-3090-046.class.las.iastate.edu:8080/api/userprofile/username/" + username;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        handleUserFound(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error.networkResponse != null && error.networkResponse.statusCode == 404) {
                            noUserFoundText.setText("No user found");
                        } else {
                            noUserFoundText.setText("Error: " + error.getMessage());
                        }
                    }
                }
        );

        requestQueue.add(jsonObjectRequest);
    }

    private void handleUserFound(JSONObject response) {
        try {
            String returnedUsername = response.getString("username");
            String email = response.getString("email");

            // Log and show a success message if needed
            Toast.makeText(payCheckSearchActivity.this, "User found: " + returnedUsername, Toast.LENGTH_SHORT).show();

            // Navigate to the overview page and pass the username
            Intent intent = new Intent(payCheckSearchActivity.this, payCheckOverviewActivity.class);
            intent.putExtra("username", returnedUsername);
            startActivity(intent);
        } catch (JSONException e) {
            noUserFoundText.setText("Error parsing response");
        }
    }

    private void checkUserExistsAndModify(String username) {
        String url = "http://coms-3090-046.class.las.iastate.edu:8080/api/userprofile/username/" + username;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // If we get a response, the user exists, so proceed to modify
                        Intent intent = new Intent(payCheckSearchActivity.this, payCheckModifyActivity.class);
                        intent.putExtra("username", username);
                        startActivity(intent);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // If user does not exist, show a message
                        if (error.networkResponse != null && error.networkResponse.statusCode == 404) {
                            Toast.makeText(payCheckSearchActivity.this, "User does not exist. Cannot modify pay.", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(payCheckSearchActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );

        requestQueue.add(jsonObjectRequest);
    }
}

