package com.example.androidexample;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;


import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * This class handles the signing up of a new user
 */
public class joinNowActivity extends AppCompatActivity {

    private Button backButton;
    private Button signUp;
    private EditText name;
    private EditText email;
    private EditText usernameFill;
    private EditText password;
    private EditText verifyPassword;
    private TextView nameErrorMessage;
    private TextView emailErrorMessage;
    private TextView verifyPasswordErrorMessage;
    private ImageButton showPassword1;
    private ImageButton showPassword2;

    boolean isPasswordVisible1 = false;
    boolean isPasswordVisible2 = false;

    private String signup_url = "http://coms-3090-046.class.las.iastate.edu:8080/login/signup";
    private JSONObject signup_details;


    /**
     * The running of the class, initiallizes all buttons and maps them to the XML
     * @param savedInstanceState
     */
    @SuppressLint({"WrongViewCast", "MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.joinnow);

        backButton = findViewById(R.id.backButton);
        signUp = findViewById(R.id.joinNow);
        name = findViewById(R.id.enterName);
        email = findViewById(R.id.emailInput);
        usernameFill = findViewById(R.id.usernameInput);
        password = findViewById(R.id.enterPassword);
        verifyPassword = findViewById(R.id.reenterPassword);
        nameErrorMessage = findViewById(R.id.nameError);
        emailErrorMessage = findViewById(R.id.emailError);
        verifyPasswordErrorMessage = findViewById(R.id.verifyPasswordError);
        showPassword1 = findViewById(R.id.showPassword1);
        showPassword2 = findViewById(R.id.showPassword2);


        // Set initial visibility of error messages to gone
        nameErrorMessage.setVisibility(View.GONE);
        emailErrorMessage.setVisibility(View.GONE);
        verifyPasswordErrorMessage.setVisibility(View.GONE);

        signup_details = new JSONObject();

        /**
         * Username text entry
         */
        name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                fillUsername(name, usernameFill);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        /**
         * User's name text entry
         */
        // Add TextWatcher for name EditText
        name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (isValidFullName(s.toString())) {
                    nameErrorMessage.setVisibility(View.GONE);
                } else {
                    nameErrorMessage.setVisibility(View.VISIBLE);
                    nameErrorMessage.setText("Enter a valid full name");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        /**
         * Text entry for email
         */
        // Add TextWatcher for email EditText
        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (isValidEmail(s.toString())) {
                    emailErrorMessage.setVisibility(View.GONE);
                } else {
                    emailErrorMessage.setVisibility(View.VISIBLE);
                    emailErrorMessage.setText("Enter a valid email");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        /**
         * Text entry for password
         */
        // Add TextWatcher for password EditText
        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (isValidPassword(s.toString())) {
                    // Hide any alert or error related to password
                } else {
                    // Optionally display an error message if needed
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        /**
         * Text entry for verify password
         */
        // Add TextWatcher for verify password EditText
        verifyPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().equals(password.getText().toString())) {
                    verifyPasswordErrorMessage.setVisibility(View.GONE);
                } else {
                    verifyPasswordErrorMessage.setVisibility(View.VISIBLE);
                    verifyPasswordErrorMessage.setText("Passwords do not match");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        /**
         * Button to go back to login screen
         */
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(joinNowActivity.this, loginActivity.class);
                startActivity(intent);
            }
        });

        /**
         * Submit all entries to sign up
         */
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nameFilled = name.getText().toString();
                Log.i("hidden", nameFilled);
                String emailFilled = email.getText().toString();
                Log.i("hidden", emailFilled);
                String usernameFilled = usernameFill.getText().toString();
                Log.i("hidden", usernameFilled);
                String passwordFilled = password.getText().toString();
                Log.i("hidden", passwordFilled);
                String verifyPasswordFilled = verifyPassword.getText().toString();

                // Check if name includes first and last name
                if (!isValidFullName(nameFilled)) {
                    nameErrorMessage.setText("Enter a valid full name");
                    nameErrorMessage.setVisibility(View.VISIBLE);
                } else {
                    nameErrorMessage.setVisibility(View.GONE);
                }

                // Check if email is valid
                if (!isValidEmail(emailFilled)) {
                    emailErrorMessage.setText("Enter a valid email");
                    emailErrorMessage.setVisibility(View.VISIBLE);
                } else {
                    emailErrorMessage.setVisibility(View.GONE);
                }

                // Check to make sure password has minimum requirements
                if (!isValidPassword(passwordFilled)) {
                    showAlertDialog("Error", "Password must be at least 8 characters long, contain 1 uppercase letter, 1 number, and 1 special character");
                    return; // Exit the method if password is invalid
                }

                // Check to see if passwords match
                if (!passwordFilled.equals(verifyPasswordFilled)) {
                    verifyPasswordErrorMessage.setText("Passwords do not match");
                    verifyPasswordErrorMessage.setVisibility(View.VISIBLE);
                    return; // Exit the method if passwords do not match
                } else {
                    verifyPasswordErrorMessage.setVisibility(View.GONE);
                }
                try {
                    signup_details.put("username", usernameFilled);
                    signup_details.put("email", emailFilled);
                    signup_details.put("password", passwordFilled);
                    signup_details.put("full_name", nameFilled);

                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

                // If all validations pass, proceed to loginActivity
                postRequest(signup_details);
                Intent intent = new Intent(joinNowActivity.this, loginActivity.class);
                startActivity(intent);
            }
        });


