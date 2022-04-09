package com.ahmed.bank.api.model;

public class DataTransferValidate {

    private int numero;
    private double amount;

    public DataTransferValidate(int numero, double amount) {
        this.numero = numero;
        this.amount = amount;
    }

    public int getNumero() {
        return numero;
    }

    public double getAmount() {
        return amount;
    }
}
