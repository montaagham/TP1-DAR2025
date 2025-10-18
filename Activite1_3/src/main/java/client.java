import java.io.*;
import java.net.*;
import java.util.Scanner;

public class client {

    public static void main(String[] args) {

        String ipServeur = "localhost";
        int port = 6000;

        Socket socket = null;
        Scanner sc = new Scanner(System.in);

        try {
            System.out.println("Connexion au serveur " + ipServeur + ":" + port);
            socket = new Socket(ipServeur, port);
            System.out.println("Connecte au serveur !");
            System.out.println();

            // creer les flux
            PrintWriter sortie = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader entree = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // afficher le menu
            System.out.println("===== CALCULATRICE RESEAU =====");
            System.out.println("Choisissez une operation :");
            System.out.println("1. Addition");
            System.out.println("2. Soustraction");
            System.out.println("3. Multiplication");
            System.out.println("4. Division");
            System.out.print("Votre choix (1-4) : ");
            String choix = sc.nextLine();


            sortie.println(choix);

            System.out.print("Entrez le premier nombre : ");
            String nb1 = sc.nextLine();

            System.out.print("Entrez le deuxieme nombre : ");
            String nb2 = sc.nextLine();

            sortie.println(nb1);
            sortie.println(nb2);

            System.out.println();
            System.out.println("Donnees envoyees au serveur...");
            System.out.println("Attente du resultat...");

            // res
            String resultat = entree.readLine();
            System.out.println();
            System.out.println("Resultat recu : " + resultat);

        } catch (UnknownHostException e) {
            System.out.println("Erreur : impossible de trouver le serveur");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Erreur de connexion : " + e.getMessage());
            System.out.println("Verifiez que le serveur est bien demarre !");
            e.printStackTrace();
        } finally {

            try {
                if (socket != null) {
                    socket.close();
                }
                sc.close();
                System.out.println("Connexion fermee");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}