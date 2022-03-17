package com.voncinema;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Karte {
    private int ID,  platzkategorie, kartentyp;

    Karte( int platzkategorie, int kartentyp) {

        this.platzkategorie = platzkategorie;
        this.kartentyp = kartentyp;
    }

    Karte(int ID, int platzkategorie, int kartentyp) {
        this.ID = ID;
        this.platzkategorie = platzkategorie;
        this.kartentyp = kartentyp;
    }

    public double berechnePreis(double zuschlagFilm) {
        Kartentyp kartentyp = Kinoverwaltung.getKartentyp(this.kartentyp);
        double grundpreis = kartentyp.getPreis();
        Platzkategorie platzkategorie = Kinoverwaltung.getPlatzkategorie(this.platzkategorie);
        double zuschlagPlatz = platzkategorie.getZuschlagFix();
        double fixpreis = grundpreis + zuschlagPlatz;
        double zuschlag = fixpreis * (zuschlagFilm/100 + 1);
        double endpreis = zuschlag - (zuschlag);
        return endpreis;
    }

    public void saveToDB()
    {
        try {
            Connection conn = Kinoverwaltung.setupConnection();
            Statement stat = conn.createStatement();
            String sql = "INSERT INTO vc_karte (platzkategorie, kartentyp) VALUES("+ platzkategorie + "," + kartentyp + ");";
            stat.executeUpdate(sql);
            conn.close();
        }
        catch (ClassNotFoundException | SQLException e){e.printStackTrace();}
    }

    public void getLastIDFromDB()
    {
        try {
            Connection conn = Kinoverwaltung.setupConnection();
            Statement stat = conn.createStatement();
            String sql = "SELECT ID FROM vc_karte ORDER BY ID DESC LIMIT 1;";
            ResultSet rs = stat.executeQuery(sql);
            this.ID = rs.getInt("ID");
            rs.close();
            conn.close();
        }
        catch (ClassNotFoundException | SQLException e){e.printStackTrace();}
    }

    public int getID() {
        return ID;
    }

    public int getPlatzkategorie() {
        return platzkategorie;
    }

    public int getKartentyp() {
        return kartentyp;
    }

    @Override
    public String toString() {
        Platzkategorie objPlatzkategorie = Kinoverwaltung.getPlatzkategorie(platzkategorie);
        Kartentyp objKartentyp = Kinoverwaltung.getKartentyp(kartentyp);
        return objKartentyp + " (" + objPlatzkategorie + ")";
    }
}
