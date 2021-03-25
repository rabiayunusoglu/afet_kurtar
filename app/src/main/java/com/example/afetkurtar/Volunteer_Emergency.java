package com.example.afetkurtar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Looper;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
import android.webkit.MimeTypeMap;
import android.content.ContentResolver;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.jibble.simpleftp.*;


public class Volunteer_Emergency extends AppCompatActivity {
    ArrayList<String> arrayListAfet = new ArrayList<>();
    ArrayAdapter<String> adapterAfet;
    DrawerLayout drawerLayout;
    Spinner afetSpinner;
    RequestQueue queue;
    static boolean resimKontrol = false, photoControl=true;
    static File finalFile;
    String url = "https://afetkurtar.site/api/notice/create.php";
    public static final int CAMERA_PERM_CODE = 101;
    private static final int REQUEST_CODE_LOCATION_PERMISSION = 1;
    public static final int CAMERA_REQUEST_CODE = 102;
    public static final int GALLERY_REQUEST_CODE = 105;
    Double latitude, longtitude;
    ImageView selectedImage;
    Button cameraBtn, galleryBtn, submitBtn, yukleBtn;
    String currentPhotoPath;
    static String photoUrl = "";
    int index = 0;
    static String afetBolgesi = "";
    OutputStream outputstrema;
    private EditText message, type;
    StorageReference storageReference;
    GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_volunteer_emergency);
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(Volunteer_Emergency.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE_LOCATION_PERMISSION);

        } else {

            getCurrentLocation();
        }
        drawerLayout = findViewById(R.id.drawer_layout);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        selectedImage = findViewById(R.id.imageView);
        cameraBtn = findViewById(R.id.CameraBtn);
        galleryBtn = findViewById(R.id.GaleryBtn);
        yukleBtn = findViewById(R.id.UpdateButton);
        message = (EditText) findViewById(R.id.message_disaster);
        afetSpinner = (Spinner) findViewById(R.id.spinnerAfetTipi);
        loadSpinnerDataAfet();

        submitBtn = findViewById(R.id.submit);
        queue = Volley.newRequestQueue(this);
        submitBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                try {
                    if (message.getText().toString().length() == 0 || resimKontrol == false || photoControl==false)
                        throw new Exception("");

                    UploadFile uploadFile = new UploadFile();
                    uploadFile.execute();
                    System.out.println("PHOTO URL:*********************************************************" + photoUrl);
                } catch (Exception e) {
                    if (resimKontrol == false| photoControl==false)
                        Toast.makeText(Volunteer_Emergency.this, "Afet bölgesinin resmini yüklemelisiniz.", Toast.LENGTH_LONG).show();
                    else if (message.getText().toString().length() == 0)
                        Toast.makeText(Volunteer_Emergency.this, "Detaylı bilgi kısmını doldurunuz...", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    private void loadSpinnerDataAfet() {
        adapterAfet = new ArrayAdapter<String>(Volunteer_Emergency.this, android.R.layout.simple_spinner_dropdown_item, arrayListAfet);
        adapterAfet.add("Deprem");
        adapterAfet.add("Yangın");
        adapterAfet.add("Sel");
        adapterAfet.add("Heyelan");
        adapterAfet.add("Çığ");
        afetSpinner.setAdapter(adapterAfet);
        afetSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                afetBolgesi = adapterAfet.getItem(position).toString();
                index = position;
                Toast.makeText(getApplicationContext(), afetBolgesi, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    private void getCurrentLocation() {

        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(3000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        LocationServices.getFusedLocationProviderClient(Volunteer_Emergency.this).requestLocationUpdates(locationRequest, new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                super.onLocationResult(locationResult);
                LocationServices.getFusedLocationProviderClient(Volunteer_Emergency.this).removeLocationUpdates(this);
                if (locationResult != null && locationResult.getLocations().size() > 0) {
                    int latestlocationIndex = locationResult.getLocations().size() - 1;
                    latitude = locationResult.getLocations().get(latestlocationIndex).getLatitude();
                    longtitude = locationResult.getLocations().get(latestlocationIndex).getLongitude();
                }

            }
        }, Looper.getMainLooper());

    }


    private void signOut() {
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

    public void ClickRegisterInfo() {
        redirectActivity(this, Volunteer_RegisterInfo.class);
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
        redirectActivity(this, Volunteer_Anasayfa.class);
    }

    public void ClickParticipateForm(View view) {
        //redirect activity to emergency
        redirectActivity(this, Volunteer_RegisterInfo.class);
    }

    public void ClickParticipateRequest(View view) {
        //redirect activity to volunter
        redirectActivity(this, Volunteer_ParticipateRequest.class);
    }

    public void ClickEmergency(View view) {
        //redirect activity to emergency
        redirectActivity(this, Volunteer_Emergency.class);
    }

    public void ClickExit(View view) {
        //redirect activity to main screen
        signOut();
        redirectActivity(this, MainActivity.class);
    }

    public void ClickPersonel(View view) {
        //redirect activity to main screen
        redirectActivity(this, Personel_Progress.class);
    }

    public void ClickAfetBolgesi(View view) {
        //redirect activity to main screen
        redirectActivity(this, Afet_Bolgesi.class);
    }

    public static void redirectActivity(Activity activity, Class aClass) {
        //initialize intent
        Intent intent = new Intent(activity, aClass);
        //Set flag
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //Start activity
        activity.startActivity(intent);

    }

    private void openCamera() {
        Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(camera, CAMERA_REQUEST_CODE);

    }


    private void askCameraPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(Volunteer_Emergency.this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERM_CODE);
        } else {
            openCamera();
        }
    }

    public void ClickCameraBtn(View view) {
        askCameraPermissions();
        resimKontrol = true;
    }

    public void ClickGaleryBtn(View view) {
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(gallery, GALLERY_REQUEST_CODE);
        resimKontrol = true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERM_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera();
            } else {
                Toast.makeText(this, "Camera Permissin is required to Use Camera.", Toast.LENGTH_SHORT).show();
            }
        }

        if (requestCode == REQUEST_CODE_LOCATION_PERMISSION && grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation();
            } else {
                Toast.makeText(this, "permission denied!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @SuppressLint("MissingSuperCall")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == CAMERA_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                selectedImage.setImageBitmap(photo);
                Uri tempUri = getImageUri(getApplicationContext(), photo);

                // CALL THIS METHOD TO GET THE ACTUAL PATH
                finalFile = new File(getRealPathFromURI(tempUri));
                System.out.println(finalFile + "***************************************************");

                try {
                    SaveImage(photo, finalFile.getName());
                } catch (IOException e) {
                    e.printStackTrace();
                }


            }

        }
        if (requestCode == GALLERY_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                Uri contentUri = data.getData();
                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                String imageFileName = "JPEG_" + timeStamp + "." + getFileExt(contentUri);
                Log.d("tag", "onActivityResult: Gallery Image Uri:  " + imageFileName);
                selectedImage.setImageURI(contentUri);
                finalFile = new File(getRealPathFromURI(contentUri));
                System.out.println(finalFile + "***************************************************");

                //uploadImageToFirebase(imageFileName, contentUri);


            }

        }

    }

    private String getFileExt(Uri contentUri) {
        ContentResolver c = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(c.getType(contentUri));
    }

    private void SaveImage(Bitmap finalBitmap, String filename) throws IOException {
        File root = android.os.Environment.getExternalStorageDirectory();
        File dir = new File(root.getAbsolutePath() + "/Pictures");
        dir.mkdirs();
        File file = new File(dir, filename);
        // photoUrl = file.getAbsolutePath();
        Reader pr;
        String line = "";
        try {
            pr = new FileReader(file);
            int data = pr.read();
            while (data != -1) {
                line += (char) data;
                data = pr.read();
            }
            pr.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
//do stuff with line
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp;
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, imageFileName, null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri) {
        String path = "";
        if (getContentResolver() != null) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                path = cursor.getString(idx);
                cursor.close();
            }
        }
        return path;
    }

    public static String getStringContent(HttpResponse response) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "utf-8"));
        String body;
        String content = "";

        while ((body = bufferedReader.readLine()) != null) {
            content += body + "\n";
        }
        return content.trim();
    }

    class UploadFile extends AsyncTask<File, Integer, String> {
        ProgressDialog dialog;

        @Override
        protected String doInBackground(File... params) {
            try {
                if (resimKontrol == false || message.getText().toString().length() == 0)
                    throw new Exception("");
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost("https://afetkurtar.site/api/uploadImage.php");

                MultipartEntity mpEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
                if (finalFile != null) {
                    Log.d("EDIT USER PROFILE", "UPLOAD: file length = " + finalFile.length());
                    Log.d("EDIT USER PROFILE", "UPLOAD: file exist = " + finalFile.exists());
                    mpEntity.addPart("image", new FileBody(finalFile, "application/octet"));
                }
                httppost.setEntity(mpEntity);

                try {

                    HttpResponse response = httpclient.execute(httppost);
                    photoUrl = getStringContent(response);
                    if(photoUrl==null || photoUrl.length()==0)
                        photoControl=false;
                } catch (IOException e) {
                    e.printStackTrace();
                }

            } catch (Exception e) {
                e.printStackTrace();
                if (resimKontrol == false || photoControl==false)
                    Toast.makeText(Volunteer_Emergency.this, "Afet bölgesinin resmini yüklemelisiniz.", Toast.LENGTH_LONG).show();
                else if (message.getText().toString().length() == 0)
                    Toast.makeText(Volunteer_Emergency.this, "Detaylı bilgi kısmını doldurunuz...", Toast.LENGTH_LONG).show();
                //Toast.makeText(MainActivity.this, "Something Went Wrong...", Toast.LENGTH_LONG).show();
                dialog.dismiss();
            }
            return null;
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.e("FTP", "onPreExecute*************************");
            dialog = new ProgressDialog(Volunteer_Emergency.this);
            dialog.setMessage("Please wait...");
            dialog.setCancelable(false);
            dialog.show();

        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            Log.e("FTP", "onPostExecute******************************");
            super.onPostExecute(result);
            if (photoUrl.length() != 0) {
                try {
                    JSONObject obj = new JSONObject();
                    if (message.getText().toString().length() == 0 || resimKontrol == false|| photoControl==false)
                        throw new Exception("");
                    try {

                        obj.put("type", afetBolgesi);
                        obj.put("latitude", latitude);
                        obj.put("longitude", longtitude);
                        obj.put("message", message.getText().toString());
                        obj.put("imageURL", photoUrl);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, obj, new Response.Listener<JSONObject>() {
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
                    dialog.dismiss();
                    Toast.makeText(Volunteer_Emergency.this, "Bildiriminiz iletilmiştir.", Toast.LENGTH_LONG).show();
                    finish();
                } catch (Exception e) {
                    if (resimKontrol == false|| photoControl==false)
                        Toast.makeText(Volunteer_Emergency.this, "Afet bölgesinin resmini yüklemelisiniz.", Toast.LENGTH_LONG).show();
                    else if (message.getText().toString().length() == 0)
                        Toast.makeText(Volunteer_Emergency.this, "Detaylı bilgi kısmını doldurunuz...", Toast.LENGTH_LONG).show();
                }

            } else {
                dialog.dismiss();
                Toast.makeText(Volunteer_Emergency.this, "Bildirim sırasında bir hata oluştu.Tekrar deneyiniz!", Toast.LENGTH_LONG).show();
            }

        }

    }


}