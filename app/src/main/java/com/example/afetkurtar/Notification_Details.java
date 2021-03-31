package com.example.afetkurtar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

public class Notification_Details extends AppCompatActivity implements OnMapReadyCallback {
    RequestQueue queue;
    JSONObject data;
    private GoogleMap mMap;
    private Geocoder geocoder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification__details);
        queue = Volley.newRequestQueue(this);
        findViewById(R.id.Notification_geri_button).setOnClickListener(this::onClick);
        findViewById(R.id.Create_new_Disaster).setOnClickListener(this::onClick);
        findViewById(R.id.Delete_notice).setOnClickListener(this::onClick);

        Bundle bundle = getIntent().getExtras();
        String message = bundle.getString("json");
        try {
            data = new JSONObject(message);
        } catch (JSONException e) {
            e.printStackTrace();
        }
       // System.out.println(data.toString());

        try {
            readImage(data.getString("imageURL"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        TextView a = findViewById(R.id.notification_mesaj);
        try {
            a.setText(data.getString("message"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        geocoder = new Geocoder(this);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.notification_map);
        mapFragment.getMapAsync(Notification_Details.this) ;
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        Toast.makeText(this, "map is ready", Toast.LENGTH_SHORT).show();
        LatLng latLng = null;
        try {
            latLng = new LatLng(Double.parseDouble(data.getString("latitude")), Double.parseDouble(data.getString("longitude")));
            Marker marker = mMap.addMarker(new MarkerOptions().position(latLng).title("Enkaz Noktasi").draggable(true));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,10));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public void readImage(String url){
        ImageView mImageView;
        mImageView = (ImageView) findViewById(R.id.Notification_image);


// Retrieves an image specified by the URL, displays it in the UI.

        ImageRequest request = new ImageRequest(url,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap bitmap) {
                        mImageView.setImageBitmap(bitmap);
                    }
                }, 0, 0, null,
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });
// Access the RequestQueue through your singleton class.
        queue.add(request);
    }
    public void delete(JSONObject data){
        JSONObject obj = new JSONObject();
        try {
            obj.put("noticeID", data.getString("noticeID"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // ASAGISI DEGISECEK -- TEST AMACLI YAPILDI

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, "https://afetkurtar.site/api/notice/delete.php", obj, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            //  System.out.println(response.toString());
                            ReturnBack();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error
                        System.out.println(error);
                    }
                });
        queue.add(jsonObjectRequest);
    }
    public void ReturnBack(){
        finish();
    }
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.Notification_geri_button:
              //  Intent intent = new Intent(this, Authorized_Notification.class); //DEGISECEK
              //  startActivity(intent);
                finish();
                break;
            case R.id.Create_new_Disaster:
                  Intent intent = new Intent(this, Create_Subpart_On_Map.class);
                  intent.putExtra("json", data.toString());
                  startActivity(intent);
                //  finish();
                break;
            case R.id.Delete_notice:
               delete(data);
                //  finish();
                break;
    }
}
}