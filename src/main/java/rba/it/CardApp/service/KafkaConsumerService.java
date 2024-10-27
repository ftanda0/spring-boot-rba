package rba.it.CardApp.service;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import rba.it.CardApp.dto.CardStatusMessage;
import rba.it.CardApp.model.CardStatus;
import rba.it.CardApp.model.Client;
import rba.it.CardApp.repository.ClientRepository;

import java.util.Optional;

@Service
public class KafkaConsumerService {

    private final ClientRepository clientRepository;

    public KafkaConsumerService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @KafkaListener(topics = "${kafka.topic.card-status-updates}", groupId = "card_status_group", containerFactory = "cardStatusKafkaListenerContainerFactory")
    public void consumeCardStatusUpdate(CardStatusMessage message) {
        System.out.println("Primljena poruka: " + message);

        // Pronalaženje klijenta po OIB-u
        Optional<Client> optionalClient = clientRepository.findByOib(message.getOib());

        if (optionalClient.isPresent()) {
            Client client = optionalClient.get();
            // Ažuriranje statusa
            client.setStatus(CardStatus.valueOf(message.getStatus()));
            clientRepository.save(client);
            System.out.println("Status klijenta ažuriran na: " + message.getStatus());
        } else {
            System.out.println("Klijent s OIB-om " + message.getOib() + " nije pronađen.");
        }
    }
}
