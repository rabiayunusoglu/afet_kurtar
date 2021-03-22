package com.example.afetkurtar;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

public class Create_Main_Disaster_Event_Info extends AppCompatActivity {
    RequestQueue queue;

    String disasterID;
    String disasterType;
    int emergencyLevel;
    double latitudeStart;
    double latitudeEnd;
    double longitudeStart;
    double longitudeEnd;
    String disasterDate;
    String disasterBase;
    String disasterName;

    EditText editDisasterID;
    EditText editDisasterType;
    EditText editEmergencyLevel;
    EditText editLatitudeStart;
    EditText editLatitudeEnd;
    EditText editLongitudeStart;
    EditText editLongitudeEnd;
    EditText editDisasterDate;
    EditText editDisasterBase;
    EditText editDisasterName;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_main_disaster_event_info);

        queue = Volley.newRequestQueue(this);
        findViewById(R.id.btn_createMainDisasterInfo).setOnClickListener(this::onClick);
        findViewById(R.id.btn_createMainDisasterResetInfo).setOnClickListener(this::onClick);

    }//onCreate end

    public void resetTexts(){
        try{
            ((EditText)(findViewById(R.id.afet_id))).setText("");
            ((EditText)(findViewById(R.id.afet_name))).setText("");
            ((EditText)(findViewById(R.id.afet_type))).setText("");
            ((EditText)(findViewById(R.id.afet_emergency_level))).setText("");
            ((EditText)(findViewById(R.id.afet_latitude_start))).setText("");
            ((EditText)(findViewById(R.id.afet_longitude_start))).setText("");
            ((EditText)(findViewById(R.id.afet_latitude_end))).setText("");
            ((EditText)(findViewById(R.id.afet_longitude_end))).setText("");
            ((EditText)(findViewById(R.id.afet_date))).setText("");
            ((EditText)(findViewById(R.id.afet_base))).setText("");

        }catch (Exception e){
            e.getMessage();
        }
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_createMainDisasterInfo:
                //createInfo();
                //reset after create
                resetTexts();
                break;
            case R.id.btn_createMainDisasterResetInfo:
                resetTexts();
                break;
        }
    }
}//class end
