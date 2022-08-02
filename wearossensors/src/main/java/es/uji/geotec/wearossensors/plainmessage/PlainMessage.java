package es.uji.geotec.wearossensors.plainmessage;

import com.google.gson.Gson;

public class PlainMessage {
    private String message;
    private PlainMessage inResponseTo;

    public PlainMessage(String message) {
        this.message = message;
    }

    public PlainMessage(String message, PlainMessage inResponseTo) {
        this.message = message;
        this.inResponseTo = inResponseTo;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public PlainMessage getInResponseTo() {
        return inResponseTo;
    }

    public void setInResponseTo(PlainMessage inResponseTo) {
        this.inResponseTo = inResponseTo;
    }

    @Override
    public String toString() {
        return "PlainMessage{" +
                "message='" + message + '\'' +
                ", inResponseTo=" + inResponseTo +
                '}';
    }

    public static String encodePlainMessage(PlainMessage plainMessage) {
        return new Gson().toJson(plainMessage);
    }

    public static PlainMessage decodePlainMessage(String stringMessage) {
        return new Gson().fromJson(stringMessage, PlainMessage.class);
    }
}
