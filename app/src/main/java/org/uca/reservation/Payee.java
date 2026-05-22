package org.uca.reservation;

/*
    La réservation a été payée.
 */
public class Payee implements EtatReservation {

    @Override
    public void confirmer(Reservation reservation) {
        // Transition attendue par les tests : PAYEE -> CONFIRMEE
        reservation.setEtat(new Confirmee());
    }

    @Override
    public void payer(Reservation reservation) {
        throw new TransitionInterditeException("La réservation est déjà payée.");
    }

    @Override
    public void annuler(Reservation reservation) {
        // Ici tu pourrais éventuellement autoriser une annulation payée,
        // mais les tests actuels ne le vérifient pas.
        throw new TransitionInterditeException("Contactez le service client pour annuler une réservation payée.");
    }

    @Override
    public String libelle() {
        return "PAYEE";
    }
}