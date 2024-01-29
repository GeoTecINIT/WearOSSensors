package es.uji.geotec.wearossensors.permissions;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;

import java.util.ArrayList;
import java.util.Collections;

import es.uji.geotec.wearossensors.intent.IntentManager;

public class PermissionsManager {

    private static final String PREFERENCES_NAME = "WEAROSSENSORS_PREFS";
    private static final String PERMISSIONS_ACTIVITY_KEY = "PERMISSIONS_ACTIVITY";
    public static final int PERMISSIONS_RC = 51;

    public static ArrayList<String> permissionsToRequestFromIntent(Intent intent) {
        return IntentManager.permissionsFromIntent(intent);
    }

    public static ArrayList<String> specialPermissionsToRequestFromIntent(Intent intent) {
        return IntentManager.specialPermissionsFromIntent(intent);
    }

    public static ArrayList<String> permissionsToBeRequested(Context context, ArrayList<String> required) {
        ArrayList<String> toBeRequested = new ArrayList<>(required.size());
        for (String permission : required) {
            int granted = ActivityCompat.checkSelfPermission(context, permission);
            if (granted == PackageManager.PERMISSION_DENIED) {
                toBeRequested.add(permission);
            }
        }

        return toBeRequested;
    }

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    public static void launchRequiredPermissionsRequest(Activity activity) {
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
            return;
        }

        PendingIntent intent = IntentManager.pendingIntentFromPermissionsToRequest(
                activity,
                getPermissionsActivity(activity),
                new ArrayList<>(Collections.singletonList(
                        Manifest.permission.POST_NOTIFICATIONS
                ))
        );

        try {
            intent.send();
        } catch (PendingIntent.CanceledException e) {
            e.printStackTrace();
        }
    }

    public static boolean launchPermissionsRequestIfNeeded(Activity activity, ArrayList<String> permissions) {
        ArrayList<String> permissionsToBeRequested = permissionsToBeRequested(activity, permissions);
        if (permissionsToBeRequested.size() == 0) {
            return false;
        }

        PendingIntent intent = IntentManager.pendingIntentFromPermissionsToRequest(
                activity,
                getPermissionsActivity(activity),
                permissionsToBeRequested
        );
        try {
            intent.send();
        } catch (PendingIntent.CanceledException e) {
            e.printStackTrace();
        }
        return true;
    }

    public static void requestPermissions(Activity activity, ArrayList<String> permissions) {
        String[] permissionsArray = new String[permissions.size()];
        permissions.toArray(permissionsArray);
        activity.requestPermissions(permissionsArray, PERMISSIONS_RC);
    }

    public static void setPermissionsActivity(Context context, Class<?> permissionsActivity) {
        SharedPreferences preferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(PERMISSIONS_ACTIVITY_KEY, permissionsActivity.getName());
        editor.apply();
    }

    public static Class<?> getPermissionsActivity(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        String className = preferences.getString(PERMISSIONS_ACTIVITY_KEY, "");

        Class<?> permissionsActivityClass = null;
        try {
            permissionsActivityClass = Class.forName(className);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return permissionsActivityClass;
    }
}
