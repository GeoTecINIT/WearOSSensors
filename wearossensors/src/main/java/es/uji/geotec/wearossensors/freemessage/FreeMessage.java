package es.uji.geotec.wearossensors.freemessage;

import com.google.gson.Gson;

public class FreeMessage {
    private String message;
    private FreeMessage inResponseTo;

    public FreeMessage(String message) {
        this.message = message;
    }

    public FreeMessage(String message, FreeMessage inResponseTo) {
        this.message = message;
        this.inResponseTo = inResponseTo;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public FreeMessage getInResponseTo() {
        return inResponseTo;
    }

    public void setInResponseTo(FreeMessage inResponseTo) {
        this.inResponseTo = inResponseTo;
    }

    @Override
    public String toString() {
        return "FreeMessage{" +
                "message='" + message + '\'' +
                ", inResponseTo=" + inResponseTo +
                '}';
    }

    public static String encodeFreeMessage(FreeMessage freeMessage) {
        return new Gson().toJson(freeMessage);
    }

    public static FreeMessage decodeFreeMessage(String stringMessage) {
        return new Gson().fromJson(stringMessage, FreeMessage.class);
    }
}
