package es.uji.geotec.wearossensors.intent;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;

import es.uji.geotec.wearossensors.messaging.ResultMessagingProtocol;

public class IntentManager {

    private static final int PENDING_INTENT_RC = 50;

    private static final String PERMISSIONS_EXTRAS = "PERMISSIONS";
    private static final String NODE = "NODE";
    private static final String PROTOCOL = "PROTOCOL";

    private IntentManager() {
    }

    public static PendingIntent pendingIntentFromPermissionsToRequest(
            Context context,
            Class<?> permissionsActivity,
            ArrayList<String> permissions,
            String sourceNodeId,
            ResultMessagingProtocol resultProtocol
    ) {
        Intent permissionRequester = new Intent(context.getApplicationContext(), permissionsActivity);
        permissionRequester.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        permissionRequester.putStringArrayListExtra(PERMISSIONS_EXTRAS, permissions);
        permissionRequester.putExtra(NODE, sourceNodeId);
        permissionRequester.putExtra(PROTOCOL, resultProtocol);

        return PendingIntent.getActivity(
                context,
                PENDING_INTENT_RC,
                permissionRequester,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );
    }

    public static ArrayList<String> permissionsToRequestFromIntent(Intent intent) {
        return intent.getStringArrayListExtra(PERMISSIONS_EXTRAS);
    }

    public static String sourceNodeIdFromIntent(Intent intent) {
        return intent.getStringExtra(NODE);
    }

    public static ResultMessagingProtocol resultProtocolFromIntent(Intent intent) {
        return (ResultMessagingProtocol) intent.getSerializableExtra(PROTOCOL);
    }
}
