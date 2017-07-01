package com.example.brolin.pinjamreminder;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.view.View;
import android.widget.Button;
import android.content.Intent;

import java.security.Permission;
import java.security.Permissions;
import java.util.GregorianCalendar;
import java.util.jar.Manifest;

public class MainActivity extends AppCompatActivity {

    Button showNotificationBut, stopNotificationBut, alertButton;

    NotificationManager notificationManager;

    boolean isNotificActive = false;
    public static int REQUEST_RECEIVE_BOOT=1001;

    int notifID = 33;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkPermission();
        showNotificationBut =  (Button) findViewById(R.id.ShowNotificationBut);
        stopNotificationBut =  (Button) findViewById(R.id.StopNotificationBut);
        alertButton =  (Button) findViewById(R.id.alertButton);
    }

    public void showNotification(View view){

        NotificationCompat.Builder notificBuilder = new
                NotificationCompat.Builder(this)
                .setContentTitle("Message")
                .setContentText("New Message")
                .setTicker("Alert New Message")
                .setSmallIcon(R.drawable.androiddicon);

        Intent moreInfoIntent = new Intent(this, MoreInfoNotification.class);

        TaskStackBuilder tStackBuilder = TaskStackBuilder.create(this);

        tStackBuilder.addParentStack(MoreInfoNotification.class);

        tStackBuilder.addNextIntent(moreInfoIntent);

        PendingIntent pendingIntent = tStackBuilder.getPendingIntent(0,
                PendingIntent.FLAG_UPDATE_CURRENT);

        notificBuilder.setContentIntent(pendingIntent);

        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(notifID, notificBuilder.build());

        isNotificActive = true;

    }

    public void stopNotification(View view){

        if(isNotificActive){

            notificationManager.cancel(notifID);
        }

    }

    public void setAlarm(View view){

        Long alertTime = new GregorianCalendar().getTimeInMillis()+10*1000;

        Intent alertIntent = new Intent(this, AlertReceiver.class);

        AlarmManager alarmManager = (AlarmManager)
                getSystemService(Context.ALARM_SERVICE);

        alarmManager.set(AlarmManager.RTC_WAKEUP, alertTime,
                PendingIntent.getBroadcast(this, 1, alertIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT));


    }
    private void checkPermission(){
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            int a=checkSelfPermission(android.Manifest.permission.RECEIVE_BOOT_COMPLETED);
            if(a!= PackageManager.PERMISSION_GRANTED){
                //teruskan
                ActivityCompat.requestPermissions(MainActivity.this,new String[]{android.Manifest.permission.RECEIVE_BOOT_COMPLETED},REQUEST_RECEIVE_BOOT);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode==REQUEST_RECEIVE_BOOT){

        }
    }
}
