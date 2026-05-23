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
 *      - two flights with the same number may have different technical identities
 *      - uniqueness of the flight number is enforced by Compagnie, not by Vol.equals
 *
 *  -----
 *  Added tests for Escale: addEscale, removeEscale, getEscales 
 * 
 */

package org.uca.aeroport;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;

import java.util.Date;

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

    private Aeroport creerAeroport(String code, String nom, String nomVille) {
        return new Aeroport(code, nom, new Ville(nomVille));
    }

    private Vol creerVolSimple(String numero) {
        return new Vol(numero);
    }

    // ============== Tests de réussite ==============

    @Test
    @DisplayName("Reussite 1.1 : obtenirDuree retourne la duree entre depart et arrivee")
    public void obtenirDureeRetourneLaDureeEntreDepartEtArrivee() {
        Vol vol = new Vol("AF123");

        vol.setDateDepart(dateParis(24, 9, 30));
        vol.setDateArrivee(dateIstanbul(24, 13, 45));

        assertEquals(Duration.ofHours(3).plusMinutes(15), vol.obtenirDuree());
    }

    @Test
    @DisplayName("Reussite 1.2 : setCompagnie ajoute le vol a la compagnie")
    public void setCompagnieAjouteLeVolALaCompagnie() {
        Vol vol = creerVolSimple("AF123");
        Compagnie compagnie = new Compagnie();
        compagnie.setName("Air France");

        vol.setCompagnie(compagnie);

        assertEquals(compagnie, vol.getCompagnie());
        assertTrue(compagnie.getVols().contains(vol));
    }

    @Test
    @DisplayName("Reussite 1.3 : les setters definissent les informations du vol")
    public void settersDefinissentCorrectementLesInformationsDuVol() {
        Vol vol = new Vol("AF123");

        Aeroport depart = creerAeroport("CDG", "Charles de Gaulle", "Paris");
        Aeroport arrivee = creerAeroport("IST", "Istanbul Airport", "Istanbul");
        ZonedDateTime dateDepart = dateParis(24, 9, 30);
        ZonedDateTime dateArrivee = dateIstanbul(24, 13, 45);

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
    @DisplayName("Echec 2.1 : obtenirDuree retourne null si la date de depart manque")
    public void obtenirDureeRetourneNullSiDateDepartManquante() {
        Vol vol = new Vol("AF123");

        vol.setDateArrivee(dateIstanbul(24, 13, 45));

        assertNull(vol.obtenirDuree());
    }

    @Test
    @DisplayName("Echec 2.2 : obtenirDuree retourne null si la date d'arrivee manque")
    public void obtenirDureeRetourneNullSiDateArriveeManquante() {
        Vol vol = new Vol("AF123");

        vol.setDateDepart(dateParis(24, 9, 30));

        assertNull(vol.obtenirDuree());
    }

    @Test
    @DisplayName("Echec 2.3 : setCompagnie null retire le vol de son ancienne compagnie")
    public void setCompagnieNullRetireLeVolDeSonAncienneCompagnie() {
        Vol vol = creerVolSimple("AF123");
        Compagnie compagnie = new Compagnie();

        vol.setCompagnie(compagnie);
        vol.setCompagnie(null);

        assertNull(vol.getCompagnie());
        assertFalse(compagnie.getVols().contains(vol));
    }

    @Test
    @DisplayName("Echec 2.4 : on ne peut pas ajouter une escale nulle")
    public void addEscaleRefuseNull() {
        Vol vol = new Vol("AF123");

        assertThrows(
                IllegalArgumentException.class,
                () -> vol.addEscale(null));
    }

    // ============== Tests de validité ==============

    @Test
    @DisplayName("Validite 3.1 : changerCompagnie retire le vol de l'ancienne compagnie")
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
    @DisplayName("Validite 3.2 : deux vols avec le meme numero ont des identites differentes")
    public void deuxVolsAvecMemeNumeroOntDesIdentitesDifferentes() {
        Vol vol1 = creerVolSimple("AF123");
        Vol vol2 = creerVolSimple("AF123");

        assertNotEquals(vol1, vol2);
        assertNotEquals(vol1.getId(), vol2.getId());
    }

    @Test
    @DisplayName("Validite 3.3 : deux vols avec des numeros differents ne sont pas egaux")
    public void deuxVolsAvecNumerosDifferentsNeSontPasEgaux() {
        Vol vol1 = creerVolSimple("AF123");
        Vol vol2 = creerVolSimple("TK456");

        assertNotEquals(vol1, vol2);
    }

    @Test
    @DisplayName("Validite 3.4 : un HashSet distingue deux vols avec des identites differentes")
    public void hashSetDistingueDeuxVolsAvecIdentitesDifferentes() {
        Vol vol1 = creerVolSimple("AF123");
        Vol vol2 = creerVolSimple("AF123");

        Set<Vol> vols = new HashSet<>();
        vols.add(vol1);
        vols.add(vol2);

        assertEquals(2, vols.size());
    }

    @Test
    @DisplayName("Validite 3.5 : setCompagnie refuse un numero deja present dans la compagnie")
    public void setCompagnieRefuseUnNumeroDejaPresentDansLaCompagnie() {
        Compagnie compagnie = new Compagnie();

        compagnie.creerVol(
                "AF123",
                dateParis(24, 9, 30),
                dateIstanbul(24, 13, 45),
                creerAeroport("CDG", "Charles de Gaulle", "Paris"),
                creerAeroport("IST", "Istanbul Airport", "Istanbul"));

        Vol autreVol = new Vol("AF123");

        assertThrows(IllegalArgumentException.class, () -> autreVol.setCompagnie(compagnie));
    }

    @Test
    @DisplayName("Validite 3.6 : un vol peut contenir une escale")
    public void volPeutContenirUneEscale() {
        Vol vol = new Vol("AF123");

        Aeroport aeroport = creerAeroport("IST", "Istanbul Airport", "Istanbul");

        Escale escale = new Escale(
                new Date(1_000_000L),
                new Date(2_000_000L),
                aeroport);

        vol.addEscale(escale);

        assertTrue(vol.getEscales().contains(escale));
        assertEquals(1, vol.getEscales().size());
    }

    @Test
    @DisplayName("Validite 3.7 : la liste des escales est non modifiable depuis l'exterieur")
    public void getEscalesRetourneListeNonModifiable() {
        Vol vol = new Vol("AF123");

        Aeroport aeroport = creerAeroport("IST", "Istanbul Airport", "Istanbul");

        Escale escale = new Escale(
                new Date(1_000_000L),
                new Date(2_000_000L),
                aeroport);

        assertThrows(
                UnsupportedOperationException.class,
                () -> vol.getEscales().add(escale));
    }

    @Test
    @DisplayName("Validite 3.8 : un vol peut supprimer une escale")
    public void volPeutSupprimerUneEscale() {
        Vol vol = new Vol("AF123");

        Aeroport aeroport = creerAeroport("IST", "Istanbul Airport", "Istanbul");

        Escale escale = new Escale(
                new Date(1_000_000L),
                new Date(2_000_000L),
                aeroport);

        vol.addEscale(escale);
        vol.removeEscale(escale);

        assertFalse(vol.getEscales().contains(escale));
        assertTrue(vol.getEscales().isEmpty());
    }

    @Test
    @DisplayName("Validite 3.10 : un vol sans escale contient une liste vide")
    public void volSansEscaleContientListeVide() {
        Vol vol = new Vol("AF123");

        assertTrue(vol.getEscales().isEmpty());
        assertEquals(0, vol.getEscales().size());
    }
}