package com.voncinema;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class Rabatt {
    private int ID;
    private String name;
    private double wert;

    Rabatt (int ID, String name, double wert) {
        this.ID = ID;
        this.name = name;
        this.wert = wert;
    }

    public void saveToDB()
    {
        try {
            Connection conn = Kinoverwaltung.setupConnection();
            Statement stat = conn.createStatement();
            String sql = "INSERT INTO vc_rabatt VALUES(" + name + "," + wert + ");";
            stat.executeUpdate(sql);
            conn.close();
        }
        catch (ClassNotFoundException | SQLException e)
        {
            System.err.println(e);
        }
    }
}
