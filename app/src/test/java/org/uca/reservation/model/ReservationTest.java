package org.uca.reservation.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.uca.aeroport.Aeroport;
import org.uca.aeroport.Compagnie;
import org.uca.aeroport.Ville;
import org.uca.aeroport.Vol;

import org.uca.reservation.state.TransitionInterditeException;

import java.time.ZonedDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Vérifie les règles principales d'une réservation.
 */
public class ReservationTest {

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

    private Reservation newReservation() {
        return new ReservationFactory().creer(
                100.0,
                newClient(),
                newPassager(),
                newVol());
    }

    // ------------------ Tests de reussite ------------------

    @Test
    @DisplayName("1.1 Reussite : la creation initialise les champs")
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
    @DisplayName("1.2 Reussite : payer change l'etat en payee")
    public void payerDevraitChangerLEtatEnPayee() {
        Reservation reservation = newReservation();

        reservation.payer();

        assertEquals("PAYEE", reservation.getEtat().libelle());
    }

    @Test
    @DisplayName("1.3 Reussite : confirmer apres paiement donne confirmee")
    public void confirmerApresPaiementDevraitDonnerConfirmee() {
        Reservation reservation = newReservation();

        reservation.payer();
        reservation.confirmer();

        assertEquals("CONFIRMEE", reservation.getEtat().libelle());
    }

    @Test
    @DisplayName("1.4 Reussite : annuler depuis en attente donne annulee")
    public void annulerDepuisEnAttenteDevraitDonnerAnnulee() {
        Reservation reservation = newReservation();

        reservation.annuler();

        assertEquals("ANNULEE", reservation.getEtat().libelle());
    }

    // ------------------ Tests d'invalidite ------------------

    @Test
    @DisplayName("2.1 Invalidite : confirmer depuis en attente est interdit")
    public void confirmerDepuisEnAttenteDoitEchouer() {
        Reservation reservation = newReservation();

        assertEquals("EN_ATTENTE", reservation.getEtat().libelle());

        assertThrows(TransitionInterditeException.class, reservation::confirmer);
    }

    @Test
    @DisplayName("2.2 Invalidite : annuler depuis payee est interdit")
    public void annulerDepuisPayeeDoitEchouer() {
        Reservation reservation = newReservation();

        reservation.payer();
        assertEquals("PAYEE", reservation.getEtat().libelle());

        assertThrows(TransitionInterditeException.class, reservation::annuler);
    }

    @Test
    @DisplayName("2.3 Invalidite : payer depuis confirmee est interdit")
    public void payerDepuisConfirmeeDoitEchouer() {
        Reservation reservation = newReservation();

        reservation.payer();
        reservation.confirmer();
        assertEquals("CONFIRMEE", reservation.getEtat().libelle());

        assertThrows(TransitionInterditeException.class, reservation::payer);
    }

    // ------------------ Tests de validite ------------------

    @Test
    @DisplayName("3.1 Validite : le scenario complet respecte les transitions")
    public void scenarioCompletReservationEco() {
        Reservation reservation = newReservation();

        assertEquals("EN_ATTENTE", reservation.getEtat().libelle());
        assertEquals(100.0, reservation.getPrix(), 0.0001);

        reservation.payer();
        assertEquals("PAYEE", reservation.getEtat().libelle());

        reservation.confirmer();
        assertEquals("CONFIRMEE", reservation.getEtat().libelle());

        assertThrows(TransitionInterditeException.class, reservation::annuler);
    }

    @Test
    @DisplayName("3.2 Validite : payer debite le paiement")
    public void payerDoitDebiterLePaiement() {
        Reservation reservation = newReservation();

        reservation.payer();

        assertTrue(reservation.getPaiement().estDebite());
        assertEquals("PAYEE", reservation.getEtat().libelle());
    }
}