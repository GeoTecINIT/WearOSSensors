package es.uji.geotec.wearossensors.records;

import es.uji.geotec.backgroundsensors.record.Record;
import es.uji.geotec.wearossensors.sensor.WearSensor;

public class HeartRateRecord extends Record {

    private int value;

    public HeartRateRecord(long timestamp, int value) {
        super(WearSensor.HEART_RATE, timestamp);
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
