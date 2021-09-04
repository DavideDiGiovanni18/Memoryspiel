import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Collections;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.Timer;

/**
 * Klasse für ein Memoryspiels. Hier befindet sich die Logik für das WelcomeGUI, EinstellungsGUI, ZwischenstandGUI
 * Die Memorykarten werden geladen mit Bilder, Punkte werden vergeben (plus Jokerpunkte), auch wird ein Timer gesetzt.
 * Zudem werden Karten anhand der usgewählten Grösse geladen
 */


public class Memoryspiel extends JFrame{
    private JDialog welcome;//Wilkommensfenster
    private JDialog einstellung;//einstelllungsfenster
    private JDialog zwischenstand;//zwischenstandsfenster

    private Vector<Karte> karten;//Die Karten
    private Vector<Integer> punkte;//für die Memorykarten
    private Vector<Karte>richtigeMatches = new Vector<>();
    private int anzahlMatched =0;

    private String[] kartengroesse={"5x5","6x6","7x7","8x8","9x9","10x10"};//Kartengrössen die fix gesetzt sind

    private JPanel kartenpanel;
    private JPanel panelObenSpiel;
    private JPanel paneluntenSpiel;

    private StatistikGUI gui = new StatistikGUI();//wird nur eimal erezugt
    private Imageloader loader = new Imageloader();
    private Vector<Spieler> spieler = new Vector<>();

    private int punkteplayer1;
    private int punkteplayer2;
    private int AktuellerSpieler;

    private JButton beenden;
    private JButton neustart;
    private JButton start;
    private JButton beendenStatistik;

    private JLabel spieler1NameGUI;
    private JLabel spieler2NameGui;
    private JLabel punkte1;
    private JLabel punkte2;

    private JTextField eins = new JTextField("Spieler1");
    private JTextField zwei = new JTextField("Spieler2");
    
    private String spielGroesse;
    private int SpielBreite;
    private int SpielLaenge;


    private JLabel timerCountDown;
    private int zeitt =0;
    private Timer a;




    /**
     * Konstruktor für die Klasse Memoryspiel
     */
    public Memoryspiel(){
     welcomeGUI();

    }

    /**
     * erstellt das Hauptgui= MemoryspielGUI
     */
    public void init() {
        punkteplayer1=0;//punkte werden bei beiden auf 0 gesetzt
        punkteplayer2=0;
        AktuellerSpieler=0;

        //Panel oben des MemoryspielGUI wird erstellt
        panelObenSpiel = new JPanel(new GridLayout(2,3,290,10));

        spieler1NameGUI = new JLabel(spieler.get(0).getBenutzername());//Name Spieler1
        spieler2NameGui = new JLabel(spieler.get(1).getBenutzername());//Name Spieler2
        spieler1NameGUI.setForeground(Color.BLUE);//Spieler1 startet deshalb wird er markiert
        spieler2NameGui.setForeground(Color.BLACK);//Spieler zwei wird auf Scharz gesetzt, da er nicht and er Reohe ist

        JLabel timer = new JLabel("Timer");//Beschriftung Timer
        punkte1 = new JLabel("Punkte: "+ this.punkteplayer1);//adden zweite Reihe
        timerCountDown = new JLabel("   15");
        punkte2 = new JLabel("Punkte: "+ this.punkteplayer2);


        panelObenSpiel.add(spieler1NameGUI);//adden erste Reihe
        panelObenSpiel.add(timer);
        panelObenSpiel.add(spieler2NameGui);

        panelObenSpiel.add(punkte1);//adden zweite Reihe
        panelObenSpiel.add(timerCountDown);
        panelObenSpiel.add(punkte2);

        getContentPane().add(panelObenSpiel,BorderLayout.NORTH);// adden aufs JFrame


        //Mittelpanel mit den Karten
        kartenpanel = new JPanel(new GridLayout(getSpielBreite(),getSpielLaenge(),10,10));
        //Es Werden so viele Reihen und spalten für die Spielgrösse erstellt wie ausgewählt(Layout).
        kartenpanel.setBackground(Color.WHITE);// Der Hintergund wird auf weiss gesetzt

        createCard();//Karten werden ersetllt
        Collections.shuffle(karten);//Sie werden gemischt
        
        for (int i = 0; i <karten.size(); i++) {//Karten auf MittelPanel geadded
            kartenpanel.add(karten.get(i));
        }
        getContentPane().add(kartenpanel,BorderLayout.CENTER);//Auf JFrame geadded
        

        //Panel unten wird erstellt
        JPanel untenRechts = new JPanel(new BorderLayout());
        beenden = new JButton("beenden");
        untenRechts.add(beenden,BorderLayout.EAST);//setzt den Button in die Ecke

        paneluntenSpiel = new JPanel(new BorderLayout());
        paneluntenSpiel.add(untenRechts,BorderLayout.SOUTH);//setzt das Panel mit dem Button in den süden
        getContentPane().add(paneluntenSpiel,BorderLayout.SOUTH);

        
        beenden.addActionListener(new ActionListener() {//Listener wird augerufen für das Drücken des Buttons(beenden)
            @Override
            public void actionPerformed(ActionEvent e) {
                a.stop();//Der Timer wird gestoppt,sodass er bei einem neustart neu startet
                zwischenstand();//der Zwischenstand wird aufgerufen
            }
        });

        Timer(15);//Timer wird erstellt
        addListener();//added die Listener

        setVisible(true);
    }

