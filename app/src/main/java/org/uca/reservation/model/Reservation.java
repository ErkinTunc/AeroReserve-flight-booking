
package org.uca.reservation.model;

import java.time.ZonedDateTime;

import org.uca.reservation.state.EnAttente;
import org.uca.reservation.state.EtatReservation;

import org.uca.aeroport.Vol;

/*
     Entité centrale — API publique inchangée par rapport au repo d'origine,
    enrichie avec le pattern State via EtatReservation.
    
 */
public class Reservation {

    private final String numero; // identifiant unique
    private final ZonedDateTime date; // readonly après construction
    private final double prix; // readonly après construction
    private EtatReservation etat;

    private final Client client;
    private final Passager passager;
    private final Vol vol;

    // -------------------- Constructeur -------------------- //

    // Constructeur package-private : passer par ReservationFactory.
    Reservation(String numero, double prix, Client client, Passager passager, Vol vol) {
        if (numero == null || numero.isBlank()) {
            throw new IllegalArgumentException("Le numero de reservation est obligatoire");
        }
        if (client == null) {
            throw new IllegalArgumentException("Le client est obligatoire");
        }
        if (passager == null) {
            throw new IllegalArgumentException("Le passager est obligatoire");
        }
        if (vol == null) {
            throw new IllegalArgumentException("Le vol est obligatoire");
        }

        this.numero = numero;
        this.date = ZonedDateTime.now();
        this.prix = prix;
        this.client = client;
        this.passager = passager;
        this.vol = vol;
        this.etat = new EnAttente();

        client.addReservation(this);
    }

    // ------------------------------------------------------------------ //
    // Méthodes métier — délèguent à l'état courant
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
    // Accesseurs //
    // ------------------------------------------------------------------ //

    public String getNumero() {
        return numero;
    }

    public ZonedDateTime getDate() {
        return date;
    }

    public double getPrix() {
        return prix;
    }

    public Client getClient() {
        return client;
    }

    public Passager getPassager() {
        return passager;
    }

    public EtatReservation getEtat() {
        return etat;
    }

    public Vol getVol() {
        return vol;
    }

    // Appelé uniquement par les classes d'état — pas par le code métier.
    public void setEtat(EtatReservation nouvelEtat) {
        if (nouvelEtat == null) {
            throw new IllegalArgumentException("L'etat de reservation est obligatoire");
        }
        this.etat = nouvelEtat;
    }

    @Override
    public String toString() {
        return "Reservation{" + numero + ", " + etat.libelle() + ", " + prix + "}";
    }

}
