package es.uji.geotec.wearossensors.intent;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;

import es.uji.geotec.wearossensors.messaging.ResultMessagingProtocol;

public class IntentManager {
    private static final int PENDING_INTENT_RC = 50;

    private IntentManager() {
    }

    public static PendingIntent pendingIntentFromPermissionsToRequest(
            Context context,
            Class<?> permissionsActivity,
            ArrayList<String> permissions
    ) {
        Intent permissionRequester = new IntentBuilder()
                .setContext(context.getApplicationContext(), permissionsActivity)
                .setPermissionsToRequest(permissions)
                .build();

        return PendingIntent.getActivity(
                context,
                PENDING_INTENT_RC,
                permissionRequester,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );
    }

    public static PendingIntent pendingIntentFromPermissionsToRequest(
            Context context,
            Class<?> permissionsActivity,
            ArrayList<String> permissions,
            String sourceNodeId,
            ResultMessagingProtocol resultProtocol
    ) {
        Intent permissionRequester = new IntentBuilder()
                .setContext(context.getApplicationContext(), permissionsActivity)
                .setPermissionsToRequest(permissions)
                .setRemoteNodeInfo(sourceNodeId, resultProtocol)
                .build();

        return PendingIntent.getActivity(
                context,
                PENDING_INTENT_RC,
                permissionRequester,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );
    }

    public static ArrayList<String> permissionsFromIntent(Intent intent) {
        return intent.getStringArrayListExtra(IntentBuilder.PERMISSIONS_EXTRAS);
    }

    public static ArrayList<String> specialPermissionsFromIntent(Intent intent) {
        return intent.getStringArrayListExtra(IntentBuilder.PERMISSIONS_EXTRAS_SPECIAL);
    }

    public static boolean isRemoteRequest(Intent intent) {
        return sourceNodeIdFromIntent(intent) != null;
    }

    public static String sourceNodeIdFromIntent(Intent intent) {
        return intent.getStringExtra(IntentBuilder.NODE);
    }

    public static ResultMessagingProtocol resultProtocolFromIntent(Intent intent) {
        return (ResultMessagingProtocol) intent.getSerializableExtra(IntentBuilder.PROTOCOL);
    }
}
