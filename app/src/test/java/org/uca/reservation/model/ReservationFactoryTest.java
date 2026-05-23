package org.uca.reservation.model;

import org.junit.jupiter.api.Test;

import org.uca.aeroport.Aeroport;
import org.uca.aeroport.Compagnie;
import org.uca.aeroport.Ville;
import org.uca.aeroport.Vol;

import org.uca.reservation.pricing.PolitiqueTarif;
import org.uca.reservation.pricing.TarifBusiness;
import org.uca.reservation.pricing.TarifEco;
import org.uca.reservation.pricing.TarifPromo;

import static org.junit.jupiter.api.Assertions.*;

import java.time.ZonedDateTime;

public class ReservationFactoryTest {

    // ---------------- Helper methods ----------------

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
        Client c = new Client();
        c.setNom("Durand");
        c.setMail("client@example.com");
        c.setMoyenPaiement("CB");
        return c;
    }

    private Passager newPassager() {
        return new Passager("Dupont", "Alice", 30, "AB123456");
    }

    // ---------------- Test methods ----------------

    @Test
    public void creerAvecTarifEcoDoitConserverLePrixDeBase() {
        Client client = newClient();
        Passager passager = newPassager();
        ReservationFactory factory = new ReservationFactory();
        PolitiqueTarif eco = new TarifEco();

        Reservation reservation = factory.creer(100.0, eco, client, passager, newVol());

        assertEquals(100.0, reservation.getPrix(), 0.0001);
    }

    @Test
    public void creerAvecTarifBusinessDoitAugmenterLePrixDe50Pourcents() {
        Client client = newClient();
        Passager passager = newPassager();
        ReservationFactory factory = new ReservationFactory();
        PolitiqueTarif business = new TarifBusiness();

        Reservation reservation = factory.creer(100.0, business, client, passager, newVol());

        assertEquals(150.0, reservation.getPrix(), 0.0001);
    }

    @Test
    public void creerAvecTarifPromoDoitAppliquerUneRemiseDe20Pourcents() {
        Client client = newClient();
        Passager passager = newPassager();
        ReservationFactory factory = new ReservationFactory();
        PolitiqueTarif promo = new TarifPromo();

        Reservation reservation = factory.creer(100.0, promo, client, passager, newVol());

        assertEquals(80.0, reservation.getPrix(), 0.0001);
    }
}
