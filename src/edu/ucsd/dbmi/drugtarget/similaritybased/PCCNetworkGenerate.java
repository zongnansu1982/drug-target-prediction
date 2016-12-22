package edu.ucsd.dbmi.drugtarget.similaritybased;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

public class PCCNetworkGenerate {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	
	public SimpleWeightedGraph<String, DefaultWeightedEdge> graph;
	public HashSet<String> ynodes;
	public HashMap<String,HashSet<String>> data;
	public SimpleWeightedGraph<String, DefaultWeightedEdge> getGraph() {
		return graph;
	}
	public void setGraph(SimpleWeightedGraph<String, DefaultWeightedEdge> graph) {
		this.graph = graph;
	}
	public PCCNetworkGenerate(HashMap<String,HashSet<String>> data, HashSet<String> ynodes){
		this.graph=new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		this.ynodes=ynodes;
		this.data=data;
	}
	
	public void generate(){
		ArrayList<String> xnodes =new ArrayList<>();
		for(String x:data.keySet()){
			xnodes.add(x);
		}
		
		for (int i = 0; i < xnodes.size()-1; i++) {
			for (int j = i+1; j < xnodes.size(); j++) {
					Double sim=Similarities.pearsonCorrelationCoefficient(data.get(xnodes.get(i)), data.get(xnodes.get(j)), ynodes);
//					System.out.println(xnodes.get(i)+" "+xnodes.get(j)+" pcc sim: "+sim);
					graph.addVertex(xnodes.get(i));
					graph.addVertex(xnodes.get(j));
					DefaultWeightedEdge edge=new DefaultWeightedEdge();
					graph.addEdge(xnodes.get(i), xnodes.get(j), edge);
					graph.setEdgeWeight(edge, sim);
			}
		}
		
	}
	
	
}
