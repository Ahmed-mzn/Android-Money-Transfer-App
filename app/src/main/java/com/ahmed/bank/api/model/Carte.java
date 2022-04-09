package com.ahmed.bank.api.model;

public class Carte {
    private int id;
    private int numero;
    private String libelle;

    public Carte() {
    }

    public Carte(int id, int numero, String libelle) {
        this.id = id;
        this.numero = numero;
        this.libelle = libelle;
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
}
