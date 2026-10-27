package org.uca.reservation.model;

import java.time.ZonedDateTime;
import java.util.Objects;

import org.uca.aeroport.Vol;
import org.uca.reservation.state.EnAttente;
import org.uca.reservation.state.EtatReservation;

/** Aggregate root for a reservation and its simulated payment. */
public class Reservation {

    private final String numero;
    private final ZonedDateTime date;
    private final double prix;
    private EtatReservation etat;
    private final Client client;
    private final Passager passager;
    private final Vol vol;
    private final Paiement paiement;

    Reservation(String numero, double prix, Client client, Passager passager, Vol vol) {
        if (numero == null || numero.isBlank()) {
            throw new IllegalArgumentException("Reservation reference is required");
        }
        if (!Double.isFinite(prix) || prix < 0) {
            throw new IllegalArgumentException("Reservation price must be finite and nonnegative");
        }
        this.numero = numero;
        this.date = ZonedDateTime.now();
        this.prix = prix;
        this.client = Objects.requireNonNull(client, "client");
        this.passager = Objects.requireNonNull(passager, "passager");
        this.vol = Objects.requireNonNull(vol, "vol");
        this.etat = new EnAttente();
        this.paiement = new Paiement(prix);
        client.addReservation(this);
    }

    public void confirmer() {
        etat.confirmer(this);
    }

    /** Preserve the existing API; payment coordination lives in its own service. */
    public void payer() {
        ReservationPaymentService.pay(this);
    }

    public void annuler() {
        etat.annuler(this);
    }

    // Kept for backwards compatibility with existing tests/callers.
    // The application service now uses Paiement rather than these shortcuts.
    public void debiter() {
        paiement.debiter();
    }

    public void rembourser() {
        paiement.rembourser();
    }

    public String getNumero() { return numero; }
    public ZonedDateTime getDate() { return date; }
    public double getPrix() { return prix; }
    public Client getClient() { return client; }
    public Passager getPassager() { return passager; }
    public EtatReservation getEtat() { return etat; }
    public Vol getVol() { return vol; }
    public Paiement getPaiement() { return paiement; }

    /**
     * Legacy state-pattern integration point.
     * TODO: replace the public setter with controlled transitions; currently callers
     * can bypass lifecycle rules, so do not expose this domain to untrusted clients.
     */
    public void setEtat(EtatReservation nouvelEtat) {
        this.etat = Objects.requireNonNull(nouvelEtat, "nouvelEtat");
    }

    @Override
    public String toString() {
        return "Reservation{" + numero + ", " + etat.libelle() + ", " + prix + "}";
    }
}
