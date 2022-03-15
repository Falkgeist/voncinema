package com.voncinema;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Karte {
    private int ID, rabatt, platzkategorie, kartentyp;

    Karte(int rabatt, int platzkategorie, int kartentyp) {
        this.rabatt = rabatt;
        this.platzkategorie = platzkategorie;
        this.kartentyp = kartentyp;
    }

    Karte(int ID, int rabatt, int platzkategorie, int kartentyp) {
        this.ID = ID;
        this.rabatt = rabatt;
        this.platzkategorie = platzkategorie;
        this.kartentyp = kartentyp;
    }

    public double berechnePreis(double zuschlagFilm) {
        Kartentyp kartentyp = Kinoverwaltung.getKartentyp(this.kartentyp);
        double grundpreis = kartentyp.getPreis();
        Platzkategorie platzkategorie = Kinoverwaltung.getPlatzkategorie(this.platzkategorie);
        double zuschlagPlatz = platzkategorie.getZuschlagFix();
        double rabatt = 0.0;
        if (this.rabatt != 0) {
            Rabatt objRabatt = Kinoverwaltung.getRabatt(this.rabatt);
            rabatt = objRabatt.getWert();
        }
        double fixpreis = grundpreis + zuschlagPlatz;
        double zuschlag = fixpreis * (zuschlagFilm/100 + 1);
        double endpreis = zuschlag - (zuschlag * rabatt);
        return endpreis;
    }

    public void saveToDB()
    {
        try {
            Connection conn = Kinoverwaltung.setupConnection();
            Statement stat = conn.createStatement();
            String sql = "INSERT INTO vc_karte (rabatt, platzkategorie, kartentyp) VALUES(" + rabatt + "," + platzkategorie + "," + kartentyp + ");";
            stat.executeUpdate(sql);
            conn.close();
        }
        catch (ClassNotFoundException | SQLException e)
        {
            e.printStackTrace();
        }
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
        catch (ClassNotFoundException | SQLException e)
        {
            e.printStackTrace();
        }
    }

    public int getID() {
        return ID;
    }
}
