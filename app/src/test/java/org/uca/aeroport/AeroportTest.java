package org.uca.aeroport;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Vérifie la création et la validation des aéroports.
 */
public class AeroportTest {

    // ------------------ Tests de reussite ------------------

    @Test
    @DisplayName("1.1 Reussite : le constructeur initialise les champs")
    public void constructeurDoitInitialiserLesChamps() {
        Ville ville = new Ville("Paris");
        Aeroport aeroport = new Aeroport("CDG", "Charles de Gaulle", ville);

        assertEquals("CDG", aeroport.getCode());
        assertEquals("Charles de Gaulle", aeroport.getNom());
        assertEquals(ville, aeroport.getVille());
    }

    @Test
    @DisplayName("1.2 Reussite : les setters modifient les champs")
    public void settersDoiventModifierLesChamps() {
        Aeroport aeroport = new Aeroport();

        Ville ville = new Ville("Istanbul");

        aeroport.setCode("IST");
        aeroport.setNom("Istanbul Airport");
        aeroport.setVille(ville);

        assertEquals("IST", aeroport.getCode());
        assertEquals("Istanbul Airport", aeroport.getNom());
        assertEquals(ville, aeroport.getVille());
    }

    @Test
    @DisplayName("1.3 Reussite : toString contient le code, le nom et la ville")
    public void toStringDoitContenirCodeNomEtVille() {
        Ville ville = new Ville("Paris");
        Aeroport aeroport = new Aeroport("CDG", "Charles de Gaulle", ville);

        String s = aeroport.toString();

        assertTrue(s.contains("CDG"));
        assertTrue(s.contains("Charles de Gaulle"));
        assertTrue(s.contains("Paris"));
    }

    // ------------------ Tests d'invalidite ------------------

    @Test
    @DisplayName("2.1 Invalidite : le constructeur refuse un code null")
    public void constructeurDoitRefuserCodeNull() {
        Ville ville = new Ville("Paris");

        assertThrows(
                IllegalArgumentException.class,
                () -> new Aeroport(null, "Charles de Gaulle", ville));
    }

    @Test
    @DisplayName("2.2 Invalidite : le constructeur refuse un code vide")
    public void constructeurDoitRefuserCodeVide() {
        Ville ville = new Ville("Paris");

        assertThrows(
                IllegalArgumentException.class,
                () -> new Aeroport("   ", "Charles de Gaulle", ville));
    }

    @Test
    @DisplayName("2.3 Invalidite : le constructeur refuse un nom null")
    public void constructeurDoitRefuserNomNull() {
        Ville ville = new Ville("Paris");

        assertThrows(
                IllegalArgumentException.class,
                () -> new Aeroport("CDG", null, ville));
    }

    @Test
    @DisplayName("2.4 Invalidite : le constructeur refuse un nom vide")
    public void constructeurDoitRefuserNomVide() {
        Ville ville = new Ville("Paris");

        assertThrows(
                IllegalArgumentException.class,
                () -> new Aeroport("CDG", "   ", ville));
    }

    @Test
    @DisplayName("2.5 Invalidite : le constructeur refuse une ville null")
    public void constructeurDoitRefuserVilleNull() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new Aeroport("CDG", "Charles de Gaulle", null));
    }

    @Test
    @DisplayName("2.6 Invalidite : setCode refuse un code null")
    public void setCodeDoitRefuserCodeNull() {
        Aeroport aeroport = new Aeroport();

        assertThrows(
                IllegalArgumentException.class,
                () -> aeroport.setCode(null));
    }

    @Test
    @DisplayName("2.7 Invalidite : setCode refuse un code vide")
    public void setCodeDoitRefuserCodeVide() {
        Aeroport aeroport = new Aeroport();

        assertThrows(
                IllegalArgumentException.class,
                () -> aeroport.setCode("   "));
    }

    @Test
    @DisplayName("2.8 Invalidite : setNom refuse un nom null")
    public void setNomDoitRefuserNomNull() {
        Aeroport aeroport = new Aeroport();

        assertThrows(
                IllegalArgumentException.class,
                () -> aeroport.setNom(null));
    }

    @Test
    @DisplayName("2.9 Invalidite : setNom refuse un nom vide")
    public void setNomDoitRefuserNomVide() {
        Aeroport aeroport = new Aeroport();

        assertThrows(
                IllegalArgumentException.class,
                () -> aeroport.setNom("   "));
    }

    @Test
    @DisplayName("2.10 Invalidite : setVille refuse une ville null")
    public void setVilleDoitRefuserVilleNull() {
        Aeroport aeroport = new Aeroport();

        assertThrows(
                IllegalArgumentException.class,
                () -> aeroport.setVille(null));
    }
}