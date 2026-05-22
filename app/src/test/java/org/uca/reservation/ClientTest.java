package org.uca.reservation;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ClientTest {

    @Test
    public void gettersEtSettersDoiventStockerLesValeurs() {
        Client client = new Client();
        client.setNom("Durand");
        client.setMail("client@example.com");
        client.setMoyenPaiement("CB");
        client.setFidelitePoints(42);

        assertEquals("Durand", client.getNom());
        assertEquals("client@example.com", client.getMail());
        assertEquals("CB", client.getMoyenPaiement());
        assertEquals(42, client.getFidelitePoints());
    }

    @Test
    public void getReservationsRetourneUneListeNonModifiable() {
        Client client = new Client();

        List<Reservation> reservations = client.getReservations();

        assertNotNull(reservations);
        assertThrows(UnsupportedOperationException.class,
                () -> reservations.add(null));
    }

    @Test
    public void addReservationAjouteUneReservation() {
        Client client = new Client();
        Reservation r = new ReservationFactory().creer(100.0,
                client,
                new Passager("Dupont", "Alice", 30, "AB123456"));

        client.addReservation(r);

        assertTrue(client.getReservations().contains(r));
    }

    @Test
    public void removeReservationRetireLaReservation() {
        Client client = new Client();
        Reservation r = new ReservationFactory().creer(100.0,
                client,
                new Passager("Dupont", "Bob", 40, "CD789012"));

        client.addReservation(r);
        client.removeReservation(r);

        assertFalse(client.getReservations().contains(r));
    }

    @Test
    public void addReservationIgnoreNullEtDoublons() {
        Client client = new Client();
        Reservation r = new ReservationFactory().creer(100.0,
                client,
                new Passager("Dupont", "Claire", 28, "EF345678"));

        client.addReservation(null);
        client.addReservation(r);
        client.addReservation(r); // doublon

        assertEquals(1, client.getReservations().size());
        assertTrue(client.getReservations().contains(r));
    }
}
