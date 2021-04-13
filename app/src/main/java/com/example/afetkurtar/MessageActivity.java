package com.example.afetkurtar;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class MessageActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    GoogleSignInClient mGoogleSignInClient;
    private String teamID = "";
    private String userID = "";
    private String senderName = "";
    private String messageData ="";
    private ScrollView sv;
    NotificationSender notificationSender;
    private String senderNameForReadMessage = "";
    private String timeForReadMessage ="";

    private ArrayList<String> messageList = new ArrayList<String>();
    private ArrayList<JSONObject> messageJsonObjectList = new ArrayList<JSONObject>();
    private ArrayList<JSONObject> newMessagesObjectList = new ArrayList<JSONObject>();

    private static boolean isUserMessageOwner; // this means if we send the mesaj data; it is true

    EditText et_messageData;

    JSONObject myUser;
    RequestQueue queue;
     BroadcastReceiver rec = new BroadcastReceiver() {
         @Override
         public void onReceive(Context context, Intent intent) {
             Bundle extras = intent.getExtras();
             System.out.println("************************** 0000000000000000000000000000 2222222222222222222222222222222222222222");
             String state = extras.getString("extra");

             refreshActivity();
         }
     };

    ArrayList<String> personnelStringList = new ArrayList<String>();
    ArrayList<JSONObject> jsonPersonnelObjectList = new ArrayList<JSONObject>();

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        queue = Volley.newRequestQueue(this);
        findViewById(R.id.btn_send).setOnClickListener(this::onClick);

        notificationSender = new NotificationSender(getApplicationContext());
        drawerLayout = findViewById(R.id.message_drawer_layout);

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.my.app.onMessageReceived");
        registerReceiver(rec, intentFilter);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        sv = ((ScrollView)findViewById(R.id.message_main_scroll));


        //////AFTER INITALIZE necessary variables etc///////////////////////

        getUserInfo(); // this method get userID and userName



        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            readMessage();// for Read oldest messages
        }
        scrollDownMethod();

    }//on create end

    @Override
    protected void onStop() {
        super.onStop();
        System.out.println("Unregister YAPTI");
       unregisterReceiver(rec);
    }
    public void refreshActivity(){  //BURADA GELEN BILDIRIM MESAJ EKRANINDA ISEK YENILENECEK
        handleNewMessages();
    }
    public void handleNewMessages(){

            JSONObject obj = new JSONObject();
            try {
                obj.put("teamID",teamID);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                    (Request.Method.POST, "https://afetkurtar.site/api/message/search.php", obj, new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                ArrayList<String> list = new ArrayList<String>();
                                    String cevap = response.getString("records");
                                    cevap = cevap.substring(1, cevap.length() - 1);

                                    while (cevap.indexOf(",{") > -1) {
                                        list.add(cevap.substring(0, cevap.indexOf(",{")));
                                        cevap = cevap.substring(cevap.indexOf(",{") + 1);
                                    }
                                    list.add(cevap);
                                ArrayList<JSONObject> tmpMessageList = new ArrayList<JSONObject>();
                                    for(String x : list){
                                        tmpMessageList.add(new JSONObject(x));
                                    }

                                    for(int x = messageJsonObjectList.size(); x < tmpMessageList.size(); x++){
                                        newMessagesObjectList.add(tmpMessageList.get(x));
                                    }

                                    for(int i = 0; i<newMessagesObjectList.size(); i++){
                                        if(!newMessagesObjectList.get(i).getString("userID").equals(MainActivity.userInfo.getString("userID"))){
                                            String tmpMessageData=newMessagesObjectList.get(i).getString("messageData").toString();
                                            String tmpSenderName=newMessagesObjectList.get(i).getString("messageName").toString();
                                            String tmpMessageTime=newMessagesObjectList.get(i).getString("messageTime").toString();
                                            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                            View view = inflater.inflate(R.layout.chat_item_left, null);
                                            LinearLayout scroll = findViewById(R.id.message_lay_scroll);
                                            TextView messageText = (TextView) view.findViewById(R.id.show_message_left);
                                            TextView messageSendTimeAndName = (TextView) view.findViewById(R.id.show_message_time_and_name_left);
                                            messageText.setText(tmpMessageData);
                                            messageSendTimeAndName.setText(tmpMessageTime +" " + tmpSenderName);// zaman kayması yüzünden gönderirken zamanı göndermicez
                                            scroll.addView(view);
                                            messageJsonObjectList.add(newMessagesObjectList.get(i));
                                        }else{
                                            messageJsonObjectList.add(newMessagesObjectList.get(i));
                                        }

                                    }


                            }catch (Exception e){
                                e.printStackTrace();
                            }
                            newMessagesObjectList.clear();
                            scrollDownMethod();
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

    public void viewMessageOnScreenSendMessage(){
                //if the message created by us
                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View view = inflater.inflate(R.layout.chat_item_right, null);
                LinearLayout scroll = findViewById(R.id.message_lay_scroll);
                TextView messageText = (TextView) view.findViewById(R.id.show_message_right);
                TextView messageSendTimeAndName = (TextView) view.findViewById(R.id.show_message_time_and_name_right);
                messageText.setText(messageData);
                messageSendTimeAndName.setText(senderName);// zaman kayması yüzünden gönderirken zamanı göndermicez
                scroll.addView(view);
    }
    public void viewMessageOnScreenOnRead(){
        for(int i = 0; i<messageJsonObjectList.size(); i++){
            try {
                messageData = messageJsonObjectList.get(i).getString("messageData").toString();
                senderNameForReadMessage = messageJsonObjectList.get(i).getString("messageName").toString();
                timeForReadMessage =  messageJsonObjectList.get(i).getString("messageTime").toString();
                if(userID.trim().equalsIgnoreCase(messageJsonObjectList.get(i).getString("userID").toString().trim())){
                    isUserMessageOwner = true;
                }else{
                    isUserMessageOwner = false;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            if(!isUserMessageOwner){
                // if the message created by another teamMember;
                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View view = inflater.inflate(R.layout.chat_item_left, null);
                LinearLayout scroll = findViewById(R.id.message_lay_scroll);
                TextView messageText = (TextView) view.findViewById(R.id.show_message_left);
                TextView messageSendTimeAndName = (TextView) view.findViewById(R.id.show_message_time_and_name_left);
                messageText.setText(messageData);
                messageSendTimeAndName.setText(timeForReadMessage + " " +senderNameForReadMessage);
                scroll.addView(view);
            }else{

                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View view = inflater.inflate(R.layout.chat_item_right, null);
                LinearLayout scroll = findViewById(R.id.message_lay_scroll);
                TextView messageText = (TextView) view.findViewById(R.id.show_message_right);
                TextView messageSendTimeAndName = (TextView) view.findViewById(R.id.show_message_time_and_name_right);
                messageText.setText(messageData);
                messageSendTimeAndName.setText(timeForReadMessage + " " +senderNameForReadMessage);
                scroll.addView(view);
            }

        }// for end
        scrollDownMethod();
    }
    public void scrollDownMethod(){
        sv.postDelayed(new Runnable() {
            @Override
            public void run() {
                sv.fullScroll(ScrollView.FOCUS_DOWN);
            }
        }, 600);
    }


    public void getUserInfo(){
        try {
            this.userID = MainActivity.userInfo.getString("userID");
            this.senderName = MainActivity.userInfo.getString("userName");
            this.teamID = Personel_Anasayfa.PersonelInfo.getString("teamID");

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void sendMessage(){
        isUserMessageOwner = true; //we send the message otherwise false;

        //Get mesaj data
        try{
            messageData = et_messageData.getText().toString();
        }catch (Exception e){
            e.printStackTrace();
        }
        sendMessageDataToDB();
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_send:
                if(!isTextEmpty()){
                    sendMessage();// send message data base and in this method, some variables initialize.
                    scrollDownMethod();
                    notificationSender.sendToTeam(teamID,messageData);
                    ((EditText)(findViewById(R.id.text_send))).setText("");
                    break;
                }
        }
    }
    public boolean isTextEmpty(){
        try{
            et_messageData = (EditText)findViewById(R.id.text_send);
            if(et_messageData.getText().toString().equals("") || et_messageData == null){
                Toast.makeText(this, "Boş mesaj gönderilemez!", Toast.LENGTH_SHORT).show();
                return true;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    public void sendMessageDataToDB(){
        JSONObject obj = new JSONObject();
        try {
            //obj.put("messageID", "0");
            obj.put("teamID", teamID);
            obj.put("userID", userID);
            obj.put("messageData", messageData);
            obj.put("messageName", senderName);
            //obj.put("messageTime", messageTime);
        } catch (Exception e) {
            e.getMessage();
        }
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, "https://afetkurtar.site/api/message/create.php", obj, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //System.out.println(response);
                viewMessageOnScreenSendMessage();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error);
            }
        });
        queue.add(request);
        Toast.makeText(this, "Mesaj gönderildi.", Toast.LENGTH_LONG).show();
        //finish();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void readMessage(){
        getMessageDataFromDB(teamID);
    }
    public void getMessageDataFromDB(String teamID){
        JSONObject obj = new JSONObject();
        try {
                obj.put("teamID",teamID);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, "https://afetkurtar.site/api/message/search.php", obj, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            handleResponseGetMessageDataFromDB(response);
                            viewMessageOnScreenOnRead();

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
    public void handleResponseGetMessageDataFromDB(JSONObject a) {
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
                        messageJsonObjectList.add(tmp);
                   //     messageJsonObjectList = getMessageListToCompareTime(messageJsonObjectList);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }// handle response end
    /////////////////////////////////////////////////burasi ve altındakiler drawer işlemleri için ancak drawer da bir kata mevcut onu sonra düzelt
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
    public static void redirectActivity(Activity activity, Class aClass) {
        //initialize intent
        Intent intent = new Intent(activity, aClass);
        //Set flag
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //Start activity
        activity.startActivity(intent);
    }
    /*
     *************************************** ASAGIDAKI KISIMLAR YONLENDIRMELERI AYARLAR
     */

    public void ClickTeamManagement(View view) {
       // Yetki = "kaptan";  //*************************************************************************** TEST AMACLI KALDIRILACAK
        if(Personel_Anasayfa.Yetki.equalsIgnoreCase("kaptan"))
            redirectActivity(this, Personel_Progress.class);
        else
            Toast.makeText(getApplicationContext(), "Gerekli Yetkiye Sahip Değilsiniz", Toast.LENGTH_LONG).show();
    }

    public void ClickPersonelNotification(View view) {
        //  redirectActivity(this, Authorized_Notification.class);
    }

    public void ClickPersonelInfo(View view) {
        redirectActivity(this, Personel_Information.class);
    }

    public void ClickPersonelArea(View view) {
        redirectActivity(this, Team_Member_Locations.class);
    }
    public void ClickPersonelMessage(View view) {
        redirectActivity(this, MessageActivity.class);
    }

    // CIKIS
    public void ClickPersonelExit(View view) {
        signOut();
        redirectActivity(this, MainActivity.class );
    }
    // ANA SAYFA
    public void ClickPersonelAnasayfa(View view) {
        redirectActivity(this, Personel_Anasayfa.class );
    }

}// class end
