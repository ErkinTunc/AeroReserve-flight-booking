package org.uca.reservation;

import java.math.BigDecimal;

/*
  Tarif promotionnel : -20% sur le prix de base.
 */

public class TarifPromo implements PolitiqueTarif {

    @Override
    public Money calculer(Money prixDeBase) {
        BigDecimal nouveau = prixDeBase.getAmount().multiply(BigDecimal.valueOf(0.8));
        return newMoneyLike(prixDeBase, nouveau);
    }
}