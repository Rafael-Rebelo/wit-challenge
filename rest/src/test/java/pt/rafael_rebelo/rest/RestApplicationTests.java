package pt.rafael_rebelo.rest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
//@DirtiesContext
//@EmbeddedKafka(brokerProperties = { "listeners=PLAINTEXT://localhost:9092", "port=9092" },
//        topics = {"request", "reply"})
//@AutoConfigureMockMvc
class RestApplicationTests {
//
//    private static final String REQUEST_TOPIC = "request";
//    private static final String REPLY_TOPIC = "reply";
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Autowired
//    private EmbeddedKafkaBroker broker;
//
//

    @Test
    void contextLoads() {
    }

}
