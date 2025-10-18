import java.io.Serializable;

// classe : operation mathematique
// elle doit implementer Serializable pour pouvoir etre envoyee par le reseau
public class Operation implements Serializable {


    private double operande1;
    private char operateur;
    private double operande2;


    // constructeur avec parametres
    public Operation(double operande1, char operateur, double operande2) {
        this.operande1 = operande1;
        this.operateur = operateur;
        this.operande2 = operande2;
    }

    public double getOperande1() {
        return operande1;
    }

    public void setOperande1(double operande1) {
        this.operande1 = operande1;
    }

    public char getOperateur() {
        return operateur;
    }

    public void setOperateur(char operateur) {
        this.operateur = operateur;
    }

    public double getOperande2() {
        return operande2;
    }

    public void setOperande2(double operande2) {
        this.operande2 = operande2;
    }

    // methode toString pour afficher l'operation
    @Override
    public String toString() {
        return operande1 + " " + operateur + " " + operande2;
    }
}