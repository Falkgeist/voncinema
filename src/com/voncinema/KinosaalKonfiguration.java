package com.voncinema;

public class KinosaalKonfiguration {
    private int ID;

    KinosaalKonfiguration(int ID) {
        this.ID = ID;
    }

    public int getID() {
        return ID;
    }

    public boolean hasID(int ID) {
        return this.ID == ID;
    }
}