package es.uji.geotec.wearossensors.freemessage;

import android.util.Log;

import com.google.android.gms.wearable.MessageEvent;

public class FreeMessageHandler {

    private static FreeMessageHandler instance;

    private FreeMessageProtocol protocol;
    private FreeMessageListener listener;

    private FreeMessageHandler() {
        this.protocol = FreeMessageProtocol.getProtocol();
    }

    public static FreeMessageHandler getInstance() {
        if (instance == null) {
            instance = new FreeMessageHandler();
        }
        return instance;
    }

    public void setListener(FreeMessageListener listener) {
        this.listener = listener;
    }

    public void clearListener() {
        this.listener = null;
    }

    public void handleMessage(MessageEvent event) {
        String path = event.getPath();

        if (!path.equals(this.protocol.getExpectingResponsePath())
                && !path.equals(this.protocol.getWithoutResponsePath())) {
            return;
        }

        if (event.getData().length == 0) {
            return;
        }

        String sourceNodeId = event.getSourceNodeId();
        String encodedMessage = new String(event.getData());

        FreeMessage message = FreeMessage.decodeFreeMessage(encodedMessage);
        ReceivedMessage receivedMessage = new ReceivedMessage(
            sourceNodeId,
            message,
            path.equals(this.protocol.getExpectingResponsePath())
        );

        if (listener == null) {
            Log.d("FreeMessageHandler", "received message " + receivedMessage + " but there are no callbacks set");
        }

        this.listener.onMessageReceived(receivedMessage);
    }
}
