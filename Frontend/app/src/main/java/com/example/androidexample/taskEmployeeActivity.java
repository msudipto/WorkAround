package com.example.androidexample;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * This class shows an employee what tasks they are assigned to, and lets the employee modify them
 */
public class taskEmployeeActivity extends AppCompatActivity {

    private LinearLayout taskListLayout;

    /**
     * Create the class and map all buttons to the XML
     * @param savedInstanceState
     */
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.taskemployee);

        taskListLayout = findViewById(R.id.tasklayoutEmployee);

        Toolbar toolbar = findViewById(R.id.toolbarEmployee);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Tasks");

        // Fetch all tasks from the backend and display them
        fetchAllTasks();
    }

    /**
     * GET all tasks assigned to the currently logged in user
     */
    private void fetchAllTasks() {
        String url = "https://55f1aed6-2955-4d90-a1cc-fe885ca7571f.mock.pstmn.io/tasks";

        // Use JsonObjectRequest to handle JSONObject response
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            // Get the 'tasks' array from the response
                            JSONArray tasksArray = response.getJSONArray("tasks");

                            // Clear the layout to avoid duplicating task cards
                            taskListLayout.removeAllViews();

                            // Loop through each task in the 'tasks' array
                            for (int i = 0; i < tasksArray.length(); i++) {
                                JSONObject taskObject = tasksArray.getJSONObject(i);

                                // Extract fields including the task ID
                                final long taskId = taskObject.optLong("id"); // Get the task ID
                                String taskName = taskObject.optString("name", "Unnamed Task");
                                String taskDescription = taskObject.optString("description", "No description available.");
                                String status = taskObject.optString("status", "No status available.");
                                String employeeAssignedTo = taskObject.optString("employeeAssignedTo", "");
                                String employerAssignedTo = taskObject.optString("employerAssignedTo", "");
                                String createdAt = taskObject.optString("createdAt", "");
                                String updatedAt = taskObject.optString("updatedAt", "");
                                int progress = taskObject.optInt("progress", 0);



                                // Create a new CardView for this task
                                CardView taskCard = new CardView(taskEmployeeActivity.this);
                                LinearLayout.LayoutParams cardLayoutParams = new LinearLayout.LayoutParams(
                                        LinearLayout.LayoutParams.MATCH_PARENT,
                                        LinearLayout.LayoutParams.WRAP_CONTENT
                                );
                                cardLayoutParams.setMargins(16, 16, 16, 16);
                                taskCard.setLayoutParams(cardLayoutParams);
                                taskCard.setCardBackgroundColor(getResources().getColor(R.color.cardBackground));
                                taskCard.setRadius(16);
                                taskCard.setCardElevation(8);

                                // Set the task ID as a tag to retrieve later
                                taskCard.setTag(taskId); // Set the task ID on the card as a tag

                                // Create the layout for the CardView content
                                LinearLayout taskLayout = new LinearLayout(taskEmployeeActivity.this);
                                taskLayout.setOrientation(LinearLayout.VERTICAL);
                                taskLayout.setPadding(24, 24, 24, 24);

                                // Add Task Name
                                TextView taskNameView = new TextView(taskEmployeeActivity.this);
                                taskNameView.setText("Task: " + taskName);
                                taskNameView.setTextSize(18);
                                taskNameView.setTextColor(getResources().getColor(R.color.black));
                                taskLayout.addView(taskNameView);

                                // Add Task Description
                                TextView taskDescriptionView = new TextView(taskEmployeeActivity.this);
                                taskDescriptionView.setText("Description: " + taskDescription);
                                taskDescriptionView.setTextColor(getResources().getColor(R.color.black));
                                taskDescriptionView.setPadding(0, 8, 0, 8);
                                taskLayout.addView(taskDescriptionView);

                                // Status Layout
                                FrameLayout statusLayout = new FrameLayout(taskEmployeeActivity.this);
                                LinearLayout.LayoutParams statusLayoutParams = new LinearLayout.LayoutParams(
                                        LinearLayout.LayoutParams.MATCH_PARENT,
                                        LinearLayout.LayoutParams.WRAP_CONTENT
                                );
                                statusLayoutParams.setMargins(0, 16, 0, 0);
                                statusLayout.setLayoutParams(statusLayoutParams);

                                // Status Box Color
                                TextView colorBox = new TextView(taskEmployeeActivity.this);
                                LinearLayout.LayoutParams colorBoxLayoutParams = new LinearLayout.LayoutParams(
                                        LinearLayout.LayoutParams.MATCH_PARENT,
                                        LinearLayout.LayoutParams.WRAP_CONTENT
                                );
                                colorBox.setLayoutParams(colorBoxLayoutParams);
                                colorBox.setPadding(0, 40, 0, 40);

                                // Set background color based on status
                                if (status.equals("Completed")) {
                                    colorBox.setBackgroundColor(getResources().getColor(R.color.green));
                                } else if (status.equals("In Progress")) {
                                    colorBox.setBackgroundColor(getResources().getColor(R.color.yellow));
                                } else {
                                    colorBox.setBackgroundColor(getResources().getColor(R.color.red));
                                }

                                // Task Status Text
                                TextView taskStatusView = new TextView(taskEmployeeActivity.this);
                                taskStatusView.setText(status);
                                taskStatusView.setTextColor(getResources().getColor(R.color.white));
                                taskStatusView.setTextSize(16);
                                taskStatusView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                                FrameLayout.LayoutParams taskStatusViewParams = new FrameLayout.LayoutParams(
                                        FrameLayout.LayoutParams.WRAP_CONTENT,
                                        FrameLayout.LayoutParams.WRAP_CONTENT
                                );
                                taskStatusViewParams.gravity = android.view.Gravity.CENTER;
                                taskStatusView.setLayoutParams(taskStatusViewParams);

                                // Add status elements to layout
                                statusLayout.addView(colorBox);
                                statusLayout.addView(taskStatusView);
                                taskLayout.addView(statusLayout);

                                // Add the task layout to the card
                                taskCard.addView(taskLayout);

                                // Add the card to the main layout
                                taskListLayout.addView(taskCard);

                                // Set the click listener to update status when clicked
                                taskCard.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        // Get the task ID from the card tag
                                        long taskId = (long) v.getTag();
                                        String currentStatus = taskStatusView.getText().toString();

                                        // Determine next status in cycle: "Assigned" -> "In Progress" -> "Completed" -> "Assigned"
                                        String nextStatus = getNextStatus(currentStatus);

                                        // Call the function to update the task status
                                        updateTaskStatusOnServer(taskId, nextStatus, taskStatusView, colorBox, taskName, taskDescription, progress, createdAt, updatedAt, employeeAssignedTo, employerAssignedTo);
                                    }
                                });
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            //Toast.makeText(taskEmployeeActivity.this, "Error fetching tasks", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley Error", "Error fetching tasks: " + error.getMessage());
                        //Toast.makeText(taskEmployeeActivity.this, "Error fetching tasks: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        // Add the request to the RequestQueue
        Volley.newRequestQueue(this).add(jsonObjectRequest);
    }


    /**
     * Get the next status of the project.
     * Order is Assigned -> In Progress -> Completed -> Assigned -> ...
     * @param currentStatus
     * @return
     */
    private String getNextStatus(String currentStatus) {
        if ("Assigned".equals(currentStatus)) {
            return "In Progress";
        } else if ("In Progress".equals(currentStatus)) {
            return "Completed";
        } else {
            return "Assigned"; // Cycle back to "Assigned"
        }
    }

    /**
     * Update the task object on the server using PUT requests
     * @param taskId
     * @param newStatus
     * @param taskStatusView
     * @param colorBox
     * @param taskName
     * @param taskDescription
     * @param progress
     * @param createdAt
     * @param updatedAt
     * @param employeeAssignedTo
     * @param employerAssignedTo
     */
    private void updateTaskStatusOnServer(long taskId, String newStatus, TextView taskStatusView, TextView colorBox, String taskName, String taskDescription, int progress, String createdAt, String updatedAt, String employeeAssignedTo, String employerAssignedTo) {
        // URL to update the task status
        String url = "http://coms-3090-046.class.las.iastate.edu:8080/tasks/" + taskId;

        // Create the JSON object with updated status and all required fields
        JSONObject taskData = new JSONObject();
        try {
            taskData.put("id", taskId);
            taskData.put("name", taskName);
            taskData.put("description", taskDescription);
            taskData.put("status", newStatus);
            taskData.put("progress", progress);
            taskData.put("createdAt", createdAt);
            taskData.put("updatedAt", updatedAt);
            taskData.put("employeeAssignedTo", employeeAssignedTo);
            taskData.put("employerAssignedTo", employerAssignedTo);

            // Create the PUT request to update the task
            JsonObjectRequest updateRequest = new JsonObjectRequest(
                    Request.Method.PUT, url, taskData,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            // On success, update the status on the UI
                            //Toast.makeText(taskEmployeeActivity.this, "Task status updated to " + newStatus, Toast.LENGTH_SHORT).show();
                            taskStatusView.setText(newStatus);

                            // Change color based on new status
                            if ("Completed".equals(newStatus)) {
                                colorBox.setBackgroundColor(getResources().getColor(R.color.green));
                            } else if ("In Progress".equals(newStatus)) {
                                colorBox.setBackgroundColor(getResources().getColor(R.color.yellow));
                            } else {
                                colorBox.setBackgroundColor(getResources().getColor(R.color.red));
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("Volley Error", "Error updating task status: " + error.getMessage());
                            //Toast.makeText(taskEmployeeActivity.this, "Error updating task status: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

            // Add the request to the RequestQueue
            Volley.newRequestQueue(this).add(updateRequest);

        } catch (JSONException e) {
            e.printStackTrace();
            //Toast.makeText(taskEmployeeActivity.this, "Error creating task data", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Close activity when done
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish(); // Close the activity when back is pressed
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
