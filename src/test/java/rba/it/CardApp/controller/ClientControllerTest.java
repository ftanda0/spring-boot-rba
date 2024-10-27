package rba.it.CardApp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import rba.it.CardApp.exception.CustomExceptions;
import rba.it.CardApp.exception.GlobalExceptionHandler;
import rba.it.CardApp.model.CardStatus;
import rba.it.CardApp.model.Client;
import rba.it.CardApp.service.ClientService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = ClientController.class)
@Import({GlobalExceptionHandler.class, TestMethodSecurityConfig.class}) // Uvezi GlobalExceptionHandler i TestMethodSecurityConfig
class ClientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ClientService clientService;

    @Autowired
    private ObjectMapper objectMapper;

    private Client client;

    @BeforeEach
    void setUp() {
        client = new Client();
        client.setFirstName("Ana");
        client.setLastName("Kovačić");
        client.setOib("12345678901");
        client.setStatus(CardStatus.valueOf("PENDING"));
    }

    @Test
    @WithMockUser(authorities = {"SCOPE_write:clients"})
    void createClient_Success() throws Exception {
        when(clientService.createClient(any(Client.class))).thenReturn(client);

        mockMvc.perform(post("/api/v1/card-request")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(client)))
                .andExpect(status().isCreated())
                .andExpect(content().string("New card request successfully created."));

        verify(clientService, times(1)).createClient(any(Client.class));
    }

    @Test
    @WithMockUser(authorities = {"SCOPE_write:clients"})
    void createClient_InvalidOib() throws Exception {
        client.setOib("12345"); // Neispravan OIB
        when(clientService.createClient(any(Client.class)))
                .thenThrow(new CustomExceptions.BadRequestException("OIB must contain 11 digits."));

        mockMvc.perform(post("/api/v1/card-request")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(client)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("description").value("OIB must contain 11 digits."));

        verify(clientService, times(1)).createClient(any(Client.class));
    }

    @Test
    @WithMockUser(authorities = {"SCOPE_read:clients"})
    void getClientByOib_Success() throws Exception {
        when(clientService.getClientByOib("12345678901")).thenReturn(client);

        mockMvc.perform(get("/api/v1/card-request/12345678901")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Ana"))
                .andExpect(jsonPath("$.lastName").value("Kovačić"))
                .andExpect(jsonPath("$.oib").value("12345678901"))
                .andExpect(jsonPath("$.status").value("PENDING"));

        verify(clientService, times(1)).getClientByOib("12345678901");
    }

    @Test
    @WithMockUser(authorities = {"SCOPE_read:clients"})
    void getClientByOib_NotFound() throws Exception {
        when(clientService.getClientByOib("12345678901"))
                .thenThrow(new CustomExceptions.NotFoundException("Client with OIB 12345678901 not found."));

        mockMvc.perform(get("/api/v1/card-request/12345678901")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.description").value("Client with OIB 12345678901 not found."));

        verify(clientService, times(1)).getClientByOib("12345678901");
    }

    @Test
    @WithMockUser(authorities = {"SCOPE_delete:clients"})
    void deleteClientByOib_Success() throws Exception {
        doNothing().when(clientService).deleteClientByOib("12345678901");

        mockMvc.perform(delete("/api/v1/card-request/12345678901")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(clientService, times(1)).deleteClientByOib("12345678901");
    }

    @Test
    @WithMockUser(authorities = {"SCOPE_delete:clients"})
    void deleteClientByOib_NotFound() throws Exception {
        doThrow(new CustomExceptions.NotFoundException("Client with OIB 12345678901 not found."))
                .when(clientService).deleteClientByOib("12345678901");

        mockMvc.perform(delete("/api/v1/card-request/12345678901")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.description").value("Client with OIB 12345678901 not found."));

        verify(clientService, times(1)).deleteClientByOib("12345678901");
    }


    @Test
    @WithMockUser(authorities = {"SCOPE_write:clients"})
    void getClientByOib_Forbidden() throws Exception {
        // Korisnik ima SCOPE_write:clients, ali endpoint možda zahtijeva drugačiji scope
        when(clientService.getClientByOib("12345678901")).thenReturn(client);

        mockMvc.perform(get("/api/v1/card-request/12345678901")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());

        verify(clientService, never()).getClientByOib("12345678901");
    }

    @Test
    @WithMockUser(authorities = {"SCOPE_read:clients"}) // Nedostatak SCOPE_delete:clients za DELETE
    void deleteClientByOib_Forbidden() throws Exception {
        doNothing().when(clientService).deleteClientByOib("12345678901");

        mockMvc.perform(delete("/api/v1/card-request/12345678901")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());

        verify(clientService, never()).deleteClientByOib("12345678901");
    }

}
