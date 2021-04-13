package com.example.afetkurtar;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Team_Member_Locations extends AppCompatActivity implements OnMapReadyCallback {
    private static final String TAG = "Team_Member_Locations";
    RequestQueue queue;
    JSONObject data = new JSONObject();
    public GoogleMap mMap;
    public Geocoder geocoder;
    public String teamID = "";

    public ArrayList<JSONObject> teamMemberJsonObjectList = new ArrayList<JSONObject>();
    public ArrayList<Marker> markerList = new ArrayList<Marker>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_member_locations);
        queue = Volley.newRequestQueue(this);

        initMap();
    }//on create end

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


        //System.out.println("test : map is ready");
        //Toast.makeText(this, "map is ready", Toast.LENGTH_SHORT).show();
        //System.out.println("onMapready içinde");

        try {
            // bir şekilde team id e ulaşmamız gerekli
            findTeamMemberLocations(Personel_Anasayfa.PersonelInfo.getString("teamID").toString().trim());
            //System.out.println("Personel_Anasayfa.PersonelInfo.getString(\"teamID\").toString(): " + Personel_Anasayfa.PersonelInfo.getString("teamID").toString());
            //findTeamMemberLocations("13");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }//on map ready end
    public void initMap(){
        Log.d(TAG, "initMap: initializeing map");
        System.out.println("initMap içinde");
        geocoder = new Geocoder(Team_Member_Locations.this);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.team_member_locations_map);
        mapFragment.getMapAsync(Team_Member_Locations.this) ;

    }

    public void findTeamMemberLocations(String teamID){
        //System.out.println("findTeamMemberLocations içinde ");
        /* burada team ıd ye göre tüm personnel elemanlarını bul
        bulunan elemanların konumlarını haritada marker olarak işaretle
         */
        // test amaçlı team ıd burada 13 atandı
        //teamID = "13";
        getLocationDataFromDB(teamID);


    }
    public void getLocationDataFromDB(String teamID){
        //System.out.println("getLocationDataFromDB içinde");
        JSONObject obj = new JSONObject();
        try {
            obj.put("teamID",teamID);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, "https://afetkurtar.site/api/personnelUser/search.php", obj, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            //System.out.println("personnel search : " + response.toString());
                            handleResponseGetLocationDataFromDB(response);
                            //System.out.println("(((((((((((((((((((()))))))))))))))))))))))))))" + teamMemberJsonObjectList.toString());
                            initializeTeamMemberLocationsOnMap(teamMemberJsonObjectList);
                        }catch (Exception e){
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
    public void handleResponseGetLocationDataFromDB(JSONObject a) {
        //System.out.println("handleResponseGetLocationDataFromDB içinde ");
        //System.out.println("handleResponseGetMessageDataFromDB'e girdi");
        ArrayList<String> list = new ArrayList<String>();
        try {

            String cevap = a.getString("records");
            cevap = cevap.substring(1, cevap.length() - 1);

            while (cevap.indexOf(",{") > -1) {
                list.add(cevap.substring(0, cevap.indexOf(",{")));
                cevap = cevap.substring(cevap.indexOf(",{") + 1);
            }
            list.add(cevap);

            for (String x : list) {
                try {
                    JSONObject tmp = new JSONObject(x);
                    teamMemberJsonObjectList.add(tmp);
                    //System.out.println("tmp obje içeriği : " + tmp);
                    //messageJsonObjectList = getMessageListToCompareTime(messageJsonObjectList);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }// handle response end

    public void initializeTeamMemberLocationsOnMap(ArrayList<JSONObject> teamMembersList){
        //System.out.println("initializeTeamMemberLocationsOnMap içinde ");
        for(int i = 0 ; i<teamMembersList.size(); i++){
            addTeamMemberMarker(teamMembersList.get(i));
        }
    }
    public void addTeamMemberMarker(JSONObject member){
        System.out.println("addTeamMemberMarker içinde ");
        try {
            String stringLatitude = member.getString("latitude").toString();
            String stringLongitude = member.getString("longitude").toString();
            teamID = member.getString("teamID").toString();
            double latitude = Double.parseDouble(stringLatitude);
            double longitude = Double.parseDouble(stringLongitude);

            LatLng latLng = new LatLng(latitude, longitude);
            Marker memberLocationMarker = mMap.addMarker(new MarkerOptions().position(latLng)
                    .title("Team ID: " + teamID).snippet(member.getString("personnelName").toString()).icon(bitmapDescriptorFromVector(getApplicationContext(),R.drawable.ic_teammember)));
            mMap.setInfoWindowAdapter(new CustomInfoWindowAdapter(Team_Member_Locations.this));
            markerList.add(memberLocationMarker);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    public void resetMap(){
        try{
            mMap.clear();
        }catch (Exception e){
            e.getMessage();
        }
    }
    private BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResId){
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        vectorDrawable.setBounds(0,0, vectorDrawable.getIntrinsicWidth(),vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(),vectorDrawable.getIntrinsicHeight(),Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

}//class end
