package com.example.androidexample;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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
import java.util.List;

public class GiveReviewActivity extends AppCompatActivity {

    private Spinner employeeSpinner;
    private Spinner standardSpinner; // New spinner for standards
    private EditText reviewEditText;
    private Button submitReviewButton;
    private String loggedInUsername;
    private String selectedEmployeeName;
    private int selectedEmployeeId;
    private String selectedStandard; // To store the selected standard

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_give_review);

        // Set up the toolbar
        Toolbar toolbar = findViewById(R.id.toolBarGiveReview);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Give Review");

        // Retrieve username from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        loggedInUsername = sharedPreferences.getString("username", null);

        // Initialize views
        employeeSpinner = findViewById(R.id.employeeSpinner);
        standardSpinner = findViewById(R.id.standardSpinner); // Initialize standard spinner
        reviewEditText = findViewById(R.id.reviewEditText);
        submitReviewButton = findViewById(R.id.submitReviewButton);

        // Load employee list (simulating from an API)
        loadEmployees();

        // Load standard spinner
        setupStandardSpinner();

        // Set up the submit button click listener
        submitReviewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitReview();
            }
        });
    }

    private void loadEmployees() {
        // Fetch the list of user profiles (API call)
        String url = "http://coms-3090-046.class.las.iastate.edu:8080/api/userprofile/all";
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            // Temporary list to hold usernames
                            List<String> userNames = new ArrayList<>();

                            // Populate the list, excluding the logged-in user
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject user = response.getJSONObject(i);
                                String username = user.getString("username");
                                if (!username.equals(loggedInUsername)) {
                                    userNames.add(username);
                                }
                            }

                            // Convert the list to an array
                            String[] userNameArray = userNames.toArray(new String[0]);

                            // Set up the spinner adapter
                            ArrayAdapter<String> adapter = new ArrayAdapter<>(GiveReviewActivity.this,
                                    android.R.layout.simple_spinner_item, userNameArray);
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            employeeSpinner.setAdapter(adapter);

                            // Set up the spinner item selection listener
                            employeeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                                    try {
                                        // Find the corresponding user from the original response
                                        for (int i = 0; i < response.length(); i++) {
                                            JSONObject user = response.getJSONObject(i);
                                            if (user.getString("username").equals(userNameArray[position])) {
                                                selectedEmployeeName = user.getString("username");
                                                selectedEmployeeId = user.getInt("userId");
                                                break;
                                            }
                                        }
                                    } catch (JSONException e) {
                                        Log.e("Error", "Failed to parse user data", e);
                                    }
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parentView) {
                                    // Do nothing
                                }
                            });
                        } catch (JSONException e) {
                            Log.e("Error", "Failed to parse user data", e);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Toast.makeText(GiveReviewActivity.this, "Failed to load user data", Toast.LENGTH_SHORT).show();
                    }
                });

        // Add the request to the Volley queue
        Volley.newRequestQueue(GiveReviewActivity.this).add(request);
    }

    private void setupStandardSpinner() {
        // Options for the standards spinner
        String[] standards = {"Below Standards", "Meets Standards", "Above Standards"};

        // Set up the spinner adapter
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, standards);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        standardSpinner.setAdapter(adapter);

        // Set up the spinner item selection listener
        standardSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedStandard = standards[position]; // Store the selected standard
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedStandard = null; // Reset if nothing is selected
            }
        });
    }

    // Method to submit the review
    private void submitReview() {
        // Retrieve and validate review text
        String reviewText = reviewEditText.getText().toString().trim();
        if (reviewText.isEmpty()) {
            //Toast.makeText(this, "Please enter a review description", Toast.LENGTH_SHORT).show();
            return;
        }

        // Ensure a standard is selected
        if (selectedStandard == null) {
            //Toast.makeText(this, "Please select a standard", Toast.LENGTH_SHORT).show();
            return;
        }

        // Prepare the review data JSON object
        JSONObject reviewData = new JSONObject();
        try {
            // Include required information in the request payload
            reviewData.put("reviewer", loggedInUsername);
            reviewData.put("employeeId", selectedEmployeeId);
            reviewData.put("standards", selectedStandard);
            reviewData.put("description", reviewText);

            // API endpoint URL for submitting the review
            String url = "http://coms-3090-046.class.las.iastate.edu:8080/api/performance-reviews/create";

            // Create a POST request to send the review data
            JsonObjectRequest submitRequest = new JsonObjectRequest(Request.Method.POST, url, reviewData,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            // Display a success message and close the activity
                            //Toast.makeText(GiveReviewActivity.this, "Review submitted successfully", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // Handle errors, display an error message
                            //Toast.makeText(GiveReviewActivity.this, "Failed to submit review", Toast.LENGTH_SHORT).show();
                            Log.e("SubmitReviewError", "Error occurred while submitting the review", error);
                        }
                    });

            // Add the request to the Volley request queue
            Volley.newRequestQueue(GiveReviewActivity.this).add(submitRequest);

        } catch (JSONException e) {
            // Handle JSON construction errors
            Log.e("JSONError", "Failed to prepare review data", e);
            //Toast.makeText(this, "Error preparing review data", Toast.LENGTH_SHORT).show();
        }
    }

}

