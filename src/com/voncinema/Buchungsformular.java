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
    private JSpinner spinnerAnzahlKinder;
    private JSpinner spinnerAnzahlErwachsene;
    private JSpinner spinnerAnzahlErmaessigt;
    private JTextField inputRabattcode;
    private JLabel labelAnzahlKinder;
    private JLabel labelAnzahlErwachsene;
    private JLabel labelAnzahlErmaessigt;
    private JLabel labelRabattcode;
    private JPanel tickets;
    private JLabel labelFilm;
    private JLabel labelVorstellung;
    private JTextField inputName;
    private JLabel lableName;
    private JTextField inputPerson;
    private JLabel labelPerson;

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
            public void actionPerformed(ActionEvent actionEvent) {

            }
        });
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("VonCinema");
        Buchungsformular form = new Buchungsformular();
        ArrayList<Object> filme = Kinoverwaltung.getFromDB("Film", "vc_film");
        ArrayList<Object> vorstellungen = Kinoverwaltung.getFromDB("Vorstellung", "vc_vorstellung");
        form.selectFilm.setModel(new DefaultComboBoxModel(filme.toArray()));
        form.selectVorstellung.setModel(new DefaultComboBoxModel(vorstellungen.toArray()));
        JPanel contentPane = form.start;
        frame.setContentPane(contentPane);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
}
