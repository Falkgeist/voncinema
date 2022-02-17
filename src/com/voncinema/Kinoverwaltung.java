package com.voncinema;

import java.sql.*;
import java.util.*;

public class Kinoverwaltung {
    ArrayList<Film> filme = new ArrayList<Film>();

    Kinoverwaltung() {
        getFilme();
    }

    public void getFilme()
    {
        try {
            Connection conn = setupConnection();
            Statement stat = conn.createStatement();
            String sql = "select * from vc_film;";
            ResultSet rs = stat.executeQuery(sql);
            System.out.println();

            while (rs.next()) {
                this.filme.add(new Film(rs.getInt("ID"), rs.getString("name"), rs.getString("beschreibung"), rs.getInt("laenge")));
            }

            rs.close();
            conn.close();
        }
        catch (ClassNotFoundException | SQLException e)
        {
            System.err.println(e);
        }
    }

    public void anzeigenVorstellungen()
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
