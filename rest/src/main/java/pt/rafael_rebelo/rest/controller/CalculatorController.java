package pt.rafael_rebelo.rest.controller;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.requestreply.RequestReplyFuture;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.kafka.support.SendResult;
import org.springframework.web.bind.annotation.*;
import pt.rafael_rebelo.wit_challenge.models.CalculatorRequest;
import pt.rafael_rebelo.wit_challenge.models.CalculatorReply;

import java.math.BigDecimal;

@RestController
@RequestMapping("/")
public class CalculatorController {

    private static final String REQUEST_TOPIC = "request";
    private static final String REPLY_TOPIC = "reply";

    private static final Logger logger = LoggerFactory.getLogger(CalculatorController.class);

    private final ReplyingKafkaTemplate<String, CalculatorRequest, CalculatorReply> kafkaTemplate;

    @Autowired
    public CalculatorController(ReplyingKafkaTemplate<String, CalculatorRequest, CalculatorReply> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @GetMapping("/sum")
    public ResponseEntity<String> sum(@RequestParam("a") BigDecimal a, @RequestParam("b") BigDecimal b) {
        CalculatorRequest request = new CalculatorRequest("sum", a, b);

        ProducerRecord<String, CalculatorRequest> record = new ProducerRecord<>(REQUEST_TOPIC, request);
        record.headers().add(new RecordHeader(KafkaHeaders.REPLY_TOPIC, REPLY_TOPIC.getBytes()));

        RequestReplyFuture<String, CalculatorRequest, CalculatorReply> sendAndReceive = kafkaTemplate.sendAndReceive(record);

        SendResult<String, CalculatorRequest> sendResult = sendAndReceive.getSendFuture().get();



        return ResponseEntity.ok("ok");
    }

    @GetMapping("/subtract")
    public ResponseEntity<String> subtract(@RequestParam("a") BigDecimal a, @RequestParam("b") BigDecimal b) {
        CalculatorRequest request = new CalculatorRequest("subtract", a, b);



        return ResponseEntity.ok("ok");
    }

    @GetMapping("/multiply")
    public ResponseEntity<String> multiply(@RequestParam("a") BigDecimal a, @RequestParam("b") BigDecimal b) {
        CalculatorRequest request = new CalculatorRequest("multiply", a, b);



        return ResponseEntity.ok("ok");
    }

    @GetMapping("/divide")
    public ResponseEntity<String> divide(@RequestParam("a") BigDecimal a, @RequestParam("b") BigDecimal b) {
        CalculatorRequest request = new CalculatorRequest("divide", a, b);



        return ResponseEntity.ok("ok");
    }
}
