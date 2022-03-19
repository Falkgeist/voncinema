package com.voncinema;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class Kinosaal {
    private int ID, konfiguration;

    Kinosaal(int ID, int konfiguration) {
        this.ID = ID;
        this.konfiguration = konfiguration;
    }

    public void saveToDB()
    {
        try {
            Connection conn = Kinoverwaltung.setupConnection();
            Statement stat = conn.createStatement();
            String sql = "INSERT INTO vc_kinosaal VALUES(" + konfiguration + ");";
            stat.executeUpdate(sql);
            conn.close();
        }
        catch (ClassNotFoundException | SQLException e){e.printStackTrace();}
    }

    public int getKonfiguration() {
        return konfiguration;
    }

    public boolean hasID(int ID) {
        return this.ID == ID;
    }
}
