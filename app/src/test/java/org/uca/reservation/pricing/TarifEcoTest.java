package org.uca.reservation.pricing;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Vérifie le calcul du tarif eco.
 */
public class TarifEcoTest {

    // ------------------ Tests de reussite ------------------

    @Test
    @DisplayName("1.1 Reussite : calculer retourne le prix de base")
    public void calculerDoitRetournerLePrixDeBase() {
        PolitiqueTarif tarif = new TarifEco();

        assertEquals(100.0, tarif.calculer(100.0), 0.0001);
        assertEquals(0.0, tarif.calculer(0.0), 0.0001);
        assertEquals(50.5, tarif.calculer(50.5), 0.0001);
    }
}