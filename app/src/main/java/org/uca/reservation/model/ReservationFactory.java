package org.uca.reservation.model;

import org.uca.reservation.pricing.PolitiqueTarif;
import org.uca.reservation.pricing.TarifEco;

import org.uca.aeroport.Vol;

public class ReservationFactory {

    // Génération simple de numéros (à adapter à ton implémentation actuelle)
    private static int compteur = 1;

    // -------------------- Factory methods --------------------

    // Crée une réservation avec une politique par défaut (eco)
    public Reservation creer(double basePrice, Client client, Passager passager, Vol vol) {
        return creer(basePrice, new TarifEco(), client, passager, vol);
    }

    // Surcharge : création avec une politique de tarif explicite
    public Reservation creer(double basePrice, PolitiqueTarif politique,
            Client client, Passager passager, Vol vol) {

        if (basePrice < 0) {
            throw new IllegalArgumentException("Le prix de base ne peut pas être négatif");
        }

        if (politique == null) {
            throw new IllegalArgumentException("La politique tarifaire est obligatoire");
        }

        double finalPrice = politique.calculer(basePrice);
        return new Reservation(generateNumero(), finalPrice, client, passager, vol);
    }

    // --------------------- Helper methods ----------------

    private String generateNumero() {
        return "RES-" + (compteur++);
    }
}