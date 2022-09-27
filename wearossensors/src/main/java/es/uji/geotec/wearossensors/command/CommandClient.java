package es.uji.geotec.wearossensors.command;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.wearable.CapabilityClient;
import com.google.android.gms.wearable.MessageClient;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.Wearable;

import java.util.Set;

import es.uji.geotec.backgroundsensors.collection.CollectionConfiguration;
import es.uji.geotec.backgroundsensors.sensor.Sensor;

public class CommandClient {

    private CapabilityClient capabilityClient;
    private MessageClient messageClient;

    public CommandClient(Context context) {
        this.capabilityClient = Wearable.getCapabilityClient(context);
        this.messageClient = Wearable.getMessageClient(context);
    }

    public void sendStartCommand(CollectionConfiguration configuration) {
        sendCommand(
                "start-" + configuration.getSensor().toString().toLowerCase(),
                configuration.getSensorDelay(),
                configuration.getBatchSize()
        );
    }

    public void sendStopCommand(Sensor sensor) {
        sendCommand("stop-" + sensor.toString().toLowerCase());
    }

    protected void sendCommand(String commandName, int sensorDelay, int batchSize) {
        String command = commandFromParameters(commandName, sensorDelay, batchSize);
        sendCommand(command);
    }

    private String commandFromParameters(String commandName, int sensorDelay, int batchSize) {
        StringBuilder sb = new StringBuilder();
        sb.append(commandName);
        sb.append("#");
        sb.append(sensorDelay);
        sb.append("#");
        sb.append(batchSize);
        return sb.toString();
    }

    private void sendCommand(String command) {
        capabilityClient.getCapability("main-node", CapabilityClient.FILTER_ALL)
                .addOnSuccessListener(capabilityInfo -> {
                    Set<Node> mainNodes = capabilityInfo.getNodes();
                    if (mainNodes.isEmpty())
                        return;

                    Node mainNode = mainNodes.iterator().next();
                    messageClient.sendMessage(mainNode.getId(), "command", command.getBytes());
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("Failure", e.getMessage());
                    }
                });
    }
}
