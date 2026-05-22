package org.uca.reservation;

import java.util.UUID;

/**
    Factory Method — point d'entrée unique pour créer des réservations.
 
    Avantages :
    - génère le numéro automatiquement
    - garantit que toute Reservation démarre en EnAttente
    - facilite les tests (on peut sous-classer la factory)
 */
public class ReservationFactory {

    /**
     * Crée une réservation standard avec un numéro auto-généré.
     */
    public Reservation creer(Money prix, Client client, Passager passager) {
        String numero = genererNumero();
        return new Reservation(numero, prix, client, passager);
    }

    /*
        Crée une réservation avec un numéro explicite (import, tests...).
     */
    public Reservation creerAvecNumero(String numero, Money prix,
                                       Client client, Passager passager) {
        return new Reservation(numero, prix, client, passager);
    }

    //  Génération du numéro — surchargeable dans une sous-classe. 
    protected String genererNumero() {
        return "RES-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}