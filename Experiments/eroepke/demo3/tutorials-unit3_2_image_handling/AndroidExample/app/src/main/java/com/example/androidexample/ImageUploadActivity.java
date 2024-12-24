package com.example.androidexample;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ImageUploadActivity extends AppCompatActivity {

    Button selectBtn;
    Button uploadBtn;
    ImageView mImageView;
    Uri selectedUri;  // Corrected variable name
    ProgressBar progressBar;

    private static String UPLOAD_URL = "http://10.0.2.2:8080/images";

    private ActivityResultLauncher<String> mGetContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_upload);

        mImageView = findViewById(R.id.imageSelView);
        selectBtn = findViewById(R.id.selectBtn);
        uploadBtn = findViewById(R.id.uploadBtn);
        progressBar = findViewById(R.id.progressBar);

        mGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(),
                uri -> {
                    if (uri != null) {
                        selectedUri = uri;  // Corrected variable name
                        mImageView.setImageURI(uri);
                    }
                });

        selectBtn.setOnClickListener(v -> mGetContent.launch("image/*"));
        uploadBtn.setOnClickListener(v -> uploadImage());
    }

    private void uploadImage() {
        // Ensure an image is selected
        if (selectedUri == null) {
            Toast.makeText(this, "Please select an image first", Toast.LENGTH_SHORT).show();
            return;
        }

        byte[] imageData = convertImageUriToBytes(selectedUri);

        // Show progress bar
        progressBar.setVisibility(View.VISIBLE);
        progressBar.setProgress(0);

        MultipartRequest multipartRequest = new MultipartRequest(
                Request.Method.POST,
                UPLOAD_URL,
                imageData,
                response -> {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(), "Upload successful!", Toast.LENGTH_LONG).show();
                    Log.d("Upload", "Response: " + response);
                },
                error -> {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(), "Upload failed: " + error.getMessage(), Toast.LENGTH_LONG).show();
                    Log.e("Upload", "Error: " + error.getMessage());
                }
        ) {
            @Override
            protected void onProgress(long bytesWritten, long totalSize) {
                if (totalSize > 0) {
                    int progress = (int) ((bytesWritten * 100) / totalSize);
                    progressBar.setProgress(progress);
                }
            }
        };

        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(multipartRequest);
    }

    private byte[] convertImageUriToBytes(Uri imageUri) {
        try {
            InputStream inputStream = getContentResolver().openInputStream(imageUri);
            ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();

            int bufferSize = 1024;
            byte[] buffer = new byte[bufferSize];

            int len;
            while ((len = inputStream.read(buffer)) != -1) {
                byteBuffer.write(buffer, 0, len);
            }

            return byteBuffer.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}




