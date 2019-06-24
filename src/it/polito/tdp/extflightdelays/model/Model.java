package it.polito.tdp.extflightdelays.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.extflightdelays.db.ExtFlightDelaysDAO;

public class Model {
	
	SimpleWeightedGraph<Airport, DefaultWeightedEdge> grafo;
	Map<Integer, Airport> idMap;
	ExtFlightDelaysDAO dao;
	List<Rotta> rotte;
	
	//ricorsione
	private List<Airport> pest;
	
	public Model() {
		grafo=new SimpleWeightedGraph<Airport, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		idMap=new HashMap<Integer, Airport>();
		dao=new ExtFlightDelaysDAO();
		dao.loadAllAirports(idMap);
		rotte= new ArrayList<Rotta>();
	}
	
	public void creaGrafo(double distanza) {
		//vertici
		//Graphs.addAllVertices(this.grafo, dao.);
		int pesi=0;
		
		//vertici e archi
		for(Rotta r: dao.getRotte(distanza, idMap)) {
			if(!grafo.containsVertex(r.getA1()))
				grafo.addVertex(r.getA1());
			if(!grafo.containsVertex(r.getA2()))
				grafo.addVertex(r.getA2());
			if (grafo.getEdge(r.getA1(), r.getA2())==null) {
				Graphs.addEdge(this.grafo, r.getA1(), r.getA2(), r.getDistanza());
				
			} else {
				double peso=grafo.getEdgeWeight(this.grafo.getEdge(r.getA1(), r.getA2()));
				this.grafo.setEdgeWeight(this.grafo.getEdge(r.getA1(), r.getA2()), ((peso+r.getDistanza())/2));
				//System.out.println("Aggiornare peso! Peso vecchio: " + peso + " peso nuovo: " + grafo.getEdgeWeight(this.grafo.getEdge(r.getA1(), r.getA2())));
				pesi++;
			}
		}
		
		System.out.println("Vertici grafo: "+this.grafo.vertexSet().size());
		System.out.println("Archi grafo: "+this.grafo.edgeSet().size());
		//System.out.println("Pesi registrati: "+pesi);
	}

	public Set<Airport> getGrafo() {
		return this.grafo.vertexSet();
	}
	
	public List<Rotta> vicini(Airport a) {
		List<Airport> vicini= Graphs.neighborListOf(this.grafo, a);
		List<Rotta> res = new ArrayList<Rotta>();
		
		for (Airport t : vicini) {
			double peso= this.grafo.getEdgeWeight(this.grafo.getEdge(a, t));
			res.add(new Rotta(a, t, peso));
		}
		Collections.sort(res);
		return res;
	}
	
	public List<Airport> cittaMax(Airport a, double distanzaMax) {
		pest=null;
		
		List<Airport> parziale= new ArrayList<>();
		
		ricorsione(a, parziale, distanzaMax);
		
		return pest;
	}

	private void ricorsione(Airport a, List<Airport> parziale, double distanzaMax) {
		//do always
		parziale.add(a);
		List<Airport> toVisit= new ArrayList<Airport>();
		for (Airport t : Graphs.neighborListOf(this.grafo, a)) {
			if(!parziale.contains(t)) {
				toVisit.add(t);
			}
		}
		
		//term
		if (parziale.size()==this.grafo.vertexSet().size() || toVisit.size()==0) {
			if(pest==null || parziale.size()>=pest.size()) {
				if (calcolaMiglia(parziale)<distanzaMax) {
					pest= new ArrayList<>(parziale);
				}
			}
		}
		
		if (calcolaMiglia(parziale)<distanzaMax) {
			for (Airport airport : toVisit) {
				if (this.grafo.containsVertex(airport)) {
					ricorsione(airport, parziale, distanzaMax);
				}
			}
		}
		
		//backtrack
		parziale.remove(a);		
		
		/*	//term
		if (calcolaMiglia(parziale)>distanzaMax || parziale.size()==this.grafo.vertexSet().size() || toVisit.size()==0) {
			parziale.remove(parziale.size()-1);
			if(pest==null || parziale.size()>pest.size()) {
				pest= new ArrayList<>(parziale);
			}
		} else {
			for (Airport airport : toVisit) {
				if (this.grafo.containsVertex(airport)) {
					ricorsione(airport, parziale, distanzaMax);
				}
			}
		}*/
	}

	private double calcolaMiglia(List<Airport> parziale) {
		double somma=0;
		
		for(int i=0; i<parziale.size()-1; i++) {
			Airport a1=parziale.get(i);
			Airport a2=parziale.get(i+1);
			
			DefaultWeightedEdge arco=this.grafo.getEdge(a1, a2);
			double frt=this.grafo.getEdgeWeight(arco);
			somma+=frt;
		}
		return somma;
	}

	public Map<Integer, Airport> getIdMap() {
		return idMap;
	}

	public List<Airport> getPest() {
		return pest;
	}
	
	public int cittaCt() {
		return this.pest.size();
	}
	
	public double migliaUsate() {
		double somma=0;
		
		for(int i=0; i<pest.size()-1; i++) {
			Airport a1=pest.get(i);
			Airport a2=pest.get(i+1);
			
			DefaultWeightedEdge arco=this.grafo.getEdge(a1, a2);
			double frt=this.grafo.getEdgeWeight(arco);
			somma+=frt;
		}
		return somma;
	}

}
