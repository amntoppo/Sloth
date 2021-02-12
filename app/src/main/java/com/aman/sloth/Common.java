package com.aman.sloth;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;

import com.aman.sloth.Model.CustomerInfoModel;
import com.aman.sloth.Services.FirebaseMessagingService;

import androidx.core.app.NotificationCompat;

public class Common {
    public static final String CUSTOMER_INFO_REFERENCE = "CustomerInfo";
    public static final String TOKEN_REFERENCE = "UserToken";
    public static final String NOTIFICATION_TITLE = "title";
    public static final String NOTIFICATION_CONTENT = "content";
    public static CustomerInfoModel currentCustomer;
    public static final String[] text = {"", "Feeling \nlazy?", "Get anything \n delivered", "Even \n     yourself!"};
    public int ID_MAP = 1;
    public int ID_HOME = 2;
    public int ID_SETTINGS = 3;

    public static void showNotification(Context context, int id, String title, String body, Intent intent) {
        PendingIntent pendingIntent = null;
        if (intent != null)
            pendingIntent = PendingIntent.getActivity(context, id, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        String NOTIFICATION_CHANNEL_ID = "sloth_channel";
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "Sloth", NotificationManager.IMPORTANCE_DEFAULT);
            notificationChannel.setDescription("Sloth description");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
            notificationChannel.enableVibration(true);

            notificationManager.createNotificationChannel(notificationChannel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID);
        builder.setContentTitle(title)
                .setContentText(body)
                .setAutoCancel(false)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setSmallIcon(R.drawable.bike_icon)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.bike_icon));
        if(pendingIntent != null) {
            builder.setContentIntent(pendingIntent);
        }
        Notification notification = builder.build();
        notificationManager.notify(id, notification);

    }
}
