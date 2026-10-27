package org.uca.reservation.state;

import org.uca.reservation.model.Reservation;

public class EnAttente implements EtatReservation {

    @Override
    public void confirmer(Reservation reservation) {
        throw new TransitionInterditeException("Cannot confirm before payment.");
    }

    @Override
    public boolean peutPayer() {
        return true;
    }

    @Override
    public void payer(Reservation reservation) {
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
