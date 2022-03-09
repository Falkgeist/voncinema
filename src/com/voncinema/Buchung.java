package com.voncinema;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class Buchung {
    private int ID;
    private String person;
    private int vorstellung;
    private String status;
    private ArrayList<Karte> karten = new ArrayList<>();

    Buchung() {
    }

    Buchung(String person, int vorstellung) {
        this.person = person;
        this.vorstellung = vorstellung;
    }

    Buchung(int ID, String person, int vorstellung, String status) {
        this.ID = ID;
        this.person = person;
        this.vorstellung = vorstellung;
        this.status = status;
    }



    public void hinzufuegenKarte(String rabattcode, int platzkategorie, int kartentyp)
    {
        int rabattID = Rabatt.findIDByString(rabattcode);
        Karte karte = new Karte(rabattID, platzkategorie, kartentyp);
        karten.add(karte);
    }

    public void setVorstellung()
    {

    }

    public void bezahleBuchung()
    {

    }

    public void berechnePreis()
    {

    }

    public void saveToDB()
    {
        try {
            Connection conn = Kinoverwaltung.setupConnection();
            Statement stat = conn.createStatement();
            String sql = "Insert into vc_buchung VALUES(" + person + "," + vorstellung + "," + status + ");";
            stat.executeUpdate(sql);
            conn.close();
        }
        catch (ClassNotFoundException | SQLException e)
        {
            System.err.println(e);
        }
    }
}
