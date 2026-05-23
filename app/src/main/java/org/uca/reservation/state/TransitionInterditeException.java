package org.uca.reservation.state;

import org.uca.reservation.model.Reservation;

/*
   Levée quand une transition d'état est invalide.
 */

public class TransitionInterditeException extends RuntimeException {

    public TransitionInterditeException(String message) {
        super(message);
    }
}