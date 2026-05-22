package org.uca.aeroport;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

public class EtapeTest {

    // --------------- Helper functions and classes ---------------

    // Petite classe concrète pour tester Etape
    private static class TestEtape extends Etape {
        public TestEtape(Date depart, Date arrivee) {
            super(depart, arrivee);
        }
    }

    // ------------------ Test de Reussite ------------------

    @Test
    @DisplayName("Reussite : getDuree retourne zero si une date est absente")
    public void getDureeRetourneZeroSiDatesNulles() {
        Etape e1 = new TestEtape(null, null);
        assertEquals(Duration.ZERO, e1.getDuree());

        Etape e2 = new TestEtape(new Date(), null);
        assertEquals(Duration.ZERO, e2.getDuree());

        Etape e3 = new TestEtape(null, new Date());
        assertEquals(Duration.ZERO, e3.getDuree());
    }

    @Test
    @DisplayName("Reussite : getDuree calcule la difference entre depart et arrivee")
    public void getDureeCalculeDifferenceEntreDepartEtArrivee() {
        Date depart = new Date(1_000_000L);
        Date arrivee = new Date(1_000_000L + 3_600_000L + 120_000L);

        Etape e = new TestEtape(depart, arrivee);

        assertEquals(Duration.ofHours(1).plusMinutes(2), e.getDuree());
    }

    // ------------------ Test d'Invalidite ------------------

    @Test
    @DisplayName("Invalidite : depart ne peut pas etre apres arrivee")
    public void departNePeutPasEtreApresArrivee() {
        Date depart = new Date(2_000_000L);
        Date arrivee = new Date(1_000_000L);

        assertThrows(
                IllegalArgumentException.class,
                () -> new TestEtape(depart, arrivee));
    }

    // === Setters Tests ===
    @Test
    @DisplayName("Invalidite : setDepart refuse une date apres arrivee")
    public void setDepartRefuseDateApresArrivee() {
        Etape e = new TestEtape(
                new Date(1_000_000L),
                new Date(2_000_000L));

        assertThrows(
                IllegalArgumentException.class,
                () -> e.setDepart(new Date(3_000_000L)));
    }

    @Test
    @DisplayName("Invalidite : setArrivee refuse une date avant depart")
    public void setArriveeRefuseDateAvantDepart() {
        Etape e = new TestEtape(
                new Date(1_000_000L),
                new Date(2_000_000L));

        assertThrows(
                IllegalArgumentException.class,
                () -> e.setArrivee(new Date(500_000L)));
    }

}
