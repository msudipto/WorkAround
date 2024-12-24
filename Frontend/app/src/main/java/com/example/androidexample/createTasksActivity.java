package com.example.androidexample;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class createTasksActivity extends AppCompatActivity {

    private EditText taskNameText;
    private EditText taskDescriptionText;
    private Spinner priorityLevelSpinner;
    private EditText employeeAssignedText;
    private Button saveButton;

    // Replace with your actual API URL
    private static final String API_URL = "http://coms-3090-046.class.las.iastate.edu:8080/tasks/create";

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.createtasks);

        taskNameText = findViewById(R.id.taskName);
        taskDescriptionText = findViewById(R.id.taskDescription);
        priorityLevelSpinner = findViewById(R.id.priorityLevel);
        employeeAssignedText = findViewById(R.id.employeeAssigned);
        saveButton = findViewById(R.id.saveButton);

        Toolbar toolbar = findViewById(R.id.toolBarCreate);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Create Tasks");

        // Priority level dropdown
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.priority_levels, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        priorityLevelSpinner.setAdapter(adapter);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveTask();
            }
        });
    }

    private void saveTask() {
        // Retrieve user input
        String taskName = taskNameText.getText().toString();
        String taskDescription = taskDescriptionText.getText().toString();
        String priorityLevel = priorityLevelSpinner.getSelectedItem().toString();
        String employeeAssigned = employeeAssignedText.getText().toString();

        // Get employerAssignedTo (username from SharedPreferences)
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String employerAssignedTo = sharedPreferences.getString("username", null);

        if (taskName.isEmpty() || taskDescription.isEmpty() || employeeAssigned.isEmpty() || employerAssignedTo == null) {
            //Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Default values
        String status = "Assigned";  // Default status for new tasks
        int progress = 0;  // Progress starts at 0%

        // Assume the project ID is passed or selected from somewhere.
        // For now, we're assuming it's hardcoded to 101.
        long projectId = 101;  // Replace with the actual project ID

        // Prepare the JSON object
        JSONObject taskData = new JSONObject();
        try {
            taskData.put("name", taskName);
            taskData.put("description", taskDescription);
            taskData.put("status", status);  // Status can be dynamic based on UI
            taskData.put("progress", progress);  // Default progress
            taskData.put("projectId", projectId);  // Ensure projectId is set correctly
            taskData.put("employeeAssignedTo", employeeAssigned);  // Get the employee's username
            taskData.put("employerAssignedTo", employerAssignedTo);  // Get employer from SharedPreferences
        } catch (JSONException e) {
            e.printStackTrace();
            //Toast.makeText(this, "Failed to create task data", Toast.LENGTH_SHORT).show();
            return;
        }

        // Send the request via Volley
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                API_URL,
                taskData,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Success creating task
                        //Toast.makeText(createTasksActivity.this, "Task created successfully!", Toast.LENGTH_SHORT).show();
                        setResult(RESULT_OK);
                        finish();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Error creating task
                        //Toast.makeText(createTasksActivity.this, "Error creating task: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
        );
        Volley.newRequestQueue(this).add(jsonObjectRequest);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            // Redirect based on permissions (Admin, Employer, etc.)
            Intent intent = new Intent(createTasksActivity.this, projectEmployerActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
