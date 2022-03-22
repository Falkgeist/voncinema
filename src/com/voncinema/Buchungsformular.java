package com.voncinema;

import javax.swing.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Objects;

public class Buchungsformular {
    private static JFrame frame = new JFrame("VonCinema - Kinokartenbuchungssystem");
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
    private JButton buttonEntfernenKarte;
    private ArrayList<Karte> karten = new ArrayList<>();


    public Buchungsformular() {
        selectFilm.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JComboBox cb = (JComboBox)e.getSource();
                Film film = (Film)cb.getSelectedItem();
                int filmID = film.getID();
                ArrayList<Object> vorstellungen = Kinoverwaltung.getFromDB("vc_vorstellung", "WHERE film="+filmID);
                selectVorstellung.setModel(new DefaultComboBoxModel(vorstellungen.toArray()));
                Vorstellung vorstellung = (Vorstellung)selectVorstellung.getSelectedItem();
                compileSelectPlatzkategorie(vorstellung, 0);
            }
        });
        selectVorstellung.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JComboBox cb = (JComboBox)e.getSource();
                Vorstellung vorstellung = (Vorstellung)cb.getSelectedItem();
                compileSelectPlatzkategorie(vorstellung, 0);
            }
        });
        buttonBuchen.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                bookBuchung();
            }
        });
        buttonHinzufuegenKarte.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                addKarte();
            }
        });
        buttonEntfernenKarte.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                removeKarte();
            }
        });
        buttonBuchungenAnzeigen.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                showBuchungen();
            }
        });
        textFieldNameLogin.getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "enter");
        textFieldNameLogin.getActionMap().put("enter", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showBuchungen();
            }
        });
        buttonBezahlen.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                payBuchung();
                showBuchungen();
            }
        });
        buttonStornieren.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                cancelBuchung();
                showBuchungen();
            }
        });
        inputRabattcode.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);
                checkRabattcode();
            }
        });
    }

    public static void main(String[] args) {
        form.getOptionsFromDB("vc_film");
        Kinoverwaltung.getFromDB("vc_film_kategorie");
        form.getOptionsFromDB("vc_kartentyp");
        Kinoverwaltung.getFromDB("vc_kinosaal");
        Kinoverwaltung.getFromDB("vc_kinosaalkonfiguration");
        Kinoverwaltung.getFromDB("vc_platzkategorie");
        Kinoverwaltung.getFromDB("vc_rabatt");
        Kinoverwaltung.getFromDB("vc_vorstellung");
        Film film = (Film)form.selectFilm.getSelectedItem();
        ArrayList<Object> vorstellungen = Kinoverwaltung.getFromDB("vc_vorstellung", "WHERE film="+film.getID());
        form.selectVorstellung.setModel(new DefaultComboBoxModel(vorstellungen.toArray()));
        Vorstellung vorstellung = (Vorstellung)form.selectVorstellung.getSelectedItem();
        compileSelectPlatzkategorie(vorstellung, 0);
        form.textAusgabe.setLineWrap(true);
        form.spinnerAnzahl.getModel().setValue(1);
        ((SpinnerNumberModel)form.spinnerAnzahl.getModel()).setMinimum(1);
        JPanel contentPane = form.start;
        frame.setContentPane(contentPane);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    public void getOptionsFromDB(String table) {
        ArrayList<Object> objArray = Kinoverwaltung.getFromDB(table);
        switch (table) {
            case "vc_film":
                this.selectFilm.setModel(new DefaultComboBoxModel(objArray.toArray()));
                break;
            case "vc_platzkategorie":
                this.selectPlatzkategorie.setModel(new DefaultComboBoxModel(objArray.toArray()));
                break;
            case "vc_kartentyp":
                this.selectKartentyp.setModel(new DefaultComboBoxModel(objArray.toArray()));
                break;
        }
    }

    private static void compileSelectPlatzkategorie(Vorstellung vorstellung, int fromCurrentBooking) {
        Kinosaal kinosaal = Kinoverwaltung.getKinosaal(vorstellung.getKinosaal());
        KinosaalKonfiguration konfiguration = Kinoverwaltung.getKinosaalKonfiguration(kinosaal.getKonfiguration());
        ArrayList<Object> konfigurationPlatzkategorien = Kinoverwaltung.getFromDB("vc_kinosaalkonfiguration_platzkategorie", "WHERE konfiguration="+konfiguration.getID());
        ArrayList<Platzkategorie> platzkategorien = new ArrayList<>();
        for (Object objKonfigurationPlatzkategorie : konfigurationPlatzkategorien) {
            KinosaalKonfigurationPlatzkategorie konfigurationPlatzkategorie = (KinosaalKonfigurationPlatzkategorie)objKonfigurationPlatzkategorie;
            Platzkategorie platzkategorie = Kinoverwaltung.getPlatzkategorie(konfigurationPlatzkategorie.getPlatzkategorie());
            platzkategorie.setTempFreiePlaetze(vorstellung, fromCurrentBooking);
            platzkategorien.add(platzkategorie);
        }
        form.selectPlatzkategorie.setModel(new DefaultComboBoxModel(platzkategorien.toArray()));
    }

    private void removeKarte() {
        Kartentyp kartentyp = (Kartentyp)selectKartentyp.getSelectedItem();
        Platzkategorie platzkategorie = (Platzkategorie)selectPlatzkategorie.getSelectedItem();
        int removeCount = (int)spinnerAnzahl.getValue();

        int i = 0;
        boolean notFound = true;
        while (karten.size() > i && removeCount > 0) {
            Karte karte = karten.get(i);
            if (karte.getPlatzkategorie() == platzkategorie.getID() && karte.getKartentyp() == kartentyp.getID()) {
                karten.remove(i);
                notFound = false;
                removeCount--;
            } else {
                i++;
            }
        }
        if (notFound) {
            textAusgabe.append("Es wurden keine Karten zum Entfernen gefunden.\n");
        } else {
            compileSelectPlatzkategorie((Vorstellung)selectVorstellung.getSelectedItem(), -(int)spinnerAnzahl.getValue());
            textAusgabe.append(spinnerAnzahl.getValue() + " Karten (" + kartentyp.getName() + ", " + platzkategorie.getName() + ") entfernt.\n");
        }
    }

    private void addKarte() {
        Kartentyp kartentyp = (Kartentyp)selectKartentyp.getSelectedItem();
        Platzkategorie platzkategorie = (Platzkategorie)selectPlatzkategorie.getSelectedItem();

        // check for available seats
        Vorstellung vorstellung = (Vorstellung)selectVorstellung.getSelectedItem();
        platzkategorie.setTempFreiePlaetze(vorstellung, 0);
        int freiePlaetze = platzkategorie.getTempFreiePlaetze();
        if (!karten.isEmpty()) {
            for (Karte karte : karten) {
                if (karte.getPlatzkategorie() == platzkategorie.getID()) {
                    freiePlaetze--;
                }
            }
        }
        if (freiePlaetze >= (int)spinnerAnzahl.getValue()) {

            for (int i = 1; i <= (int) spinnerAnzahl.getValue(); i++) {

                Karte karte = new Karte(platzkategorie.getID(), kartentyp.getID());
                karten.add(karte);
            }
            if (textAusgabe.getText().startsWith("Die Buchung")) {
                textAusgabe.setText("");
            }
            textAusgabe.append(spinnerAnzahl.getValue() + " Karten (" + kartentyp.getName() + ", " + platzkategorie.getName() + ") hinzugefügt.\n");
            compileSelectPlatzkategorie(vorstellung, (int)spinnerAnzahl.getValue());
        }
        else {
            if (textAusgabe.getText().startsWith("Die Buchung")) {
                textAusgabe.setText("");
            }
            if (freiePlaetze == 1) {
                textAusgabe.append("Für diese Platzkategorie ist nur noch " + freiePlaetze + " weiterer Platz frei. Versuchen Sie es mit weniger Plätzen oder wählen Sie eine andere Platzkategorie.\n");
            } else if (freiePlaetze > 1) {
                textAusgabe.append("Für diese Platzkategorie sind nur noch " + freiePlaetze + " weitere Plätze frei. Versuchen Sie es mit weniger Plätzen oder wählen Sie eine andere Platzkategorie.\n");
            } else {
                textAusgabe.append("Für diese Platzkategorie sind keine Plätze mehr frei. Bitte wählen Sie eine andere Platzkategorie.\n");
            }
        }
    }

    private void bookBuchung() {
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
        // TODO: Kann das Speichern der Karten einfach direkt hier passieren?
        //  Haben wir hier die ID der Buchung oder können sie über buchung.getLastIdFromDb() bekommen (nachdem die Buchung gespeichert wurde)?
        //  Dann könnte man sich die Instanzvariable karten in Buchung sparen...
        for (Karte karte : karten) {
            buchung.hinzufuegenKarte(karte);
        }
        karten.clear();
        Kinoverwaltung.bucheBuchung(vorstellung, buchung);
        textAusgabe.setText("Die Buchung wurde gespeichert.\n" +
                "----------\n" +
                "Details:\n" +
                buchung.toText() + "\n" +
                "Karten:\n" +
                buchung.getKartenAsList());
    }

    private void showBuchungen(){
        String username = textFieldNameLogin.getText();
        if (username.equals("")){
            textAreaFeedback.setText("Bitte einen Namen eingeben\n");
        }
        else {
            // TODO: Evtl. direkt nur die gewünschten Buchungen aus der DB holen (Performance und Sicherheit)
            Kinoverwaltung.getFromDB("vc_buchung");
            ArrayList<Buchung> buchungenForUser = Kinoverwaltung.getBuchungenByName(username);
            DefaultListModel model = new DefaultListModel();
            for (Buchung buchung : buchungenForUser) {
                model.addElement(buchung);
            }
            listMeineBuchungen.setModel(model);
            listMeineBuchungen.revalidate();
        }
    }

    public void payBuchung() {
        Buchung buchungAusListe = (Buchung)listMeineBuchungen.getSelectedValue();
        int buchungsID = buchungAusListe.getID();
        Buchung buchung = (Buchung)Kinoverwaltung.getFromDB("vc_buchung", "WHERE id="+buchungsID).get(0);
        buchung.saveStatus("bezahlt");
        textAreaFeedback.setText("Die ausgewählte Buchung wurde bezahlt.");
    }

    public void cancelBuchung() {
        Buchung buchungAusListe = (Buchung)listMeineBuchungen.getSelectedValue();
        int buchungsID = buchungAusListe.getID();
        Buchung buchung = (Buchung)Kinoverwaltung.getFromDB("vc_buchung", "WHERE id="+buchungsID).get(0);
        buchung.saveStatus("storniert");
        textAreaFeedback.setText("Die ausgewählte Buchung wurde storniert.");
    }

    public void checkRabattcode() {
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
}
