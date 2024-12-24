package com.example.androidexample;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ImageUploadService {
    @Multipart
    @POST("/images")
    Call<String> uploadImage(@Part MultipartBody.Part image);
}

