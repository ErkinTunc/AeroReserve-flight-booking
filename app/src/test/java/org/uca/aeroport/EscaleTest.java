package org.uca.aeroport;

import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

public class EscaleTest {

    @Test
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
    public void setAeroportDoitChangerLAeroport() {
        Escale escale = new Escale(new Date(), new Date(),
                new Aeroport("CDG", "Charles de Gaulle", new Ville("Paris")));

        Aeroport autre = new Aeroport("IST", "Istanbul Airport", new Ville("Istanbul"));
        escale.setAeroport(autre);

        assertEquals(autre, escale.getAeroport());
    }
}

