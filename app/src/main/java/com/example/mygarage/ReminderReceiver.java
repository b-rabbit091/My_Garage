package com.example.mygarage;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class ReminderReceiver extends BroadcastReceiver {

    private static final String CHANNEL_ID = "mygarage_reminders";
    private static final String GROUP_KEY_REMINDERS = "mygarage_group_reminders";

    @Override
    public void onReceive(Context context, Intent intent) {
        String title = intent.getStringExtra("title");
        int notificationId = intent.getIntExtra("notification_id", 0);

        if (title == null || title.trim().isEmpty()) {
            title = "Vehicle reminder";
        }

        NotificationManager nm =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (nm == null) return;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "My Garage Reminders",
                    NotificationManager.IMPORTANCE_HIGH
            );
            nm.createNotificationChannel(channel);
        }

        Intent openIntent = new Intent(context, MainActivity.class);
        openIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pi = PendingIntent.getActivity(
                context,
                notificationId,
                openIntent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        NotificationCompat.Builder notification =
                new NotificationCompat.Builder(context, CHANNEL_ID)
                        .setSmallIcon(R.drawable.mygarage_icon)
                        .setContentTitle("My Garage Reminder")
                        .setContentText(title)
                        .setGroup(GROUP_KEY_REMINDERS)
                        .setAutoCancel(true)
                        .setContentIntent(pi)
                        .setPriority(NotificationCompat.PRIORITY_HIGH);

        NotificationCompat.Builder summary =
                new NotificationCompat.Builder(context, CHANNEL_ID)
                        .setSmallIcon(R.drawable.mygarage_icon)
                        .setStyle(new NotificationCompat.InboxStyle()
                                .setSummaryText("Multiple reminders"))
                        .setGroup(GROUP_KEY_REMINDERS)
                        .setGroupSummary(true)
                        .setPriority(NotificationCompat.PRIORITY_LOW);

        NotificationManagerCompat.from(context).notify(notificationId, notification.build());
        NotificationManagerCompat.from(context).notify(999999, summary.build());
    }
}

