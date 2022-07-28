package es.uji.geotec.wearossensors.notifications;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.util.ArrayList;

import es.uji.geotec.wearossensors.R;
import es.uji.geotec.wearossensors.intent.IntentManager;
import es.uji.geotec.wearossensors.messaging.ResultMessagingProtocol;

public class NotificationProvider {

    private static final String REQUEST_PERMISSIONS_CHANNEL = "REQUEST_PERMISSIONS_CHANNEL";
    private static final int REQUEST_PERMISSIONS_DESCRIPTION = R.string.request_permissions_channel_description;
    private static final int REQUEST_PERMISSIONS_NOTIFICATION_ID = 23;

    private Context context;
    private NotificationManagerCompat notificationManager;

    public NotificationProvider(Context context) {
        this.context = context;
        this.notificationManager = NotificationManagerCompat.from(context);
    }

    public void createNotificationForPermissions(
            ArrayList<String> permissions,
            Class<?> permissionsActivity,
            String sourceNodeId,
            ResultMessagingProtocol resultProtocol
    ) {
        setupNotificationChannelIfNeeded(
                REQUEST_PERMISSIONS_CHANNEL,
                context.getString(REQUEST_PERMISSIONS_DESCRIPTION)
        );

        PendingIntent pendingIntent = IntentManager.pendingIntentFromPermissionsToRequest(
                context,
                permissionsActivity,
                permissions,
                sourceNodeId,
                resultProtocol
        );

        Notification notification = buildNotification(
                REQUEST_PERMISSIONS_CHANNEL,
                context.getString(R.string.permissions_notification_title),
                context.getString(R.string.permissions_notification_text),
                pendingIntent
        );

        notificationManager.notify(REQUEST_PERMISSIONS_NOTIFICATION_ID, notification);
    }

    private void setupNotificationChannelIfNeeded(String id, String name) {
        if (notificationManager.getNotificationChannel(id) != null) {
            return;
        }

        NotificationChannel channel = new NotificationChannel(
                id,
                name,
                NotificationManager.IMPORTANCE_HIGH
        );

        channel.enableVibration(true);

        notificationManager.createNotificationChannel(channel);
    }

    private Notification buildNotification(
            String channelId,
            String title,
            String text,
            PendingIntent pendingIntent
    ) {
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(context, channelId)
                        .setContentTitle(title)
                        .setContentText(text)
                        .setSmallIcon(R.drawable.ic_watch)
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000});

        if (pendingIntent != null) {
            notificationBuilder.setContentIntent(pendingIntent);
            notificationBuilder.setAutoCancel(true);
        }

        return notificationBuilder.build();
    }
}
