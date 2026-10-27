package org.uca.reservation.model;

import static org.junit.jupiter.api.Assertions.*;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.junit.jupiter.api.Test;
import org.uca.aeroport.Aeroport;
import org.uca.aeroport.Compagnie;
import org.uca.aeroport.Ville;
import org.uca.aeroport.Vol;
import org.uca.reservation.pricing.TarifEco;
import org.uca.reservation.state.TransitionInterditeException;

class ReservationPaymentRegressionTest {

    private Reservation newReservation() {
        Client client = new Client();
        client.setNom("Demo Buyer");
        client.setMail("buyer@example.com");
        client.setMoyenPaiement("SIMULATED_CARD");
        Vol flight = new Compagnie().creerVol("AF123",
                ZonedDateTime.now(), ZonedDateTime.now().plusHours(2),
                new Aeroport("CDG", "Charles de Gaulle", new Ville("Paris")),
                new Aeroport("IST", "Istanbul Airport", new Ville("Istanbul")));
        return new ReservationFactory().creer(100.0, new TarifEco(), client,
                new Passager("Sample", "Alex", 25, "DEMO0001"), flight);
    }

    @Test
    void failedDebitMustNotChangeStateToPaid() {
        Reservation reservation = newReservation();
        reservation.getPaiement().debiter(); // Simulate an inconsistent external action.
        assertThrows(IllegalStateException.class, reservation::payer);
        assertEquals("EN_ATTENTE", reservation.getEtat().libelle());
    }

    @Test
    void forbiddenPaymentMustNotDebit() {
        Reservation reservation = newReservation();
        reservation.annuler();
        assertThrows(TransitionInterditeException.class, reservation::payer);
        assertFalse(reservation.getPaiement().estDebite());
        assertEquals("ANNULEE", reservation.getEtat().libelle());
    }

    @Test
    void rejectsNonFinitePrice() {
        Reservation example = newReservation();
        ReservationFactory factory = new ReservationFactory();
        for (double bad : new double[] {Double.NaN, Double.POSITIVE_INFINITY, -1}) {
            assertThrows(IllegalArgumentException.class,
                    () -> factory.creer(bad, new TarifEco(), example.getClient(),
                            example.getPassager(), example.getVol()));
        }
    }

    @Test
    void atomicCounterProducesUniqueReferencesAcrossThreads() throws Exception {
        Reservation example = newReservation();
        ReservationFactory factory = new ReservationFactory();
        int workers = 20;
        CountDownLatch start = new CountDownLatch(1);
        List<Callable<String>> jobs = new ArrayList<>();
        for (int i = 0; i < workers; i++) {
            jobs.add(() -> {
                start.await();
                return factory.creer(100.0, new TarifEco(),
                        newClient(), example.getPassager(), example.getVol()).getNumero();
            });
        }
        try (var pool = Executors.newFixedThreadPool(workers)) {
            List<Future<String>> futures = jobs.stream().map(pool::submit).toList();
            start.countDown();
            Set<String> references = new java.util.HashSet<>();
            for (Future<String> future : futures) {
                references.add(future.get());
            }
            assertEquals(workers, references.size());
        }
    }

    private Client newClient() {
        Client client = new Client();
        client.setNom("Demo Buyer");
        client.setMail("buyer@example.com");
        client.setMoyenPaiement("SIMULATED_CARD");
        return client;
    }
}
