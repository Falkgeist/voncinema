package com.voncinema;

import java.util.ArrayList;

public class Film {
    public int ID;
    public String name;
    public String beschreibung;
    public int laenge;
    public ArrayList<FilmKategorie> kategorien = new ArrayList<FilmKategorie>();

    Film(int ID, String name, String beschreibung, int laenge) {
        this.ID = ID;
        this.name = name;
        this.beschreibung = beschreibung;
        this.laenge = laenge;
    }

    @Override
    public String toString() {
        return name + " (" +laenge+" min)";
    }
}
