package com.example.androidexample;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    private TextView messageText;   // define message textview variable
    private ImageView ISU;
    private boolean isISU = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);             // link to Main activity XML

        /* Initialize ImageView */
        ISU = findViewById(R.id.example_image);
        messageText = findViewById(R.id.main_msg_txt);

        ISU.setImageResource(R.drawable.isulogo);
        messageText.setText("This is a Cyclone State");

        /* initialize UI elements */
        // messageText = findViewById(R.id.main_msg_txt);      // link to message textview in the Main activity XML
        // messageText.setText("Hello World");

        ISU.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toggle between the two images when clicked
                if (isISU) {
                    ISU.setImageResource(R.drawable.iowa);  // Switch to image2
                    messageText.setText("#FTH");
                } else {
                    ISU.setImageResource(R.drawable.isulogo);  // Switch back to image1
                    messageText.setText("This is a Cylcone State");
                }
                isISU = !isISU;  // Update the boolean value
            }
        });
    }
}