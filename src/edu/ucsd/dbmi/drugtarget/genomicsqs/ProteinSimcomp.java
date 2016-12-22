package edu.ucsd.dbmi.drugtarget.genomicsqs;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ProteinSimcomp {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	public HashMap<String,String> idx;
	public HashSet<String> sourceProtein;

	public ProteinSimcomp () throws IOException{
		idx=new ProteinCollector().getProtein("data/input/drugbank/drugbank_dump.nt");
		sourceProtein=new ProteinCollector().getSourceProtein("D:/data/drug-target/input/tri/external/connected/external_connected_filtered.nt");
	}
	
	public void getBatchScores(String outfolder) throws InterruptedException, IOException{
		int k=0;
		HashSet<String> computations=new HashSet<>();
		for (String source:sourceProtein) {
			for(Entry<String,String> entry:idx.entrySet()){
				computations.add(source+" "+entry.getKey());
			}	
		}
		
		ArrayList<String> computationList=new ArrayList<>();
		for(String string:computations){
			computationList.add(string);
		}
		int size=computations.size()/10;
		
		ExecutorService pool = Executors.newFixedThreadPool(10);
		for (int i = 0; i < 10; i++) {
			HashSet<String> set=new HashSet<>(); 
			for (int j = size*i; j < size*(i+1)&j<computationList.size(); j++) {
				set.add(computationList.get(j));
			}
			pool.execute(new ProteinSimilarityRunable(set, idx, new File(outfolder+"/thread_"+i+".txt"),"@ Thread_"+i));
		}
		
		pool.shutdown(); // Disable new tasks from being submitted

		while(!pool.awaitTermination(10, TimeUnit.MINUTES))
		{	
			  Date now = new Date(); 
		      Calendar cal = Calendar.getInstance(); 
		      DateFormat d2 = DateFormat.getDateTimeInstance(); 
		      String str2 = d2.format(now); 
		      
			System.out.println(str2);
		}
		
		
	}
}
