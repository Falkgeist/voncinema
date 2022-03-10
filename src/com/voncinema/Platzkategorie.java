package com.voncinema;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class Platzkategorie {
    private int ID;
    private String name;
    private double zuschlagFix;

    Platzkategorie(int ID, String name, double zuschlagFix) {
        this.ID = ID;
        this.name = name;
        this.zuschlagFix = zuschlagFix;
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
        catch (ClassNotFoundException | SQLException e)
        {
            System.err.println(e);
        }
    }

    public int getID() {
        return ID;
    }

    @Override
    public String toString() {
        return name;
    }
}
