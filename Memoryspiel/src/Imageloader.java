import java.util.Vector;

/**
 * Klasse f�r das Laden der Bilder(pfade) in einem Vector.
 *
 * @author Davide Di Giovanni
 * @since 28.06.2021
 */

public class Imageloader {
    private String pfad = "/Bilder";
    private Vector<String>bilder;
    private String hintergrundbild;//standart Hintergrund Bild der Karten wenn sie umgedereht sind

    /**
     * Konstruktor f�r den Imageloader
     */
    public Imageloader(){
        bilder = new Vector<>();
        hintergrundbild= "Hintergrundkarte.jpg";
    }

    /**
     * Getter f�r den Grundpfad der zu den Bilder f�hrt
     *
     * @return pfad der Bilder f�r daa Memomryspiel
     */
    public String getPfad() {
        return pfad;
    }

    /**
     * getter f�r den Pfad eines bestimmten Bildes
     *
     * @param index des bestimmten pfads im Vector
     * @return den bestimmten pfad eines Bildes
     */
    public String getBilder(int index) {
        return bilder.get(index);
    }


    /**
     * getter f�r die anzahl vorhandenebilder
     *
     * @return anzahl Bilder die geladen wurden
     */
    public int getAnzahlBilder() {
        return bilder.size();
    }

    /**
     * setter f�r die anzahl Bilder die laut ausgew�hlter spielgr�sse ben�tigt werden
     *
     * @param size der ben�tigten Karten
     */
    public void setBilder(int size) {
        for (int i = 1; i <size+1 ; i++) {
            bilder.add(pfad +"\\bild"+i+".jpg");//Grundpfad,plus Bild, zahl des Bildes, endung der Datei


        }//bilder sind numeriert 1-50
    }


    /**
     * getter f�r das Hintergrundbild der Memorykarten
     *
     * @return pfad des Hintergundbildes
     */
    public String getHintergrundbild() {
        return pfad +"\\"+hintergrundbild;
    }
}
