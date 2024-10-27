package rba.it.CardApp.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import rba.it.CardApp.model.Client;
import rba.it.CardApp.service.ClientService;



@RestController
@RequestMapping("/api/v1/card-request")

public class ClientController {

    private final ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }


    @PreAuthorize("hasAuthority('SCOPE_write:clients')")
    @PostMapping
    public ResponseEntity<String> createClient(@RequestBody Client client) {
        Client createdClient = clientService.createClient(client);
        return ResponseEntity.status(201).body("New card request successfully created.");
    }

    @PreAuthorize("hasAuthority('SCOPE_read:clients')")
    @GetMapping("/{oib}")
    public ResponseEntity<Client> getClientByOib(@PathVariable String oib) {
        Client client = clientService.getClientByOib(oib);
        return ResponseEntity.ok(client);
    }

    @PreAuthorize("hasAuthority('SCOPE_delete:clients')")
    @DeleteMapping("/{oib}")
    public ResponseEntity<Void> deleteClientByOib(@PathVariable String oib) {
        clientService.deleteClientByOib(oib);
        return ResponseEntity.noContent().build();
    }
}
