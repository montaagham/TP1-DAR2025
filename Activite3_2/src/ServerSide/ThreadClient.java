package ServerSide;

import Shared.operation;
import Shared.res;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicInteger;

public class ThreadClient implements Runnable {
    private Socket clientSocket;
    private AtomicInteger operationCount;

    public ThreadClient(Socket clientSocket, AtomicInteger operationCount) {
        this.clientSocket = clientSocket;
        this.operationCount = operationCount;
    }

    @Override
    public void run() {
        try (
                // Important : créer ObjectOutputStream avant ObjectInputStream côté serveur ou client,
                // et flush l'OOS immédiatement pour éviter blocage (handshake).
                ObjectOutputStream oos = new ObjectOutputStream(clientSocket.getOutputStream());
                ObjectInputStream ois = new ObjectInputStream(clientSocket.getInputStream())
        ) {
            oos.flush();
            System.out.println("Flux d'objets établis avec " + clientSocket.getRemoteSocketAddress());
            Object obj;
            while ((obj = ois.readObject()) != null) {
                if (obj instanceof operation) {
                    operation op = (operation) obj;
                    res res = compute(op);
                    // incrémenter le compteur global et afficher sa valeur (thread-safe)
                    int current = operationCount.incrementAndGet();
                    System.out.println("Op traitée (" + op + ") par " + Thread.currentThread().getName()
                            + " — compteur global = " + current);
                    // renvoyer le résultat
                    oos.writeObject(res);
                    oos.flush();
                } else {
                    System.out.println("Objet inconnu reçu: " + obj.getClass().getName());
                    oos.writeObject(new res(false, 0, "Type d'objet non supporté"));
                    oos.flush();
                }
            }
        } catch (java.io.EOFException eof) {
            // client a fermé la connexion proprement
            System.out.println("Client " + clientSocket.getRemoteSocketAddress() + " déconnecté.");
        } catch (Exception e) {
            System.err.println("Erreur dans ClientHandler: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try { clientSocket.close(); } catch (Exception ignored) {}
        }
    }

    private res compute(operation op) {
        double a = op.getA();
        double b = op.getB();
        String optr = op.getOperator();
        try {
            switch (optr) {
                case "+": return new res(true, a + b, "OK");
                case "-": return new res(true, a - b, "OK");
                case "*": return new res(true, a * b, "OK");
                case "/":
                    if (b == 0) return new res(false, 0, "Division par zéro");
                    return new res(true, a / b, "OK");
                default:
                    return new res(false, 0, "Opérateur inconnu: " + optr);
            }
        } catch (Exception e) {
            return new res(false, 0, "Erreur calcul: " + e.getMessage());
        }
    }
}
