package com.example.androidexample;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;
import android.content.SharedPreferences;

public class payCheckOverviewActivity extends AppCompatActivity {

    private LinearLayout payDetailsContainer;
    private Button showHideButton;
    private TextView userName;
    private TextView takeHomePay;
    private TextView grossPay;
    private String loggedInUsername;
    private TextView hoursWorked;
    private TextView pay_Rate;
    private TextView bonus_Pay;
    private TextView deductibles1;
    private ProgressBar takeHomePayProgressBar;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.paycheckoverview);

        // Retrieve username from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        loggedInUsername = sharedPreferences.getString("username", null);


        // Initialize the views
        payDetailsContainer = findViewById(R.id.payDetailsContainer);
        showHideButton = findViewById(R.id.showHideDetailsButton);
        userName = findViewById(R.id.userName);
        takeHomePay = findViewById(R.id.takeHomePay);
        grossPay = findViewById(R.id.grossPay);
        hoursWorked = findViewById(R.id.hoursWorked);
        pay_Rate = findViewById(R.id.payRate);
        bonus_Pay = findViewById(R.id.bonusPay);
        deductibles1 = findViewById(R.id.deductibles);
        takeHomePayProgressBar = findViewById(R.id.firstTakHomePay);


        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        Toolbar toolbar = findViewById(R.id.toolBarPay);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Pay Details");
        }

        // Display the logged-in username
        userName.setText(loggedInUsername);

        // Fetch user data using the logged-in username
        fetchUserData(loggedInUsername);

        // Set up the button click listener to toggle the paycheck details
        showHideButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                togglePayDetails();
            }
        });
    }


    // Method to fetch user data from the backend and set it in the TextViews
    private void fetchUserData(String username) {
        if (username == null || username.isEmpty()) {
            //Toast.makeText(this, "Username not found", Toast.LENGTH_SHORT).show();
            return;
        }

        String url = "https://550fd271-29a4-4a24-8d5b-66afcaadabc0.mock.pstmn.io/payDetails";

        // Create a new request
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            // Parse the response and set the values
                            String name = response.optString("username", "N/A");
                            String takeHome = response.optString("takeHomePay", "0.00");
                            String gross = response.optString("grossPay", "0.00");
                            String hours = response.optString("hoursWorked", "0.00");
                            String payRate = response.optString("payRate", "0.00");
                            String bonusPay = response.optString("bonusPay", "0.00");
                            String deductibles = response.optString("deductibles", "0.00");

                            userName.setText(name);
                            takeHomePay.setText("Take Home Pay: $" + takeHome);
                            grossPay.setText("Gross Pay: $" + gross);
                            hoursWorked.setText("Hours worked: " + hours);
                            pay_Rate.setText("Pay Rate: $" + payRate);
                            bonus_Pay.setText("Bonus Pay: $" + bonusPay);
                            deductibles1.setText("Deductibles: $" + deductibles);

                            //ProgressBar
                            float takeHomeValue = Float.parseFloat(takeHome);

                            takeHomePayProgressBar.setProgress((int) takeHomeValue);


                        } catch (Exception e) {
                            //Toast.makeText(payCheckOverviewActivity.this, "Error parsing data", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Toast.makeText(payCheckOverviewActivity.this, "Error fetching data", Toast.LENGTH_SHORT).show();
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
                                    intent = new Intent(payCheckOverviewActivity.this, adminActivity.class);
                                    break;
                                case "EMPLOYER":
                                    intent = new Intent(payCheckOverviewActivity.this, employerActivity.class);
                                    break;
                                case "EMPLOYEE":
                                    intent = new Intent(payCheckOverviewActivity.this, employeeActivity.class);
                                    break;
                                default:
                                    //Toast.makeText(payCheckOverviewActivity.this, "Unknown user type", Toast.LENGTH_SHORT);
                                    return;
                            }
                            startActivity(intent);

                        } catch (JSONException e) {
                            //Toast.makeText(payCheckOverviewActivity.this,"Error parsing user profile.", Toast.LENGTH_SHORT);
                            Log.e("Profile Error", "JSON parsing error", e);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Toast.makeText(payCheckOverviewActivity.this,"Failed to fetch user profile.", Toast.LENGTH_SHORT);
                        Log.e("Profile Error", error.toString());
                    }
                });

        // Add the profile request to the Volley request queue
        Volley.newRequestQueue(payCheckOverviewActivity.this).add(profileRequest);
    }

    // Method to toggle the visibility of the pay details container
    private void togglePayDetails() {
        if (payDetailsContainer.getVisibility() == View.VISIBLE) {
            payDetailsContainer.setVisibility(View.GONE);
            showHideButton.setText("Show Pay Details");
        } else {
            payDetailsContainer.setVisibility(View.VISIBLE);
            showHideButton.setText("Hide Pay Details");
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            fetchUserProfile(loggedInUsername);
        }
        return super.onOptionsItemSelected(item);
    }
}


