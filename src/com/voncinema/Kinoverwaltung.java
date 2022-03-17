package com.voncinema;

import java.sql.*;
import java.util.*;

public class Kinoverwaltung {
    private static ArrayList<Buchung> buchungen = new ArrayList<>();
    private static ArrayList<BuchungKarten> buchungKarten = new ArrayList<>();
    private static ArrayList<Film> filme = new ArrayList<>();
    private static ArrayList<FilmKategorie> filmKategorien = new ArrayList<>();
    private static ArrayList<Karte> karten = new ArrayList<>();
    private static ArrayList<Kartentyp> kartentypen = new ArrayList<>();
    private static ArrayList<Kinosaal> kinosaele = new ArrayList<>();
    private static ArrayList<KinosaalKonfiguration> kinosaalKonfigurationen = new ArrayList<>();
    private static ArrayList<KinosaalKonfigurationPlatzkategorie> kinosaalKonfigurationPlatzkategorien = new ArrayList<>();
    private static ArrayList<Platzkategorie> platzkategorien = new ArrayList<>();
    private static ArrayList<Rabatt> rabatte = new ArrayList<>();
    private static ArrayList<Vorstellung> vorstellungen = new ArrayList<>();

    public static ArrayList<Object> getFromDB(String objectClass, String databaseTable)
    {
        ArrayList<Object> objects = new ArrayList<>();
        try {
            Connection conn = setupConnection();
            Statement stat = conn.createStatement();
            String sql = "select * from "+databaseTable+";";
            ResultSet rs = stat.executeQuery(sql);

            while (rs.next()) {
                switch (objectClass) {
                    case "Buchung":
                        Buchung buchung = new Buchung(rs.getInt("ID"), rs.getString("person"), rs.getInt("vorstellung"), rs.getString("status"));
                        objects.add(buchung);
                        buchungen.add(buchung);
                        break;
                    case "BuchungKarten":
                        BuchungKarten objBuchungKarten = new BuchungKarten(rs.getInt("ID"), rs.getInt("buchung"), rs.getInt("karte"));
                        objects.add(objBuchungKarten);
                        buchungKarten.add(objBuchungKarten);
                        break;
                    case "Film":
                        Film film = new Film(rs.getInt("ID"), rs.getString("name"), rs.getString("beschreibung"), rs.getInt("laenge"), rs.getInt("kategorie"), rs.getString("genre"));
                        objects.add(film);
                        filme.add(film);
                        break;
                    case "FilmKategorie":
                        FilmKategorie filmKategorie = new FilmKategorie(rs.getInt("ID"), rs.getString("name"), rs.getDouble("zuschlagProzent"));
                        objects.add(filmKategorie);
                        filmKategorien.add(filmKategorie);
                        break;
                    case "Karte":
                        Karte karte = new Karte(rs.getInt("ID"), rs.getInt("rabatt"), rs.getInt("platzkategorie"));
                        objects.add(karte);
                        karten.add(karte);
                        break;
                    case "Kartentyp":
                        Kartentyp kartentyp = new Kartentyp(rs.getInt("ID"), rs.getString("name"), rs.getDouble("preis"));
                        objects.add(kartentyp);
                        kartentypen.add(kartentyp);
                        break;
                    case "Kinosaal":
                        Kinosaal kinosaal = new Kinosaal(rs.getInt("ID"), rs.getInt("konfiguration"));
                        objects.add(kinosaal);
                        kinosaele.add(kinosaal);
                        break;
                    case "KinosaalKonfiguration":
                        KinosaalKonfiguration kinosaalKonfiguration = new KinosaalKonfiguration(rs.getInt("ID"));
                        objects.add(kinosaalKonfiguration);
                        kinosaalKonfigurationen.add(kinosaalKonfiguration);
                        break;
                    case "KinosaalKonfigurationPlatzkategorie":
                        KinosaalKonfigurationPlatzkategorie kinosaalKonfigurationPlatzkategorie = new KinosaalKonfigurationPlatzkategorie(rs.getInt("ID"), rs.getInt("konfiguration"), rs.getInt("kategorie"), rs.getInt("anzahl"));
                        objects.add(kinosaalKonfigurationPlatzkategorie);
                        kinosaalKonfigurationPlatzkategorien.add(kinosaalKonfigurationPlatzkategorie);
                        break;
                    case "Platzkategorie":
                        Platzkategorie platzkategorie = new Platzkategorie(rs.getInt("ID"), rs.getString("name"), rs.getDouble("zuschlagFix"));
                        objects.add(platzkategorie);
                        platzkategorien.add(platzkategorie);
                        break;
                    case "Rabatt":
                        Rabatt rabatt = new Rabatt(rs.getInt("ID"), rs.getString("name"), rs.getDouble("wert"));
                        objects.add(rabatt);
                        rabatte.add(rabatt);
                        break;
                    case "Vorstellung":
                        Vorstellung vorstellung = new Vorstellung(rs.getInt("ID"), rs.getInt("film"), rs.getInt("kinosaal"), rs.getString("beginn"), rs.getString("ende"));
                        objects.add(vorstellung);
                        vorstellungen.add(vorstellung);
                        break;
                }
            }

            rs.close();
            conn.close();
        }
        catch (ClassNotFoundException | SQLException e)
        {
            e.printStackTrace();
        }
        return objects;
    }

