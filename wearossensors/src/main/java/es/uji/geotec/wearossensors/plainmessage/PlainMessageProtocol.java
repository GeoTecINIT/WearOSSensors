package es.uji.geotec.wearossensors.plainmessage;

public class PlainMessageProtocol {
    private String withoutResponsePath;
    private String expectingResponsePath;

    public PlainMessageProtocol(String withoutResponsePath, String expectingResponsePath) {
        this.withoutResponsePath = withoutResponsePath;
        this.expectingResponsePath = expectingResponsePath;
    }

    public String getWithoutResponsePath() {
        return withoutResponsePath;
    }

    public String getExpectingResponsePath() {
        return expectingResponsePath;
    }

    public static PlainMessageProtocol getProtocol() {
        return new PlainMessageProtocol(
                "plain-message-no-response",
                "plain-message-expecting-response"
        );
    }
}
