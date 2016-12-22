package edu.ucsd.dbmi.drugtarget.chemicstrc;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.HashSet;


import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

public class ChemicalBasedMethod {
	 public HashMap<String,HashMap<String,Double>> getSimilarDrug(SimpleWeightedGraph<String, DefaultWeightedEdge> graph,HashSet<String> queries,
	    		HashSet<String> allDrugs) throws Exception {
			// TODO Auto-generated method stub
		 
		 HashMap<String,HashMap<String,Double>> map=new HashMap<>();
			for(String query:queries){
				HashMap<String,Double> amap=new HashMap<>();
				
				for(String drug:allDrugs){
					if(!query.equals(drug)){
						Double sim=0.0;
						
//						try{
							if(graph.containsEdge(drug, query)){
								sim=graph.getEdgeWeight(graph.getEdge(drug, query));
							}
//						}catch(Exception e){
//							sim=0.0;
//						}
						amap.put(drug, sim);
					}
				}
				
				map.put(query, amap);
			}
			
			return map;
		}
}
