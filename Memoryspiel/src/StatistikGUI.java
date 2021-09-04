import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Vector;
import java.io.*;

/**
 * Klasse für die Statistik des Memoryspiel
 * Funktionen: Temporär Punkte der Spieler1/2 speichern, Totalpunkte ausrechnen,
 * Gewinner ermitteln, Anzahl gespielte spiele zählen, Statistik mit allen werten in
 * "auswertung.txt" Datei speichern mit Benutzernamen.
 *
 * @author Davide Di Giovanni
 * @since 27.06.2021
 *
 * (Nach wahl Pfad nach gewünschten
 * Speicherort der Statistik ändern)
 */

public class StatistikGUI extends JDialog {
    private Vector<Integer> punktePlayer1;//Punkte pro Spiel Spieler1
    private Vector<Integer> punktePlayer2;//Punkte pro Spiel Spieler2
    private String benutzername1 = "";
    private String benutzername2 = "";
    private int anzahlSpiele = 0;//Total Spiele
    private int TotalPunkte1 = 0;//Totalpunkte Spieler1
    private int TotalPunkte2 = 0;//Totalpunkte Spieler2
    private String gewinner = "";
    private String pfad = "Memoryspiel\\src\\Auswertung\\auswertungen.txt";//Speicherort
    private GregorianCalendar datum = new GregorianCalendar();
    /**
     * Konstruktor für die Statistik
     */
    public StatistikGUI() {
        punktePlayer1 = new Vector<>();
        punktePlayer2 = new Vector<>();

    }


    /**
     * Methode um das GUI mit den Komponenten zu erstellen
     */
    public void init() {
        setTitle("Statistik");
        JLabel a = new JLabel("Statistik");
        JLabel gewinn = new JLabel("Der Gewinner ist: " + gewinner);
        JLabel pfad = new JLabel("Sie fiden ihre Daten unter: " + this.pfad);

        JPanel ganz = new JPanel(new GridLayout(3, 1, 20, 20));
        ganz.add(a);
        ganz.add(gewinn);
        ganz.add(pfad);
        add(ganz, BorderLayout.CENTER);
        JButton schliessen = new JButton("Schliessen");
        add(schliessen, BorderLayout.SOUTH);

        schliessen.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        setVisible(true);
        pack();
    }

    
    /**
     * setter für die Punkte pro Spiel des Spieler1 im Vector
     *
     * @param punkteplayer1 geholte Punkte
     */
    public void setPunkteplayer1(int punkteplayer1) {
        this.punktePlayer1.add(punkteplayer1);
    }


    /**
     * setter für die Punkte pro Spiel des Spieler1 im Vector
     *
     * @param punkteplayer2 geholte Punkte
     */
    public void setPunkteplayer2(int punkteplayer2) {
        this.punktePlayer2.add(punkteplayer2);
    }

    /**
     * Rechnet Totalpunkte ders Spieler1 aus
     */
    public void totalPunkte1() {
        for (int i = 0; i < punktePlayer1.size(); i++) {
            TotalPunkte1 += punktePlayer1.get(i);
        }
    }
    
    /**
     * getter für die Totalpunkte des Spieler1
     *
     * @return die Totalpunkte
     */
    public int getTotalPunkte1() {
        return TotalPunkte1;
    }

    
    /**
     * Rechnet Totalpunkte ders Spieler2 aus
     */

    public void totalPunkte2() {
        for (int i = 0; i < punktePlayer2.size(); i++) {
            TotalPunkte2 += punktePlayer2.get(i);
        }
    }


    /**
     * getter für die Totalpunkte des Spieler2
     *
     * @return die Totalpunkte
     */
    public int getTotalPunkte2() {
        return TotalPunkte2;
    }
    
    
    /**
     * setter für Anzahl Spiele
     *
     * @param anzahlspiele anzahl Spiele
     */
    public void setAnzahlspiele(int anzahlspiele) {
        this.anzahlSpiele += anzahlspiele;
    }

