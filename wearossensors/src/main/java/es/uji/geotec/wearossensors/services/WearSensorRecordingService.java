package es.uji.geotec.wearossensors.services;

import es.uji.geotec.backgroundsensors.collection.CollectorManager;
import es.uji.geotec.backgroundsensors.service.SensorRecordingService;
import es.uji.geotec.wearossensors.collection.WearCollectorManager;

public class WearSensorRecordingService extends SensorRecordingService {
    @Override
    public CollectorManager getCollectorManager() {
        return new WearCollectorManager(this);
    }
}
