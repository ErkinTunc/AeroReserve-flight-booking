package org.uca.aeroport;

import java.util.Comparator;
import java.util.List;

public final class Trajet {

    private final String code;
    private final List<EtapeTrajet> etapes;

    // ------------------- Constructors ------------------

    public Trajet(String code, List<EtapeTrajet> etapes) {
        if (code == null || code.isBlank()) {
            throw new IllegalArgumentException("Le code du trajet est obligatoire");
        }

        if (etapes == null || etapes.size() < 2) {
            throw new IllegalArgumentException("Un trajet doit contenir au moins deux etapes");
        }

        long nombreOrdresDistincts = etapes.stream()
                .map(EtapeTrajet::getOrdre)
                .distinct()
                .count();

        if (nombreOrdresDistincts != etapes.size()) {
            throw new IllegalArgumentException("Deux etapes ne peuvent pas avoir le meme ordre");
        }

        this.code = code;
        this.etapes = etapes.stream()
                .sorted(Comparator.comparingInt(EtapeTrajet::getOrdre))
                .toList();
    }   

    // ------------------- Getters ------------------

    public String getCode() {
        return code;
    }

    public List<EtapeTrajet> getEtapes() {
        return etapes;
    }

    public EtapeTrajet getPremiereEtape() {
        return etapes.get(0);
    }

    public EtapeTrajet getDerniereEtape() {
        return etapes.get(etapes.size() - 1);
    }
}