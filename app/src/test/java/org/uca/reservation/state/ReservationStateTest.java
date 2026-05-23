package org.uca.reservation.state;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.ZonedDateTime;

import org.junit.jupiter.api.Test;

import org.uca.aeroport.Aeroport;
import org.uca.aeroport.Compagnie;
import org.uca.aeroport.Ville;
import org.uca.aeroport.Vol;

import org.uca.reservation.model.Reservation;

import org.uca.reservation.model.Client;
import org.uca.reservation.model.Passager;
import org.uca.reservation.model.ReservationFactory;

public class ReservationStateTest {

    // ---------------------------- Helper methods ----------------
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

    private Reservation newReservation() {
        Client client = new Client();
        client.setNom("Durand");
        client.setMail("client@example.com");
        client.setMoyenPaiement("CB");

        Passager passager = new Passager("Dupont", "Alice", 30, "AB123456");

        return new ReservationFactory().creer(100.0, client, passager, newVol());
    }

    // ----------------------------- Test methods ----------------

    @Test
    public void payerDepuisEnAttenteDoitPasserEnPayee() {
        Reservation reservation = newReservation();

        reservation.payer();

        assertEquals("PAYEE", reservation.getEtat().libelle());
    }

    @Test
    public void confirmerApresPaiementDoitPasserEnConfirmee() {
        Reservation reservation = newReservation();

        reservation.payer();
        reservation.confirmer();

        assertEquals("CONFIRMEE", reservation.getEtat().libelle());
    }

    @Test
    public void confirmerDepuisEnAttenteDoitEchouer() {
        Reservation reservation = newReservation();

        assertThrows(TransitionInterditeException.class, reservation::confirmer);
    }
}
