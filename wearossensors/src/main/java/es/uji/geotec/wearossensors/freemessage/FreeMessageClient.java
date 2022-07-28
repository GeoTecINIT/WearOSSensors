package es.uji.geotec.wearossensors.freemessage;

import android.content.Context;
import android.util.Log;

import com.google.android.gms.wearable.CapabilityClient;
import com.google.android.gms.wearable.MessageClient;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.Wearable;

import java.util.Set;

public class FreeMessageClient {

    private CapabilityClient capabilityClient;
    private MessageClient messageClient;
    private FreeMessageProtocol protocol;

    public FreeMessageClient(Context context) {
        this.capabilityClient = Wearable.getCapabilityClient(context);
        this.messageClient = Wearable.getMessageClient(context);
        this.protocol = FreeMessageProtocol.getProtocol();
    }

    public void registerListener(FreeMessageListener listener) {
        FreeMessageHandler.getInstance().setListener(listener);
    }

    public void unregisterListener() {
        FreeMessageHandler.getInstance().clearListener();
    }

    public void send(FreeMessage freeMessage) {
        capabilityClient.getCapability("main-node", CapabilityClient.FILTER_ALL)
                .addOnSuccessListener(capabilityInfo -> {
                    Set<Node> mainNodes = capabilityInfo.getNodes();
                    if (mainNodes.isEmpty())
                        return;

                    Node mainNode = mainNodes.iterator().next();
                    String jsonString = FreeMessage.encodeFreeMessage(freeMessage);
                    messageClient.sendMessage(mainNode.getId(), this.protocol.getWithoutResponsePath(), jsonString.getBytes());
                })
                .addOnFailureListener(e -> Log.d("Failure", e.getMessage()));
    }
}
