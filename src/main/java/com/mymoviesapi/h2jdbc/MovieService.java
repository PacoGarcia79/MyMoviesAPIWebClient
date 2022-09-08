package com.mymoviesapi.h2jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.mymoviesapi.model.UserMovie;

@Service
public class MovieService {

	WebClient webClient = WebClient.create("https://api.themoviedb.org/3/movie/");

	@Value("${themoviedatabase.api_key}")
	private String api_key;

	private static final String INSERT_USER_SQL = "INSERT INTO user_movie"
			+ "  (userid , movieid , favorite , personal_rating , notes ) VALUES " + " (?, ?, ?, ?, ?);";

	private static final String UPDATE_USER_SQL = "UPDATE user_movie SET favorite = ?, personal_rating = ?, notes = ? WHERE userid = ? AND movieid = ?;";

	private static final String GET_USER_SQL = "select userid , movieid , favorite , personal_rating , notes from user_movie where userid =? and movieid=?;";

	public void insertRecord(UserMovie user_movie, int id_movie) throws SQLException {

		try (Connection connection = H2JDBCUtils.getConnection();

				PreparedStatement preparedStatement = connection.prepareStatement(INSERT_USER_SQL)) {

			if (user_movie.getPersonal_rating() < 0 || user_movie.getPersonal_rating() > 5) {
				throw new SQLException("Bad personal rating");
			}

			preparedStatement.setInt(1, user_movie.getUserid());
			preparedStatement.setInt(2, id_movie);
			preparedStatement.setBoolean(3, user_movie.isFavourite());
			preparedStatement.setInt(4, user_movie.getPersonal_rating());
			preparedStatement.setString(5, user_movie.getNotes());

			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public void updateRecord(UserMovie user_movie, int id_movie) throws SQLException {

		try (Connection connection = H2JDBCUtils.getConnection();

				PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_USER_SQL)) {

			if (user_movie.getPersonal_rating() < 0 || user_movie.getPersonal_rating() > 5) {
				throw new SQLException("Bad personal rating");
			}

			preparedStatement.setInt(1, (user_movie.isFavourite() ? 1 : 0));
			preparedStatement.setInt(2, user_movie.getPersonal_rating());
			preparedStatement.setString(3, user_movie.getNotes());
			preparedStatement.setInt(4, user_movie.getUserid());
			preparedStatement.setInt(5, id_movie);

			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public UserMovie getUser_movieByID(int id_movie, int user_id) throws SQLException {

		try (Connection connection = H2JDBCUtils.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(GET_USER_SQL);) {
			preparedStatement.setInt(1, user_id);
			preparedStatement.setInt(2, id_movie);

			ResultSet rs = preparedStatement.executeQuery();
			if (!rs.next()) {
				return null;
			} else {
				UserMovie user_movie = new UserMovie();
				boolean favorite = rs.getBoolean("favorite");
				int personal_rating = rs.getInt("personal_rating");
				String notes = rs.getString("notes");

				user_movie.setFavourite(favorite);
				user_movie.setNotes(notes);
				user_movie.setPersonal_rating(personal_rating);
				return user_movie;
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public int getAuthenticatedUserId(String userName) throws SQLException {
		String consulta = "select userid from users where username = '" + userName + "'";
		try (Connection connection = H2JDBCUtils.getConnection(); Statement statement = connection.createStatement()) {

			ResultSet rs = statement.executeQuery(consulta);

			if (rs.next()) {
				return rs.getInt("userid");
			}
			return 0;

		}
	}

	/**
	 * Using .block()
	 * 
	 * @param movieId
	 * @return
	 * @throws InterruptedException
	 * @throws ExecutionException
	 */
//	@Cacheable("top_rated")
//	public HashMap<String, Object> getTopRatedMovies() {
//		return webClient.get().uri(uriBuilder -> uriBuilder.path("top_rated").queryParam("api_key", api_key).build())
//				.retrieve().bodyToMono(HashMap.class).block();
//	}

	/**
	 * Using .toFuture().get()
	 * 
	 * @param movieId
	 * @return
	 * @throws InterruptedException
	 * @throws ExecutionException
	 */
	@Cacheable("movie")
	public HashMap<String, Object> getMovie(int movieId) throws InterruptedException, ExecutionException {
		return webClient.get()
				.uri(uriBuilder -> uriBuilder.path("{movie_id}").queryParam("api_key", api_key).build(movieId))
				.retrieve().bodyToMono(HashMap.class).toFuture().get();
	}

	@Cacheable("popular")
	public HashMap<String, Object> getPopularMovies() throws InterruptedException, ExecutionException {
		return webClient.get().uri(uriBuilder -> uriBuilder.path("popular").queryParam("api_key", api_key).build())
				.retrieve().bodyToMono(HashMap.class).toFuture().get();
	}

	@Cacheable("top_rated")
	public HashMap<String, Object> getTopRatedMovies() throws InterruptedException, ExecutionException {
		return webClient.get().uri(uriBuilder -> uriBuilder.path("top_rated").queryParam("api_key", api_key).build())
				.retrieve().bodyToMono(HashMap.class).toFuture().get();
	}

	@Cacheable("images")
	public HashMap<String, Object> getMovieImages(int movieId) throws InterruptedException, ExecutionException {
		return webClient.get()
				.uri(uriBuilder -> uriBuilder.path("{movie_id}/images").queryParam("api_key", api_key).build(movieId))
				.retrieve().bodyToMono(HashMap.class).toFuture().get();
	}

	@Cacheable("credits")
	public HashMap<String, Object> getMovieCredits(int movieId) throws InterruptedException, ExecutionException {
		return webClient.get()
				.uri(uriBuilder -> uriBuilder.path("{movie_id}/credits").queryParam("api_key", api_key).build(movieId))
				.retrieve().bodyToMono(HashMap.class).toFuture().get();
	}

	@Cacheable("keywords")
	public HashMap<String, Object> getMovieKeyWords(int movieId) throws InterruptedException, ExecutionException {
		return webClient.get()
				.uri(uriBuilder -> uriBuilder.path("{movie_id}/keywords").queryParam("api_key", api_key).build(movieId))
				.retrieve().bodyToMono(HashMap.class).toFuture().get();
	}

	@Cacheable("similar")
	public HashMap<String, Object> getSimilarMovies(int movieId) throws InterruptedException, ExecutionException {
		return webClient.get()
				.uri(uriBuilder -> uriBuilder.path("{movie_id}/similar").queryParam("api_key", api_key).build(movieId))
				.retrieve().bodyToMono(HashMap.class).toFuture().get();
	}

	/**
	 * Using share().block()
	 * 
	 * @param movieId
	 * @return
	 * @throws InterruptedException
	 * @throws ExecutionException
	 */
	@Cacheable("recommendations")
	public HashMap<String, Object> getMovieRecommendations(int movieId)
			throws InterruptedException, ExecutionException {
		return webClient.get().uri(uriBuilder -> uriBuilder.path("{movie_id}/recommendations")
				.queryParam("api_key", api_key).build(movieId)).retrieve().bodyToMono(HashMap.class).share().block();
	}

}
