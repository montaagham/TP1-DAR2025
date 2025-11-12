package service;

import corbaBanque.*;
import java.util.ArrayList;
import java.util.List;

// Implementation du service bancaire
// Cette classe herite de IBanqueRemotePOA generee par idlj
public class BanqueImpl extends IBanqueRemotePOA {

    // liste pour stocker les comptes
    private List<Compte> comptes;

    // taux de conversion euro vers dinar
    private static final double TAUX_CONVERSION = 3.3;

    public BanqueImpl() {
        comptes = new ArrayList<>();
    }

    @Override
    public void creerCompte(Compte cpte) {
        // verifier si le compte existe deja
        for (Compte c : comptes) {
            if (c.code == cpte.code) {
                System.out.println("Compte " + cpte.code + " existe deja!");
                return;
            }
        }
        comptes.add(cpte);
        System.out.println("Compte cree: code=" + cpte.code + ", solde=" + cpte.solde);
    }

    @Override
    public void verser(float mt, int code) {
        // chercher le compte
        for (Compte c : comptes) {
            if (c.code == code) {
                c.solde += mt;
                System.out.println("Versement de " + mt + " DT sur compte " + code);
                System.out.println("Nouveau solde: " + c.solde + " DT");
                return;
            }
        }
        System.out.println("Compte " + code + " introuvable!");
    }

    @Override
    public void retirer(float mt, int code) {
        // chercher le compte
        for (Compte c : comptes) {
            if (c.code == code) {
                if (c.solde >= mt) {
                    c.solde -= mt;
                    System.out.println("Retrait de " + mt + " DT du compte " + code);
                    System.out.println("Nouveau solde: " + c.solde + " DT");
                } else {
                    System.out.println("Solde insuffisant!");
                }
                return;
            }
        }
        System.out.println("Compte " + code + " introuvable!");
    }

    @Override
    public Compte getCompte(int code) {
        // chercher et retourner le compte
        for (Compte c : comptes) {
            if (c.code == code) {
                return c;
            }
        }
        // retourner un compte vide si non trouve
        return new Compte(0, 0);
    }

    @Override
    public Compte[] getComptes() {
        // convertir la liste en tableau
        Compte[] tab = new Compte[comptes.size()];
        for (int i = 0; i < comptes.size(); i++) {
            tab[i] = comptes.get(i);
        }
        return tab;
    }

    @Override
    public double conversion(float mt) {
        // convertir euro vers dinar
        return mt * TAUX_CONVERSION;
    }
}