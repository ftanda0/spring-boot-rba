package rba.it.CardApp.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import rba.it.CardApp.dto.CardStatusMessage;
import rba.it.CardApp.exception.CustomExceptions;
import rba.it.CardApp.model.CardStatus;
import rba.it.CardApp.model.Client;
import rba.it.CardApp.repository.ClientRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ClientServiceTest {

    @InjectMocks
    private ClientService clientService;

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private KafkaProducerService producerService;

    private Client client;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        client = new Client();
        client.setFirstName("Ana");
        client.setLastName("Kovačić");
        client.setOib("12345678901");
        client.setStatus(CardStatus.valueOf("PENDING"));
    }

    @Test
    void createClient_Success() {
        when(clientRepository.existsByOib(client.getOib())).thenReturn(false);
        when(clientRepository.save(client)).thenReturn(client);

        Client createdClient = clientService.createClient(client);

        assertNotNull(createdClient);
        assertEquals("Ana", createdClient.getFirstName());
        verify(clientRepository, times(1)).save(client);
        // Ako je Kafka producer aktivan, provjeri da li je poslan message
        // verify(producerService, times(1)).sendMessage(any(CardStatusMessage.class));
    }

    @Test
    void createClient_InvalidOib() {
        client.setOib("12345"); // Neispravan OIB

        Exception exception = assertThrows(CustomExceptions.BadRequestException.class, () -> {
            clientService.createClient(client);
        });

        String expectedMessage = "OIB must contain 11 digits.";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
        verify(clientRepository, never()).save(any(Client.class));
    }

    @Test
    void createClient_OibAlreadyExists() {
        when(clientRepository.existsByOib(client.getOib())).thenReturn(true);

        Exception exception = assertThrows(CustomExceptions.ConflictException.class, () -> {
            clientService.createClient(client);
        });

        String expectedMessage = "OIB already exists.";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
        verify(clientRepository, never()).save(any(Client.class));
    }

    @Test
    void getClientByOib_Success() {
        when(clientRepository.findByOib("12345678901")).thenReturn(Optional.of(client));

        Client foundClient = clientService.getClientByOib("12345678901");

        assertNotNull(foundClient);
        assertEquals("Ana", foundClient.getFirstName());
        verify(clientRepository, times(1)).findByOib("12345678901");
    }

    @Test
    void getClientByOib_NotFound() {
        when(clientRepository.findByOib("12345678901")).thenReturn(Optional.empty());

        Exception exception = assertThrows(CustomExceptions.NotFoundException.class, () -> {
            clientService.getClientByOib("12345678901");
        });

        String expectedMessage = "Client with OIB 12345678901 not found.";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void deleteClientByOib_Success() {
        when(clientRepository.existsByOib("12345678901")).thenReturn(true);
        doNothing().when(clientRepository).deleteByOib("12345678901");

        assertDoesNotThrow(() -> {
            clientService.deleteClientByOib("12345678901");
        });

        verify(clientRepository, times(1)).deleteByOib("12345678901");
    }

    @Test
    void deleteClientByOib_NotFound() {
        when(clientRepository.existsByOib("12345678901")).thenReturn(false);

        Exception exception = assertThrows(CustomExceptions.NotFoundException.class, () -> {
            clientService.deleteClientByOib("12345678901");
        });

        String expectedMessage = "Client with OIB 12345678901 not found.";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
        verify(clientRepository, never()).deleteByOib(anyString());
    }
}
