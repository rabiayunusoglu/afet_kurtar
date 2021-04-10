package com.example.afetkurtar;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import java.util.Calendar;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class GetLocationService extends Service {
    private String channelID = "channelID1";
    private Timer timer = new Timer();
    private TimerTask timerTask;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        createNotificationChannel();
        Intent intent1 = new Intent(this,MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent1, 0);

        Notification notification = new NotificationCompat.Builder(this, channelID)
                .setContentTitle("Get Location Service")
                .setContentText("Get Location Service is running")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(pendingIntent).build();

        startForeground(1,notification);
        System.out.println("********DDDFFF****************************************************************");
        System.out.println(notification.toString());
        System.out.println("********DDDFFF****************************************************************");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            updateLocationsInMinutes(1,5);
        }
        return START_STICKY; // start sticky because we dont want to service end
    }

    private void createNotificationChannel(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel notificationChannel = new NotificationChannel(
                    channelID,"Foreground notification", NotificationManager.IMPORTANCE_DEFAULT);

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(notificationChannel);
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void updateLocationsInMinutes(int specifiedMinute, int checkInHowManySeconds) {
        //int minuteInMiliseconds = specifiedMinute*60*1000;
        timerTask = new TimerTask() {
            @Override
            public void run() {

                Locale locale = new Locale("tr", "TR");
                //if (Calendar.getInstance(locale).getTime().getMinutes() % specifiedMinute == 0) {
                    System.out.println("In SERVICEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE");
                    System.out.println("InService Get Minute : " + Calendar.getInstance(locale).getTime().getMinutes());

                    //checkUserIsVolunteerOrPersonnel();
                    // this method find the personnel in personnel table and after, update the new location of that personnel

                //}

            }
        };
        // 1(one) second 1000 milisecond, period time type is milisecond
        timer.schedule(timerTask, 0, 1000*checkInHowManySeconds);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        stopForeground(true); // true if we want to remove notification
        stopSelf();
        super.onDestroy();
    }
}
