package org.uca.reservation.model;

import org.uca.reservation.pricing.PolitiqueTarif;
import org.uca.reservation.pricing.TarifEco;

import org.uca.aeroport.Vol;

public class ReservationFactory {

    // Crée une réservation avec une politique par défaut (eco)
    public Reservation creer(double basePrice, Client client, Passager passager, Vol vol) {
        PolitiqueTarif politique = new TarifEco(); // stratégie par défaut
        double finalPrice = politique.calculer(basePrice);
        return new Reservation(generateNumero(), finalPrice, client, passager, vol);
    }

    // Surcharge : création avec une politique de tarif explicite
    public Reservation creer(double basePrice, PolitiqueTarif politique,
            Client client, Passager passager, Vol vol) {
        double finalPrice = politique.calculer(basePrice);
        return new Reservation(generateNumero(), finalPrice, client, passager, vol);
    }

    // Génération simple de numéros (à adapter à ton implémentation actuelle)
    private static int compteur = 1;

    private String generateNumero() {
        return "RES-" + (compteur++);
    }
}