package es.uji.geotec.wearossensors.services;

import android.content.Context;
import android.content.SharedPreferences;

public class RecordingServiceManager {

    private static final String PREFERENCES_NAME = "WEAROSSENSORS_PREFS";
    private static final String RECORDING_SERVICE_KEY = "RECORDING_SERVICE";

    public static void setService(Context context, Class<?> permissionsActivity) {
        SharedPreferences preferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(RECORDING_SERVICE_KEY, permissionsActivity.getName());
        editor.apply();
    }

    public static Class<?> getService(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        String className = preferences.getString(RECORDING_SERVICE_KEY, WearSensorRecordingService.class.getName());

        Class<?> permissionsActivityClass = null;
        try {
            permissionsActivityClass = Class.forName(className);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return permissionsActivityClass;
    }
}
