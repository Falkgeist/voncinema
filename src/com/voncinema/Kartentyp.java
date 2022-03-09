package com.voncinema;

public class Kartentyp {
    int ID;
    String name;
    double preis;

    Kartentyp (int ID, String name, double preis) {
        this.ID = ID;
        this.name = name;
        this.preis = preis;
    }

    public int getID() {
        return ID;
    }

    @Override
    public String toString() {
        return name;
    }
}