    public static ArrayList<Object> getFromDB(String objectClass, String databaseTable, String query)
    {
        ArrayList<Object> objects = new ArrayList<>();
        try {
            Connection conn = setupConnection();
            Statement stat = conn.createStatement();
            String sql = "select * from "+databaseTable+" "+query+";";
            ResultSet rs = stat.executeQuery(sql);

            while (rs.next()) {
                switch (objectClass) {
                    case "Buchung":
                        Buchung buchung = new Buchung(rs.getInt("ID"), rs.getString("person"), rs.getInt("vorstellung"), rs.getString("status"));
                        objects.add(buchung);
                        Kinoverwaltung.buchungen.add(buchung);
                        break;
                    case "BuchungKarten":
                        BuchungKarten buchungKarten = new BuchungKarten(rs.getInt("ID"), rs.getInt("buchung"), rs.getInt("karte"));
                        objects.add(buchungKarten);
                        Kinoverwaltung.buchungKarten.add(buchungKarten);
                        break;
                    case "Film":
                        Film film = new Film(rs.getInt("ID"), rs.getString("name"), rs.getString("beschreibung"), rs.getInt("laenge"), rs.getInt("kategorie"), rs.getString("genre"));
                        objects.add(film);
                        Kinoverwaltung.filme.add(film);
                        break;
                    case "FilmKategorie":
                        FilmKategorie filmKategorie = new FilmKategorie(rs.getInt("ID"), rs.getString("name"), rs.getDouble("zuschlagProzent"));
                        objects.add(filmKategorie);
                        Kinoverwaltung.filmKategorien.add(filmKategorie);
                        break;
                    case "Karte":
                        Karte karte = new Karte(rs.getInt("ID"), rs.getInt("rabatt"), rs.getInt("platzkategorie"));
                        objects.add(karte);
                        Kinoverwaltung.karten.add(karte);
                        break;
                    case "Kartentyp":
                        Kartentyp kartentyp = new Kartentyp(rs.getInt("ID"), rs.getString("name"), rs.getDouble("preis"));
                        objects.add(kartentyp);
                        Kinoverwaltung.kartentypen.add(kartentyp);
                        break;
                    case "Kinosaal":
                        Kinosaal kinosaal = new Kinosaal(rs.getInt("ID"), rs.getInt("konfiguration"));
                        objects.add(kinosaal);
                        Kinoverwaltung.kinosaele.add(kinosaal);
                        break;
                    case "KinosaalKonfiguration":
                        KinosaalKonfiguration kinosaalKonfiguration = new KinosaalKonfiguration(rs.getInt("ID"));
                        objects.add(kinosaalKonfiguration);
                        Kinoverwaltung.kinosaalKonfigurationen.add(kinosaalKonfiguration);
                        break;
                    case "KinosaalKonfigurationPlatzkategorie":
                        KinosaalKonfigurationPlatzkategorie kinosaalKonfigurationPlatzkategorie = new KinosaalKonfigurationPlatzkategorie(rs.getInt("ID"), rs.getInt("konfiguration"), rs.getInt("kategorie"), rs.getInt("anzahl"));
                        objects.add(kinosaalKonfigurationPlatzkategorie);
                        Kinoverwaltung.kinosaalKonfigurationPlatzkategorien.add(kinosaalKonfigurationPlatzkategorie);
                        break;
                    case "Platzkategorie":
                        Platzkategorie platzkategorie = new Platzkategorie(rs.getInt("ID"), rs.getString("name"), rs.getDouble("zuschlagFix"));
                        objects.add(platzkategorie);
                        Kinoverwaltung.platzkategorien.add(platzkategorie);
                        break;
                    case "Rabatt":
                        Rabatt rabatt = new Rabatt(rs.getInt("ID"), rs.getString("name"), rs.getDouble("wert"));
                        objects.add(rabatt);
                        Kinoverwaltung.rabatte.add(rabatt);
                        break;
                    case "Vorstellung":
                        Vorstellung vorstellung = new Vorstellung(rs.getInt("ID"), rs.getInt("film"), rs.getInt("kinosaal"), rs.getString("uhrzeit"));
                        objects.add(vorstellung);
                        Kinoverwaltung.vorstellungen.add(vorstellung);
                        break;
                }
            }

            rs.close();
            conn.close();
        }
        catch (ClassNotFoundException | SQLException e)
        {
            e.printStackTrace();
        }
        return objects;
    }

