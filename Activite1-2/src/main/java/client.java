import java.io.*;
import java.net.*;
import java.util.Scanner;

public class client {
    private static final String HOST = "localhost";
    private static final int PORT = 5000;

    public static void main(String[] args) {
        Socket socket = null;
        Scanner scanner = new Scanner(System.in);

        try {
            System.out.println("Tentative de connexion au serveur " + HOST + ":" + PORT);
            socket = new Socket(HOST, PORT);
            System.out.println("Connecté au serveur !");

            PrintWriter out = new PrintWriter(
                    socket.getOutputStream(), true
            );
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(socket.getInputStream())
            );

            // saisit d un nombre
            System.out.print("Entrez un nombre entier : ");
            int x = scanner.nextInt();

            out.println(x);
            System.out.println("Nombre envoyé au serveur : " + x);

            // res
            String resultat = in.readLine();
            System.out.println("Résultat reçu du serveur : " + resultat);
            System.out.println("Le serveur a calculé : " + x + " * 5 = " + resultat);

        } catch (UnknownHostException e) {
            System.err.println("Hôte inconnu : " + HOST);
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("Erreur de connexion : " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Erreur : " + e.getMessage());
            e.printStackTrace();
        } finally {

            try {
                if (socket != null && !socket.isClosed()) {
                    socket.close();
                    System.out.println("Connexion fermée");
                }
                scanner.close();
            } catch (IOException e) {
                System.err.println("Erreur lors de la fermeture : " + e.getMessage());
            }
        }
    }
}