package org.uca.reservation.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import org.uca.aeroport.Aeroport;
import org.uca.aeroport.Compagnie;
import org.uca.aeroport.Ville;
import org.uca.aeroport.Vol;

import org.uca.reservation.state.TransitionInterditeException;

import java.time.ZonedDateTime;

public class ReservationTest {

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
        // Adapter aux paramètres de ton constructeur Passager(String, String, int,
        // String)
        return new Passager("Dupont", "Alice", 30, "AB123456");
    }

    // --------------------------------- Test methods -------------------

    @Test
    public void creationReservationDoitInitialiserLesChamps() {
        Client client = newClient();
        Passager passager = newPassager();
        Vol vol = newVol();

        Reservation reservation = new ReservationFactory()
                .creer(100.0, client, passager, vol);

        assertNotNull(reservation.getNumero());
        assertTrue(reservation.getNumero().startsWith("RES-"));
        assertNotNull(reservation.getDate());

        assertEquals(100.0, reservation.getPrix(), 0.0001);
        assertEquals(client, reservation.getClient());
        assertEquals(passager, reservation.getPassager());
        assertEquals(vol, reservation.getVol());
        assertEquals("EN_ATTENTE", reservation.getEtat().libelle());

        assertTrue(client.getReservations().contains(reservation));
    }

    @Test
    public void payerDevraitChangerLEtatEnPayee() {
        Client client = newClient();
        Passager passager = newPassager();
        ReservationFactory factory = new ReservationFactory();

        Reservation reservation = factory.creer(100.0, client, passager, newVol());

        reservation.payer();

        assertEquals("PAYEE", reservation.getEtat().libelle());
    }

    @Test
    public void confirmerApresPaiementDevraitDonnerConfirmee() {
        Client client = newClient();
        Passager passager = newPassager();
        ReservationFactory factory = new ReservationFactory();

        Reservation reservation = factory.creer(100.0, client, passager, newVol());

        reservation.payer();
        reservation.confirmer();

        assertEquals("CONFIRMEE", reservation.getEtat().libelle());
    }

    @Test
    public void annulerDepuisEnAttenteDevraitDonnerAnnulee() {
        Client client = newClient();
        Passager passager = newPassager();
        ReservationFactory factory = new ReservationFactory();

        Reservation reservation = factory.creer(100.0, client, passager, newVol());

        reservation.annuler();

        assertEquals("ANNULEE", reservation.getEtat().libelle());
    }

    @Test
    public void confirmerDepuisEnAttenteDoitEchouer() {
        Client client = newClient();
        Passager passager = newPassager();
        ReservationFactory factory = new ReservationFactory();

        Reservation reservation = factory.creer(100.0, client, passager, newVol());

        assertEquals("EN_ATTENTE", reservation.getEtat().libelle());

        assertThrows(TransitionInterditeException.class, reservation::confirmer);
    }

    @Test
    public void annulerDepuisPayeeDoitEchouer() {
        Client client = newClient();
        Passager passager = newPassager();
        ReservationFactory factory = new ReservationFactory();

        Reservation reservation = factory.creer(100.0, client, passager, newVol());

        reservation.payer();
        assertEquals("PAYEE", reservation.getEtat().libelle());

        assertThrows(TransitionInterditeException.class, reservation::annuler);
    }

    @Test
    public void payerDepuisConfirmeeDoitEchouer() {
        Client client = newClient();
        Passager passager = newPassager();
        ReservationFactory factory = new ReservationFactory();

        Reservation reservation = factory.creer(100.0, client, passager, newVol());

        reservation.payer();
        reservation.confirmer();
        assertEquals("CONFIRMEE", reservation.getEtat().libelle());

        assertThrows(TransitionInterditeException.class, reservation::payer);
    }

    @Test
    public void scenarioCompletReservationEco() {
        Client client = newClient();
        Passager passager = newPassager();
        ReservationFactory factory = new ReservationFactory();

        Reservation reservation = factory.creer(100.0, client, passager, newVol());

        // EN_ATTENTE
        assertEquals("EN_ATTENTE", reservation.getEtat().libelle());
        assertEquals(100.0, reservation.getPrix(), 0.0001);

        // PAYEE
        reservation.payer();
        assertEquals("PAYEE", reservation.getEtat().libelle());

        // CONFIRMEE
        reservation.confirmer();
        assertEquals("CONFIRMEE", reservation.getEtat().libelle());

        // plus d’annulation possible
        assertThrows(TransitionInterditeException.class, reservation::annuler);
    }

}
