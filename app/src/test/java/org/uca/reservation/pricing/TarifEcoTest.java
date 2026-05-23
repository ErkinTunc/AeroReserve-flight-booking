package org.uca.reservation.pricing;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TarifEcoTest {

    @Test
    public void calculerDoitRetournerLePrixDeBase() {
        PolitiqueTarif tarif = new TarifEco();

        assertEquals(100.0, tarif.calculer(100.0), 0.0001);
        assertEquals(0.0, tarif.calculer(0.0), 0.0001);
        assertEquals(50.5, tarif.calculer(50.5), 0.0001);
    }
}
