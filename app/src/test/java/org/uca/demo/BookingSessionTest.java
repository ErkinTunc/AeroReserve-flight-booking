package org.uca.demo;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.uca.reservation.model.Reservation;
import org.uca.reservation.state.TransitionInterditeException;

class BookingSessionTest {

    private static BookingSession.BookingRequest request(BookingSession.Fare fare) {
        return new BookingSession.BookingRequest(
                "Demo Buyer", "buyer@example.com", "Alex", "Sample", 25, "TEST0001", fare);
    }

    @Test
    void customerCanCreatePayAndConfirmBooking() {
        BookingSession session = new BookingSession();
        Reservation reservation = session.createReservation(request(BookingSession.Fare.PROMOTION));
        assertEquals(160.0, reservation.getPrix());
        assertEquals("EN_ATTENTE", reservation.getEtat().libelle());
        assertThrows(TransitionInterditeException.class,
                () -> session.confirm(reservation.getNumero()));
        assertEquals("EN_ATTENTE", reservation.getEtat().libelle());

        session.pay(reservation.getNumero());
        assertTrue(reservation.getPaiement().estDebite());
        assertEquals("PAYEE", reservation.getEtat().libelle());

        session.confirm(reservation.getNumero());
        assertEquals("CONFIRMEE", reservation.getEtat().libelle());
        assertThrows(TransitionInterditeException.class,
                () -> session.pay(reservation.getNumero()));
    }

    @Test
    void cancellationPreventsPayment() {
        BookingSession session = new BookingSession();
        Reservation reservation = session.createReservation(request(BookingSession.Fare.ECONOMY));
        session.cancel(reservation.getNumero());
        assertEquals("ANNULEE", reservation.getEtat().libelle());
        assertThrows(TransitionInterditeException.class,
                () -> session.pay(reservation.getNumero()));
        assertFalse(reservation.getPaiement().estDebite());
    }

    @Test
    void unknownReservationIsRejected() {
        BookingSession session = new BookingSession();
        assertThrows(java.util.NoSuchElementException.class,
                () -> session.findReservation("RES-DOES-NOT-EXIST"));
    }
}
