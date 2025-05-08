package pt.rafael_rebelo.wit_challenge.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;
import pt.rafael_rebelo.wit_challenge.models.CalculatorRequest;
import pt.rafael_rebelo.wit_challenge.models.CalculatorReply;
import pt.rafael_rebelo.wit_challenge.service.CalculatorService;

import java.math.BigDecimal;

@Component
public class CalculatorConsumer {
    private static final String REQUEST_TOPIC = "request";
    private static final String REPLY_TOPIC = "reply";

    private static final Logger logger = LoggerFactory.getLogger(CalculatorConsumer.class);

    private final CalculatorService calculatorService;

    public CalculatorConsumer(CalculatorService service) {
        this.calculatorService = service;
    }

    @KafkaListener(topics = REQUEST_TOPIC)
    @SendTo
    public CalculatorReply receive(CalculatorRequest request) {
        BigDecimal result = null;
        String error = null;

        logger.info("Received request: {} {} {}", request.getOperation(), request.getA(), request.getB());

        try {
            result = switch (request.getOperation().toLowerCase()) {
                case "sum" -> calculatorService.sum(request.getA(), request.getB());
                case "subtract" -> calculatorService.subtract(request.getA(), request.getB());
                case "multiply" -> calculatorService.multiply(request.getA(), request.getB());
                case "divide" -> calculatorService.divide(request.getA(), request.getB());
                default -> null;
            };
        } catch (ArithmeticException ae) {
            error = ae.getMessage();
            logger.error(error, ae);
        } catch (Exception e) {
            error = "Service error";
            logger.error("Error processing request: {}", e.getMessage(), e);
        }

        CalculatorReply response = (error == null) ? new CalculatorReply(result) : new CalculatorReply(error);
        logger.info("Sending response: {}", response);
        return response;
    }
}
