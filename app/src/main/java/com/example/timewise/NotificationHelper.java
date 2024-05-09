package com.example.timewise;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.SystemClock;

public class NotificationHelper {

    public static void scheduleNotifications(Context context, String medicineName, int hour, int minute) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "MyNotificationChannel"; // Channel name
            String description = "Channel Description"; // Channel description
            int importance = NotificationManager.IMPORTANCE_DEFAULT; // Importance level

            NotificationChannel channel = new NotificationChannel("channel_id", name, importance);
            channel.setDescription(description);

            // Register the channel with the system; you can't change the importance or other notification behaviors after this
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }


        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        // Create an Intent to be triggered when the notification time is reached
        Intent notificationIntent = new Intent(context, NotificationReceiver.class);
        notificationIntent.putExtra("medicineName", medicineName); // Pass medicine name as extra data

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        long triggerTime = getTriggerTime(hour, minute);
        // Schedule the notification
        alarmManager.set(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent);
    }

    private static long getTriggerTime(int hour, int minute) {
        // Calculate the trigger time based on the specified hour and minute
        long now = System.currentTimeMillis();
        long today = now - (now % (24 * 60 * 60 * 1000)); // Midnight today
        long triggerTime = today + (hour * 60 * 60 * 1000) + (minute * 60 * 1000);
        if (triggerTime < now) {
            triggerTime += 24 * 60 * 60 * 1000; // If the time is in the past, schedule it for the next day
        }
        return triggerTime;
    }
}

