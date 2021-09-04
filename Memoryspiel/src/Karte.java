import javax.swing.*;

/**
 * Klasse für das erstellen der Memorykarten
 *
 * @author Davide Di Giovanni
 * @since 28.06.2021
 */

public class Karte extends JButton {

    private int punkte =0;
    private String bild ="";


    /**
     * Konstruktor für eine Karte
     */
    public Karte(){}

    /**
     * getter für die Punkte der Karten
     *
     * @return die Punkte der aufgedeckten Karte
     */
    public int getPunkte() {
        return punkte;
    }

    /**
     * setter für die Punkte der Karten
     *
     * @param punkte der jeweiligen Karte
     */
    public void setPunkte(int punkte) {
        this.punkte = punkte;
    }

    /**
     * setter für ein Bild auf den Memorykarten
     *
     * @param bild objekt(pfad) des Bildes
     */
    public void setBild(String bild) {
        this.bild = bild;
    }

    /**
     * getter für ein Bild auf den Memorykarten
     *
     * @return das Bild(objekt)der Memorykarte
     */
    public String getBild() {
        return bild;
    }
}
