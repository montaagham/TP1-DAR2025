import java.io.*;
import java.net.*;
import java.util.Scanner;

public class ClientObjets {

    public static void main(String[] args) {

        String hote = "localhost";
        int port = 8000;

        Socket socket = null;
        Scanner sc = new Scanner(System.in);

        try {
            // connexion
            System.out.println("Connexion au serveur " + hote + ":" + port);
            socket = new Socket(hote, port);
            System.out.println("Connecte au serveur !");
            System.out.println();


            ObjectOutputStream oos = new ObjectOutputStream(
                    socket.getOutputStream()
            );
            ObjectInputStream ois = new ObjectInputStream(
                    socket.getInputStream()
            );

            // demander les informations a l'utilisateur
            System.out.println("calculatrice");
            System.out.println();

            // demander le premier operande
            System.out.print("Entrez le premier nombre : ");
            double op1 = sc.nextDouble();

            // demander l'operateur
            System.out.print("Entrez l'operateur (+, -, *, /) : ");
            char operateur = sc.next().charAt(0);

            // demander le deuxieme operande
            System.out.print("Entrez le deuxieme nombre : ");
            double op2 = sc.nextDouble();

            // creer l'objet Operation
            Operation operation = new Operation(op1, operateur, op2);
            System.out.println();
            System.out.println("Operation creee : " + operation.toString());

            // envoyer l'objet au serveur
            System.out.println("Envoi de l'objet au serveur...");
            oos.writeObject(operation);
            System.out.println("Objet envoye !");

            // recevoir le resultat
            System.out.println("Attente du resultat...");
            String resultat = (String) ois.readObject();

            // afficher le resultat
            System.out.println();
            System.out.println("Resultat");
            System.out.println(operation.toString() + " = " + resultat);

        } catch (UnknownHostException e) {
            System.out.println("Erreur : serveur introuvable");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Erreur de connexion : " + e.getMessage());
            System.out.println("Verifiez que le serveur est demarre !");
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.out.println("Erreur : classe non trouvee");
            e.printStackTrace();
        } finally {
            // fermer tout
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