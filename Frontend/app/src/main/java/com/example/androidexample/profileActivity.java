package com.example.androidexample;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class profileActivity extends AppCompatActivity {

    private String loggedInUsername;
    private TextView usernameView;
    private TextView fullNameView;
    private TextView emailView;
    private TextView jobTitleView;
    private TextView userTypeView;
    private TextView payRateView;
    private TextView departmentView;
    private TextView dateOfHireView;
    private EditText usernameEdit;
    private EditText fullNameEdit;
    private EditText emailEdit;
    private EditText payRateEdit;
    private EditText jobTitleEdit;
    private EditText userTypeEdit;
    private EditText departmentEdit;
    private EditText dateOfHireEdit;
    private Button changeInfo;

    private Boolean isEdit = false;


    @SuppressLint("MissingSuperCall")
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Profile");

        // Retrieve username from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        loggedInUsername = sharedPreferences.getString("username", null);

        fetchUserProfile(loggedInUsername);

        changeInfo = findViewById(R.id.changeInfoButton);
        usernameView = findViewById(R.id.username);
        fullNameView = findViewById(R.id.fullName);
        emailView = findViewById(R.id.email);
        payRateView = findViewById(R.id.payrate);
        jobTitleView = findViewById(R.id.jobTitle);
        userTypeView = findViewById(R.id.userType);
        departmentView = findViewById(R.id.department);
        dateOfHireView = findViewById(R.id.datOfHire);
        usernameEdit = findViewById(R.id.usernamEdit);
        fullNameEdit = findViewById(R.id.fullNameEdit);
        emailEdit = findViewById(R.id.emailEdit);
        payRateEdit = findViewById(R.id.payRateEdit);
        jobTitleEdit = findViewById(R.id.jobTitleEdit);
        userTypeEdit = findViewById(R.id.userTypeEdit);
        departmentEdit = findViewById(R.id.departmentEdit);
        dateOfHireEdit = findViewById(R.id.dateOfHireEdit);


        changeInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleEditMode();
            }
        });
    }

    //Get all of users profile information and display specific parts
    private void fetchUserProfile(String username) {
        String url = "http://coms-3090-046.class.las.iastate.edu:8080/api/userprofile/username/" + username;

        // Create a new request
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            // Parse the response and set the values
                            String username = response.optString("username", "N/A");
                            String fullName = response.optString("fullName", "N/A");
                            String email = response.optString("email", "N/A");
                            String jobTitle = response.optString("jobTitle", "N/A");
                            String userType = response.optString("userType", "N/A");
                            String department = response.optString("department", "N/A");
                            String dateOfHire = response.optString("dateOfHire", "N/A");

                            // Set values to TextViews
                            usernameView.setText(username);
                            fullNameView.setText(fullName);
                            emailView.setText(email);
                            jobTitleView.setText(jobTitle);
                            userTypeView.setText(userType);
                            departmentView.setText(department);
                            dateOfHireView.setText(dateOfHire);

                        } catch (Exception e) {
                            //Toast.makeText(profileActivity.this, "Error parsing data", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Toast.makeText(profileActivity.this, "Error fetching profile", Toast.LENGTH_SHORT).show();
                    }
                });

        // Add the request to the RequestQueue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonObjectRequest);
    }

    private void toggleEditMode() {
        isEdit = !isEdit;

        if (isEdit) {
            // Switch to edit mode
            changeInfo.setText("Save Changes");

            // Show EditTexts and hide TextViews
            usernameEdit.setVisibility(View.VISIBLE);
            fullNameEdit.setVisibility(View.VISIBLE);
            emailEdit.setVisibility(View.VISIBLE);
            usernameView.setVisibility(View.GONE);
            fullNameView.setVisibility(View.GONE);
            emailView.setVisibility(View.GONE);

            // Populate EditTexts with current data
            usernameEdit.setText(usernameView.getText().toString());
            fullNameEdit.setText(fullNameView.getText().toString());
            emailEdit.setText(emailView.getText().toString());
        } else {
            // Save changes and switch to view mode
            changeInfo.setText("Save Information");

            // Get values from EditTexts
            String updatedUsername = usernameEdit.getText().toString();
            String updatedFullName = fullNameEdit.getText().toString();
            String updatedEmail = emailEdit.getText().toString();

            // Update TextViews with new data
            usernameView.setText(updatedUsername);
            fullNameView.setText(updatedFullName);
            emailView.setText(updatedEmail);

            // Hide EditTexts and show TextViews
            usernameEdit.setVisibility(View.GONE);
            fullNameEdit.setVisibility(View.GONE);
            emailEdit.setVisibility(View.GONE);
            usernameView.setVisibility(View.VISIBLE);
            fullNameView.setVisibility(View.VISIBLE);
            emailView.setVisibility(View.VISIBLE);

            // Optionally, send updated data to the server here
            saveUpdatedProfile(updatedUsername, updatedFullName, updatedEmail);
        }
    }

    private void saveUpdatedProfile(String username, String fullName, String email) {
        // Implement an API call to save updated profile data
        //Toast.makeText(this, "Changes saved successfully!", Toast.LENGTH_SHORT).show();
    }

    // When on back button check userType to make sure goes back to right page
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
                            Intent intent;
                            switch (userType) {
                                case "ADMIN":
                                    intent = new Intent(profileActivity.this, adminActivity.class);
                                    break;
                                case "EMPLOYER":
                                    intent = new Intent(profileActivity.this, employerActivity.class);
                                    break;
                                case "EMPLOYEE":
                                    intent = new Intent(profileActivity.this, employeeActivity.class);
                                    break;
                                default:
                                    Toast.makeText(profileActivity.this, "Unknown user type", Toast.LENGTH_SHORT);
                                    return;
                            }
                            startActivity(intent);

                        } catch (JSONException e) {
                            //Toast.makeText(profileActivity.this,"Error parsing user profile.", Toast.LENGTH_SHORT);
                            Log.e("Profile Error", "JSON parsing error", e);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Toast.makeText(profileActivity.this,"Failed to fetch user profile.", Toast.LENGTH_SHORT);
                        Log.e("Profile Error", error.toString());
                    }
                });

        // Add the profile request to the Volley request queue
        Volley.newRequestQueue(profileActivity.this).add(profileRequest);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            checkUserType(loggedInUsername);
        }
        return super.onOptionsItemSelected(item);
    }
}
