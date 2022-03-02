package com.voncinema;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class Karte {
    private int ID, rabatt, platzkategorie;

    Karte(int ID, int rabatt, int platzkategorie) {
        this.ID = ID;
        this.rabatt = rabatt;
        this.platzkategorie = platzkategorie;
    }

    public void saveToDB()
    {
        try {
            Connection conn = Kinoverwaltung.setupConnection();
            Statement stat = conn.createStatement();
            String sql = "INSERT INTO vc_karte VALUES(" + rabatt + "," + platzkategorie + ");";
            stat.executeUpdate(sql);
            conn.close();
        }
        catch (ClassNotFoundException | SQLException e)
        {
            System.err.println(e);
        }
    }
}
