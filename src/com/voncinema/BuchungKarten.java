package com.voncinema;

public class BuchungKarten {
    private int ID, buchung, karte;

    BuchungKarten (int buchung, int karte) {
        this.buchung = buchung;
        this.karte = karte;
    }

    BuchungKarten (int ID, int buchung, int karte) {
        this.ID = ID;
        this.buchung = buchung;
        this.karte = karte;
    }
}
