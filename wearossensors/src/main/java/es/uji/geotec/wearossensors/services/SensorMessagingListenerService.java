package es.uji.geotec.wearossensors.services;

import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

import es.uji.geotec.wearossensors.capabilities.CapabilityAdvertisementHandler;
import es.uji.geotec.wearossensors.freemessage.FreeMessageHandler;
import es.uji.geotec.wearossensors.messaging.handlers.AccelerometerMessagingHandler;
import es.uji.geotec.wearossensors.messaging.handlers.GyroscopeMessagingHandler;
import es.uji.geotec.wearossensors.messaging.handlers.HeartRateMessagingHandler;
import es.uji.geotec.wearossensors.messaging.handlers.LocationMessagingHandler;
import es.uji.geotec.wearossensors.messaging.handlers.MagnetometerMessagingHandler;


public class SensorMessagingListenerService extends WearableListenerService {

    @Override
    public void onMessageReceived(MessageEvent event) {
        String path = event.getPath();

        if (path.contains("advertise-capabilities")) {
            new CapabilityAdvertisementHandler(this).handleRequest(event);
        } else if (path.contains("accelerometer")) {
            new AccelerometerMessagingHandler(this).handleMessage(event);
        } else if (path.contains("gyroscope")) {
            new GyroscopeMessagingHandler(this).handleMessage(event);
        } else if (path.contains("magnetometer")) {
            new MagnetometerMessagingHandler(this).handleMessage(event);
        } else if(path.contains("heart_rate")) {
            new HeartRateMessagingHandler(this).handleMessage(event);
        } else if (path.contains("location")) {
            new LocationMessagingHandler(this).handleMessage(event);
        } else if (path.contains("free-message")) {
            FreeMessageHandler.getInstance().handleMessage(event);
        }
    }
}
