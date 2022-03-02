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
    private int karten;
    private String status;

    Buchung(int ID, String person, int vorstellung, int karten, String status) {
        this.ID = ID;
        this.person = person;
        this.vorstellung = vorstellung;
        this.karten = karten;
        this.status = status;
    }

    public void hinzufuegenKarte(int rabatt, int platzkategorie)
    {
        Karte karte = new Karte(rabatt, platzkategorie);
        karte.saveToDB();
        int kartenID = Karte.getLastIdFromDB();
        BuchungKarten karten = new BuchungKarten(this.ID, karte.getID());
        //karten.saveToDB();
        System.out.println(karte);
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
            String sql = "Insert into vc_buchung VALUES(" + person + "," + vorstellung + "," + karten + "," + status + ");";
            stat.executeUpdate(sql);
            conn.close();
        }
        catch (ClassNotFoundException | SQLException e)
        {
            System.err.println(e);
        }
    }
}
