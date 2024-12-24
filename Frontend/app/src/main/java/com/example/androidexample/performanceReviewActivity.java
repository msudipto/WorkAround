package com.example.androidexample;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
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

public class performanceReviewActivity extends AppCompatActivity {

    private Spinner employeeSpinner;
    private ScrollView reviewLayout;
    private String loggedInUsername;
    private String employerName;
    private ArrayList<String> employeeUsernames = new ArrayList<>();

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_performance_review);

        // Set up the toolbar
        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        Toolbar toolbar = findViewById(R.id.toolbarPReview);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Performance Reviews");

        // Retrieve username and employer name from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        loggedInUsername = sharedPreferences.getString("username", null);
        employerName = sharedPreferences.getString("fullName", null);

        // Initialize views
        employeeSpinner = findViewById(R.id.employeeSpinner);
        reviewLayout = findViewById(R.id.reviewLayout);

        // Load employee list
        loadEmployees();
    }

    // Method to load employees from the API
    private void loadEmployees() {
        // URL to fetch the list of employees
        String url = "http://coms-3090-046.class.las.iastate.edu:8080/api/userprofile/all";

        // Make a GET request to fetch the list of employees
        JsonArrayRequest employeeRequest = new JsonArrayRequest(Request.Method.GET, url,
                null, // No body needed for GET request
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            // Clear the previous list of employee usernames
                            employeeUsernames.clear();

                            // Parse the response and add employee usernames to the list
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject employee = response.getJSONObject(i);
                                String username = employee.getString("username");
                                employeeUsernames.add(username);
                            }

                            // Populate the Spinner with employee usernames
                            ArrayAdapter<String> adapter = new ArrayAdapter<>(performanceReviewActivity.this,
                                    android.R.layout.simple_spinner_item, employeeUsernames);
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            employeeSpinner.setAdapter(adapter);
                        } catch (JSONException e) {
                            //Toast.makeText(performanceReviewActivity.this, "Error parsing employee list.", Toast.LENGTH_SHORT).show();
                            Log.e("Employee List Error", "JSON parsing error", e);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Toast.makeText(performanceReviewActivity.this, "Failed to fetch employee list.", Toast.LENGTH_SHORT).show();
                        Log.e("Employee List Error", error.toString());
                    }
                });

        // Add the request to the Volley request queue
        Volley.newRequestQueue(performanceReviewActivity.this).add(employeeRequest);
    }

    // This method will display the reviews for the current employee
    private void displayEmployeeReviews(String username) {
        // API endpoint URL
        String url = "http://coms-3090-046.class.las.iastate.edu:8080/api/performance-reviews/all";

        // Create a GET request to fetch all reviews
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            boolean hasReviews = false;

                            // Loop through the response and filter reviews by username
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject review = response.getJSONObject(i);

                                // Check if the review is for the logged-in user
                                if (review.getString("username").equals(username)) {
                                    hasReviews = true;

                                    // Extract review details
                                    String reviewer = review.getString("reviewer");
                                    String standards = review.getString("standards");
                                    String description = review.getString("description");

                                    // Add the review to the layout using CardView
                                    addReviewCard(reviewer, standards, description);
                                }
                            }

                            // If no reviews are found, display a "no reviews" message
                            if (!hasReviews) {
                                TextView noReviewsMessage = new TextView(performanceReviewActivity.this);
                                noReviewsMessage.setText("No performance reviews available at the moment.");
                                noReviewsMessage.setTextSize(16);
                                noReviewsMessage.setPadding(16, 8, 16, 8);
                                reviewLayout.addView(noReviewsMessage);
                            }


                        } catch (JSONException e) {
                            Log.e("Error", "Failed to parse reviews", e);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Toast.makeText(performanceReviewActivity.this, "Failed to load reviews", Toast.LENGTH_SHORT).show();
                        Log.e("Error", "API Error", error);
                    }
                });

        // Add the request to the Volley queue
        Volley.newRequestQueue(this).add(request);
    }

    // Helper method to add a review as a CardView
    @SuppressLint("SetTextI18n")
    private void addReviewCard(String reviewer, String standards, String description) {
        // Create a CardView
        androidx.cardview.widget.CardView cardView = new androidx.cardview.widget.CardView(this);

        // Set CardView layout parameters
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(16, 16, 16, 16); // Set margin
        cardView.setLayoutParams(params);
        cardView.setRadius(12);
        cardView.setCardElevation(6);

        // Create a LinearLayout for CardView content
        LinearLayout cardContent = new LinearLayout(this);
        cardContent.setOrientation(LinearLayout.VERTICAL);
        cardContent.setPadding(16, 16, 16, 16);

        // Add TextViews for reviewer, standards, and description
        TextView reviewerText = new TextView(this);
        reviewerText.setText("Reviewer: " + reviewer);
        reviewerText.setTextSize(16);
        reviewerText.setPadding(0, 0, 0, 8);

        TextView standardsText = new TextView(this);
        standardsText.setText("Standards: " + standards);
        standardsText.setTextSize(16);
        standardsText.setPadding(0, 0, 0, 8);

        TextView descriptionText = new TextView(this);
        descriptionText.setText("Description: " + description);
        descriptionText.setTextSize(16);
        descriptionText.setPadding(0, 0, 0, 8);

        // Add TextViews to the card content
        cardContent.addView(reviewerText);
        cardContent.addView(standardsText);
        cardContent.addView(descriptionText);

        // Add content to CardView
        cardView.addView(cardContent);

        // Add CardView to the main layout
        reviewLayout.addView(cardView);
    }


    // When on back button, check userType to make sure it goes back to the right page
    private void checkUserType(final String username) {
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
                            // (Handle redirect based on the userType logic here)
                        } catch (JSONException e) {
                            //Toast.makeText(performanceReviewActivity.this, "Error parsing user profile.", Toast.LENGTH_SHORT).show();
                            Log.e("Profile Error", "JSON parsing error", e);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Toast.makeText(performanceReviewActivity.this, "Failed to fetch user profile.", Toast.LENGTH_SHORT).show();
                        Log.e("Profile Error", error.toString());
                    }
                });

        // Add the profile request to the Volley request queue
        Volley.newRequestQueue(performanceReviewActivity.this).add(profileRequest);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            checkUserType(loggedInUsername);
        }
        return super.onOptionsItemSelected(item);
    }
}
