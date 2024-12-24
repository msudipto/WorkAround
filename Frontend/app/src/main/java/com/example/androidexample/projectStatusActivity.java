package com.example.androidexample;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class projectStatusActivity extends AppCompatActivity {

    private LinearLayout linearLayoutProjects;
    private String loggedInUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.projectstatus);

        // Retrieve username from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        loggedInUsername = sharedPreferences.getString("username", null);

        // Initialize the container for dynamic CardView generation
        linearLayoutProjects = findViewById(R.id.linearLayoutProjects);

        // Setup Toolbar
        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        Toolbar toolbar = findViewById(R.id.toolbarPStatus);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Project Status");

        // Fetch project data from backend and generate UI
        fetchProjectData();
    }

    private void fetchProjectData() {
        String url = "https://edb7e976-41f4-48a0-b7c9-a3977d49ba22.mock.pstmn.io/projectStatus"; //MOCK URL

        // Volley JsonArrayRequest to fetch project data
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            // Loop through JSON array and create CardViews for each project
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject project = response.getJSONObject(i);

                                // Extract project details
                                String projectName = project.getString("name");
                                String dueDate = project.getString("dueDate");
                                int totalTasks = project.getInt("totalTasks");
                                int completedTasks = project.getInt("completedTasks");

                                // Dynamically add a CardView for this project
                                addProjectCard(projectName, dueDate, totalTasks, completedTasks);
                            }
                        } catch (JSONException e) {
                            Log.e("JSONError", "Error parsing project data: " + e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("VolleyError", "Error fetching project data: " + error.getMessage());
                    }
                });

        // Add the request to the Volley request queue
        Volley.newRequestQueue(this).add(jsonArrayRequest);
    }

    private void addProjectCard(String projectName, String dueDate, int totalTasks, int completedTasks) {
        // Create a new CardView
        CardView cardView = new CardView(this);
        cardView.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        ));
        cardView.setCardElevation(8);
        cardView.setRadius(12);
        cardView.setUseCompatPadding(true);
        cardView.setContentPadding(16, 16, 16, 16);
        cardView.setCardBackgroundColor(Color.WHITE);

        // Create a LinearLayout inside the CardView
        LinearLayout cardLayout = new LinearLayout(this);
        cardLayout.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        ));
        cardLayout.setOrientation(LinearLayout.VERTICAL);

        // Add TextView for project name
        TextView textProjectName = new TextView(this);
        textProjectName.setText("Project: " + projectName);
        textProjectName.setTextSize(18);
        textProjectName.setTextColor(Color.BLACK);
        textProjectName.setPadding(0, 0, 0, 8);

        // Add TextView for due date
        TextView textDueDate = new TextView(this);
        textDueDate.setText("Due Date: " + dueDate);
        textDueDate.setTextSize(16);
        textDueDate.setTextColor(Color.DKGRAY);
        textDueDate.setPadding(0, 0, 0, 16);

        // Add a ProgressBar for task progress
        ProgressBar progressBar = new ProgressBar(this, null, android.R.attr.progressBarStyleHorizontal);
        progressBar.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                24
        ));
        progressBar.setMax(totalTasks);
        progressBar.setProgress(completedTasks);

        // Add all views to the card layout
        cardLayout.addView(textProjectName);
        cardLayout.addView(textDueDate);
        cardLayout.addView(progressBar);

        // Add the card layout to the CardView
        cardView.addView(cardLayout);

        // Check if all tasks are completed
        if (completedTasks == totalTasks) {
            // Add CardView to the Completed Projects LinearLayout
            LinearLayout completedProjectsLayout = findViewById(R.id.linearLayoutCompletedProjects);
            completedProjectsLayout.addView(cardView);
        } else {
            // Add CardView to the Ongoing Projects LinearLayout
            linearLayoutProjects.addView(cardView);
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            fetchUserProfile(loggedInUsername);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void fetchUserProfile(String username) {
        String url = "http://coms-3090-046.class.las.iastate.edu:8080/api/userprofile/username/" + username;

        JsonObjectRequest profileRequest = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                response -> {
                    try {
                        int userId = response.getInt("userId");
                        String fullName = response.getString("fullName");
                        String userType = response.getString("userType");

                        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putInt("userId", userId);
                        editor.putString("username", username);
                        editor.putString("userType", userType);
                        editor.putString("fullName", fullName);
                        editor.apply();

                        Intent intent;
                        switch (userType) {
                            case "ADMIN":
                                intent = new Intent(projectStatusActivity.this, adminActivity.class);
                                break;
                            case "EMPLOYER":
                                intent = new Intent(projectStatusActivity.this, employerActivity.class);
                                break;
                            case "EMPLOYEE":
                                intent = new Intent(projectStatusActivity.this, employeeActivity.class);
                                break;
                            default:
                                //Toast.makeText(this, "Unknown user type", Toast.LENGTH_SHORT).show();
                                return;
                        }
                        startActivity(intent);

                    } catch (JSONException e) {
                        //Toast.makeText(this, "Error parsing user profile.", Toast.LENGTH_SHORT).show();
                        Log.e("Profile Error", "JSON parsing error", e);
                    }
                },
                error -> {
                    //Toast.makeText(this, "Failed to fetch user profile.", Toast.LENGTH_SHORT).show();
                    Log.e("Profile Error", error.toString());
                });

        Volley.newRequestQueue(this).add(profileRequest);
    }
}

