package com.voncinema;

public class Kartentyp {
    int id;
    String name;
    double preis;

    Kartentyp (int id, String name, double preis) {
        this.id = id;
        this.name = name;
        this.preis = preis;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return name;
    }
}