    /**
     * Methode um den Benutzernamen auf ein Regex zu testen
     *
     * @param benuztername welcher der Spieler auswählt
     * @return true oder false = regex bedingt
     */
    public boolean regex(String benuztername){
        String regex = "\\D";//keine Zahlen erlaubt
        Pattern tester  = Pattern.compile(regex);
        Matcher mt = tester.matcher(benuztername);
        return mt.matches();
    }

    /**
     * Erstellt die Karten die für das Memoryspiel benötigt werden und setzt das Hintergrundbild der Memorykarte
     * als Icon, weil die Memorykarten zuerst umgedreht sind. Hintergund sieht man also Zuerst
     */
    public void createCard(){
        karten = new Vector<>();
        for (int i = 0; i < getSpielGroesse(); i++) {
            karten.add(new Karte());
            karten.get(i).setIcon(new ImageIcon(getClass().getResource(loader.getHintergrundbild())));
        }

        imageloader();//laded die Bilder

        if(karten.size()%2==1){//Hier wird geschaut ob es sich um eine Gerade oder Ungerade ausgewählte Spielgrösse handelt
            karten.setSize(karten.size()-1);//Bei ungerader wird eine Karte gelöscht, das sie keine Bedeutung hat
            // (beinhaltet Kein Bild und Punkte )
        }
    }

    /**
     * Methode um die Punkte zu den vercschiedenen Karten zu setzen.
     * Es gibt Normale Karten =20 Punkte(2*10) und Jokerkarten =40 Punkte(2*20),
     * da die punkte pro Karte gesetzt wird und nicht pro paar.
     */
    public void setPunkte() {
        punkte = new Vector<>();
        for (int i = 0; i <3 ; i++) {//Standart 3 Jokerkarten i
            punkte.add(20);//jokerkarten
        }
        for (int i = 0; i <(getSpielGroesse()/2)-2 ; i++) {//die restlichen Karten für die Vervollständigung
            punkte.add(10);
        }
    }

