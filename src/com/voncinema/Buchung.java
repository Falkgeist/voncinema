package com.voncinema;

import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class Buchung {
    private int ID;
    private String person;
    private int vorstellung;
    private ArrayList<Karte> karten = new ArrayList<>();
    private String status;

    Buchung(){}

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

    public void speichernKarten()
    {
        for (Karte karte : this.karten) {
            karte.saveToDB();
            karte.getLastIDFromDB();
            BuchungKarten buchungKarten = new BuchungKarten(this.ID, karte.getID());
            buchungKarten.saveToDB();
        }
    }

    public void setVorstellung()
    {

    }

    public void bezahleBuchung()
    {

    }

    public double berechneGesamtpreis()
    {
        // Hole den prozentualen Zuschlag über die Vorstellung, den Film und entsprechend die Kategorie
        ArrayList<Object> resultVorstellung = Kinoverwaltung.getFromDB("Vorstellung", "vc_vorstellung", "WHERE id="+this.vorstellung);
        Vorstellung vorstellung = (Vorstellung) resultVorstellung.get(0);
        ArrayList<Object> resultFilm = Kinoverwaltung.getFromDB("Film", "vc_film", "WHERE id="+vorstellung.getFilm());
        Film film = (Film) resultFilm.get(0);
        ArrayList<Object> resultKategorie = Kinoverwaltung.getFromDB("FilmKategorie", "vc_film_kategorie", "WHERE id="+film.getKategorie());
        FilmKategorie kategorie = (FilmKategorie) resultKategorie.get(0);
        double filmZuschlag = kategorie.getZuschlagProzent();
        double sum = 0;
        for (Karte karte : karten) {
            sum += karte.berechnePreis(filmZuschlag);
        }
        // TODO: round to 2 decimal places
        //DecimalFormat df = new DecimalFormat("#####0,00");
        //sum = Double.parseDouble(df.format(sum));
        return sum;
    }

    public void getLastIDFromDB()
    {
        try {
            Connection conn = Kinoverwaltung.setupConnection();
            Statement stat = conn.createStatement();
            String sql = "SELECT ID FROM vc_buchung ORDER BY ID DESC LIMIT 1;";
            ResultSet rs = stat.executeQuery(sql);
            this.ID = rs.getInt("ID");
            rs.close();
            conn.close();
        }
        catch (ClassNotFoundException | SQLException e)
        {
            e.printStackTrace();
        }
    }

    public void saveToDB()
    {
        try {
            Connection conn = Kinoverwaltung.setupConnection();
            Statement stat = conn.createStatement();
            String sql = "INSERT INTO vc_buchung (person, vorstellung) VALUES ('"+ person + "','" + vorstellung + "');";
            stat.executeUpdate(sql);
            conn.close();
        }
        catch (ClassNotFoundException | SQLException e)
        {
            e.printStackTrace();
        }
    }
    public void setPerson(String person) {
        this.person = person;
    }

    public void setVorstellung(int vorstellung) {
        this.vorstellung = vorstellung;
    }
}
