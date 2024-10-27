package rba.it.CardApp.controller;

import org.springframework.web.bind.annotation.*;
import rba.it.CardApp.simulator.ExternalSystemSimulator;

@RestController
@RequestMapping("/simulate")
public class SimulationController {

    private final ExternalSystemSimulator simulator;

    public SimulationController(ExternalSystemSimulator simulator) {
        this.simulator = simulator;
    }

    @PostMapping("/status-update")
    public String simulateStatusUpdate(@RequestParam String oib, @RequestParam String status) {
        simulator.sendCardStatusUpdate(oib, status);
        return "Simulacija poslana.";
    }
}
