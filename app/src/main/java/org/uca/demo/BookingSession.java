package org.uca.demo;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;

import org.uca.aeroport.Vol;
import org.uca.reservation.model.Client;
import org.uca.reservation.model.Passager;
import org.uca.reservation.model.Reservation;
import org.uca.reservation.model.ReservationFactory;
import org.uca.reservation.pricing.PolitiqueTarif;
import org.uca.reservation.pricing.TarifBusiness;
import org.uca.reservation.pricing.TarifEco;
import org.uca.reservation.pricing.TarifPromo;

/** Local session for the CLI. NOT a seat-inventory or persistence service. */
final class BookingSession {

    static final double BASE_FARE_EUR = 200.00;

    enum Fare {
        ECONOMY("Economy", new TarifEco()),
        BUSINESS("Business", new TarifBusiness()),
        PROMOTION("Promotion", new TarifPromo());

        private final String label;
        private final PolitiqueTarif policy;

        Fare(String label, PolitiqueTarif policy) {
            this.label = label;
            this.policy = policy;
        }

        String label() {
            return label;
        }

        double price() {
            return policy.calculer(BASE_FARE_EUR);
        }

        PolitiqueTarif policy() {
            return policy;
        }

        static Fare fromOption(int option) {
            return switch (option) {
                case 1 -> ECONOMY;
                case 2 -> BUSINESS;
                case 3 -> PROMOTION;
                default -> throw new IllegalArgumentException("Unknown fare option: " + option);
            };
        }
    }

    record BookingRequest(String customerName, String customerEmail,
                          String passengerFirstName, String passengerLastName,
                          int passengerAge, String passportNumber, Fare fare) {
        BookingRequest {
            Objects.requireNonNull(fare, "fare");
        }
    }

    private final Vol flight;
    private final ReservationFactory reservationFactory;
    private final Map<String, Reservation> reservations = new LinkedHashMap<>();

    BookingSession() {
        this(DemoFixtures.createFlight(), new ReservationFactory());
    }

    BookingSession(Vol flight, ReservationFactory reservationFactory) {
        this.flight = Objects.requireNonNull(flight, "flight");
        this.reservationFactory = Objects.requireNonNull(reservationFactory, "reservationFactory");
    }

    Vol flight() {
        return flight;
    }

    Collection<Reservation> reservations() {
        return Collections.unmodifiableCollection(reservations.values());
    }

    Reservation createReservation(BookingRequest request) {
        Objects.requireNonNull(request, "request");
        Client customer = new Client();
        customer.setNom(request.customerName());
        customer.setMail(request.customerEmail());
        customer.setMoyenPaiement("SIMULATED_CARD");
        Passager passenger = new Passager(
                request.passengerLastName(), request.passengerFirstName(),
                request.passengerAge(), request.passportNumber());

        Reservation reservation = reservationFactory.creer(
                BASE_FARE_EUR, request.fare().policy(), customer, passenger, flight);
        reservations.put(reservation.getNumero(), reservation);
        return reservation;
    }

    Reservation findReservation(String reference) {
        Reservation reservation = reservations.get(reference);
        if (reservation == null) {
            throw new NoSuchElementException("Reservation not found: " + reference);
        }
        return reservation;
    }

    Reservation pay(String reference) {
        Reservation reservation = findReservation(reference);
        reservation.payer(); // Simulated payment only; domain enforces lifecycle.
        return reservation;
    }

    Reservation confirm(String reference) {
        Reservation reservation = findReservation(reference);
        reservation.confirmer();
        return reservation;
    }

    Reservation cancel(String reference) {
        Reservation reservation = findReservation(reference);
        reservation.annuler();
        return reservation;
    }
}
