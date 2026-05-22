package org.uca.reservation;

/*
    La réservation a été payée — état terminal (sauf remboursement futur).
 */

public class Payee implements EtatReservation {

    @Override
    public void confirmer(Reservation reservation) {
        throw new TransitionInterditeException("La réservation est déjà payée.");
    }

    @Override
    public void payer(Reservation reservation) {
        throw new TransitionInterditeException("La réservation est déjà payée.");
    }

    @Override
    public void annuler(Reservation reservation) {

        // On pourrait ici déclencher un remboursement
        
        throw new TransitionInterditeException("Contactez le service client pour annuler une réservation payée.");
    }

    @Override
    public String libelle() {
        return "PAYEE";
    }
}

