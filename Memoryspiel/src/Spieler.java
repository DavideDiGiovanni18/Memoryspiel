/**
 * Klasse für einen Benutzer des Memoryspiels
 *
 * @author Davide Di Giovanni
 * @since 27.06.2021
 *
 */

public class Spieler {
    private String benutzername;


    /**
     * Konstruktor des Spielers
     */
    public Spieler(){}

    /**
     * getter für den Benutzernamen des Spielers
     *
     * @return den Benutzernamen des Spielers
     */
    public String getBenutzername() {
        return benutzername;
    }

    /**
     * setter für den Benutzernamen des Spielers
     *
     * @param benutzername des Spielers
     */
    public void setBenutzername(String benutzername) {
        this.benutzername = benutzername;
    }
}
