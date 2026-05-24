
package org.uca.aeroport;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Vérifie la création des vols par une compagnie et les règles associées.
 */
public class CompagnieTest {

        // ------------------ Methodes utilitaires pour les tests ------------------

        private Aeroport creerAeroport(String nom, String nomVille) {
                Aeroport aeroport = new Aeroport();
                aeroport.setNom(nom);
                Ville ville = new Ville(nomVille);
                aeroport.setVille(ville);
                return aeroport;
        }

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

        private Vol creerVolParisIstanbul(Compagnie compagnie, String numero) {
                return compagnie.creerVol(
                                numero,
                                dateParis(24, 9, 30),
                                dateIstanbul(24, 13, 45),
                                creerAeroport("Charles de Gaulle", "Paris"),
                                creerAeroport("Istanbul Airport", "Istanbul"));
        }

        // ------------------ Tests de reussite ------------------

        @Test
        @DisplayName("1.1 Reussite : une compagnie peut creer un vol rattache a elle")
        public void creerVolAjouteLeVolALaCompagnie() {
                Compagnie compagnie = new Compagnie();
                compagnie.setName("Air France");

                Vol vol = creerVolParisIstanbul(compagnie, "AF123");

                assertAll(
                                () -> assertEquals("AF123", vol.getNumero()),
                                () -> assertSame(compagnie, vol.getCompagnie()),
                                () -> assertTrue(compagnie.getVols().contains(vol)));
        }

        @Test
        @DisplayName("1.2 Reussite : creerVol initialise les dates et les aeroports du vol")
        public void creerVolDefinitLesDatesEtAeroports() {
                Compagnie compagnie = new Compagnie();

                Aeroport depart = creerAeroport("Charles de Gaulle", "Paris");
                Aeroport arrivee = creerAeroport("Istanbul Airport", "Istanbul");

                ZonedDateTime dateDepart = dateParis(24, 9, 30);
                ZonedDateTime dateArrivee = dateIstanbul(24, 13, 45);

                Vol vol = compagnie.creerVol(
                                "AF123",
                                dateDepart,
                                dateArrivee,
                                depart,
                                arrivee);

                assertEquals(dateDepart, vol.getDateDepart());
                assertEquals(dateArrivee, vol.getDateArrivee());
                assertEquals(depart, vol.getDepart());
                assertEquals(arrivee, vol.getArrivee());
        }

        @Test
        @DisplayName("1.3 Reussite : obtenirDuree retourne la duree correcte du vol")
        public void obtenirDureeRetourneLaDureeDuVol() {
                Compagnie compagnie = new Compagnie();

                Vol vol = compagnie.creerVol(
                                "AF123",
                                dateParis(24, 9, 30),
                                dateIstanbul(24, 13, 45),
                                creerAeroport("Charles de Gaulle", "Paris"),
                                creerAeroport("Istanbul Airport", "Istanbul"));

                assertEquals(Duration.ofHours(3).plusMinutes(15), vol.obtenirDuree());
        }

        @Test
        @DisplayName("1.4 Reussite : addVol transfere un vol depuis son ancienne compagnie")
        public void addVolRetireLeVolDeSonAncienneCompagnie() {
                Compagnie ancienneCompagnie = new Compagnie();
                ancienneCompagnie.setName("Air France");

                Compagnie nouvelleCompagnie = new Compagnie();
                nouvelleCompagnie.setName("Turkish Airlines");

                Vol vol = ancienneCompagnie.creerVol(
                                "AF123",
                                dateParis(24, 9, 30),
                                dateIstanbul(24, 13, 45),
                                creerAeroport("Charles de Gaulle", "Paris"),
                                creerAeroport("Istanbul Airport", "Istanbul"));

                nouvelleCompagnie.addVol(vol);

                assertEquals(nouvelleCompagnie, vol.getCompagnie());
                assertFalse(ancienneCompagnie.getVols().contains(vol));
                assertTrue(nouvelleCompagnie.getVols().contains(vol));
        }

        // ------------------ Tests d'invalidite ------------------

        @Test
        @DisplayName("2.1 Invalidite : obtenirDuree retourne null si les dates sont manquantes")
        public void obtenirDureeRetourneNullSiDateDepartOuArriveeManquante() {
                Vol vol = new Vol("AF123");

                assertNull(vol.obtenirDuree());
        }

        @Test
        @DisplayName("2.2 Invalidite : setCompagnie null retire le vol de la compagnie")
        public void setCompagnieNullRetireLeVolDeLaCompagnie() {
                Compagnie compagnie = new Compagnie();

                Vol vol = compagnie.creerVol(
                                "AF123",
                                dateParis(24, 9, 30),
                                dateIstanbul(24, 13, 45),
                                creerAeroport("Charles de Gaulle", "Paris"),
                                creerAeroport("Istanbul Airport", "Istanbul"));

                vol.setCompagnie(null);

                assertNull(vol.getCompagnie());
                assertFalse(compagnie.getVols().contains(vol));
        }

