package org.uca.aeroport;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TrajetTest {

    private Aeroport aeroport(String code, String ville) {
        return new Aeroport(code, code, new Ville(ville));
    }

    @Test
    @DisplayName("Reussite : le constructeur initialise le code et les etapes")
    public void constructeurDoitInitialiserLesChamps() {
        EtapeTrajet depart = new EtapeTrajet(
                0,
                aeroport("CDG", "Paris"),
                null,
                Duration.ZERO);

        EtapeTrajet arrivee = new EtapeTrajet(
                1,
                aeroport("LYS", "Lyon"),
                Duration.ofHours(1),
                null);

        Trajet trajet = new Trajet("CDG-LYS", List.of(depart, arrivee));

        assertEquals("CDG-LYS", trajet.getCode());
        assertEquals(2, trajet.getEtapes().size());
        assertTrue(trajet.getEtapes().contains(depart));
        assertTrue(trajet.getEtapes().contains(arrivee));
    }

    @Test
    @DisplayName("Invalidite : le constructeur refuse un code null ou vide")
    public void constructeurDoitRefuserCodeInvalide() {
        EtapeTrajet depart = new EtapeTrajet(
                0,
                aeroport("CDG", "Paris"),
                null,
                Duration.ZERO);

        EtapeTrajet arrivee = new EtapeTrajet(
                1,
                aeroport("LYS", "Lyon"),
                Duration.ofHours(1),
                null);

        assertThrows(IllegalArgumentException.class,
                () -> new Trajet(null, List.of(depart, arrivee)));

        assertThrows(IllegalArgumentException.class,
                () -> new Trajet("   ", List.of(depart, arrivee)));
    }

    @Test
    @DisplayName("Invalidite : un trajet doit contenir au moins deux etapes")
    public void constructeurDoitRefuserMoinsDeDeuxEtapes() {
        EtapeTrajet depart = new EtapeTrajet(
                0,
                aeroport("CDG", "Paris"),
                null,
                Duration.ZERO);

        assertThrows(IllegalArgumentException.class,
                () -> new Trajet("CDG", null));

        assertThrows(IllegalArgumentException.class,
                () -> new Trajet("CDG", List.of(depart)));
    }

    @Test
    @DisplayName("Validite : getEtapes retourne une liste non modifiable")
    public void getEtapesRetourneListeNonModifiable() {
        EtapeTrajet depart = new EtapeTrajet(
                0,
                aeroport("CDG", "Paris"),
                null,
                Duration.ZERO);

        EtapeTrajet arrivee = new EtapeTrajet(
                1,
                aeroport("LYS", "Lyon"),
                Duration.ofHours(1),
                null);

        Trajet trajet = new Trajet("CDG-LYS", List.of(depart, arrivee));

        assertThrows(UnsupportedOperationException.class,
                () -> trajet.getEtapes().add(depart));
    }
}