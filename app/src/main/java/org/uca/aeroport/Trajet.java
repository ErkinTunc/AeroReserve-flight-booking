package org.uca.aeroport;

import java.util.List;

public final class Trajet {
    private final String code;
    private final List<Etape> etapes;

    public Trajet(String code, List<Etape> etapes) {
        if (code == null || code.isBlank()) {
            throw new IllegalArgumentException("Le code du trajet est obligatoire");
        }
        if (etapes == null || etapes.size() < 2) {
            throw new IllegalArgumentException("Un trajet doit contenir au moins deux étapes");
        }

        this.code = code;
        this.etapes = List.copyOf(etapes);
    }

    public String getCode() {
        return code;
    }

    public List<Etape> getEtapes() {
        return etapes;
    }
}