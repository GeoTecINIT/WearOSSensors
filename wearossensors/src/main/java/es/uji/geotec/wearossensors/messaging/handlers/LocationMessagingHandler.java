package es.uji.geotec.wearossensors.messaging.handlers;

import android.Manifest;
import android.content.Context;

import java.util.ArrayList;
import java.util.Arrays;

import es.uji.geotec.wearossensors.messaging.MessagingProtocol;
import es.uji.geotec.wearossensors.messaging.ResultMessagingProtocol;
import es.uji.geotec.wearossensors.sensor.WearSensor;

public class LocationMessagingHandler extends AbstractMessagingHandler{

    public LocationMessagingHandler(Context context) {
        super(context);
    }

    @Override
    protected ArrayList<String> getRequiredPermissions() {
        return new ArrayList<>(Arrays.asList(Manifest.permission.ACCESS_FINE_LOCATION));
    }

    @Override
    protected MessagingProtocol getProtocol() {
        return new MessagingProtocol(
                "/location/start",
                "/location/stop",
                "/location/new-record",
                new ResultMessagingProtocol("/location/ready"),
                new ResultMessagingProtocol("/location/prepare")
        );
    }

    @Override
    protected WearSensor getWearSensorType() {
        return WearSensor.LOCATION;
    }
}
