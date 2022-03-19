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
    private int rabatt = 0;
    private static double zuschlagUeberlaenge = 0.2; // für die Preisberechnung

    Buchung(){}

    Buchung(String person, int vorstellung) {
        this.person = person;
        this.vorstellung = vorstellung;
    }

    Buchung(int ID, String person, int vorstellung, String status, int rabatt) {
        this.ID = ID;
        this.person = person;
        this.vorstellung = vorstellung;
        this.status = status;
        this.rabatt = rabatt;
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
        double gesamtpreis = 0;
        try {
            //läd die raten IDs in das ResultSet rs_vc_buchung_karten
            Connection conn = Kinoverwaltung.setupConnection();
            Statement stat = conn.createStatement();
            String sql = "SELECT karte FROM vc_buchung_karten WHERE buchung = " + ID + ";";
            ResultSet rs_vc_buchung_karten = stat.executeQuery(sql);
            //durchläuft alle Karten
            while(rs_vc_buchung_karten.next()){
                //läd aus der Tabelle vc_karten die Platzkategorie und läd dann aus der Tabelle vc_platzkategorie dem zuschlagFix Wert
                //und rechnet diesen zu dem gesamtpreis
                Statement stat2 = conn.createStatement();
                sql ="SELECT zuschlagFIX FROM vc_platzkategorie WHERE ID = (SELECT platzkategorie FROM vc_karte WHERE ID = "  + rs_vc_buchung_karten.getInt("karte") +");";
                ResultSet rs_vc_karte = stat2.executeQuery(sql);
                gesamtpreis += rs_vc_karte.getInt("zuschlagFix");
                rs_vc_karte.close();

                //läd aus der Tabelle vc_karten den kartentyp und läd dann aus der Tabelle vc_kartentyp den preis
                //und rechnet diesen zu dem gesamtpreis
                Statement stat3 = conn.createStatement();
                sql ="SELECT preis FROM vc_kartentyp WHERE ID = (SELECT kartentyp FROM vc_karte WHERE ID = "  + rs_vc_buchung_karten.getInt("karte") +");";
                ResultSet vc_kartentyp = stat3.executeQuery(sql);
                gesamtpreis += vc_kartentyp.getInt("preis");
                vc_kartentyp.close();
            }
            rs_vc_buchung_karten.close();


            //läd den Film der Vorstellung, um in der Tabelle vc_Film die kategorie.ID zu laden, um in der Tabelle vc_film_kategorie den zuschlagProzent zu laden
            //rechnet dann diesen zuschlag auf den gesamtpreis
            stat = conn.createStatement();
            sql = "SELECT zuschlagProzent FROM vc_film_kategorie WHERE ID = (SELECT kategorie FROM vc_film WHERE ID = (SELECT film FROM vc_vorstellung WHERE ID = "+ this.vorstellung+"));";
            ResultSet rs_vc_film_kategorie = stat.executeQuery(sql);
            gesamtpreis *= 1 + ((double)rs_vc_film_kategorie.getInt("zuschlagProzent") / 100);
            rs_vc_film_kategorie.close();

            //läd aus der Tabelle vc_vorstellung den Film, um aus der Tabelle vc_film die länge zu laden
            //Wenn der Film länger ist als 180 Minuten wird der zuschlagUeberlanege dazugerechnet
            sql = "SELECT laenge FROM vc_film WHERE ID = (SELECT film FROM vc_vorstellung WHERE ID = "+ this.vorstellung+");";
            ResultSet rs_vc_film = stat.executeQuery(sql);
            if(rs_vc_film.getInt("laenge") > 180){
                gesamtpreis *= (1 + zuschlagUeberlaenge);
            }

            //läd aus der Tabelle vc_buchung den rabatt
            Statement stat2 = conn.createStatement();
            sql = "SELECT rabatt FROM vc_buchung WHERE ID = " + this.ID +";";
            ResultSet rs_vc_buchung = stat2.executeQuery(sql);
            rabatt = rs_vc_buchung.getInt("rabatt");
            rs_vc_buchung.close();

            //wenn die Buchung einen Rabatt hat wird der wert aus der Tabelle vc_rabatt mit dem gesamtpreis verrechnet
            if(rabatt > 0){
                Statement stat3 = conn.createStatement();
                sql = "SELECT wert FROM vc_rabatt WHERE ID = " + rabatt +";";
                ResultSet rs_vc_rabatt = stat3.executeQuery(sql);
                gesamtpreis *= 1 - rs_vc_rabatt.getDouble("wert");
                rs_vc_rabatt.close();
            }
            conn.close();
        }
        catch (ClassNotFoundException | SQLException e){e.printStackTrace();}
        return gesamtpreis;
    }

    public void saveStatus(String status)
    {
        try {
            Connection conn = Kinoverwaltung.setupConnection();
            Statement stat = conn.createStatement();
            String sql = "UPDATE vc_buchung SET status = '" + status + "' WHERE ID = " + this.ID +";";
            stat.executeUpdate(sql);
            conn.close();
        }
        catch (ClassNotFoundException | SQLException e){e.printStackTrace();}
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
        catch (ClassNotFoundException | SQLException e){e.printStackTrace();}
    }

    public void saveToDB()
    {
        try {
            Connection conn = Kinoverwaltung.setupConnection();
            Statement stat = conn.createStatement();
            String sql;
            if(rabatt == 0){
                sql = "INSERT INTO vc_buchung (person, vorstellung) VALUES ('"+ person + "','" + vorstellung +"');";
            }
            else{
                sql = "INSERT INTO vc_buchung (person, vorstellung, rabatt) VALUES ('"+ person + "','" + vorstellung + "','" + rabatt + "');";
            }
            stat.executeUpdate(sql);
            conn.close();
        }
        catch (ClassNotFoundException | SQLException e){e.printStackTrace();}
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
        list.setLength(list.length() - 1);
        return list.toString();
    }

    public int getID() {
        return ID;
    }

    public int getVorstellung() {
        return vorstellung;
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

    public void setRabatt(int rabatt) {this.rabatt = rabatt;
    }

    public String toText() {
        Vorstellung objVorstellung = Kinoverwaltung.getVorstellung(this.vorstellung);
        Film objFilm = Kinoverwaltung.getFilm(objVorstellung.getFilm());
        Kinosaal objKinosaal = Kinoverwaltung.getKinosaal(objVorstellung.getKinosaal());
        return "Buchungsname: " + this.person + "\n" +
                "Film: " + objFilm + "\n" +
                "Kinosaal: " + objKinosaal + "\n" +
                "Uhrzeit: " + objVorstellung + "\n" +
                "Preis: " + String.format("%.2f", this.berechneGesamtpreis()) + " €";

    }

    @Override
    public String toString() {
        Vorstellung objVorstellung = Kinoverwaltung.getVorstellung(this.vorstellung);
        Film objFilm = Kinoverwaltung.getFilm(objVorstellung.getFilm());
        Kinosaal objKinosaal = Kinoverwaltung.getKinosaal(objVorstellung.getKinosaal());
        return "<html>" +
                "Buchungsname: " + this.person + "<br>" +
                "Film: " + objFilm + "<br>" +
                "Kinosaal: " + objKinosaal + "<br>" +
                "Uhrzeit: " + objVorstellung + "<br>" +
                "Preis: " + String.format("%.2f", this.berechneGesamtpreis()) + " €" + "<br>" +
                "Status: " + status + "<br>" +
                "</html>";
    }
}
