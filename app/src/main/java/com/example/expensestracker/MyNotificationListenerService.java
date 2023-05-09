package com.example.expensestracker;
import android.app.Notification;
import android.content.Intent;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

public class MyNotificationListenerService extends NotificationListenerService {

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        // Called when a new notification is posted
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        // Called when a notification is removed
    }

    public void sendExistingNotifications() {
        StatusBarNotification[] notifications = getActiveNotifications();
        for (StatusBarNotification notification : notifications) {
            String packageName = notification.getPackageName();
            if (packageName.equals("com.cibc.android.mobi")) { // Replace this with the package name of the CIBC Banking app
                String title = notification.getNotification().extras.getString(Notification.EXTRA_TITLE);
                String text = notification.getNotification().extras.getString(Notification.EXTRA_TEXT);

                Intent intent = new Intent("com.example.expensestracker.NOTIFICATION_RECEIVED");
                intent.putExtra("title", title);
                intent.putExtra("text", text);
                LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
            }
        }
    }

}
