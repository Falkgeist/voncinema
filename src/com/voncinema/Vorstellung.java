package com.voncinema;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalTime;

public class Vorstellung {
    private int ID, film, kinosaal;
    private String uhrzeit;

    Vorstellung(int ID, int film, int kinosaal, String uhrzeit){
        this.ID = ID;
        this.film = film;
        this.kinosaal = kinosaal;
        // TODO: change from time to datetime
        this.uhrzeit = uhrzeit;
    }

    public void saveToDB()
    {
        try {
            Connection conn = Kinoverwaltung.setupConnection();
            Statement stat = conn.createStatement();
            String sql = "INSERT INTO vc_vorstellung (film, kinosaal, uhrzeit) VALUES(" + film + "," + kinosaal + "," + uhrzeit +");";
            stat.executeUpdate(sql);
            conn.close();
        }
        catch (ClassNotFoundException | SQLException e){e.printStackTrace();}
    }

    public int getID() {
        return ID;
    }

    public int getFilm() {
        return film;
    }

    public int getKinosaal() {
        return kinosaal;
    }

    public boolean hasID(int ID) {
        return this.ID == ID;
    }

    @Override
    public String toString() {
        Film film = Kinoverwaltung.getFilm(this.film);
        LocalTime time = LocalTime.parse(uhrzeit);
        time = time.plusMinutes(film.getLaenge());
        return uhrzeit + "-" + String.format("%02d", time.getHour()) + ":" + String.format("%02d", time.getMinute()) + " – Saal " + this.kinosaal;
    }
}
