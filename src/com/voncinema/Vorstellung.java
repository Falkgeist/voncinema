package com.voncinema;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class Vorstellung {
    private int ID, film, kinosaal;
    private String beginn, ende;

    Vorstellung(int ID, int film, int kinosaal, String beginn, String ende){
        this.ID = ID;
        this.film = film;
        this.kinosaal = kinosaal;
        this.beginn = beginn;
        this.ende = ende;
    }

    @Override
    public String toString() {
        return beginn + " - " +ende;
    }

    public void saveToDB()
    {
        try {
            Connection conn = Kinoverwaltung.setupConnection();
            Statement stat = conn.createStatement();
            String sql = "INSERT INTO vc_vorstellung (film, kinosaal, beginn, ende) VALUES(" + film + "," + kinosaal + "," + beginn + "," + ende +");";
            stat.executeUpdate(sql);
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
