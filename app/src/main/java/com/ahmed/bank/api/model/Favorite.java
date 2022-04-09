package com.ahmed.bank.api.model;

import com.google.gson.annotations.SerializedName;

public class Favorite {
    private int id;

    @SerializedName("favorite_carte")
    private Carte carte;

    private String comments;

    @SerializedName("get_created_at")
    private String date;


    public Favorite(int id, Carte carte, String comments, String date) {
        this.id = id;
        this.carte = carte;
        this.comments = comments;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public Carte getCarte() {
        return carte;
    }

    public String getComments() {
        return comments;
    }

    public String getDate() {
        return date;
    }
}
