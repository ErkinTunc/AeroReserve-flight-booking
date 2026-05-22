package org.uca.reservation;

public class ReservationFactory {

    // Crée une réservation avec une politique par défaut (eco)
    public Reservation creer(double basePrice, Client client, Passager passager) {
        PolitiqueTarif politique = new TarifEco(); // stratégie par défaut
        double finalPrice = politique.calculer(basePrice);
        return new Reservation(generateNumero(), finalPrice, client, passager);
    }

    // Surcharge : création avec une politique de tarif explicite
    public Reservation creer(double basePrice, PolitiqueTarif politique,
                             Client client, Passager passager) {
        double finalPrice = politique.calculer(basePrice);
        return new Reservation(generateNumero(), finalPrice, client, passager);
    }

    // Génération simple de numéros (à adapter à ton implémentation actuelle)
    private static int compteur = 1;

    private String generateNumero() {
        return "RES-" + (compteur++);
    }
}