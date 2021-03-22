package com.example.afetkurtar;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

public class Afet_Bolgesi extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private static final int ERROR_DIALOG_REQUEST = 9001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_afet_bolgesi);

        if(isServicesOk()){
            init();
        }
    }

    private void init(){
        Button btnMap = (Button) findViewById(R.id.btnMap);
        btnMap.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(Afet_Bolgesi.this, MapActivity.class);
                startActivity(intent);
            }
        });


        Button btnCreateDis = (Button) findViewById(R.id.btnCreateDisaster);
        btnCreateDis.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(Afet_Bolgesi.this, Create_Disaster_Event_On_Map.class);
                startActivity(intent);
            }
        });

        Button btnShowSubpartMap = (Button) findViewById(R.id.btnShowSubparts);
        btnShowSubpartMap.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(Afet_Bolgesi.this, Show_Subparts_Of_Disaster.class);
                startActivity(intent);
            }
        });

        Button btnCreateDisasterInfo = (Button) findViewById(R.id.btnCreateMainDisasterEvent);
        btnCreateDisasterInfo.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(Afet_Bolgesi.this, Create_Main_Disaster_Event_Info.class);
                startActivity(intent);
            }
        });
    }

    public boolean isServicesOk(){
        Log.d(TAG, "isServicesOK: checking google services version");

        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(Afet_Bolgesi.this);
        if(available == ConnectionResult.SUCCESS){
            Log.d(TAG, "isServicesOK: Google Play Services working");
            return true;
        }else if(GoogleApiAvailability.getInstance().isUserResolvableError(available)){
            Log.d(TAG, ",isServicesOK: an error occured but we can fix it");
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(Afet_Bolgesi.this, available, ERROR_DIALOG_REQUEST);
            dialog.show();
        }else{
            Toast.makeText(this,"You can't make map requests", Toast.LENGTH_SHORT).show();
        }
        return false;

    }
}