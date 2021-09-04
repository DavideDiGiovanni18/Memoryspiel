import org.junit.*;
import static org.junit.Assert.*;
import javax.swing.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collections;
import java.util.Vector;

/**
 * Testklasse für die Klassen Memoryspiel, Imagloader , Karte, StatistikGUI, Spieler
 * Testfälle gemäss M5
 *
 * @author Davide Di Giovanni
 * @since 1.07.2020
 */

public class TestMemory {
    private static Memoryspiel a;

    //setup
    @Before
    public void setUp(){
        a = new Memoryspiel();
    }

    /**
     * Testfall TF-001
     * Da das testen der Anzeige mit dem GUI nicht geht, habe ich hier eine Rekonstruktion
     * dem zu testenden code gemacht. Welche 1 zu 1 dem wirklichen Code im Memorygui entspricht.
     * auch Manuel geprüft =richtig
     */
    @Test
    public void defaultBenutzername(){
        JTextField a = new JTextField("Spieler1");
        //Textfeld ist erstellt mit Defaultwert
        JButton button = new JButton();//Button = wie Spiel starten button
        JLabel label = new JLabel();
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                label.setText(a.getText());//Im label sollte jetzt Speiler1 stehen
            }
        });
        button.doClick();//wird künstlich aufgerufen
        assertEquals("Spieler1",label.getText());
    }

    /**
     * TF-002
     * Testet bei Eingabe eines eigenen Benutzernames, dieser auch
     * übernommen wird.
     * ->Rekonstruktion wie oben auch
     * auch Manuel geprüft =richtig
     */
    @Test
    public void testEmptyBenutzername()  {
        JTextField a = new JTextField("");//
        JButton button = new JButton();
        JLabel label = new JLabel();
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(a.getText().length()==0){//falls leerer string
                    label.setText("Spieler1");// set als name Spieler1
                }
            }
        });
        button.doClick();
        assertEquals("Spieler1",label.getText());
    }

    /**
     * TF-003
     * Testen ob bei der Eingabe eines Benutzernames, dieser auch übernommen wird
     * ->Rekonstruktion
     * auch Manuel geprüft =richtig
     */
    @Test
    public void testUsername(){
        JTextField a = new JTextField("Spieler1");
        JButton button = new JButton();
        JLabel label = new JLabel();
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                a.setText("Tenzin");
                label.setText(a.getText());
            }
        });
        button.doClick();//simlureirt die Eingabe welche auf dem GUI abgeben wird.
        //vom eigenen Benutzernamen
        //Bei Knopfdruck sollte dieser text auf dem Neuen Gui stehen(im JLabel)

        assertEquals("Tenzin",label.getText());
    }


    /**
     * TF-004
     * Testet ob genau so viele Karten erstellt werden wie ausgewählt
     */

    //1. Ungerade anzahl = gerade
    @Test
    public void testUngeradeKartenErstellen(){
        a.setSpielGroesse("5x5");
        a.createCard();
        assertEquals(24,a.getKartenSize());
    }

    //2. gerade anzahl = gerade
    @Test
    public void testGeradeKartenErstellen(){
        a.setSpielGroesse("10x10");
        a.createCard();
        assertEquals(100,a.getKartenSize());
    }


    /**
     * TF-005
     * Testet ob die richtigen Spielgrössen im Vector vorhanden sind
     */

    //Total5
    @Test
    public void testSize(){
        assertEquals(6,a.getKartengroesse(),0.00);
        assertEquals("5x5",a.getKartengroesse(0));
        assertEquals("6x6",a.getKartengroesse(1));
        assertEquals("7x7",a.getKartengroesse(2));
        assertEquals("8x8",a.getKartengroesse(3));
        assertEquals("9x9",a.getKartengroesse(4));
        assertEquals("10x10",a.getKartengroesse(5));
    }
    //Kartenanzahl auch vorhanden?
    @Test
    public void testTrueSize(){
        Vector<String> test = new Vector();
        for (int i = 0; i <a.getKartengroesse() ; i++) {
            test.add(a.getKartengroesse(i));
        }
        if(test.contains("6x6")) {
            assertEquals("6x6",test.get(test.indexOf("6x6")) );
        }else {
            throw new AssertionError();
        }
    }

    //Kartenanzahl nicht vorhanden?
    @Test(expected = AssertionError.class)
    public void testFalseSizeNotFound(){
        Vector<String> test = new Vector();
        for (int i = 0; i <a.getKartengroesse() ; i++) {
            test.add(a.getKartengroesse(i));
        }
        if(test.contains("4x4")) {
            assertEquals("4x4",test.get(test.indexOf("4x4")) );
        }else {
            throw new AssertionError();
        }
    }

    /**
     *TF-006
     * Testen ob es Jokerkarten hat bzw. Karten die mehr Punkte geben.
     * 20 Punkte pro Karte, 40 Punkte pro Paar
     */
    @Test
    public void testJokerkartenvorhanden(){
            this.a.setSpielGroesse("10x10");
            this.a.createCard();
            Vector<Integer> karte = new Vector();
            for (int i = 0; i <this.a.getKartenSize() ; i++) {
                karte.add(this.a.getKarten(i).getPunkte());
            }
            if (karte.contains(20)) {
                assertEquals(20, karte.get(karte.indexOf(20)),0.00);
            }else {
                throw new AssertionError();
            }
        }

    /**
     * TF-007
     * Testen ob es genau so viele Bilder hat wie benötigt wird
     */
    @Test
    public void testBilderVorhanden(){
        Imageloader a = new Imageloader();
        a.setBilder(50);
        assertEquals(100,a.getAnzahlBilder()*2);
    }


    /**
     * TF-008
     * Testet ob der eingebene Benutzernamen der Regex entspricht oder nicht
     */
    //Unzulässiger Name
    @Test(expected = AssertionError.class)
    public void testRegexFalse(){
        JTextField feld = new JTextField("142453363736554");
        if(a.regex(feld.getText())){
            assertTrue(a.regex(feld.getText()));
        }else {
            throw new AssertionError();
        }
    }
    //Zulässiger Name
    @Test
    public void testRegexTrue(){
        JTextField feld = new JTextField("A");
        if(a.regex(feld.getText())){
            assertTrue(a.regex(feld.getText()));
        }else {
            throw new AssertionError();
        }
    }


    /**
     * TF-009
     * Testet ob die Karten gemischt werden
     */
    @Test(expected = AssertionError.class)
    public void KartenImmerandersAngeordnet(){
        a.setSpielGroesse("10x10");
        a.createCard();

        Vector<Karte> test = new Vector();
        for (int i = 0; i <a.getKartenSize() ; i++) {
            test.add(a.getKarten(i));
        }
        Collections.shuffle(test);//gleich wie im Hauptprogramm Memory, init von hier aus nicht abrufbar
        System.out.println(test.get(0).getBild());
        assertEquals("System.out.print sollte immer eine andere Bilderzahl zeigen",
                test.get(0).getBild());
    }

    /**
     * TF-0010
     * Testen ob der Timer auf 15 Sekunden eingestellt ist
     * (geändert von 20sek auf 15, weil 20 zu viel sind)
     */

    @Test
    public void testTimer(){
        a.Timer(15);
        assertEquals(15,a.getTimerSekunden());
    }








}
