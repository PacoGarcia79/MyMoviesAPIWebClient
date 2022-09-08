package com.mymoviesapi;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.mymoviesapi.controller.MovieController;
import com.mymoviesapi.h2jdbc.MovieService;

//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@AutoConfigureWebTestClient
@WebFluxTest(controllers = MovieController.class)
@Import(MovieService.class)
class MyMoviesApiTests {
	
	@Autowired
    private WebTestClient webTestClient;
	
	@Test //NO FUNCIONA POR LA AUTENTIFICACIÓN
	@WithMockUser(username = "user", password = "5678")
    void movie() {
        webTestClient.get()
                .uri("/api/movie/550")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().valueEquals("Content-Type", "application/json");
    }
	
	
	@Test
	@WithMockUser(username = "user", password = "5678")
    void popular() throws Exception {
				
        webTestClient.get()
                .uri("/api/movie/popular")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().valueEquals("Content-Type", "application/json");  

    }
	
	@Test
	@WithMockUser(username = "user", password = "5678")
    void topRated() {
        webTestClient.get()
                .uri("/api/movie/top_rated")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().valueEquals("Content-Type", "application/json");
    }
	
	@Test
	@WithMockUser(username = "user", password = "5678")
    void credits() {
        webTestClient.get()
                .uri("/api/movie/550/credits")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().valueEquals("Content-Type", "application/json");
    }
	
	@Test
	@WithMockUser(username = "user", password = "5678")
    void images() {
        webTestClient.get()
                .uri("/api/movie/550/images")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().valueEquals("Content-Type", "application/json");
    }
	
	@Test
	@WithMockUser(username = "user", password = "5678")
    void keywords() {
        webTestClient.get()
                .uri("/api/movie/550/keywords")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().valueEquals("Content-Type", "application/json");
    }
	
	@Test
	@WithMockUser(username = "user", password = "5678")
    void recommendations() {
        webTestClient.get()
                .uri("/api/movie/550/recommendations")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().valueEquals("Content-Type", "application/json");
    }
	
	@Test
	@WithMockUser(username = "user", password = "5678")
    void similar() {
        webTestClient.get()
                .uri("/api/movie/550/similar")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().valueEquals("Content-Type", "application/json");
    }
	
}


