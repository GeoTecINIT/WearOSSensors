package es.uji.geotec.wearossensors.plainmessage;

import android.content.Context;
import android.util.Log;

import com.google.android.gms.wearable.CapabilityClient;
import com.google.android.gms.wearable.MessageClient;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.Wearable;

import java.util.Set;

public class PlainMessageClient {

    private CapabilityClient capabilityClient;
    private MessageClient messageClient;
    private PlainMessageProtocol protocol;

    public PlainMessageClient(Context context) {
        this.capabilityClient = Wearable.getCapabilityClient(context);
        this.messageClient = Wearable.getMessageClient(context);
        this.protocol = PlainMessageProtocol.getProtocol();
    }

    public void registerListener(PlainMessageListener listener) {
        PlainMessageHandler.getInstance().setListener(listener);
    }

    public void unregisterListener() {
        PlainMessageHandler.getInstance().clearListener();
    }

    public void send(PlainMessage plainMessage) {
        capabilityClient.getCapability("main-node", CapabilityClient.FILTER_ALL)
                .addOnSuccessListener(capabilityInfo -> {
                    Set<Node> mainNodes = capabilityInfo.getNodes();
                    if (mainNodes.isEmpty())
                        return;

                    Node mainNode = mainNodes.iterator().next();
                    String jsonString = PlainMessage.encodePlainMessage(plainMessage);
                    messageClient.sendMessage(mainNode.getId(), this.protocol.getWithoutResponsePath(), jsonString.getBytes());
                })
                .addOnFailureListener(e -> Log.d("Failure", e.getMessage()));
    }
}
