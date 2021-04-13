package com.example.afetkurtar;

import android.app.Activity;
import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONException;
import org.json.JSONObject;

public class LogoutHandler extends Activity {
    RequestQueue queue;
    Context context;
    public LogoutHandler(Context context){
        this.context = context;
        queue = Volley.newRequestQueue(context);
    }

    public void updateUser(){

        JSONObject tmp = new JSONObject();
        try {
            tmp.put("userID", MainActivity.userInfo.getString("userID"));
            tmp.put("userType", MainActivity.userInfo.getString("userType"));
            tmp.put("userName", MainActivity.userInfo.getString("userName"));
            tmp.put("email", MainActivity.userInfo.getString("email"));
            tmp.put("createTime", MainActivity.userInfo.getString("createTime"));
            tmp.put("userToken", " ");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            if (MainActivity.userInfo.getString("userToken").length() > 10) {

                JsonObjectRequest request = new JsonObjectRequest(
                        Request.Method.POST, // the request method
                        "https://afetkurtar.site/api/users/update.php", // the URL
                        tmp, // the parameters for the php
                        new Response.Listener<JSONObject>() { // the response listener
                            @Override
                            public void onResponse(JSONObject response) {

                            }
                        },
                        new Response.ErrorListener() { // the error listener
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                System.out.println(error.getMessage());

                                error.printStackTrace();

                            }
                        });
                queue.add(request);
            } else {

            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
