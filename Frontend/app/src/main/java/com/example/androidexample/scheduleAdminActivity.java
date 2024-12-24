package com.example.androidexample;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class scheduleAdminActivity extends AppCompatActivity {

    private CalendarView scheduleCalendar;
    private TextView eventNameTextView;
    private TextView eventTimeTextView;
    private Map<String, List<String>> eventDetails;
    private String loggedInUsername;
    private Button createSchedule;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scheduleadmin);

        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        loggedInUsername = sharedPreferences.getString("username", null);

        Toolbar toolbar = findViewById(R.id.toolBarScheduleView);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Schedule");

        // Initialize views for event details
        eventNameTextView = findViewById(R.id.eventName);
        eventTimeTextView = findViewById(R.id.eventTime);
        createSchedule = findViewById(R.id.createScheduleButtonAdmin);

        scheduleCalendar = findViewById(R.id.scheduleCalendar);

        // Initialize the map for storing event details
        eventDetails = new HashMap<>();

        // Fetch event data from the server
        fetchEventData(loggedInUsername);

        createSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(scheduleAdminActivity.this, createScheduleAdminActivity.class);
                startActivity(intent);
            }
        });

        // Set a listener for date changes
        scheduleCalendar.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            // Convert the selected date from CalendarView (milliseconds) to "YYYY-MM-DD" format
            String selectedDate = formatDateToString(year, month, dayOfMonth);

            // Debugging log to check the selected date
            Log.d("SelectedDate", "Selected date: " + selectedDate);

            // Check if the selected date has events
            if (eventDetails.containsKey(selectedDate)) {
                List<String> events = eventDetails.get(selectedDate);
                StringBuilder eventMessage = new StringBuilder("Events on " + selectedDate + ":\n");

                // Append all event details for that date
                for (String event : events) {
                    // Assuming event format "EventType from StartTime to EndTime"
                    String[] eventParts = event.split(" from ");
                    String eventName = eventParts[0];  // Event name
                    String eventTime = eventParts[1];  // Time range (from start to end)

                    // Extract only the time part (HH:mm) from the start and end time
                    String startTime = formatTime(eventTime.split(" to ")[0]); // Get start time (HH:mm)
                    String endTime = formatTime(eventTime.split(" to ")[1]);   // Get end time (HH:mm)

                    // Set event name and time (only time, not date) in the TextViews
                    eventNameTextView.setText("Project Assigned: " + eventName);
                    eventTimeTextView.setText("Hours: " + startTime + " to " + endTime);
                }
            } else {
                // If no events are found for the selected date, show N/A
                eventNameTextView.setText("Project Assigned: N/A");
                eventTimeTextView.setText("Hours: N/A");
            }
        });
    }

    // Method to fetch event data and process it
    private void fetchEventData(String username) {
        // Updated URL that directly fetches the logged-in user's schedules
        String url = "https://eceda3fd-5b51-430f-bd55-3a91ebb3ca23.mock.pstmn.io/schedule/johndoe";

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject event = response.getJSONObject(i);

                                // Extract event details
                                String startTime = event.getString("startTime");
                                String endTime = event.getString("endTime");
                                String eventType = event.getString("eventType");
                                String eventDate = startTime.split("T")[0];
                                String eventInfo = eventType + " from " + startTime + " to " + endTime;

                                // Store event details by date
                                if (!eventDetails.containsKey(eventDate)) {
                                    eventDetails.put(eventDate, new ArrayList<>());
                                }
                                eventDetails.get(eventDate).add(eventInfo);
                            }
                        } catch (JSONException e) {
                            Log.e("Schedule", "Error processing the event data.", e);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Schedule", "Error fetching data.", error);
                    }
                });

        Volley.newRequestQueue(this).add(request);
    }



    // Utility method to convert the selected date to a "YYYY-MM-DD" format
    private String formatDateToString(int year, int month, int dayOfMonth) {
        // Month is 0-based, so add 1 to get the correct month number
        month = month + 1;

        // Format the date as "YYYY-MM-DD"
        return String.format("%04d-%02d-%02d", year, month, dayOfMonth);
    }

    // Method to format the time part (HH:mm) from the full datetime string
    // Method to format the time part (HH:mm a) from the full datetime string
    private String formatTime(String datetime) {
        try {
            // Create a SimpleDateFormat object to parse the datetime
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            // Parse the datetime string
            java.util.Date date = inputFormat.parse(datetime);

            // Now format the date object to extract time in 12-hour format with AM/PM (e.g., 03:15 PM)
            SimpleDateFormat outputFormat = new SimpleDateFormat("h:mm a");
            return outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(scheduleAdminActivity.this, adminActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}