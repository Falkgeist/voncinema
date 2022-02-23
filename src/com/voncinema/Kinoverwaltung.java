package com.voncinema;

import java.sql.*;
import java.util.*;

public class Kinoverwaltung {
    public ArrayList<Film> getFilme()
    {
        ArrayList<Film> filme = new ArrayList<>();
        try {
            Connection conn = setupConnection();
            Statement stat = conn.createStatement();
            String sql = "select * from vc_film;";
            ResultSet rs = stat.executeQuery(sql);

            while (rs.next()) {
                filme.add(new Film(rs.getInt("ID"), rs.getString("name"), rs.getString("beschreibung"), rs.getInt("laenge"), rs.getInt("kategorie"), rs.getString("genre")));
            }

            rs.close();
            conn.close();
        }
        catch (ClassNotFoundException | SQLException e)
        {
            System.err.println(e);
        }
        return filme;
    }

    public void getVorstellungen()
    {

    }

    public void bucheBuchung()
    {

    }

    public void storniereBuchung()
    {

    }

    private Connection setupConnection() throws ClassNotFoundException, SQLException {
        Class.forName("org.sqlite.JDBC");
        return DriverManager.getConnection("jdbc:sqlite:voncinema_db.sqlite");
    }
}
