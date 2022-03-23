package com.voncinema;

import javax.swing.*;
import javax.swing.event.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class Buchungsformular {
    private static JFrame frame = new JFrame("VonCinema - Kinokartenbuchungssystem");
    private static Buchungsformular form = new Buchungsformular();

    private static String titleTabLogin = "Login";
    private static String titleTabRegister = "Registrieren";
    private static String titleTabNewBooking = "Neue Buchung";
    private static String titleTabMyBookings = "Meine Buchungen";

    private User currentUser;

    private JPanel start;
    private JTabbedPane tabbedPane;
    private JPanel tabLogin;
    private JPanel tabRegister;
    private JPanel tabNewBooking;
    private JPanel tabMyBookings;
    // Login fields
    private JLabel labelEmail;
    private JTextField inputEmail;
    private JLabel labelPassword;
    private JPasswordField inputPassword;
    private JButton buttonLogin;
    private JTextArea textFeedbackLogin;
    // Register fields
    private JLabel labelRegisterEmail;
    private JTextField inputRegisterEmail;
    private JButton buttonRegister;
    private JLabel labelRegisterPassword;
    private JPasswordField inputRegisterPassword;
    private JLabel labelPasswordBestaetigen;
    private JPasswordField inputPasswordBestaetigen;
    private JTextArea textFeedbackRegister;
    // New booking fields
    private JLabel labelFilm;
    private JComboBox selectFilm;
    private JLabel labelVorstellung;
    private JComboBox selectVorstellung;
    private JLabel labelAnzahl;
    private JSpinner spinnerAnzahl;
    private JLabel labelKartentyp;
    private JComboBox selectKartentyp;
    private JLabel labelPlatzkategorie;
    private JComboBox selectPlatzkategorie;
    private JButton buttonHinzufuegenKarte;
    private JButton buttonEntfernenKarte;
    private JLabel labelRabattcode;
    private JTextField inputRabattcode;
    private JTextArea textAreaRabattcodeFeedback;
    private JButton buttonBuchen;
    private JTextArea textAusgabe;
    // My bookings fields
    private JList listMeineBuchungen;
    private JButton buttonBuchungStornieren;
    private JButton buttonBuchungBezahlen;
    private JTextArea textAreaFeedback;
    private JButton buttonLogout;

    private ArrayList<Karte> karten = new ArrayList<>();


    public Buchungsformular() {
        selectFilm.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JComboBox cb = (JComboBox) e.getSource();
                Film film = (Film) cb.getSelectedItem();
                int filmID = film.getID();
                ArrayList<Object> vorstellungen = Kinoverwaltung.getFromDB("vc_vorstellung", "WHERE film=" + filmID + " ORDER BY uhrzeit");
                selectVorstellung.setModel(new DefaultComboBoxModel(vorstellungen.toArray()));
                Vorstellung vorstellung = (Vorstellung) selectVorstellung.getSelectedItem();
                compileSelectPlatzkategorie(vorstellung, 0);
            }
        });
        selectVorstellung.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JComboBox cb = (JComboBox) e.getSource();
                Vorstellung vorstellung = (Vorstellung) cb.getSelectedItem();
                compileSelectPlatzkategorie(vorstellung, 0);
            }
        });
        buttonBuchen.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                confirmBooking();
            }
        });
        buttonHinzufuegenKarte.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                addTicket();
            }
        });
        buttonEntfernenKarte.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                removeTicket();
            }
        });

        buttonLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                login();
            }
        });
        buttonRegister.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                register();
            }
        });
        buttonLogout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                logout();
            }
        });

        buttonBuchungBezahlen.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                payBuchung();
                showBuchungen();
            }
        });
        buttonBuchungStornieren.addMouseListener(new MouseAdapter() {
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
        listMeineBuchungen.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                Buchung buchung = (Buchung) listMeineBuchungen.getSelectedValue();
                if (!listMeineBuchungen.isSelectionEmpty() && !Objects.equals(buchung.getStatus(), "gebucht")) {
                    buttonBuchungBezahlen.setEnabled(false);
                    buttonBuchungStornieren.setEnabled(false);
                } else {
                    buttonBuchungBezahlen.setEnabled(true);
                    buttonBuchungStornieren.setEnabled(true);
                }
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
        Kinoverwaltung.getFromDB("vc_user");
        Kinoverwaltung.getFromDB("vc_vorstellung");
        Film film = (Film) form.selectFilm.getSelectedItem();
        ArrayList<Object> vorstellungen = Kinoverwaltung.getFromDB("vc_vorstellung", "WHERE film=" + film.getID() + " ORDER BY uhrzeit");
        form.selectVorstellung.setModel(new DefaultComboBoxModel(vorstellungen.toArray()));
        Vorstellung vorstellung = (Vorstellung) form.selectVorstellung.getSelectedItem();
        compileSelectPlatzkategorie(vorstellung, 0);
        form.tabbedPane.remove(form.tabNewBooking);
        form.tabbedPane.remove(form.tabMyBookings);
        // TODO: Hier muss noch ein scrollPane drumherum, sonst verschwindet irgendwann der buttonBuchen
        form.textAusgabe.setLineWrap(true);
        form.buttonBuchungBezahlen.setEnabled(false);
        form.buttonBuchungStornieren.setEnabled(false);
        form.spinnerAnzahl.getModel().setValue(1);
        ((SpinnerNumberModel) form.spinnerAnzahl.getModel()).setMinimum(1);
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
        ArrayList<Object> konfigurationPlatzkategorien = Kinoverwaltung.getFromDB("vc_kinosaalkonfiguration_platzkategorie", "WHERE konfiguration=" + konfiguration.getID());
        ArrayList<Platzkategorie> platzkategorien = new ArrayList<>();
        for (Object objKonfigurationPlatzkategorie : konfigurationPlatzkategorien) {
            KinosaalKonfigurationPlatzkategorie konfigurationPlatzkategorie = (KinosaalKonfigurationPlatzkategorie) objKonfigurationPlatzkategorie;
            Platzkategorie platzkategorie = Kinoverwaltung.getPlatzkategorie(konfigurationPlatzkategorie.getPlatzkategorie());
            platzkategorie.setTempFreiePlaetze(vorstellung, fromCurrentBooking);
            platzkategorien.add(platzkategorie);
        }
        form.selectPlatzkategorie.setModel(new DefaultComboBoxModel(platzkategorien.toArray()));
    }

    private void removeTicket() {
        Kartentyp kartentyp = (Kartentyp) selectKartentyp.getSelectedItem();
        Platzkategorie platzkategorie = (Platzkategorie) selectPlatzkategorie.getSelectedItem();
        int removeCount = (int) spinnerAnzahl.getValue();

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
            compileSelectPlatzkategorie((Vorstellung) selectVorstellung.getSelectedItem(), -(int) spinnerAnzahl.getValue());
            textAusgabe.append(spinnerAnzahl.getValue() + " Karten (" + kartentyp.getName() + ", " + platzkategorie.getName() + ") entfernt.\n");
        }
    }

    private void addTicket() {
        Kartentyp kartentyp = (Kartentyp) selectKartentyp.getSelectedItem();
        Platzkategorie platzkategorie = (Platzkategorie) selectPlatzkategorie.getSelectedItem();

        // check for available seats
        Vorstellung vorstellung = (Vorstellung) selectVorstellung.getSelectedItem();
        if (!platzkategorie.hasTempFreiePlaetze()) {
            platzkategorie.setTempFreiePlaetze(vorstellung, 0);
        }
        int freiePlaetze = platzkategorie.getTempFreiePlaetze();
        if (!karten.isEmpty()) {
            for (Karte karte : karten) {
                if (karte.getPlatzkategorie() == platzkategorie.getID()) {
                    freiePlaetze--;
                }
            }
        }
        if (freiePlaetze >= (int) spinnerAnzahl.getValue()) {

            for (int i = 1; i <= (int) spinnerAnzahl.getValue(); i++) {

                Karte karte = new Karte(platzkategorie.getID(), kartentyp.getID());
                karten.add(karte);
            }
            if (textAusgabe.getText().startsWith("Die Buchung")) {
                textAusgabe.setText("");
            }
            textAusgabe.append(spinnerAnzahl.getValue() + " Karten (" + kartentyp.getName() + ", " + platzkategorie.getName() + ") hinzugefügt.\n");
            compileSelectPlatzkategorie(vorstellung, (int) spinnerAnzahl.getValue());
        } else {
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

    private void confirmBooking() {
        if (karten.isEmpty()) {
            textAusgabe.append("Bitte Karten hinzufügen!\n----------\n");
            return;
        }
        Buchung buchung = new Buchung();
        Vorstellung vorstellung = (Vorstellung) selectVorstellung.getSelectedItem();
        buchung.setPerson(currentUser.getEmail());
        buchung.setVorstellung(vorstellung.getID());
        buchung.setRabatt(Rabatt.findIDByString(inputRabattcode.getText()));
        // TODO: Kann das Speichern der Karten einfach direkt hier passieren?
        //  Haben wir hier die ID der Buchung oder können sie über buchung.getLastIdFromDb() bekommen (nachdem die Buchung gespeichert wurde)?
        //  Dann könnte man sich die Instanzvariable karten in Buchung sparen...
        for (Karte karte : karten) {
            buchung.hinzufuegenKarte(karte);
        }
        karten.clear();
        Kinoverwaltung.resetFreiePlaetze();
        Kinoverwaltung.bucheBuchung(vorstellung, buchung);
        textAusgabe.setText("Die Buchung wurde gespeichert.\n" +
                "----------\n" +
                "Details:\n" +
                buchung.toText() + "\n" +
                "Karten:\n" +
                buchung.getKartenAsList());
        showBuchungen();
    }

    private void showBuchungen() {
        String username = currentUser.getEmail();
        // TODO: Evtl. direkt nur die gewünschten Buchungen aus der DB holen (Performance und Sicherheit)
        Kinoverwaltung.getFromDB("vc_buchung");
        ArrayList<Buchung> buchungenForUser = Kinoverwaltung.getBuchungenByName(username);
        if (!buchungenForUser.isEmpty()) {
            DefaultListModel model = new DefaultListModel();
            for (Buchung buchung : buchungenForUser) {
                model.addElement(buchung);
            }
            listMeineBuchungen.setModel(model);
            listMeineBuchungen.revalidate();
            if (buchungenForUser.size() > 1) {
                textAreaFeedback.setText(buchungenForUser.size() + " Buchungen gefunden.");
            } else {
                textAreaFeedback.setText(buchungenForUser.size() + " Buchung gefunden.");
            }
        } else {
            textAreaFeedback.setText("Keine Buchungen gefunden.");
        }
    }

    public void payBuchung() {
        Buchung buchungAusListe = (Buchung) listMeineBuchungen.getSelectedValue();
        int buchungsID = buchungAusListe.getID();
        Buchung buchung = (Buchung) Kinoverwaltung.getFromDB("vc_buchung", "WHERE id=" + buchungsID).get(0);
        buchung.saveStatus("bezahlt");
        textAreaFeedback.setText("Die ausgewählte Buchung wurde bezahlt.");
    }

    public void cancelBuchung() {
        Buchung buchungAusListe = (Buchung) listMeineBuchungen.getSelectedValue();
        int buchungsID = buchungAusListe.getID();
        Buchung buchung = (Buchung) Kinoverwaltung.getFromDB("vc_buchung", "WHERE id=" + buchungsID).get(0);
        buchung.saveStatus("storniert");
        Vorstellung vorstellung = (Vorstellung)selectVorstellung.getSelectedItem();
        compileSelectPlatzkategorie(vorstellung, 0);
        textAreaFeedback.setText("Die ausgewählte Buchung wurde storniert.");
    }

    public void checkRabattcode() {
        if (inputRabattcode.getText().length() > 0) {
            if (Rabatt.findIDByString(inputRabattcode.getText()) == 0) {
                textAreaRabattcodeFeedback.setText("Der Rabattcode ist ungültig.");
            } else {
                textAreaRabattcodeFeedback.setText("Der Rabattcode ist gültig.");
            }
        } else {
            textAreaRabattcodeFeedback.setText("");
        }
    }

    private void login() {
        User checkUser = Kinoverwaltung.getUserByEmail(inputEmail.getText());
        if (checkUser == null) {
            textFeedbackLogin.setText("Für diese Email wurde kein Konto gefunden.");
        } else if (!Objects.equals(checkUser.getPassword(), Arrays.toString(inputPassword.getPassword()))) {
            textFeedbackLogin.setText("Das Passwort ist nicht korrekt.");
        }
        else {
            currentUser = checkUser;
            form.tabbedPane.add(form.tabNewBooking);
            int indexNewBooking = form.tabbedPane.indexOfComponent(form.tabNewBooking);
            form.tabbedPane.setTitleAt(indexNewBooking, titleTabNewBooking);
            form.tabbedPane.add(form.tabMyBookings);
            int indexMyBookings = form.tabbedPane.indexOfComponent(form.tabMyBookings);
            form.tabbedPane.setTitleAt(indexMyBookings, titleTabMyBookings);
            if (currentUser.hasBookings()) {
                form.tabbedPane.setSelectedComponent(tabMyBookings);
            } else {
                form.tabbedPane.setSelectedComponent(tabNewBooking);
            }
            form.tabbedPane.remove(form.tabLogin);
            form.tabbedPane.remove(form.tabRegister);
            form.buttonLogout.setEnabled(true);
            textFeedbackLogin.setText("Sie wurden eingeloggt.");
            showBuchungen();
            frame.pack();
        }
    }

    private void register() {
        if (Kinoverwaltung.getUserByEmail(inputRegisterEmail.getText()) != null) {
            textFeedbackRegister.setText("Für diese Email gibt es bereits ein Konto.");
        } else if (!Objects.equals(Arrays.toString(inputRegisterPassword.getPassword()), Arrays.toString(inputPasswordBestaetigen.getPassword()))) {
            textFeedbackRegister.setText("Die Passwörter stimmen nicht überein.");
        } else {
            User user = new User(inputRegisterEmail.getText(), Arrays.toString(inputRegisterPassword.getPassword()));
            Kinoverwaltung.addUser(user);
            user.saveToDB();
            textFeedbackRegister.setText("Konto wurde erstellt.");
        }
    }

    private void logout() {
        currentUser = null;
        form.tabbedPane.add(form.tabLogin);
        int indexLogin = form.tabbedPane.indexOfComponent(form.tabLogin);
        form.tabbedPane.setTitleAt(indexLogin, titleTabLogin);
        form.tabbedPane.add(form.tabRegister);
        int indexRegister = form.tabbedPane.indexOfComponent(form.tabRegister);
        form.tabbedPane.setTitleAt(indexRegister, titleTabRegister);
        form.tabbedPane.setSelectedComponent(tabLogin);
        form.tabbedPane.remove(form.tabNewBooking);
        form.tabbedPane.remove(form.tabMyBookings);
        form.buttonLogout.setEnabled(false);
        textFeedbackLogin.setText("Sie wurden ausgeloggt.");
    }
}
