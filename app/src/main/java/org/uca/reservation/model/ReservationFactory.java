package org.uca.reservation.model;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

import org.uca.aeroport.Vol;
import org.uca.reservation.pricing.PolitiqueTarif;
import org.uca.reservation.pricing.TarifEco;

public class ReservationFactory {

    // Thread-safe for single-JVM demos; persistent uniqueness requires a database.
    private static final AtomicInteger NEXT_NUMBER = new AtomicInteger(1);

    public Reservation creer(double basePrice, Client client, Passager passager, Vol vol) {
        return creer(basePrice, new TarifEco(), client, passager, vol);
    }

    public Reservation creer(double basePrice, PolitiqueTarif politique,
                             Client client, Passager passager, Vol vol) {
        if (!Double.isFinite(basePrice) || basePrice < 0) {
            throw new IllegalArgumentException("Base price must be finite and nonnegative");
        }
        if (politique == null) {
            throw new IllegalArgumentException("Pricing policy is required");
        }
        Objects.requireNonNull(client, "client");
        Objects.requireNonNull(passager, "passager");
        Objects.requireNonNull(vol, "vol");

        double finalPrice = politique.calculer(basePrice);
        if (!Double.isFinite(finalPrice) || finalPrice < 0) {
            throw new IllegalArgumentException("Final price must be finite and nonnegative");
        }
        return new Reservation("RES-" + NEXT_NUMBER.getAndIncrement(),
                finalPrice, client, passager, vol);
    }
}
