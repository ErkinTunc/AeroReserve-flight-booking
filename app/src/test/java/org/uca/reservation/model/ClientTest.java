package org.uca.reservation.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.ZonedDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

import org.uca.aeroport.Aeroport;
import org.uca.aeroport.Compagnie;
import org.uca.aeroport.Ville;
import org.uca.aeroport.Vol;

/**
 * Vérifie les règles principales d'un client.
 */
public class ClientTest {

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

    private Reservation newReservation(Client client, String nom, String prenom, int age, String passeport) {
        return new ReservationFactory().creer(
                100.0,
                client,
                new Passager(nom, prenom, age, passeport),
                newVol());
    }

    // ------------------ Tests de reussite ------------------

    @Test
    @DisplayName("1.1 Reussite : les getters et setters stockent les valeurs")
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
    @DisplayName("1.2 Reussite : addReservation ajoute une reservation")
    public void addReservationAjouteUneReservation() {
        Client client = new Client();
        Reservation reservation = newReservation(client, "Dupont", "Alice", 30, "AB123456");

        client.addReservation(reservation);

        assertTrue(client.getReservations().contains(reservation));
    }

    @Test
    @DisplayName("1.3 Reussite : removeReservation retire la reservation")
    public void removeReservationRetireLaReservation() {
        Client client = new Client();
        Reservation reservation = newReservation(client, "Dupont", "Bob", 40, "CD789012");

        client.addReservation(reservation);
        client.removeReservation(reservation);

        assertFalse(client.getReservations().contains(reservation));
    }

    // ------------------ Tests de validite ------------------

    @Test
    @DisplayName("3.1 Validite : getReservations retourne une liste non modifiable")
    public void getReservationsRetourneUneListeNonModifiable() {
        Client client = new Client();

        List<Reservation> reservations = client.getReservations();

        assertNotNull(reservations);
        assertThrows(
                UnsupportedOperationException.class,
                () -> reservations.add(null));
    }

    @Test
    @DisplayName("3.2 Validite : addReservation ignore null et les doublons")
    public void addReservationIgnoreNullEtDoublons() {
        Client client = new Client();
        Reservation reservation = newReservation(client, "Dupont", "Claire", 28, "EF345678");

        client.addReservation(null);
        client.addReservation(reservation);
        client.addReservation(reservation);

        assertEquals(1, client.getReservations().size());
        assertTrue(client.getReservations().contains(reservation));
    }
}