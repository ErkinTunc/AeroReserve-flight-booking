package org.uca.aeroport;

import java.time.Duration;
import java.util.Date;

/*
    Classe de base qui va être commune aux éléments ayant un départ et une arrivée
    On pourrait l'utiliser pour factoriser du code si nécessaire
*/

public abstract class Etape {

    protected Date depart;

    protected Date arrivee;

    // ------------------- Constructors ------------------

    protected Etape(Date depart, Date arrivee) {
        verifierDatesCoherentes(depart, arrivee);

        this.depart = depart;
        this.arrivee = arrivee;
    }

    // ------------------- Methods ------------------

    // Duree dérivée = arrivee - depart
    public Duration getDuree() {
        if (depart == null || arrivee == null) {
            return Duration.ZERO;
        }
        return Duration.ofMillis(arrivee.getTime() - depart.getTime());
    }

    private void verifierDatesCoherentes(Date depart, Date arrivee) {
        if (depart != null && arrivee != null && depart.after(arrivee)) {
            throw new IllegalArgumentException(
                    "La date de depart ne peut pas etre apres la date d'arrivee");
        }
    }

    // ------------------- Getters and Setters ------------------
    public Date getDepart() {
        return depart;
    }

    public void setDepart(Date depart) {
        verifierDatesCoherentes(depart, this.arrivee);
        this.depart = depart;
    }

    public Date getArrivee() {
        return arrivee;
    }

    public void setArrivee(Date arrivee) {
        verifierDatesCoherentes(this.depart, arrivee);
        this.arrivee = arrivee;
    }

}