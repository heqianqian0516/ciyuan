package com.ciyuanplus.mobile.server;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;

import com.ciyuanplus.mobile.R;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Created by Alen on 2017/7/28.
 */

public class BadgeIntentService extends IntentService {

    private int notificationId = 0;
    private NotificationManager mNotificationManager;

    public BadgeIntentService() {
        super("BadgeIntentService");
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            int badgeCount = intent.getIntExtra("badgeCount", 0);

            mNotificationManager.cancel(notificationId);
            notificationId++;

            Notification.Builder builder = new Notification.Builder(getApplicationContext())
                    .setContentTitle("")
                    .setContentText("")
                    .setSmallIcon(R.mipmap.ic_launcher);
            Notification notification = builder.build();
            try {
                Field field = notification.getClass().getDeclaredField("extraNotification");
                Object extraNotification = field.get(notification);
                Method method = extraNotification.getClass().getDeclaredMethod("setMessageCount", int.class);
                method.invoke(extraNotification, badgeCount);
            } catch (Exception e) {
                e.printStackTrace();
            }
            //mNotificationManager.notify(notificationId, notification);
        }
    }
}
