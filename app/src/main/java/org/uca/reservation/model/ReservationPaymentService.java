package org.uca.reservation.model;

import java.util.Objects;

import org.uca.reservation.state.TransitionInterditeException;

/** Coordinates simulated payment and reservation state transition. */
final class ReservationPaymentService {

    private ReservationPaymentService() {
    }

    static void pay(Reservation reservation) {
        Objects.requireNonNull(reservation, "reservation");
        if (!reservation.getEtat().peutPayer()) {
            throw new TransitionInterditeException(
                    "Payment is not allowed from state " + reservation.getEtat().libelle());
        }
        // The debit can fail. It must happen BEFORE switching to PAYEE.
        reservation.getPaiement().debiter();
        reservation.getEtat().payer(reservation);
    }
}
