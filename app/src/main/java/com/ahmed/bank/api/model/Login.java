package com.ahmed.bank.api.model;

public class Login {

    private int numero;
    private int code;

    public Login(int numero, int code) {
        this.numero = numero;
        this.code = code;
    }

    public int getNumero() {
        return numero;
    }

    public int getCode() {
        return code;
    }
}
