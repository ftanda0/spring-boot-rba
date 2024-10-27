package rba.it.CardApp.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import rba.it.CardApp.dto.CardStatusMessage;
import rba.it.CardApp.exception.CustomExceptions;
import rba.it.CardApp.model.Client;
import rba.it.CardApp.repository.ClientRepository;

import java.util.Optional;

@Service
public class ClientService {

    private final ClientRepository clientRepository;
    private final KafkaProducerService producerService;
    private final ObjectMapper objectMapper;

    public ClientService(ClientRepository clientRepository, KafkaProducerService producerService) {
        this.clientRepository = clientRepository;
        this.producerService = producerService;
        this.objectMapper = new ObjectMapper();
    }

    public Client createClient(Client client) {

        if (client.getOib().length() != 11) {
            throw new CustomExceptions.BadRequestException("OIB must contain 11 digits.");
        }

        if (clientRepository.existsByOib(client.getOib())) {
            throw new CustomExceptions.ConflictException("OIB already exists.");
        }


        Client savedClient = clientRepository.save(client);

        CardStatusMessage message = new CardStatusMessage(savedClient.getOib(), savedClient.getStatus().toString());



        producerService.sendMessage(message);

        return savedClient;
    }

    public Client getClientByOib(String oib) {
        Optional<Client> client = clientRepository.findByOib(oib);
        if (client.isEmpty()) {
            throw new CustomExceptions.NotFoundException("Client with OIB " + oib + " not found.");
        }
        return client.get();
    }

    @Transactional
    public void deleteClientByOib(String oib) {
        System.out.println("Attempting to delete client with OIB: " + oib);
        if (!clientRepository.existsByOib(oib)) {
            throw new CustomExceptions.NotFoundException("Client with OIB " + oib + " not found.");
        }
        System.out.println("Client found. Proceeding to delete.");
        clientRepository.deleteByOib(oib);
        System.out.println("Client with OIB " + oib + " successfully deleted.");
    }


}
