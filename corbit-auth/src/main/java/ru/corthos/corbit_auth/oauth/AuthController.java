package ru.corthos.corbit_auth.oauth;

import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@Profile("oauth")
@RestController
public class AuthController {


    @GetMapping("/")
    public String home() {
        return "<h1>Главная</h1><a href='/oauth2/authorization/github'>Войти через GitHub</a>";
    }

    @GetMapping("/auth")
    @ResponseBody
    public ResponseEntity<String> auth(Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @GetMapping("/profile")
    public String profile(@AuthenticationPrincipal OAuth2User user) {
        return "<h1>Привет, " + user.getAttribute("login") + "!</h1>" +
                "<img src='" + user.getAttribute("avatar_url") + "' width='80'/><br>" +
                "<a href='/logout'>Выйти</a>";
    }

}
