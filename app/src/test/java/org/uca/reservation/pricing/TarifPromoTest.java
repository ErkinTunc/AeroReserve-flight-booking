package org.uca.reservation.pricing;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Vérifie le calcul du tarif promo.
 */
public class TarifPromoTest {

    // ------------------ Tests de reussite ------------------

    @Test
    @DisplayName("1.1 Reussite : calculer applique une remise de 20 pourcents")
    public void calculerDoitAppliquerUneRemiseDe20Pourcents() {
        PolitiqueTarif tarif = new TarifPromo();

        assertEquals(80.0, tarif.calculer(100.0), 0.0001);
        assertEquals(0.0, tarif.calculer(0.0), 0.0001);
        assertEquals(40.0, tarif.calculer(50.0), 0.0001);
    }
}