package es.uji.geotec.wearossensorsdemo.command;

import android.content.Context;
import android.util.Log;

import es.uji.geotec.backgroundsensors.collection.CollectionConfiguration;
import es.uji.geotec.backgroundsensors.service.manager.ServiceManager;
import es.uji.geotec.wearossensors.sensor.WearSensor;
import es.uji.geotec.wearossensors.services.WearSensorRecordingService;

public class LocalCollectionCommand implements CollectionCommand {

    private ServiceManager serviceManager;

    public LocalCollectionCommand(Context context) {
        this.serviceManager = new ServiceManager(context, WearSensorRecordingService.class);
    }
    @Override
    public void executeStart(WearSensor sensor) {
        CollectionConfiguration config = new CollectionConfiguration(
                sensor,
                android.hardware.SensorManager.SENSOR_DELAY_GAME,
                sensor == WearSensor.HEART_RATE || sensor == WearSensor.LOCATION ? 1 : 50
        );
        this.serviceManager.startCollection(config, records -> {
            Log.d("LOCAL COLLECTION", sensor.toString()+ " records: " + records.toString());
        });
    }

    @Override
    public void executeStop(WearSensor sensor) {
        this.serviceManager.stopCollection(sensor);
    }
}