    /**
     * Diese Methode wird benötigt um die Bilder und die Punkte an den jeweiligen Karten zu zu teilen.
     */
    public void imageloader(){
        int kartenmenge=0;
        setPunkte();//Punkte werden gesetzt
        loader.setBilder((int)Math.ceil(getSpielGroesse()/2));
        for (int j= 0; j < 2 ; j++) {//Da nur die Hälfte der Karten vorhaden ist z.B. bis 12 muss es doppelt durchgeführt werden,
            //weil Vector nur 12 stellen beinhaltet, 24 Karten mit Bilder benötigt werden und i zwingend nach wert 12
            // wieder auf 0 gesetzt werden muss.
            for (int i = 0; i < getSpielGroesse()/2; i++) {
                karten.get(kartenmenge).setBild(loader.getBilder(i));
                karten.get(kartenmenge).setPunkte(punkte.get(i));
                kartenmenge++;
            }
        }
    }


    /**
     * Das ZwischenstandGUI erscheint immer nach jedem Beendetem Spiel und sollte dem Benutzer die Möglichkeit geben
     * Das Resultat des vorherigen Spiels pro Spieler anzuzeigen.
     */
    public void zwischenstand(){
        zwischenstand = new JDialog();//erstellt das JDialog
        zwischenstand.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        zwischenstand.setTitle("Zwischendstand");
        zwischenstand.setSize(420,420);
        zwischenstand.setResizable(false);
        zwischenstand.setLocationRelativeTo(null);

        //Panel oben
        JPanel oben = new JPanel(new BorderLayout());
        JLabel nachricht = new JLabel("Das Spiel ist fertig");
        oben.add(nachricht,BorderLayout.NORTH);
        oben.add(new JSeparator(),BorderLayout.SOUTH);//Linie die es separiert
        zwischenstand.add(oben,BorderLayout.NORTH);

        //Panel Mitte
        JPanel mitte = new JPanel(new GridLayout(3,1,10,10));

        JLabel punktestand = new JLabel("Punktestand:");
        punkte1 = new JLabel(spieler.get(0).getBenutzername()+": "+ this.punkteplayer1);//gleichen Label wie im MemoryGUI verwendet
        punkte2 = new JLabel(spieler.get(1).getBenutzername()+ ": "+ this.punkteplayer2);

        mitte.add(punktestand);
        mitte.add(punkte1);
        mitte.add(punkte2);
        zwischenstand.add(mitte,BorderLayout.CENTER);

        //Panel unten mit den Knöpfen
        JPanel unten = new JPanel(new GridLayout(1,2,20,20));
        neustart = new JButton("Neustarten");
        beendenStatistik = new JButton("Beenden/Statistik");
        unten.add(neustart);
        unten.add(beendenStatistik);

        zwischenstand.add(unten,BorderLayout.SOUTH);

        //Listener für den Neustart
        neustart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);//lässt das MemorykartenGUI verschwinden
                zwischenstand.setVisible(false);//lässt das zwischenstandGUi verschwinden


                kartenpanel.setVisible(false);//setzt das alte Panel auf nicht sehbar
                panelObenSpiel.setVisible(false);//dadurch wird das alte panel wie unsichtbar
                paneluntenSpiel.setVisible(false);//und wird unter den neuen vergraben

                //Statistik wird ausgeführt. Leitet die Punkte an das StatstikGUI
                setStatistikwerte();

