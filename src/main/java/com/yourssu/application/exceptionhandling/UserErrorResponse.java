package com.yourssu.application.exceptionhandling;

public class UserErrorResponse {

    private long timestamp;
    private int status;
    private String message;
    private String requestURL;

    public UserErrorResponse(long timestamp, int status, String message, String requestURL) {
        this.timestamp = timestamp;
        this.status = status;
        this.message = message;
        this.requestURL = requestURL;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getRequestURL() {
        return requestURL;
    }

    public void setRequestURL(String requestURL) {
        this.requestURL = requestURL;
    }
}
