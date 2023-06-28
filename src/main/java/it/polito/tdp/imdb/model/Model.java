package it.polito.tdp.imdb.model;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import java.util.*;

import it.polito.tdp.imdb.db.ImdbDAO;

public class Model {
	
	private ImdbDAO dao;
	private Graph<Director, DefaultWeightedEdge> grafo;
	
	public Model() {
		super();
		this.dao = new ImdbDAO();
	}
	
	public void creaGrafo(int anno) {
		
		this.grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		
		Graphs.addAllVertices(grafo, this.dao.getVertici(anno));
		
		Map<Integer, Director> mappa = new HashMap<>();
		for(Director d: this.dao.getVertici(anno)) {
			mappa.put(d.getId(), d);
		}
		
		for(Arco a: this.dao.getArchi(anno, mappa)) {
			this.grafo.addEdge(a.getD1(), a.getD2());
			this.grafo.setEdgeWeight(a.getD1(), a.getD2(), a.getPeso());
		}
		
	}
	
	public List<RegistiPeso> getVicini(Director d){
		
		List<RegistiPeso> res = new LinkedList<>();
		
		for(Director r: Graphs.neighborListOf(grafo, d)) {
			res.add(new RegistiPeso(r, this.grafo.getEdgeWeight(this.grafo.getEdge(d, r))));
		}
		return res;
		
	}

	public int getNumVertici() {
		return this.grafo.vertexSet().size();
	}

	public int getNumArchi() {
		return this.grafo.edgeSet().size();
	}
	
	public List<Director> getVertici(){
		
		List<Director> res = new LinkedList<>();
		
		for(Director d: this.grafo.vertexSet()) {
			res.add(d);
		}
		Collections.sort(res);
		return res;
	}

}
