package es.uji.geotec.wearossensors.plainmessage;

public class ReceivedMessage {
    private String senderNodeId;
    private PlainMessage plainMessage;
    private boolean requiresResponse;

    public ReceivedMessage(String senderNodeId, PlainMessage plainMessage, boolean requiresResponse) {
        this.senderNodeId = senderNodeId;
        this.plainMessage = plainMessage;
        this.requiresResponse = requiresResponse;
    }

    public String getSenderNodeId() {
        return senderNodeId;
    }

    public void setSenderNodeId(String senderNodeId) {
        this.senderNodeId = senderNodeId;
    }

    public PlainMessage getPlainMessage() {
        return plainMessage;
    }

    public void setPlainMessage(PlainMessage plainMessage) {
        this.plainMessage = plainMessage;
    }

    public boolean responseRequired() {
        return requiresResponse;
    }

    public void setRequiresResponse(boolean requiresResponse) {
        this.requiresResponse = requiresResponse;
    }

    @Override
    public String toString() {
        return "ReceivedMessage{" +
                "senderNodeId='" + senderNodeId + '\'' +
                ", plainMessage=" + plainMessage +
                ", requiresResponse=" + requiresResponse +
                '}';
    }
}
