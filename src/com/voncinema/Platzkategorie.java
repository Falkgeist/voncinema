package com.voncinema;

import java.util.ArrayList;

public class Platzkategorie {
    private int ID;
    private String name;
    private double zuschlagFix;

    Platzkategorie(int ID, String name, double zuschlagFix) {
        this.ID = ID;
        this.name = name;
        this.zuschlagFix = zuschlagFix;
    }
}
