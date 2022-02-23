package com.voncinema;

public class KinosaalKonfigurationPlatzkategorie {
    private int ID;
    private int konfiguration;
    private int platzkategorie;
    private int anzahl;

    KinosaalKonfigurationPlatzkategorie(int ID, int konfiguration, int platzkategorie, int anzahl) {
        this.ID = ID;
        this.konfiguration = konfiguration;
        this.platzkategorie = platzkategorie;
        this.anzahl = anzahl;
    }
}
