package com.example.afetkurtar;

import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
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

import java.util.ArrayList;

public class Create_Subpart_On_Map extends AppCompatActivity implements OnMapReadyCallback {
    private static final String TAG = "Create_Disaster_Event_On_Map";
    RequestQueue queue;
    JSONObject data = new JSONObject();
    private GoogleMap mMap;
    private Geocoder geocoder;

    //variables for info
    private String disasterID = "";
    private String subpartName;
    private String subpartID;
    private String address;
    private double latitude;
    private double longitude;
    private int missingPerson;
    private int rescuedPerson;
    private String stringIsOpenForVolunteers;
    private String disasterName = "";
    private String disasterStatus;
    private int disasterEmergencyLevel;

    private static boolean isOpenForVolunteers; //dikkat burasi sonra boolean olarak degistir

    private TextView tvCoordinatesText;

    //textVariables
    TextView textAfetId;
    EditText editAfetSubpartName;
    EditText editAfetSubpartID;
    EditText editAfetAddress;
    EditText editAfetLatitude;
    EditText editAfetLongitude;
    EditText editAfetMissingPerson;
    EditText editAfetRescuedPerson;
    EditText editAfetStatus;
    EditText editAfetEmergencyLevel;

    RadioGroup radioGroupIsOpenForVolunteers;
    private RadioButton radioButtonIsOpenForVolunteers;

    //marker OnClick Method
    Marker clickMarker;

    Spinner spinner;
    ArrayAdapter adapter;


