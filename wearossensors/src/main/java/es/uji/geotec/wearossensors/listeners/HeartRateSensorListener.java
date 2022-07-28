package es.uji.geotec.wearossensors.listeners;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;

import es.uji.geotec.backgroundsensors.record.accumulator.RecordAccumulator;
import es.uji.geotec.wearossensors.records.HeartRateRecord;
import es.uji.geotec.wearossensors.sensor.WearSensor;


public class HeartRateSensorListener implements SensorEventListener {

    private WearSensor sensor;
    private RecordAccumulator accumulator;

    public HeartRateSensorListener(RecordAccumulator recordAccumulator) {
        this.sensor = WearSensor.HEART_RATE;
        this.accumulator = recordAccumulator;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() != sensor.getType())
            return;

        int value = (int) event.values[0];

        HeartRateRecord record = new HeartRateRecord(System.currentTimeMillis(), value);
        accumulator.accumulateRecord(record);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
