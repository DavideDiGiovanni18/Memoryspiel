import javax.swing.*;

/**
 * Klasse f�r das erstellen der Memorykarten
 *
 * @author Davide Di Giovanni
 * @since 28.06.2021
 */

public class Karte extends JButton {

    private int punkte =0;
    private String bild ="";


    /**
     * Konstruktor f�r eine Karte
     */
    public Karte(){}

    /**
     * getter f�r die Punkte der Karten
     *
     * @return die Punkte der aufgedeckten Karte
     */
    public int getPunkte() {
        return punkte;
    }

    /**
     * setter f�r die Punkte der Karten
     *
     * @param punkte der jeweiligen Karte
     */
    public void setPunkte(int punkte) {
        this.punkte = punkte;
    }

    /**
     * setter f�r ein Bild auf den Memorykarten
     *
     * @param bild objekt(pfad) des Bildes
     */
    public void setBild(String bild) {
        this.bild = bild;
    }

    /**
     * getter f�r ein Bild auf den Memorykarten
     *
     * @return das Bild(objekt)der Memorykarte
     */
    public String getBild() {
        return bild;
    }
}
