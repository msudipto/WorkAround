package com.example.androidexample;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class forgotpasswordActivity extends AppCompatActivity {

    private Button back_button;
    private EditText emailInput;
    private Button submit_button;
    private TextView messageText;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgotpassword);

        back_button = findViewById(R.id.backButton);
        emailInput = findViewById(R.id.usernameInput);
        submit_button = findViewById(R.id.submitButton);
        messageText = findViewById(R.id.messageText);

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(forgotpasswordActivity.this, loginActivity.class);
                startActivity(intent);
            }
        });

        submit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailInput.getText().toString().trim();

                // Email empty add message
                if (email.isEmpty()) {
                    messageText.setText("Please enter your email.");
                    return;
                }

                messageText.setText("");

                String url = "http://coms-3090-046.class.las.iastate.edu:8080/login/forgotPassword?email=" + email;


                RequestQueue queue = Volley.newRequestQueue(forgotpasswordActivity.this);

                StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Intent intent = new Intent(forgotpasswordActivity.this, resetPasswordActivity.class);
                                intent.putExtra("email", email);
                                startActivity(intent);
                                finish();
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                if (error.networkResponse != null && error.networkResponse.statusCode == 404) {
                                    messageText.setText("No user exists with this email.");
                                } else {
                                    messageText.setText("An error occurred. Please try again.");
                                }
                            }
                        });
                queue.add(stringRequest);
            }
        });

    }
}