package com.example.afetkurtar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.os.Bundle;
import android.view.View;

import java.lang.annotation.AnnotationTypeMismatchException;

public class Dashboard extends AppCompatActivity {
//Initialize variable
    DrawerLayout drawerLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        //assign variable
        drawerLayout=findViewById(R.id.drawer_layout);
    }

}