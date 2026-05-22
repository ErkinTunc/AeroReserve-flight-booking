package org.uca.reservation;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TarifBusinessTest {

    @Test
    public void calculerDoitMajorerd50Pourcents() {
        PolitiqueTarif tarif = new TarifBusiness();

        assertEquals(150.0, tarif.calculer(100.0), 0.0001);
        assertEquals(0.0, tarif.calculer(0.0), 0.0001);
        assertEquals(225.0, tarif.calculer(150.0), 0.0001);
    }
}
