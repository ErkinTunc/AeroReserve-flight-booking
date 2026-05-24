package org.uca.aeroport;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Vérifie les règles principales d'un vol.
 */
public class VolTest {

    // ------------------ Methodes utilitaires pour les tests ------------------

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

    // ------------------ Tests de reussite ------------------

    @Test
    @DisplayName("1.1 Reussite : obtenirDuree retourne la duree entre depart et arrivee")
    public void obtenirDureeRetourneLaDureeEntreDepartEtArrivee() {
        Vol vol = new Vol("AF123");

        vol.setDateDepart(dateParis(24, 9, 30));
        vol.setDateArrivee(dateIstanbul(24, 13, 45));

        assertEquals(Duration.ofHours(3).plusMinutes(15), vol.obtenirDuree());
    }

    @Test
    @DisplayName("1.2 Reussite : setCompagnie ajoute le vol a la compagnie")
    public void setCompagnieAjouteLeVolALaCompagnie() {
        Vol vol = creerVolSimple("AF123");
        Compagnie compagnie = new Compagnie();
        compagnie.setName("Air France");

        vol.setCompagnie(compagnie);

        assertEquals(compagnie, vol.getCompagnie());
        assertTrue(compagnie.getVols().contains(vol));
    }

    @Test
    @DisplayName("1.3 Reussite : les setters definissent les informations du vol")
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

    // ------------------ Tests d'invalidite ------------------

    @Test
    @DisplayName("2.1 Invalidite : obtenirDuree retourne null si la date de depart manque")
    public void obtenirDureeRetourneNullSiDateDepartManquante() {
        Vol vol = new Vol("AF123");

        vol.setDateArrivee(dateIstanbul(24, 13, 45));

        assertNull(vol.obtenirDuree());
    }

    @Test
    @DisplayName("2.2 Invalidite : obtenirDuree retourne null si la date d'arrivee manque")
    public void obtenirDureeRetourneNullSiDateArriveeManquante() {
        Vol vol = new Vol("AF123");

        vol.setDateDepart(dateParis(24, 9, 30));

        assertNull(vol.obtenirDuree());
    }

    @Test
    @DisplayName("2.3 Invalidite : setCompagnie null retire le vol de son ancienne compagnie")
    public void setCompagnieNullRetireLeVolDeSonAncienneCompagnie() {
        Vol vol = creerVolSimple("AF123");
        Compagnie compagnie = new Compagnie();

        vol.setCompagnie(compagnie);
        vol.setCompagnie(null);

        assertNull(vol.getCompagnie());
        assertFalse(compagnie.getVols().contains(vol));
    }

    @Test
    @DisplayName("2.4 Invalidite : on ne peut pas ajouter une escale nulle")
    public void addEscaleRefuseNull() {
        Vol vol = new Vol("AF123");

        assertThrows(
                IllegalArgumentException.class,
                () -> vol.addEscale(null));
    }

    // ------------------ Tests de validite ------------------

    @Test
    @DisplayName("3.1 Validite : changerCompagnie retire le vol de l'ancienne compagnie")
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
    @DisplayName("3.2 Validite : deux vols avec le meme numero ont des identites differentes")
    public void deuxVolsAvecMemeNumeroOntDesIdentitesDifferentes() {
        Vol vol1 = creerVolSimple("AF123");
        Vol vol2 = creerVolSimple("AF123");

        assertNotEquals(vol1, vol2);
        assertNotEquals(vol1.getId(), vol2.getId());
    }

    @Test
    @DisplayName("3.3 Validite : deux vols avec des numeros differents ne sont pas egaux")
    public void deuxVolsAvecNumerosDifferentsNeSontPasEgaux() {
        Vol vol1 = creerVolSimple("AF123");
        Vol vol2 = creerVolSimple("TK456");

        assertNotEquals(vol1, vol2);
    }

    @Test
    @DisplayName("3.4 Validite : un HashSet distingue deux vols avec des identites differentes")
    public void hashSetDistingueDeuxVolsAvecIdentitesDifferentes() {
        Vol vol1 = creerVolSimple("AF123");
        Vol vol2 = creerVolSimple("AF123");

        Set<Vol> vols = new HashSet<>();
        vols.add(vol1);
        vols.add(vol2);

        assertEquals(2, vols.size());
    }

    @Test
    @DisplayName("3.5 Validite : setCompagnie refuse un numero deja present dans la compagnie")
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
    @DisplayName("3.6 Validite : un vol peut contenir une escale")
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
    @DisplayName("3.7 Validite : la liste des escales est non modifiable depuis l'exterieur")
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
    @DisplayName("3.8 Validite : un vol peut supprimer une escale")
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
    @DisplayName("3.9 Validite : un vol sans escale contient une liste vide")
    public void volSansEscaleContientListeVide() {
        Vol vol = new Vol("AF123");

        assertTrue(vol.getEscales().isEmpty());
        assertEquals(0, vol.getEscales().size());
    }
}