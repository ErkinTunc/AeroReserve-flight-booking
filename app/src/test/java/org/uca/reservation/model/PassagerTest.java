package org.uca.reservation.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Vérifie la création et la validation des passagers.
 */
public class PassagerTest {

    // ------------------ Tests de reussite ------------------

    @Test
    @DisplayName("1.1 Reussite : le constructeur initialise les champs")
    public void constructeurDoitInitialiserLesChamps() {
        Passager passager = new Passager("Dupont", "Alice", 30, "AB123456");

        assertEquals("Dupont", passager.getNom());
        assertEquals("Alice", passager.getPrenom());
        assertEquals(30, passager.getAge());
        assertEquals("AB123456", passager.getNumeroPasseport());
    }

    @Test
    @DisplayName("1.2 Reussite : setTelephone modifie le telephone")
    public void setTelephoneDoitModifierLeTelephone() {
        Passager passager = new Passager("Dupont", "Alice", 30, "AB123456");

        passager.setTelephone("0601020304");

        assertEquals("0601020304", passager.getTelephone());
    }

    // ------------------ Tests d'invalidite ------------------

    @Test
    @DisplayName("2.1 Invalidite : le constructeur refuse un nom null")
    public void constructeurDoitRefuserNomNull() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new Passager(null, "Alice", 30, "AB123456"));
    }

    @Test
    @DisplayName("2.2 Invalidite : le constructeur refuse un nom vide")
    public void constructeurDoitRefuserNomVide() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new Passager("   ", "Alice", 30, "AB123456"));
    }

    @Test
    @DisplayName("2.3 Invalidite : le constructeur refuse un prenom null")
    public void constructeurDoitRefuserPrenomNull() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new Passager("Dupont", null, 30, "AB123456"));
    }

    @Test
    @DisplayName("2.4 Invalidite : le constructeur refuse un prenom vide")
    public void constructeurDoitRefuserPrenomVide() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new Passager("Dupont", "   ", 30, "AB123456"));
    }

    @Test
    @DisplayName("2.5 Invalidite : le constructeur refuse un age negatif")
    public void constructeurDoitRefuserAgeNegatif() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new Passager("Dupont", "Alice", -1, "AB123456"));
    }

    @Test
    @DisplayName("2.6 Invalidite : le constructeur refuse un numero de passeport null")
    public void constructeurDoitRefuserNumeroPasseportNull() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new Passager("Dupont", "Alice", 30, null));
    }

    @Test
    @DisplayName("2.7 Invalidite : le constructeur refuse un numero de passeport vide")
    public void constructeurDoitRefuserNumeroPasseportVide() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new Passager("Dupont", "Alice", 30, "   "));
    }

    @Test
    @DisplayName("2.8 Invalidite : setTelephone refuse un telephone null")
    public void setTelephoneDoitRefuserTelephoneNull() {
        Passager passager = new Passager("Dupont", "Alice", 30, "AB123456");

        assertThrows(
                IllegalArgumentException.class,
                () -> passager.setTelephone(null));
    }

    @Test
    @DisplayName("2.9 Invalidite : setTelephone refuse un telephone vide")
    public void setTelephoneDoitRefuserTelephoneVide() {
        Passager passager = new Passager("Dupont", "Alice", 30, "AB123456");

        assertThrows(
                IllegalArgumentException.class,
                () -> passager.setTelephone("   "));
    }

}