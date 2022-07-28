package es.uji.geotec.wearossensors.records;

import android.location.Location;

import es.uji.geotec.backgroundsensors.record.Record;
import es.uji.geotec.wearossensors.sensor.WearSensor;

public class LocationRecord extends Record {

    private double latitude;
    private double longitude;
    private double altitude;

    public LocationRecord(long timestamp, double latitude, double longitude, double altitude) {
        super(WearSensor.LOCATION, timestamp);
        this.latitude = latitude;
        this.longitude = longitude;
        this.altitude = altitude;
    }

    public LocationRecord(Location location) {
        super(WearSensor.LOCATION, location.getTime());
        this.latitude = location.getLatitude();
        this.longitude = location.getLongitude();
        this.altitude = location.getAltitude();
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getAltitude() {
        return altitude;
    }
}
