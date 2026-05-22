package org.uca.reservation;

/*
  La réservation a été confirmée.
  Transitions autorisées : payer, annuler.
 */
public class Confirmee implements EtatReservation {

    @Override
    public void confirmer(Reservation reservation) {
        throw new TransitionInterditeException("La réservation est déjà confirmée.");
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
        return "CONFIRMEE";
    }
}
