package es.uji.geotec.wearossensors.messaging;

import java.io.Serializable;

public class MessagingProtocol implements Serializable {
    private String startMessagePath;
    private String stopMessagePath;
    private String newRecordMessagePath;
    private ResultMessagingProtocol readyProtocol;
    private ResultMessagingProtocol prepareProtocol;

    public MessagingProtocol(
            String startMessagePath,
            String stopMessagePath,
            String newRecordMessagePath,
            ResultMessagingProtocol readyProtocol,
            ResultMessagingProtocol prepareProtocol)
    {
        this.startMessagePath = startMessagePath;
        this.stopMessagePath = stopMessagePath;
        this.newRecordMessagePath = newRecordMessagePath;
        this.readyProtocol = readyProtocol;
        this.prepareProtocol = prepareProtocol;
    }

    public String getStartMessagePath() {
        return startMessagePath;
    }

    public String getStopMessagePath() {
        return stopMessagePath;
    }

    public String getNewRecordMessagePath() {
        return newRecordMessagePath;
    }

    public ResultMessagingProtocol getReadyProtocol() {
        return readyProtocol;
    }

    public ResultMessagingProtocol getPrepareProtocol() {
        return prepareProtocol;
    }
}
