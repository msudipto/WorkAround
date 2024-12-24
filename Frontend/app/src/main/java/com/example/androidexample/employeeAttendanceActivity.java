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
import android.view.View;
import android.widget.ImageButton;
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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class employeeAttendanceActivity extends AppCompatActivity {

    // UI components
    private ScrollView currentUsers;
    private ScrollView previousUsers;
    private LinearLayout currentUsersLayout;
    private LinearLayout previousUsersLayout;
    private String loggedInUsername;

    @SuppressLint("CutPasteId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.employeeattendance);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Employee Attendance");

        // Retrieve username from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        loggedInUsername = sharedPreferences.getString("username", null);


        currentUsers = findViewById(R.id.currentUsersScrollView);
        previousUsers = findViewById(R.id.previousUsersScrollView);
        currentUsersLayout = findViewById(R.id.currentUsersScrollView).findViewById(R.id.currentUserLayout);
        previousUsersLayout = findViewById(R.id.previousUsersScrollView).findViewById(R.id.previousUserLayout);

        // Populate UI dynamically
        populateCurrentUsers();
        populatePreviousUsers();
    }

    /**
     * Populates the list of current users working.
     */
    private void populateCurrentUsers() {
        String url = "https://33a10a78-e275-47f1-aa89-ae3a5cfad850.mock.pstmn.io/usersWorked"; //Mock server URL

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        JSONArray currentUsers = response.getJSONArray("data");
                        for (int i = 0; i < currentUsers.length(); i++) {
                            JSONObject user = currentUsers.getJSONObject(i);
                            String name = user.getString("name");
                            String clockInTime = user.getString("clockInTime");



                            // Create a CardView to wrap each user's info
                            CardView cardView = new CardView(this);
                            cardView.setCardBackgroundColor(getResources().getColor(android.R.color.white));
                            cardView.setRadius(10);
                            cardView.setCardElevation(5);

                            // Set layout params for the CardView and add margin for spacing between cards
                            LinearLayout.LayoutParams cardLayoutParams = new LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.MATCH_PARENT,
                                    LinearLayout.LayoutParams.WRAP_CONTENT
                            );
                            cardLayoutParams.setMargins(16, 8, 16, 8);
                            cardView.setLayoutParams(cardLayoutParams);

                            // Create a TextView inside the CardView
                            TextView userView = new TextView(this);
                            userView.setText(name + " - Clocked In: " + clockInTime);
                            userView.setTextSize(16);
                            userView.setPadding(16, 16, 16, 16);
                            userView.setLayoutParams(new CardView.LayoutParams(
                                    LinearLayout.LayoutParams.MATCH_PARENT,
                                    LinearLayout.LayoutParams.WRAP_CONTENT
                            ));

                            // Add the TextView to the CardView
                            cardView.addView(userView);

                            // Add the CardView to the layout
                            currentUsersLayout.addView(cardView);
                        }
                    } catch (Exception e) {
                        //Toast.makeText(this, "Error parsing current users data", Toast.LENGTH_SHORT).show();
                        Log.e("CurrentUsersError", e.toString());
                    }
                },
                error -> {
                    //Toast.makeText(this, "Failed to fetch current users", Toast.LENGTH_SHORT).show();
                    Log.e("CurrentUsersError", error.toString());
                });

        // Add request to Volley queue
        Volley.newRequestQueue(this).add(request);
    }


    /**
     * Populates the list of users who worked today.
     */
    private void populatePreviousUsers() {
        String url = "https://72b97328-1352-43d0-9e69-8c9ad2eb9414.mock.pstmn.io/currentUsersToday"; // Change with URL

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        JSONArray previousUsers = response.getJSONArray("data");
                        for (int i = 0; i < previousUsers.length(); i++) {
                            JSONObject user = previousUsers.getJSONObject(i);
                            String name = user.getString("name");
                            String clockInTime = user.getString("clockInTime");
                            String clockOutTime = user.getString("clockOutTime");


                            // Create a CardView to wrap each user's info
                            CardView cardView = new CardView(this);
                            cardView.setCardBackgroundColor(getResources().getColor(android.R.color.white));
                            cardView.setRadius(10);
                            cardView.setCardElevation(5);

                            // Set layout params for the CardView
                            LinearLayout.LayoutParams cardLayoutParams = new LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.MATCH_PARENT,
                                    LinearLayout.LayoutParams.WRAP_CONTENT
                            );
                            cardLayoutParams.setMargins(16, 8, 16, 8);
                            cardView.setLayoutParams(cardLayoutParams);

                            // Create a TextView to display the user info
                            TextView userView = new TextView(this);
                            userView.setText(name + "\nClocked In: " + clockInTime + ", Clocked Out: " + clockOutTime);
                            userView.setTextSize(16);
                            userView.setPadding(16, 16, 16, 16);
                            userView.setLayoutParams(new CardView.LayoutParams(
                                    LinearLayout.LayoutParams.MATCH_PARENT,
                                    LinearLayout.LayoutParams.WRAP_CONTENT
                            ));

                            // Add the TextView to the CardView
                            cardView.addView(userView);

                            // Add the CardView to the layout
                            previousUsersLayout.addView(cardView);
                        }
                    } catch (JSONException e) {
                        //Toast.makeText(this, "Error parsing previous users data", Toast.LENGTH_SHORT).show();
                        Log.e("PreviousUsersError", e.toString());
                    }
                },
                error -> {
                    //Toast.makeText(this, "Failed to fetch previous users", Toast.LENGTH_SHORT).show();
                    Log.e("PreviousUsersError", error.toString());
                });

        // Add request to Volley queue
        Volley.newRequestQueue(this).add(request);
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
                                    intent = new Intent(employeeAttendanceActivity.this, adminActivity.class);
                                    break;
                                case "EMPLOYER":
                                    intent = new Intent(employeeAttendanceActivity.this, employerActivity.class);
                                    break;
                                case "EMPLOYEE":
                                    intent = new Intent(employeeAttendanceActivity.this, employeeActivity.class);
                                    break;
                                default:
                                    //Toast.makeText(employeeAttendanceActivity.this, "Unknown user type", Toast.LENGTH_SHORT);
                                    return;
                            }
                            startActivity(intent);

                        } catch (JSONException e) {
                            //Toast.makeText(employeeAttendanceActivity.this,"Error parsing user profile.", Toast.LENGTH_SHORT);
                            Log.e("Profile Error", "JSON parsing error", e);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Toast.makeText(employeeAttendanceActivity.this,"Failed to fetch user profile.", Toast.LENGTH_SHORT);
                        Log.e("Profile Error", error.toString());
                    }
                });

        // Add the profile request to the Volley request queue
        Volley.newRequestQueue(employeeAttendanceActivity.this).add(profileRequest);
    }
}
