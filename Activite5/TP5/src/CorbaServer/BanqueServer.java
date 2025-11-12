package CorbaServer;

import org.omg.CORBA.ORB;
import org.omg.CosNaming.NameComponent;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.POAHelper;
import service.BanqueImpl;
import corbaBanque.IBanqueRemote;
import corbaBanque.IBanqueRemoteHelper;

public class BanqueServer {

    public static void main(String[] args) {
        try{
            ORB orb = ORB.init(args, null);
            System.out.println("ORB initialise");

            // 2. recuperer le POA et activer le POAManager
            POA rootPOA = POAHelper.narrow(orb.resolve_initial_references("RootPOA"));
            rootPOA.the_POAManager().activate();
            System.out.println("POA active");

            // 3. creer le servant
            BanqueImpl banqueImpl = new BanqueImpl();
            System.out.println("Servant cree");

            // 4. enregistrer le servant dans le POA
            org.omg.CORBA.Object obj = rootPOA.servant_to_reference(banqueImpl);
            IBanqueRemote banqueRef = IBanqueRemoteHelper.narrow(obj);

            // 5. enregistrer dans l'annuaire JNDI
            org.omg.CORBA.Object objRef = orb.resolve_initial_references("NameService");
            NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);

            // creer le nom pour l'annuaire
            NameComponent[] path = ncRef.to_name("BanqueService");
            ncRef.rebind(path, banqueRef);

            System.out.println("Serveur Banque pret et en attente de requetes...");

            // 6. mettre l'ORB en attente
            orb.run();

        } catch (Exception e) {
            System.err.println("Erreur serveur: " + e.getMessage());
            e.printStackTrace();
        }
    }
}