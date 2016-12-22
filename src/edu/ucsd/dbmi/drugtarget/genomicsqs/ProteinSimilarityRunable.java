package edu.ucsd.dbmi.drugtarget.genomicsqs;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;

public class ProteinSimilarityRunable implements Runnable {
	
	HashMap<String,String> idx;
	HashSet<String> pairs;
	String name;
	File file;
	public ProteinSimilarityRunable(HashSet<String> pairs, HashMap<String,String> idx, File file,String name){
		this.idx=idx;
		this.pairs=pairs;
		this.name=name;
		this.file=file;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		BufferedWriter bw=null;
		try {
			bw = new BufferedWriter(new FileWriter(file));
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		System.out.println(name+" is running now, size: "+pairs.size());
		int i=0;
		for(String pair:pairs){
			i++;
			if(i%1000==0){
				try {
					bw.flush();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			String[] elements=pair.split(" ");
			
			String tmp1 = null;
			try {
				tmp1 = new  String(idx.get(elements[0]).trim().getBytes(),"UTF-8");
			} catch (UnsupportedEncodingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			String tmp2 = null;
			try {
				tmp2 = new  String(idx.get(elements[1]).trim().getBytes(),"UTF-8");
			} catch (UnsupportedEncodingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			StringBuffer sb1 =new StringBuffer();
			for(String e:tmp1.split("\\\\n")){
				sb1.append(e+" \n");
			}
			StringBuffer sb2 =new StringBuffer();
			for(String e:tmp2.split("\\\\n")){
				sb2.append(e+" \n");
			}
			
			String protein1=sb1.toString();
			String protein2=sb2.toString();
			
				String str1 =(new FastaSequence(protein1)).getSequence();
				String str2 =(new FastaSequence(protein2)).getSequence();
				Boolean error=false;
				String msg="";
				Double value=0.0;
				try{
				Double score0=0.0;
				SmithWaterman sw0=new SmithWaterman(str1, str2);
				score0=sw0.getAlignmentScore();
				SmithWaterman sw1=new SmithWaterman(str1, str1);
				Double score1=sw1.getAlignmentScore();
				SmithWaterman sw2=new SmithWaterman(str2, str2);
				Double score2=sw2.getAlignmentScore();
				
				value=score0/(Math.sqrt(score1)*Math.sqrt(score2));
				}catch(Exception e){
//					System.err.println("eorros happens in : "+pair);
					msg=e.toString();
					error=true;
				}
			try {
				if(error){
					bw.write(elements[0]+" "+elements[1]+" "+msg+"\n");
				}else{
					bw.write(elements[0]+" "+elements[1]+" "+value+" \n");
				}
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		try {
			bw.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			bw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
