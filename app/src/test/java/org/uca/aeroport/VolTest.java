/**
 * This test class checks the main behavior of the Vol class.
 * It verifies that a flight can correctly store:
 *      - its number
 *      - departure and arrival dates
 *      - departure and arrival airports
 *      - its company
 *
 * It also checks that the flight duration is correctly calculated from the
 * departure and arrival dates.
 *
 * The failure tests check simple edge cases, such as missing departure or
 * arrival dates, and removing a flight from its company.
 *
 * Finally, the validity tests confirm the main business rules:
 *      - changing a flight's company updates both sides of the association
 *      - two flights with the same number are considered the same flight
 *      - the same flight number must not appear twice in a HashSet
 */

package org.uca.aeroport;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class VolTest {

    // ------------------ Méthodes utilitaires pour les tests -----------------

    private ZonedDateTime dateParis(int jour, int heure, int minute) {
        return ZonedDateTime.of(
                2020, 10, jour, heure, minute, 0, 0,
                ZoneId.of("Europe/Paris"));
    }

    private ZonedDateTime dateIstanbul(int jour, int heure, int minute) {
        return ZonedDateTime.of(
                2020, 10, jour, heure, minute, 0, 0,
                ZoneId.of("Europe/Istanbul"));
    }

    private Aeroport creerAeroport(String nom, String ville) {
        Aeroport aeroport = new Aeroport();
        aeroport.setNom(nom);
        aeroport.setVille(ville);
        return aeroport;
    }

    private Vol creerVolSimple(String numero) {
        Vol vol = new Vol();
        vol.setNumero(numero);
        return vol;
    }

    // ============== Tests de réussite ==============

    @Test
    @Order(1)
    @DisplayName("Reussite 1.1 (obtenir Duree Retourne La Duree Entre Depart Et Arrivee)")
    public void obtenirDureeRetourneLaDureeEntreDepartEtArrivee() {
        Vol vol = new Vol();

        vol.setDateDepart(dateParis(24, 9, 30));
        vol.setDateArrivee(dateIstanbul(24, 13, 45));

        assertEquals(Duration.ofHours(3).plusMinutes(15), vol.obtenirDuree());
    }

    @Test
    @Order(2)
    @DisplayName("Reussite 1.2 (setCompagnie Ajoute Le Vol A La Compagnie)")
    public void setCompagnieAjouteLeVolALaCompagnie() {
        Vol vol = creerVolSimple("AF123");
        Compagnie compagnie = new Compagnie();
        compagnie.setName("Air France");

        vol.setCompagnie(compagnie);

        assertEquals(compagnie, vol.getCompagnie());
        assertTrue(compagnie.getVols().contains(vol));
    }

    @Test
    @Order(3)
    @DisplayName("Reussite 1.3 (setters Definissent Correctement Les Informations Du Vol)")
    public void settersDefinissentCorrectementLesInformationsDuVol() {
        Vol vol = new Vol();

        Aeroport depart = creerAeroport("Charles de Gaulle", "Paris");
        Aeroport arrivee = creerAeroport("Istanbul Airport", "Istanbul");
        ZonedDateTime dateDepart = dateParis(24, 9, 30);
        ZonedDateTime dateArrivee = dateIstanbul(24, 13, 45);

        vol.setNumero("AF123");
        vol.setDepart(depart);
        vol.setArrivee(arrivee);
        vol.setDateDepart(dateDepart);
        vol.setDateArrivee(dateArrivee);

        assertEquals("AF123", vol.getNumero());
        assertEquals(depart, vol.getDepart());
        assertEquals(arrivee, vol.getArrivee());
        assertEquals(dateDepart, vol.getDateDepart());
        assertEquals(dateArrivee, vol.getDateArrivee());
    }

    // ============== Tests d'échec ==============

    @Test
    @Order(4)
    @DisplayName("Reussite 2.4 (obtenir Duree Retourne Null Si Date Depart Ou Arrivee Manquante)")
    public void obtenirDureeRetourneNullSiDateDepartManquante() {
        Vol vol = new Vol();

        vol.setDateArrivee(dateIstanbul(24, 13, 45));

        assertNull(vol.obtenirDuree());
    }

    @Test
    @Order(5)
    @DisplayName("Reussite 2.5 (obtenir Duree Retourne Null Si Date Arrivee Manquante)")
    public void obtenirDureeRetourneNullSiDateArriveeManquante() {
        Vol vol = new Vol();

        vol.setDateDepart(dateParis(24, 9, 30));

        assertNull(vol.obtenirDuree());
    }

    @Test
    @Order(6)
    @DisplayName("Reussite 2.6 (set Compagnie Null Retire Le Vol De La Compagnie)")
    public void setCompagnieNullRetireLeVolDeSonAncienneCompagnie() {
        Vol vol = creerVolSimple("AF123");
        Compagnie compagnie = new Compagnie();

        vol.setCompagnie(compagnie);
        vol.setCompagnie(null);

        assertNull(vol.getCompagnie());
        assertFalse(compagnie.getVols().contains(vol));
    }

    // ============== Tests de validité ==============

    @Test
    @Order(7)
    @DisplayName("Validite 3.1 (Changer Compagnie Retire Le Vol De L'Ancienne Compagnie)")
    public void changerCompagnieRetireLeVolDeLAncienneCompagnie() {
        Vol vol = creerVolSimple("AF123");

        Compagnie ancienneCompagnie = new Compagnie();
        ancienneCompagnie.setName("Air France");

        Compagnie nouvelleCompagnie = new Compagnie();
        nouvelleCompagnie.setName("Turkish Airlines");

        vol.setCompagnie(ancienneCompagnie);
        vol.setCompagnie(nouvelleCompagnie);

        assertEquals(nouvelleCompagnie, vol.getCompagnie());
        assertFalse(ancienneCompagnie.getVols().contains(vol));
        assertTrue(nouvelleCompagnie.getVols().contains(vol));
    }

    @Test
    @Order(8)
    @DisplayName("Validite 3.2 (Deux Vols Avec Meme Numero Sont Egaux)")
    public void deuxVolsAvecMemeNumeroSontEgaux() {
        Vol vol1 = creerVolSimple("AF123");
        Vol vol2 = creerVolSimple("AF123");

        assertEquals(vol1, vol2);
    }

    @Test
    @Order(9)
    @DisplayName("Validite 3.3 (Deux Vols Avec Numeros Differents Ne Sont Pas Egaux)")
    public void deuxVolsAvecNumerosDifferentsNeSontPasEgaux() {
        Vol vol1 = creerVolSimple("AF123");
        Vol vol2 = creerVolSimple("TK456");

        assertNotEquals(vol1, vol2);
    }

    @Test
    @Order(10)
    @DisplayName("Validite 3.4 (HashSet Ne Contient Pas Deux Fois Le Meme Numero De Vol)")
    public void hashSetNeContientPasDeuxFoisLeMemeNumeroDeVol() {
        Vol vol1 = creerVolSimple("AF123");
        Vol vol2 = creerVolSimple("AF123");

        Set<Vol> vols = new HashSet<>();
        vols.add(vol1);
        vols.add(vol2);

        assertEquals(1, vols.size());
    }
}