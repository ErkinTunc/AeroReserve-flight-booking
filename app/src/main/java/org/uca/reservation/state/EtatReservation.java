package org.uca.reservation.state;

import org.uca.reservation.model.Reservation;

/*
     Interface State — chaque état concret gère ses propres transitions
   et lève une exception si la transition est interdite.

 */

public interface EtatReservation {

    void confirmer(Reservation reservation);
    void payer(Reservation reservation);
    void annuler(Reservation reservation);

    // Libellé lisible (pour logs, affichage)
    String libelle();
}

