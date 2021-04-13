package com.example.afetkurtar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class Authorized_Team_Equipment_Request_Details extends AppCompatActivity {
    RequestQueue queue;
    JSONObject data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authorized__team__equipment__request__details);

        queue = Volley.newRequestQueue(this);
        findViewById(R.id.Equipment_geri_button).setOnClickListener(this::onClick);
        findViewById(R.id.Delete_equip_request).setOnClickListener(this::onClick);

        Bundle bundle = getIntent().getExtras();
        String message = bundle.getString("json");
        try {
            data = new JSONObject(message);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // System.out.println(data.toString());

   //     try {
        //    readImage(data.getString("equipmentID"));
     //   } catch (JSONException e) {
       //     e.printStackTrace();
       // }
        /*
        TextView a = findViewById(R.id.request_mesaj);
        try {
            a.setText(data.getString("message"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
*/
        try {
            getEquipData(data.getString("equipmentID"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void getEquipData(String ID){
        JSONObject tmp = new JSONObject();

        try {
            tmp.put("equipmentID",ID);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, "https://afetkurtar.site/api/equipment/search.php", tmp, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            //  System.out.println(response.toString());
                            String tmp = response.getString("records");
                            tmp = tmp.substring(1,tmp.length()-1);
                            JSONObject tmpjson = new JSONObject(tmp);

                            TextView text = findViewById(R.id.request_mesaj);
                            text.setText("EKİPMAN İSTEĞİ DETAYLARI"+"\n\n"+"İstek ID :\n\n" + data.getString("equipmentRequestID")
                            +"\n\n"+"İstenilen Ekipman :\n\n" + tmpjson.getString("equipmentName")
                            +"\n\n"+"İstenen Miktar :\n\n"+data.getString("quantity"));
                            // BURADA EKRANA BASTIR BISEYLER

                            //readImage("https://afetkurtar.site/imgs/"+response.getString("equipmentImageURL")); // ******************************************************************************** BURADA KALDIN
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
    /*
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
     */
    public void delete(JSONObject data){
        JSONObject obj = new JSONObject();
        try {
            obj.put("equipmentRequestID", data.getString("equipmentRequestID"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // ASAGISI DEGISECEK -- TEST AMACLI YAPILDI

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, "https://afetkurtar.site/api/equipmentRequest/delete.php", obj, new Response.Listener<JSONObject>() {

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
            case R.id.Equipment_geri_button:
                //  Intent intent = new Intent(this, Authorized_Notification.class); //DEGISECEK
                //  startActivity(intent);
                finish();
                break;

            case R.id.Delete_equip_request:
                delete(data);
                //  finish();
                break;
        }
    }
}