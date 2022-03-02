package com.voncinema;

import java.sql.*;
import java.util.*;

public class Kinoverwaltung {
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
                        objects.add(new Buchung(rs.getInt("ID"), rs.getString("person"), rs.getInt("vorstellung"), rs.getInt("karten"), rs.getString("status")));
                        break;
                    case "BuchungKarten":
                        objects.add(new BuchungKarten(rs.getInt("ID"), rs.getInt("buchung"), rs.getInt("karte")));
                        break;
                    case "Film":
                        objects.add(new Film(rs.getInt("ID"), rs.getString("name"), rs.getString("beschreibung"), rs.getInt("laenge"), rs.getInt("kategorie"), rs.getString("genre")));
                        break;
                    case "Filmkategorie":
                        objects.add(new FilmKategorie(rs.getInt("ID"), rs.getString("name"), rs.getDouble("zuschlagProzent")));
                        break;
                    case "Karte":
                        objects.add(new Karte(rs.getInt("ID"), rs.getInt("rabatt"), rs.getInt("platzkategorie")));
                        break;
                    case "Kinosaal":
                        objects.add(new Kinosaal(rs.getInt("ID"), rs.getInt("konfiguration")));
                        break;
                    case "KinosaalKonfiguration":
                        objects.add(new KinosaalKonfiguration(rs.getInt("ID")));
                        break;
                    case "KinosaalKonfigurationPlatzkategorie":
                        objects.add(new KinosaalKonfigurationPlatzkategorie(rs.getInt("ID"), rs.getInt("konfiguration"), rs.getInt("kategorie"), rs.getInt("anzahl")));
                        break;
                    case "Platzkategorie":
                        objects.add(new Platzkategorie(rs.getInt("ID"), rs.getString("name"), rs.getDouble("zuschlagFix")));
                        break;
                    case "Rabatt":
                        objects.add(new Rabatt(rs.getInt("ID"), rs.getString("name"), rs.getDouble("wert")));
                        break;
                    case "Vorstellung":
                        objects.add(new Vorstellung(rs.getInt("ID"), rs.getInt("film"), rs.getInt("kinosaal"), rs.getString("beginn"), rs.getString("ende")));
                        break;
                }
            }

            rs.close();
            conn.close();
        }
        catch (ClassNotFoundException | SQLException e)
        {
            System.err.println(e);
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
                        objects.add(new Buchung(rs.getInt("ID"), rs.getString("person"), rs.getInt("vorstellung"), rs.getInt("karten"), rs.getString("status")));
                        break;
                    case "BuchungKarten":
                        objects.add(new BuchungKarten(rs.getInt("ID"), rs.getInt("buchung"), rs.getInt("karte")));
                        break;
                    case "Film":
                        objects.add(new Film(rs.getInt("ID"), rs.getString("name"), rs.getString("beschreibung"), rs.getInt("laenge"), rs.getInt("kategorie"), rs.getString("genre")));
                        break;
                    case "Filmkategorie":
                        objects.add(new FilmKategorie(rs.getInt("ID"), rs.getString("name"), rs.getDouble("zuschlagProzent")));
                        break;
                    case "Karte":
                        objects.add(new Karte(rs.getInt("ID"), rs.getInt("rabatt"), rs.getInt("platzkategorie")));
                        break;
                    case "Kinosaal":
                        objects.add(new Kinosaal(rs.getInt("ID"), rs.getInt("konfiguration")));
                        break;
                    case "KinosaalKonfiguration":
                        objects.add(new KinosaalKonfiguration(rs.getInt("ID")));
                        break;
                    case "KinosaalKonfigurationPlatzkategorie":
                        objects.add(new KinosaalKonfigurationPlatzkategorie(rs.getInt("ID"), rs.getInt("konfiguration"), rs.getInt("kategorie"), rs.getInt("anzahl")));
                        break;
                    case "Platzkategorie":
                        objects.add(new Platzkategorie(rs.getInt("ID"), rs.getString("name"), rs.getDouble("zuschlagFix")));
                        break;
                    case "Rabatt":
                        objects.add(new Rabatt(rs.getInt("ID"), rs.getString("name"), rs.getDouble("wert")));
                        break;
                    case "Vorstellung":
                        objects.add(new Vorstellung(rs.getInt("ID"), rs.getInt("film"), rs.getInt("kinosaal"), rs.getString("beginn"), rs.getString("ende")));
                        break;
                }
            }

            rs.close();
            conn.close();
        }
        catch (ClassNotFoundException | SQLException e)
        {
            System.err.println(e);
        }
        return objects;
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

    static Connection setupConnection() throws ClassNotFoundException, SQLException
    {
        Class.forName("org.sqlite.JDBC");
        return DriverManager.getConnection("jdbc:sqlite:voncinema_db.sqlite");
    }
}
