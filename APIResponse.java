package com.zydexindustries.zydex.utils;

/**
 * Created by Bhavesh Desai
 */
public class APIResponse {
    private int responseCode;
    private String responseMessage;

    public APIResponse(int responseCode, String responseMessage) {
        this.responseCode = responseCode;
        this.responseMessage = responseMessage;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

}
