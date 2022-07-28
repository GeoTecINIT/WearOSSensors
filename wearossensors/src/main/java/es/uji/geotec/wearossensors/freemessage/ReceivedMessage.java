package es.uji.geotec.wearossensors.freemessage;

public class ReceivedMessage {
    private String senderNodeId;
    private FreeMessage freeMessage;
    private boolean requiresResponse;

    public ReceivedMessage(String senderNodeId, FreeMessage freeMessage, boolean requiresResponse) {
        this.senderNodeId = senderNodeId;
        this.freeMessage = freeMessage;
        this.requiresResponse = requiresResponse;
    }

    public String getSenderNodeId() {
        return senderNodeId;
    }

    public void setSenderNodeId(String senderNodeId) {
        this.senderNodeId = senderNodeId;
    }

    public FreeMessage getFreeMessage() {
        return freeMessage;
    }

    public void setFreeMessage(FreeMessage freeMessage) {
        this.freeMessage = freeMessage;
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
                ", freeMessage=" + freeMessage +
                ", requiresResponse=" + requiresResponse +
                '}';
    }
}
