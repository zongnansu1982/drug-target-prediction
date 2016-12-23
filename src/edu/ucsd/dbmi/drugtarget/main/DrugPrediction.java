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

import edu.ucsd.dbmi.drugtarget.deepwalk.DeepWalkMethod;
import edu.ucsd.dbmi.drugtarget.genomicsqs.GenomicSeqBasedMethod;

public class DrugPrediction implements Prediction{

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
	}
	

	String modelfile="data/input/deepwalk_external_100.txt";
	String idxfile="data/input/deepwalk_externalidx_100.txt";

	public DrugPrediction(String modelfile, String idxfile){
		this.modelfile=modelfile;
		this.idxfile=idxfile;
	}
	
	@Override
	public HashMap predict(HashSet queries, HashMap associations, HashSet allDrugs, HashSet allTargets) throws Exception {
		// TODO Auto-generated method stub
		HashMap<String,HashMap<String,Double>> tmp=new DeepWalkMethod().getSimilarTarget( modelfile, idxfile, queries,  allTargets); 
		HashMap<String,HashMap<String,Double>> results=getAssociateDrugs(tmp, associations, allDrugs);
		return results;
	}

	@Override
	public HashMap predict(String query, HashMap associations, HashSet allDrugs, HashSet allTargets) throws Exception {
		// TODO Auto-generated method stub
		HashMap<String,Double> tmp=new DeepWalkMethod().getSimilarTarget( modelfile, idxfile, query,  allTargets); 
		HashMap<String,HashMap<String,Double>> map=new HashMap<>();
		map.put(query, tmp);
		HashMap<String,HashMap<String,Double>> results=getAssociateDrugs(map, associations, allDrugs);
		return results;
	}
	
	
	
	public HashMap<String,HashMap<String,Double>> predictBySimilarTarget(String idxfile,HashSet<String> queries, HashMap<String,HashSet<String>> associations,
			HashSet<String> allDrugs, HashSet<String> allTargets ) throws Exception{
		HashMap<String,HashMap<String,Double>> tmp=new DeepWalkMethod().getSimilarTarget( modelfile, idxfile, queries,  allTargets); 
		HashMap<String,HashMap<String,Double>> results=getAssociateDrugs(tmp, associations, allDrugs);
		return results;
	}
	
	
	
	public HashMap<String,HashMap<String, Double>> getAssociateDrugs(HashMap<String,HashMap<String,Double>> input, 
			HashMap<String,HashSet<String>> associations, HashSet<String> allDrug) throws IOException{
		HashMap<String,HashMap<String, Double>>  results=new HashMap<>();
		for(Entry<String,HashMap<String,Double>> entry1:input.entrySet()){
			HashMap<String,Double> tmp= new HashMap<>();
			for(Entry<String,Double> entry:entry1.getValue().entrySet()){
					if(associations.containsKey(entry.getKey())){
						for(String target:associations.get(entry.getKey())){
							if(allDrug.contains(target)){
								if(!associations.get(entry1.getKey()).contains(target)){
									if(tmp.containsKey(target)){
										tmp.put(target, tmp.get(target)+entry.getValue());
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
