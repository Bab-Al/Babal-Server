package BabAl.BabalServer.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthCheckController {

    // AWS EB Health Check
    @GetMapping("/health")
    public String healthCheck() {
        return "I'm healthy";
    }
}
