package com.voncinema;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Objects;

public class Buchungsformular {
    private static JFrame frame = new JFrame("VonCinema");
    private static Buchungsformular form = new Buchungsformular();

    private JPanel start, labelKarten;
    private JComboBox selectFilm, selectVorstellung, selectKartentyp, selectPlatzkategorie;
    private JList listMeineBuchungen;
    private JButton buttonBuchen, buttonHinzufuegenKarte, buttonBuchungenAnzeigen;
    private JSpinner spinnerAnzahl;
    private JTextField inputRabattcode, inputName, textFieldNameLogin;
    private JLabel labelRabattcode, labelPerson, labelFilm, labelVorstellung, labelName, labelPlatzkategorie, labelKartentyp, labelAnzahl;
    private JTabbedPane tabbedPane1;
    private JTextArea textAusgabe, textAreaFeedback;
    private JScrollPane scrollPaneMeineBuchungen;
    private JPanel paneBuchungButtons;
    private JButton buttonStornieren;
    private JButton buttonBezahlen;
    private JTextArea textAreaRabattcodeFeedback;
    private ArrayList<Karte> karten = new ArrayList<>();


    public Buchungsformular() {
        selectFilm.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JComboBox cb = (JComboBox)e.getSource();
                Film film = (Film)cb.getSelectedItem();
                int filmID = film.getID();
                ArrayList<Object> vorstellungen = Kinoverwaltung.getFromDB("vc_vorstellung", "WHERE id="+filmID);
                selectVorstellung.setModel(new DefaultComboBoxModel(vorstellungen.toArray()));
            }
        });
        buttonBuchen.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (Objects.equals(inputName.getText(), "")) {
                    textAusgabe.append("Bitte einen Namen für die Buchung angeben!\n----------\n");
                    return;
                } else if (karten.isEmpty()) {
                    textAusgabe.append("Bitte Karten hinzufügen!\n----------\n");
                    return;
                }
                Buchung buchung = new Buchung();
                Vorstellung vorstellung = (Vorstellung)selectVorstellung.getSelectedItem();
                buchung.setPerson(inputName.getText());
                buchung.setVorstellung(vorstellung.getID());
                buchung.setRabatt(Rabatt.findIDByString(inputRabattcode.getText()));
                for (Karte karte : karten) {
                    buchung.hinzufuegenKarte(karte);
                }
                karten.clear();
                Kinoverwaltung.bucheBuchung(vorstellung, buchung);
                textAusgabe.setText("Die Buchung wurde gespeichert.\n" +
                        "----------\n" +
                        "Details:\n" +
                        buchung + "\n" +
                        "Karten:\n" +
                        buchung.getKartenAsList());
            }
        });
        buttonHinzufuegenKarte.addActionListener(new ActionListener() {

            //TODO: Überprüfung ob im Kinosaal genug Plätze der kategorie frei sind

            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                Kartentyp kartentyp = (Kartentyp)selectKartentyp.getSelectedItem();
                Platzkategorie platzkategorie = (Platzkategorie)selectPlatzkategorie.getSelectedItem();

                for (int i = 1; i <= (int)spinnerAnzahl.getValue(); i++){

                    Karte karte = new Karte( platzkategorie.getID(), kartentyp.getID());
                    karten.add(karte);
                }
                if (textAusgabe.getText().startsWith("Die Buchung")) {
                    textAusgabe.setText("");
                }
                textAusgabe.append(spinnerAnzahl.getValue() + " Karten ("+kartentyp.toString()+", "+platzkategorie.toString()+") hinzugefügt.\n");
            }
        });
        buttonBuchungenAnzeigen.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String username = textFieldNameLogin.getText();
                if (username.equals("")){
                    textAreaFeedback.setText("Bitte einen Namen eingeben\n");
                }
                else {
                    Kinoverwaltung.getFromDB("vc_buchung");
                    ArrayList<Buchung> buchungenForUser = Kinoverwaltung.getBuchungenByName(username);
                    DefaultListModel model = new DefaultListModel();
                    for (Buchung buchung : buchungenForUser) {
                        model.addElement(buchung.toHTML());
                    }
                    listMeineBuchungen.setModel(model);
                    listMeineBuchungen.revalidate();
                }
            }
        });
        inputRabattcode.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);
                if(inputRabattcode.getText().length() > 0){
                    if(Rabatt.findIDByString(inputRabattcode.getText()) == 0){
                        textAreaRabattcodeFeedback.setText("Der Rabattcode ist ungültig.");
                    }
                    else{
                        textAreaRabattcodeFeedback.setText("Der Rabattcode ist gültig.");
                    }
                }
                else{
                    textAreaRabattcodeFeedback.setText("");
                }

            }
        });
        buttonBezahlen.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        buttonStornieren.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
    }

    public static void main(String[] args) {
        form.getOptionsFromDB("vc_film");
        Kinoverwaltung.getFromDB("vc_film_kategorie");
        Film film = (Film)form.selectFilm.getSelectedItem();
        int filmID = film.getID();
        ArrayList<Object> vorstellungen = Kinoverwaltung.getFromDB("vc_vorstellung", "WHERE id="+filmID);
        form.selectVorstellung.setModel(new DefaultComboBoxModel(vorstellungen.toArray()));
        form.getOptionsFromDB("vc_platzkategorie");
        form.getOptionsFromDB("vc_kartentyp");
        Kinoverwaltung.getFromDB("vc_rabatt");
        form.textAusgabe.setLineWrap(true);
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

    public void getOptionsFromDB(String table) {
        ArrayList<Object> objArray = Kinoverwaltung.getFromDB(table);
        switch (table) {
            case "vc_film":
                this.selectFilm.setModel(new DefaultComboBoxModel(objArray.toArray()));
                break;
            case "vc_vorstellung":
                this.selectVorstellung.setModel(new DefaultComboBoxModel(objArray.toArray()));
                break;
            case "vc_platzkategorie":
                this.selectPlatzkategorie.setModel(new DefaultComboBoxModel(objArray.toArray()));
                break;
            case "vc_kartentyp":
                this.selectKartentyp.setModel(new DefaultComboBoxModel(objArray.toArray()));
                break;
        }
    }

    private void createUIComponents() {
    }
}
