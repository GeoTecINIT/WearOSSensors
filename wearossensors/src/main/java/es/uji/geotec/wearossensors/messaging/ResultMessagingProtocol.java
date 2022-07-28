package es.uji.geotec.wearossensors.messaging;

import java.io.Serializable;

public class ResultMessagingProtocol implements Serializable {
    private String messagePath;
    private String successResponse;
    private String failureResponse;

    public ResultMessagingProtocol(String messagePath) {
        this.messagePath = messagePath;
        this.successResponse = "success";
        this.failureResponse = "failure";
    }

    public ResultMessagingProtocol(String messagePath, String successResponse, String failureResponse) {
        this.messagePath = messagePath;
        this.successResponse = successResponse;
        this.failureResponse = failureResponse;
    }

    public String getMessagePath() {
        return messagePath;
    }

    public String getSuccessResponse() {
        return successResponse;
    }

    public String getFailureResponse() {
        return failureResponse;
    }
}
