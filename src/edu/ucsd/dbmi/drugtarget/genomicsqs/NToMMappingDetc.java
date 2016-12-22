package edu.ucsd.dbmi.drugtarget.genomicsqs;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;

import org.semanticweb.yars.nx.Node;
import org.semanticweb.yars.nx.parser.NxParser;


public class NToMMappingDetc {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		new NToMMappingDetc().pdbToDrugBank() ;
		new NToMMappingDetc().keggToPdb() ;
		new NToMMappingDetc().hgncToDrugBank() ;
		new NToMMappingDetc().keggToHgnc() ;
	}
	
	
	public void pdbToDrugBank() throws IOException{
		HashMap<String,HashSet<String>> map1=new HashMap<>();
		HashMap<String,HashSet<String>> map2=new HashMap<>();
		BufferedReader br=new BufferedReader(new FileReader(new File("data/input/drugbank/drugbank_dump.nt")));
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
					
					if(s.startsWith("<http://www4.wiwiss.fu-berlin.de/drugbank/resource/targets/")
						&o.startsWith("<http://")&p.equals("<http://www4.wiwiss.fu-berlin.de/drugbank/resource/drugbank/pdbId>")){
						if(map1.containsKey(s)){
							map1.get(s).add(o);
						}else{
							HashSet<String> set=new HashSet<>();
							set.add(o);
							map1.put(s, set);
						}
						
						if(map2.containsKey(o)){
							map2.get(o).add(s);
						}else{
							HashSet<String> set=new HashSet<>();
							set.add(s);
							map2.put(o, set);
						}
					}
				}
		}
		
		for(Entry<String,HashSet<String>> entry:map1.entrySet()){
			if(entry.getValue().size()>1){
				System.out.println("pdbToDrugBank map1 is not 1:1");
				break;
			}
		}
		for(Entry<String,HashSet<String>> entry:map2.entrySet()){
			if(entry.getValue().size()>1){
				System.out.println("pdbToDrugBank map2 is not 1:1");
				break;
			}
		}
		
	}
	
	
	public void keggToPdb() throws IOException{
		HashMap<String,HashSet<String>> map1=new HashMap<>();
		HashMap<String,HashSet<String>> map2=new HashMap<>();
		
		BufferedReader br=new BufferedReader(new FileReader(new File("D:/data/input/kgg/kegg-genes.nq")));
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
					
					if(s.startsWith("<http://bio2rdf.org/kegg:hsa")
						&o.startsWith("<http://bio2rdf.org/pdb:")&p.equals("<http://bio2rdf.org/kegg_vocabulary:x-pdb>")){
						if(map1.containsKey(s)){
							map1.get(s).add(o);
						}else{
							HashSet<String> set=new HashSet<>();
							set.add(o);
							map1.put(s, set);
						}
						
						if(map2.containsKey(o)){
							map2.get(o).add(s);
						}else{
							HashSet<String> set=new HashSet<>();
							set.add(s);
							map2.put(o, set);
						}
					}
				}
		}
		for(Entry<String,HashSet<String>> entry:map1.entrySet()){
			if(entry.getValue().size()>1){
				System.out.println("keggToPdb map1 is not 1:1");
				break;
			}
		}
		for(Entry<String,HashSet<String>> entry:map2.entrySet()){
			if(entry.getValue().size()>1){
				System.out.println("keggToPdb map2 is not 1:1");
				break;
			}
		}
	}
	
	
	public void hgncToDrugBank() throws IOException{
		HashMap<String,HashSet<String>> map1=new HashMap<>();
		HashMap<String,HashSet<String>> map2=new HashMap<>();
		BufferedReader br=new BufferedReader(new FileReader(new File("data/input/drugbank/drugbank_dump.nt")));
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
					
					if(s.startsWith("<http://www4.wiwiss.fu-berlin.de/drugbank/resource/targets/")
						&o.startsWith("<http://")&p.equals("<http://www4.wiwiss.fu-berlin.de/drugbank/resource/drugbank/hgncId>")){
						if(map1.containsKey(s)){
							map1.get(s).add(o);
						}else{
							HashSet<String> set=new HashSet<>();
							set.add(o);
							map1.put(s, set);
						}
						
						if(map2.containsKey(o)){
							map2.get(o).add(s);
						}else{
							HashSet<String> set=new HashSet<>();
							set.add(s);
							map2.put(o, set);
						}
					}
					
				}
		}
		
		for(Entry<String,HashSet<String>> entry:map1.entrySet()){
			if(entry.getValue().size()>1){
				System.out.println("hgncToDrugBank map1 is not 1:1");
				break;
			}
		}
		for(Entry<String,HashSet<String>> entry:map2.entrySet()){
			if(entry.getValue().size()>1){
				System.out.println("hgncToDrugBank map2 is not 1:1");
				break;
			}
		}
	}
	
	
	public void keggToHgnc() throws IOException{
		HashMap<String,HashSet<String>> map1=new HashMap<>();
		HashMap<String,HashSet<String>> map2=new HashMap<>();
		BufferedReader br=new BufferedReader(new FileReader(new File("D:/data/input/kgg/kegg-genes.nq")));
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
					
					if(s.startsWith("<http://bio2rdf.org/kegg:hsa")
						&o.startsWith("<http://bio2rdf.org/hgnc:")&p.equals("<http://bio2rdf.org/kegg_vocabulary:x-hgnc>")){
						if(map1.containsKey(s)){
							map1.get(s).add(o);
						}else{
							HashSet<String> set=new HashSet<>();
							set.add(o);
							map1.put(s, set);
						}
						
						if(map2.containsKey(o)){
							map2.get(o).add(s);
						}else{
							HashSet<String> set=new HashSet<>();
							set.add(s);
							map2.put(o, set);
						}
					}
				}
		}
		for(Entry<String,HashSet<String>> entry:map1.entrySet()){
			if(entry.getValue().size()>1){
				System.out.println("keggToHgnc map1 is not 1:1");
				break;
			}
		}
		for(Entry<String,HashSet<String>> entry:map2.entrySet()){
			if(entry.getValue().size()>1){
				System.out.println("keggToHgnc map2 is not 1:1");
				break;
			}
		}
	}
	
}