    /**
     * getter für die Gesamtanzahl gespielte Spiele
     * @return anzahl gespielte Spielte
     */
    public int getAnzahlspiele() {
        return anzahlSpiele;
    }


    /**
     * Methode um den Gewinner des Memoryspiel zu ermitteln
     * @return der Benutzername des Memoryspieler der gewonnen hat
     */
    public String gewinner() {
        if (TotalPunkte1 > TotalPunkte2) {
            gewinner = benutzername1;
        } else if (TotalPunkte1 < TotalPunkte2) {
            gewinner = benutzername2;
        } else {
            gewinner = "Unentschieden";
        }
        return gewinner;
    }


    /**
     * setter für den benutzernamen des 1. Spielers
     * @param benutzername1 benutzernamen des 1. Spielers
     */
    public void setBenutzername1(String benutzername1) {
        this.benutzername1 = benutzername1;
    }

    /**
     * * setter für den benutzernamen des 2. Spielers
     * @param benutzername2 benutzernamen des 2. Spielers
     */
    public void setBenutzername2(String benutzername2) {
        this.benutzername2 = benutzername2;
    }

    /**
     * bereitet die Daten für das GUi vor. Erstellt eine Datei um die Auswertung darin zu speicern.
     * Rechnet die Daten der spieler aus(Totalpunkte) uns schreibt diese in die neu erstellte Datei. Danch
     * wird das GUI sichtbar.
     */
    public void showDatenGUI() {
        createfile(pfad);
        totalPunkte1();
        totalPunkte2();
        writeinFile(pfad);
        init();
    }

    /**
     * erstellt ein File, indem die Memorydaten gepeichert werden
     * quelle:https://java-tutorial.org/file.html
     *
     * @param pfad des gewünschten Speicherort der Datei
     */
    private void createfile(String pfad) {
            File f = new File(pfad);
            try {
                f.createNewFile();

            } catch (IOException e) {
                e.printStackTrace();
            }
    }

    /**
     * Schreibt die Statistikdaten in das erstellte File rein
     * quelle:https://wiki.byte-welt.net/wiki/Text_in_eine_Datei_schreiben_(Java)
     *
     * @param pfad für das Speichern der Daten in der richtgen Datei
     */
    private void writeinFile(String pfad) {
        File file = new File(pfad); //Datei, in die geschrieben werden soll
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(file)); //Erzeugen eines effizienten Writers für Textdateien
            writer.write("Die Memoryspielauswertung vom " + datum.get(Calendar.DATE) +"."+ (datum.get(Calendar.MONTH)+1) +"."+
                    datum.get(Calendar.YEAR));
            writer.newLine();
            writer.newLine();
            writer.write("Der Gewinner ist: " + gewinner());
            writer.newLine();
            writer.newLine();
            writer.write("Es wurde/n " + getAnzahlspiele() + " Spiel/e gespielt");
            writer.newLine();
            writer.newLine();
            writer.write(benutzername1+" erzielte insgesamt: " + getTotalPunkte1() + " Punkte. \n"+ benutzername2+ " " +
                    "erzielte insgesamt: " + getTotalPunkte2()+" Punkte.");
            writer.newLine();
            writer.newLine();
            writer.write("Punkte pro Spiel Spieler1");
            writer.newLine();
            for (int i = 0; i < punktePlayer1.size(); i++) {
                writer.write((i + 1) + ". " + punktePlayer1.get(i));
                writer.newLine();
            }

            writer.write("Punkte pro Spiel Spieler2");
            writer.newLine();
            for (int i = 0; i < punktePlayer2.size(); i++) {
                writer.write((i + 1) + ". " + punktePlayer2.get(i));
                writer.newLine();
            }
            writer.close();
        } catch (IOException ioe) {
            System.err.println(ioe);
        }
    }

    

}



