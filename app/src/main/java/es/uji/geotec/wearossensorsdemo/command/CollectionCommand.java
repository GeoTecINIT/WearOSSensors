package es.uji.geotec.wearossensorsdemo.command;

import es.uji.geotec.wearossensors.sensor.WearSensor;

public interface CollectionCommand {
    void executeStart(WearSensor sensor);
    void executeStop(WearSensor sensor);
}
