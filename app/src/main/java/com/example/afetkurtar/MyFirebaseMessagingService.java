package com.example.afetkurtar;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;


import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Random;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    /*
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        showNotification(remoteMessage.getData().get("message")); // Mesaj içeriği alınıp bildirim gösteren metod çağırılıyor
    }

    private void showNotification(String message) {

        Intent i = new Intent(this, MainActivity.class); // Bildirime basıldığında hangi aktiviteye gidilecekse
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,i,PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                .setAutoCancel(true) // Kullanıcı bildirime girdiğinde otomatik olarak silinsin. False derseniz bildirim kalıcı olur.
                .setContentTitle("FCM Test") // Bildirim başlığı
                .setContentText(message) // Bildirim mesajı
                .setSmallIcon(R.drawable.ic_info) // Bildirim simgesi
                .setContentIntent(pendingIntent);

        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        manager.notify(0,builder.build());
    }

     */

    private static final String TAG = "MyFirebaseMsgService";

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    // [START receive_message]
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        /*
       ********************* NOT : BU KISIMA ANDROID UYGULAMASI FOREGROUND DA ISE GELIYOR. BACKGROUNDDA DEFAULT OLARAK CALISIR BURAYA GELMEZ
        Asagida Data ve notification kisimlarina göre davranislarini not aldim.
         */
        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: " + remoteMessage.getFrom());
        // Check if message contains a data payload.
        /*
            Data Ornegi:
                    {
 "to" : "faek0BAUT26VTjq62mBs_v:APA91bEU0thQI4tGZ7hlt3HDYelRIlWTGYXswjsHBXfV3VVn2TLQyiI1vvjWNNG0VetwaUcWyzOjpV5x4uqObT45JxhGN0xqX_mPiO79hYgwuX5dKNvw7Ylxm93e0c5Jf9sEz1krK5iC",
 "notification" : {
     "body" : "Body of Your Notification",
     "title": "Title of Your Notification"
 },
 "data" : {
     "body" : "Body of Your Notification in Data",
     "title": "Title of Your Notification in Title",
     "patates":"Bu da patates datası"

 }
}
         */
        System.out.println( "Message data payload: " + remoteMessage.getData());

        if (remoteMessage.getData().size() > 0) {  // Eger Data varsa ( sadece notification degil)
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
            String a =remoteMessage.getData().get("title"); //Data içinden istedigin bolumu almak icin get
            String b  =remoteMessage.getData().get("body"); //Data içinden istedigin bolumu almak icin get
            showNotification(remoteMessage); // Foreground icin notification oluşturulan yer
                                                                             // Detayli test ve islevler ileride yapilacak

        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
            // Burayı else if e alip Bunu da sendNotification a gonder. Yoksa bildirin cikmaz gibi (Test Edilmedi)
        }
        System.out.println(" ");
        System.out.println("remoteMessage.getCollapseKey(): " + remoteMessage.getCollapseKey());
        System.out.println("remoteMessage.getData(): "+remoteMessage.getData());
        System.out.println("remoteMessage.getFrom(): " + remoteMessage.getFrom());
        System.out.println("remoteMessage.getMessageId(): " + remoteMessage.getMessageId());
        System.out.println("remoteMessage.getMessageType(): " + remoteMessage.getMessageType());
        System.out.println("remoteMessage.getNotification(): " + remoteMessage.getNotification());
        System.out.println("remoteMessage.getOriginalPriority(): " + remoteMessage.getOriginalPriority());
        System.out.println("remoteMessage.getRawData(): " + remoteMessage.getRawData());
        System.out.println("remoteMessage.getSenderId(): " + remoteMessage.getSenderId());
        System.out.println("remoteMessage.getSentTime(): " + remoteMessage.getSentTime());
        System.out.println("remoteMessage.getTo(): " + remoteMessage.getTo());
        System.out.println("remoteMessage.getTtl(): " + remoteMessage.getTtl());
        System.out.println("remoteMessage.describeContents(): " + remoteMessage.describeContents());
        System.out.println(" ");

    }
    // [END receive_message]

    /*
     * Create and show a simple notification containing the received FCM message.
     *
     * @param messageBody FCM message body received.
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void showNotification(RemoteMessage remoteMessage) {
        /*
        *********** Channel Oluşturup o channel üzerinden foreground notification gonderiyor.
        *   Channel i birden fazla kere oluşturmasi sikinti yaratabilir Test Edilmedi.
        *   Sorun çıkar ise channel yaratma isini bir kere yapip o kanali kullanmaya calis.
         */

        Random Rand = new Random();
        int NOTIFICATION_ID = Rand.nextInt(9999);
        NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            String CHANNEL_ID = "my_channel_01";
            CharSequence name = "my_channel";
            String Description = "This is my channel";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
            mChannel.setDescription(Description);
            mChannel.enableLights(true);
            mChannel.setLightColor(Color.RED);
            mChannel.enableVibration(true);
            mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            mChannel.setShowBadge(false);
            notificationManager.createNotificationChannel(mChannel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "my_channel_01")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(remoteMessage.getNotification().getTitle())
                .setContentText(remoteMessage.getNotification().getBody());

        Intent resultIntent = new Intent(this, MainActivity.class);
        resultIntent.putExtra("title",remoteMessage.getData().get("title"));
        resultIntent.putExtra("body",remoteMessage.getData().get("body"));
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(resultPendingIntent);
        notificationManager.notify(NOTIFICATION_ID, builder.build());

    }
}