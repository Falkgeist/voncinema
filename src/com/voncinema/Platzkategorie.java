package com.voncinema;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class Platzkategorie {
    private int ID;
    private String name;
    private double zuschlagFix;
    private boolean hasTempFreiePlaetze;
    private int tempFreiePlaetze;

    Platzkategorie(int ID, String name, double zuschlagFix) {
        this.ID = ID;
        this.name = name;
        this.zuschlagFix = zuschlagFix;
    }

    private int getFreiePlaetze(Vorstellung vorstellung) {
        // get the applicable place category configuration
        Kinosaal kinosaal = Kinoverwaltung.getKinosaal(vorstellung.getKinosaal());
        KinosaalKonfiguration konfiguration = Kinoverwaltung.getKinosaalKonfiguration(kinosaal.getKonfiguration());
        KinosaalKonfigurationPlatzkategorie konfigurationPlatzkategorie = (KinosaalKonfigurationPlatzkategorie)Kinoverwaltung.getFromDB("vc_kinosaalkonfiguration_platzkategorie", "WHERE konfiguration = " + konfiguration.getID() + " AND kategorie = " + this.ID).get(0);

        // get the already reserved seats (payed)
        int alreadyReserved = 0;
        ArrayList<Object> buchungen = Kinoverwaltung.getFromDB("vc_buchung", "WHERE vorstellung = " + vorstellung.getID() + " AND (status = 'bezahlt' OR status = 'gebucht')");
        for (Object objBuchung : buchungen) {
            Buchung buchung = (Buchung)objBuchung;
            ArrayList<Object> buchungKarten = Kinoverwaltung.getFromDB("vc_buchung_karten", "WHERE buchung = " + buchung.getID());
            for (Object objBuchungKarten :
                    buchungKarten) {
                BuchungKarten buchungKarte = (BuchungKarten)objBuchungKarten;
                ArrayList<Object> karten = Kinoverwaltung.getFromDB("vc_karte", "WHERE id = " + buchungKarte.getKarte());
                for (Object objKarte : karten) {
                    Karte karte = (Karte)objKarte;
                    if (karte.getPlatzkategorie() == this.ID) {
                        alreadyReserved++;
                    }
                }
            }
        }

        // compare reserved with available
        return konfigurationPlatzkategorie.getAnzahl() - alreadyReserved;
    }

    public void saveToDB()
    {
        try {
            Connection conn = Kinoverwaltung.setupConnection();
            Statement stat = conn.createStatement();
            String sql = "INSERT INTO vc_platzkategorie (name, zuschlagFix) VALUES(" + name + "," + zuschlagFix + ");";
            stat.executeUpdate(sql);
            conn.close();
        }
        catch (ClassNotFoundException | SQLException e){e.printStackTrace();}
    }

    public void setTempFreiePlaetze(Vorstellung vorstellung, int fromCurrentBooking) {
        this.hasTempFreiePlaetze = true;
        this.tempFreiePlaetze = getFreiePlaetze(vorstellung);
        if (fromCurrentBooking >= 0) {
            this.tempFreiePlaetze -= fromCurrentBooking;
        }
    }

    public int getID() {
        return ID;
    }

    public String getName() {
        return name;
    }

    public double getZuschlagFix() {
        return zuschlagFix;
    }

    public boolean hasTempFreiePlaetze() {
        return hasTempFreiePlaetze;
    }

    public int getTempFreiePlaetze() {
        return tempFreiePlaetze;
    }

    public boolean hasID(int ID) {
        return this.ID == ID;
    }

    @Override
    public String toString() {
        String string = "";
        string += name;
        if (this.hasTempFreiePlaetze) {
            string += " (" + this.tempFreiePlaetze + " frei)";
        }
        return string;
    }
}
