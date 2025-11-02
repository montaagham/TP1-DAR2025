package Shared;

import java.io.Serializable;

public class res implements Serializable {
    private static final long serialVersionUID = 1L;
    private final boolean success;
    private final double value;
    private final String message;

    public res(boolean success, double value, String message) {
        this.success = success;
        this.value = value;
        this.message = message;
    }

    public boolean isSuccess() { return success; }
    public double getValue() { return value; }
    public String getMessage() { return message; }

    @Override
    public String toString() {
        if (success) return "Result: " + value;
        else return "Error: " + message;
    }
}
