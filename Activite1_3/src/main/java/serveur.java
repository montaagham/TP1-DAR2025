import java.io.*;
import java.net.*;

public class serveur {

    public static void main(String[] args) {

        int port = 6000;
        ServerSocket ss = null;
        Socket socket = null;

        try {
            // creation
            ss = new ServerSocket(port);
            System.out.println("Adresse IP du serveur : localhost (127.0.0.1)");

            System.out.println("Serveur demarre sur le port " + port);
            System.out.println("En attente d'un client...");

            // attendre un client
            socket = ss.accept();
            System.out.println("Client connecte : " + socket.getInetAddress().getHostAddress());

            // flux d'entree et sortie
            BufferedReader entree = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter sortie = new PrintWriter(socket.getOutputStream(), true);


            String choix = entree.readLine();
            System.out.println("Operation choisie : " + choix);

            //  premier nombre
            String nb1_str = entree.readLine();
            int nb1 = Integer.parseInt(nb1_str);
            System.out.println("Premier nombre recu : " + nb1);

            // deuxieme nombre
            String nb2_str = entree.readLine();
            int nb2 = Integer.parseInt(nb2_str);
            System.out.println("Deuxieme nombre recu : " + nb2);
            double resultat = 0;
            String message = "";

            if (choix.equals("1")) {
                // +
                resultat = nb1 + nb2;
                message = nb1 + " + " + nb2 + " = " + resultat;
                System.out.println("Calcul : " + message);
            }
            else if (choix.equals("2")) {
                // -
                resultat = nb1 - nb2;
                message = nb1 + " - " + nb2 + " = " + resultat;
                System.out.println("Calcul : " + message);
            }
            else if (choix.equals("3")) {
                // *
                resultat = nb1 * nb2;
                message = nb1 + " * " + nb2 + " = " + resultat;
                System.out.println("Calcul : " + message);
            }
            else if (choix.equals("4")) {
                // div
                if (nb2 == 0) {
                    message = "Erreur : division par zero impossible !";
                    System.out.println(message);
                } else {
                    resultat = (double) nb1 / nb2;
                    message = nb1 + " / " + nb2 + " = " + resultat;
                    System.out.println("Calcul : " + message);
                }
            }
            else {
                message = "Erreur : choix invalide";
                System.out.println(message);
            }


            sortie.println(message);
            System.out.println("Resultat envoye au client");

        } catch (Exception e) {
            System.out.println("Erreur : " + e.getMessage());
            e.printStackTrace();
        } finally {

            try {
                if (socket != null) {
                    socket.close();
                }
                if (ss != null) {
                    ss.close();
                }
                System.out.println("Serveur arrete");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}