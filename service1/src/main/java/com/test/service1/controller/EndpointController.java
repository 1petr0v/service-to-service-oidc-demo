package com.test.service1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@RestController
public class EndpointController {

    private final WebClient webclient;

    @Autowired
    public EndpointController(final WebClient webclient) {
        this.webclient = webclient;
    }

    @PreAuthorize("hasAuthority('SCOPE_service1:resource1')")
    @GetMapping("/resource1")
    public String resource1() {
        return "Service1.Resource1";
    }

    @PreAuthorize("hasAuthority('SCOPE_service1:resource2')")
    @GetMapping("/resource2")
    public String resource2() {
        return "Service1.Resource2";
    }

    @GetMapping("/public/jump/{toService}/{toResource}")
    public String jumpEndpoint(@PathVariable("toService") final String toService,
                               @PathVariable("toResource") final String toResource) {
        final String service = switch (toService) {
            case "service2" -> "http://localhost:10002";
            case "service3" -> "http://localhost:10003";
            default -> "none";
        };

        if ("none".equals(service)) {
            return "Not able to call that service. Try /public/jump/service2/resource1";
        }

        final String calleeUrl = service + "/" + toResource;
        final String response1 = webclient.get().uri(calleeUrl)
                .exchangeToMono(x -> {
                    final int rawStatusCode = x.rawStatusCode();
                    return switch (rawStatusCode) {
                        case 401, 403 -> Mono.just("Access denied");
                        case 200 -> x.bodyToMono(String.class);
                        default -> Mono.just("Some other error. Status: " + rawStatusCode);
                    };
                }).block();

        return "Service 1 called " + calleeUrl + " and got: " + response1;
    }

}
