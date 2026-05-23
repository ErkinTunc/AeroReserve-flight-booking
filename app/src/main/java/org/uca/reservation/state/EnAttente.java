package org.uca.reservation.state;

import org.uca.reservation.model.Reservation;

/**
 * État initial d'une réservation.
 * Transitions attendues par les tests :
 * - payer() -> PAYEE
 * - annuler() -> ANNULEE
 * - confirmer() depuis EN_ATTENTE : interdit
 */
public class EnAttente implements EtatReservation {

    @Override
    public void confirmer(Reservation reservation) {
        // On ne peut pas confirmer directement une réservation en attente
        throw new TransitionInterditeException("Impossible de confirmer une réservation non payée.");
    }

    @Override
    public void payer(Reservation reservation) {
        // Transition attendue : EN_ATTENTE -> PAYEE
        reservation.setEtat(new Payee());
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