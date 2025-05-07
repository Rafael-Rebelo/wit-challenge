package pt.rafael_rebelo.wit_challenge.models;

import java.math.BigDecimal;

public class CalculatorRequest {
    private String operation;
    private BigDecimal a;
    private BigDecimal b;

    // Default constructor (required for Kafka deserialization)
    public CalculatorRequest() {}

    public CalculatorRequest(String operation, BigDecimal a, BigDecimal b) {
        this.operation = operation;
        this.a = a;
        this.b = b;
    }

    public String getOperation() {
        return operation;
    }

    public BigDecimal getA() {
        return a;
    }

    public BigDecimal getB() {
        return b;
    }
}
