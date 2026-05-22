package org.uca.reservation;

/**
    État initial d'une réservation.
     Transitions autorisées : confirmer, annuler.
     Le siège est bloqué pendant un certains temps lorsque le client fait la réservation
 */
public class EnAttente implements EtatReservation {

    @Override
    public void confirmer(Reservation reservation) {
        reservation.setEtat(new Confirmee());
    }

    @Override
    public void payer(Reservation reservation) {
        throw new TransitionInterditeException("Impossible de payer une réservation non confirmée.");
    }

    @Override
    public void annuler(Reservation reservation) {
        reservation.setEtat(new Annulee());
    }

    @Override
    public String libelle() {
        return "EN_ATTENTE";
    }
}