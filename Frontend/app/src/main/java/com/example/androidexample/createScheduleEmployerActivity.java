package com.example.androidexample;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class createScheduleEmployerActivity extends AppCompatActivity {
    private EditText dateEditText;
    private TextView startTimeText, endTimeText;
    private Button saveButton;
    private EditText nameEntry, employeeAssignedEditText;

    private boolean doesNotExist;
    private RequestQueue requestQueue;

    private String selectedStartTime;
    private String selectedEndTime;
    private String selectedDate;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.createschedule);

        // Initialize views
        dateEditText = findViewById(R.id.dateScheduled);
        startTimeText = findViewById(R.id.startTimeText);
        endTimeText = findViewById(R.id.endTimeText);
        saveButton = findViewById(R.id.saveButton);
        nameEntry = findViewById(R.id.nameEntry);
        employeeAssignedEditText = findViewById(R.id.nameEntry);

        // Set up the toolbar
        Toolbar toolbar = findViewById(R.id.toolBarScheduler);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Create Schedule");

        requestQueue = Volley.newRequestQueue(this);

        // Set up the date picker for the EditText
        setUpDatePicker();

        // Set up time picker for start and end times
        setUpTimePicker(startTimeText, true); // true for start time
        setUpTimePicker(endTimeText, false);  // false for end time

        // Set up the save button
        saveButton.setOnClickListener(view -> {
            checkUserExists();
            if (!doesNotExist) {
                // Create schedule request
                createSchedule();
            } else {
                //Toast.makeText(this, "User does not exist.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setUpDatePicker() {
        dateEditText.setOnClickListener(v -> {
            final Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(createScheduleEmployerActivity.this,
                    (view, selectedYear, selectedMonth, selectedDay) -> {
                        selectedDate = selectedYear + "-" + (selectedMonth + 1) + "-" + selectedDay;
                        dateEditText.setText((selectedMonth + 1) + "/" + selectedDay + "/" + selectedYear);
                    }, year, month, day);

            datePickerDialog.show();
        });
    }

    private void setUpTimePicker(TextView timeText, boolean isStartTime) {
        timeText.setOnClickListener(v -> {
            final Calendar calendar = Calendar.getInstance();
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);

            TimePickerDialog timePickerDialog = new TimePickerDialog(createScheduleEmployerActivity.this,
                    (view, selectedHour, selectedMinute) -> {
                        boolean isAM = selectedHour < 12;
                        int hourIn12Format = selectedHour % 12;
                        if (hourIn12Format == 0) hourIn12Format = 12;

                        String formattedTime = String.format("%02d:%02d %s", hourIn12Format, selectedMinute, isAM ? "AM" : "PM");

                        // Store the time in a variable based on start or end time
                        if (isStartTime) {
                            selectedStartTime = formattedTime;
                        } else {
                            selectedEndTime = formattedTime;
                        }

                        timeText.setText(formattedTime);
                    }, hour, minute, false);

            timePickerDialog.show();
        });
    }

    private void createSchedule() {
        // Combine date and time for start and end time in ISO 8601 format
        String startDateTime = selectedDate + "T" + formatTimeTo24Hour(selectedStartTime);
        String endDateTime = selectedDate + "T" + formatTimeTo24Hour(selectedEndTime);

        // Get employee (from text entry) and employer (from SharedPreferences)
        String employeeAssignedTo = employeeAssignedEditText.getText().toString().trim();

        // Fetch employerAssignedTo (username) from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String employerAssignedTo = sharedPreferences.getString("username", "");  // Default to empty string if no value is found

        // Prepare the schedule data to send in the POST request
        JSONObject scheduleData = new JSONObject();
        try {
            scheduleData.put("eventType", "Sample");  // Example eventType, modify as necessary
            scheduleData.put("startTime", startDateTime); // Format: yyyy-MM-dd'T'HH:mm:ss
            scheduleData.put("endTime", endDateTime); // Format: yyyy-MM-dd'T'HH:mm:ss
            scheduleData.put("userId", 101);  // Assuming userId is 101, modify as necessary
            scheduleData.put("projectId", 101); // Modify projectId as necessary
            scheduleData.put("employeeAssignedTo", employeeAssignedTo);
            scheduleData.put("employerAssignedTo", employerAssignedTo);

            Log.e("JSON RESPONSE", scheduleData.toString());
        } catch (JSONException e) {
            e.printStackTrace();
            return;
        }

        // Send the POST request to create the schedule
        String url = "http://coms-3090-046.class.las.iastate.edu:8080/schedules/create";
        JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, url, scheduleData,
                response -> {
                    // Success response
                    Log.d("POST Response", response.toString());

                    Intent intent = new Intent(createScheduleEmployerActivity.this, scheduleEmployerActivity.class);
                    startActivity(intent);
                },
                error -> {
                    // Error handling: log error and print response body if possible
                    if (error.networkResponse != null) {
                        Log.e("POST Error", "Status Code: " + error.networkResponse.statusCode);
                        Log.e("POST Error", "Response Body: " + new String(error.networkResponse.data));
                    }
                    //Toast.makeText(this, "Failed to create schedule", Toast.LENGTH_SHORT).show();
                });

        requestQueue.add(postRequest);
    }

    // Converts 12-hour time format to 24-hour format (backend expected)
    private String formatTimeTo24Hour(String time) {
        try {
            SimpleDateFormat twelveHourFormat = new SimpleDateFormat("hh:mm a");
            SimpleDateFormat twentyFourHourFormat = new SimpleDateFormat("HH:mm");
            Date date = twelveHourFormat.parse(time);
            return twentyFourHourFormat.format(date);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    private void checkUserExists() {
        String username = nameEntry.getText().toString().trim();

        // Send a GET request to check if the user exists
        String url = "http://coms-3090-046.class.las.iastate.edu:8080/api/userprofile/username/" + username;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    // Check if the response is empty or null
                    doesNotExist = response.length() == 0;
                    Log.d("Volley Response", response.toString());
                }, error -> {
            doesNotExist = true;
            Log.e("Volley Error", error.toString());
        });

        requestQueue.add(request);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            // Redirect user to the previous screen (adjust based on your permissions)
            Intent intent = new Intent(createScheduleEmployerActivity.this, scheduleEmployerActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
