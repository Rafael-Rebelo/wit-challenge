package pt.rafael_rebelo.wit_challenge;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;
import pt.rafael_rebelo.wit_challenge.service.CalculatorService;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = CalculatorService.class)
class CalculatorApplicationTests {

    @Autowired
    private CalculatorService calculatorService;

    @Test
    void testSum() {
        BigDecimal a = new BigDecimal("10.5");
        BigDecimal b = new BigDecimal("5.2");
        assertEquals(new BigDecimal("15.7"), calculatorService.sum(a, b));
    }

    @Test
    void testSubtract() {
        BigDecimal a = new BigDecimal("10.5");
        BigDecimal b = new BigDecimal("5.2");
        assertEquals(new BigDecimal("5.3"), calculatorService.subtract(a, b));
    }

    @Test
    void testMultiply() {
        BigDecimal a = new BigDecimal("10.5");
        BigDecimal b = new BigDecimal("2");
        assertEquals(new BigDecimal("21.0"), calculatorService.multiply(a, b));
    }

    @Test
    void testDivide() {
        BigDecimal a = new BigDecimal("10.5");
        BigDecimal b = new BigDecimal("2");
        assertEquals(new BigDecimal("5.3"), calculatorService.divide(a, b));
    }

}