        @Test
        @DisplayName("2.3 Invalidite : creerVol refuse une information null")
        public void creerVolLanceExceptionSiInformationNull() {
                Compagnie compagnie = new Compagnie();

                assertThrows(IllegalArgumentException.class, () -> compagnie.creerVol(
                                null,
                                dateParis(24, 9, 30),
                                dateIstanbul(24, 13, 45),
                                creerAeroport("Charles de Gaulle", "Paris"),
                                creerAeroport("Istanbul Airport", "Istanbul")));
        }

        // ------------------ Tests de validite ------------------

        @Test
        @DisplayName("3.1 Validite : chaque vol cree par une compagnie est rattache a elle")
        public void chaqueVolCreeParCompagnieEstRattacheAElle() {
                Compagnie compagnie = new Compagnie();

                Vol vol = compagnie.creerVol(
                                "AF123",
                                dateParis(24, 9, 30),
                                dateIstanbul(24, 13, 45),
                                creerAeroport("Charles de Gaulle", "Paris"),
                                creerAeroport("Istanbul Airport", "Istanbul"));

                assertSame(compagnie, vol.getCompagnie());
        }

        @Test
        @DisplayName("3.2 Validite : une compagnie ne contient pas deux fois le meme vol")
        public void uneCompagnieNeContientPasDeuxFoisLeMemeVol() {
                Compagnie compagnie = new Compagnie();

                Vol vol = compagnie.creerVol(
                                "AF123",
                                dateParis(24, 9, 30),
                                dateIstanbul(24, 13, 45),
                                creerAeroport("Charles de Gaulle", "Paris"),
                                creerAeroport("Istanbul Airport", "Istanbul"));

                compagnie.addVol(vol);
                compagnie.addVol(vol);

                assertEquals(1, compagnie.getVols().size());
        }

        @Test
        @DisplayName("3.3 Validite : une compagnie refuse deux vols avec le meme numero")
        public void compagnieRefuseDeuxVolsAvecLeMemeNumero() {
                Compagnie compagnie = new Compagnie();

                compagnie.creerVol(
                                "AF123",
                                dateParis(24, 9, 30),
                                dateIstanbul(24, 13, 45),
                                creerAeroport("Charles de Gaulle", "Paris"),
                                creerAeroport("Istanbul Airport", "Istanbul"));

                assertThrows(IllegalArgumentException.class, () -> compagnie.creerVol(
                                "AF123",
                                dateParis(25, 9, 30),
                                dateIstanbul(25, 13, 45),
                                creerAeroport("Charles de Gaulle", "Paris"),
                                creerAeroport("Istanbul Airport", "Istanbul")));
        }

        @Test
        @DisplayName("3.4 Validite : addVol refuse un numero deja present dans la compagnie")
        public void addVolRefuseUnNumeroDejaPresentDansLaCompagnie() {
                Compagnie compagnie = new Compagnie();

                compagnie.creerVol(
                                "AF123",
                                dateParis(24, 9, 30),
                                dateIstanbul(24, 13, 45),
                                creerAeroport("Charles de Gaulle", "Paris"),
                                creerAeroport("Istanbul Airport", "Istanbul"));

                Vol autreVol = new Vol("AF123");

                assertThrows(IllegalArgumentException.class, () -> compagnie.addVol(autreVol));
        }

        @Test
        @DisplayName("3.5 Validite : une compagnie peut generer automatiquement des numeros de vol uniques")
        public void compagnieGenereAutomatiquementDesNumerosUniques() {
                Compagnie compagnie = new Compagnie();

                Vol vol1 = compagnie.creerVol(
                                dateParis(24, 9, 30),
                                dateIstanbul(24, 13, 45),
                                creerAeroport("Charles de Gaulle", "Paris"),
                                creerAeroport("Istanbul Airport", "Istanbul"));

                Vol vol2 = compagnie.creerVol(
                                dateParis(25, 9, 30),
                                dateIstanbul(25, 13, 45),
                                creerAeroport("Charles de Gaulle", "Paris"),
                                creerAeroport("Istanbul Airport", "Istanbul"));

                assertNotEquals(vol1.getNumero(), vol2.getNumero());
                assertEquals("VOL-1", vol1.getNumero());
                assertEquals("VOL-2", vol2.getNumero());
        }

        @Test
        @DisplayName("3.6 Validite : le generateur saute un numero deja utilise manuellement")
        public void generateurSauteUnNumeroDejaUtiliseManuellement() {
                Compagnie compagnie = new Compagnie();

                compagnie.creerVol(
                                "VOL-1",
                                dateParis(24, 9, 30),
                                dateIstanbul(24, 13, 45),
                                creerAeroport("Charles de Gaulle", "Paris"),
                                creerAeroport("Istanbul Airport", "Istanbul"));

                Vol volAutomatique = compagnie.creerVol(
                                dateParis(25, 9, 30),
                                dateIstanbul(25, 13, 45),
                                creerAeroport("Charles de Gaulle", "Paris"),
                                creerAeroport("Istanbul Airport", "Istanbul"));

                assertEquals("VOL-2", volAutomatique.getNumero());
        }
}