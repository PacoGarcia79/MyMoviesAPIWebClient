package com.mymoviesapi.controller;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mymoviesapi.h2jdbc.MovieService;
import com.mymoviesapi.model.UserMovie;

@RestController
@RequestMapping("/api")
public class MovieController {

	@Autowired
	MovieService movieService;

	@GetMapping("/movie/{movie_id}")
	public HashMap<String, Object> getMovie(@PathVariable("movie_id") int movieId) throws SQLException, InterruptedException, ExecutionException {

		HashMap<String, Object> movie = movieService.getMovie(movieId);

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (!(authentication instanceof AnonymousAuthenticationToken)) {
			String currentUserName = authentication.getName();
			int userId = movieService.getAuthenticatedUserId(currentUserName);
			UserMovie userMovie = movieService.getUser_movieByID(movieId, userId);

			configureMovieResponse(movie, userMovie);

		}

		return movie;
	}

	private void configureMovieResponse(HashMap<String, Object> movie, UserMovie userMovie) {
		if (userMovie == null) {

			movie.put("favourite", false);
			movie.put("personal_rating", 0);
			movie.put("notes", "");

		} else {

			movie.put("favourite", userMovie.isFavourite());
			movie.put("personal_rating", userMovie.getPersonal_rating());
			movie.put("notes", userMovie.getNotes());
		}
	}

	@PatchMapping("/movie/{movie_id}")
	public HashMap<String, Object> postMovie(@PathVariable int movie_id, @RequestBody UserMovie user_movie)
			throws SQLException, InterruptedException, ExecutionException {

		String currentUserName = "";
		int userId = 0;

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (!(authentication instanceof AnonymousAuthenticationToken)) {

			currentUserName = authentication.getName();
			System.out.println(currentUserName);
			userId = movieService.getAuthenticatedUserId(currentUserName);
			System.out.println("USER ID -> " + userId);
			user_movie.setUserid(userId);
			try {

				UserMovie user = movieService.getUser_movieByID(movie_id, userId);

				if (user == null) {
					movieService.insertRecord(user_movie, movie_id);
				} else {
					movieService.updateRecord(user_movie, movie_id);
				}

			} catch (SQLException e) {

				e.printStackTrace();
			}
		}

		UserMovie userMovie = movieService.getUser_movieByID(movie_id, userId);
		HashMap<String, Object> movie = movieService.getMovie(movie_id);

		configureMovieResponse(movie, userMovie);

		return movie;
	}

	@GetMapping("/movie/popular")
	public HashMap<String, Object> getPopularMovies() throws InterruptedException, ExecutionException {

		return movieService.getPopularMovies();
	}

	@GetMapping("/movie/top_rated")
	public HashMap<String, Object> getTopRatedMovies() throws InterruptedException, ExecutionException {

		return movieService.getTopRatedMovies();
	}

	@GetMapping("/movie/{movie_id}/credits")
	public HashMap<String, Object> getMovieCredits(@PathVariable("movie_id") int movieId) throws InterruptedException, ExecutionException {

		return movieService.getMovieCredits(movieId);
	}

	@GetMapping("/movie/{movie_id}/images")
	public HashMap<String, Object> getMovieImages(@PathVariable("movie_id") int movieId) throws InterruptedException, ExecutionException {

		return movieService.getMovieImages(movieId);
	}

	@GetMapping("/movie/{movie_id}/keywords")
	public HashMap<String, Object> getMovieKeyWords(@PathVariable("movie_id") int movieId) throws InterruptedException, ExecutionException {

		return movieService.getMovieKeyWords(movieId);
	}

	@GetMapping("/movie/{movie_id}/recommendations")
	public HashMap<String, Object> getMovieRecommendations(@PathVariable("movie_id") int movieId) throws InterruptedException, ExecutionException {

		return movieService.getMovieRecommendations(movieId);
	}

	@GetMapping("/movie/{movie_id}/similar")
	public HashMap<String, Object> getSimilarMovies(@PathVariable("movie_id") int movieId) throws InterruptedException, ExecutionException {

		return movieService.getSimilarMovies(movieId);
	}

}