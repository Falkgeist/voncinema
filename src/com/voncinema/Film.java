package com.voncinema;

import java.util.ArrayList;

public class Film {
    private int ID, laenge, kategorie;
    private String name, beschreibung, genre;

    Film(int ID, String name, String beschreibung, int laenge, int kategorie, String genre) {
        this.ID = ID;
        this.name = name;
        this.beschreibung = beschreibung;
        this.laenge = laenge;
        this.kategorie = kategorie;
        this.genre = genre;
    }

    @Override
    public String toString() {
        return name + " (" +laenge+" min)";
    }
}
