package edu.ucsd.dbmi.drugtarget.main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
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

import org.semanticweb.yars.nx.Node;
import org.semanticweb.yars.nx.parser.NxParser;

import edu.ucsd.dbmi.drugtarget.deepwalk.DeepWalkMethod;
import edu.ucsd.dbmi.drugtarget.predict.DrugPrediction;
import edu.ucsd.dbmi.drugtarget.predict.TargetPrediction;

public class Job {
	
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub

		Job testJob=new Job("data/input/source/network.nt", "data/input/deep/deepwalk_external_100.txt", "data/input/deep/deepwalkidx_external_100.txt");
		HashMap<String,HashMap<String, Double>> results=testJob.executePredict(Job.deepwalk_similarity,Job.dbsi_prediction,
				"<http://www4.wiwiss.fu-berlin.de/drugbank/resource/drugs/DB01110>");
		print(results,5);
	}
	
	public static final String deepwalk_similarity="Deepwalk";

	
	public static final String dbsi_prediction="DBSI";
	public static final String tbsi_prediction="TBSI";
	
	public String datafile;
	public String modelfile;
	public String idxfile;
	
	public Job(String datafile, String modelfile, String idxfile){
		this.datafile=datafile;
		this.modelfile=modelfile;
		this.idxfile=idxfile;
	}
	
	public HashMap<String,HashMap<String, Double>> executePredict(String similarity_measure, String prediction_type, String query) throws Exception{
//		TargetPrediction predict = new TargetPrediction();  use default model files and idx files of deepwalk
		
		HashSet<String> allDrugs=getAllDrugs(datafile);
		HashSet<String> allTargets=getAllTargets(datafile);
		HashMap<String,HashSet<String>> drugTargetAssociations=getDrugTargetAssociations(datafile);
		HashMap<String,HashSet<String>> targetDrugAssociations=getDrugTargetAssociations(datafile);
		
		
		HashMap<String,HashMap<String, Double>> results = null;
		
		if(prediction_type.equals(dbsi_prediction)){
			
			TargetPrediction predict = new TargetPrediction(modelfile,idxfile);
			results = predict.predict(query,
					drugTargetAssociations, allDrugs,allTargets);
			
		}else if(prediction_type.equals(tbsi_prediction)){
			DrugPrediction predict = new DrugPrediction(modelfile,idxfile);
			results = predict.predict(query,
					drugTargetAssociations, allDrugs,allTargets);
		}
		
		return results;
	}
	
	
	public HashMap<String,HashMap<String, Double>> executePredict(String similarity_measure, String prediction_type, HashSet<String> quries) throws Exception{
//		TargetPrediction predict = new TargetPrediction();  use default model files and idx files of deepwalk
		
		HashSet<String> allDrugs=getAllDrugs(datafile);
		HashSet<String> allTargets=getAllTargets(datafile);
		HashMap<String,HashSet<String>> drugTargetAssociations=getDrugTargetAssociations(datafile);
		TargetPrediction predict = new TargetPrediction(modelfile,idxfile);
		HashMap<String,HashMap<String, Double>> results = null;
		
		if(prediction_type.equals(dbsi_prediction)){
		
			results = predict.predict(quries,
					drugTargetAssociations, allDrugs,allTargets);
			
		}else if(prediction_type.equals(tbsi_prediction)){
			
			results = predict.predict(quries,
					drugTargetAssociations, allDrugs,allTargets);
		}
		return results;
	}
	
	public void executeTraining(int vector, int window) throws Exception{
		new DeepWalkMethod().training(datafile,  modelfile,  idxfile,  vector, window);
	}
	
	
	public HashSet<String> getAllTargets(String datafile) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(new File(datafile)));
		String line = null;
		HashSet<String> set = new HashSet<>();
		while ((line = br.readLine()) != null) {
			InputStream inputStream = new ByteArrayInputStream(line.getBytes());
			NxParser nxp = new NxParser();
			nxp.parse(inputStream);

			while (nxp.hasNext()) {
				Node[] quard = nxp.next();
				String s = quard[0].toString().trim();
				String p = quard[1].toString().trim();
				String o = quard[2].toString().trim();

				if (s.startsWith("<http://www4.wiwiss.fu-berlin.de/drugbank/resource/drugs/")
						& p.equals("<http://www4.wiwiss.fu-berlin.de/drugbank/resource/drugbank/target>")
						& o.startsWith("<http://www4.wiwiss.fu-berlin.de/drugbank/resource/targets/")) {
					set.add(o);
				}
			}
		}
		return set;
	}

	public HashSet<String> getAllDrugs(String datafile) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(new File(datafile)));
		String line = null;
		HashSet<String> set = new HashSet<>();
		while ((line = br.readLine()) != null) {
			InputStream inputStream = new ByteArrayInputStream(line.getBytes());
			NxParser nxp = new NxParser();
			nxp.parse(inputStream);

			while (nxp.hasNext()) {
				Node[] quard = nxp.next();
				String s = quard[0].toString().trim();
				String p = quard[1].toString().trim();
				String o = quard[2].toString().trim();

				if (s.startsWith("<http://www4.wiwiss.fu-berlin.de/drugbank/resource/drugs/")
						& p.equals("<http://www4.wiwiss.fu-berlin.de/drugbank/resource/drugbank/target>")
						& o.startsWith("<http://www4.wiwiss.fu-berlin.de/drugbank/resource/targets/")) {
					set.add(s);
				}
			}
		}
		return set;
	}

	public HashMap<String, HashSet<String>> getDrugTargetAssociations(String datafile) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(new File(datafile)));
		HashMap<String, List<Map.Entry<String, Double>>> results = new HashMap<>();
		String line = null;
		HashMap<String, HashSet<String>> associations = new HashMap<>();
		while ((line = br.readLine()) != null) {
			if (!line.contains("\"")) {
				InputStream inputStream = new ByteArrayInputStream(line.getBytes());
				NxParser nxp = new NxParser();
				nxp.parse(inputStream);
				while (nxp.hasNext()) {

					Node[] quard = nxp.next();
					String s = quard[0].toString().trim();
					String p = quard[1].toString().trim();
					String o = quard[2].toString().trim();
					if (s.startsWith("<http://www4.wiwiss.fu-berlin.de/drugbank/resource/drugs/")
							& p.equals("<http://www4.wiwiss.fu-berlin.de/drugbank/resource/drugbank/target>")
							& o.startsWith("<http://www4.wiwiss.fu-berlin.de/drugbank/resource/targets/")) {

						if (associations.containsKey(s)) {
							associations.get(s).add(o);
						} else {
							HashSet<String> set = new HashSet<>();
							set.add(o);
							associations.put(s, set);
						}

					}
				}
			}
		}
		return associations;
	}

	public HashMap<String, HashSet<String>> getTargetDrugAssociations(String datafile) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(new File(datafile)));
		HashMap<String, List<Map.Entry<String, Double>>> results = new HashMap<>();
		String line = null;
		HashMap<String, HashSet<String>> associations = new HashMap<>();
		while ((line = br.readLine()) != null) {
			if (!line.contains("\"")) {
				InputStream inputStream = new ByteArrayInputStream(line.getBytes());
				NxParser nxp = new NxParser();
				nxp.parse(inputStream);
				while (nxp.hasNext()) {

					Node[] quard = nxp.next();
					String s = quard[0].toString().trim();
					String p = quard[1].toString().trim();
					String o = quard[2].toString().trim();
					if (s.startsWith("<http://www4.wiwiss.fu-berlin.de/drugbank/resource/drugs/")
							& p.equals("<http://www4.wiwiss.fu-berlin.de/drugbank/resource/drugbank/target>")
							& o.startsWith("<http://www4.wiwiss.fu-berlin.de/drugbank/resource/targets/")) {

						if (associations.containsKey(o)) {
							associations.get(o).add(s);
						} else {
							HashSet<String> set = new HashSet<>();
							set.add(s);
							associations.put(o, set);
						}

					}
				}
			}
		}
		return associations;
	}
	
