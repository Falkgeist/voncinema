package com.voncinema;

import java.sql.*;

public class Rabatt {
    private int ID;
    private String name;
    private double wert;

    Rabatt (int ID, String name, double wert) {
        this.ID = ID;
        this.name = name;
        this.wert = wert;
    }

    public static int findIDByString(String name) {
        int id = 0;
        try {
            Connection conn = Kinoverwaltung.setupConnection();
            String sql = "select id from vc_rabatt where name=?;";
            PreparedStatement stat = conn.prepareStatement(sql);
            stat.setString(1, name);
            ResultSet rs = stat.executeQuery();

            while (rs.next()) {
                id = rs.getInt("ID");
            }

            rs.close();
            conn.close();
        }
        catch (ClassNotFoundException | SQLException e)
        {
            System.err.println(e);
        }
        return id;
    }

    public double getWert() {
        return wert;
    }

    public void saveToDB()
    {
        try {
            Connection conn = Kinoverwaltung.setupConnection();
            Statement stat = conn.createStatement();
            String sql = "INSERT INTO vc_rabatt (name, wert) VALUES(" + name + "," + wert + ");";
            stat.executeUpdate(sql);
            conn.close();
        }
        catch (ClassNotFoundException | SQLException e)
        {
            System.err.println(e);
        }
    }

    public boolean hasID(int ID) {
        if (this.ID == ID) {
            return true;
        }
        return false;
    }
}
