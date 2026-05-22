package org.uca.aeroport;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

public class EscaleTest {

    // ------------------ Test de Reussite ------------------
    @Test
    @DisplayName("Reussite : le constructeur initialise les champs de l'escale")
    public void constructeurDoitInitialiserLesChamps() {
        Date depart = new Date(1_000_000L);
        Date arrivee = new Date(2_000_000L);
        Aeroport aeroport = new Aeroport("CDG", "Charles de Gaulle", new Ville("Paris"));

        Escale escale = new Escale(depart, arrivee, aeroport);

        assertEquals(depart, escale.getDepart());
        assertEquals(arrivee, escale.getArrivee());
        assertEquals(aeroport, escale.getAeroport());
    }

    @Test
    @DisplayName("Reussite : setAeroport change l'aeroport de l'escale")
    public void setAeroportDoitChangerLAeroport() {
        Escale escale = new Escale(
                new Date(1_000_000L),
                new Date(2_000_000L),
                new Aeroport("CDG", "Charles de Gaulle", new Ville("Paris")));

        Aeroport autre = new Aeroport("IST", "Istanbul Airport", new Ville("Istanbul"));

        escale.setAeroport(autre);

        assertEquals(autre, escale.getAeroport());
    }

    @Test
    @DisplayName("Reussite : getDuree retourne la duree de l'escale")
    public void getDureeRetourneDureeEscale() {
        Aeroport aeroport = new Aeroport("IST", "Istanbul Airport", new Ville("Istanbul"));

        Escale escale = new Escale(
                new Date(1_000_000L),
                new Date(1_000_000L + 3_600_000L),
                aeroport);

        assertEquals(Duration.ofHours(1), escale.getDuree());
    }

    // ------------------ Test d'Invalidite ------------------
    @Test
    @DisplayName("Invalidite : une escale doit avoir un aeroport")
    public void escaleDoitAvoirUnAeroport() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new Escale(
                        new Date(1_000_000L),
                        new Date(2_000_000L),
                        null));
    }

    @Test
    @DisplayName("Invalidite : setAeroport refuse un aeroport null")
    public void setAeroportRefuseAeroportNull() {
        Escale escale = new Escale(
                new Date(1_000_000L),
                new Date(2_000_000L),
                new Aeroport("CDG", "Charles de Gaulle", new Ville("Paris")));

        assertThrows(
                IllegalArgumentException.class,
                () -> escale.setAeroport(null));
    }

}
