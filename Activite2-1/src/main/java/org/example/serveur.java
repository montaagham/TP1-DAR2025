import java.io.*;
import java.net.*;

public class serveur {

    public static void main(String[] args) {

        int port = 7000;
        ServerSocket serveurSocket = null;
        Socket clientSocket = null;

        try {
            // demarage
            serveurSocket = new ServerSocket(port);
            System.out.println("Serveur demarre sur le port " + port);
            System.out.println("En attente de connexion...");

            // connexion client
            clientSocket = serveurSocket.accept();
            System.out.println("Client connecte : " + clientSocket.getInetAddress());

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(clientSocket.getInputStream())
            );
            PrintWriter writer = new PrintWriter(
                    clientSocket.getOutputStream(), true
            );

            String operationRecue = reader.readLine();
            System.out.println("Operation recue : " + operationRecue);

            // calculer res
            String resultat = calculer(operationRecue);
            System.out.println("Resultat calcule : " + resultat);
            writer.println(resultat);
            System.out.println("Resultat envoye au client");

        } catch (IOException e) {
            System.out.println("Erreur serveur : " + e.getMessage());
            e.printStackTrace();
        } finally {

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

    public static String calculer(String operation) {
        try {

            operation = operation.trim();
            char operateur = ' ';
            int posOperateur = -1;
            for (int i = 1; i < operation.length(); i++) {
                char c = operation.charAt(i);
                if (c == '+' || c == '-' || c == '*' || c == '/') {
                    operateur = c;
                    posOperateur = i;
                    break;
                }
            }

            if (posOperateur == -1) {
                return "Erreur : operateur non trouve";
            }


            String op1Str = operation.substring(0, posOperateur).trim();
            String op2Str = operation.substring(posOperateur + 1).trim();


            double op1 = Double.parseDouble(op1Str);
            double op2 = Double.parseDouble(op2Str);

            // faire le calcul
            double resultat = 0;

            switch (operateur) {
                case '+':
                    resultat = op1 + op2;
                    break;
                case '-':
                    resultat = op1 - op2;
                    break;
                case '*':
                    resultat = op1 * op2;
                    break;
                case '/':
                    if (op2 == 0) {
                        return "Erreur : division par zero";
                    }
                    resultat = op1 / op2;
                    break;
                default:
                    return "Erreur : operateur invalide";
            }

            // res fianle
            return String.valueOf(resultat);

        } catch (NumberFormatException e) {
            return "Erreur : format de nombre invalide";
        } catch (Exception e) {
            return "Erreur : " + e.getMessage();
        }
    }
}