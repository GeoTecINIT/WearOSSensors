package es.uji.geotec.wearossensors.listeners;

import android.location.Location;

import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;

import es.uji.geotec.backgroundsensors.record.accumulator.RecordAccumulator;
import es.uji.geotec.backgroundsensors.time.TimeProvider;
import es.uji.geotec.wearossensors.records.LocationRecord;

public class LocationSensorListener extends LocationCallback {

    private RecordAccumulator accumulator;
    private TimeProvider timeProvider;

    public LocationSensorListener(RecordAccumulator accumulator, TimeProvider timeProvider) {
        this.accumulator = accumulator;
        this.timeProvider = timeProvider;
    }

    @Override
    public void onLocationResult(LocationResult locationResult) {
        for (Location location : locationResult.getLocations()) {
            location.setTime(timeProvider.getTimestamp());
            LocationRecord record = new LocationRecord(location);
            accumulator.accumulateRecord(record);
        }
    }

    @Override
    public void onLocationAvailability(LocationAvailability locationAvailability) {

    }
}
