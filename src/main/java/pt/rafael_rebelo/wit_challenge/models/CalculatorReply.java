package pt.rafael_rebelo.wit_challenge.models;

import java.math.BigDecimal;

public class CalculatorReply {
    private BigDecimal result;
    private String error;

    // Default constructor (required for Kafka deserialization)
    public CalculatorReply() {}

    public CalculatorReply(BigDecimal result) {
        this.result = result;
        this.error = null;
    }

    public CalculatorReply(String error) {
        this.result = null;
        this.error = error;
    }

    public BigDecimal getResult() {
        return result;
    }

    public String getError() {
        return error;
    }

    @Override
    public String toString() {
        return (error == null) ? result.toString() : error;
    }
}
