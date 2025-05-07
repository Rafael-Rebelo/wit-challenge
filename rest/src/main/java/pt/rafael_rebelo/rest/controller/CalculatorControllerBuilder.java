package pt.rafael_rebelo.rest.controller;

import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import pt.rafael_rebelo.wit_challenge.models.CalculatorReply;
import pt.rafael_rebelo.wit_challenge.models.CalculatorRequest;

public class CalculatorControllerBuilder {
    private ReplyingKafkaTemplate<String, CalculatorRequest, CalculatorReply> kafkaTemplate;

    public CalculatorControllerBuilder setKafkaTemplate(ReplyingKafkaTemplate<String, CalculatorRequest, CalculatorReply> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
        return this;
    }

    public CalculatorController createCalculatorController() {
        return new CalculatorController(kafkaTemplate);
    }
}