    ArrayList<String> afetStringList = new ArrayList<String>();
    ArrayList<JSONObject> jsonObjectList = new ArrayList<JSONObject>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_disaster_event);
        queue = Volley.newRequestQueue(this);
        findViewById(R.id.btn_createDisaster).setOnClickListener(this::onClick);
        findViewById(R.id.btn_resetButton).setOnClickListener(this::onClick);

        textAfetId = (TextView)findViewById(R.id.afet_id);
        editAfetSubpartName = (EditText)findViewById(R.id.afet_subpart_name);
        editAfetSubpartID = (EditText)findViewById(R.id.afet_subpart_ID);
        editAfetAddress = (EditText)findViewById(R.id.afet_address);
        editAfetLatitude = (EditText)findViewById(R.id.afet_latitude);
        editAfetLongitude = (EditText)findViewById(R.id.afet_longitude);
        editAfetMissingPerson = (EditText)findViewById(R.id.afet_missing_person);
        editAfetRescuedPerson = (EditText)findViewById(R.id.afet_rescued_person);
        editAfetStatus = (EditText)findViewById(R.id.afet_status);
        editAfetEmergencyLevel = (EditText)findViewById(R.id.afet_emergency_level);


        radioGroupIsOpenForVolunteers = findViewById(R.id.groupForVolunteers);
        int radioId = radioGroupIsOpenForVolunteers.getCheckedRadioButtonId();
        radioButtonIsOpenForVolunteers = (RadioButton)findViewById(radioId);



       // editIsOpenForVolunteers = (EditText)findViewById(R.id.afet_isOpenForVolunteers);

        spinner = (Spinner) findViewById(R.id.mainDisasterSpinner);
        setAfetToSpinner();
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if(((Spinner)findViewById(R.id.mainDisasterSpinner)).getSelectedItem().toString().equalsIgnoreCase("Afet Seçin")){
                    textAfetId.setText("seçilmedi");
                }else {


                    for (JSONObject x : jsonObjectList) {
                        try {
                            if (x.getString("disasterName").equals(((Spinner) findViewById(R.id.mainDisasterSpinner)).getSelectedItem().toString())) {
                                disasterID = x.getString("disasterID");
                                disasterName = x.getString("disasterName");

                            }
                        } catch (Exception e) {
                            e.getMessage();
                        }
                    }
                    textAfetId.setText(disasterID);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //init textView for Coordinates
        tvCoordinatesText = (TextView) findViewById(R.id.coordinateText);
        /*
         Onceden belirtilen kordinatları almak icin asagidaki islemi ekledim
         */

        try {

            Bundle bundle = getIntent().getExtras();
            if(!bundle.isEmpty()){
                try {
                    String message = bundle.getString("json");
                    data = new JSONObject(message);
                }catch (Exception e){
                    e.getMessage();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        /*
         NOT : data.getstring("latitude") veya "longitude" ile bu sayfaya gönderilmis olan
         veriden kordinatini alıyosun.
         */
        //create Map
        initMap();
    }// onCreate End
    public void setAfetToSpinner(){
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, "https://afetkurtar.site/api/disasterEvents/read.php", null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            //  System.out.println(response.toString());
                            handleResponse(response);
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
    public void handleResponse(JSONObject a){
        ArrayList<String> list = new ArrayList<String>();
        try {
            //    System.out.println(response.toString());
            String cevap = a.getString("records");
            cevap = cevap.substring(1,cevap.length()-1);

            while(cevap.indexOf(",{") >-1){
                list.add(cevap.substring(0,cevap.indexOf(",{")));
                cevap = cevap.substring(cevap.indexOf(",{")+1);
            }
            list.add(cevap);

            afetStringList.add("Afet Seçin");

            for(String x:list){
                try {
                    JSONObject tmp = new JSONObject(x);
                    afetStringList.add(tmp.getString("disasterName"));
                    jsonObjectList.add(tmp);
                }catch (Exception e){
                    // e.printStackTrace();
                }
            }

            adapter = new ArrayAdapter<String>(getApplicationContext(),  android.R.layout.simple_spinner_dropdown_item, afetStringList);
            adapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);
        }catch (Exception e){
            e.printStackTrace();
        }
    }



    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        System.out.println("test : map is ready");
        Toast.makeText(this, "map is ready", Toast.LENGTH_SHORT).show();

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener()
        {
            @Override
            public void onMapClick(LatLng arg0)
            {

                //for calculate coordinates and show on screen
                calculateCoordinates(arg0);
            }
        });

        // if data is not empty get dataLatitude and dataLongitude

        if(!data.equals(new JSONObject())){

            try {
                String dataLatitude = data.getString("latitude");
                String dataLongitude = data.getString("longitude");

                double doubleDataLatitude = Double.parseDouble(dataLatitude);
                double doubleDataLongitude = Double.parseDouble(dataLongitude);

                LatLng latLng = new LatLng(doubleDataLatitude, doubleDataLongitude);

                ((EditText)(findViewById(R.id.afet_latitude))).setText(dataLatitude);
                ((EditText)(findViewById(R.id.afet_longitude))).setText(dataLongitude);

                Marker dataMarker = mMap.addMarker(new MarkerOptions().position(latLng));
                mMap.setInfoWindowAdapter(new CustomInfoWindowAdapter(Create_Subpart_On_Map.this));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,13));

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }//onMapReady end

    public void calculateCoordinates(LatLng myLatlng){
        try{
            if(!clickMarker.equals(null)){
                clickMarker.remove();
            }
        }catch (Exception e){
            e.getMessage();
        }

        double tmpLatitude = myLatlng.latitude;
        double tmpLongitude = myLatlng.longitude;

        String tmpLatitudeString = String.valueOf(myLatlng.latitude);
        String tmpLongitudeString = String.valueOf(myLatlng.longitude);

        try{
            if(tmpLatitudeString.indexOf(".") != -1 &&tmpLatitudeString.substring(tmpLatitudeString.indexOf(".")).length() >4){
                tmpLatitude = Double.parseDouble(tmpLatitudeString.substring(0,tmpLatitudeString.indexOf(".")+4));
            }
            if(tmpLongitudeString.indexOf(".") != -1 && tmpLongitudeString.substring(tmpLongitudeString.indexOf(".")).length() >4){
                tmpLongitude = Double.parseDouble(tmpLongitudeString.substring(0,tmpLongitudeString.indexOf(".")+4));
            }
        }catch(Exception e){
            e.getMessage();
        }
        ((EditText)(findViewById(R.id.afet_latitude))).setText(tmpLatitudeString);
        ((EditText)(findViewById(R.id.afet_longitude))).setText(tmpLongitudeString);

        tvCoordinatesText.setText("(" + tmpLatitude + ", " + tmpLongitude + ")");

        LatLng latLng = new LatLng(myLatlng.latitude,myLatlng.longitude);
        clickMarker = mMap.addMarker(new MarkerOptions().position(latLng));
        mMap.setInfoWindowAdapter(new CustomInfoWindowAdapter(Create_Subpart_On_Map.this));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,8));
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_createDisaster:

                if(!data.equals(new JSONObject())){
                        delete(data);
                }

                if(!textAfetId.getText().equals("seçilmedi") && !isTextsAreEmpty() && !textAfetId.getText().equals("")){
                    getEditTextData();
                    //sendDataToDB(); bunu yaz
                    reset(); // after create, reset stats
                }else{
                    Toast.makeText(this, "Boş Kısımları Tamamlayınız!", Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.btn_resetButton:
                resetWithMap();
                break;
        }
    }
    public boolean isTextsAreEmpty(){
        try{
            textAfetId = (TextView)findViewById(R.id.afet_id);
            editAfetSubpartName = (EditText)findViewById(R.id.afet_subpart_name);
            editAfetSubpartID = (EditText)findViewById(R.id.afet_subpart_ID);
            editAfetAddress = (EditText)findViewById(R.id.afet_address);
            editAfetLatitude = (EditText)findViewById(R.id.afet_latitude);
            editAfetLongitude = (EditText)findViewById(R.id.afet_longitude);
            editAfetMissingPerson = (EditText)findViewById(R.id.afet_missing_person);
            editAfetRescuedPerson = (EditText)findViewById(R.id.afet_rescued_person);
            editAfetStatus = (EditText)findViewById(R.id.afet_status);
            editAfetEmergencyLevel = (EditText)findViewById(R.id.afet_emergency_level);

            radioGroupIsOpenForVolunteers = findViewById(R.id.groupForVolunteers);
            int radioId = radioGroupIsOpenForVolunteers.getCheckedRadioButtonId();
            radioButtonIsOpenForVolunteers = (RadioButton)findViewById(radioId);
            if(radioButtonIsOpenForVolunteers == null){
                return false;
            }

            ArrayList<EditText> tmpEditTextList = new ArrayList<EditText>();

            for(EditText eText : tmpEditTextList){
                if(eText.getText().toString().equals("") || eText == null){
                    return true;
                }
            }

            if(textAfetId.getText().toString().equals("seçilmedi") || textAfetId.getText().toString().equals("") || textAfetId == null){
                return true;
            }

        }catch(Exception e){

        }

        return false;
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
                        dataReset();
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
    public void dataReset(){
        data = new JSONObject();
    }

    public void getEditTextData(){
        try {
            //mMap.clear(); //reset map
            for(JSONObject x: jsonObjectList){
                try{
                    if(x.getString("disasterName").equals(((Spinner)findViewById(R.id.mainDisasterSpinner)).getSelectedItem().toString())){
                        disasterID = x.getString("disasterID");
                        disasterName = x.getString("disasterName");

                    }
                }catch (Exception e){
                    e.getMessage();
                }
            }
             textAfetId.setText(disasterID);
            /*
            System.out.println("editAfetId : " + editAfetId.getText().toString());
            System.out.println("editAfetAddress : " + editAfetAddress.getText().toString());
            System.out.println("editAfetLatitude : " + editAfetLatitude.getText().toString());
            System.out.println("editAfetLongitude : " + editAfetLongitude.getText().toString());
            System.out.println("editAfetMissingPerson : " + editAfetMissingPerson.getText().toString());
            System.out.println("editAfetRescuedPerson : " + editAfetRescuedPerson.getText().toString());
            System.out.println("editIsOpenForVolunteers : " + editIsOpenForVolunteers.getText().toString());
             */
            //Dikkat!!! burada bos input kontrolu olursa diye kontrol ekle sonra
            try{
                //disasterID = editAfetId.getText().toString();
                subpartID = editAfetSubpartID.getText().toString();
                subpartName = editAfetSubpartName.getText().toString();
                address = editAfetAddress.getText().toString();
                latitude = Double.parseDouble(editAfetLatitude.getText().toString());
                longitude = Double.parseDouble(editAfetLongitude.getText().toString());
                missingPerson = Integer.parseInt(editAfetMissingPerson.getText().toString());
                rescuedPerson = Integer.parseInt(editAfetRescuedPerson.getText().toString());
                disasterStatus = editAfetStatus.getText().toString();
                disasterEmergencyLevel = Integer.parseInt(editAfetEmergencyLevel.getText().toString());

                if (radioButtonIsOpenForVolunteers.getText().toString().equals("EVET")) {
                    isOpenForVolunteers = true;
                    stringIsOpenForVolunteers = "evet";
                } else {
                    isOpenForVolunteers = false;
                    stringIsOpenForVolunteers = "hayır";
                }

                //isOpenForVolunteers = editIsOpenForVolunteers.getText().toString();
            }catch (Exception e){
                e.getMessage();
            }

            LatLng latLng = null;
            try {
                latLng = new LatLng(latitude,longitude);
                Marker marker = mMap.addMarker(new MarkerOptions().position(latLng).title(disasterID).snippet("Afet Parçası ID : " + subpartID + "\n" +
                                "Afet Parçası İsmi : " + subpartName + "\n" +
                                "Adress : " + address + "\n" +
                                "Enlem : " + latitude + "\n" +
                                "Boylam : " + longitude + "\n" +
                                "Kayıp İnsan Sayısı : " + missingPerson + "\n" +
                                "Kurtarılan İnsan Sayısı : " + rescuedPerson + "\n" +
                                "Afet Durum Bilgisi : " + disasterStatus + "\n" +
                                "Afet Durum Düzeyi : " + disasterEmergencyLevel + "\n" +
                                "Gönüllülere Açık Mı : " + stringIsOpenForVolunteers + "\n")
                        );
                mMap.setInfoWindowAdapter(new CustomInfoWindowAdapter(Create_Subpart_On_Map.this));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,8));
                //Dikkat!!! burada bellki bilgi olusturulup database e gönderildikten sonra ekrandaki coordinat hesaplama tekrar yaptırılabilir.

                // Dikkat !!! burada database e girilen verileri gönderme methodu eklenebilir ya da onClick icine eklenebilir
                // sendDataToDB(); methodu yazılacak

            } catch (Exception e) {
                e.printStackTrace();
            }

        }catch (NullPointerException e){
            e.printStackTrace();
        }
    }
    public void initMap(){
        geocoder = new Geocoder(this);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.create_disaster_event_map);
        mapFragment.getMapAsync(Create_Subpart_On_Map.this) ;
    }
    public void resetWithMap(){
        try{
            mMap.clear();
            reset();
        }catch (Exception e){
            e.getMessage();
        }
    }
    public void reset(){
        try{
            ((TextView)(findViewById(R.id.afet_id))).setText("");
            ((EditText)(findViewById(R.id.afet_subpart_name))).setText("");
            ((EditText)(findViewById(R.id.afet_subpart_ID))).setText("");
            ((EditText)(findViewById(R.id.afet_address))).setText("");
            ((EditText)(findViewById(R.id.afet_latitude))).setText("");
            ((EditText)(findViewById(R.id.afet_longitude))).setText("");
            ((EditText)(findViewById(R.id.afet_missing_person))).setText("");
            ((EditText)(findViewById(R.id.afet_rescued_person))).setText("");
            ((EditText)(findViewById(R.id.afet_status))).setText("");
            ((EditText)(findViewById(R.id.afet_emergency_level))).setText("");

            ((RadioGroup)(findViewById(R.id.groupForVolunteers))).clearCheck();
            stringIsOpenForVolunteers = "";
            isOpenForVolunteers = false;

            spinner.setSelection(0);
        }catch (Exception e){
            e.getMessage();
        }

    }
}//class end
