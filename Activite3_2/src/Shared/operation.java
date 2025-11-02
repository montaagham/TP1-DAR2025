package Shared;

import java.io.Serializable;

public class operation implements Serializable {
    private static final long serialVersionUID = 1L;
    private final double a;
    private final double b;
    private final String operator;

    public operation(double a, double b, String operator) {
        this.a = a;
        this.b = b;
        this.operator = operator;
    }

    public double getA() { return a; }
    public double getB() { return b; }
    public String getOperator() { return operator; }

    @Override
    public String toString() {
        return a + " " + operator + " " + b;
    }
}
