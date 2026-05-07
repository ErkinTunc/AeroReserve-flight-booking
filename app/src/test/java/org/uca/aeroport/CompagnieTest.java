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

import org.junit.jupiter.api.Test;

import java.time.ZoneId;
import java.time.ZonedDateTime;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;

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

        // ============== Tests d'échec ==============

        @Test
        public void obtenirDureeRetourneNullSiDateDepartOuArriveeManquante() {
                Vol vol = new Vol();

                assertNull(vol.obtenirDuree());
        }

        @Test
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