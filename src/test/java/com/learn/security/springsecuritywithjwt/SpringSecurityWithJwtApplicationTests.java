package com.learn.security.springsecuritywithjwt;

import com.learn.security.springsecuritywithjwt.pojo.LoginRequest;
import com.learn.security.springsecuritywithjwt.pojo.LoginResponse;
import com.learn.security.springsecuritywithjwt.pojo.SignupRequest;
import com.learn.security.springsecuritywithjwt.pojo.SignupResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SpringSecurityWithJwtApplicationTests {
    @Value("${local.server.port}")
    private int port;

    @Test
    @DisplayName("Test /login with correct details")
    void Should_ReturnJWTToken_When_CorrectCredentialsProvided() {
        WebTestClient.bindToServer().baseUrl("http://localhost:" + port).build().post().uri("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(new LoginRequest("sanjay", "12345")), LoginRequest.class)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.token").isNotEmpty();
    }

    @Test
    @DisplayName("Test /login with incorrect details")
    void Should_NotReturnJWTToken_When_InCorrectCredentialsProvided() {
        WebTestClient.bindToServer().baseUrl("http://localhost:" + port).build().post().uri("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(new LoginRequest("sanjay", "123456")), LoginRequest.class)
                .exchange()
                .expectStatus().isUnauthorized()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.token").doesNotExist();
    }

    @Test
    @DisplayName("Test /hello when logged in")
    void Should_Return_InputString_WhenLoggedIn() {
        WebClient webClient = WebClient.builder().baseUrl("http://localhost:" + port).build();

        LoginResponse loginResponse = webClient.post()
                .uri("/login")
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(new LoginRequest("sanjay", "12345")), LoginRequest.class)
                .retrieve()
                .bodyToMono(LoginResponse.class)
                .block();

        assert loginResponse != null;
        WebTestClient.bindToServer().baseUrl("http://localhost:" + port)
                .build()
                .get()
                .uri(uriBuilder -> uriBuilder.path("/hello").queryParam("name", "Sanjay").build())
                .accept(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + loginResponse.getToken())
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(String.class).isEqualTo("Hello Sanjay");
    }

    @Test
    @DisplayName("Test /hello when not logged in")
    void Should_NotReturn_InputString_WhenNotLoggedIn() {
        WebTestClient.bindToServer().baseUrl("http://localhost:" + port)
                .build()
                .get()
                .uri(uriBuilder -> uriBuilder.path("/hello").queryParam("name", "Sanjay").build())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isForbidden();
    }

    @Test
    @DisplayName("Test /signup with correct details")
    void Should_SignupUser_When_CorrectCredentialsProvided() {
        SignupRequest signupRequest = SignupRequest.build("Sanjay Namdeo", "1234567890", "test@test.com", "sanjaynamdeo", "12345");

        WebTestClient.bindToServer().baseUrl("http://localhost:" + port)
                .build()
                .post()
                .uri("/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(signupRequest), SignupRequest.class)
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(SignupResponse.class)
                .isEqualTo(SignupResponse.build("Sanjay Namdeo", "sanjaynamdeo", "test@test.com", "1234567890"));
    }

    @Test
    @DisplayName("Test /signup with incorrect details")
    void Should_NotSignupUser_When_CorrectCredentialsProvided() {
        SignupRequest signupRequest = SignupRequest.build(null, null, null, null, null);

        WebTestClient.bindToServer().baseUrl("http://localhost:" + port)
                .build()
                .post()
                .uri("/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(signupRequest), SignupRequest.class)
                .exchange()
                .expectStatus().isBadRequest()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.name").isEqualTo("Please enter your name")
                .jsonPath("$.mobile").isEqualTo("Please enter a valid phone number")
                .jsonPath("$.email").isEqualTo("Please enter a valid email address")
                .jsonPath("$.username").isEqualTo("Please enter a username")
                .jsonPath("$.password").isEqualTo("Please enter password");
    }
}
