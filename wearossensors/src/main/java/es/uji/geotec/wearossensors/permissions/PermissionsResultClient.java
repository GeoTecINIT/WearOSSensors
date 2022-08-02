package es.uji.geotec.wearossensors.permissions;

import android.content.Context;
import android.content.Intent;

import es.uji.geotec.wearossensors.intent.IntentManager;
import es.uji.geotec.wearossensors.messaging.MessagingClient;
import es.uji.geotec.wearossensors.messaging.ResultMessagingProtocol;

public class PermissionsResultClient {
    private MessagingClient messagingClient;

    public PermissionsResultClient(Context context) {
        this.messagingClient = new MessagingClient(context);
    }

    public void sendPermissionsSuccessfulResponse(Intent intent) {
        String sourceNodeId = IntentManager.sourceNodeIdFromIntent(intent);
        ResultMessagingProtocol protocol = IntentManager.resultProtocolFromIntent(intent);
        messagingClient.sendSuccessfulResponse(sourceNodeId, protocol);
    }

    public void sendPermissionsFailureResponse(Intent intent, String failureReason) {
        String sourceNodeId = IntentManager.sourceNodeIdFromIntent(intent);
        ResultMessagingProtocol protocol = IntentManager.resultProtocolFromIntent(intent);
        messagingClient.sendFailureResponseWithReason(sourceNodeId, protocol, failureReason);
    }
}
