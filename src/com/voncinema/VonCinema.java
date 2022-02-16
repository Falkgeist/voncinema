package com.voncinema;

import javax.swing.*;

public class VonCinema {
    private JPanel start;
    private JComboBox selectFilm;
    private JButton buttonBuchen;
    private JComboBox selectVorstellung;
    private JSpinner spinnerAnzahlKinder;
    private JSpinner spinnerAnzahlErwachsene;
    private JSpinner spinnerAnzahlErmaeßigt;
    private JTextField textField1;
    private JLabel labelAnzahlKinder;
    private JLabel labelAnzahlErwachsene;
    private JLabel labelAnzahlErmaeßigt;
    private JLabel labelRabattcode;
    private JPanel tickets;
    private JLabel labelFilm;
    private JLabel labelVorstellung;

    public static void main(String[] args) {
        JFrame frame = new JFrame("VonCinema");
        frame.setContentPane(new VonCinema().start);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
}
