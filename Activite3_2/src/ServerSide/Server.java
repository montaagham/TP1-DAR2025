package ServerSide;
import java.util.concurrent.atomic.AtomicInteger;
import java.io.IOException;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.lang.Thread;
import Shared.operation;

public class Server {
    static int port=500;
    //pour compter le nbr d'opération treter
    private static final AtomicInteger operationCount = new AtomicInteger(0);
    public Server(int port) {
        this.port= port;
    }
    public void start(){
        int nbr=0;
        System.out.println("Serveur démarrer  sur le port "+port+"....");
        try(ServerSocket serverSocket=new ServerSocket(port)){
            while(true){
                Socket clientSocket = serverSocket.accept();
                nbr++;
                System.out.println(" Nouveau client (" + nbr + ") connecté depuis : " + clientSocket.getInetAddress());
                ThreadClient clientHandler = new ThreadClient(clientSocket,operationCount);
                Thread t =new Thread(clientHandler);
                t.start();
            }
        } catch (Exception e) {
            System.out.println("Erreur de connexion"+e.getMessage());
        }
    }
    public static void main(String[] args) {
        int port=500;
        if(args.length>0){
            try {
                port=Integer.parseInt(args[0]);
            }catch (NumberFormatException ignored){}
        }
        new Server(port).start();
    }

}
