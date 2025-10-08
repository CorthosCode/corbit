package ru.corthos.corbit_auth;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    @GetMapping("/auth")
    public ResponseEntity<String> auth() {
        return ResponseEntity.ok("auth ok");
    }

}
