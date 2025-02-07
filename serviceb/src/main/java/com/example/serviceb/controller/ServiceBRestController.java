package com.example.serviceb.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;

@RestController
@Slf4j
public class ServiceBRestController {

    private final RestClient restClient;

    public ServiceBRestController(RestClient.Builder restClientBuilder) {
        restClient = restClientBuilder.build();
    }

    @GetMapping("helloEureka")
    public String helloWorld() {
        log.info("helloEureka triggered on service B");
        final var response = restClient.get()
            .uri("http://servicea/helloWorld")
            .retrieve()
            .body(String.class);
        log.info("Response: " + response);
        return response;
    }

}