    public static void bucheBuchung(Vorstellung vorstellung, Buchung buchung)
    {
        buchung.saveToDB();
        buchung.getLastIDFromDB();
        buchung.speichernKarten();
    }

    public static void storniereBuchung()
    {

    }

    public static Connection setupConnection() throws ClassNotFoundException, SQLException
    {
        Class.forName("org.sqlite.JDBC");
        return DriverManager.getConnection("jdbc:sqlite:voncinema_db.sqlite");
    }

    public static ArrayList<Buchung> getBuchungenByName(String name) {
        ArrayList<Buchung> returnArrBuchung = new ArrayList<>();
        for (Buchung buchung : buchungen) {
            if (buchung.hasName(name)){
                returnArrBuchung.add(buchung);
            }
        }
        return returnArrBuchung;
    }

    public static Film getFilm(int ID){
        Film returnFilm = null;
        for (Film film : filme) {
            if (film.hasID(ID)){
                returnFilm = film;
                break;
            }
        }
        return returnFilm;
    }

    public static FilmKategorie getFilmKategorie(int ID) {
        FilmKategorie returnFilmKategorie = null;
        for (FilmKategorie filmKategorie : filmKategorien) {
            if (filmKategorie.hasID(ID)){
                returnFilmKategorie = filmKategorie;
                break;
            }
        }
        return returnFilmKategorie;
    }

    public static Kartentyp getKartentyp(int ID) {
        Kartentyp returnKartentyp = null;
        for (Kartentyp kartentyp : kartentypen) {
            if (kartentyp.hasID(ID)){
                returnKartentyp = kartentyp;
                break;
            }
        }
        return returnKartentyp;
    }

    public static Platzkategorie getPlatzkategorie(int ID) {
        Platzkategorie returnPlatzkategorie = null;
        for (Platzkategorie platzkategorie : platzkategorien) {
            if (platzkategorie.hasID(ID)){
                returnPlatzkategorie = platzkategorie;
                break;
            }
        }
        return returnPlatzkategorie;
    }

    public static Rabatt getRabatt(int ID) {
        Rabatt returnRabatt = null;
        for (Rabatt rabatt : rabatte) {
            if (rabatt.hasID(ID)){
                returnRabatt = rabatt;
                break;
            }
        }
        return returnRabatt;
    }

    public static Vorstellung getVorstellung(int ID) {
        Vorstellung returnVorstellung = null;
        for (Vorstellung vorstellung : vorstellungen) {
            if (vorstellung.hasID(ID)){
                returnVorstellung = vorstellung;
                break;
            }
        }
        return returnVorstellung;
    }
}
