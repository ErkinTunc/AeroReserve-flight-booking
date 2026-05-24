package org.uca.aeroport;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

public class EtapeTrajetTest {

    private Aeroport aeroport() {
        return new Aeroport("CDG", "Charles de Gaulle", new Ville("Paris"));
    }

    // ------------------ Tests de reussite ------------------

    @Test
    @DisplayName("1.1 Reussite : le constructeur initialise les champs")
    public void constructeurDoitInitialiserLesChamps() {
        Aeroport aeroport = aeroport();

        EtapeTrajet etape = new EtapeTrajet(
                1,
                aeroport,
                Duration.ofHours(1),
                Duration.ofHours(2));

        assertEquals(1, etape.getOrdre());
        assertEquals(aeroport, etape.getAeroport());
        assertEquals(Duration.ofHours(1), etape.getDecalageArrivee());
        assertEquals(Duration.ofHours(2), etape.getDecalageDepart());
    }

    @Test
    @DisplayName("1.2 Reussite : getDureeArret calcule la difference entre depart et arrivee")
    public void getDureeArretDoitCalculerLaDifferenceEntreDepartEtArrivee() {
        EtapeTrajet etape = new EtapeTrajet(
                1,
                aeroport(),
                Duration.ofHours(1),
                Duration.ofHours(1).plusMinutes(30));

        assertEquals(Duration.ofMinutes(30), etape.getDureeArret());
    }

    @Test
    @DisplayName("1.3 Reussite : getDureeArret retourne zero si un decalage manque")
    public void getDureeArretRetourneZeroSiUnDecalageManque() {
        EtapeTrajet depart = new EtapeTrajet(
                0,
                aeroport(),
                null,
                Duration.ZERO);

        EtapeTrajet arrivee = new EtapeTrajet(
                1,
                aeroport(),
                Duration.ofHours(2),
                null);

        assertEquals(Duration.ZERO, depart.getDureeArret());
        assertEquals(Duration.ZERO, arrivee.getDureeArret());
    }

    // ------------------ Tests d'invalidite ------------------

    @Test
    @DisplayName("2.1 Invalidite : le constructeur refuse un ordre negatif")
    public void constructeurDoitRefuserOrdreNegatif() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new EtapeTrajet(
                        -1,
                        aeroport(),
                        Duration.ofHours(1),
                        Duration.ofHours(2)));
    }

    @Test
    @DisplayName("2.2 Invalidite : le constructeur refuse un aeroport null")
    public void constructeurDoitRefuserAeroportNull() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new EtapeTrajet(
                        1,
                        null,
                        Duration.ofHours(1),
                        Duration.ofHours(2)));
    }

    @Test
    @DisplayName("2.3 Invalidite : le constructeur refuse un decalage d'arrivee negatif")
    public void constructeurDoitRefuserDecalageArriveeNegatif() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new EtapeTrajet(
                        1,
                        aeroport(),
                        Duration.ofMinutes(-1),
                        Duration.ofHours(2)));
    }

    @Test
    @DisplayName("2.4 Invalidite : le constructeur refuse un decalage de depart negatif")
    public void constructeurDoitRefuserDecalageDepartNegatif() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new EtapeTrajet(
                        1,
                        aeroport(),
                        Duration.ofHours(1),
                        Duration.ofMinutes(-1)));
    }

    @Test
    @DisplayName("2.5 Invalidite : le depart relatif ne peut pas etre avant l'arrivee relative")
    public void constructeurDoitRefuserDepartAvantArrivee() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new EtapeTrajet(
                        1,
                        aeroport(),
                        Duration.ofHours(2),
                        Duration.ofHours(1)));
    }
}