        // Set initial input types for password fields to hidden
        password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        verifyPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

        /**
         * Show password button
         */
        //Show and hide password for first line
        showPassword1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isPasswordVisible1){
                    password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    showPassword1.setImageResource(R.drawable.eyehide);
                }
                else{
                    password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    showPassword1.setImageResource(R.drawable.eyeshow);
                }
                isPasswordVisible1 = !isPasswordVisible1;
            }
        });

        /**
         * Hide password button
         */
        //Show and hide password for second line
        showPassword2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isPasswordVisible2){
                    verifyPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    showPassword2.setImageResource(R.drawable.eyehide);
                }
                else{
                    verifyPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    showPassword2.setImageResource(R.drawable.eyeshow);
                }
                isPasswordVisible2 = !isPasswordVisible2;
            }
        });
    }

    /**
     * Check if the full name is valid
     * @param nameFilled
     * @return
     */
    private boolean isValidFullName(String nameFilled) {
        // Check if full name is not empty and contains at least one space (indicating two words)
        return !TextUtils.isEmpty(nameFilled) && nameFilled.contains(" ") && nameFilled.trim().split("\\s+").length >= 2;
    }

    /**
     * Check if the email is valid
     * @param emailFilled
     * @return
     */
    private boolean isValidEmail(String emailFilled) {
        // Check if email matches legit email addresses
        return !TextUtils.isEmpty(emailFilled) && Patterns.EMAIL_ADDRESS.matcher(emailFilled).matches();
    }

    /**
     * Check if the password meets all parameters. One uppercase letter, one digit, one special character.
     * @param passwordFilled
     * @return
     */
    private boolean isValidPassword(String passwordFilled) {
        // Password must be at least 8 characters long, contain an uppercase letter, a number, and a special character
        return passwordFilled.length() >= 8
                && passwordFilled.matches(".*[A-Z].*")        // At least one uppercase letter
                && passwordFilled.matches(".*[0-9].*")        // At least one digit
                && passwordFilled.matches(".*[!@#\\$%^&*].*"); // At least one special character
    }

    /**
     * Helper method to show alerts
     * @param title
     * @param message
     */
    private void showAlertDialog(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(joinNowActivity.this);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton("OK", (dialog, which) -> dialog.dismiss());
        builder.show();
    }

    /**
     * Helper method to autofill username from given name
     * @param name
     * @param username
     */
    private void fillUsername(EditText name, EditText username){
        String fullName = name.getText().toString().trim();
        //Split name by space
        String[] firstLast = fullName.split("\\s+");

        //Check first and last is in name field
        if(firstLast.length >= 2){
            //first initial
            String firstInitial = firstLast[0].substring(0, 1).toLowerCase();
            //last name
            String lastName = firstLast[firstLast.length - 1].toLowerCase();

            String usernameFinal = firstInitial + lastName;
            usernameFill.setText(usernameFinal);
        }
    }

    /**
     * POST request to add a new userprofile object to the database
     * @param j
     */
    public void postRequest(JSONObject j) {
        JsonObjectRequest post_join = new JsonObjectRequest(
                Request.Method.POST,
                signup_url,
                j,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Volley Response", response.toString());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley Error", error.toString());
                    }
                }

        )
        {
            // dont know if necessary
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                return headers;
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                return params;
            }
        };

        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(post_join);
    }


}

