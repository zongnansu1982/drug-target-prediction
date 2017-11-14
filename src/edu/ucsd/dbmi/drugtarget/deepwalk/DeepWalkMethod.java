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

}
