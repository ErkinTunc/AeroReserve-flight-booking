package org.uca.reservation;

import java.math.BigDecimal;

/**
 * Tarif Business : +50% sur le prix de base.
 */
public class TarifBusiness implements PolitiqueTarif {

    @Override
    public Money calculer(Money prixDeBase) {
        BigDecimal nouveau = prixDeBase.getAmount().multiply(BigDecimal.valueOf(1.5));
        return newMoneyLike(prixDeBase, nouveau);
    }
}