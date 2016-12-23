package edu.ucsd.dbmi.drugtarget.main;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;
import org.semanticweb.yars.nx.Node;
import org.semanticweb.yars.nx.parser.NxParser;

import edu.ucsd.dbmi.drugtarget.chemicstrc.ChemicalBasedMethod;
import edu.ucsd.dbmi.drugtarget.deepwalk.DeepWalkMethod;

public class TargetPrediction implements Prediction{

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		

	}
	String modelfile="data/input/deepwalk_external_100.txt";
	String idxfile="data/input/deepwalk_externalidx_100.txt";

	public TargetPrediction(String modelfile, String idxfile){
		this.modelfile=modelfile;
		this.idxfile=idxfile;
	}
	
	@Override
	public HashMap<String,HashMap<String, Double>> predict(HashSet queries,HashMap associations
			, HashSet allDrug, HashSet allTarget) throws Exception{
		HashMap<String,HashMap<String,Double>> tmp=new DeepWalkMethod().getSimilarDrug( modelfile, idxfile,	queries,  allDrug);
		HashMap<String,HashMap<String, Double>> results=getAssociateTargets(tmp, associations,allTarget);
		return results;
	}
	
	@Override
	public HashMap<String,HashMap<String, Double>> predict(String query,HashMap associations
			,HashSet allDrug, HashSet allTarget) throws Exception{
		HashMap<String,Double> tmp=new DeepWalkMethod().getSimilarDrug( modelfile, idxfile,	query,  allDrug);
		HashMap<String,HashMap<String,Double>> map=new HashMap<>();
		map.put(query, tmp);
		HashMap<String,HashMap<String, Double>> results=getAssociateTargets(map, associations,allTarget);
		return results;
	}
	
	
	public HashMap<String,HashMap<String, Double>> getAssociateTargets(HashMap<String,HashMap<String,Double>> input, 
			HashMap<String,HashSet<String>> associations, HashSet<String> allTarget) throws IOException{
		HashMap<String,HashMap<String, Double>>  results=new HashMap<>();
		for(Entry<String,HashMap<String,Double>> entry1:input.entrySet()){
			HashMap<String,Double> tmp= new HashMap<>();
			for(Entry<String,Double> entry:entry1.getValue().entrySet()){
//				System.err.println("======================================");
//				System.err.println(entry.getKey());
					if(associations.containsKey(entry.getKey())){
						for(String target:associations.get(entry.getKey())){
							if(allTarget.contains(target)){
//								System.err.println( " =========> " +entry1.getKey()+" "+entry.getKey());
									if(!associations.get(entry1.getKey()).contains(target)){
										if(tmp.containsKey(target)){
											tmp.put(target, tmp.get(target)+entry.getValue());
//											System.err.println(drug+" associated drug score: "+entry.getValue().get(string));
										}else{
											tmp.put(target, entry.getValue());
										}
										
									}
									
							}

						}
				}
			}
			
			results.put(entry1.getKey(), tmp);
		}
		
		return results;
		
	}
	
	
	
}
