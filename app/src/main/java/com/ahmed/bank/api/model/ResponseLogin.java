package com.ahmed.bank.api.model;

public class ResponseLogin {
    private String status;
    private String token;

    public ResponseLogin(String status, String token) {
        this.status = status;
        this.token = token;
    }

    public String getStatus() {
        return status;
    }

    public String getToken() {
        return token;
    }
}
