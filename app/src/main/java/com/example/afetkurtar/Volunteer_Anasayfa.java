package com.example.afetkurtar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;


public class Volunteer_Anasayfa extends AppCompatActivity {
    DrawerLayout drawerLayout;
    GoogleSignInClient mGoogleSignInClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volunteer_anasayfa);
        drawerLayout = findViewById(R.id.drawer_layout);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);


        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);

        //System.out.println(account.getId());

        /*
         * Asagidaki Kısımda Ad ve Soyad Kısmı dolduruluyor.
         * Istenmezse comment'e alınabilir
         */
        /*TextView name = findViewById(R.id.multiAutoCompleteTextView);
        TextView surname = findViewById(R.id.multiAutoCompleteTextView5);
        String k = account.getDisplayName();
        name.setText(account.getGivenName());
        surname.setText(account.getFamilyName());*/




    }
    @Override
    public void onBackPressed() {
      //  redirectActivity(this,Volunteer_Anasayfa.class);
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        startActivity(intent);
    }

    public void ClickRegisterInfo(){
        redirectActivity(this,Volunteer_RegisterInfo.class);
    }
    private void signOut() {
        LogoutHandler lout = new LogoutHandler(getApplicationContext());
        lout.updateUser();
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    public void onComplete(@NonNull Task<Void> task) {
                        System.out.println("Cikis basarili");
                    }
                });
    }
    public void ClickMenu(View view) {
        //open drawer
        openDrawer(drawerLayout);
    }

    static void openDrawer(DrawerLayout drawerLayout) {
        //Open drawer Layout
        drawerLayout.openDrawer(GravityCompat.START);
    }

    public void ClickLogo(View view) {
        //Close drawer
        closeDrawer(drawerLayout);
    }

    public static void closeDrawer(DrawerLayout drawerLayout) {
        //Close drawer layout
        //check condition
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            //when driver is open
            //close drawer
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }


    public void ClickAnasayfa(View view) {
        //redirect activity to dashboard
        redirectActivity(this, Volunteer_Anasayfa.class );
    }


    public void ClickParticipateRequest(View view) {
        //redirect activity to volunter
        redirectActivity(this, Volunteer_ParticipateRequest.class );
    }

    public void ClickEmergency(View view) {
        //redirect activity to emergency
        redirectActivity(this, Volunteer_Emergency.class );
    }
    public void ClickParticipateForm(View view) {
        //redirect activity to emergency
        redirectActivity(this, Volunteer_RegisterInfo.class );
    }
    public void ClickExit(View view) {
        //redirect activity to main screen
        signOut();
        redirectActivity(this, MainActivity.class );
    }
    public void ClickPersonel(View view) {
        //redirect activity to main screen
        redirectActivity(this, Personel_Progress.class );
    }
    public void ClickAfetBolgesi(View view) {
        //redirect activity to main screen
        redirectActivity(this, Afet_Bolgesi.class );
    }


    public static void redirectActivity(Activity activity, Class aClass) {
        //initialize intent
        Intent intent = new Intent(activity, aClass);
        //Set flag
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //Start activity
        activity.startActivity(intent);

    }
}