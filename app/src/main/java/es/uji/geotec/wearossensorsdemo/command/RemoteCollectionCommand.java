package es.uji.geotec.wearossensorsdemo.command;

import android.content.Context;

import es.uji.geotec.backgroundsensors.collection.CollectionConfiguration;
import es.uji.geotec.wearossensors.command.CommandClient;
import es.uji.geotec.wearossensors.sensor.WearSensor;

public class RemoteCollectionCommand implements CollectionCommand {

    private CommandClient commandClient;

    public RemoteCollectionCommand(Context context) {
        this.commandClient = new CommandClient(context);
    }
    @Override
    public void executeStart(WearSensor sensor) {
        CollectionConfiguration config = new CollectionConfiguration(
                sensor,
                android.hardware.SensorManager.SENSOR_DELAY_GAME,
                sensor == WearSensor.HEART_RATE || sensor == WearSensor.LOCATION ? 1 : 50
        );
        this.commandClient.sendStartCommand(config);
    }

    @Override
    public void executeStop(WearSensor sensor) {
        this.commandClient.sendStopCommand(sensor);
    }
}
