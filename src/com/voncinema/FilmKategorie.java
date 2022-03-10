package com.voncinema;

public class FilmKategorie {
    private int ID;
    private String name;
    private double zuschlagProzent;

    FilmKategorie(int ID, String name, double zuschlagProzent) {
        this.ID = ID;
        this.name = name;
        this.zuschlagProzent = zuschlagProzent;
    }

    public double getZuschlagProzent() {
        return zuschlagProzent;
    }

    public boolean hasID(int ID) {
        if (this.ID == ID) {
            return true;
        }
        return false;
    }
}
