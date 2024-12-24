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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.core.content.ContextCompat;

/**
 * This class is for Admin view, they are able to create new projects. They are able to view all projects made.
 */
public class projectActivity extends AppCompatActivity {
    private LinearLayout projectListLayout;
    private List<Map<String, String>> projectList;
    private Button createProjButton;
    private String loggedInUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.projectassgin);

        // Retrieve username from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        loggedInUsername = sharedPreferences.getString("username", null);

        createProjButton = findViewById(R.id.createProject);
        projectListLayout = findViewById(R.id.project_list_layout);
        projectList = new ArrayList<>();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Projects");

        createProjButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(projectActivity.this, createProject.class);
                startActivity(intent);
            }
        });

        // Fetch projects from the backend
        fetchProjects();
    }


    private void fetchProjects() {
        String url = "https://dfb6bb63-c0ea-4c10-bbd9-c6201d4aa3a3.mock.pstmn.io/project";

        // Use JsonObjectRequest to parse the response as a JSON object
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            System.out.println("JSON Response: " + response.toString());

                            // Get the 'projects' array from the response object
                            JSONArray projectsArray = response.getJSONArray("projects");

                            // Iterate over the array and extract each project object
                            for (int i = 0; i < projectsArray.length(); i++) {
                                JSONObject projectObject = projectsArray.getJSONObject(i);

                                // Extract fields from the JSON object
                                String projectName = projectObject.optString("projectName", "Unnamed Project");
                                String projectDescription = projectObject.optString("description", "No description available.");
                                String priority = projectObject.optString("priority", "No priority");
                                String dueDate = projectObject.optString("dueDate", "No due date");

                                // Format text for each line
                                String projectNameText = "Project: " + projectName;
                                String projectDescriptionText = "Description: " + projectDescription;
                                String dueDateText = "Due Date: " + dueDate;

                                // Create a CardView for each project
                                CardView cardView = new CardView(projectActivity.this);
                                cardView.setCardElevation(8);
                                cardView.setRadius(16);
                                cardView.setUseCompatPadding(true);

                                // Create a LinearLayout to hold the TextViews inside the CardView
                                LinearLayout cardLayout = new LinearLayout(projectActivity.this);
                                cardLayout.setOrientation(LinearLayout.VERTICAL);
                                cardLayout.setPadding(16, 16, 16, 16);

                                // Create TextViews for each line
                                TextView nameView = new TextView(projectActivity.this);
                                nameView.setText(projectNameText);
                                nameView.setPadding(0, 0, 0, 8);

                                TextView descriptionView = new TextView(projectActivity.this);
                                descriptionView.setText(projectDescriptionText);
                                descriptionView.setPadding(0, 8, 0, 8);

                                // TextView for Due Date
                                TextView dueDateView = new TextView(projectActivity.this);
                                dueDateView.setText(dueDateText);
                                dueDateView.setPadding(0, 8, 0, 8);

                                TextView priorityView = new TextView(projectActivity.this);
                                priorityView.setText("Priority: " + priority);
                                priorityView.setPadding(8, 4, 8, 4); // padding for a badge-like look

                                // Set background color based on priority level
                                int priorityBackgroundColor;
                                switch (priority.toLowerCase()) {
                                    case "high":
                                        priorityBackgroundColor = ContextCompat.getColor(projectActivity.this, android.R.color.holo_red_light);
                                        break;
                                    case "medium":
                                        priorityBackgroundColor = ContextCompat.getColor(projectActivity.this, android.R.color.holo_orange_light);
                                        break;
                                    case "low":
                                        priorityBackgroundColor = ContextCompat.getColor(projectActivity.this, android.R.color.holo_green_light);
                                        break;
                                    default:
                                        priorityBackgroundColor = ContextCompat.getColor(projectActivity.this, android.R.color.darker_gray);
                                }
                                priorityView.setBackgroundColor(priorityBackgroundColor);
                                priorityView.setTextColor(ContextCompat.getColor(projectActivity.this, android.R.color.white));
                                priorityView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

                                cardLayout.addView(nameView);
                                cardLayout.addView(descriptionView);
                                cardLayout.addView(dueDateView);
                                cardLayout.addView(priorityView);

                                cardView.addView(cardLayout);
                                projectListLayout.addView(cardView);

                                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) cardView.getLayoutParams();
                                layoutParams.setMargins(16, 16, 16, 16);
                                cardView.setLayoutParams(layoutParams);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
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
                                    intent = new Intent(projectActivity.this, adminActivity.class);
                                    break;
                                case "EMPLOYER":
                                    intent = new Intent(projectActivity.this, employerActivity.class);
                                    break;
                                case "EMPLOYEE":
                                    intent = new Intent(projectActivity.this, employeeActivity.class);
                                    break;
                                default:
                                    //Toast.makeText(projectActivity.this, "Unknown user type", Toast.LENGTH_SHORT);
                                    return;
                            }
                            startActivity(intent);

                        } catch (JSONException e) {
                            //Toast.makeText(projectActivity.this,"Error parsing user profile.", Toast.LENGTH_SHORT);
                            Log.e("Profile Error", "JSON parsing error", e);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Toast.makeText(projectActivity.this,"Failed to fetch user profile.", Toast.LENGTH_SHORT);
                        Log.e("Profile Error", error.toString());
                    }
                });

        // Add the profile request to the Volley request queue
        Volley.newRequestQueue(projectActivity.this).add(profileRequest);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            fetchUserProfile(loggedInUsername);
        }
        return super.onOptionsItemSelected(item);
    }
}






