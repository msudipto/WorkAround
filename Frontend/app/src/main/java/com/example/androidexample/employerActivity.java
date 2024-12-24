package com.example.androidexample;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.FrameLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class employerActivity extends AppCompatActivity {
    private boolean isClockedIn = false;

    private long clockInTime;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());

    private FrameLayout borderChange;

    private Button checkButton;
    private Button projectStatButton;
    private Button assignProjButton;
    private Button employeeAttendanceButton;
    private Button employeeStatButton;
    private Button messageButton;
    private Button performanceReviewButton;
    private Button profileButton;
    private Button projButton;
    private Button selfServiceButton;
    private Button payButton;
    private TextView checkInMsg;
    private Chronometer timeClockMsg;
    private TextView payHome;
    private TextView hoursHome;
    private TextView payDayHome;
    private String loggedInUsername;
    private TextView welcomeMessage;

    private SearchView searchView;
    private Button searchButton;
    private TextView resultTextView;
    private TextView shiftDateNextHome;
    private TextView shiftHoursNextHome;
    private TextView shiftProjectNextHome;

    private List<String> sampleData;


    @SuppressLint({"WrongViewCast", "MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.employer);

        // Retrieve username from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        loggedInUsername = sharedPreferences.getString("username", null);

        // Fetch user data using the logged-in username
        fetchPayData(loggedInUsername);
        fetchScheduleData(loggedInUsername);
        fetchUsersName(loggedInUsername);

        //resetButtonBorders();
        //updateCheckFrameBorder();

        borderChange = findViewById(R.id.frameChange);
        checkButton = findViewById(R.id.checkButtonLogin);
        checkInMsg = findViewById(R.id.checkText);
        timeClockMsg = findViewById(R.id.timeText);
        projectStatButton = findViewById(R.id.projStatusButton);
        assignProjButton = findViewById(R.id.assignProjButton);
        employeeAttendanceButton = findViewById(R.id.employeeAttendanceButton);
        employeeStatButton = findViewById(R.id.employeeStatusbutton);
        messageButton = findViewById(R.id.messageButton);
        performanceReviewButton = findViewById(R.id.performanceButton);
        profileButton = findViewById(R.id.profileButton);
        projButton = findViewById(R.id.projButton);
        selfServiceButton = findViewById(R.id.selfServiceButton);
        payButton = findViewById(R.id.payButton);
        searchView = findViewById(R.id.searchView);
        searchButton = findViewById(R.id.searchButton);
        resultTextView = findViewById(R.id.resultTextView);
        payHome = findViewById(R.id.payText);
        hoursHome = findViewById(R.id.hoursWorkedText);
        payDayHome = findViewById(R.id.payDateText);
        shiftDateNextHome = findViewById(R.id.nextShiftText);
        shiftHoursNextHome = findViewById(R.id.shiftHoursText);
        shiftProjectNextHome = findViewById(R.id.assignedProjText);
        welcomeMessage = findViewById(R.id.welcomeMessage);

        initializeSampleData();


        // Restore clock-in state and time from SharedPreferences
        isClockedIn = sharedPreferences.getBoolean("isClockedIn", false);
        clockInTime = sharedPreferences.getLong("clockInTime", 0);

        LayerDrawable layerDrawable = (LayerDrawable) borderChange.getBackground();
        Drawable borderDrawable = layerDrawable.getDrawable(0);

        if (borderDrawable instanceof GradientDrawable) {
            GradientDrawable gradientDrawable = (GradientDrawable) borderDrawable;

            if (isClockedIn) {
                gradientDrawable.setStroke(15, Color.GREEN);
                checkInMsg.setText("Clock Out");

                // Resume the chronometer based on the saved clock-in time
                timeClockMsg.setBase(SystemClock.elapsedRealtime() - (System.currentTimeMillis() - clockInTime));
                timeClockMsg.start();
            } else {
                gradientDrawable.setStroke(15, Color.GRAY);
                checkInMsg.setText("Clock In");
                timeClockMsg.stop();
                timeClockMsg.setBase(SystemClock.elapsedRealtime());
            }
        }

        // Clock In/Out button listener
        checkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (borderDrawable instanceof GradientDrawable) {
                    GradientDrawable gradientDrawable = (GradientDrawable) borderDrawable;

                    if (isClockedIn) {
                        gradientDrawable.setStroke(15, Color.GRAY);
                        checkInMsg.setText("Clock In");

                        timeClockMsg.stop();
                        timeClockMsg.setBase(SystemClock.elapsedRealtime());

                        String clockOutTime = dateFormat.format(new Date());
                        showClockOutPopup(clockInTime, System.currentTimeMillis() - clockInTime, clockOutTime);
                    } else {
                        gradientDrawable.setStroke(15, Color.GREEN);
                        checkInMsg.setText("Clock Out");

                        timeClockMsg.setBase(SystemClock.elapsedRealtime());
                        timeClockMsg.start();

                        clockInTime = System.currentTimeMillis();
                    }

                    isClockedIn = !isClockedIn;
                }
            }
        });


        // Search button listener
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performSearch();
            }
        });

        //All Intents for buttons to new pages down below
        projectStatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(employerActivity.this, projectStatusActivity.class);
                startActivity(intent);
            }
        });
        assignProjButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(employerActivity.this, createTasksActivity.class);
                startActivity(intent);
            }
        });
        // TEMPORARILY CHANGED THIS TO CREATE SCHEDULE BUTTON
        employeeAttendanceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(employerActivity.this, employeeAttendanceActivity.class);
                startActivity(intent);
            }
        });
        employeeStatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(employerActivity.this, employeeStatusActivity.class);
                startActivity(intent);
            }
        });
        messageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(employerActivity.this, messageActivity.class);
                startActivity(intent);
            }
        });
        performanceReviewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(employerActivity.this, EmployeePerformanceReviewActivity.class);
                startActivity(intent);
            }
        });
        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(employerActivity.this, profileActivity.class);
                startActivity(intent);
            }
        });
        projButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(employerActivity.this, projectEmployerActivity.class);
                startActivity(intent);
            }
        });
        selfServiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(employerActivity.this, scheduleEmployerActivity.class);
                startActivity(intent);
            }
        });
        payButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(employerActivity.this, payCheckOverviewActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();

        // Save the clock-in state and time to SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isClockedIn", isClockedIn);
        editor.putLong("clockInTime", clockInTime);
        editor.apply();
    }


    private void initializeSampleData() {
        sampleData = new ArrayList<>();
        sampleData.add("Project A");
        sampleData.add("Project B");
        sampleData.add("Employee 1");
        sampleData.add("Employee 2");
        sampleData.add("Attendance Report");
        sampleData.add("Performance Review");
    }

    private void performSearch() {
        String query = searchView.getQuery().toString().toLowerCase();
        if (!query.isEmpty()) {
            StringBuilder results = new StringBuilder("Search Results:\n");
            boolean found = false;

            for (String item : sampleData) {
                if (item.toLowerCase().contains(query)) {
                    results.append(item).append("\n");
                    found = true;
                }
            }

            if (found) {
                resultTextView.setText(results.toString());
                resultTextView.setVisibility(View.VISIBLE);
            } else {
                resultTextView.setText("No results found for: " + query);
                resultTextView.setVisibility(View.VISIBLE);
            }
        } else {
            resultTextView.setText("Please enter a search term.");
            resultTextView.setVisibility(View.VISIBLE);
        }
    }

    private void showClockOutPopup(long clockInTime, long elapsedMillis, String clockOutTime) {
        long elapsedHours = elapsedMillis / 3600000;
        long elapsedMinutes = (elapsedMillis % 3600000) / 60000;

        String clockInTimeFormatted = dateFormat.format(new Date(clockInTime));
        String workedHours = String.format(Locale.getDefault(), "%02d:%02d", elapsedHours, elapsedMinutes);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Clock Out Summary");
        builder.setMessage("Clock In Time: " + clockInTimeFormatted +
                "\nClock Out Time: " + clockOutTime +
                "\nHours Worked: " + workedHours);
        builder.setPositiveButton("OK", (dialog, which) -> dialog.dismiss());
        builder.show();
    }

    // Method to fetch user data from the backend and set it in the TextViews
    private void fetchPayData(String username) {
        if (username == null || username.isEmpty()) {
            //Toast.makeText(this, "Username not found", Toast.LENGTH_SHORT).show();
            return;
        }

        String url = "http://coms-3090-046.class.las.iastate.edu:8080/api/salary/username/" + username;

        // Create a new request
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            // Parse the response and set the values
                            String takeHome = response.optString("takeHomePay", "0.00");
                            String hours = response.optString("hoursWorked", "0.00");
                            String payDay = response.optString("payday", "00/00/00");

                            payHome.setText("Pay: $" + takeHome);
                            hoursHome.setText("Hours worked: " + hours + " hours");
                            payDayHome.setText("Payday: " + payDay);



                        } catch (Exception e) {
                            //Toast.makeText(employerActivity.this, "Error parsing data", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Toast.makeText(employerActivity.this, "Error fetching data", Toast.LENGTH_SHORT).show();
                    }
                });

        // Add the request to the RequestQueue
        Volley.newRequestQueue(this).add(jsonObjectRequest);
    }

    //Method to fetch all of users data thats logged in and then post name to welcome message
    private void fetchUsersName(String username) {
        if (username == null || username.isEmpty()) {
            //Toast.makeText(this, "Username not found", Toast.LENGTH_SHORT).show();
            return;
        }

        String url = "http://coms-3090-046.class.las.iastate.edu:8080/api/userprofile/username/" + username;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            // Extract the "fullName" field from the response
                            String fullName = response.getString("fullName");

                            // Update the welcome message
                            TextView welcomeMessage = findViewById(R.id.welcomeMessage);
                            welcomeMessage.setText("Welcome, " + fullName + "!");

                        } catch (JSONException e) {
                            e.printStackTrace();
                            //Toast.makeText(employerActivity.this, "Error parsing user data", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Toast.makeText(employerActivity.this, "Error fetching user data: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        // Add the request to the RequestQueue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonObjectRequest);
    }


    // Method to fetch user data for schedules. Get all users schedules and get next upcoming shift
    private void fetchScheduleData(String username) {
        if (username == null || username.isEmpty()) {
            //Toast.makeText(this, "Username not found", Toast.LENGTH_SHORT).show();
            return;
        }

        String url = "http://coms-3090-046.class.las.iastate.edu:8080/schedules/assigned/" + username;

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            // Initialize variables to track the next upcoming schedule
                            JSONObject nextSchedule = null;
                            LocalDateTime now = null;
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                now = LocalDateTime.now();
                            }
                            LocalDateTime closestTime = null;

                            // Date formatter for displaying the date and time
                            DateTimeFormatter dateFormatter = null;
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                dateFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
                            }
                            DateTimeFormatter timeFormatter = null; // 12-hour format with AM/PM
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                timeFormatter = DateTimeFormatter.ofPattern("hh:mm a");
                            }

                            // Iterate through all schedules
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject schedule = response.getJSONObject(i);

                                // Check if the schedule is assigned to the specified username
                                String assignedTo = schedule.optString("employerAssignedTo", "");
                                if (!assignedTo.equals(username)) {
                                    // Skip this schedule if it's not assigned to the provided username
                                    continue;
                                }

                                // Parse the start time
                                String startTimeStr = schedule.optString("startTime", "");
                                LocalDateTime startTime = null;
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                    startTime = LocalDateTime.parse(startTimeStr);
                                }

                                // Check if the schedule is upcoming and closer than any previously found schedule
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                    if (startTime.isAfter(now) && (closestTime == null || startTime.isBefore(closestTime))) {
                                        closestTime = startTime;
                                        nextSchedule = schedule;
                                    }
                                }
                            }

                            // If an upcoming schedule was found, set it in the TextViews
                            if (nextSchedule != null) {
                                // Format the date and time
                                String dateHome = null;
                                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                                    dateHome = closestTime.toLocalDate().format(dateFormatter);
                                }
                                String hoursStartHome = null;
                                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                                    hoursStartHome = closestTime.format(timeFormatter);
                                }
                                String hoursEndHome = null;
                                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                                    hoursEndHome = LocalDateTime.parse(nextSchedule.optString("endTime")).format(timeFormatter);
                                }
                                String projectHome = nextSchedule.optString("eventType", "N/A");

                                shiftDateNextHome.setText("Next Shift: " + dateHome);
                                shiftHoursNextHome.setText("Hours: " + hoursStartHome + " - " + hoursEndHome);
                                shiftProjectNextHome.setText("Assigned Project: " + projectHome);
                            } else {
                                // No upcoming schedule found for the user
                                shiftDateNextHome.setText("No upcoming shifts");
                                shiftHoursNextHome.setText("");
                                shiftProjectNextHome.setText("");
                            }
                        } catch (Exception e) {
                            //Toast.makeText(employerActivity.this, "Error parsing data", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Toast.makeText(employerActivity.this, "Error fetching data", Toast.LENGTH_SHORT).show();
                    }
                });

        // Add the request to the RequestQueue
        Volley.newRequestQueue(this).add(jsonArrayRequest);
    }
}
