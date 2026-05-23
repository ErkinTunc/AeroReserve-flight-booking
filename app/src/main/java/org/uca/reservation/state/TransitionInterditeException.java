package org.uca.reservation.state;

/*
   Levée quand une transition d'état est invalide.
 */

public class TransitionInterditeException extends RuntimeException {

    public TransitionInterditeException(String message) {
        super(message);
    }
}