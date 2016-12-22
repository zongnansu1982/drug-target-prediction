package edu.ucsd.dbmi.drugtarget.deepwalk;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.deeplearning4j.graph.api.Vertex;
import org.deeplearning4j.graph.graph.Graph;
import org.deeplearning4j.graph.iterator.RandomWalkIterator;
import org.deeplearning4j.graph.models.GraphVectors;
import org.deeplearning4j.graph.models.deepwalk.DeepWalk;
import org.deeplearning4j.graph.models.loader.GraphVectorSerializer;
import org.semanticweb.yars.nx.Node;
import org.semanticweb.yars.nx.parser.NxParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DeepWalkMethod  {
	
	public static void main(String[] args) throws Exception {
		

   	 for (int i = 1; i < 11; i++) {
   	
   	    	String datafile="data/input/drugCloud/data_bi_"+i*100+".nt";
   	    	String modelfile="data/input/drugCloud/deepwalk_bi_"+i+100+"_100.txt";
   	    	String idxfile="data/input/drugCloud/deepwalkidx_bi_"+i+100+"_100.txt";
   	    	String removefile="data/input/drugCloud/removed_bi_"+i*100+".nt";
   	    	
   	    	new DeepWalkMethod().training(datafile,  modelfile,  idxfile,  100, 5);
   	 
   	 }
}

	
	private static Logger log = LoggerFactory.getLogger(DeepWalkMethod.class);
	
	public void training(String input, String modelfile, String idxfile, int vector, int windowsize) throws Exception {
		// TODO Auto-generated method stub
		HashSet<String> nodes= new HashSet<>();
		HashSet<String> triples= new HashSet<>();
		BufferedReader br = new BufferedReader(new FileReader(new File(input)));
		String line=null;
		while((line=br.readLine())!=null){
			InputStream inputStream = new ByteArrayInputStream(line.getBytes());
			NxParser nxp = new NxParser();
			nxp.parse(inputStream);
			
			while (nxp.hasNext()) {
				Node[] quard = nxp.next();
				String s = quard[0].toString().trim();
				String p = quard[1].toString().trim();
				String o = quard[2].toString().trim();
				
				if(s.startsWith("<http://")&o.startsWith("<http://")){
					nodes.add(s);
					nodes.add(o);
					triples.add(s+" "+p+" "+o);
				}
			}
		}
		BufferedWriter bw = new BufferedWriter(new FileWriter(new File(idxfile)));
		ArrayList<Vertex<String>> vlist= new ArrayList<Vertex<String>>();
		HashMap<String,Integer> idx= new HashMap<>();
		int i=0;
		for(String node:nodes){
			Vertex<String> v= new Vertex<String>(i, node);
			vlist.add(v);
			bw.write(i+" "+node+"\n");
			idx.put(node, i);
			i++;
		}
		bw.flush();
		bw.close();
		
		Graph<String, String> graph=new Graph<>(vlist);
		for(String triple:triples){
			String[] elements=triple.split(" ");
			graph.addEdge(idx.get(elements[0]), idx.get(elements[2]), elements[1], false); // undirected graph, original code
		}
		
//		for (int j = 0; j < nodes.size(); j++) {
//			if(graph.getVertexDegree(j)==0){
//				graph.addEdge(j, j, "<http://www.w3.org/2002/07/owl#sameAs>", true);
//			}
//		}
		
		 RandomWalkIterator iter = new RandomWalkIterator<>(graph, 40);
	        // Split on white spaces in the line to get words

	     log.info("Building model....");
	        
	     DeepWalk<String, String> walk = new DeepWalk.Builder().vectorSize(vector).windowSize(windowsize).build();
	     walk.initialize(graph);
	     log.info("Fitting Word2Vec model....");
	     walk.fit(iter);

	     log.info("Writing word vectors to text file....");

	        // Write word vectors
	    
	     GraphVectorSerializer.writeGraphVectors(walk, modelfile);
		
		
	}

	
	
//	public HashMap<String,Double> getSimilarDrug(String modelfile, String idxfile, HashSet<String> queries, HashSet<String> allDrugs) throws Exception {
//		// TODO Auto-generated method stub
//		BufferedReader br = new BufferedReader(new FileReader(new File(idxfile)));
//		String line=null;
//		HashMap<String,Integer> idx= new HashMap<>();
//		HashMap<Integer,String> iidx= new HashMap<>();
//		while((line=br.readLine())!=null){
//			String[] elements=line.split(" ");
//			idx.put(elements[1], Integer.valueOf(elements[0]));
//			iidx.put(Integer.valueOf(elements[0]), elements[1]);
//		}
//		
//		HashMap<String,Double> map=new HashMap<>();
//		GraphVectors graphvector = GraphVectorSerializer.loadTxtVectors(new File(modelfile));
//		for(String query:queries){
//			
//			for(String drug:allDrugs){
//				if(graphvector.similarity(idx.get(query), idx.get(drug))>0.0&!queries.contains(drug)){
//	        		if(map.containsKey(drug)){
//	        		map.put(drug, graphvector.similarity(idx.get(query), idx.get(drug))+map.get(drug));
//	        		}else{
//	        		map.put(drug, graphvector.similarity(idx.get(query), idx.get(drug)));
//	        		}
//				}
//			}
//		}
//		return map;
//	}
	
	
	 public HashMap<String,HashMap<String,Double>> getSimilarDrug(String modelfile, String idxfile,HashSet<String> queries,
	    		HashSet<String> allDrugs) throws Exception {
			// TODO Auto-generated method stub
		 BufferedReader br = new BufferedReader(new FileReader(new File(idxfile)));
			String line=null;
			HashMap<String,Integer> idx= new HashMap<>();
			HashMap<Integer,String> iidx= new HashMap<>();
			while((line=br.readLine())!=null){
				String[] elements=line.split(" ");
				idx.put(elements[1], Integer.valueOf(elements[0]));
				iidx.put(Integer.valueOf(elements[0]), elements[1]);
			}
			
		 
		 HashMap<String,HashMap<String,Double>> map=new HashMap<>();
		 GraphVectors graphvector = GraphVectorSerializer.loadTxtVectors(new File(modelfile));
			for(String query:queries){
				HashMap<String,Double> amap=new HashMap<>();
				
				for(String drug:allDrugs){
					if(!query.equals(drug)){
//						System.out.println(drug +" "+idx.get(drug));
						amap.put(drug, graphvector.similarity(idx.get(query), idx.get(drug)));
					}
				}
				
				map.put(query, amap);
			}
			
			return map;
		}
	 
	 
	/**
	 * search from drug
	 * @param modelfile
	 * @param idxfile
	 * @param queries
	 * @param topN
	 * @return
	 * @throws Exception
	 */
	 public HashMap<String,HashMap<String,Double>> getSimilarDrug(String modelfile, String idxfile, HashSet<String> queries, int topN) throws Exception {
			// TODO Auto-generated method stub
		 	BufferedReader br = new BufferedReader(new FileReader(new File(idxfile)));
			String line=null;
			HashMap<String,Integer> idx= new HashMap<>();
			HashMap<Integer,String> iidx= new HashMap<>();
			while((line=br.readLine())!=null){
				String[] elements=line.split(" ");
				idx.put(elements[1], Integer.valueOf(elements[0]));
				iidx.put(Integer.valueOf(elements[0]), elements[1]);
			}
			
			
			HashMap<String,HashMap<String,Double>> map=new HashMap<>();
			GraphVectors graphvector = GraphVectorSerializer.loadTxtVectors(new File(modelfile));
			for(String query:queries){
				HashMap<String,Double> amap=new HashMap<>();
//				System.out.println(query+" "+idx.get(query));
				if(idx.containsKey(query)){
					int[] lst=graphvector.verticesNearest(idx.get(query), topN*10);
					for(int i:lst){
						if(amap.size()<topN){
							String target=iidx.get(lst[i]);
				        	if(target.startsWith("<http://www4.wiwiss.fu-berlin.de/drugbank/resource/drugs/")&graphvector.similarity(idx.get(query), lst[i])>0.0){
				        		amap.put(target, graphvector.similarity(idx.get(query), lst[i]));
						}
						}
					}		
				}
				
				map.put(query, amap);
			}
			
			return map;
		}
	
	 public HashMap<String,HashMap<String,Double>> getSimilarTarget(String modelfile, String idxfile,HashSet<String> queries, HashSet<String> allTargets) throws Exception {
			// TODO Auto-generated method stub
		 BufferedReader br = new BufferedReader(new FileReader(new File(idxfile)));
			String line=null;
			HashMap<String,Integer> idx= new HashMap<>();
			HashMap<Integer,String> iidx= new HashMap<>();
			while((line=br.readLine())!=null){
				String[] elements=line.split(" ");
				idx.put(elements[1], Integer.valueOf(elements[0]));
				iidx.put(Integer.valueOf(elements[0]), elements[1]);
			}
			
	    	HashMap<String,HashMap<String,Double>> map=new HashMap<>();
	    	GraphVectors graphvector = GraphVectorSerializer.loadTxtVectors(new File(modelfile));
			for(String query:queries){
				HashMap<String,Double> amap=new HashMap<>();
				for(String target:allTargets){
					if(!query.equals(target)){
						amap.put(target, graphvector.similarity(idx.get(query), idx.get(target)));
					}
				}
				map.put(query, amap);
			}
			return map;
		}
	 
//	 public HashMap<String,Double> getSimilarTarget(String modelfile, String idxfile,HashSet<String> queries, HashSet<String> allTargets) throws Exception {
//			// TODO Auto-generated method stub
//		 BufferedReader br = new BufferedReader(new FileReader(new File(idxfile)));
//			String line=null;
//			HashMap<String,Integer> idx= new HashMap<>();
//			HashMap<Integer,String> iidx= new HashMap<>();
//			while((line=br.readLine())!=null){
//				String[] elements=line.split(" ");
//				idx.put(elements[1], Integer.valueOf(elements[0]));
//				iidx.put(Integer.valueOf(elements[0]), elements[1]);
//			}
//				
//		 
//		 HashMap<String,Double> map=new HashMap<>();
//		 GraphVectors graphvector = GraphVectorSerializer.loadTxtVectors(new File(modelfile));
//			for(String query:queries){
//				
//				for(String target:allTargets){
//					if(graphvector.similarity(idx.get(query), idx.get(target))>0.0&!queries.contains(target)){
//		        		if(map.containsKey(target)){
//		        		map.put(target, graphvector.similarity(idx.get(query), idx.get(target))+map.get(target));
//		        		}else{
//		        		map.put(target, graphvector.similarity(idx.get(query), idx.get(target)));
//		        		}
//					
//					}
//				}
//			}
//			return map;
//		}
	 
	 
	 /**
	  * search from target
	  * @param modelfile
	  * @param queries
	  * @param topN
	  * @return
	  * @throws Exception
	  */
	 public HashMap<String,HashMap<String,Double>> getSimilarTarget(String modelfile, String idxfile, HashSet<String> queries, int topN) throws Exception {
			// TODO Auto-generated method stub
		 BufferedReader br = new BufferedReader(new FileReader(new File(idxfile)));
			String line=null;
			HashMap<String,Integer> idx= new HashMap<>();
			HashMap<Integer,String> iidx= new HashMap<>();
			while((line=br.readLine())!=null){
				String[] elements=line.split(" ");
				idx.put(elements[1], Integer.valueOf(elements[0]));
				iidx.put(Integer.valueOf(elements[0]), elements[1]);
			}
			
		 
		 HashMap<String,HashMap<String,Double>> map=new HashMap<>();
			GraphVectors graphvector = GraphVectorSerializer.loadTxtVectors(new File(modelfile));
			for(String query:queries){
				HashMap<String,Double> amap=new HashMap<String,Double>();
				if(idx.containsKey(query)){
					int[] lst=graphvector.verticesNearest(idx.get(query), topN*10);
					for(int i:lst){
						if(amap.size()<topN){
							String target=iidx.get(lst[i]);
				        	if(target.startsWith("<http://www4.wiwiss.fu-berlin.de/drugbank/resource/targets/")&graphvector.similarity(idx.get(query), lst[i])>0.0){
				        		amap.put(target, graphvector.similarity(idx.get(query), lst[i]));
				        	}
						}
					}	
				}
				
				map.put(query, amap);
			}
			
			return map;
		}
	 
	 public HashMap<String,HashMap<String, Double>> getSimilarDrug(String modelfile, String idxfile, HashMap<String,HashSet<String>> queries, 
	    		HashMap<String,HashSet<String>> associations, HashSet<String> allTarget) throws Exception {
	  		// TODO Auto-generated method stub
	  		
		 BufferedReader br = new BufferedReader(new FileReader(new File(idxfile)));
			String line=null;
			HashMap<String,Integer> idx= new HashMap<>();
			HashMap<Integer,String> iidx= new HashMap<>();
			while((line=br.readLine())!=null){
				String[] elements=line.split(" ");
				idx.put(elements[1], Integer.valueOf(elements[0]));
				iidx.put(Integer.valueOf(elements[0]), elements[1]);
			}
			
		 HashMap<String,HashMap<String, Double>> map=new HashMap<>();
		 GraphVectors graphvector = GraphVectorSerializer.loadTxtVectors(new File(modelfile));
	  		
	  		for(Entry<String,HashSet<String>> entry:queries.entrySet()){
	  			HashMap<String,Double> rankings=new HashMap<>();
	  			for(String query:entry.getValue()){
	  				for(String target:allTarget){
	  					if(!entry.getValue().contains(target)){ // this is same with existing
	  						Double sim=graphvector.similarity(idx.get(query), idx.get(target));
				        		if(rankings.containsKey(target)){
				        			rankings.put(target, sim+rankings.get(target));
				        		}else{
				        			rankings.put(target, sim);
				        		}
	  						}
	  					}
	  				}
	  			map.put(entry.getKey(), rankings);
	  		}
	  		
	  		return map;
	  	}
	 
	 public List<Map.Entry<String, Double>> getSimilarDrug(String modelfile, String idxfile,HashMap<String,HashSet<String>> queries,HashSet<String> candidates) throws Exception {
			// TODO Auto-generated method stub
		 BufferedReader br = new BufferedReader(new FileReader(new File(idxfile)));
			String line=null;
			HashMap<String,Integer> idx= new HashMap<>();
			HashMap<Integer,String> iidx= new HashMap<>();
			while((line=br.readLine())!=null){
				String[] elements=line.split(" ");
				idx.put(elements[1], Integer.valueOf(elements[0]));
				iidx.put(Integer.valueOf(elements[0]), elements[1]);
			}
			
			HashMap<String,List<Map.Entry<String, Double>>> map=new HashMap<>();
			GraphVectors graphvector = GraphVectorSerializer.loadTxtVectors(new File(modelfile));
			
			HashMap<String,Double> rankings=new HashMap<>();
			HashSet<String> drugs=new HashSet<>();
			for(Entry<String,HashSet<String>> entry:queries.entrySet()){
				drugs.addAll(entry.getValue());
			}
			
			
				
			for(String d:drugs){
				for(String drug:candidates){
					if(!drugs.contains(drug)
			        			&graphvector.similarity(idx.get(d), idx.get(drug))>0.0){
			        	Double sim=graphvector.similarity(idx.get(d), idx.get(drug));
			        		if(rankings.containsKey(drug)){
			        			rankings.put(drug, sim+rankings.get(drug));
			        		}else{
			        			rankings.put(drug, sim);
			        		}
						}
					}
					
				}
			for(String drug:candidates){
				if(!rankings.containsKey(drug)){
					rankings.put(drug, 0.0);
				}
			}
			
			List<Map.Entry<String, Double>> list = new ArrayList<Map.Entry<String, Double>>(rankings.entrySet());  
			  
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
			
//			System.err.println("========================================");
//			System.err.println(entry.getKey());
			
			return list;
		}
	 
	 /**
	  * search from target
	  * @param modelfile
	  * @param queries
	  * @param topN
	  * @return
	  * @throws Exception
	  */
	 public HashMap<String,List<Map.Entry<String, Double>>> getSimilarDrug(String modelfile, String idxfile,HashMap<String,HashSet<String>> queries, int topN) throws Exception {
			// TODO Auto-generated method stub
		 
		 	BufferedReader br = new BufferedReader(new FileReader(new File(idxfile)));
			String line=null;
			HashMap<String,Integer> idx= new HashMap<>();
			HashMap<Integer,String> iidx= new HashMap<>();
			while((line=br.readLine())!=null){
				String[] elements=line.split(" ");
				idx.put(elements[1], Integer.valueOf(elements[0]));
				iidx.put(Integer.valueOf(elements[0]), elements[1]);
			}
			
			HashMap<String,List<Map.Entry<String, Double>>> map=new HashMap<>();
			GraphVectors graphvector = GraphVectorSerializer.loadTxtVectors(new File(modelfile));
			for(Entry<String,HashSet<String>> entry:queries.entrySet()){
				HashMap<String,Double> rankings=new HashMap<>();
				for(String query:entry.getValue()){
					int targetCounter=0;
					if(idx.containsKey(query)){
						int[] lst=graphvector.verticesNearest(idx.get(query), topN*10);
						for(int i:lst){
							if(targetCounter<topN){
								String target=iidx.get(lst[i]);
					        	if(target.startsWith("<http://www4.wiwiss.fu-berlin.de/drugbank/resource/drugs/")&!entry.getValue().contains(target)
					        			&graphvector.similarity(idx.get(query), lst[i])>0.0){
					        		targetCounter++;
					        		Double sim=graphvector.similarity(idx.get(query), lst[i]);
					        		if(rankings.containsKey(target)){
					        			rankings.put(target, sim+rankings.get(target));
					        		}else{
					        			rankings.put(target, sim);
					        		}
					        	}
							}
						}	
					}
					
				}
				
				List<Map.Entry<String, Double>> list = new ArrayList<Map.Entry<String, Double>>(rankings.entrySet());  
	    		  
	    		Collections.sort(list, new Comparator<Map.Entry<String, Double>>() {  
	    		    @Override  
	    		    public int compare(Entry<String, Double> o1, Entry<String, Double> o2) {  
	    		         return o2.getValue().compareTo(o1.getValue()); // 降序  
//	    		        return o1.getValue().compareTo(o2.getValue()); // 升序  
	    		    }  
	    		}); 
	    		
//	    		System.err.println("========================================");
//	    		System.err.println(entry.getKey());
	    		
	    		
	    		
	    		Double max=list.get(0).getValue();
	    		int length=0;
	    		Double min=0.0;
	    		if(topN>list.size()){
	    			length=list.size();
	    		}else{
	    			length=topN;
	    		}
	    		min=list.get(length-1).getValue();
	    		Double nor=max-min;
	    		
	    		for (int i = 0; i < length; i++) {
	    			list.get(i).setValue((list.get(i).getValue()-min)/nor);	
	    			
	    		
//	    			System.err.println(list.get(i).getKey()+" "+list.get(i).getValue());
//	    			System.err.println("========================================");
	    		}
	    		
	    		map.put(entry.getKey(), list.subList(0, length-1));
			}
			
			return map;
		}
	 
	 public HashMap<String,HashMap<String, Double>> getSimilarTarget(String modelfile, String idxfile,HashMap<String,HashSet<String>> queries, 
	    		HashMap<String,HashSet<String>> associations, HashSet<String> allTarget) throws Exception {
	  		// TODO Auto-generated method stub
		 BufferedReader br = new BufferedReader(new FileReader(new File(idxfile)));
			String line=null;
			HashMap<String,Integer> idx= new HashMap<>();
			HashMap<Integer,String> iidx= new HashMap<>();
			while((line=br.readLine())!=null){
				String[] elements=line.split(" ");
				idx.put(elements[1], Integer.valueOf(elements[0]));
				iidx.put(Integer.valueOf(elements[0]), elements[1]);
			}	
		 
		 
		 HashMap<String,HashMap<String, Double>> map=new HashMap<>();
		 GraphVectors graphvector = GraphVectorSerializer.loadTxtVectors(new File(modelfile));
	  		
	  		for(Entry<String,HashSet<String>> entry:queries.entrySet()){
	  			HashMap<String,Double> rankings=new HashMap<>();
	  			for(String query:entry.getValue()){
	  				for(String target:allTarget){
	  					if(!entry.getValue().contains(target)){ // this is same with existing
	  						Double sim=graphvector.similarity(idx.get(query), idx.get(target));
	  						if(sim>0){
	  							rankings.put(target, sim);
	  						}	
	  						}
	  					}
	  				}
	  			map.put(entry.getKey(), rankings);
	  		}
	  		
	  		return map;
	  	}
	 
	 public List<Map.Entry<String, Double>> getSimilarTarget(String modelfile, String idxfile, HashMap<String,HashSet<String>> queries,HashSet<String> candidates) throws Exception {
			// TODO Auto-generated method stub
		 BufferedReader br = new BufferedReader(new FileReader(new File(idxfile)));
			String line=null;
			HashMap<String,Integer> idx= new HashMap<>();
			HashMap<Integer,String> iidx= new HashMap<>();
			while((line=br.readLine())!=null){
				String[] elements=line.split(" ");
				idx.put(elements[1], Integer.valueOf(elements[0]));
				iidx.put(Integer.valueOf(elements[0]), elements[1]);
			}
			
		 
			GraphVectors graphvector = GraphVectorSerializer.loadTxtVectors(new File(modelfile));
			
			HashMap<String,Double> rankings=new HashMap<>();
			HashSet<String> targets=new HashSet<>();
			for(Entry<String,HashSet<String>> entry:queries.entrySet()){
				targets.addAll(entry.getValue());
			}
			
			
				
			for(String t:targets){
				for(String target:candidates){
					if(!targets.contains(target)
			        			&graphvector.similarity(idx.get(t), idx.get(target))>0.0){
			        	Double sim=graphvector.similarity(idx.get(t), idx.get(target));
			        		if(rankings.containsKey(target)){
			        			rankings.put(target, sim+rankings.get(target));
			        		}else{
			        			rankings.put(target, sim);
			        		}
						}
					}
					
				}
				
			for(String target:candidates){
				if(!rankings.containsKey(target)){
					rankings.put(target, 0.0);
				}
			}
			
			
			List<Map.Entry<String, Double>> list = new ArrayList<Map.Entry<String, Double>>(rankings.entrySet());  
			  
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
			
//			System.err.println("========================================");
//			System.err.println(entry.getKey());
			
			return list;
		}
	 
	 
	 /**
	  * search from drug
	  * @param modelfile
	  * @param idxfile
	  * @param queries
	  * @param topN
	  * @return
	  * @throws Exception
	  */
	 public HashMap<String,List<Map.Entry<String, Double>>> getSimilarTarget(String modelfile, String idxfile, HashMap<String,HashSet<String>> queries, int topN) throws Exception {
			// TODO Auto-generated method stub
		 	
		 	BufferedReader br = new BufferedReader(new FileReader(new File(idxfile)));
			String line=null;
			HashMap<String,Integer> idx= new HashMap<>();
			HashMap<Integer,String> iidx= new HashMap<>();
			while((line=br.readLine())!=null){
				String[] elements=line.split(" ");
				idx.put(elements[1], Integer.valueOf(elements[0]));
				iidx.put(Integer.valueOf(elements[0]), elements[1]);
			}
			
			HashMap<String,List<Map.Entry<String, Double>>> map=new HashMap<>();
			GraphVectors graphvector = GraphVectorSerializer.loadTxtVectors(new File(modelfile));
			for(Entry<String,HashSet<String>> entry:queries.entrySet()){
				HashMap<String,Double> rankings=new HashMap<>();
				for(String query:entry.getValue()){
					int targetCounter=0;
					if(idx.containsKey(query)){
						int[] lst=graphvector.verticesNearest(idx.get(query), topN*10);
						
						for(int i:lst){
							if(targetCounter<topN){
								
					        	if(iidx.get(lst[i]).startsWith("<http://www4.wiwiss.fu-berlin.de/drugbank/resource/targets/")&!entry.getValue().contains(iidx.get(lst[i]))
					        			&graphvector.similarity(idx.get(query), lst[i])>0.0){
					        		targetCounter++;
					        		Double sim=graphvector.similarity(idx.get(query), lst[i]);
					        		if(rankings.containsKey(iidx.get(lst[i]))){
					        			rankings.put(iidx.get(lst[i]), sim+rankings.get(iidx.get(lst[i])));
					        		}else{
					        			rankings.put(iidx.get(lst[i]), sim);
					        		}
					        	}
							}
						}	
					}
					
				}
				
				List<Map.Entry<String, Double>> list = new ArrayList<Map.Entry<String, Double>>(rankings.entrySet());  
				  
				Collections.sort(list, new Comparator<Map.Entry<String, Double>>() {  
				    @Override  
				    public int compare(Entry<String, Double> o1, Entry<String, Double> o2) {  
				         return o2.getValue().compareTo(o1.getValue()); // 降序  
//				        return o1.getValue().compareTo(o2.getValue()); // 升序  
				    }  
				}); 
				
//				System.err.println("========================================");
//				System.err.println(entry.getKey());
				
				Double max=list.get(0).getValue();
				int length=0;
				Double min=0.0;
				if(topN>list.size()){
					length=list.size();
				}else{
					length=topN;
				}
				
				
				min=list.get(length-1).getValue();
				Double nor=max-min;
				
				for (int i = 0; i < length; i++) {
					list.get(i).setValue((list.get(i).getValue()-min)/nor);	
					
				
//					System.err.println(list.get(i).getKey()+" "+list.get(i).getValue());
//					System.err.println("========================================");
				}
				
				map.put(entry.getKey(), list.subList(0, length-1));
				
				
			}
			
			return map;
		}

}
