package es.uji.geotec.wearossensors.intent;

import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;

import es.uji.geotec.wearossensors.messaging.ResultMessagingProtocol;

public class IntentBuilder {

    public static final String PERMISSIONS_EXTRAS = "PERMISSIONS";
    public static final String PERMISSIONS_EXTRAS_SPECIAL = "PERMISSIONS_SPECIAL";
    public static final String NODE = "NODE";
    public static final String PROTOCOL = "PROTOCOL";

    private Intent intent;

    public IntentBuilder() {
        this.intent = new Intent();
    }

    public IntentBuilder setContext(Context context, Class<?> targetActivity) {
        this.intent.setClass(context, targetActivity);
        this.intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        return this;
    }

    public IntentBuilder setPermissionsToRequest(ArrayList<String> permissions) {
        ArrayList<String> normalPermissions = new ArrayList<>();
        ArrayList<String> specialPermissions = new ArrayList<>();
        for (String permission : permissions) {
            if (permission.contains("BACKGROUND")) {
                specialPermissions.add(permission);
            } else {
                normalPermissions.add(permission);
            }
        }

        this.intent.putStringArrayListExtra(PERMISSIONS_EXTRAS, normalPermissions);
        this.intent.putStringArrayListExtra(PERMISSIONS_EXTRAS_SPECIAL, specialPermissions);
        return this;
    }

    public IntentBuilder setRemoteNodeInfo(String sourceNodeId, ResultMessagingProtocol resultProtocol) {
        this.intent.putExtra(NODE, sourceNodeId);
        this.intent.putExtra(PROTOCOL, resultProtocol);
        return this;
    }

    public Intent build() {
        return this.intent;
    }
}
