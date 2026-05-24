package org.uca.reservation.state;

import org.uca.reservation.model.Reservation;

/**
 * La réservation a été confirmée.
 * État final côté client : aucune modification directe possible.
 */
public class Confirmee implements EtatReservation {

    @Override
    public void confirmer(Reservation reservation) {
        throw new TransitionInterditeException("La réservation est déjà confirmée.");
    }

    @Override
    public void payer(Reservation reservation) {
        throw new TransitionInterditeException("La réservation est déjà confirmée.");
    }

    @Override
    public void annuler(Reservation reservation) {
        throw new TransitionInterditeException("Impossible d'annuler une réservation confirmée.");
    }

    @Override
    public String libelle() {
        return "CONFIRMEE";
    }
}
