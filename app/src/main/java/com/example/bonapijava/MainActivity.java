package com.example.bonapijava;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    Button btn_openRecipe;
    Intent browserIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Initializing the queue object
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_openRecipe = findViewById(R.id.btn_openRecipe);

        btn_openRecipe.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                // Instantiate the RequestQueue.
                RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
                String url = "https://bon-api.com/api/v1/grocery/delivery/app/recipe-check/";

                JSONObject object = new JSONObject();
                try {
                    // Request body
                    object.put("recipe_id","11004");
                    object.put("recipe_name","Widget inApp Testing");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, object,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    JSONObject json= new JSONObject(response.toString());
                                    String widget_url = json.getString("grocery_widget_url");
                                    browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(String.valueOf(widget_url)));
                                    startActivity(browserIntent);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();
                    }
                }) {
                    /**
                     * Passing request headers
                     */
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        HashMap<String, String> headers = new HashMap<String, String>();
                        headers.put("Content-Type", "application/json");
                        headers.put("Authorization", "Token 344f1e6e60b59946a4429f6a4b72ea9cd1b13d64");
                        return headers;
                    }
                };

                // Add the request to the RequestQueue.
                queue.add(jsonObjectRequest);
            }
        });

    }
}