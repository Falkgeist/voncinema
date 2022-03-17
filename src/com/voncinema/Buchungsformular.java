package com.voncinema;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
    private JTextArea textKarten, textBuchung, textAreaFeedback;
    private JScrollPane scrollPaneMeineBuchungen;
    private JPanel paneBuchungButtons;
    private JButton buttonStornieren;
    private JButton buttonBezahlen;
    private ArrayList<Karte> karten = new ArrayList<>();

    /**TODO: im Tab Meine Buchungen:
     * eine Funktion zum hinzufügen eines Textfeldes und einen button bezahlen und einen button stornieren
     * beim klick auf BuchungAnzeigen jede Buchung mit Funktion zum Tab hinzufügen
     */



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
                    textBuchung.append("Bitte einen Namen für die Buchung angeben!");
                    return;
                }
                Buchung buchung = new Buchung();
                Vorstellung vorstellung = (Vorstellung)selectVorstellung.getSelectedItem();
                buchung.setPerson(inputName.getText());
                buchung.setVorstellung(vorstellung.getID());
                for (Karte karte : karten) {
                    buchung.hinzufuegenKarte(karte);
                }
                karten.clear();
                Kinoverwaltung.bucheBuchung(vorstellung, buchung);
                textBuchung.append("Die Buchung wurde gespeichert.\n" +
                        "Details:\n" +
                        "Name des Buchenden: " + inputName.getText() + "\n" +
                        buchung + "\n" +
                        "Karten:\n" +
                        buchung.getKartenAsList());
            }
        });
        buttonHinzufuegenKarte.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                Kartentyp kartentyp = (Kartentyp)selectKartentyp.getSelectedItem();
                Platzkategorie platzkategorie = (Platzkategorie)selectPlatzkategorie.getSelectedItem();

                for (int i = 1; i <= (int)spinnerAnzahl.getValue(); i++){
                    int rabattID = Rabatt.findIDByString(inputRabattcode.getText());
                    Karte karte = new Karte(rabattID, platzkategorie.getID(), kartentyp.getID());
                    karten.add(karte);
                }
                textKarten.append(spinnerAnzahl.getValue() + " Karten ("+kartentyp.toString()+", "+platzkategorie.toString()+") hinzugefügt.\n----------\n");
            }
        });
        buttonBuchungenAnzeigen.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String username = textFieldNameLogin.getText();
                Kinoverwaltung.getFromDB("vc_buchung");
                ArrayList<Buchung> buchungenForUser = Kinoverwaltung.getBuchungenByName(username);
                DefaultListModel model = new DefaultListModel();
                for (Buchung buchung : buchungenForUser) {
                    model.addElement(buchung.toHTML());
                }
                listMeineBuchungen.setModel(model);
                listMeineBuchungen.revalidate();
            }
        });
    }

    public static void main(String[] args) {
        form.getOptionsFromDB("vc_film");
        Kinoverwaltung.getFromDB("vc_film_kategorie");
        form.getOptionsFromDB("vc_vorstellung");
        form.getOptionsFromDB("vc_platzkategorie");
        form.getOptionsFromDB("vc_kartentyp");
        Kinoverwaltung.getFromDB("vc_rabatt");
        form.textKarten.setLineWrap(true);
        form.textBuchung.setLineWrap(true);
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
        // TODO: place custom component creation code here
    }
}
