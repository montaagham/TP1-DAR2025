import java.io.*;
import java.net.*;
import java.util.Scanner;

public class client {

    public static void main(String[] args) {

        String hote = "localhost";
        int port = 7000;

        Socket socket = null;
        Scanner sc = new Scanner(System.in);

        try {
            // connexion
            System.out.println("Connexion au serveur " + hote + ":" + port);
            socket = new Socket(hote, port);
            System.out.println("Connecte !");
            System.out.println();

            PrintWriter writer = new PrintWriter(
                    socket.getOutputStream(), true
            );
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(socket.getInputStream())
            );


            String operation = "";
            boolean valide = false;

            // validation 1
            while (!valide) {
                System.out.println("Entrez une operation (ex: 55 * 25) :");
                System.out.print("> ");
                operation = sc.nextLine();

                // validation 2
                if (validerOperation(operation)) {
                    valide = true;
                } else {
                    System.out.println("Erreur : operation invalide !");
                    System.out.println("Format attendu : nombre operateur nombre");
                    System.out.println("Operateurs acceptes : + - * /");
                    System.out.println();
                }
            }

            System.out.println("Envoi de l'operation au serveur...");
            writer.println(operation);

            String resultat = reader.readLine();
            System.out.println();
            System.out.println("Resultat recu : " + resultat);
            System.out.println(operation + " = " + resultat);

        } catch (UnknownHostException e) {
            System.out.println("Erreur : serveur introuvable");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Erreur de connexion : " + e.getMessage());
            System.out.println("Verifiez que le serveur est demarre !");
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

    public static boolean validerOperation(String operation) {
        try {

            operation = operation.trim();

            if (operation.isEmpty()) {
                return false;
            }


            boolean operateurTrouve = false;
            int posOperateur = -1;

            for (int i = 1; i < operation.length(); i++) {
                char c = operation.charAt(i);
                if (c == '+' || c == '-' || c == '*' || c == '/') {
                    operateurTrouve = true;
                    posOperateur = i;
                    break;
                }
            }

            if (!operateurTrouve) {
                return false;
            }
            String op1 = operation.substring(0, posOperateur).trim();
            String op2 = operation.substring(posOperateur + 1).trim();

            if (op1.isEmpty() || op2.isEmpty()) {
                return false;
            }
            Double.parseDouble(op1);
            Double.parseDouble(op2);

            return true;

        } catch (Exception e) {
            return false;
        }
    }
}