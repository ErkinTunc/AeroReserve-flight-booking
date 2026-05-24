package org.uca.aeroport;

import java.time.Duration;

public final class EtapeTrajet {

    private final int ordre;
    private final Aeroport aeroport;
    private final Duration decalageArrivee;
    private final Duration decalageDepart;

    // ------------------- Constructors ------------------

    public EtapeTrajet(int ordre, Aeroport aeroport,
            Duration decalageArrivee,
            Duration decalageDepart) {

        if (ordre < 0) {
            throw new IllegalArgumentException("L'ordre ne peut pas etre negatif");
        }
        if (aeroport == null) {
            throw new IllegalArgumentException("L'aeroport est obligatoire");
        }
        if (decalageArrivee != null && decalageArrivee.isNegative()) {
            throw new IllegalArgumentException("Le decalage d'arrivee ne peut pas etre negatif");
        }
        if (decalageDepart != null && decalageDepart.isNegative()) {
            throw new IllegalArgumentException("Le decalage de depart ne peut pas etre negatif");
        }
        if (decalageArrivee != null
                && decalageDepart != null
                && decalageDepart.compareTo(decalageArrivee) < 0) {
            throw new IllegalArgumentException("Le depart ne peut pas etre avant l'arrivee");
        }

        this.ordre = ordre;
        this.aeroport = aeroport;
        this.decalageArrivee = decalageArrivee;
        this.decalageDepart = decalageDepart;
    }

    // ------------------- Getters ------------------
    
    public int getOrdre() {
        return ordre;
    }

    public Aeroport getAeroport() {
        return aeroport;
    }

    public Duration getDecalageArrivee() {
        return decalageArrivee;
    }

    public Duration getDecalageDepart() {
        return decalageDepart;
    }

    public Duration getDureeArret() {
        if (decalageArrivee == null || decalageDepart == null) {
            return Duration.ZERO;
        }
        return decalageDepart.minus(decalageArrivee);
    }
}