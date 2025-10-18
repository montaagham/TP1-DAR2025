package clientPackage;

//lire du texte
import java.io.BufferedReader;
//gère les exception
import java.io.IOException;
//convertir les octets en texte lisible
import java.io.InputStreamReader;
//envoyer des text facilement
import java.io.PrintWriter;
//pour lma connection rx entre le client et le serveur
import java.net.Socket;

public class Client {
    public static void main(String[] args) {
    	//pour l'addresse de serveur 
        String serverAddress = "192.168.56.1";
        //le meme port de serveur
        int port = 1234;

        // Première étape
        System.out.println("Je suis un client pas encore connecté…");
        //try avec des ressource  assure la ferméture du serversocket a la sortir du block 
        //meme s'il y a une exception 
        try (
        	//ouverture une connexion tcp avec le serveur
            Socket socket = new Socket(serverAddress, port);
        	//envoiyer le text au serveur
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        	//resevoir la répence de serveur
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        	//permet de lire ce que l'utilisateur tape dans le console 
        	BufferedReader consoleInput = new BufferedReader(new InputStreamReader(System.in))
        ) {
            // Connexion réussie
            System.out.println("Je suis un client connecté à " + serverAddress + ":" + port);

            // Envoyer un message
            System.out.print("Tapez un message à envoyer au serveur : ");
            //récupère le texte tapez 
            String userMessage = consoleInput.readLine();
            //evoi le text au serveur
            out.println(userMessage);

            // Lire la réponse
            //attend un message de serveur
            String response = in.readLine();
            //affiche le message au console client
            System.out.println("Réponse du serveur : " + response);

            // Fermeture automatique
            System.out.println("Déconnexion du client...");

        } catch (IOException e) {
            System.err.println("Erreur client : " + e.getMessage());
            e.printStackTrace();
        }
    }
}
