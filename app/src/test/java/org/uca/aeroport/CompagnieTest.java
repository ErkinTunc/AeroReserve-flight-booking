/**
 * This test class checks the main behavior of the flight management model. 
 * It verifies that a company can create a 
 *      - flight with the correct number 
 *      - dates
 *      - departure airport
 *      - arrival airport
 * 
 * It also ensures that the created flight is automatically linked to the company 
 * and added to its flight collection. 
 * 
 * The failure tests check simple edge cases, such as missing dates or removing 
 * a flight from a company. 
 * 
 * Finally, the validity tests confirm the main business rule: 
 *      every flight created by a company must belong to that company and must not
 *      appear twice in the company’s flight list.
 */

package org.uca.aeroport;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestMethodOrder;

import java.time.ZoneId;
import java.time.ZonedDateTime;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CompagnieTest {

        // ------------------ Méthodes utilitaires pour les tests -----------------

        private Aeroport creerAeroport(String nom, String ville) {
                Aeroport aeroport = new Aeroport();
                aeroport.setNom(nom);
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

        // ============== Tests de réussite ==============

        @Test
        @Order(1)
        @DisplayName("Reussite 1.1 (creer Vol Ajoute Le Vol A La Compagnie)")
        public void creerVolAjouteLeVolALaCompagnie() {
                Compagnie compagnie = new Compagnie();
                compagnie.setName("Air France");

                Aeroport depart = creerAeroport("Charles de Gaulle", "Paris");
                Aeroport arrivee = creerAeroport("Istanbul Airport", "Istanbul");

                Vol vol = compagnie.creerVol(
                                "AF123",
                                dateParis(24, 9, 30),
                                dateIstanbul(24, 13, 45),
                                depart,
                                arrivee);

                assertEquals("AF123", vol.getNumero());
                assertEquals(compagnie, vol.getCompagnie());
                assertTrue(compagnie.getVols().contains(vol));
        }

        @Test
        @Order(2)
        @DisplayName("Reussite 1.2 (creer Vol Definit Les Dates Et Aeroports)")
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
        @Order(3)
        @DisplayName("Reussite 1.3 (obtenir Duree Retourne La Duree Du Vol)")
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
        @Order(4)
        @DisplayName("Reussite 1.4 (add Vol Retire Le Vol De Son Ancienne Compagnie)")
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

        // ============== Tests d'échec ==============

        @Test
        @Order(5)
        @DisplayName("Echec 2.1 (obtenir Duree Retourne Null Si Date Depart Ou Arrivee Manquante)")
        public void obtenirDureeRetourneNullSiDateDepartOuArriveeManquante() {
                Vol vol = new Vol();

                assertNull(vol.obtenirDuree());
        }

        @Test
        @Order(6)
        @DisplayName("Echec 2.2 (set Compagnie Null Retire Le Vol De La Compagnie)")
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

        // ============== Tests de validité ==============

        @Test
        @Order(7)
        @DisplayName("Validite 3.1 (Chaque Vol Cree Par Compagnie Est Rattache A Elle)")
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
        @Order(8)
        @DisplayName("Validite 3.2 (Une Compagnie Ne Contient Pas Deux Fois Le Meme Vol)")
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
}