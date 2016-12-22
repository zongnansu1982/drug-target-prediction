package edu.ucsd.dbmi.drugtarget.genomicsqs;

import java.util.HashMap;
import java.util.HashSet;

import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

public class GenomicSeqBasedMethod {
	public HashMap<String,HashMap<String,Double>> getSimilarTarget(SimpleWeightedGraph<String, DefaultWeightedEdge> graph,HashSet<String> queries, HashSet<String> allTargets) throws Exception {
		// TODO Auto-generated method stub
		
    	HashMap<String,HashMap<String,Double>> map=new HashMap<>();
		for(String query:queries){
			HashMap<String,Double> amap=new HashMap<>();
			for(String target:allTargets){
				if(!query.equals(target)){
					Double sim=0.0;
//					try{
						if(graph.containsEdge(target, query)){
							sim=graph.getEdgeWeight(graph.getEdge(target, query));
						}
//					}catch(Exception e){
//						sim=0.0;
//					}
					amap.put(target, sim);
				}
			}
			
			map.put(query, amap);
		}
		return map;
	}
}
