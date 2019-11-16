package com.ndamelio.mutant.config;

import org.springframework.http.HttpStatus;

public class MutantCustomError {

    private HttpStatus status;
    private int code;
    private String timestamp;
    private String message;

    public MutantCustomError(HttpStatus status, String message, String timestamp, int code) {
        super();
        this.code = code;
        this.timestamp = timestamp;
        this.status = status;
        this.message = message;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return "{\"FlashCustomError\":{"
            + "\"status\":\"" + status + "\""
            + ", \"code\":\"" + code + "\""
            + ", \"timestamp\":\"" + timestamp + "\""
            + ", \"message\":\"" + message + "\""
            + "}}";
    }
}