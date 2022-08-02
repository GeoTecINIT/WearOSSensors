package es.uji.geotec.wearossensors.plainmessage;

import android.util.Log;

import com.google.android.gms.wearable.MessageEvent;

public class PlainMessageHandler {

    private static PlainMessageHandler instance;

    private PlainMessageProtocol protocol;
    private PlainMessageListener listener;

    private PlainMessageHandler() {
        this.protocol = PlainMessageProtocol.getProtocol();
    }

    public static PlainMessageHandler getInstance() {
        if (instance == null) {
            instance = new PlainMessageHandler();
        }
        return instance;
    }

    public void setListener(PlainMessageListener listener) {
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

        PlainMessage message = PlainMessage.decodePlainMessage(encodedMessage);
        ReceivedMessage receivedMessage = new ReceivedMessage(
            sourceNodeId,
            message,
            path.equals(this.protocol.getExpectingResponsePath())
        );

        if (listener == null) {
            Log.d("PlainMessageHandler", "received message " + receivedMessage + " but there are no callbacks set");
        }

        this.listener.onMessageReceived(receivedMessage);
    }
}
