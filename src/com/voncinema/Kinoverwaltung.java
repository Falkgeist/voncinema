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
    private static ArrayList<User> users = new ArrayList<>();
    private static ArrayList<Vorstellung> vorstellungen = new ArrayList<>();

    public static ArrayList<Object> getFromDB(String databaseTable)
    {
        ArrayList<Object> objects = new ArrayList<>();
        Kinoverwaltung.clearTable(databaseTable);
        try {
            Connection conn = setupConnection();
            Statement stat = conn.createStatement();
            String sql = "select * from "+databaseTable+";";
            ResultSet rs = stat.executeQuery(sql);

            while (rs.next()) {
                switch (databaseTable) {
                    case "vc_buchung":
                        Buchung buchung = new Buchung(rs.getInt("ID"), rs.getString("person"), rs.getInt("vorstellung"), rs.getString("status"), rs.getInt("rabatt"));
                        objects.add(buchung);
                        Kinoverwaltung.buchungen.add(buchung);
                        break;
                    case "vc_buchung_karten":
                        BuchungKarten objBuchungKarten = new BuchungKarten(rs.getInt("ID"), rs.getInt("buchung"), rs.getInt("karte"));
                        objects.add(objBuchungKarten);
                        Kinoverwaltung.buchungKarten.add(objBuchungKarten);
                        break;
                    case "vc_film":
                        Film film = new Film(rs.getInt("ID"), rs.getString("name"), rs.getString("beschreibung"), rs.getInt("laenge"), rs.getInt("kategorie"), rs.getString("genre"));
                        objects.add(film);
                        Kinoverwaltung.filme.add(film);
                        break;
                    case "vc_film_kategorie":
                        FilmKategorie filmKategorie = new FilmKategorie(rs.getInt("ID"), rs.getString("name"), rs.getDouble("zuschlagProzent"));
                        objects.add(filmKategorie);
                        Kinoverwaltung.filmKategorien.add(filmKategorie);
                        break;
                    case "vc_karte":
                        Karte karte = new Karte(rs.getInt("ID"), rs.getInt("platzkategorie"), rs.getInt("kartentyp"));
                        objects.add(karte);
                        Kinoverwaltung.karten.add(karte);
                        break;
                    case "vc_kartentyp":
                        Kartentyp kartentyp = new Kartentyp(rs.getInt("ID"), rs.getString("name"), rs.getDouble("preis"));
                        objects.add(kartentyp);
                        Kinoverwaltung.kartentypen.add(kartentyp);
                        break;
                    case "vc_kinosaal":
                        Kinosaal kinosaal = new Kinosaal(rs.getInt("ID"), rs.getInt("konfiguration"));
                        objects.add(kinosaal);
                        Kinoverwaltung.kinosaele.add(kinosaal);
                        break;
                    case "vc_kinosaalkonfiguration":
                        KinosaalKonfiguration kinosaalKonfiguration = new KinosaalKonfiguration(rs.getInt("ID"));
                        objects.add(kinosaalKonfiguration);
                        Kinoverwaltung.kinosaalKonfigurationen.add(kinosaalKonfiguration);
                        break;
                    case "vc_kinosaalkonfiguration_platzkategorie":
                        KinosaalKonfigurationPlatzkategorie kinosaalKonfigurationPlatzkategorie = new KinosaalKonfigurationPlatzkategorie(rs.getInt("ID"), rs.getInt("konfiguration"), rs.getInt("kategorie"), rs.getInt("anzahl"));
                        objects.add(kinosaalKonfigurationPlatzkategorie);
                        Kinoverwaltung.kinosaalKonfigurationPlatzkategorien.add(kinosaalKonfigurationPlatzkategorie);
                        break;
                    case "vc_platzkategorie":
                        Platzkategorie platzkategorie = new Platzkategorie(rs.getInt("ID"), rs.getString("name"), rs.getDouble("zuschlagFix"));
                        objects.add(platzkategorie);
                        Kinoverwaltung.platzkategorien.add(platzkategorie);
                        break;
                    case "vc_rabatt":
                        Rabatt rabatt = new Rabatt(rs.getInt("ID"), rs.getString("name"), rs.getDouble("wert"));
                        objects.add(rabatt);
                        Kinoverwaltung.rabatte.add(rabatt);
                        break;
                    case "vc_user":
                        User user = new User(rs.getInt("ID"), rs.getString("email"), rs.getString("password"));
                        objects.add(user);
                        Kinoverwaltung.users.add(user);
                        break;
                    case "vc_vorstellung":
                        Vorstellung vorstellung = new Vorstellung(rs.getInt("ID"), rs.getInt("film"), rs.getInt("kinosaal"), rs.getString("uhrzeit"));
                        objects.add(vorstellung);
                        Kinoverwaltung.vorstellungen.add(vorstellung);
                        break;
                }
            }
            rs.close();
            conn.close();
        }
        catch (ClassNotFoundException | SQLException e){e.printStackTrace();}
        return objects;
    }

    public static ArrayList<Object> getFromDB(String databaseTable, String query)
    {
        ArrayList<Object> objects = new ArrayList<>();
        try {
            Connection conn = setupConnection();
            Statement stat = conn.createStatement();
            String sql = "select * from "+databaseTable+" "+query+";";
            ResultSet rs = stat.executeQuery(sql);

            while (rs.next()) {
                switch (databaseTable) {
                    case "vc_buchung":
                        Buchung buchung = new Buchung(rs.getInt("ID"), rs.getString("person"), rs.getInt("vorstellung"), rs.getString("status"), rs.getInt("rabatt"));
                        objects.add(buchung);
                        break;
                    case "vc_buchung_karten":
                        BuchungKarten objBuchungKarten = new BuchungKarten(rs.getInt("ID"), rs.getInt("buchung"), rs.getInt("karte"));
                        objects.add(objBuchungKarten);
                        break;
                    case "vc_film":
                        Film film = new Film(rs.getInt("ID"), rs.getString("name"), rs.getString("beschreibung"), rs.getInt("laenge"), rs.getInt("kategorie"), rs.getString("genre"));
                        objects.add(film);
                        break;
                    case "vc_film_kategorie":
                        FilmKategorie filmKategorie = new FilmKategorie(rs.getInt("ID"), rs.getString("name"), rs.getDouble("zuschlagProzent"));
                        objects.add(filmKategorie);
                        break;
                    case "vc_karte":
                        Karte karte = new Karte(rs.getInt("ID"), rs.getInt("platzkategorie"), rs.getInt("kartentyp"));
                        objects.add(karte);
                        break;
                    case "vc_kartentyp":
                        Kartentyp kartentyp = new Kartentyp(rs.getInt("ID"), rs.getString("name"), rs.getDouble("preis"));
                        objects.add(kartentyp);
                        break;
                    case "vc_kinosaal":
                        Kinosaal kinosaal = new Kinosaal(rs.getInt("ID"), rs.getInt("konfiguration"));
                        objects.add(kinosaal);
                        break;
                    case "vc_kinosaalkonfiguration":
                        KinosaalKonfiguration kinosaalKonfiguration = new KinosaalKonfiguration(rs.getInt("ID"));
                        objects.add(kinosaalKonfiguration);
                        break;
                    case "vc_kinosaalkonfiguration_platzkategorie":
                        KinosaalKonfigurationPlatzkategorie kinosaalKonfigurationPlatzkategorie = new KinosaalKonfigurationPlatzkategorie(rs.getInt("ID"), rs.getInt("konfiguration"), rs.getInt("kategorie"), rs.getInt("anzahl"));
                        objects.add(kinosaalKonfigurationPlatzkategorie);
                        break;
                    case "vc_platzkategorie":
                        Platzkategorie platzkategorie = new Platzkategorie(rs.getInt("ID"), rs.getString("name"), rs.getDouble("zuschlagFix"));
                        objects.add(platzkategorie);
                        break;
                    case "vc_rabatt":
                        Rabatt rabatt = new Rabatt(rs.getInt("ID"), rs.getString("name"), rs.getDouble("wert"));
                        objects.add(rabatt);
                        break;
                    case "vc_user":
                        User user = new User(rs.getInt("ID"), rs.getString("email"), rs.getString("password"));
                        objects.add(user);
                        break;
                    case "vc_vorstellung":
                        Vorstellung vorstellung = new Vorstellung(rs.getInt("ID"), rs.getInt("film"), rs.getInt("kinosaal"), rs.getString("uhrzeit"));
                        objects.add(vorstellung);
                        break;
                }
            }
            rs.close();
            conn.close();
        }
        catch (ClassNotFoundException | SQLException e){e.printStackTrace();}
        return objects;
    }

    private static void clearTable(String databaseTable) {
        switch (databaseTable) {
            case "vc_buchung":
                Kinoverwaltung.buchungen.clear();
                break;
            case "vc_buchung_karten":
                Kinoverwaltung.buchungKarten.clear();
                break;
            case "vc_film":
                Kinoverwaltung.filme.clear();
                break;
            case "vc_film_kategorie":
                Kinoverwaltung.filmKategorien.clear();
                break;
            case "vc_karte":
                Kinoverwaltung.karten.clear();
                break;
            case "vc_kartentyp":
                Kinoverwaltung.kartentypen.clear();
                break;
            case "vc_kinosaal":
                Kinoverwaltung.kinosaele.clear();
                break;
            case "vc_kinosaalkonfiguration":
                Kinoverwaltung.kinosaalKonfigurationen.clear();
                break;
            case "vc_kinosaalkonfiguration_platzkategorie":
                Kinoverwaltung.kinosaalKonfigurationPlatzkategorien.clear();
                break;
            case "vc_platzkategorie":
                Kinoverwaltung.platzkategorien.clear();
                break;
            case "vc_rabatt":
                Kinoverwaltung.rabatte.clear();
                break;
            case "vc_user":
                Kinoverwaltung.users.clear();
                break;
            case "vc_vorstellung":
                Kinoverwaltung.vorstellungen.clear();
                break;
        }
    }

    public static void bucheBuchung(Vorstellung vorstellung, Buchung buchung)
    {
        buchung.saveToDB();
        buchung.getLastIDFromDB();
        buchung.speichernKarten();
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

    public static User getUserByEmail(String email) {
        User returnUser = null;
        for (User user : users) {
            if (user.hasEmail(email)){
                returnUser = user;
                break;
            }
        }
        return returnUser;
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

    public static Kinosaal getKinosaal(int ID) {
        Kinosaal returnKinosaal = null;
        for (Kinosaal kinosaal : kinosaele) {
            if (kinosaal.hasID(ID)){
                returnKinosaal = kinosaal;
                break;
            }
        }
        return returnKinosaal;
    }

    public static KinosaalKonfiguration getKinosaalKonfiguration(int ID) {
        KinosaalKonfiguration returnKinosaalKonfiguration = null;
        for (KinosaalKonfiguration kinosaalKonfiguration : kinosaalKonfigurationen) {
            if (kinosaalKonfiguration.hasID(ID)){
                returnKinosaalKonfiguration = kinosaalKonfiguration;
                break;
            }
        }
        return returnKinosaalKonfiguration;
    }

    public static KinosaalKonfigurationPlatzkategorie getKinosaalKonfigurationPlatzkategorie(int ID) {
        KinosaalKonfigurationPlatzkategorie returnKinosaalKonfigurationPlatzkategorie = null;
        for (KinosaalKonfigurationPlatzkategorie kinosaalKonfigurationPlatzkategorie : kinosaalKonfigurationPlatzkategorien) {
            if (kinosaalKonfigurationPlatzkategorie.hasID(ID)){
                returnKinosaalKonfigurationPlatzkategorie = kinosaalKonfigurationPlatzkategorie;
                break;
            }
        }
        return returnKinosaalKonfigurationPlatzkategorie;
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

    public static User getUser(int ID) {
        User returnUser = null;
        for (User user : users) {
            if (user.hasID(ID)){
                returnUser = user;
                break;
            }
        }
        return returnUser;
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
