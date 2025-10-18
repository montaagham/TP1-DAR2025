import java.io.*;
import java.net.*;

public class serveur {
    private static final int PORT = 5000;

    public static void main(String[] args) {
        ServerSocket serverSocket = null;
        Socket clientSocket = null;

        try {
            // Création
            serverSocket = new ServerSocket(PORT);
            System.out.println("Serveur démarré sur le port " + PORT);
            System.out.println("En attente de connexion client...");

            // connexion
            clientSocket = serverSocket.accept();
            System.out.println("Client connecté : " + clientSocket.getInetAddress());


            BufferedReader in = new BufferedReader(
                    new InputStreamReader(clientSocket.getInputStream())
            );
            PrintWriter out = new PrintWriter(
                    clientSocket.getOutputStream(), true
            );

            //  nombre envoyé par le client
            String nombreRecu = in.readLine();
            System.out.println("Nombre reçu du client : " + nombreRecu);

            // calcul
            int x = Integer.parseInt(nombreRecu);
            int resultat = x * 5;
            System.out.println("Calcul effectué : " + x + " * 5 = " + resultat);

            // res
            out.println(resultat);
            System.out.println("Résultat envoyé au client : " + resultat);

        } catch (IOException e) {
            System.err.println("Erreur serveur : " + e.getMessage());
            e.printStackTrace();
        } catch (NumberFormatException e) {
            System.err.println("Erreur : format de nombre invalide");
            e.printStackTrace();
        } finally {

            try {
                if (clientSocket != null && !clientSocket.isClosed()) {
                    clientSocket.close();
                    System.out.println("Connexion client fermée");
                }
                if (serverSocket != null && !serverSocket.isClosed()) {
                    serverSocket.close();
                    System.out.println("Serveur arrêté");
                }
            } catch (IOException e) {
                System.err.println("Erreur lors de la fermeture : " + e.getMessage());
            }
        }
    }
}