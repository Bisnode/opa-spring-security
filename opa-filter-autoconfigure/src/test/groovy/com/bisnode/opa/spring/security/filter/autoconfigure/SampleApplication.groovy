package com.bisnode.opa.spring.security.filter.autoconfigure


import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@SpringBootApplication
@RestController
class SampleApplication {
    static void main(String[] args) {
        SpringApplication.run(SampleApplication, args);
    }

    @GetMapping
    void controllerMethod() {
    }
}
