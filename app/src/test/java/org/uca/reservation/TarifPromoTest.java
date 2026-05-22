package org.uca.reservation;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TarifPromoTest {

    @Test
    public void calculerDoitAppliquerUneRemiseDe20Pourcents() {
        PolitiqueTarif tarif = new TarifPromo();

        assertEquals(80.0, tarif.calculer(100.0), 0.0001);
        assertEquals(0.0, tarif.calculer(0.0), 0.0001);
        assertEquals(40.0, tarif.calculer(50.0), 0.0001);
    }
}
