package org.uca.aeroport;

import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

public class EtapeTest {

    // Petite classe concrète pour tester Etape
    private static class TestEtape extends Etape {
        public TestEtape(Date depart, Date arrivee) {
            super(depart, arrivee);
        }
    }

    @Test
    public void getDureeRetourneZeroSiDatesNulles() {
        Etape e1 = new TestEtape(null, null);
        assertEquals(Duration.ZERO, e1.getDuree());

        Etape e2 = new TestEtape(new Date(), null);
        assertEquals(Duration.ZERO, e2.getDuree());

        Etape e3 = new TestEtape(null, new Date());
        assertEquals(Duration.ZERO, e3.getDuree());
    }

    @Test
    public void getDureeCalculeDifferenceEntreDepartEtArrivee() {
        Date depart = new Date(1_000_000L);
        Date arrivee = new Date(1_000_000L + 3_600_000L + 120_000L); // +1h2min

        Etape e = new TestEtape(depart, arrivee);

        assertEquals(Duration.ofHours(1).plusMinutes(2), e.getDuree());
    }
}

