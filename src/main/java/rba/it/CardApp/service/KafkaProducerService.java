package rba.it.CardApp.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import rba.it.CardApp.dto.CardStatusMessage;

@Service
public class KafkaProducerService {

    private final KafkaTemplate<String, CardStatusMessage> kafkaTemplate;
    private final String topicName;

    public KafkaProducerService(KafkaTemplate<String, CardStatusMessage> kafkaTemplate,
                                @Value("${kafka.topic.name}") String topicName) {
        this.kafkaTemplate = kafkaTemplate;
        this.topicName = topicName;
    }

    public void sendMessage(CardStatusMessage message) {
        kafkaTemplate.send(topicName, message);
    }
}
