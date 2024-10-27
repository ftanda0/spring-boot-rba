package rba.it.CardApp.simulator;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import rba.it.CardApp.dto.CardStatusMessage;

@Component
public class ExternalSystemSimulator {

    private final KafkaTemplate<String, CardStatusMessage> kafkaTemplate;
    private final String cardStatusUpdatesTopic;

    public ExternalSystemSimulator(
            KafkaTemplate<String, CardStatusMessage> kafkaTemplate,
            @Value("${kafka.topic.card-status-updates}") String cardStatusUpdatesTopic) {
        this.kafkaTemplate = kafkaTemplate;
        this.cardStatusUpdatesTopic = cardStatusUpdatesTopic;
    }

    public void sendCardStatusUpdate(String oib, String status) {
        CardStatusMessage message = new CardStatusMessage(oib, status);
        kafkaTemplate.send(cardStatusUpdatesTopic, message);
        System.out.println("Poslana poruka: " + message);
    }
}
