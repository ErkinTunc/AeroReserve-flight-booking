package org.uca.aeroport;

import java.util.Date;

public class Escale extends Etape {

    private Aeroport aeroport;

    // ------------------- Constructors ------------------

    public Escale(Date depart, Date arrivee, Aeroport aeroport) {
        super(depart, arrivee);

        if (aeroport == null) {
            throw new IllegalArgumentException("L'aeroport de l'escale est obligatoire");
        }

        this.aeroport = aeroport;
    }

    // ------------------- Getters and Setters ------------------

    public Aeroport getAeroport() {
        return aeroport;
    }

    public void setAeroport(Aeroport aeroport) {
        if (aeroport == null) {
            throw new IllegalArgumentException("L'aeroport de l'escale est obligatoire");
        }

        this.aeroport = aeroport;
    }

}