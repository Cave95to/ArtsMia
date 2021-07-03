package it.polito.tdp.artsmia.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.artsmia.model.Adiacenza;
import it.polito.tdp.artsmia.model.ArtObject;

public class ArtsmiaDAO {
	
	// QUESTO METODO AGGIUNGE OGNI VOLTA DA CAPO I VERTICI !!
	
	public List<ArtObject> listObjects() {
		
		String sql = "SELECT * from objects";
		List<ArtObject> result = new ArrayList<>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				ArtObject artObj = new ArtObject(res.getInt("object_id"), res.getString("classification"), res.getString("continent"), 
						res.getString("country"), res.getInt("curator_approved"), res.getString("dated"), res.getString("department"), 
						res.getString("medium"), res.getString("nationality"), res.getString("object_name"), res.getInt("restricted"), 
						res.getString("rights_type"), res.getString("role"), res.getString("room"), res.getString("style"), res.getString("title"));
				
				result.add(artObj);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	// MEGLIO TRASFORMARLO CON IDMAP
	
	public void idMapObjects(Map<Integer, ArtObject> map) {
			
			String sql = "SELECT * from objects";
		
			Connection conn = DBConnect.getConnection();
	
			try {
				PreparedStatement st = conn.prepareStatement(sql);
				ResultSet res = st.executeQuery();
				while (res.next()) {
					
					// se id dell'oggetto non esiste nella mappa lo aggiungiamo
					if(!map.containsKey(res.getInt("object_id"))) {
						ArtObject artObj = new ArtObject(res.getInt("object_id"), res.getString("classification"), res.getString("continent"), 
								res.getString("country"), res.getInt("curator_approved"), res.getString("dated"), res.getString("department"), 
								res.getString("medium"), res.getString("nationality"), res.getString("object_name"), res.getInt("restricted"), 
								res.getString("rights_type"), res.getString("role"), res.getString("room"), res.getString("style"), res.getString("title"));
						
						map.put(artObj.getId(), artObj);
					}
				}
				conn.close();
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}


	public int getPeso(ArtObject a1, ArtObject a2) {
		String sql = "SELECT COUNT(*) AS PESO "
				+ "FROM exhibition_objects AS E1, exhibition_objects AS E2 "
				+ "WHERE E1.exhibition_id=E2.exhibition_id AND E1.object_id = ? AND E2.object_id = ?";
		

		Connection conn = DBConnect.getConnection();

		try {
			
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, a1.getId());
			st.setInt(2, a2.getId());
			ResultSet res = st.executeQuery();
			int peso = 0;
			
			if (res.first()) {
				
				peso = res.getInt("peso");
				
			}
			
			conn.close();
			
			return peso;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return 0;
		}	
	}
	
	public List<Adiacenza> getAdiacenze() {
		
		String sql = "SELECT E1.object_id as id1, E2.object_id as id2, COUNT(*) AS PESO "
				+ "FROM exhibition_objects AS E1, exhibition_objects AS E2 "
				+ "WHERE E1.exhibition_id=E2.exhibition_id AND E2.object_id < E1.object_id "
				+ "GROUP BY E1.object_id, E2.object_id";
		Connection conn = DBConnect.getConnection();
		List<Adiacenza> adiacenze = new ArrayList<>();
		
		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {
			
				adiacenze.add(new Adiacenza(res.getInt("id1"), res.getInt("id2"), res.getInt("peso")));
				
			}
			conn.close();
			return adiacenze;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}

	}
		
}
