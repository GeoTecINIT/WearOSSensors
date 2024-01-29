package es.uji.geotec.wearossensors.sensor;

import static android.os.Build.VERSION_CODES.TIRAMISU;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;

import java.util.ArrayList;
import java.util.Collections;

import es.uji.geotec.backgroundsensors.sensor.Sensor;

public enum WearSensor implements Sensor {
    ACCELEROMETER(
            android.hardware.Sensor.TYPE_ACCELEROMETER,
            PackageManager.FEATURE_SENSOR_ACCELEROMETER,
            new ArrayList<>()
            ),
    GYROSCOPE(
            android.hardware.Sensor.TYPE_GYROSCOPE,
            PackageManager.FEATURE_SENSOR_GYROSCOPE,
            new ArrayList<>()
    ),
    MAGNETOMETER(
            android.hardware.Sensor.TYPE_MAGNETIC_FIELD,
            PackageManager.FEATURE_SENSOR_COMPASS,
            new ArrayList<>()
    ),
    HEART_RATE(
            android.hardware.Sensor.TYPE_HEART_RATE,
            PackageManager.FEATURE_SENSOR_HEART_RATE,
            new ArrayList<>(Collections.singletonList(Manifest.permission.BODY_SENSORS))
    ),
    LOCATION(
            -1,
            PackageManager.FEATURE_LOCATION_GPS,
            new ArrayList<>(Collections.singletonList(Manifest.permission.ACCESS_FINE_LOCATION))
    );

    private int sensorType;
    private String feature;
    private ArrayList<String> requiredPermissions;

    WearSensor(int sensorType, String feature, ArrayList<String> requiredPermissions) {
        this.sensorType = sensorType;
        this.feature = feature;
        this.requiredPermissions = requiredPermissions;

        if (this.sensorType == android.hardware.Sensor.TYPE_HEART_RATE && Build.VERSION.SDK_INT >= TIRAMISU) {
            this.requiredPermissions.add(Manifest.permission.BODY_SENSORS_BACKGROUND);
        }

        if (this.sensorType == -1 && Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            this.requiredPermissions.add(Manifest.permission.ACCESS_BACKGROUND_LOCATION);
        }
    }

    @Override
    public int getType() {
        return sensorType;
    }

    @Override
    public String getSystemFeature() {
        return feature;
    }

    public ArrayList<String> getRequiredPermissions() {
        return this.requiredPermissions;
    }
}
