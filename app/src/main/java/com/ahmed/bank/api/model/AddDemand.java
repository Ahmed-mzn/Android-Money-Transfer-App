package com.ahmed.bank.api.model;

public class AddDemand {
    private int numero;
    private double amount;
    private String comments;

    public AddDemand(int numero, double amount, String comments) {
        this.numero = numero;
        this.amount = amount;
        this.comments = comments;
    }

    public int getNumero() {
        return numero;
    }

    public double getAmount() {
        return amount;
    }

    public String getComments() {
        return comments;
    }
}
