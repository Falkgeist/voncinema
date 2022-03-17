package com.voncinema;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Objects;

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

    public void hinzufuegenKarte(Karte karte)
    {
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

    public double berechneGesamtpreis()
    {
        // Hole den prozentualen Zuschlag über die Vorstellung, den Film und entsprechend die Kategorie
        ArrayList<Object> resultVorstellung = Kinoverwaltung.getFromDB("vc_vorstellung", "WHERE id="+this.vorstellung);
        Vorstellung vorstellung = (Vorstellung) resultVorstellung.get(0);
        ArrayList<Object> resultFilm = Kinoverwaltung.getFromDB("vc_film", "WHERE id="+vorstellung.getFilm());
        Film film = (Film) resultFilm.get(0);
        ArrayList<Object> resultKategorie = Kinoverwaltung.getFromDB("vc_film_kategorie", "WHERE id="+film.getKategorie());
        FilmKategorie kategorie = (FilmKategorie) resultKategorie.get(0);
        double filmZuschlag = kategorie.getZuschlagProzent();
        double sum = 0;
        for (Karte karte : karten) {
            sum += karte.berechnePreis(filmZuschlag);
        }
        BigDecimal decSum = new BigDecimal(sum).setScale(2, RoundingMode.HALF_UP);
        sum = decSum.doubleValue();
        return sum;
    }

    public void setStatus(String status)
    {
        try {
            Connection conn = Kinoverwaltung.setupConnection();
            Statement stat = conn.createStatement();
            String sql = "UPDATE vc_buchung status = WHERE '" + status + "'ID = " + this.ID +";";
            stat.executeQuery(sql);
            conn.close();
        }
        catch (ClassNotFoundException | SQLException e)
        {
            e.printStackTrace();
        }
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

    public ArrayList<Karte> getKarten() {
        return karten;
    }

    public String getKartenAsList() {
        StringBuilder list = new StringBuilder();
        int anzahlKarten = 0;
        for (Karte karte : karten) {
            list.append("- ").append(karte).append("\n");
        }
        return list.toString();
    }

    public void setPerson(String person) {
        this.person = person;
    }

    public void setVorstellung(int vorstellung) {
        this.vorstellung = vorstellung;
    }

    public boolean hasName(String name) {
        return Objects.equals(this.person, name);
    }

    @Override
    public String toString() {
        Vorstellung objVorstellung = Kinoverwaltung.getVorstellung(this.vorstellung);
        Film objFilm = Kinoverwaltung.getFilm(objVorstellung.getFilm());
        return "Film: " + objFilm + "\n" +
                "Uhrzeit: " + objVorstellung + "\n" +
                "Preis: " + String.format("%.2f", this.berechneGesamtpreis()) + " €";
    }

    public String toHTML() {
        Vorstellung objVorstellung = Kinoverwaltung.getVorstellung(this.vorstellung);
        Film objFilm = Kinoverwaltung.getFilm(objVorstellung.getFilm());
        return "<html>Film: " + objFilm + "<br>" +
                "Uhrzeit: " + objVorstellung + "<br>" +
                "Preis: " + String.format("%.2f", this.berechneGesamtpreis()) + " €</html>";
    }
}
