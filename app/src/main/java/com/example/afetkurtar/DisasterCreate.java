package com.example.afetkurtar;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class DisasterCreate extends AppCompatActivity implements OnMapReadyCallback {
    ArrayList<String> arrayListAfetType = new ArrayList<>();
    RequestQueue queue;
    JSONObject data = new JSONObject();
    private GoogleMap mMap;
    private Geocoder geocoder;
    ArrayAdapter<String> adapterAfetType;
    Spinner afetTypeSpinner;
    GoogleSignInClient mGoogleSignInClient;
    DrawerLayout drawerLayout;
    Marker clickMarker;
    ArrayList<String> arrayListEmergencyLevel = new ArrayList<>();
    ArrayAdapter<String> adapterEmergencyLevel;
    Spinner emergencyLevelSpinner;
    ArrayList<String> arrayListAfetBase = new ArrayList<>();
    ArrayAdapter<String> adapterAfetBase;
    Spinner afetBaseSpinner;
    EditText afetName, latitude, longtitude;
    static Double latitudeMap, logtitudeMap;
    Button gonder, geri;
    static String afetTipi = "", afetussu = "";
    static String level = "";
    static int indexType, indexbase, indexlevel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disaster_create);
        queue = Volley.newRequestQueue(this);
        drawerLayout = findViewById(R.id.drawer_layout_disaster_create);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        afetTypeSpinner = findViewById(R.id.afetlistner);
        emergencyLevelSpinner = findViewById(R.id.emergencylevellistliner);
        afetBaseSpinner = findViewById(R.id.disasterBaseListliner);
        latitude = findViewById(R.id.latitudeBtn);
        longtitude = findViewById(R.id.longtitudeBtn);
        loadSpinnerAfetBase();
        loadSpinnerDataAfetType();
        loadSpinnerEmergencyLevel();
        afetName = findViewById(R.id.disasterNameBtn);
        afetName.setText(afetussu + " " + afetTipi + " " + "Bölgesi");
        gonder = findViewById(R.id.afetolusturBTN);
        /*
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
        */

        initMap();
        gonder.setOnClickListener(new View.OnClickListener() {

            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
               if(afetussu.equals("Seçilmedi")){
                   Toast.makeText(getApplicationContext(), "Afet üssü seçiniz", Toast.LENGTH_SHORT).show();

               }
               if(level.equals("0")){
                   Toast.makeText(getApplicationContext(), "Aciliyet seviyesi seçiniz", Toast.LENGTH_SHORT).show();

                }
               if(afetTipi.equals("Seçilmedi")){
                   Toast.makeText(getApplicationContext(), "Afet tipi seçiniz", Toast.LENGTH_SHORT).show();
               }
               else{
                   JSONObject obj = new JSONObject();
                   try {
//Get current date time
                       afetName.setText(afetussu + " " + afetTipi + " " + "Bölgesi");
                       System.out.println("*********************************************************************");
                       System.out.println(afetTipi + "***********" + level + "**********" + afetussu);
                       System.out.println("*********************************************************************");
                       LocalDateTime now = LocalDateTime.now();
                       DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                       String formatDateTime = now.format(formatter);
                       obj.put("disasterType", afetTipi);
                       obj.put("emergencyLevel", Integer.parseInt(level));
                       obj.put("latitude", latitudeMap);
                       obj.put("longitude", logtitudeMap);
                       obj.put("disasterDate", formatDateTime);
                       obj.put("disasterBase", afetussu);
                       obj.put("disasterName", afetName.getText().toString());
                   } catch (Exception e) {

                   }
                   JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, "https://afetkurtar.site/api/disasterEvents/create.php", obj, new Response.Listener<JSONObject>() {
                       @Override
                       public void onResponse(JSONObject response) {
                           System.out.println(response.toString());
                       }
                   }, new Response.ErrorListener() {
                       @Override
                       public void onErrorResponse(VolleyError error) {
                           System.out.println(error);
                       }
                   });
                   queue.add(request);
                   Toast.makeText(DisasterCreate.this, "Afet Oluşturuldu.", Toast.LENGTH_LONG).show();
                   finish();
               }
            }
        });
    }

    public void initMap() {
        Locale locale = new Locale("tr", "TR");
        geocoder = new Geocoder(this, locale);


        //geocoder = new Geocoder(this);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.disaster_create_map);
        mapFragment.getMapAsync(DisasterCreate.this);
    }

    public void calculateCoordinates(LatLng myLatlng) {
        try {
            if (!clickMarker.equals(null)) {
                clickMarker.remove();
            }
        } catch (Exception e) {
            e.getMessage();
        }

        double tmpLatitude = myLatlng.latitude;
        double tmpLongitude = myLatlng.longitude;

        String tmpLatitudeString = String.valueOf(myLatlng.latitude);
        String tmpLongitudeString = String.valueOf(myLatlng.longitude);

        try {
            if (tmpLatitudeString.indexOf(".") != -1 && tmpLatitudeString.substring(tmpLatitudeString.indexOf(".")).length() > 4) {
                tmpLatitude = Double.parseDouble(tmpLatitudeString.substring(0, tmpLatitudeString.indexOf(".") + 4));
            }
            if (tmpLongitudeString.indexOf(".") != -1 && tmpLongitudeString.substring(tmpLongitudeString.indexOf(".")).length() > 4) {
                tmpLongitude = Double.parseDouble(tmpLongitudeString.substring(0, tmpLongitudeString.indexOf(".") + 4));
            }
        } catch (Exception e) {
            e.getMessage();
        }
        ((EditText) (findViewById(R.id.latitudeBtn))).setText(tmpLatitudeString);
        ((EditText) (findViewById(R.id.longtitudeBtn))).setText(tmpLongitudeString);

        latitudeMap = tmpLatitude;
        logtitudeMap = tmpLongitude;

        LatLng latLng = new LatLng(myLatlng.latitude, myLatlng.longitude);
        clickMarker = mMap.addMarker(new MarkerOptions().position(latLng));
        mMap.setInfoWindowAdapter(new CustomInfoWindowAdapter(DisasterCreate.this));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 8));
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        System.out.println("test : map is ready");
        Toast.makeText(this, "map is ready", Toast.LENGTH_SHORT).show();

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng arg0) {

                //for calculate coordinates and show on screen
                calculateCoordinates(arg0);
            }
        });

        // if data is not empty get dataLatitude and dataLongitude

        if (!data.equals(new JSONObject())) {

            try {
                String dataLatitude = data.getString("latitude");
                String dataLongitude = data.getString("longitude");

                double doubleDataLatitude = Double.parseDouble(dataLatitude);
                double doubleDataLongitude = Double.parseDouble(dataLongitude);

                LatLng latLng = new LatLng(doubleDataLatitude, doubleDataLongitude);

                Marker dataMarker = mMap.addMarker(new MarkerOptions().position(latLng));
                mMap.setInfoWindowAdapter(new CustomInfoWindowAdapter(DisasterCreate.this));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 13));

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }

    private void loadSpinnerAfetBase() {
        adapterAfetBase = new ArrayAdapter<String>(DisasterCreate.this, android.R.layout.simple_spinner_dropdown_item, arrayListAfetBase);
        adapterAfetBase.add("Seçilmedi");
        adapterAfetBase.add("Adana");
        adapterAfetBase.add("Adıyaman");
        adapterAfetBase.add("Afyon");
        adapterAfetBase.add("Ağrı");
        adapterAfetBase.add("Amasya");
        adapterAfetBase.add("Ankara");
        adapterAfetBase.add("Antalya");
        adapterAfetBase.add("Artvin");
        adapterAfetBase.add("Aydın");
        adapterAfetBase.add("Balıkesir");
        adapterAfetBase.add("Bilecik");
        adapterAfetBase.add("Bingöl");
        adapterAfetBase.add("Bitlis");
        adapterAfetBase.add("Bolu");
        adapterAfetBase.add("Burdur");
        adapterAfetBase.add("Bursa");
        adapterAfetBase.add("Çanakkale");
        adapterAfetBase.add("Çankırı");
        adapterAfetBase.add("Çorum");
        adapterAfetBase.add("Denizli");
        adapterAfetBase.add("Diyarbakır");
        adapterAfetBase.add("Edirne");
        adapterAfetBase.add("Elazığ");
        adapterAfetBase.add("Erzincan");
        adapterAfetBase.add("Erzurum");
        adapterAfetBase.add("Eskişehir");
        adapterAfetBase.add("Gaziantep");
        adapterAfetBase.add("Giresun");
        adapterAfetBase.add("Gümüşhane");
        adapterAfetBase.add("Hakkari");
        adapterAfetBase.add("Hatay");
        adapterAfetBase.add("Isparta");
        adapterAfetBase.add("İçel (Mersin)");
        adapterAfetBase.add("İstanbul");
        adapterAfetBase.add("İzmir");
        adapterAfetBase.add("Kars");
        adapterAfetBase.add("Kastamonu");
        adapterAfetBase.add("Kayseri");
        adapterAfetBase.add("Kırklareli");
        adapterAfetBase.add("Kırşehir");
        adapterAfetBase.add("Kocaeli");
        adapterAfetBase.add("Konya");
        adapterAfetBase.add("Kütahya");
        adapterAfetBase.add("Malatya");
        adapterAfetBase.add("Manisa");
        adapterAfetBase.add("Kahramanmaraş");
        adapterAfetBase.add("Mardin");
        adapterAfetBase.add("Muğla");
        adapterAfetBase.add("Muş");
        adapterAfetBase.add("Nevşehir");
        adapterAfetBase.add("Niğde");
        adapterAfetBase.add("Ordu");
        adapterAfetBase.add("Rize");
        adapterAfetBase.add("Sakarya");
        adapterAfetBase.add("Samsun");
        adapterAfetBase.add("Siirt");
        adapterAfetBase.add("Sinop");
        adapterAfetBase.add("Sivas");
        adapterAfetBase.add("Tekirdağ");
        adapterAfetBase.add("Tokat");
        adapterAfetBase.add("Trabzon");
        adapterAfetBase.add("Tunceli");
        adapterAfetBase.add("Şanlıurfa");
        adapterAfetBase.add("Uşak");
        adapterAfetBase.add("Van");
        adapterAfetBase.add("Yozgat");
        adapterAfetBase.add("Zonguldak");
        adapterAfetBase.add("Aksaray");
        adapterAfetBase.add("Bayburt");
        adapterAfetBase.add("Karaman");
        adapterAfetBase.add("Kırıkkale");
        adapterAfetBase.add("Batman");
        adapterAfetBase.add("Şırnak");
        adapterAfetBase.add("Bartın");
        adapterAfetBase.add("Ardahan");
        adapterAfetBase.add("Iğdır");
        adapterAfetBase.add("Yalova");
        adapterAfetBase.add("Karabük");
        adapterAfetBase.add("Kilis");
        adapterAfetBase.add("Osmaniye");
        adapterAfetBase.add("Düzce");

        afetBaseSpinner.setAdapter(adapterAfetBase);
        afetBaseSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                afetussu = adapterAfetBase.getItem(position).toString();
                if(afetussu.equals("Seçilmedi")){
                    Toast.makeText(getApplicationContext(), "Afet üssü seçiniz", Toast.LENGTH_SHORT).show();
                }else{
                    afetName.setText(afetussu + " " + afetTipi + " " + "Bölgesi");
                    try {
                        setMapLocation();
                    } catch (Exception e) {
                        e.getMessage();
                    }

                    indexbase = position;
                    Toast.makeText(getApplicationContext(), afetussu, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void loadSpinnerEmergencyLevel() {
        adapterEmergencyLevel = new ArrayAdapter<String>(DisasterCreate.this, android.R.layout.simple_spinner_dropdown_item, arrayListEmergencyLevel);
        adapterEmergencyLevel.add("0");
        adapterEmergencyLevel.add("1");
        adapterEmergencyLevel.add("2");
        adapterEmergencyLevel.add("3");
        adapterEmergencyLevel.add("4");
        adapterEmergencyLevel.add("5");
        adapterEmergencyLevel.add("6");
        adapterEmergencyLevel.add("7");
        adapterEmergencyLevel.add("8");
        adapterEmergencyLevel.add("9");
        adapterEmergencyLevel.add("10");

        emergencyLevelSpinner.setAdapter(adapterEmergencyLevel);
        emergencyLevelSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                level = adapterEmergencyLevel.getItem(position).toString();
                if(level.equals("0")){
                    Toast.makeText(getApplicationContext(), "Aciliyet seviyesi seçiniz", Toast.LENGTH_SHORT).show();
                }else{
                    indexlevel = position;
                    Toast.makeText(getApplicationContext(), level, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    private void loadSpinnerDataAfetType() {
        adapterAfetType = new ArrayAdapter<String>(DisasterCreate.this, android.R.layout.simple_spinner_dropdown_item, arrayListAfetType);
       adapterAfetType.add("Seçilmedi");
        adapterAfetType.add("Deprem");
        adapterAfetType.add("Yangın");
        adapterAfetType.add("Sel");
        adapterAfetType.add("Heyelan");
        adapterAfetType.add("Çığ");
        afetTypeSpinner.setAdapter(adapterAfetType);
        afetTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                afetTipi = adapterAfetType.getItem(position).toString();
                if(afetTipi.equals("Seçilmedi")){
                    Toast.makeText(getApplicationContext(), "Afet tipi seçiniz", Toast.LENGTH_SHORT).show();
                }else {
                    afetName.setText(afetussu + " " + afetTipi + " " + "Bölgesi");
                    indexType = position;
                    Toast.makeText(getApplicationContext(), afetTipi, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    public void setMapLocation() {
        mMap.clear();
        String location = controlString(afetussu);
        //String location = afetussu;

        System.out.println("location :  ==============================" + location);
        // = //// aldığımız yazı
        if (location != null && !location.equals("")) {

            List<Address> addressList = null;
            //geocoder = new Geocoder(MapActivity.this);

            try {
                addressList = geocoder.getFromLocationName(location, 1);

            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (addressList.size() > 0) {
                    Address address = addressList.get(0);
                    LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());

                    Marker mMarker = mMap.addMarker(new MarkerOptions().position(latLng).title("Burası " + location));
                    mMap.setInfoWindowAdapter(new CustomInfoWindowAdapter(DisasterCreate.this));
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));
                    latitudeMap = address.getLatitude();
                    logtitudeMap = address.getLongitude();
                    ((EditText) (findViewById(R.id.latitudeBtn))).setText(latitudeMap + "");
                    ((EditText) (findViewById(R.id.longtitudeBtn))).setText(logtitudeMap + "");
                } else {
                    //Log.d(TAG,"onMapReady: can not find this area!");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    public String controlString(String line) {
        line = line.toLowerCase();
        String part1 = "";
        String part2 = "";
        System.out.println("control String e girdi");
        for (int i = 0; i < line.length(); i++) {
            if (line.charAt(i) == 'ç') {

                part1 = line.substring(0, i);
                part2 = line.substring(i + 1);
                line = part1 + "c" + part2;
            } else if (line.charAt(i) == 'ş') {
                part1 = line.substring(0, i);
                part2 = line.substring(i + 1);
                line = part1 + "s" + part2;
            } else if (line.charAt(i) == 'ü') {
                part1 = line.substring(0, i);
                part2 = line.substring(i + 1);
                line = part1 + "u" + part2;
            } else if (line.charAt(i) == 'ğ') {
                part1 = line.substring(0, i);
                part2 = line.substring(i + 1);
                line = part1 + "g" + part2;
            } else if (line.charAt(i) == 'ı') {
                part1 = line.substring(0, i);
                part2 = line.substring(i + 1);
                System.out.println("part1 :" + part1);
                System.out.println("part2 :" + part2);
                line = part1 + "i" + part2;
                System.out.println("line p1p2 :" + line);
            } else if (line.charAt(i) == 'ö') {
                part1 = line.substring(0, i);
                part2 = line.substring(i + 1);
                line = part1 + "o" + part2;
            }
            part1 = "";
            part2 = "";
        }
        System.out.println("line " + line);
        return line;
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
        System.out.println("logo");
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


    /*
     *************************************** ASAGIDAKI KISIMLAR YONLENDIRMELERI AYARLAR
     */
    private void signOut() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    public void onComplete(@NonNull Task<Void> task) {
                        System.out.println("Cikis basarili");
                    }
                });
    }

    // IHBARLAR
    public void ClickAuthorizedNotice(View view) {
        redirectActivity(this, Authorized_Notification.class);
    }

    // AKTIF AFET
    public void ClickAuthorizeActiveDisaster(View view) {
        redirectActivity(this, Authorized_ActiveDisasters.class);
    }

    // PERSONEL KAYIT
    public void ClickAuthorizedPersonelRegistration(View view) {
        redirectActivity(this, Authorized_PersonelRegister.class);
    }

    // GONULLU ISTEKLERI
    public void ClickAuthrizedVolunteerRequest(View view) {
        redirectActivity(this, Authorized_VolunteerRequest.class);
    }
    //MESAJ

    // CIKIS
    public void ClickAuthorizedExit(View view) {
        signOut();
        redirectActivity(this, MainActivity.class);
    }

    public void ClickNotificationSend(View view) {
        redirectActivity(this, Authorized_Send_Notification.class);
    }

    // ANA SAYFA
    public void ClickAuthAnasayfa(View view) {
        // ZATEN BU SAYFADA OLDUGUNDAN KAPALI
        redirectActivity(this, Authorized_Anasayfa.class );
    }
    public static void redirectActivity(Activity activity, Class aClass) {
        //initialize intent
        Intent intent = new Intent(activity, aClass);
        //Set flag
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //Start activity
        activity.startActivity(intent);

    }
    public void ClickAuthorizedPersoneller(View view) {
        redirectActivity(this, Authorized_Anasayfa.class );
    }
    public void ClickAuthorizedEkipman(View view) {
        redirectActivity(this, Authorized_Anasayfa.class );
    }

    public void ClickAuthorizedTeam(View view) {
        redirectActivity(this, Authorized_Anasayfa.class );
    }
}