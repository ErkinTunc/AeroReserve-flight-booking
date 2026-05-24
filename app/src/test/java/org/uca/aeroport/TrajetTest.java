package org.uca.aeroport;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TrajetTest {

    private Aeroport aeroport(String code, String ville) {
        return new Aeroport(code, code, new Ville(ville));
    }

    @Test
    @DisplayName("Reussite : le constructeur initialise le code et les etapes")
    public void constructeurDoitInitialiserLesChamps() {
        Escale depart = new Escale(new Date(0), new Date(0), aeroport("CDG", "Paris"));
        Escale arrivee = new Escale(new Date(1000), new Date(1000), aeroport("LYS", "Lyon"));

        Trajet trajet = new Trajet("CDG-LYS", List.of(depart, arrivee));

        assertEquals("CDG-LYS", trajet.getCode());
        assertEquals(2, trajet.getEtapes().size());
        assertTrue(trajet.getEtapes().contains(depart));
        assertTrue(trajet.getEtapes().contains(arrivee));
    }

    @Test
    @DisplayName("Invalidite : le constructeur refuse un code null ou vide")
    public void constructeurDoitRefuserCodeInvalide() {
        Escale depart = new Escale(new Date(0), new Date(0), aeroport("CDG", "Paris"));
        Escale arrivee = new Escale(new Date(1000), new Date(1000), aeroport("LYS", "Lyon"));

        assertThrows(IllegalArgumentException.class,
                () -> new Trajet(null, List.of(depart, arrivee)));

        assertThrows(IllegalArgumentException.class,
                () -> new Trajet("   ", List.of(depart, arrivee)));
    }

    @Test
    @DisplayName("Invalidite : un trajet doit contenir au moins deux etapes")
    public void constructeurDoitRefuserMoinsDeDeuxEtapes() {
        Escale depart = new Escale(new Date(0), new Date(0), aeroport("CDG", "Paris"));

        assertThrows(IllegalArgumentException.class,
                () -> new Trajet("CDG", null));

        assertThrows(IllegalArgumentException.class,
                () -> new Trajet("CDG", List.of(depart)));
    }

    @Test
    @DisplayName("Validite : getEtapes retourne une liste non modifiable")
    public void getEtapesRetourneListeNonModifiable() {
        Escale depart = new Escale(new Date(0), new Date(0), aeroport("CDG", "Paris"));
        Escale arrivee = new Escale(new Date(1000), new Date(1000), aeroport("LYS", "Lyon"));

        Trajet trajet = new Trajet("CDG-LYS", List.of(depart, arrivee));

        assertThrows(UnsupportedOperationException.class,
                () -> trajet.getEtapes().add(depart));
    }
}