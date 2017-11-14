package edu.ucsd.dbmi.drugtarget.chemicstrc;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import edu.ucsd.dbmi.drugtarget.genomicsqs.ProteinSimcomp;



public class Simcomp {

	public static void main(String[] args) throws IOException, InterruptedException {
		// TODO Auto-generated method stub

		
	}
	
	public HashMap<String, String> ids;
	public HashSet<String> drugs;
	public HashSet<String> sourceDrugs;
	public Simcomp(String input, String output) throws IOException{

	}
	
	public Simcomp(String input1, String input2,String outfolder) throws IOException, InterruptedException{
		ids=new HashMap<>();
		drugs=new HashSet<>();
		sourceDrugs=new HashSet<>();
		DrugCollector collector=new DrugCollector(input1,input2);
		ids=collector.getIds();
		drugs=collector.getDrugs();
		sourceDrugs=collector.getSourceDrugs();
	}
	

	
	
	public void getBatchScores(String outfolder) throws InterruptedException, IOException{
		System.err.println(sourceDrugs.size());
		System.err.println(drugs.size());
		ArrayList<String> list=new ArrayList<>();
		for(String drug:sourceDrugs){
			list.add(drug);
		}
		int k=0;
		HashSet<String> computations=new HashSet<>();
		for (int i = 0; i < list.size()-1; i++) {
			for (int j = i+1; j < list.size(); j++) {
				computations.add(list.get(i)+" "+list.get(j));
			}
		}
		for (int i = 0; i < list.size(); i++) {
			for(String drug:drugs){
				if(!list.get(i).equals(drug)&!sourceDrugs.contains(drug)){
					computations.add(list.get(i)+" "+drug);
				}
			}	
		}
		
		ArrayList<String> computationList=new ArrayList<>();
		for(String string:computations){
			computationList.add(string);
		}
		int size=computations.size()/100;
		
		ExecutorService pool = Executors.newFixedThreadPool(100);
		for (int i = 0; i < 100; i++) {
			HashSet<String> set=new HashSet<>(); 
			for (int j = size*i; j < size*(i+1)&j<computationList.size(); j++) {
				set.add(computationList.get(j));
			}
			pool.execute(new SimilarityRunable(set, ids, new File(outfolder+"/thread_"+i+".txt"),"@ Thread_"+i));
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
	
	
	public void getScore(String drug1, String drug2, BufferedWriter bw) throws IOException {
			try{
				 String str="http://rest.genome.jp/simcomp2/" + drug1 + "/" + drug2 + "/cutoff=0.0001";  
			        URL myURL = new URL(str);  
			        HttpURLConnection conn = (HttpURLConnection) myURL.openConnection();  
			        InputStream is=conn.getInputStream();  
			        InputStreamReader isr=new InputStreamReader(is);  
			        BufferedReader br=new BufferedReader(isr);  
			        StringBuffer buffer=new StringBuffer();  
			        String line=null;  
			        while(null!=(line=br.readLine()))  
			        {  
			            buffer.append(line);  
			        }  
			        br.close();  
			        isr.close();  
			        is.close();  
			        if(buffer.toString()==null||buffer.toString()==""||buffer.toString().isEmpty()){
			        	bw.write(drug1+" "+drug2+" NA\n");
			        }else{
			        	String[] elements=buffer.toString().split("	");
				        bw.write(drug1+" "+drug2+" "+elements[2]+"\n");	
			        }
			}catch(Exception e){
					bw.write(drug1+" "+drug2+" NA\n");
					bw.flush();
			}
	       
	}
}