public static  HashMap<String,List<HashMap.Entry<String, Double>>> sort(HashMap<String,HashMap<String, Double>> results){
		
		HashMap<String,HashMap<String, Double>> copy=new HashMap<>();
		for(Entry<String,HashMap<String, Double>> entry1:results.entrySet()){
			HashMap<String,Double> map=new HashMap<>();
			for(Entry<String,Double> entry2:entry1.getValue().entrySet()){
				map.put(entry2.getKey(), entry2.getValue());
			}
			copy.put(entry1.getKey(), map);
		}
		
		HashMap<String,List<HashMap.Entry<String, Double>>> map=new HashMap<>();
		for(Entry<String,HashMap<String, Double>> entry:copy.entrySet()){
			List<Map.Entry<String, Double>> list = new ArrayList<Map.Entry<String, Double>>(entry.getValue().entrySet());  
			  
			Collections.sort(list, new Comparator<Map.Entry<String, Double>>() {  
			    @Override  
			    public int compare(Entry<String, Double> o1, Entry<String, Double> o2) {  
			         return o2.getValue().compareTo(o1.getValue()); // 降序  
//			        return o1.getValue().compareTo(o2.getValue()); // 升序  
			    }  
			}); 
			Double max=list.get(0).getValue();
			Double min=list.get(list.size()-1).getValue();
			
			for (int i = 0; i < list.size(); i++) {
				list.get(i).setValue((list.get(i).getValue()-min)/(max-min));
			}
			
			map.put(entry.getKey(), list);
			
		}
		return map;
	}
	
	public static void print(HashMap<String,HashMap<String, Double>> results){
		HashMap<String,List<HashMap.Entry<String, Double>>> map=sort(results);
	for(Entry<String,List<HashMap.Entry<String, Double>>> entry1:map.entrySet()){
		System.out.println("query: "+entry1.getKey());
		for(Entry<String,Double> entry2:entry1.getValue()){
			System.out.println("results of "+entry1.getKey()+" is: " +entry2.getKey()+" scores: "+entry2.getValue());
		}
		}
	}
	public static void print(HashMap<String,HashMap<String, Double>> results, int topN){
		HashMap<String,List<HashMap.Entry<String, Double>>> map=sort(results);
	for(Entry<String,List<HashMap.Entry<String, Double>>> entry:map.entrySet()){
		System.out.println("query: "+entry.getKey());
		for (int i = 0; i < entry.getValue().size()&&i<topN; i++) {
			System.out.println("results of "+entry.getKey()+" is: " +entry.getValue().get(i).getKey()+" scores: "+entry.getValue().get(i).getValue());
		}
	}
}
	
}
