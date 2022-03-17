package com.voncinema;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

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

    public void saveToDB()
    {
        try {
            Connection conn = Kinoverwaltung.setupConnection();
            Statement stat = conn.createStatement();
            String sql = "INSERT INTO vc_buchung_karten (buchung, karte) VALUES(" + this.buchung + "," + this.karte + ");";
            stat.executeUpdate(sql);
            conn.close();
        }
        catch (ClassNotFoundException | SQLException e){e.printStackTrace();}
    }
}
