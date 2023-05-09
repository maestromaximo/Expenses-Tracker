package com.example.expensestracker;

import android.accessibilityservice.AccessibilityService;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

public class MyAccessibilityService extends AccessibilityService {
    private NotificationListener notificationListener;

    public void setNotificationListener(NotificationListener notificationListener) {
        this.notificationListener = notificationListener;
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        if (event.getEventType() == AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED) {
            String packageName = String.valueOf(event.getPackageName());
            if (packageName.equals("com.cibc.android.mobi")) { // Replace this with the package name of the CIBC Banking app
                AccessibilityNodeInfo nodeInfo = event.getSource();
                if (nodeInfo != null && notificationListener != null) {
                    String title = nodeInfo.getText().toString();
                    String text = event.getText().toString();
                    notificationListener.onNotificationReceived(title, text);
                }
            }
        }
    }

    @Override
    public void onInterrupt() {
    }
}
