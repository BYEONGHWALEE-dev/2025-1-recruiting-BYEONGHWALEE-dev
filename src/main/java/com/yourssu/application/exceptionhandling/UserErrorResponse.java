package com.yourssu.application.exceptionhandling;

public class UserErrorResponse {

    private long timestamp;
    private String status;
    private String message;
    private String requestURL;

    public UserErrorResponse(long timestamp, String status, String message, String requestURL) {
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
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
