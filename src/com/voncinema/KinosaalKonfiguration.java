package com.voncinema;

import java.util.ArrayList;

public class KinosaalKonfiguration {
    private int ID;
    private ArrayList<ArrayList<Integer>> platzkategorien = new ArrayList<>();

    KinosaalKonfiguration(int ID) {
        this.ID = ID;
    }
}