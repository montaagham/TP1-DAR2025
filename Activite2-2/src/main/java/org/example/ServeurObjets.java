import java.io.*;
import java.net.*;

public class ServeurObjets {

    public static void main(String[] args) {

        int port = 8000;
        ServerSocket serveurSocket = null;
        Socket clientSocket = null;

        try {
            // demarrer le serveur
            serveurSocket = new ServerSocket(port);
            System.out.println("Serveur demarre sur le port " + port);
            System.out.println("En attente d'un client...");

            // accepter la connexion
            clientSocket = serveurSocket.accept();
            System.out.println("Client connecte : " + clientSocket.getInetAddress());

            // creer les flux pour lire et ecrire des objets
            ObjectInputStream ois = new ObjectInputStream(
                    clientSocket.getInputStream()
            );
            ObjectOutputStream oos = new ObjectOutputStream(
                    clientSocket.getOutputStream()
            );

            // recevoir l'objet Operation du client
            System.out.println("Attente de l'objet Operation...");
            Operation op = (Operation) ois.readObject();
            System.out.println("Objet recu : " + op.toString());

            // recuperer les donnees de l'objet
            double op1 = op.getOperande1();
            char operateur = op.getOperateur();
            double op2 = op.getOperande2();

            System.out.println("Operande 1 : " + op1);
            System.out.println("Operateur : " + operateur);
            System.out.println("Operande 2 : " + op2);

            // faire le calcul
            double resultat = 0;
            boolean erreur = false;
            String messageErreur = "";

            switch (operateur) {
                case '+':
                    resultat = op1 + op2;
                    System.out.println("Calcul : " + op1 + " + " + op2 + " = " + resultat);
                    break;
                case '-':
                    resultat = op1 - op2;
                    System.out.println("Calcul : " + op1 + " - " + op2 + " = " + resultat);
                    break;
                case '*':
                    resultat = op1 * op2;
                    System.out.println("Calcul : " + op1 + " * " + op2 + " = " + resultat);
                    break;
                case '/':
                    if (op2 == 0) {
                        erreur = true;
                        messageErreur = "Erreur : division par zero";
                        System.out.println(messageErreur);
                    } else {
                        resultat = op1 / op2;
                        System.out.println("Calcul : " + op1 + " / " + op2 + " = " + resultat);
                    }
                    break;
                default:
                    erreur = true;
                    messageErreur = "Erreur : operateur invalide";
                    System.out.println(messageErreur);
            }

            // envoyer le resultat au client
            if (erreur) {
                oos.writeObject(messageErreur);
            } else {
                oos.writeObject(String.valueOf(resultat));
            }
            System.out.println("Resultat envoye au client");

        } catch (IOException e) {
            System.out.println("Erreur serveur : " + e.getMessage());
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.out.println("Erreur : classe Operation non trouvee");
            e.printStackTrace();
        } finally {
            // fermer les connexions
            try {
                if (clientSocket != null) {
                    clientSocket.close();
                }
                if (serveurSocket != null) {
                    serveurSocket.close();
                }
                System.out.println("Serveur arrete");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}