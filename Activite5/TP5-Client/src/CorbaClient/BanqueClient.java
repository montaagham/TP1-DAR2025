package CorbaClient;

import org.omg.CORBA.ORB;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;
import corbaBanque.*;

public class BanqueClient {

    public static void main(String[] args) {
        try {
            // 1. initialiser l'ORB
            ORB orb = ORB.init(args, null);
            System.out.println("ORB client initialise");

            // 2. recuperer l'annuaire JNDI
            org.omg.CORBA.Object objRef = orb.resolve_initial_references("NameService");
            NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);

            // 3. recuperer la reference de l'objet distant
            String name = "BanqueService";
            IBanqueRemote banque = IBanqueRemoteHelper.narrow(ncRef.resolve_str(name));

            System.out.println("Connexion au service bancaire etablie");
            System.out.println("=====================================\n");

            // Test 1: creer des comptes
            System.out.println("--- Test 1: Creation de comptes ---");
            Compte c1 = new Compte(1001, 1000.0f);
            Compte c2 = new Compte(1002, 2500.0f);
            Compte c3 = new Compte(1003, 500.0f);

            banque.creerCompte(c1);
            banque.creerCompte(c2);
            banque.creerCompte(c3);
            System.out.println("3 comptes crees\n");

            // Test 2: verser de l'argent
            System.out.println("--- Test 2: Versement ---");
            banque.verser(500.0f, 1001);
            System.out.println();

            // Test 3: retirer de l'argent
            System.out.println("--- Test 3: Retrait ---");
            banque.retirer(200.0f, 1002);
            System.out.println();

            // Test 4: consulter un compte
            System.out.println("--- Test 4: Consultation d'un compte ---");
            Compte compte = banque.getCompte(1001);
            System.out.println("Compte " + compte.code + " - Solde: " + compte.solde + " DT\n");

            // Test 5: consulter tous les comptes
            System.out.println("--- Test 5: Liste de tous les comptes ---");
            Compte[] comptes = banque.getComptes();
            for (Compte c : comptes) {
                System.out.println("Compte " + c.code + " - Solde: " + c.solde + " DT");
            }
            System.out.println();

            // Test 6: conversion euro vers dinar
            System.out.println("--- Test 6: Conversion Euro -> Dinar ---");
            float montantEuro = 100.0f;
            double montantDinar = banque.conversion(montantEuro);
            System.out.println(montantEuro + " EUR = " + montantDinar + " DT\n");

            System.out.println("=====================================");
            System.out.println("Tous les tests executes avec succes!");

        } catch (Exception e) {
            System.err.println("Erreur client: " + e.getMessage());
            e.printStackTrace();
        }
    }
}