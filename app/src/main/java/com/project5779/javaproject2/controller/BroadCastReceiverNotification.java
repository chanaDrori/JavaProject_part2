/**
 * Project in java-Android part 2
 * writers: Tirtza Rubinstain and Chana Drori
 * 01/2019
 * BroadCast Receiver get the intent from the service
 * when there is a new drive and show notification
 */
package com.project5779.javaproject2.controller;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.project5779.javaproject2.R;

public class BroadCastReceiverNotification extends BroadcastReceiver{
    /**
     * onReceive display notification on status bar
     * @param context Context
     * @param intent Intent
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            String NOTIFICATION_CHANNEL_ID = "my_channel_id_01";
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
                    new Intent(context, LoginActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                @SuppressLint("WrongConstant")
                NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID,
                        "My Notifications", NotificationManager.IMPORTANCE_MAX);

                notificationChannel.setDescription("Notification alert when add new drive");
                notificationChannel.enableLights(true);
                notificationChannel.setLightColor(Color.CYAN);
                notificationChannel.setVibrationPattern(new long[]{0, 300, 100, 300});
                notificationManager.createNotificationChannel(notificationChannel);
            }
            NotificationCompat.Builder myBuilder = new NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID);
            myBuilder.setSmallIcon(R.mipmap.taxi_background)
                    .setContentTitle(context.getString(R.string.New_drive_received))
                    .setContentText(context.getString(R.string.notification_text_new_drive))
                    .setContentIntent(pendingIntent)
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_SOUND)
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(context.getString(R.string.big_text)))
                    .setShowWhen(true)
                    .setAutoCancel(true);

            if (notificationManager != null) {
                notificationManager.notify(1, myBuilder.build());
            }
        } catch (Exception ex) {
            Toast.makeText(context, ex.toString(), Toast.LENGTH_LONG).show();
        }
    }
}
