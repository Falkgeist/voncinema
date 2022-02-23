package com.voncinema;

public class Vorstellung {
    private int ID, film, kinosaal;
    private String beginn, ende;

    Vorstellung(int ID, int film, int kinosaal, String beginn, String ende){
        this.ID = ID;
        this.film = film;
        this.kinosaal = kinosaal;
        this.beginn = beginn;
        this.ende = ende;
    }

    @Override
    public String toString() {
        return beginn + " - " +ende;
    }
}
