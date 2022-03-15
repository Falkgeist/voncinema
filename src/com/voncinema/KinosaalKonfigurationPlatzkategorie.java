package com.voncinema;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

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

    public void saveToDB()
    {
        try {
            Connection conn = Kinoverwaltung.setupConnection();
            Statement stat = conn.createStatement();
            String sql = "INSERT INTO vc_kinosaalkonfiguration_platzkategorie (konfiguration, kategorie, anzahl) VALUES(" + konfiguration + "," + platzkategorie + "," + anzahl + ");";
            stat.executeUpdate(sql);
            conn.close();
        }
        catch (ClassNotFoundException | SQLException e)
        {
            e.printStackTrace();
        }
    }
}
