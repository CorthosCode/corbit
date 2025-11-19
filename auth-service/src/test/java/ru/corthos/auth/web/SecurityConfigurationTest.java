package ru.corthos.auth.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Import(SecurityTestConfig.class)
@ImportAutoConfiguration(exclude = OAuth2ClientAutoConfiguration.class)
public class SecurityConfigurationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testAuthPageWithoutAuthentication() throws Exception {
        // Пользователь не аутентифицирован
        // Должно перенаправить на login
        mockMvc.perform(get("/auth-page"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/login"));
    }

    @Test
    public void testProfileWithoutAuthentication() throws Exception {
        // Пользователь не аутентифицирован
        // Должно перенаправить на login
        mockMvc.perform(get("/profile"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/login"));
    }

    @Test
    public void testAuthCheckWithoutAuthentication() throws Exception {
        // Пользователь не аутентифицирован
        // Проверяем, что доступ к /auth-check разрешен всем
        // Контроллер возвращает 401, если authentication == null
        mockMvc.perform(get("/auth-check"))
                .andExpect(status().isUnauthorized());
    }


    @Test
    public void testAnyOtherRequestWithoutAuthentication() throws Exception {
        // Пользователь не аутентифицирован
        // Проверяем, что доступ к любому другому маршруту требует аутентификации
        // и перенаправляет на /login
        mockMvc.perform(get("/some-random-path"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/login"));
    }

    @Test
    @WithMockUser
    public void testAnyOtherRequestWithAuthenticationButNoRights() throws Exception {
        // Пользователь аутентифицирован
        // Любой ендпоинт
        // Контроллер возвращает 401
        mockMvc.perform(get("/some-random-path"))
                .andExpect(status().isForbidden());
    }


    @Test
    @WithMockUser
    public void testAuthPageWithAuthentication() throws Exception {
        // Пользователь аутентифицирован
        // Проверяем, что аутентифицированный пользователь может получить доступ к /auth-page
        // Контроллер возвращает 302 на http://nginx-service:9093/
        mockMvc.perform(get("/auth-page"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://nginx-service:9093/"));
    }


    @Test
    @WithMockUser
    public void testAuthCheckWithAuthentication() throws Exception {
        // Пользователь аутентифицирован
        // Проверяем, что аутентифицированный пользователь может получить доступ к /auth-check
        // Контроллер возвращает 200, если authentication != null и isAuthenticated()
        mockMvc.perform(get("/auth-check"))
                .andExpect(status().isOk());
    }


    @Test
    @WithMockUser
    public void testLogout() throws Exception {
        // CSRF отключен в SecurityTestConfig, но Spring Security требует его для /logout по умолчанию
        // Проверяем перенаправление после logout
        mockMvc.perform(post("/logout")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
    }

    @Test
    public void testLogoutWithoutAuthentication() throws Exception {
        // Даже неаутентифицированный пользователь может отправить logout
        mockMvc.perform(post("/logout")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
    }

}