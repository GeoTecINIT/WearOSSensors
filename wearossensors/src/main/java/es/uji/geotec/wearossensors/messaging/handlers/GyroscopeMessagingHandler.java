package es.uji.geotec.wearossensors.messaging.handlers;

import android.content.Context;

import java.util.ArrayList;

import es.uji.geotec.wearossensors.messaging.MessagingProtocol;
import es.uji.geotec.wearossensors.messaging.ResultMessagingProtocol;
import es.uji.geotec.wearossensors.sensor.WearSensor;

public class GyroscopeMessagingHandler extends AbstractMessagingHandler {

    public GyroscopeMessagingHandler(Context context) { super(context); };

    @Override
    protected ArrayList<String> getRequiredPermissions() {
        return new ArrayList<>();
    }

    @Override
    protected MessagingProtocol getProtocol() {
        return new MessagingProtocol(
                "/gyroscope/start",
                "/gyroscope/stop",
                "/gyroscope/new-record",
                new ResultMessagingProtocol("/gyroscope/ready"),
                new ResultMessagingProtocol("/gyroscope/prepare")
        );
    }

    @Override
    protected WearSensor getWearSensorType() {
        return WearSensor.GYROSCOPE;
    }
}
