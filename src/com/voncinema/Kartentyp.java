package com.voncinema;

public class Kartentyp {
    private int ID;
    private String name;
    private double preis;

    Kartentyp (int ID, String name, double preis) {
        this.ID = ID;
        this.name = name;
        this.preis = preis;
    }

    public int getID() {
        return ID;
    }

    public String getName() {
        return name;
    }

    public double getPreis() {
        return preis;
    }

    public boolean hasID(int ID) {
        if (this.ID == ID) {
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return name;
    }
}
