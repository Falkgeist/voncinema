package com.voncinema;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class Buchungsformular {
    private JPanel start;
    private JComboBox selectFilm;
    private JButton buttonBuchen;
    private JComboBox selectVorstellung;
    private JSpinner spinnerAnzahl;
    private JTextField inputRabattcode;
    private JLabel labelRabattcode;
    private JPanel labelKarten;
    private JLabel labelFilm;
    private JLabel labelVorstellung;
    private JTextField inputName;
    private JLabel labelName;
    private JComboBox selectKartentyp;
    private JComboBox selectPlatzkategorie;
    private JLabel labelPlatzkategorie;
    private JLabel labelKartentyp;
    private JLabel labelAnzahl;
    private JButton buttonHinzufuegenKarte;
    private JTextField inputPerson;
    private JLabel labelPerson;
    private Buchung buchung = new Buchung();

    public Buchungsformular() {
        selectFilm.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JComboBox cb = (JComboBox)e.getSource();
                Film film = (Film)cb.getSelectedItem();
                Integer filmID = film.getID();
                ArrayList<Object> vorstellungen = Kinoverwaltung.getFromDB("Vorstellung", "vc_vorstellung", "WHERE id="+filmID);
                selectVorstellung.setModel(new DefaultComboBoxModel(vorstellungen.toArray()));
            }
        });
        buttonBuchen.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Vorstellung vorstellung = (Vorstellung)selectVorstellung.getSelectedItem();
                Kinoverwaltung.bucheBuchung(vorstellung, buchung);
            }
        });
        buttonHinzufuegenKarte.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                Kartentyp kartentyp = (Kartentyp)selectKartentyp.getSelectedItem();
                Platzkategorie platzkategorie = (Platzkategorie)selectPlatzkategorie.getSelectedItem();

                for (int i = 1; i <= (int)spinnerAnzahl.getValue(); i++){
                    buchung.hinzufuegenKarte(inputRabattcode.getText(), kartentyp.getID(), platzkategorie.getID());
                }
            }
        });
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("VonCinema");
        Buchungsformular form = new Buchungsformular();
        form.getOptionsFromDB("Film", "vc_film");
        Kinoverwaltung.getFromDB("FilmKategorie", "vc_film_kategorie");
        form.getOptionsFromDB("Vorstellung", "vc_vorstellung");
        form.getOptionsFromDB("Platzkategorie", "vc_platzkategorie");
        form.getOptionsFromDB("Kartentyp", "vc_kartentyp");
        Kinoverwaltung.getFromDB("Rabatt", "vc_rabatt");
        JPanel contentPane = form.start;
        frame.setContentPane(contentPane);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

        // DEBUGGING FOR CALCULATION
        /*Karte karte = new Karte(1, 1, 2, 1);
        Film film = Kinoverwaltung.getFilm(1);
        FilmKategorie filmKategorie = Kinoverwaltung.getFilmKategorie(film.getKategorie());
        double zuschlagFilm = filmKategorie.getZuschlagProzent();
        System.out.println(karte.berechnePreis(zuschlagFilm));*/
    }

    public void getOptionsFromDB(String strClass, String table) {
        ArrayList<Object> objArray = Kinoverwaltung.getFromDB(strClass, table);
        switch (strClass) {
            case "Film":
                this.selectFilm.setModel(new DefaultComboBoxModel(objArray.toArray()));
                break;
            case "Vorstellung":
                this.selectVorstellung.setModel(new DefaultComboBoxModel(objArray.toArray()));
                break;
            case "Platzkategorie":
                this.selectPlatzkategorie.setModel(new DefaultComboBoxModel(objArray.toArray()));
                break;
            case "Kartentyp":
                this.selectKartentyp.setModel(new DefaultComboBoxModel(objArray.toArray()));
                break;
        }
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
}
