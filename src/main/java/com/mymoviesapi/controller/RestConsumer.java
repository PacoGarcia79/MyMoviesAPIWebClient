package com.mymoviesapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api")
public class RestConsumer {

	private static final String MAIN_ENDPOINT = "https://api.themoviedb.org/3/";

	@Autowired
	RestTemplate restTemplate;

	@Autowired
	HttpEntity request;

	@GetMapping("/movie/popular")
	public ResponseEntity<String> getPopularMovies() {

		String resourceUrl = MAIN_ENDPOINT + "/movie/popular";

		return restTemplate.exchange(resourceUrl, HttpMethod.GET, request, String.class);
	}

	@GetMapping("/movie/top_rated")
	public ResponseEntity<String> getTopRatedMovies() {

		String resourceUrl = MAIN_ENDPOINT + "/movie/top_rated";

		return restTemplate.exchange(resourceUrl, HttpMethod.GET, request, String.class);

	}

	@GetMapping("/movie/{movie_id}")
	public ResponseEntity<String> getMovie(@PathVariable("movie_id") int movieId) {

		String resourceUrl = MAIN_ENDPOINT + "/movie/" + movieId;

		return restTemplate.exchange(resourceUrl, HttpMethod.GET, request, String.class);
	}

	@GetMapping("/movie/{movie_id}/credits")
	public ResponseEntity<String> getMovieCredits(@PathVariable("movie_id") int movieId) {

		String resourceUrl = MAIN_ENDPOINT + "/movie/" + movieId + "/credits";

		return restTemplate.exchange(resourceUrl, HttpMethod.GET, request, String.class);
	}

	@GetMapping("/movie/{movie_id}/images")
	public ResponseEntity<String> getMovieImages(@PathVariable("movie_id") int movieId) {

		String resourceUrl = MAIN_ENDPOINT + "/movie/" + movieId + "/images";

		return restTemplate.exchange(resourceUrl, HttpMethod.GET, request, String.class);
	}

	@GetMapping("/movie/{movie_id}/keywords")
	public ResponseEntity<String> getMovieKeyWords(@PathVariable("movie_id") int movieId) {

		String resourceUrl = MAIN_ENDPOINT + "/movie/" + movieId + "/keywords";

		return restTemplate.exchange(resourceUrl, HttpMethod.GET, request, String.class);
	}
	
	@GetMapping("/movie/{movie_id}/recommendations")
	public ResponseEntity<String> getMovieRecommendations(@PathVariable("movie_id") int movieId) {

		String resourceUrl = MAIN_ENDPOINT + "/movie/" + movieId + "/recommendations";

		return restTemplate.exchange(resourceUrl, HttpMethod.GET, request, String.class);
	}
	
	@GetMapping("/movie/{movie_id}/similar")
	public ResponseEntity<String> getSimilarMovies(@PathVariable("movie_id") int movieId) {

		String resourceUrl = MAIN_ENDPOINT + "/movie/" + movieId + "/similar";

		return restTemplate.exchange(resourceUrl, HttpMethod.GET, request, String.class);
	}

}