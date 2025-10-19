package ru.corthos.corbit_auth.keycloak;

import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@Profile("keycloak")
@RestController
public class AuthController {

    @GetMapping("/auth-page")
    public ResponseEntity<String> auth() {
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create("http://nginx-service:9093/auth"));
        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }

    @GetMapping("/auth-check")
    public ResponseEntity<String> auth(Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @GetMapping("/profile")
    public String profile(@AuthenticationPrincipal OAuth2User user) {
        return user.toString();
    }

}
