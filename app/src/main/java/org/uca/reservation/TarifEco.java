package org.uca.reservation;

import java.math.BigDecimal;

/**
 * Tarif économique : prix inchangé.
 */
public class TarifEco implements PolitiqueTarif {

    @Override
    public Money calculer(Money prixDeBase) {
        return prixDeBase;
    }
}