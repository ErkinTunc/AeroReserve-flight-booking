package org.uca.reservation.pricing;

/**
 * Economy pricing: keep base price unchanged.
 */
public class TarifEco implements PolitiqueTarif {

    @Override
    public double calculer(double basePrice) {
        return basePrice;
    }
}