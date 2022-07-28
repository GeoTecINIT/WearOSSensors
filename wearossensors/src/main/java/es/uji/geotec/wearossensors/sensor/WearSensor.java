package es.uji.geotec.wearossensors.sensor;

import android.content.pm.PackageManager;

import es.uji.geotec.backgroundsensors.sensor.Sensor;

public enum WearSensor implements Sensor {
    ACCELEROMETER(android.hardware.Sensor.TYPE_ACCELEROMETER, PackageManager.FEATURE_SENSOR_ACCELEROMETER),
    GYROSCOPE(android.hardware.Sensor.TYPE_GYROSCOPE, PackageManager.FEATURE_SENSOR_GYROSCOPE),
    MAGNETOMETER(android.hardware.Sensor.TYPE_MAGNETIC_FIELD, PackageManager.FEATURE_SENSOR_COMPASS),
    HEART_RATE(android.hardware.Sensor.TYPE_HEART_RATE, PackageManager.FEATURE_SENSOR_HEART_RATE),
    LOCATION(-1, PackageManager.FEATURE_LOCATION_GPS);

    private int sensorType;
    private String feature;
    WearSensor(int sensorType, String feature) {
        this.sensorType = sensorType;
        this.feature = feature;
    }

    @Override
    public int getType() {
        return sensorType;
    }

    @Override
    public String getSystemFeature() {
        return feature;
    }
}
