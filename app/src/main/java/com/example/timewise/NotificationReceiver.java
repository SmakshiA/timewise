package com.example.timewise;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

public class NotificationReceiver extends BroadcastReceiver {
    String channelId = "channel_id"; // Replace with your own channel ID
    public void onReceive(Context context, Intent intent) {
        int hour = intent.getIntExtra("hour", -1);
        int minute = intent.getIntExtra("minute", -1);
        String med=intent.getStringExtra("medn");
        if (hour != -1 && minute != -1) {
            Log.d("NotificationReceiver", "Received notification for " + hour + ":" + minute);
            showNotification(context, hour, minute,med);
        }
    }


    @SuppressLint("NotificationPermission")
    private void showNotification(Context context, int hour, int minute, String medicine) {
        String notificationTitle = "Medicine Reminder";
        String notificationText = "Time to take your "+medicine +" medicine at "+ hour + ":" + minute;

        // Create a notification intent that opens your app when the notification is tapped
        Intent notificationIntent = new Intent(context, MedicineR.class); // Replace with your app's main activity

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create the channel for Android Oreo (API 26) and higher
            CharSequence channelName = "MyNotificationChannel"; // Replace with your own channel name
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(channelId, channelName, importance);
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

        // Create the notification
        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        int smallIcon = android.R.drawable.ic_dialog_info;
        Notification notification = new NotificationCompat.Builder(context, "channel_id")
                .setSmallIcon(smallIcon)
                .setContentTitle(notificationTitle)
                .setContentText(notificationText)
                .setSound(soundUri)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .build();

        // Show the notification
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, notification);
    }

}
