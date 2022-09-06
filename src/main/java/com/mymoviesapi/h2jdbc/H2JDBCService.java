package com.mymoviesapi.h2jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.mymoviesapi.model.UserMovie;

public class H2JDBCService {

	private static final String INSERT_USER_SQL = "INSERT INTO user_movie"
			+ "  (userid , movieid , favorite , personal_rating , notes ) VALUES " + " (?, ?, ?, ?, ?);";

	private static final String UPDATE_USER_SQL = "UPDATE user_movie SET favorite = ?, personal_rating = ?, notes = ? WHERE userid = ? AND movieid = ?;";

	private static final String GET_USER_SQL = "select userid , movieid , favorite , personal_rating , notes from user_movie where userid =? and movieid=?;";

	public static void insertRecord(UserMovie user_movie, int id_movie) throws SQLException {

		try (Connection connection = H2JDBCUtils.getConnection();

				PreparedStatement preparedStatement = connection.prepareStatement(INSERT_USER_SQL)) {
			
			if(user_movie.getPersonal_rating()<0 || user_movie.getPersonal_rating()>5) {
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

	public static void updateRecord(UserMovie user_movie, int id_movie) throws SQLException {

		try (Connection connection = H2JDBCUtils.getConnection();

				PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_USER_SQL)) {
			
			if(user_movie.getPersonal_rating()<0 || user_movie.getPersonal_rating()>5) {
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

	public static UserMovie getUser_movieByID(int id_movie, int user_id) throws SQLException {

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

	public static int getAuthenticatedUserId(String userName) throws SQLException {
		String consulta = "select userid from users where username = '" + userName + "'";
		try (Connection connection = H2JDBCUtils.getConnection(); Statement statement = connection.createStatement()) {

			ResultSet rs = statement.executeQuery(consulta);

			if (rs.next()) {
				return rs.getInt("userid");
			}
			return 0;

		}
	}

}
