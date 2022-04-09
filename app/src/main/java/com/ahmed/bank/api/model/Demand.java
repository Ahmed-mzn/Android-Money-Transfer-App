package com.ahmed.bank.api.model;

import com.google.gson.annotations.SerializedName;

public class Demand {

    private int id;

    @SerializedName("demand_sender")
    private Carte fromCarte;

    @SerializedName("demand_receiver")
    private Carte toCarte;

    private String status;
    private String comments;
    private double amount;

    @SerializedName("get_date")
    private String date;


    public Demand(int id, Carte fromCarte, Carte toCarte, String status, String comments, double amount, String date) {
        this.id = id;
        this.fromCarte = fromCarte;
        this.toCarte = toCarte;
        this.status = status;
        this.comments = comments;
        this.amount = amount;
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

    public String getStatus() {
        return status;
    }

    public String getComments() {
        return comments;
    }

    public double getAmount() {
        return amount;
    }

    public String getDate() {
        return date;
    }
}
