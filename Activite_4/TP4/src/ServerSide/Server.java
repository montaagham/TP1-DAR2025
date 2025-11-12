package ServerSide;


import java.io.IOException;
import java.net.*;
import java.util.*;


public class Server {

    public static final int PORT = 1234;
    // buffer size
    public static final int BUFFER_SIZE = 1024;
    private final Set<SocketAddress> clients = Collections.synchronizedSet(new HashSet<>());

    public void start() throws IOException {
        InetSocketAddress bindAddr = new InetSocketAddress("0.0.0.0", PORT);

        // Crée un DatagramSocket --->  bind()
        try (DatagramSocket serverSocket = new DatagramSocket(null)) {
            serverSocket.bind(bindAddr);
            System.out.println("Serveur UDP démarré sur " + bindAddr);

            // Buffer de réception
            byte[] buffer = new byte[BUFFER_SIZE];
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

            // Boucle principale du serveur : recevoir puis traiter
            while (true) {
                // receive() bloque jusqu'à ce qu'un paquet arrive
                serverSocket.receive(packet);

                // Convertion des bytes
                String msg = new String(packet.getData(), 0, packet.getLength(), "UTF-8");

                // adresse IP et port de sender
                SocketAddress senderAddr = packet.getSocketAddress();

                System.out.printf("Reçu de %s : %s%n", senderAddr, msg);

                //si le sender n'est pas encore dans la liste on l'ajoute
                if (!clients.contains(senderAddr)) {
                    clients.add(senderAddr);
                    System.out.println("Nouvel utilisateur ajouté : " + senderAddr);
                }

                //  bytes à renvoyer
                byte[] sendBytes = msg.getBytes("UTF-8");

               //synchronization
                synchronized (clients) {
                    for (SocketAddress clientAddr : clients) {
                        if (!clientAddr.equals(senderAddr)) {
                            // Création de DatagramPacket
                            DatagramPacket out = new DatagramPacket(sendBytes, sendBytes.length, clientAddr);
                            try {
                                serverSocket.send(out);
                            } catch (IOException e) {
                                System.err.println("Erreur envoi à " + clientAddr + " : " + e.getMessage());
                            }
                        }
                    }
                }

                // la longueur du packet au buffer complet
                packet.setLength(buffer.length);
            }
        }
    }

    public static void main(String[] args) {
        try {
            new Server().start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
