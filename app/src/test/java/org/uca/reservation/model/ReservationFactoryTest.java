package org.uca.reservation.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.uca.aeroport.Aeroport;
import org.uca.aeroport.Compagnie;
import org.uca.aeroport.Ville;
import org.uca.aeroport.Vol;

import org.uca.reservation.pricing.PolitiqueTarif;
import org.uca.reservation.pricing.TarifBusiness;
import org.uca.reservation.pricing.TarifEco;
import org.uca.reservation.pricing.TarifPromo;

import java.time.ZonedDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Vérifie la création des réservations par la factory.
 */
public class ReservationFactoryTest {

    // ------------------ Methodes utilitaires pour les tests ------------------

    private Vol newVol() {
        Compagnie compagnie = new Compagnie();

        Aeroport depart = new Aeroport("CDG", "Charles de Gaulle", new Ville("Paris"));
        Aeroport arrivee = new Aeroport("MEX", "Benito Juarez", new Ville("Mexico"));

        return compagnie.creerVol(
                "AF123",
                ZonedDateTime.now(),
                ZonedDateTime.now().plusHours(12),
                depart,
                arrivee);
    }

    private Client newClient() {
        Client client = new Client();
        client.setNom("Durand");
        client.setMail("client@example.com");
        client.setMoyenPaiement("CB");
        return client;
    }

    private Passager newPassager() {
        return new Passager("Dupont", "Alice", 30, "AB123456");
    }

    // ------------------ Tests de reussite ------------------

    @Test
    @DisplayName("1.1 Reussite : creer avec TarifEco conserve le prix de base")
    public void creerAvecTarifEcoDoitConserverLePrixDeBase() {
        Client client = newClient();
        Passager passager = newPassager();
        ReservationFactory factory = new ReservationFactory();
        PolitiqueTarif eco = new TarifEco();

        Reservation reservation = factory.creer(100.0, eco, client, passager, newVol());

        assertEquals(100.0, reservation.getPrix(), 0.0001);
    }

    @Test
    @DisplayName("1.2 Reussite : creer avec TarifBusiness augmente le prix de 50 pourcents")
    public void creerAvecTarifBusinessDoitAugmenterLePrixDe50Pourcents() {
        Client client = newClient();
        Passager passager = newPassager();
        ReservationFactory factory = new ReservationFactory();
        PolitiqueTarif business = new TarifBusiness();

        Reservation reservation = factory.creer(100.0, business, client, passager, newVol());

        assertEquals(150.0, reservation.getPrix(), 0.0001);
    }

    @Test
    @DisplayName("1.3 Reussite : creer avec TarifPromo applique une remise de 20 pourcents")
    public void creerAvecTarifPromoDoitAppliquerUneRemiseDe20Pourcents() {
        Client client = newClient();
        Passager passager = newPassager();
        ReservationFactory factory = new ReservationFactory();
        PolitiqueTarif promo = new TarifPromo();

        Reservation reservation = factory.creer(100.0, promo, client, passager, newVol());

        assertEquals(80.0, reservation.getPrix(), 0.0001);
    }

    // ------------------ Tests d'invalidite ------------------

    @Test
    @DisplayName("2.1 Invalidite : creer refuse un prix de base negatif")
    public void creerDoitRefuserUnPrixDeBaseNegatif() {
        Client client = newClient();
        Passager passager = newPassager();
        ReservationFactory factory = new ReservationFactory();

        assertThrows(
                IllegalArgumentException.class,
                () -> factory.creer(-100.0, client, passager, newVol()));
    }

    @Test
    @DisplayName("2.2 Invalidite : creer refuse une politique tarifaire null")
    public void creerDoitRefuserUnePolitiqueTarifaireNull() {
        Client client = newClient();
        Passager passager = newPassager();
        ReservationFactory factory = new ReservationFactory();

        PolitiqueTarif politique = null;

        assertThrows(
                IllegalArgumentException.class,
                () -> factory.creer(100.0, politique, client, passager, newVol()));
    }
}