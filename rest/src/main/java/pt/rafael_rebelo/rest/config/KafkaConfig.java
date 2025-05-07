package pt.rafael_rebelo.rest.config;

import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import pt.rafael_rebelo.wit_challenge.models.CalculatorReply;
import pt.rafael_rebelo.wit_challenge.models.CalculatorRequest;

public class KafkaConfig {

    @Bean
    public ReplyingKafkaTemplate<String, CalculatorRequest, CalculatorReply> replyKafkaTemplate(
            ProducerFactory<String, CalculatorRequest> pf,
            KafkaMessageListenerContainer<String, CalculatorReply> container) {
        return new ReplyingKafkaTemplate<>(pf, container);
    }

}
