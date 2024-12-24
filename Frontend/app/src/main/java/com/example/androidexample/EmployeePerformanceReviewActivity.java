package com.example.androidexample;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
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

public class EmployeePerformanceReviewActivity extends AppCompatActivity {

    private TextView performanceReviewTitle;
    private LinearLayout reviewLayout;
    private Button addReviewButton;
    private String loggedInUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.employee_perfomance_review);

        // Initialize the UI components
        reviewLayout = findViewById(R.id.reviewLayout);
        addReviewButton = findViewById(R.id.addReviewButton);

        // Setup Toolbar
        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        Toolbar toolbar = findViewById(R.id.toolbarReviews);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Performance Reviews");

        // Get the username from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        loggedInUsername = sharedPreferences.getString("username", null);

        if (loggedInUsername != null) {
            displayEmployeeReviews(loggedInUsername);
        }



        addReviewButton.setOnClickListener(v -> {
        });
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
                                    String standards = review.getString("standards");
                                    String description = review.getString("description");

                                    // Add the review to the layout using CardView
                                    addReviewCard(standards, description);
                                }
                            }

                            // If no reviews are found, display a "no reviews" message
                            if (!hasReviews) {
                                TextView noReviewsMessage = new TextView(EmployeePerformanceReviewActivity.this);
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
                        //Toast.makeText(EmployeePerformanceReviewActivity.this, "Failed to load reviews", Toast.LENGTH_SHORT).show();
                        Log.e("Error", "API Error", error);
                    }
                });

        // Add the request to the Volley queue
        Volley.newRequestQueue(this).add(request);
    }

    // Helper method to add a review as a CardView
    @SuppressLint("SetTextI18n")
    private void addReviewCard(String standards, String description) {
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

        TextView standardsText = new TextView(this);
        standardsText.setText("Standards: " + standards);
        standardsText.setTextSize(16);
        standardsText.setPadding(0, 0, 0, 8);

        TextView descriptionText = new TextView(this);
        descriptionText.setText("Description: " + description);
        descriptionText.setTextSize(16);
        descriptionText.setPadding(0, 0, 0, 8);

        // Add TextViews to the card content
        cardContent.addView(standardsText);
        cardContent.addView(descriptionText);

        // Add content to CardView
        cardView.addView(cardContent);

        // Add CardView to the main layout
        reviewLayout.addView(cardView);
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
                                intent = new Intent(EmployeePerformanceReviewActivity.this, adminActivity.class);
                                break;
                            case "EMPLOYER":
                                intent = new Intent(EmployeePerformanceReviewActivity.this, employerActivity.class);
                                break;
                            case "EMPLOYEE":
                                intent = new Intent(EmployeePerformanceReviewActivity.this, employeeActivity.class);
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
