package org.uca;

import org.uca.aeroport.Aeroport;
import org.uca.aeroport.Compagnie;
import org.uca.aeroport.Vol;

import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.time.ZonedDateTime;
//import java.util.Date;

public class Start {

    public static void main(String[] args) {
        Vol volFinal = new Vol();

        ZonedDateTime dateDepart = ZonedDateTime.of(
                2020, 10, 21, 13, 0, 0, 0,
                ZoneId.of("Europe/Paris"));

        ZonedDateTime dateArrivee = ZonedDateTime.of(
                2020, 10, 23, 2, 15, 0, 0,
                ZoneId.of("Europe/Paris"));

        volFinal.setDateDepart(dateDepart);
        volFinal.setDateArrivee(dateArrivee);

        System.out.println(volFinal.getDateArrivee());
        System.out.println(volFinal.obtenirDuree().toString().substring(2));

        // Bidirectional
        Vol vol = new Vol();
        vol.setNumero("abc1");

        Vol vol2 = new Vol();
        vol2.setNumero("abc2");

        Compagnie compagnie = new Compagnie();
        compagnie.setName("Air France");
        compagnie.addVol(vol);
        compagnie.addVol(vol2);

        for (Vol v : compagnie.getVols()) {
            System.out.println(v.getNumero());
        }

        System.out.println(vol.getCompagnie().getName());
        System.out.println(vol2.getCompagnie().getName());

        vol2.setCompagnie(null);
        System.out.println(vol2.getCompagnie());

        for (Vol v : compagnie.getVols()) {
            System.out.println(v.getNumero());
        }

        // Creation de vols par Compagnie
        Aeroport aeroportDepart = new Aeroport();
        aeroportDepart.setNom("Charles de Gaulle");
        aeroportDepart.setVille("Paris");

        Aeroport aeroportArrivee = new Aeroport();
        aeroportArrivee.setNom("Istanbul Airport");
        aeroportArrivee.setVille("Istanbul");

        ZonedDateTime dateDepartCreation = ZonedDateTime.of(
                2020, 10, 24, 9, 30, 0, 0,
                ZoneId.of("Europe/Paris"));

        ZonedDateTime dateArriveeCreation = ZonedDateTime.of(
                2020, 10, 24, 13, 45, 0, 0,
                ZoneId.of("Europe/Istanbul"));

        Vol volCree = compagnie.creerVol(
                "AF123",
                dateDepartCreation,
                dateArriveeCreation,
                aeroportDepart,
                aeroportArrivee);

        System.out.println(volCree.getNumero());
        System.out.println(volCree.getCompagnie().getName());
        System.out.println(volCree.getDepart().getNom());
        System.out.println(volCree.getArrivee().getNom());
        System.out.println(volCree.obtenirDuree().toString().substring(2));
    }
}