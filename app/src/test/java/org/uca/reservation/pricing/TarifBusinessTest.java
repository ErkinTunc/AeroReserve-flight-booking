package org.uca.reservation.pricing;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Vérifie le calcul du tarif business.
 */
public class TarifBusinessTest {

    // ------------------ Tests de reussite ------------------

    @Test
    @DisplayName("1.1 Reussite : calculer majore le prix de 50 pourcents")
    public void calculerDoitMajorerDe50Pourcents() {
        PolitiqueTarif tarif = new TarifBusiness();

        assertEquals(150.0, tarif.calculer(100.0), 0.0001);
        assertEquals(0.0, tarif.calculer(0.0), 0.0001);
        assertEquals(225.0, tarif.calculer(150.0), 0.0001);
    }
}