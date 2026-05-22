package org.uca;

import org.uca.aeroport.Aeroport;
import org.uca.aeroport.Compagnie;
import org.uca.aeroport.Ville;
import org.uca.aeroport.Vol;

import java.time.ZoneId;
import java.time.ZonedDateTime;

public class Start {

    public static void main(String[] args) {

        // Bidirectional
        Compagnie compagnie = new Compagnie();
        compagnie.setName("Air France");

        Ville paris = new Ville("Paris");
        Ville istanbul = new Ville("Istanbul");
        Ville ankara = new Ville("Ankara");

        Aeroport aeroportDepart1 = new Aeroport();
        aeroportDepart1.setNom("Charles de Gaulle");
        aeroportDepart1.setVille(paris);

        Aeroport aeroportArrivee1 = new Aeroport();
        aeroportArrivee1.setNom("Istanbul Airport");
        aeroportArrivee1.setVille(istanbul);

        ZonedDateTime dateDepart1 = ZonedDateTime.of(
                2020, 10, 21, 13, 0, 0, 0,
                ZoneId.of("Europe/Paris"));

        ZonedDateTime dateArrivee1 = ZonedDateTime.of(
                2020, 10, 23, 2, 15, 0, 0,
                ZoneId.of("Europe/Istanbul"));

        Vol vol = compagnie.creerVol(
                "abc1",
                dateDepart1,
                dateArrivee1,
                aeroportDepart1,
                aeroportArrivee1);

        Aeroport aeroportDepart2 = new Aeroport();
        aeroportDepart2.setNom("Orly");
        aeroportDepart2.setVille(paris);

        Aeroport aeroportArrivee2 = new Aeroport();
        aeroportArrivee2.setNom("Esenboga");
        aeroportArrivee2.setVille(ankara);

        ZonedDateTime dateDepart2 = ZonedDateTime.of(
                2020, 10, 22, 10, 0, 0, 0,
                ZoneId.of("Europe/Paris"));

        ZonedDateTime dateArrivee2 = ZonedDateTime.of(
                2020, 10, 22, 14, 0, 0, 0,
                ZoneId.of("Europe/Istanbul"));

        Vol vol2 = compagnie.creerVol(
                "abc2",
                dateDepart2,
                dateArrivee2,
                aeroportDepart2,
                aeroportArrivee2);

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
        aeroportDepart.setVille(paris);

        Aeroport aeroportArrivee = new Aeroport();
        aeroportArrivee.setNom("Istanbul Airport");
        aeroportArrivee.setVille(istanbul);

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

        // -----------------
        Vol volCree2 = compagnie.creerVol(
                "AF1234",
                dateDepartCreation,
                dateArriveeCreation,
                aeroportDepart,
                aeroportArrivee);

        Vol volCreeAutomatique = compagnie.creerVol(
                dateDepartCreation,
                dateArriveeCreation,
                aeroportDepart,
                aeroportArrivee);

        System.out.println(volCree2.getNumero());
        System.out.println(volCreeAutomatique.getNumero());
    }
}