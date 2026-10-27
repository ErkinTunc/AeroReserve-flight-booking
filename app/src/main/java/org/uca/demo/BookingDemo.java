package org.uca.demo;

import java.time.Duration;
import java.util.Objects;

import org.uca.aeroport.Aeroport;
import org.uca.aeroport.Vol;
import org.uca.reservation.model.Reservation;
import org.uca.reservation.model.ReservationFactory;
import org.uca.reservation.pricing.TarifBusiness;
import org.uca.reservation.pricing.TarifEco;
import org.uca.reservation.pricing.TarifPromo;
import org.uca.reservation.state.TransitionInterditeException;

/**
 * Reproducible, local demonstration of the existing domain model.
 * Does not implement seat inventory, persistence, or real payments.
 */
public final class BookingDemo {

    private static final double BASE_FARE = 200.00;

    private final ConsoleReporter reporter;

    BookingDemo(ConsoleReporter reporter) {
        this.reporter = Objects.requireNonNull(reporter, "reporter");
    }

    /** Entry point for the command-line demonstration. */
    public static void runInConsole() {
        new BookingDemo(ConsoleReporter.forCurrentTerminal()).run();
    }

    void run() {
        reporter.banner();

        Vol flight = DemoFixtures.createFlight();
        printFlight(flight);
        printPricing();

        Reservation reservation = new ReservationFactory().creer(
                BASE_FARE,
                new TarifEco(),
                DemoFixtures.createClient(),
                DemoFixtures.createPassenger(),
                flight);

        reporter.section(3, "RESERVATION CREATED");
        requireStatus(reservation, "EN_ATTENTE");
        reporter.success("Reference: " + reservation.getNumero());
        reporter.field("Status", reservation.getEtat().libelle());

        demonstrateInvalidTransition(reservation);
        demonstratePaymentAndConfirmation(reservation);

        reporter.complete();
    }

    private void printFlight(Vol flight) {
        Aeroport origin = flight.getDepart();
        Aeroport destination = flight.getArrivee();
        Duration duration = Objects.requireNonNull(
                flight.obtenirDuree(), "Demo flight duration must be available");

        reporter.section(1, "FLIGHT INFORMATION");
        reporter.field("Flight", flight.getNumero());
        reporter.field("Airline", flight.getCompagnie().getName());
        reporter.field("Route", formatAirport(origin) + " -> " + formatAirport(destination));
        reporter.field("Duration", duration.toHours() + "h " + duration.toMinutesPart() + "m");
    }

    private String formatAirport(Aeroport airport) {
        return airport.getVille().getNom() + " (" + airport.getCode() + ")";
    }

    private void printPricing() {
        reporter.section(2, "PRICING STRATEGIES");
        reporter.fare("Economy", new TarifEco().calculer(BASE_FARE));
        reporter.fare("Business", new TarifBusiness().calculer(BASE_FARE));
        reporter.fare("Promotion", new TarifPromo().calculer(BASE_FARE));
    }

    private void demonstrateInvalidTransition(Reservation reservation) {
        reporter.section(4, "BUSINESS RULE VALIDATION");

        boolean rejected = false;
        try {
            reservation.confirmer();
        } catch (TransitionInterditeException expected) {
            // Use an ASCII-only public message: domain exceptions may be in French.
            rejected = true;
        }

        if (!rejected) {
            throw new IllegalStateException("Rule broken: unpaid reservation was confirmed");
        }
        requireStatus(reservation, "EN_ATTENTE");
        reporter.expectedRejection("Cannot confirm a reservation before payment.");
    }

    private void demonstratePaymentAndConfirmation(Reservation reservation) {
        reporter.section(5, "PAYMENT AND CONFIRMATION");

        reservation.payer(); // Local, simulated payment; no external provider.
        if (!reservation.getPaiement().estDebite()) {
            throw new IllegalStateException("Simulated payment was not debited");
        }
        requireStatus(reservation, "PAYEE");
        reporter.success("Simulated payment debited");

        reservation.confirmer();
        requireStatus(reservation, "CONFIRMEE");
        reporter.success("Reservation status: " + reservation.getEtat().libelle());
    }

    private void requireStatus(Reservation reservation, String expected) {
        String actual = reservation.getEtat().libelle();
        if (!expected.equals(actual)) {
            throw new IllegalStateException(
                    "Unexpected reservation status: expected " + expected + ", got " + actual);
        }
    }
}
