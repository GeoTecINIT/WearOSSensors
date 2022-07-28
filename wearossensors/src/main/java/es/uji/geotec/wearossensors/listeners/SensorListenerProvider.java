package es.uji.geotec.wearossensors.listeners;

import android.hardware.SensorEventListener;

import com.google.android.gms.location.LocationCallback;

import es.uji.geotec.backgroundsensors.listener.TriAxialSensorListener;
import es.uji.geotec.backgroundsensors.record.accumulator.RecordAccumulator;
import es.uji.geotec.backgroundsensors.time.TimeProvider;
import es.uji.geotec.wearossensors.sensor.WearSensor;

public class SensorListenerProvider {
    private SensorListenerProvider() {
    }

    public static SensorEventListener getListenerFor(
            WearSensor wearSensor,
            RecordAccumulator recordAccumulator,
            TimeProvider timeProvider
    ) {
        switch (wearSensor) {
            case ACCELEROMETER:
            case GYROSCOPE:
            case MAGNETOMETER:
                return new TriAxialSensorListener(wearSensor, recordAccumulator, timeProvider);
            case HEART_RATE:
                return new HeartRateSensorListener(recordAccumulator);
            default:
                return null;
        }
    }

    public static LocationCallback getLocationListener(RecordAccumulator recordAccumulator, TimeProvider timeProvider) {
        return new LocationSensorListener(recordAccumulator, timeProvider);
    }
}
