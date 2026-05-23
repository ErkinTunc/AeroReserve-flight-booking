package org.uca.reservation.pricing;

/**
 * Business pricing: +50% on top of the base price.
 */
public class TarifBusiness implements PolitiqueTarif {

    @Override
    public double calculer(double basePrice) {
        return basePrice * 1.5;
    }
}