package com.example.androidexample;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.androidexample.MainActivity;
import com.example.androidexample.R;

public class CounterActivity extends AppCompatActivity {

    private TextView numberTxt; // define number textview variable
    private TextView incrementAmountTxt; // TextView to show the increment amount
    private Button increaseBtn; // define increase button variable
    private Button decreaseBtn; // define decrease button variable
    private Button backBtn;     // define back button variable
    private Button changeIncrementBtn; // Button to change increment amount

    private int counter = 0;    // counter variable
    private int incrementAmount = 1;  // default increment amount

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_counter);

        /* initialize UI elements */
        numberTxt = findViewById(R.id.number);
        incrementAmountTxt = findViewById(R.id.increment_amount_txt);
        increaseBtn = findViewById(R.id.counter_increase_btn);
        decreaseBtn = findViewById(R.id.counter_decrease_btn);
        backBtn = findViewById(R.id.counter_back_btn);
        changeIncrementBtn = findViewById(R.id.change_increment_btn);

        /* when increase btn is pressed, increase the counter by incrementAmount */
        increaseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                counter += incrementAmount;
                numberTxt.setText(String.valueOf(counter));
            }
        });

        /* when decrease btn is pressed, decrease the counter by incrementAmount */
        decreaseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                counter -= incrementAmount;
                numberTxt.setText(String.valueOf(counter));
            }
        });

        /* when change increment button is pressed, allow the user to set a new increment amount */
        changeIncrementBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(CounterActivity.this);
                builder.setTitle("Set Increment Amount");

                final EditText input = new EditText(CounterActivity.this);
                input.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_SIGNED);
                builder.setView(input);

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String inputText = input.getText().toString();
                        if (!inputText.isEmpty()) {
                            incrementAmount = Integer.parseInt(inputText);
                            incrementAmountTxt.setText("Increment Amount: " + incrementAmount);
                        }
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
            }
        });

        /* when back btn is pressed, switch back to MainActivity */
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CounterActivity.this, MainActivity.class);
                intent.putExtra("NUM", String.valueOf(counter));  // key-value to pass to the MainActivity
                startActivity(intent);
            }
        });
    }
}