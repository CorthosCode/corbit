package ru.corthos.corbit_auth.basic;

import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Profile("basic")
@RestController
public class AuthController {

    @GetMapping("/auth")
    public ResponseEntity<String> auth() {
        return ResponseEntity.ok("auth ok");
    }

}
