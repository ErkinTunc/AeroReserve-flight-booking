package org.uca.reservation;

/*
  État terminal — aucune transition possible.
 */

public class Annulee implements EtatReservation {

    @Override
    public void confirmer(Reservation reservation) {
        throw new TransitionInterditeException("Impossible de confirmer une réservation annulée.");
    }

    @Override
    public void payer(Reservation reservation) {
        throw new TransitionInterditeException("Impossible de payer une réservation annulée.");
    }

    @Override
    public void annuler(Reservation reservation) {
        throw new TransitionInterditeException("La réservation est déjà annulée.");
    }

    @Override
    public String libelle() {
        return "ANNULEE";
    }
}

