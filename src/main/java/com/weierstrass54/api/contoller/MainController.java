package com.weierstrass54.api.contoller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {
    @GetMapping("/")
    public String root() {
        return "Ok";
    }

    @GetMapping("/hello/{name}")
    public String hello(@PathVariable("name") String name) {
        return "Hello, " + name;
    }

    @GetMapping("/bad_request")
    public ResponseEntity<String> badRequest() {
        return ResponseEntity.badRequest().build();
    }

    @GetMapping("/unathorized")
    public ResponseEntity<String> unathorized() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @GetMapping("/internal_error")
    public String internalError() {
        throw new IllegalArgumentException("Something wrong.");
    }
}
