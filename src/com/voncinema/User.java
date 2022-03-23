package com.voncinema;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;

public class User {
    private int ID;
    private String email;
    private String password;

    User(int ID, String email, String password){
        this.ID = ID;
        this.email = email;
        this.password = password;
    }

    User(String email, String password){
        this.email = email;
        this.password = password;
    }

    public void saveToDB()
    {
        try {
            Connection conn = Kinoverwaltung.setupConnection();
            Statement stat = conn.createStatement();
            String sql = "INSERT INTO vc_user (email, password) VALUES ('" + this.email + "','" + this.password +"');";
            stat.executeUpdate(sql);
            conn.close();
        }
        catch (ClassNotFoundException | SQLException e){e.printStackTrace();}
    }

    public int getID() {
        return ID;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public boolean hasID(int ID) {
        return this.ID == ID;
    }

    public boolean hasEmail(String email) {
        return Objects.equals(this.email, email);
    }

    public boolean hasBookings() {
        return !Kinoverwaltung.getBuchungenByName(this.email).isEmpty();
    }
}
