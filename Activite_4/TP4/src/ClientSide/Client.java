package ClientSide;


import java.io.IOException;
import java.net.*;
import java.util.Scanner;

public class Client {
    public static final int SERVER_PORT = 1234;
    public static final int BUFFER_SIZE = 1024;

    private final String serverHost;
    private final String username;
    //constructeur
    public Client(String serverHost, String username) {
        this.serverHost = serverHost;
        this.username = username;
    }
    public void start() throws IOException {
        // OS va choisir un port local libre
        try (DatagramSocket socket = new DatagramSocket()) {
            // Pas de timeout : blocking receive dans le thread receiver
            socket.setSoTimeout(0);

            InetAddress serverInet = InetAddress.getByName(serverHost);
            SocketAddress serverAddress = new InetSocketAddress(serverInet, SERVER_PORT);

            // Thread qui écoute les messages entrants et les affiche
            Thread receiver = new Thread(() -> {
                byte[] buf = new byte[BUFFER_SIZE];
                DatagramPacket pkt = new DatagramPacket(buf, buf.length);

                // Boucle infinie
                while (true) {
                    try {
                        socket.receive(pkt);
                        String received = new String(pkt.getData(), 0, pkt.getLength(), "UTF-8");
                        // Affichage du message
                        System.out.println(received);
                        pkt.setLength(buf.length);
                    } catch (IOException e) {
                        // Si socket.close() est appelé depuis un autre thread, receive() lèvera une exception
                        System.err.println("Erreur réception: " + e.getMessage());
                        break;
                    }
                }
            }, "ReceiverThread");

            // Mettre le receiver en daemon
            receiver.setDaemon(true);
            receiver.start();

            Scanner sc = new Scanner(System.in);
            System.out.println("Bienvenue, " + username + ". Tapez vos messages (ou /quit pour quitter) :");

            while (true) {
                String line;
                try {
                    line = sc.nextLine();
                } catch (Exception e) {
                    break;
                }
                if (line == null) break;

                line = line.trim();

                if (line.equalsIgnoreCase("/quit") || line.equalsIgnoreCase("/exit")) {
                    System.out.println("Fermeture du client.");
                    break;
                }
                String message = "[" + username + "] : " + line;

                // Convertion en bytes
                byte[] bytes = message.getBytes("UTF-8");
                DatagramPacket packet = new DatagramPacket(bytes, bytes.length, serverAddress);

                socket.send(packet);
            }
        }
    }

    public static void main(String[] args) {
        String host = "localhost";
        Scanner sc = new Scanner(System.in);
        System.out.print("Serveur (Tapez entrer): ");
        String inputHost = sc.nextLine().trim();
        if (!inputHost.isEmpty()) host = inputHost;

        //Nom d'utilisateur
        System.out.print("Nom d'utilisateur: ");
        String username = sc.nextLine().trim();
        if (username.isEmpty()) username = "Invité";

        try {
            new Client(host, username).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
