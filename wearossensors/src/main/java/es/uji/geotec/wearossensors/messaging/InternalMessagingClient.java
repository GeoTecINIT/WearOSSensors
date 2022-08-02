package es.uji.geotec.wearossensors.messaging;

import android.content.Context;

import com.google.android.gms.wearable.MessageClient;
import com.google.android.gms.wearable.Wearable;

public class InternalMessagingClient {

    private MessageClient messageClient;

    public InternalMessagingClient(Context context) {
        this.messageClient = Wearable.getMessageClient(context);
    }

    public void sendSuccessfulResponse(String sourceNodeId, ResultMessagingProtocol protocol) {
        messageClient.sendMessage(
                sourceNodeId,
                protocol.getMessagePath(),
                protocol.getSuccessResponse().getBytes()
        );
    }

    public void sendFailureResponse(String sourceNodeId, ResultMessagingProtocol protocol) {
        messageClient.sendMessage(
                sourceNodeId,
                protocol.getMessagePath(),
                protocol.getFailureResponse().getBytes()
        );
    }

    public void sendFailureResponseWithReason(String sourceNodeId, ResultMessagingProtocol protocol, String failureReason) {
        String message = protocol.getFailureResponse() + "#" + failureReason;
        messageClient.sendMessage(
                sourceNodeId,
                protocol.getMessagePath(),
                message.getBytes()
        );
    }

    public void sendNewRecord(String sourceNodeId, String path, byte[] record) {
        messageClient.sendMessage(sourceNodeId, path, record);
    }
}
