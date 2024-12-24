package com.example.androidexample;

import android.content.Context;

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

import java.util.ArrayList;
import java.util.List;

public class Apiservice extends AppCompatActivity {

    //Update URL when backend is done
    private static final String BASE_URL = "";
    private final RequestQueue requestQueue;

    public Apiservice(Context context) {
        requestQueue = Volley.newRequestQueue(context);
    }

    public void getMessages(String chatId, final MessageCallback callback) {

        //URL needs to change
        String url = BASE_URL + "/messages/" + chatId;

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        List<String> messages = new ArrayList<>();
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject messageObject = response.getJSONObject(i);
                                messages.add(messageObject.getString("content")); // Assuming the JSON contains a "content" field
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        callback.onSuccess(messages);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        callback.onError(error);
                    }
                });

        requestQueue.add(jsonArrayRequest);
    }

    public void sendMessage(String chatId, String messageContent, final ResponseCallback callback) {
        String url = BASE_URL + "/messages/" + chatId;

        JSONObject messageJson = new JSONObject();
        try {
            messageJson.put("content", messageContent); // Assuming your backend accepts a "content" field
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, messageJson,
                response -> callback.onSuccess(),
                error -> {
                    error.printStackTrace();
                    callback.onError(error);
                });

        requestQueue.add(jsonObjectRequest);
    }

    public interface MessageCallback {
        void onSuccess(List<String> messages);
        void onError(VolleyError error);
    }

    public interface ResponseCallback {
        void onSuccess();
        void onError(VolleyError error);
    }
}
