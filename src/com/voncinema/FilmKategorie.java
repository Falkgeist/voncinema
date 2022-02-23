package com.voncinema;

public class FilmKategorie {
    private int ID, zuschlagProzent;
    private String name;

    FilmKategorie(int ID, String name, int zuschlagProzent) {
        this.ID = ID;
        this.name = name;
        this.zuschlagProzent = zuschlagProzent;
    }
}