                einstellungsGui();//ruft das EinstellungsGUI wieder auf um neuzustarten
            }
        });

        //listener für das Beenden eines Spiels
        beendenStatistik.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setStatistikwerte();

                gui.showDatenGUI();//ruft das Gui der Statistik auf
                gui.setLocationRelativeTo(null);

                zwischenstand.setVisible(false);//Zwischenstand verschwindet
                setVisible(false);//Das MemorykartenGUI auch
            }
        });
        a.stop();//Der Timer wird gestoppt

        zwischenstand.pack();
        zwischenstand.setVisible(true);

    }

    /**
     * Diese Methode setzt dei Spielgrösse durch die, die ihm EinstellungsGUI ausewählt wurde
     *
     * @param anzahl ist die Speilgrösse die ausgewählt wurde
     */
    public void setSpielGroesse(String anzahl){
        String[] trim = anzahl.split("x");//teilt den String in zwei teile
        setSpielLaenge(Integer.parseInt(trim[0]));//erste zahl=länge
        setSpielBreite(Integer.parseInt(trim[1]));//zweite zahl=breite
    }

    /**
     * getter für die Speilgrösse des Memoryspiels
     *
     * @return Grösse des Memoryspiels
     */
    public int getSpielGroesse(){return (getSpielBreite()*getSpielLaenge());}

    /**
     * getter für die Spielerbreite
     *
     * @return breite des Memoryspiels
     */
    public int getSpielBreite(){ return SpielBreite;}

    /**
     * setter für die Memorybreite
     *
     * @param spielBreite des Memoryspiels
     */
    public void setSpielBreite(int spielBreite) {
        SpielBreite = spielBreite;
    }

    /**
     * getter für die Memoryspiellänge
     *
     * @return die länge des Memoryspiels
     */
    public int getSpielLaenge() {
        return SpielLaenge;
    }

    /**
     * setter für die Memoryspiellänge
     *
     * @param spielLaenge des Memory
     */
    public void setSpielLaenge(int spielLaenge) {
        SpielLaenge = spielLaenge;
    }


    /**
     * Diese Methode added pro Karte zwei funktionswichtige Listener, um die Knöpfe interaktiv zu machen
     * Listener werden unten erklärt
     */
    public void addListener(){

        for (int i = 0; i <karten.size() ; i++) {
            karten.get(i).addActionListener(new memorylistener());
            karten.get(i).addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    Matched();
                }
            });
        }
    }

    /**
     * Dieser Listener ist Zuständig, bei zwei richtigen Kartenpaare, diese verschwinden zu lassen und am aktuellen Spieler Punkte zu verteilen.
     * Auch kehrt er falsche Kartenpaare um. Der Listener wechselt auch automatisch den Spieler.
     * Wichtig: dieser Listener wird erst aufgerufen, wenn zwei Karten umgedereht werden. Erst bei erneutem Drücken auf eine neue Karte werden
     * sich diese alten zwei drehen oder verschwinden.
     */
    public void Matched() {//richtigeMatches beinhaltet die Objekte der Karten die man ausgewählt hat
            if (richtigeMatches.size() == 2 && richtigeMatches.get(0).getBild().equals(richtigeMatches.get(1).getBild())) {
                //wenn zwei Karten umgedreht wurden und ihre Bilder Miteinander übereinstimmen (geht er hier rein)
                int[] zahlen =new int[2];//dieses wird erstellt um sich die Positionen der Karten die umgedreht wurden
                // im KartenVector zu merken und um danach die Punkte dieser Karten zu abfragen.

                for (int i = 0; i < karten.size() ; i++) {
                    if (karten.get(i) == richtigeMatches.get(0)) {
                        karten.get(i).setVisible(false);
                        zahlen[0]=i;
                    } else if (karten.get(i) == richtigeMatches.get(1)) {
                        karten.get(i).setVisible(false);
                        zahlen[1]=i;

                    }
                }
                richtigeMatches.setSize(0);//richtige Matches werden auf 0 gesetzt, neu 0 karten ausgewählt

                //Punkte vergabe and en Spielern
                if (AktuellerSpieler == 1) {
                    punkteplayer2 += karten.get(zahlen[0]).getPunkte()*2;
                    punkte2.setText("Punkte " + punkteplayer2);

                } else {
                    punkteplayer1 += karten.get(zahlen[0]).getPunkte()*2;
                    punkte1.setText("Punkte " + punkteplayer1);
                }


                this.repaint();//Gui wird neu gezeichnet um die Punkte zu aktualisieren
                anzahlMatched += 2;//Es gab ein Mached mit zwei Karten, das heisst z.B. noch 24-2=22 Karten übrig

                a.stop();//Timer wird gestoppt
                Timer(15);//Timer wird von neu gestartet

            } else if (richtigeMatches.size() == 2) {//Falls diese zwei Karten nicht übereinstimmen

                //Sie werden wieder (die zwei Karten) umgedreht. Man sieht wieder ihr Hintergrundbild
                for (int i = 0; i < karten.size() ; i++) {
                    if (karten.get(i) == richtigeMatches.get(0)) {
                       karten.get(i).setIcon(new ImageIcon(getClass().getResource(loader.getHintergrundbild())));
                    } else if (karten.get(i) == richtigeMatches.get(1)) {
                        karten.get(i).setIcon(new ImageIcon(getClass().getResource(loader.getHintergrundbild())));
                    }
                }
                spielerwechseln();//wechselt den Spieler

                richtigeMatches.setSize(0);//richtige Matches werden auf 0 gesetzt, neu 0 karten ausgewählt
            }
    }

    /**
     * Diese Methode wird genutzt um die Spielerrolle zu ändern, wer an der Reihe ist wird Blau angezeigt
     */
    public void spielerwechseln(){
        if (AktuellerSpieler == 1) {//Spieler2
            AktuellerSpieler--;
            spieler2NameGui.setForeground(Color.BLACK);
            spieler1NameGUI.setForeground(Color.BLUE);
            a.stop();
            Timer(15);

        } else {//Spieler1
            AktuellerSpieler++;
            spieler2NameGui.setForeground(Color.BLUE);
            spieler1NameGUI.setForeground(Color.BLACK);
            a.stop();
            Timer(15);
        }
    }

    /**
     * setter für die Ergebnisse der Spiele in der Statistikdatenbank
     */
    public void setStatistikwerte(){
        gui.setAnzahlspiele(1);//plus ein Spiel gespielt
        gui.setPunkteplayer1(Integer.valueOf(punkteplayer1+""));//Punkte1
        gui.setPunkteplayer2(Integer.valueOf(punkteplayer2+""));//punkte2
        gui.setBenutzername1(spieler.get(0).getBenutzername());//Benutzername1
        gui.setBenutzername2(spieler.get(1).getBenutzername());//Benutzername2
    }


    /**
     * Methode um den Timer einzusstellen
     *
     * @param zeit die ausgeführt werden soll
     */
    public void Timer(int zeit) {
        this.zeitt = zeit;
            a = new Timer(950, new ActionListener() {//Der Listener wird unabhängig
                //von anderen vorgängen, jede sekunde ausgeführt
                @Override
                public void actionPerformed(ActionEvent e) {
                    timerrunterzaehlen();
                }
            });
        a.start();
    }

    /**
     * Diese Methode wird jede sekunde augseführt.
     *
     * Sie ist zuständig um auf den MemoryGUI die verbleibenden Sekunden anzuzeigen die
     * ein Spieler pro Zug noch hat
     */
    public void timerrunterzaehlen(){
            if (zeitt > -1) {
                timerCountDown.setText("   " + zeitt);//setzt immer neu den texts
                zeitt--;
                this.repaint();
            } else {
                a.stop();
                spielerwechseln();//falls Timer abgelaufen ist und der Spieler noch keinen Zug(Kompletten) getätigt hat
                //wird der Spieler gewechselt
            }
    }

    /**
     * getter für die Sekunden welche für den Timer eingestellt ist
     *
     * @return Sekunden wie lange der timer dauert
     */
    public int getTimerSekunden() {
        return zeitt;
    }

    /**
     * Gui für Die Einstellungen für das Memoryspiel, die der Benutzerauswählen kann
     * Benutzername eingabe und Kartengrösse auswählen
     */
    public void einstellungsGui() {
        richtigeMatches.setSize(0);
        anzahlMatched=0;

        spielGroesse ="5x5";//der default wert der Kartenanzahl, falls nichts ausgewählt

        einstellung = new JDialog();//erstellung des JDialog

        einstellung.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        einstellung.setTitle("Einstellungen");
        einstellung.setSize(350,350);
        einstellung.setLocationRelativeTo(null);//Position Mitte des Bildschirms
        einstellung.setResizable(false);

        //JPanel der JTextfelder /Oben
        JPanel felder = new JPanel(new GridLayout(1, 2, 20, 20));
        felder.add(eins);//added die zwei JTextfield
        felder.add(zwei);

        JPanel oben = new JPanel(new BorderLayout(20,20));
        JLabel text = new JLabel("Waehlen sie einen Benutzernamen");

        oben.add(text,BorderLayout.NORTH);
        oben.add(felder,BorderLayout.CENTER);

        einstellung.add(oben,BorderLayout.NORTH);//Im Norden geadded


        //Panel in der Mitte + unten
        JPanel down = new JPanel(null);//null wegen der setBounds Methode
        JComboBox<String>groessen= new JComboBox<>(kartengroesse);//Combobox mit den Auswahl Karten
        groessen.setSelectedIndex(0);//5x5 wird zuerst angezeigt

        JButton starten = new JButton("Spiel Starten");
        JLabel auswahl = new JLabel("Waehlen sie die Spielgroesse");

        auswahl.setBounds(20,10,200,50);//setzt die Komponente genau an diesen Koordinaten
        //mit dieser länge und breite, Unterschied zu Layouts: sie könenn überall und unkompliziert platziert werden(im Panel)
        groessen.setBounds(120,70,100,20);
        starten.setBounds(100,130,150,50);

        down.add(starten);
        down.add(groessen);
        down.add(auswahl);


        //Listener der die ausgeählte Spielgrösse von der Combobox in die Variable Spielgrösse speichert
        groessen.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                spielGroesse = String.valueOf(groessen.getSelectedItem());
            }
        });

        //Listener der beim starten ausgeführt wird
        starten.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                spieler.setSize(0);//alle Spieler werden vom Vector entfernt(bei mehrfach ausführung)

                Spieler spieler1 = new Spieler();
                //erstellt beide Spieler neu um sie mit den neuen Benutzernamen im Vector zu speichern
                Spieler spieler2 = new Spieler();


                if(eins.getText().length() ==0){//Überprüft ob die eingabe leer ist
                    eins.setText("Spieler1");//wenn ja, wird der defaultname wieder gesetzt
                }
                spieler1.setBenutzername(eins.getText());//Spielername wird gespiechert


                if(zwei.getText().length() ==0){//siehe erklärung oben
                    zwei.setText("Spieler2");
                }
                spieler2.setBenutzername(zwei.getText());

                spieler.add(spieler1);//spieler werden geadded
                spieler.add(spieler2);

                setSpielGroesse(spielGroesse);//setzt die ausgewählte Spielgrösse

                einstellung.setVisible(false);

                init();//ruft das MemorykarteGUI auf
            }
        });

        einstellung.add(down,BorderLayout.CENTER);//Panel wird geadded

        einstellung.setVisible(true);
    }

    /**
     * WilkommensGUI des Memoryspiels.
     */
    public void welcomeGUI(){
        welcome = new JDialog();//erstellt ein neuer JDialog
        welcome.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        welcome.setTitle("Wilkommen");
        welcome.setLayout(null);
        welcome.setResizable(false);
        welcome.setSize(420,420);
        welcome.setLocationRelativeTo(null);


        JLabel logo = new JLabel();//Erstellt das LOGO
        ImageIcon image = new ImageIcon(new ImageIcon(getClass().getResource("\\Bilder\\" +
                "Memorylogo.png")).getImage().getScaledInstance(300, 150, Image.SCALE_SMOOTH));
        //Bild skalieren auf passende Grösse
        logo.setIcon(image);

        start = new JButton("Get Started");

        //Positionen mit SetBound umgesetzt
        logo.setBounds(60,40,300,150);
        start.setBounds(130,240,150,50);

        welcome.add(logo);
        welcome.add(start);

        //Listener für den Startbutton
        start.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                welcome.setVisible(false);
                einstellungsGui();//leitet auf das EinstellungsGUI hin
            }
        });
        welcome.setVisible(true);
    }


    /**
     * getter für die Kartenanzahl des Memory
     *
     * @return anzahl Karten des Memoryspiels
     */
    public int getKartenSize() {
        return karten.size();
    }

    /**
     * getter für  eine der Karten im Vetor
     *
     * @param index der zu auswählenden Karte
     * @return eine Karten vom Vetor
     */
    public Karte getKarten(int index) {
        return karten.get(index);
    }

    /**
     * gibt die Anzahl Kartengrössen die Vorhanden sind zurück
     * z.B. 5x5,6x6 etc.
     *
     * @return verfügaber Kartengrössen
     */
    public int getKartengroesse() {
        return kartengroesse.length;
    }

    /**
     * getter für die Kartengrösse die Vorhanden sind zurück
     *
     * @param index gibt Spielgrösse an der bestimmten Position des Arrays zurück
     *
     * @return die Kartengrösse im String format(5x5)
     */
    public String getKartengroesse(int index) {
        return kartengroesse[index];
    }


    /**
     * Main Methode für das Ausführen des Programms
     */
    public static void main(String[] args) {
        JFrame welcoome = new Memoryspiel();
        welcoome.setSize(800,900);
        welcoome.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        welcoome.setLocationRelativeTo(null);
        welcoome.setVisible(false);
    }

    /**
     * Private Klasse welche den zweiten Karten Listener beinhaltet
     * Dieser Listener kehrt die Verdeckten Karten, auf denen man drückt um, setzt dabei das Bild auf den Button und speichert
     * das aktuelle Buttonobjekt, um später den vergleich zwischen Buttons zu machen(Richtige Paare)
     */
    private class memorylistener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
                for (int j = 0; j < karten.size(); j++) {
                    if (karten.get(j) == e.getSource()) {//sucht den Button der gedrückt wurde
                        karten.get(j).setIcon (new ImageIcon(new ImageIcon(getClass().getResource(karten.get(j).getBild())).
                                getImage().getScaledInstance(karten.get(0).getWidth(),karten.get(0).getHeight(),Image.SCALE_SMOOTH)));
                        //setzt das zugeteilene Bild als Icon und passt dies an der entsprechenden Buttongrösse an
                        if (!richtigeMatches.contains(karten.get(j))) {//Redunanten Knopfdrücke werden nicht
                            // beachtet=Objekt wird nur einmal gespeichert
                            richtigeMatches.add(karten.get(j));
                        }
                    }
                }

                if(richtigeMatches.size()==2){
                  a.stop();//stoppt den Timer wenn zwei Karten gezogen wurden und die falsch bzw. richtig sind
                    // damit die Zeit nicht weiterläuft sonder stoppt, weil ja der Zug vorbei ist. Somit hat der Spieler genügend
                    //Zeit um den Nächsten Zug zu machen
                }
                //Falls die Karten Grösse z.B bei 24 Karten noch 22 ist wird eins dazu gerechnet.
                //Sodass wenn man auf die Letzte Karte drückt direkt das ZwischenGUI kommt
                if(anzahlMatched==getKartenSize()-2){
                    anzahlMatched++;
                    a.stop();
                    //hier wird das ZwischenGui aufgeführt
                }else if(anzahlMatched>=getKartenSize()-1){
                    timerCountDown.setVisible(false);
                    a.stop();
                    //Am Schluss werden noch die letzten Punkte einzeln addiert werden
                    //weil es nicht mehr geht in den anderen Punkte verteiler Listener Matched() Listener zu gehen, da es keine
                    //weiteren Karten hat.
                    if(AktuellerSpieler==0){
                        punkteplayer1 +=richtigeMatches.get(0).getPunkte()*2;
                    }else {
                        punkteplayer2+=richtigeMatches.get(0).getPunkte()*2;
                    }
                    zwischenstand();//das ZwischenstandGui wird ausgerufen

                }
            }
        }
}

