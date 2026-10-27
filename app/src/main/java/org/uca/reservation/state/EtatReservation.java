package org.uca.reservation.state;

import org.uca.reservation.model.Reservation;

/** State pattern: each state decides which transitions are permitted. */
public interface EtatReservation {

    void confirmer(Reservation reservation);
    void payer(Reservation reservation);
    void annuler(Reservation reservation);
    String libelle();

    /** Query used by the payment coordinator before attempting the debit. */
    default boolean peutPayer() {
        return false;
    }
}
