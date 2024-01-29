package es.uji.geotec.wearossensors.permissions.handler;

import android.app.Activity;
import android.content.pm.PackageManager;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import es.uji.geotec.wearossensors.intent.IntentManager;
import es.uji.geotec.wearossensors.permissions.PermissionsManager;
import es.uji.geotec.wearossensors.permissions.PermissionsResultClient;

public class PermissionsRequestHandler {

    private Activity activity;
    private Timer delayer = new Timer();
    private ArrayList<String> permissions;
    private ArrayList<String> specialPermissions;
    private boolean specialPermissionsRequested = false;

    private PermissionsResult permissionsResult;

    public PermissionsRequestHandler(Activity activity) {
        this.activity = activity;
    }

    public void handleRequest() {
        delayer.schedule(new TimerTask() {
            @Override
            public void run() {
            permissions = PermissionsManager.permissionsToRequestFromIntent(activity.getIntent());
            specialPermissions = PermissionsManager.specialPermissionsToRequestFromIntent(activity.getIntent());

            if (permissions.size() != 0) {
                PermissionsManager.requestPermissions(activity, permissions);
            } else if (specialPermissions.size() != 0) {
                specialPermissionsRequested = true;
                PermissionsManager.requestPermissions(activity, specialPermissions);
            } else {
                finishDeferred();
            }
            }
        }, 500);
    }

    public void handleResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode != PermissionsManager.PERMISSIONS_RC) {
            return;
        }

        ArrayList<String> permissionsRejected = new ArrayList<>();
        for (int i = 0; i < grantResults.length; i++) {
            if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                permissionsRejected.add(permissions[i]);
            }
        }

        if (permissionsRejected.size() != 0) {
            requestFailed(permissionsRejected);
            return;
        }

        if (!specialPermissionsRequested && specialPermissions.size() != 0) {
            Timer delayer = new Timer();
            delayer.schedule(new TimerTask() {
                @Override
                public void run() {
                    specialPermissionsRequested = true;
                    PermissionsManager.requestPermissions(activity, specialPermissions);
                }
            }, 1000);
            return;
        }

        requestSucceeded();
    }

    public void onPermissionsResult(PermissionsResult permissionsResult) {
        this.permissionsResult = permissionsResult;
    }

    private void requestSucceeded() {
        PermissionsResultClient permissionsResultClient = new PermissionsResultClient(activity);
        boolean remoteRequest = IntentManager.isRemoteRequest(activity.getIntent());
        if (remoteRequest) {
            permissionsResultClient.sendPermissionsSuccessfulResponse(activity.getIntent());
        }

        permissionsResult.onPermissionsRequestResult(true);
        finishDeferred();
    }

    private void requestFailed(ArrayList<String> permissionsRejected) {
        PermissionsResultClient permissionsResultClient = new PermissionsResultClient(activity);
        boolean remoteRequest = IntentManager.isRemoteRequest(activity.getIntent());
        if (remoteRequest) {
            String failureMessage = buildFailureMessage(permissionsRejected);
            permissionsResultClient.sendPermissionsFailureResponse(activity.getIntent(), failureMessage);
        }

        permissionsResult.onPermissionsRequestResult(false);
        finishDeferred();
    }

    private String buildFailureMessage(ArrayList<String> failures) {
        StringBuilder sb = new StringBuilder();
        sb.append("Permissions rejected: ");
        String separator = " ";
        for (String failure : failures) {
            sb.append(separator);
            separator = ", ";
            sb.append(failure);
        }

        return sb.toString();
    }

    private void finishDeferred() {
        Timer delayer = new Timer();
        delayer.schedule(new TimerTask() {
            @Override
            public void run() {
                activity.finish();
            }
        }, 1000);
    }
}
