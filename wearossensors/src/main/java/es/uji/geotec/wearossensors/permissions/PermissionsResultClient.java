package es.uji.geotec.wearossensors.permissions;

import android.content.Context;
import android.content.Intent;

import es.uji.geotec.wearossensors.intent.IntentManager;
import es.uji.geotec.wearossensors.messaging.InternalMessagingClient;
import es.uji.geotec.wearossensors.messaging.ResultMessagingProtocol;

public class PermissionsResultClient {
    private InternalMessagingClient internalMessagingClient;

    public PermissionsResultClient(Context context) {
        this.internalMessagingClient = new InternalMessagingClient(context);
    }

    public void sendPermissionsSuccessfulResponse(Intent intent) {
        String sourceNodeId = IntentManager.sourceNodeIdFromIntent(intent);
        ResultMessagingProtocol protocol = IntentManager.resultProtocolFromIntent(intent);
        internalMessagingClient.sendSuccessfulResponse(sourceNodeId, protocol);
    }

    public void sendPermissionsFailureResponse(Intent intent, String failureReason) {
        String sourceNodeId = IntentManager.sourceNodeIdFromIntent(intent);
        ResultMessagingProtocol protocol = IntentManager.resultProtocolFromIntent(intent);
        internalMessagingClient.sendFailureResponseWithReason(sourceNodeId, protocol, failureReason);
    }
}
