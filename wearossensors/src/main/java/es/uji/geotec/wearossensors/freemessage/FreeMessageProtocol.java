package es.uji.geotec.wearossensors.freemessage;

public class FreeMessageProtocol {
    private String withoutResponsePath;
    private String expectingResponsePath;

    public FreeMessageProtocol(String withoutResponsePath, String expectingResponsePath) {
        this.withoutResponsePath = withoutResponsePath;
        this.expectingResponsePath = expectingResponsePath;
    }

    public String getWithoutResponsePath() {
        return withoutResponsePath;
    }

    public String getExpectingResponsePath() {
        return expectingResponsePath;
    }

    public static FreeMessageProtocol getProtocol() {
        return new FreeMessageProtocol(
                "free-message-no-response",
                "free-message-expecting-response"
        );
    }
}
