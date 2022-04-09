package com.ahmed.bank.api.model;

import com.google.gson.annotations.SerializedName;

public class Profile {

    private int id;
    private int numero;
    private String libelle;
    private String cin;
    private double solde;

    @SerializedName("get_transactions_count")
    private int transactionsCount;

    @SerializedName("get_valid_until")
    private String validateUntil;

    public Profile(int id, int numero, String libelle, String cin, double solde, int transactionsCount, String validateUntil) {
        this.id = id;
        this.numero = numero;
        this.libelle = libelle;
        this.cin = cin;
        this.solde = solde;
        this.transactionsCount = transactionsCount;
        this.validateUntil = validateUntil;
    }

    public int getId() {
        return id;
    }

    public int getNumero() {
        return numero;
    }

    public String getLibelle() {
        return libelle;
    }

    public String getCin() {
        return cin;
    }

    public double getSolde() {
        return solde;
    }

    public int getTransactionsCount() {
        return transactionsCount;
    }

    public String getValidateUntil() {
        return validateUntil;
    }
}
