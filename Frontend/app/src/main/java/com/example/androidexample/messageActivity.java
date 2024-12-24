package com.example.androidexample;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

public class messageActivity extends AppCompatActivity {

    private RecyclerView recyclerViewIndividual;
    private RecyclerView recyclerViewGroup;
    private Button newChat;
    private String loggedInUsername;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.messagehome);

        recyclerViewIndividual = findViewById(R.id.recyclerView_individual);
        recyclerViewGroup = findViewById(R.id.recyclerView_group);
        newChat = findViewById(R.id.newChat);

        // Retrieve username from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        loggedInUsername = sharedPreferences.getString("username", null);

        setTitle("Messages");

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        newChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(messageActivity.this, chatActivity.class);
                startActivity(intent);
            }
        });


        // Fetch messages from backend
        Apiservice apiService = new Apiservice(this);
        apiService.getMessages("user_chat_id", new Apiservice.MessageCallback() {
            @Override
            public void onSuccess(List<String> messages) {
                recyclerViewIndividual.setLayoutManager(new LinearLayoutManager(messageActivity.this));
                recyclerViewIndividual.setAdapter(new messageAdapter(messageActivity.this, messages, false));

            }

            @Override
            public void onError(VolleyError error) {
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            fetchUserProfile(loggedInUsername);
        }
        return super.onOptionsItemSelected(item);
    }

    // When on back button check userType to make sure goes back to right page
    private void fetchUserProfile(final String username) {
        // URL to fetch user profile using the username
        String url = "http://coms-3090-046.class.las.iastate.edu:8080/api/userprofile/username/" + username;

        // Make a GET request to fetch the user profile
        JsonObjectRequest profileRequest = new JsonObjectRequest(Request.Method.GET, url,
                null, // No body needed for GET request
                new Response.Listener<JSONObject>() {
                    @SuppressLint("ShowToast")
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
                                    intent = new Intent(messageActivity.this, adminActivity.class);
                                    break;
                                case "EMPLOYER":
                                    intent = new Intent(messageActivity.this, employerActivity.class);
                                    break;
                                case "EMPLOYEE":
                                    intent = new Intent(messageActivity.this, employeeActivity.class);
                                    break;
                                default:
                                    //Toast.makeText(messageActivity.this, "Unknown user type", Toast.LENGTH_SHORT);
                                    return;
                            }
                            startActivity(intent);

                        } catch (JSONException e) {
                            //Toast.makeText(messageActivity.this,"Error parsing user profile.", Toast.LENGTH_SHORT);
                            Log.e("Profile Error", "JSON parsing error", e);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Toast.makeText(messageActivity.this,"Failed to fetch user profile.", Toast.LENGTH_SHORT);
                        Log.e("Profile Error", error.toString());
                    }
                });

        // Add the profile request to the Volley request queue
        Volley.newRequestQueue(messageActivity.this).add(profileRequest);
    }
}


