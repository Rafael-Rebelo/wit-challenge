package pt.rafael_rebelo.rest.controller;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.requestreply.RequestReplyFuture;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.web.bind.annotation.*;
import pt.rafael_rebelo.wit_challenge.models.CalculatorRequest;
import pt.rafael_rebelo.wit_challenge.models.CalculatorReply;

import java.math.BigDecimal;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/")
public class CalculatorController {

    private static final String REQUEST_TOPIC = "request";
    private static final String REPLY_TOPIC = "reply";

    private static final Logger logger = LoggerFactory.getLogger(CalculatorController.class);

    private final ReplyingKafkaTemplate<String, CalculatorRequest, CalculatorReply> kafkaTemplate;

    @Bean
    public KafkaMessageListenerContainer<String, CalculatorReply> replyContainer(ConsumerFactory<String, CalculatorReply> cf) {
        return new KafkaMessageListenerContainer<>(cf, new ContainerProperties(REPLY_TOPIC));
    }

    @Bean
    public ReplyingKafkaTemplate<String, CalculatorRequest, CalculatorReply> replyKafkaTemplate(
            ProducerFactory<String, CalculatorRequest> pf,
            KafkaMessageListenerContainer<String, CalculatorReply> container) {
        return new ReplyingKafkaTemplate<>(pf, container);
    }

    @Autowired
    public CalculatorController(ReplyingKafkaTemplate<String, CalculatorRequest, CalculatorReply> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    private ResponseEntity<String> processRequestReply(CalculatorRequest request) {
        ProducerRecord<String, CalculatorRequest> producerRecord = new ProducerRecord<>(REQUEST_TOPIC, request);
        producerRecord.headers().add(new RecordHeader(KafkaHeaders.REPLY_TOPIC, REPLY_TOPIC.getBytes()));

        RequestReplyFuture<String, CalculatorRequest, CalculatorReply> sendAndReceive = kafkaTemplate.sendAndReceive(producerRecord);

        try {
            sendAndReceive.getSendFuture().get();
        } catch (CancellationException ce) {
            String error = "Request was cancelled.";
            logger.error(error, ce);
            return ResponseEntity.internalServerError().body(error);
        } catch (ExecutionException ee) {
            logger.error("Request was aborted: {}", ee.getCause(), ee);
            return ResponseEntity.internalServerError().body("Request was aborted exceptionally.");
        } catch (InterruptedException ie) {
            String error = "Request was interrupted.";
            logger.error(error, ie);
            return ResponseEntity.internalServerError().body(error);
        }

        ConsumerRecord<String, CalculatorReply> consumerRecord;
        try {
            consumerRecord = sendAndReceive.get();
        } catch (CancellationException ce) {
            String error = "Reply was cancelled.";
            logger.error(error, ce);
            return ResponseEntity.internalServerError().body(error);
        } catch (ExecutionException ee) {
            logger.error("Reply was aborted: {}", ee.getCause(), ee);
            return ResponseEntity.internalServerError().body("Reply was aborted exceptionally.");
        } catch (InterruptedException ie) {
            String error = "Reply was interrupted.";
            logger.error(error, ie);
            return ResponseEntity.internalServerError().body(error);
        }

        CalculatorReply reply = consumerRecord.value();

        return (reply.getError() == null) ? ResponseEntity.ok(reply.getResult().toString())
                : ResponseEntity.internalServerError().body(reply.getError());
    }

    @GetMapping("/sum")
    public ResponseEntity<String> sum(@RequestParam("a") BigDecimal a, @RequestParam("b") BigDecimal b) {
        CalculatorRequest request = new CalculatorRequest("sum", a, b);

        return processRequestReply(request);
    }

    @GetMapping("/subtract")
    public ResponseEntity<String> subtract(@RequestParam("a") BigDecimal a, @RequestParam("b") BigDecimal b) {
        CalculatorRequest request = new CalculatorRequest("subtract", a, b);

        return processRequestReply(request);
    }

    @GetMapping("/multiply")
    public ResponseEntity<String> multiply(@RequestParam("a") BigDecimal a, @RequestParam("b") BigDecimal b) {
        CalculatorRequest request = new CalculatorRequest("multiply", a, b);

        return processRequestReply(request);
    }

    @GetMapping("/divide")
    public ResponseEntity<String> divide(@RequestParam("a") BigDecimal a, @RequestParam("b") BigDecimal b) {
        CalculatorRequest request = new CalculatorRequest("divide", a, b);

        return processRequestReply(request);
    }
}
