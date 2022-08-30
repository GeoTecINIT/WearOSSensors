package es.uji.geotec.wearossensors.records;

import android.location.Location;

import es.uji.geotec.backgroundsensors.record.Record;
import es.uji.geotec.wearossensors.sensor.WearSensor;

public class LocationRecord extends Record {

    private double latitude;
    private double longitude;
    private double altitude;
    private float verticalAccuracy;
    private float horizontalAccuracy;
    private float speed;
    private float direction;

    public LocationRecord(Location location) {
        super(WearSensor.LOCATION, location.getTime());
        this.latitude = location.getLatitude();
        this.longitude = location.getLongitude();
        this.altitude = location.getAltitude();
        this.verticalAccuracy = location.getAccuracy();
        this.horizontalAccuracy = location.getAccuracy();
        this.speed = location.getSpeed();
        this.direction = location.getBearing();
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

    public float getHorizontalAccuracy() {
        return horizontalAccuracy;
    }

    public float getVerticalAccuracy() {
        return verticalAccuracy;
    }

    public float getSpeed() {
        return speed;
    }

    public float getDirection() {
        return direction;
    }
}
