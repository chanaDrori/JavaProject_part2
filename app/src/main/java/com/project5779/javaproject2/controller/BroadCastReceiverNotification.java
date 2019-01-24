package com.project5779.javaproject2.controller;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.widget.Toast;

import com.project5779.javaproject2.R;

public class BroadCastReceiverNotification extends BroadcastReceiver{

    @Override
    public void onReceive(Context context, Intent intent) {
        try {

            NotificationManager notificationManager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
            Intent intent1 = new Intent(context, Nav_drawer.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent1, PendingIntent.FLAG_UPDATE_CURRENT);

            Notification.Builder myBuilder = new Notification.Builder(context);
            myBuilder.setSmallIcon(R.mipmap.taxi_background);
            myBuilder.setContentTitle(context.getString(R.string.New_drive_received));
            myBuilder.setContentText(context.getString(R.string.notification_text_new_drive));
            myBuilder.setContentIntent(pendingIntent);

            notificationManager.notify(1, myBuilder.build());
            Toast.makeText(context, "", Toast.LENGTH_LONG).show();

        } catch (Exception ex) {
            Toast.makeText(context, ex.toString(), Toast.LENGTH_LONG).show();
        }

    }
}
