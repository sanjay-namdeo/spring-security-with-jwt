package com.learn.security.springsecuritywithjwt;

import com.learn.security.springsecuritywithjwt.pojo.LoginRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SpringSecurityWithJwtApplicationTests {
    @Value("${local.server.port}")
    private int port;

    @Test
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

}
