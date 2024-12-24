package com.example.androidexample;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class employeeStatusActivity extends AppCompatActivity {

    private ScrollView scrollViewAvailability;
    private ScrollView scrollViewTimeOff;
    private LinearLayout availableLayout;
    private LinearLayout requestOffLayout;
    private String loggedInUsername;
    private static final String URL = "https://acf37832-c33a-49c7-befe-31b02a15f1b6.mock.pstmn.io/userStatus"; // Mock URL

    @SuppressLint({"MissingInflatedId", "CutPasteId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.employeestatus);

        // Setup Toolbar
        Toolbar toolbar = findViewById(R.id.toolbarStatus);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Employee Status");

        // Retrieve username from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        loggedInUsername = sharedPreferences.getString("username", null);

        // Initialize UI components
        scrollViewAvailability = findViewById(R.id.availabilityScroll);
        scrollViewTimeOff = findViewById(R.id.requestTimeScroll);
        availableLayout = findViewById(R.id.availabilityScroll).findViewById(R.id.availableLayout);
        requestOffLayout = findViewById(R.id.requestTimeScroll).findViewById(R.id.requestTimeLayout);

        // Load employee status
        loadEmployeeStatus();
    }

    private void loadEmployeeStatus() {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                URL,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            // Extract the "data" array from the response
                            JSONArray dataArray = response.getJSONArray("data");

                            // Clear existing views to avoid duplication
                            availableLayout.removeAllViews();
                            requestOffLayout.removeAllViews();

                            // Iterate through the JSON array
                            for (int i = 0; i < dataArray.length(); i++) {
                                JSONObject employee = dataArray.getJSONObject(i);

                                // Extract employee details
                                String name = employee.getString("name");
                                String status = employee.getString("status"); // Available, On Leave, or Time Off Requested
                                String details = employee.optString("details", ""); // Optional additional details

                                // Create a CardView to wrap each employee's info
                                CardView cardView = new CardView(employeeStatusActivity.this);
                                cardView.setCardBackgroundColor(getResources().getColor(android.R.color.white));
                                cardView.setRadius(10);
                                cardView.setCardElevation(5);

                                // Set layout params for the CardView and add margin for spacing
                                LinearLayout.LayoutParams cardLayoutParams = new LinearLayout.LayoutParams(
                                        LinearLayout.LayoutParams.MATCH_PARENT,
                                        LinearLayout.LayoutParams.WRAP_CONTENT
                                );
                                cardLayoutParams.setMargins(16, 8, 16, 8); // Adjusted margins
                                cardView.setLayoutParams(cardLayoutParams);

                                // Create a LinearLayout to contain both TextViews inside the CardView
                                LinearLayout cardContentLayout = new LinearLayout(employeeStatusActivity.this);
                                cardContentLayout.setOrientation(LinearLayout.VERTICAL);
                                cardContentLayout.setPadding(16, 16, 16, 16); // Added padding

                                // Create a TextView for the employee's name and status
                                TextView employeeStatus = new TextView(employeeStatusActivity.this);
                                employeeStatus.setText(name + ": " + status);
                                employeeStatus.setTextSize(16);
                                employeeStatus.setPadding(0, 0, 0, 8); // Padding between name/status and details

                                // Add the name/status TextView to the LinearLayout
                                cardContentLayout.addView(employeeStatus);

                                // If additional details are provided, add another TextView
                                if (!details.isEmpty() && (status.equalsIgnoreCase("On Leave") || status.equalsIgnoreCase("Time Off Requested"))) {
                                    TextView detailView = new TextView(employeeStatusActivity.this);
                                    detailView.setText("Details: " + details);
                                    detailView.setTextSize(14);
                                    detailView.setPadding(0, 8, 0, 0); // Padding for details TextView
                                    cardContentLayout.addView(detailView);
                                }

                                // Add the LinearLayout to the CardView
                                cardView.addView(cardContentLayout);

                                // Add the CardView to the appropriate layout
                                if (status.equalsIgnoreCase("Available")) {
                                    availableLayout.addView(cardView);
                                } else {
                                    requestOffLayout.addView(cardView);
                                }

                            }
                        } catch (JSONException e) {
                            Log.e("EmployeeStatus", "JSON Parsing error: " + e.getMessage());
                            //Toast.makeText(employeeStatusActivity.this, "Error parsing data", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("EmployeeStatus", "Volley error: " + error.getMessage());
                        //Toast.makeText(employeeStatusActivity.this, "Error fetching data", Toast.LENGTH_SHORT).show();
                    }
                });

        Volley.newRequestQueue(this).add(jsonObjectRequest);
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
                                intent = new Intent(employeeStatusActivity.this, adminActivity.class);
                                break;
                            case "EMPLOYER":
                                intent = new Intent(employeeStatusActivity.this, employerActivity.class);
                                break;
                            case "EMPLOYEE":
                                intent = new Intent(employeeStatusActivity.this, employeeActivity.class);
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


