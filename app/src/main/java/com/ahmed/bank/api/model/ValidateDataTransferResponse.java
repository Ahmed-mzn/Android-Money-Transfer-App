package com.ahmed.bank.api.model;

public class ValidateDataTransferResponse {

    private String message;
    private Carte carte;

    public ValidateDataTransferResponse(String message, Carte carte) {
        this.message = message;
        this.carte = carte;
    }

    public String getMessage() {
        return message;
    }

    public Carte getCarte() {
        return carte;
    }
}
