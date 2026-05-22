package org.uca.aeroport;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AeroportTest {

    @Test
    public void constructeurDoitInitialiserLesChamps() {
        Ville ville = new Ville("Paris");
        Aeroport aeroport = new Aeroport("CDG", "Charles de Gaulle", ville);

        assertEquals("CDG", aeroport.getCode());
        assertEquals("Charles de Gaulle", aeroport.getNom());
        assertEquals(ville, aeroport.getVille());
    }

    @Test
    public void setCodeDoitRefuserCodeNullOuVide() {
        Aeroport aeroport = new Aeroport();
        assertThrows(IllegalArgumentException.class, () -> aeroport.setCode(null));
        assertThrows(IllegalArgumentException.class, () -> aeroport.setCode("   "));
    }

    @Test
    public void setNomDoitRefuserNomNullOuVide() {
        Aeroport aeroport = new Aeroport();
        assertThrows(IllegalArgumentException.class, () -> aeroport.setNom(null));
        assertThrows(IllegalArgumentException.class, () -> aeroport.setNom("   "));
    }

    @Test
    public void setVilleDoitRefuserVilleNull() {
        Aeroport aeroport = new Aeroport();
        assertThrows(IllegalArgumentException.class, () -> aeroport.setVille(null));
    }

    @Test
    public void toStringDoitContenirCodeNomEtVille() {
        Ville ville = new Ville("Paris");
        Aeroport aeroport = new Aeroport("CDG", "Charles de Gaulle", ville);

        String s = aeroport.toString();
        assertTrue(s.contains("CDG"));
        assertTrue(s.contains("Charles de Gaulle"));
        assertTrue(s.contains("Paris"));
    }
}
