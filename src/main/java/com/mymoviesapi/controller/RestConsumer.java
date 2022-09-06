package com.mymoviesapi.controller;

import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mymoviesapi.h2jdbc.H2JDBCService;
import com.mymoviesapi.model.UserMovie;

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
	public ResponseEntity<JsonObject> getMovie(@PathVariable String movie_id) throws SQLException {
		String resourceUrl = MAIN_ENDPOINT + "/movie/" + movie_id;

		ResponseEntity<String> responseEntity = restTemplate.exchange(resourceUrl, HttpMethod.GET, request,
				String.class);
		String jsonResponse = responseEntity.getBody();

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (!(authentication instanceof AnonymousAuthenticationToken)) {
			String currentUserName = authentication.getName();
			int userId = H2JDBCService.getAuthenticatedUserId(currentUserName);
			UserMovie userMovie = H2JDBCService.getUser_movieByID(Integer.parseInt(movie_id), userId);

			return checkUser(userMovie, responseEntity, jsonResponse);

		}
		return responseEntity.ok(new JsonParser().parse("{}").getAsJsonObject());
	}

//	@GetMapping("/movie/{movie_id}")
//	public Movie getMovie(@PathVariable("movie_id") int movieId) {
//
//		String resourceUrl = MAIN_ENDPOINT + "/movie/" + movieId;
//		
//		ResponseEntity<Movie> movie = restTemplate.exchange(resourceUrl, HttpMethod.GET, request, Movie.class);
//		
//		movie.getBody().setFavorite(false);
//		movie.getBody().setNotes("");
//		movie.getBody().setPersonalRating(0);
//
//		return movie.getBody();
//	}

//	@PatchMapping("/movie/{movie_id}")
//	public ResponseEntity<String> postMovie(@PathVariable int movie_id, @RequestBody UserMovie user_movie)
//			throws SQLException {
//		String resourceUrl = MAIN_ENDPOINT + "/movie/" + movie_id + "?language=es-ES";
//
//		String currentUserName = "";
//
//		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//		if (!(authentication instanceof AnonymousAuthenticationToken)) {
//
//			currentUserName = authentication.getName();
//			System.out.println(currentUserName);
//			int userId = H2JDBCService.getAuthenticatedUserId(currentUserName);
//			user_movie.setUserid(userId);
//			try {
//
//				UserMovie user = H2JDBCService.getUser_movieByID(movie_id, userId);
//
//				if (user == null) {
//					H2JDBCService.insertRecord(user_movie, movie_id);
//				} else {
//					H2JDBCService.updateRecord(user_movie, movie_id);
//				}
//
//			} catch (SQLException e) {
//
//				e.printStackTrace();
//			}
//		}
//
//		return restTemplate.exchange(resourceUrl, HttpMethod.GET, request, String.class);
//	}

	@PatchMapping("/movie/{movie_id}")
	public ResponseEntity<JsonObject> postMovie(@PathVariable int movie_id, @RequestBody UserMovie user_movie)
			throws SQLException {
		String resourceUrl = MAIN_ENDPOINT + "/movie/" + movie_id + "?language=es-ES";

		String currentUserName = "";
		int userId = 0;

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (!(authentication instanceof AnonymousAuthenticationToken)) {

			currentUserName = authentication.getName();
			System.out.println(currentUserName);
			userId = H2JDBCService.getAuthenticatedUserId(currentUserName);
			user_movie.setUserid(userId);
			try {

				UserMovie user = H2JDBCService.getUser_movieByID(movie_id, userId);

				if (user == null) {
					H2JDBCService.insertRecord(user_movie, movie_id);
				} else {
					H2JDBCService.updateRecord(user_movie, movie_id);
				}

			} catch (SQLException e) {

				e.printStackTrace();
			}
		}

		UserMovie userMovie = H2JDBCService.getUser_movieByID(movie_id, userId);
		ResponseEntity<String> responseEntity = restTemplate.exchange(resourceUrl, HttpMethod.GET, request,
				String.class);
		String jsonResponse = responseEntity.getBody();

		return checkUser(userMovie, responseEntity, jsonResponse);

	}

	private ResponseEntity<JsonObject> checkUser(UserMovie userMovie, ResponseEntity<String> responseEntity,
			String jsonResponse) {
		if (userMovie == null) {
			String json = jsonResponse.substring(0, jsonResponse.length() - 1)
					.concat(",\"favourite\": \"false\", \"personal_rating\":\"null\", \"notes\": \"null\"}");

			JsonObject jsonObject = new JsonParser().parse(json).getAsJsonObject();

			return responseEntity.ok(jsonObject);
		} else {
			String json = jsonResponse.substring(0, jsonResponse.length() - 1)
					.concat(",\"favourite\": \"" + userMovie.isFavourite() + "\", \"personal_rating\":\""
							+ userMovie.getPersonal_rating() + "\", \"notes\": \"" + userMovie.getNotes() + "\"}");

			JsonObject jsonObject = new JsonParser().parse(json).getAsJsonObject();
			return responseEntity.ok(jsonObject);
		}
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