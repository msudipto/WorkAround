package com.example.androidexample;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

public class projectEmployerActivity extends AppCompatActivity {
    private LinearLayout projectListLayout;
    private Button createProjButton;
    private String loggedInUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.projectemployer);

        // Retrieve username from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        loggedInUsername = sharedPreferences.getString("username", null);

        createProjButton = findViewById(R.id.createTasks);
        projectListLayout = findViewById(R.id.projectlayoutEmployer);

        Toolbar toolbar = findViewById(R.id.toolbarEmployer);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Projects");

        createProjButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(projectEmployerActivity.this, createTasksActivity.class);
                startActivity(intent);
            }
        });

        // Fetch all projects from the backend
        List<Long> projectIds = Arrays.asList(124L, 125L, 126L);
        fetchProjects(projectIds);

    }

    private void fetchProjects(List<Long> projectIds) {
        projectListLayout.removeAllViews(); // Clear previous views

        // Loop through each project ID and fetch the project data
        for (Long projectId : projectIds) {
            String url = "http://coms-3090-046.class.las.iastate.edu:8080/api/project/projectId/" + projectId;

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            // Extract fields from the JSON object
                            String projectName = response.optString("projectName", "Unnamed Project");
                            String projectDescription = response.optString("description", "No description available.");
                            String priority = response.optString("priority", "No priority");
                            String dueDate = response.optString("dueDate", "No due date");

                            // Create and configure a CardView for each project
                            createProjectCard(projectName, projectDescription, priority, dueDate);
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            error.printStackTrace();
                        }
                    });

            // Add the request to the RequestQueue
            Volley.newRequestQueue(this).add(jsonObjectRequest);
        }
    }



    private void createProjectCard(String projectName, String description, String priority, String dueDate) {
        CardView cardView = new CardView(projectEmployerActivity.this);
        cardView.setCardElevation(8);
        cardView.setRadius(16);
        cardView.setUseCompatPadding(true);

        // Create a LinearLayout to hold the TextViews inside the CardView
        LinearLayout cardLayout = new LinearLayout(projectEmployerActivity.this);
        cardLayout.setOrientation(LinearLayout.VERTICAL);
        cardLayout.setPadding(16, 16, 16, 16);

        // Create TextViews for each line
        TextView nameView = new TextView(projectEmployerActivity.this);
        nameView.setText("Project: " + projectName);
        nameView.setPadding(0, 0, 0, 8);

        TextView descriptionView = new TextView(projectEmployerActivity.this);
        descriptionView.setText("Description: " + description);
        descriptionView.setPadding(0, 8, 0, 8);

        TextView dueDateView = new TextView(projectEmployerActivity.this);
        dueDateView.setText("Due Date: " + dueDate);
        dueDateView.setPadding(0, 8, 0, 8);

        TextView priorityView = new TextView(projectEmployerActivity.this);
        priorityView.setText("Priority: " + priority);
        priorityView.setPadding(8, 4, 8, 4);

        // Set background color based on priority level
        int priorityBackgroundColor;
        switch (priority.toLowerCase()) {
            case "high":
                priorityBackgroundColor = ContextCompat.getColor(projectEmployerActivity.this, android.R.color.holo_red_light);
                break;
            case "medium":
                priorityBackgroundColor = ContextCompat.getColor(projectEmployerActivity.this, android.R.color.holo_orange_light);
                break;
            case "low":
                priorityBackgroundColor = ContextCompat.getColor(projectEmployerActivity.this, android.R.color.holo_green_light);
                break;
            default:
                priorityBackgroundColor = ContextCompat.getColor(projectEmployerActivity.this, android.R.color.darker_gray);
        }
        priorityView.setBackgroundColor(priorityBackgroundColor);
        priorityView.setTextColor(ContextCompat.getColor(projectEmployerActivity.this, android.R.color.white));
        priorityView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

        // Add TextViews to the card layout
        cardLayout.addView(nameView);
        cardLayout.addView(descriptionView);
        cardLayout.addView(dueDateView);
        cardLayout.addView(priorityView);

        // Add the card layout to the CardView
        cardView.addView(cardLayout);

        // Add the CardView to the main layout
        projectListLayout.addView(cardView);


        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) cardView.getLayoutParams();
        layoutParams.setMargins(16, 16, 16, 16);
        cardView.setLayoutParams(layoutParams);
    }

    // When on back button check userType to make sure goes back to right page
    private void fetchUserProfile(final String username) {
        // URL to fetch user profile using the username
        String url = "https://dfb6bb63-c0ea-4c10-bbd9-c6201d4aa3a3.mock.pstmn.io/project";

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
                                    intent = new Intent(projectEmployerActivity.this, adminActivity.class);
                                    break;
                                case "EMPLOYER":
                                    intent = new Intent(projectEmployerActivity.this, employerActivity.class);
                                    break;
                                case "EMPLOYEE":
                                    intent = new Intent(projectEmployerActivity.this, employeeActivity.class);
                                    break;
                                default:
                                    //Toast.makeText(projectEmployerActivity.this, "Unknown user type", Toast.LENGTH_SHORT);
                                    return;
                            }
                            startActivity(intent);

                        } catch (JSONException e) {
                            //Toast.makeText(projectEmployerActivity.this,"Error parsing user profile.", Toast.LENGTH_SHORT);
                            Log.e("Profile Error", "JSON parsing error", e);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Toast.makeText(projectEmployerActivity.this,"Failed to fetch user profile.", Toast.LENGTH_SHORT);
                        Log.e("Profile Error", error.toString());
                    }
                });

        // Add the profile request to the Volley request queue
        Volley.newRequestQueue(projectEmployerActivity.this).add(profileRequest);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            fetchUserProfile(loggedInUsername);
        }
        return super.onOptionsItemSelected(item);
    }
}

