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
            System.err.println(e);
        }
    }

    public static int getLastIDFromDB()
    {
        try {
            Connection conn = Kinoverwaltung.setupConnection();
            Statement stat = conn.createStatement();
            String sql = "SELECT ID FROM vc_buchung ORDER BY ID DESC LIMIT 1;";
            ResultSet rs = stat.executeQuery(sql);
            conn.close();
            return rs.getInt("ID");
        }
        catch (ClassNotFoundException | SQLException e)
        {
            System.err.println(e);
        }
        return 0;
    }
}
