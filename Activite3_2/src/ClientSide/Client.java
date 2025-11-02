package ClientSide;

import Shared.operation;
import Shared.res;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    String host = "localhost";
    int port = 500;

    public Client(String host, int port) {
        this.host = host;
        this.port = port;
    }

    void start() {
        try (
                Socket socket = new Socket(host, port);
                ObjectOutputStream os = new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream io = new ObjectInputStream(socket.getInputStream());
                Scanner sc = new Scanner(System.in);
        ) {
            os.flush();
            System.out.println("Connecté au serveur " + socket.getRemoteSocketAddress());
            System.out.println("Entrer votre opération taper quit pour quitter");

            while (true) {
                String line = sc.nextLine().trim();
                if (line.equalsIgnoreCase("quit") || line.equalsIgnoreCase("exit")) break;
                if (line.isEmpty()) continue;

                String[] parts = line.split("\\s+");
                if (parts.length != 3) {
                    System.out.println("Format invalide. il doit existe une espace !!");
                    continue;
                }

                try {
                    double a = Double.parseDouble(parts[0]);
                    String op = parts[1];
                    double b = Double.parseDouble(parts[2]);
                    operation operation = new operation(a, b, op);

                    // envoyer
                    os.writeObject(operation);
                    os.flush();

                    // lire résultat
                    Object resp = io.readObject();
                    if (resp instanceof res) {
                        res r = (res) resp;
                        System.out.println(r);
                    } else {
                        System.out.println("Réponse imprévue du serveur: " + resp);
                    }
                } catch (NumberFormatException nfe) {
                    System.out.println("Nombres invalides : " + nfe.getMessage());
                }
            }

            System.out.println("Fermeture client.");
        } catch (Exception e) {
            System.err.println("Erreur client: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        String host = "localhost";
        int port = 500;

        if (args.length >= 1) host = args[0];
        if (args.length >= 2) {
            try {
                port = Integer.parseInt(args[1]);
            } catch (NumberFormatException ignored) {}
        }

        new Client(host, port).start();
    }
}
