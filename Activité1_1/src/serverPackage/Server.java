package serverPackage;
//exception d'E/S
import java.io.IOException;
//lire/ecrire de text via les sockets
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
//classe pour l'écoute et l'accept des connexion tcp
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) {
    	//choisir un port 
        final int port = 1234; 
        // Première étape : 
        //ouvre le port et commencer a écouter via serverSocket
        //try avec des ressource  assure la ferméture du serversocket a la sortir du block 
        //meme s'il y a une exception 
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Je suis un serveur en attente de la connexion d'un client");

            // Attente d’un client
            try (
            	//le code est blokcer dans accept jusqu'a ce qu'un cliant se connect
                Socket clientSocket = serverSocket.accept();
            	//clientSocket.getOutputStream flus d'entrée du client 
            	//on l'enveloppe dans un BufferReader pour lire du text plus facilement
            	//clientsocket.getOutputStream() permet d'envoiyer des données au clients
            	//en l'enveloppe dans PrintWirter pour envoiyer du texte
            	//l'argument true de PrintWriter active l'auto-flush c.a.d
            		//chaque println envoi directement le message au cliant(pas de tempons en attente
            	
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)
            ) {

                // Deuxième étape : un client est connecté
                System.out.println("Un client est connecté : " + clientSocket.getRemoteSocketAddress());
                
                // Exemple simple de communication : lire une ligne envoyée par le client
                String message = in.readLine(); // bloque jusqu’à ce que le client envoie une ligne terminée par \n
                if (message != null) {
                    System.out.println("Message du client: " + message);
                    // Répondre au client
                    out.println("Serveur: message reçu -> " + message);
                } else {
                    System.out.println("Le client s'est déconnecté sans envoyer de message.");
                }

                // Dernière étape : fermeture automatique grâce au try-with-resources
                System.out.println("Fermeture du socket client...");
            }

            System.out.println("Le serveur s'arrête (ServerSocket fermé automatiquement).");

        } catch (IOException e) {
            System.err.println("Erreur réseau / I/O : " + e.getMessage());
            e.printStackTrace();
        }
    }
}
