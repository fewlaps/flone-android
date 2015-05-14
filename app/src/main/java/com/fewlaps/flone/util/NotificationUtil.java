package com.fewlaps.flone.util;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;

import com.fewlaps.flone.R;
import com.fewlaps.flone.view.activity.FlyActivity;

/**
 * @author Roc Boronat (roc@fewlaps.com)
 * @date 15/02/2015
 */
public class NotificationUtil {
    public static final int KEY_FOREGROUND_NOTIFICATION = 42;

    public static Notification getForegroundServiceNotification(Context context) {
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, new Intent(context, FlyActivity.class), 0);

        String message = context.getString(R.string.notification_message);
        Bitmap largeIcon = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launcher);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_launcher)
                .setLargeIcon(largeIcon)
                .setContentTitle(context.getString(R.string.app_name))
                .setContentText(message)
                .setContentIntent(contentIntent);

        return builder.build();
    }
}
