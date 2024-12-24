package com.example.androidexample;

import android.annotation.SuppressLint;
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
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class createProject extends AppCompatActivity {

    private EditText projectNameEditText;
    private EditText projectDescriptionEditText;
    private EditText dueDateEditText;
    private Spinner priorityLevelSpinner;
    private EditText employerAssigned;
    private Button saveButton;

    private RequestQueue requestQueue;

    @SuppressLint({"MissingInflatedId", "WrongViewCast"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.createproject);

        projectNameEditText = findViewById(R.id.project_name);
        projectDescriptionEditText = findViewById(R.id.project_description);
        dueDateEditText = findViewById(R.id.due_date);
        priorityLevelSpinner = findViewById(R.id.priority_level);
        employerAssigned = findViewById(R.id.employer_Assigned);
        saveButton = findViewById(R.id.save_button);

        requestQueue = Volley.newRequestQueue(this);

        Toolbar toolbar = findViewById(R.id.toolBarCreate);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Create Project");

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.priority_levels, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        priorityLevelSpinner.setAdapter(adapter);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createProject();
            }
        });
    }

    private void createProject() {
        String url = "http://coms-3090-046.class.las.iastate.edu:8080/api/project/create";

        // Collecting input data
        String projectName = projectNameEditText.getText().toString();
        String projectDescription = projectDescriptionEditText.getText().toString();
        String dueDate = dueDateEditText.getText().toString();
        String priorityLevel = priorityLevelSpinner.getSelectedItem().toString();
        String employer = employerAssigned.getText().toString();

        // Constructing the JSON body
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("projectName", projectName);
            jsonBody.put("Description", projectDescription);
            jsonBody.put("dueDate", dueDate);
            jsonBody.put("priority", priorityLevel);
            jsonBody.put("employerUsername", new ArrayList<String>(){{ add(employer); }});

        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Creating a POST request
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //Toast.makeText(createProject.this, "Project created successfully", Toast.LENGTH_SHORT).show();
                        // Navigate back to project activity
                        Intent intent = new Intent(createProject.this, projectActivity.class);
                        startActivity(intent);
                        finish();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Toast.makeText(createProject.this, "Failed to create project: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        // Adding the request to the queue
        requestQueue.add(jsonObjectRequest);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(createProject.this, projectActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}


