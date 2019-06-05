package it.polito.tdp.ufo.model;

import java.time.Year;
import java.util.LinkedList;
import java.util.List;

import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedGraph;
import org.jgrapht.graph.SimpleWeightedGraph;
import org.jgrapht.traverse.DepthFirstIterator;

import it.polito.tdp.ufo.db.SightingsDAO;

public class Model {
	
	private SightingsDAO dao;
	private List<String> stati;
	private SimpleDirectedGraph<String, DefaultEdge> grafo;
	
	public Model() {
		this.dao= new SightingsDAO();
	}
	
	public List<AnnoCount> getAnni(){
		return dao.getAnni();
	}
	
	public void creaGrafo(Year anno) {
		grafo = new SimpleDirectedGraph<String, DefaultEdge>(DefaultEdge.class);
		stati = dao.getStati(anno);
		Graphs.addAllVertices(grafo, stati);
		for(String s1: grafo.vertexSet()) {
			for(String s2: grafo.vertexSet()) {
				if(!s1.equals(s2)) {
					if(dao.esisteArco(s1, s2, anno)) {
						grafo.addEdge(s1, s2);
					}
				}
			}
		}
	}

	public List<String> getStati() {
		return stati;
	}
	
	public List<String> getSuccessori(String stato){
		return Graphs.successorListOf(grafo, stato);
	}
	
	public List<String> getPredecessori(String stato){
		return Graphs.predecessorListOf(grafo, stato);
	}
	
	public List<String> getRaggiungibili(String stato){
		List<String> raggiungibili = new LinkedList<>();
		DepthFirstIterator<String, DefaultEdge> dp = new DepthFirstIterator(grafo, stato);
		dp.next(); //per scartare il primo
		while(dp.hasNext()) {
			raggiungibili.add(dp.next());
		}
		return raggiungibili;
	}
	
	

}
