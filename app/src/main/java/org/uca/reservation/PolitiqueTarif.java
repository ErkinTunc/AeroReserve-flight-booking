package org.uca.reservation;

import java.math.BigDecimal;

/*
        Strategy Pattern pour le calcul du tarif.
        
        Chaque implémentation applique un algorithme différent
        au prix de base (éco, business, promo, etc)
 */
public interface PolitiqueTarif {

    /**
     * Calcule un nouveau prix à partir d'un prix de base.
     */
    Money calculer(Money prixDeBase);

    /**
     * Utilitaire pour créer un Money en conservant la devise du prix de base.
     */
    default Money newMoneyLike(Money base, BigDecimal montant) {
        return new Money(montant, base.getCurrency());
    }
}