package it.polito.tdp.artsmia.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.artsmia.db.ArtsmiaDAO;

public class Model {
	
	private Graph<ArtObject, DefaultWeightedEdge> grafo;
	
	private ArtsmiaDAO dao;
	
	// IDMAP
	private Map<Integer, ArtObject> idMap;
	
	public Model() {
		// grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		dao = new ArtsmiaDAO();
		idMap = new HashMap<>();
	}
	
	public void creaGrafo() {
		
		// meglio creare qua dentro così ogni volta che si usa il metodo, si svuota grafo precedente
		grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		
		// Aggiungere vertici
		
		/* in questo modo OGNI VOLTA creiamo i vertici da capo TANTISSIMIIII
		 * 
		List<ArtObject> vertici = dao.listObjects();
		Graphs.addAllVertices(this.grafo, vertici);
		*/
		
		// MEGLIO USARE IDMAP CHE TIENE TRACCIA DEI VERTICI GIA AGGIUNTI
		
		dao.idMapObjects(idMap);
		Graphs.addAllVertices(this.grafo, idMap.values());
		
		// Aggiungere archi
		
		// APPROCCIO 1, doppio ciclo for sui vertici, controllo se sono collegati TROPPI VERTICI
		
		/*for(ArtObject a1 : this.grafo.vertexSet()) {
			for(ArtObject a2 : this.grafo.vertexSet()) {
				if(!a1.equals(a2) && !this.grafo.containsEdge(a2, a1)) { // se i vertici sono diversi e non esiste gia arco
					int peso = this.dao.getPeso(a1,a2);
					if(peso>0) {
						Graphs.addEdge(this.grafo, a1, a2, peso);
					}
					
				}
			}
		}
		*/
		
		// APPROCCIO 3
		
		for(Adiacenza a : this.dao.getAdiacenze()) {
			
			// con il > della query ho gia escluso i duplicati e sono sicuro che il peso sia > 0
				
			Graphs.addEdge(this.grafo, this.idMap.get(a.getId1()), this.idMap.get(a.getId2()), a.getPeso());
				
		}
		
		System.out.println("GRAFO CREATO");
		System.out.println("#VERTICI: " + this.grafo.vertexSet().size() + " #ARCHI: " + this.grafo.edgeSet().size());
	}

	public int getNVertici() {
		return this.grafo.vertexSet().size();
	}

	public int getNArchi() {
		return this.grafo.edgeSet().size();
	}
	
	
}
