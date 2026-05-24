package org.uca.aeroport;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Vérifie la création et la validation des villes.
 */
public class VilleTest {

    // ------------------ Tests de reussite ------------------

    @Test
    @DisplayName("1.1 Reussite : le constructeur initialise le nom")
    public void constructeurDoitInitialiserLeNom() {
        Ville ville = new Ville("Paris");

        assertEquals("Paris", ville.getNom());
    }

    @Test
    @DisplayName("1.2 Reussite : setNom modifie le nom")
    public void setNomDoitModifierLeNom() {
        Ville ville = new Ville("Lyon");

        ville.setNom("Marseille");

        assertEquals("Marseille", ville.getNom());
    }

    @Test
    @DisplayName("1.3 Reussite : toString retourne le nom")
    public void toStringRetourneLeNom() {
        Ville ville = new Ville("Nice");

        assertEquals("Nice", ville.toString());
    }

    // ------------------ Tests d'invalidite ------------------

    @Test
    @DisplayName("2.1 Invalidite : le constructeur refuse un nom null")
    public void constructeurDoitRefuserNomNull() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new Ville(null));
    }

    @Test
    @DisplayName("2.2 Invalidite : le constructeur refuse un nom vide")
    public void constructeurDoitRefuserNomVide() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new Ville("   "));
    }

    @Test
    @DisplayName("2.3 Invalidite : setNom refuse un nom null")
    public void setNomDoitRefuserNomNull() {
        Ville ville = new Ville("Paris");

        assertThrows(
                IllegalArgumentException.class,
                () -> ville.setNom(null));
    }

    @Test
    @DisplayName("2.4 Invalidite : setNom refuse un nom vide")
    public void setNomDoitRefuserNomVide() {
        Ville ville = new Ville("Paris");

        assertThrows(
                IllegalArgumentException.class,
                () -> ville.setNom("   "));
    }
}