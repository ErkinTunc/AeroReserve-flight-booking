package org.uca.reservation.pricing;

/**
 * Pricing strategy for reservations (Strategy pattern).
 *
 * Each implementation applies a different pricing rule
 * on top of the base price (eco, business, promo, etc.).
 */
public interface PolitiqueTarif {

    /*
     * Computes the final price from a base price.
     *
      @param basePrice base price of the reservation
      @return final price after applying this policy
     */
    
    double calculer(double basePrice);
}