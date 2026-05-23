package org.uca.reservation.pricing;

/**
 * Promotional pricing: -20% discount on the base price.
 */
public class TarifPromo implements PolitiqueTarif {

    @Override
    public double calculer(double basePrice) {
        return basePrice * 0.8;
    }
}