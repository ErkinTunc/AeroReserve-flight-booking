package org.uca.aeroport;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class VilleTest {

    @Test
    public void constructeurDoitInitialiserLeNom() {
        Ville ville = new Ville("Paris");
        assertEquals("Paris", ville.getNom());
    }

    @Test
    public void settersEtGettersDoiventFonctionner() {
        Ville ville = new Ville("Lyon");
        ville.setNom("Marseille");
        assertEquals("Marseille", ville.getNom());
    }

    @Test
    public void toStringRetourneLeNom() {
        Ville ville = new Ville("Nice");
        assertEquals("Nice", ville.toString());
    }
}
