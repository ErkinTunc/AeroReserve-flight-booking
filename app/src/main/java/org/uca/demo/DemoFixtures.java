package org.uca.demo;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.uca.aeroport.Aeroport;
import org.uca.aeroport.Compagnie;
import org.uca.aeroport.Ville;
import org.uca.aeroport.Vol;
import org.uca.reservation.model.Client;
import org.uca.reservation.model.Passager;

/** Isolated, fictional sample data. No external services or real identities. */
final class DemoFixtures {

    private DemoFixtures() {
    }

    static Vol createFlight() {
        Compagnie airline = new Compagnie();
        airline.setName("Air France");

        Aeroport origin = new Aeroport("CDG", "Charles de Gaulle", new Ville("Paris"));
        Aeroport destination = new Aeroport("IST", "Istanbul Airport", new Ville("Istanbul"));

        ZoneId parisZone = ZoneId.of("Europe/Paris");
        ZoneId istanbulZone = ZoneId.of("Europe/Istanbul");
        ZonedDateTime departure = ZonedDateTime.of(
                LocalDate.now(parisZone).plusDays(14), LocalTime.of(9, 0), parisZone);
        ZonedDateTime arrival = departure.plus(Duration.ofHours(3).plusMinutes(20))
                .withZoneSameInstant(istanbulZone);

        return airline.creerVol("AF123", departure, arrival, origin, destination);
    }

    static Client createClient() {
        Client client = new Client();
        client.setNom("Demo Customer");
        client.setMail("demo@example.com");
        client.setMoyenPaiement("SIMULATED_CARD");
        return client;
    }

    static Passager createPassenger() {
        return new Passager("Sample", "Alex", 25, "DEMO0001");
    }
}
