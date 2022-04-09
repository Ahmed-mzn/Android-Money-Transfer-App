package com.ahmed.bank.api.model;

import com.google.gson.annotations.SerializedName;

public class Transaction {
    private int id;

    @SerializedName("from_carte")
    private Carte fromCarte;

    @SerializedName("to_carte")
    private Carte toCarte;

    private double amount;

    private String type;

    @SerializedName("get_date")
    private String date;

    public Transaction(int id, Carte fromCarte, Carte toCarte, double amount, String type, String date) {
        this.id = id;
        this.fromCarte = fromCarte;
        this.toCarte = toCarte;
        this.amount = amount;
        this.type = type;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public Carte getFromCarte() {
        return fromCarte;
    }

    public Carte getToCarte() {
        return toCarte;
    }

    public double getAmount() {
        return amount;
    }

    public String getType() {
        return type;
    }

    public String getDate() {
        return date;
    }
}
