package com.ahmed.bank.api.model;

public class AddFavori {

    private int numero;
    private String comments;

    public AddFavori(int numero, String comments) {
        this.numero = numero;
        this.comments = comments;
    }

    public int getNumero() {
        return numero;
    }

    public String getComments() {
        return comments;
    }
}
