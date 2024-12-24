package com.example.androidexample;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class payCheckModifyActivity extends AppCompatActivity {

    private TextView userName;
    private EditText editHoursWorked;
    private EditText editPayRate;
    private EditText editBonusPay;
    private EditText editDeductibles;
    private Button saveChangesButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.paycheckmodify); // Ensure the layout is named properly

        // Initialize the views
        userName = findViewById(R.id.userName);
        editHoursWorked = findViewById(R.id.editHoursWorked);
        editPayRate = findViewById(R.id.editPayRate);
        editBonusPay = findViewById(R.id.editBonusPay);
        editDeductibles = findViewById(R.id.editDeductibles);
        saveChangesButton = findViewById(R.id.saveChangesButton);

        // Get the username from the Intent
        String username = getIntent().getStringExtra("username");
        if (username != null) {
            userName.setText(username); // Display the username
        }

        // Fetch user data to populate the fields
        fetchUserData(username); // Pass the username to fetch the user's data

        // Set up the button click listener to save the changes
        saveChangesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUserData(username); // Pass the username to update
            }
        });
    }

    // Method to fetch user data from the backend and set it in the fields
    private void fetchUserData(String username) {
        String url = "http://your-api-url.com/api/user/paycheck/" + username; // Replace with your API URL

        // Create a new request
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            // Parse the response and set the values
                            String hoursWorked = response.getString("hoursWorked"); // Adjust as needed
                            String payRate = response.getString("payRate"); // Adjust as needed
                            String bonusPay = response.getString("bonusPay"); // Adjust as needed
                            String deductibles = response.getString("deductibles"); // Adjust as needed

                            editHoursWorked.setText(hoursWorked);
                            editPayRate.setText(payRate);
                            editBonusPay.setText(bonusPay);
                            editDeductibles.setText(deductibles);
                        } catch (JSONException e) {
                            e.printStackTrace(); // Handle JSON parsing error
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace(); // Handle error response
                        // Optionally show an error message to the user
                    }
                });

        // Add the request to the RequestQueue
        Volley.newRequestQueue(this).add(jsonObjectRequest);
    }

    // Method to update user data on the backend
    private void updateUserData(String username) {
        String url = "http://your-api-url.com/api/user/paycheck/update"; // Replace with your update API URL

        // Validate inputs
        if (TextUtils.isEmpty(editHoursWorked.getText().toString()) ||
                TextUtils.isEmpty(editPayRate.getText().toString()) ||
                TextUtils.isEmpty(editBonusPay.getText().toString()) ||
                TextUtils.isEmpty(editDeductibles.getText().toString())) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create a JSON object to hold the updated data
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("username", username); // Include username in the request
            jsonBody.put("hoursWorked", editHoursWorked.getText().toString());
            jsonBody.put("payRate", editPayRate.getText().toString());
            jsonBody.put("bonusPay", editBonusPay.getText().toString());
            jsonBody.put("deductibles", editDeductibles.getText().toString());
            // Add any other fields as necessary
        } catch (JSONException e) {
            e.printStackTrace(); // Handle JSON creation error
        }

        // Create a new request for updating data
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Handle successful response
                        Toast.makeText(payCheckModifyActivity.this, "Pay details updated successfully", Toast.LENGTH_SHORT).show();
                        finish(); // Close the activity or navigate back
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace(); // Handle error response
                        Toast.makeText(payCheckModifyActivity.this, "Failed to update pay details", Toast.LENGTH_SHORT).show();
                    }
                });

        // Add the request to the RequestQueue
        Volley.newRequestQueue(this).add(jsonObjectRequest);
    }
}
