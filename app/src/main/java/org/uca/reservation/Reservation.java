/* 
package org.uca.reservation;

public class Reservation {

    private String client;

    public Reservation() {
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }
}


*/

package org.uca.reservation;

import java.time.ZonedDateTime;

/*
     Entité centrale — API publique inchangée par rapport au repo d'origine,
    enrichie avec le pattern State via EtatReservation.
    
 */
public class Reservation {

    private final String          numero;   // identifiant unique
    private final ZonedDateTime   date;     // readonly après construction
    private final double           prix;     // readonly après construction
    private       EtatReservation etat;
    private final Client          client;
    private final Passager        passager;

    // Constructeur package-private : passer par ReservationFactory. 

    Reservation(String numero, double prix, Client client, Passager passager) {
        this.numero   = numero;
        this.date     = ZonedDateTime.now();
        this.prix     = prix;
        this.client   = client;
        this.passager = passager;
        this.etat     = new EnAttente();   // état initial toujours EN_ATTENTE
    }

    // ------------------------------------------------------------------ //
    //  Méthodes métier — délèguent à l'état courant                       
    // ------------------------------------------------------------------ //

    public void confirmer() {
        etat.confirmer(this);
    }

    public void payer() {
        etat.payer(this);
    }

    public void annuler() {
        etat.annuler(this);
    }

    public void debiter() {
        
    }

    public void rembourser() {
        
    }

    // ------------------------------------------------------------------ //
    //  Accesseurs                                                          //
    // ------------------------------------------------------------------ //

    public String getNumero()            { return numero;  }
    public ZonedDateTime getDate()       { return date;    }
    public double getPrix()               { return prix;    }
    public Client getClient()            { return client;  }
    public Passager getPassager()        { return passager; }
    public EtatReservation getEtat()     { return etat;    }

    // Appelé uniquement par les classes d'état — pas par le code métier. 
    void setEtat(EtatReservation nouvelEtat) {
        this.etat = nouvelEtat;
    }

    @Override
    public String toString() {
        return "Reservation{" + numero + ", " + etat.libelle() + ", " + prix + "}";
    }
    
}

