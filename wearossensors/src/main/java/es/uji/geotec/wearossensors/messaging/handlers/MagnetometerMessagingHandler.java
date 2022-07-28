package es.uji.geotec.wearossensors.messaging.handlers;

import android.content.Context;

import java.util.ArrayList;

import es.uji.geotec.wearossensors.messaging.MessagingProtocol;
import es.uji.geotec.wearossensors.messaging.ResultMessagingProtocol;
import es.uji.geotec.wearossensors.sensor.WearSensor;

public class MagnetometerMessagingHandler extends AbstractMessagingHandler {

    public MagnetometerMessagingHandler(Context context) {
        super(context);
    }

    @Override
    protected ArrayList<String> getRequiredPermissions() {
        return new ArrayList<>();
    }

    @Override
    protected MessagingProtocol getProtocol() {
        return new MessagingProtocol(
                "/magnetometer/start",
                "/magnetometer/stop",
                "/magnetometer/new-record",
                new ResultMessagingProtocol("/magnetometer/ready"),
                new ResultMessagingProtocol("/magnetometer/prepare")
        );
    }

    @Override
    protected WearSensor getWearSensorType() {
        return WearSensor.MAGNETOMETER;
    }
}
