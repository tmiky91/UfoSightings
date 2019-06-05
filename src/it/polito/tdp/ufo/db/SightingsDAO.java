package it.polito.tdp.ufo.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Year;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import it.polito.tdp.ufo.model.AnnoCount;
import it.polito.tdp.ufo.model.Sighting;

public class SightingsDAO {
	
	public List<Sighting> getSightings() {
		String sql = "SELECT * FROM sighting" ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			List<Sighting> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					list.add(new Sighting(res.getInt("id"),
							res.getTimestamp("datetime").toLocalDateTime(),
							res.getString("city"), 
							res.getString("state"), 
							res.getString("country"),
							res.getString("shape"),
							res.getInt("duration"),
							res.getString("duration_hm"),
							res.getString("comments"),
							res.getDate("date_posted").toLocalDate(),
							res.getDouble("latitude"), 
							res.getDouble("longitude"))) ;
				} catch (Throwable t) {
					t.printStackTrace();
					System.out.println(res.getInt("id"));
				}
			}
			
			conn.close();
			return list ;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
	}
	
	public List<AnnoCount> getAnni(){
		final String sql=	"select year(s.datetime) as anno, count(id) as cnt " + 
							"from sighting as s " + 
							"where s.country='us' " + 
							"group by year(s.datetime)";
		List<AnnoCount> list = new LinkedList<>() ;
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				list.add(new AnnoCount(Year.of(res.getInt("anno")), res.getInt("cnt")));
			}
			
			conn.close();
			return list ;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
	}
	
	public List<String> getStati(Year anno){
		final String sql=	"select distinct state " + 
							"from sighting as s " + 
							"where s.country='us' " +
							"and Year(s.datetime)=?";
		List<String> stati = new LinkedList<>() ;
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, anno.getValue());
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				stati.add(res.getString("state"));
			}
			
			conn.close();
			return stati ;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
	}

	public boolean esisteArco(String s1, String s2, Year anno) {
		final String sql=	"select count(*) as cnt " + 
							"from sighting as s1, sighting as s2 " + 
							"where Year(s1.datetime) = Year(s2.datetime) " + 
							"and Year(s1.datetime) =? " + 
							"and s1.state=? " + 
							"and s2.state=? " + 
							"and s1.country='us' " + 
							"and s2.country='us' " + 
							"and s2.datetime > s1.datetime";
		
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, anno.getValue());
			st.setString(2, s1);
			st.setString(3, s2);
			ResultSet res = st.executeQuery() ;
			
			if(res.next()) {
				if(res.getInt("cnt") >0) {
					conn.close();
					return true;
				}else {
					conn.close();
					return false;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

}
