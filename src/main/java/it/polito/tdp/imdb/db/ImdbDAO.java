package it.polito.tdp.imdb.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.imdb.model.Actor;
import it.polito.tdp.imdb.model.Arco;
import it.polito.tdp.imdb.model.Director;
import it.polito.tdp.imdb.model.Movie;

public class ImdbDAO {
	
	public List<Actor> listAllActors(){
		String sql = "SELECT * FROM actors";
		List<Actor> result = new ArrayList<Actor>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Actor actor = new Actor(res.getInt("id"), res.getString("first_name"),
						res.getString("last_name"),
						res.getString("gender"));
				
				result.add(actor);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Movie> listAllMovies(){
		String sql = "SELECT * FROM movies";
		List<Movie> result = new ArrayList<Movie>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Movie movie = new Movie(res.getInt("id"), res.getString("name"), 
						res.getInt("year"), res.getDouble("rank"));
				
				result.add(movie);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	public List<Director> listAllDirectors(){
		String sql = "SELECT * FROM directors";
		List<Director> result = new ArrayList<Director>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Director director = new Director(res.getInt("id"), res.getString("first_name"),
						res.getString("last_name"));
				
				result.add(director);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	public List<Director> getVertici(int anno){
		String sql = "SELECT DISTINCT d.* "
				+ "FROM directors d, movies_directors md, movies m "
				+ "WHERE d.`id`=md.`director_id` "
				+ "AND md.`movie_id`=m.`id` "
				+ "AND m.`year`=?";
		List<Director> result = new ArrayList<Director>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, anno);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Director director = new Director(res.getInt("id"), res.getString("first_name"),
						res.getString("last_name"));
				
				result.add(director);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Arco> getArchi(int anno, Map<Integer, Director> mappa){
		String sql = "SELECT md1.`director_id` AS d1, md2.`director_id`AS d2, COUNT(*) AS peso "
				+ "FROM movies_directors md1, roles r1, roles r2, movies_directors md2, movies m1, movies m2 "
				+ "WHERE md1.`movie_id`=r1.`movie_id` "
				+ "AND r1.`actor_id`=r2.`actor_id` "
				+ "AND md2.`movie_id`=r2.`movie_id` "
				+ "AND m1.`id`=md1.`movie_id` "
				+ "AND m2.`id`=md2.`movie_id` "
				+ "AND md1.`director_id` IN (SELECT DISTINCT d.`id` "
				+ "				  FROM directors d, movies_directors md, movies m "
				+ "				  WHERE d.`id`=md.`director_id` "
				+ "				  AND md.`movie_id`=m.`id` "
				+ "				  AND m.`year`=?) "
				+ "AND md2.`director_id` IN (SELECT DISTINCT d.`id` "
				+ "				  FROM directors d, movies_directors md, movies m "
				+ "				  WHERE d.`id`=md.`director_id` "
				+ "				  AND md.`movie_id`=m.`id` "
				+ "				  AND m.`year`=?) "
				+ "AND md1.`director_id`<md2.`director_id` "
				+ "AND m1.`year`=? "
				+ "AND m2.`year`=? "
				+ "GROUP BY md1.`director_id`, md2.`director_id`";
		List<Arco> result = new ArrayList<>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, anno);
			st.setInt(2, anno);
			st.setInt(3, anno);
			st.setInt(4, anno);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Arco arco = new Arco(mappa.get(res.getInt("d1")), mappa.get(res.getInt("d2")),
						res.getDouble("peso"));
				
				result.add(arco);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
}
