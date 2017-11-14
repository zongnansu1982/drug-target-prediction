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

public class PPSimilarityConvertor {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		
	}
	
	HashMap<String,String> keggToUniprot ;
	HashMap<String,String> uniprotToDrugBank;
	
	HashMap<String,String> keggToHgnc ;
	HashMap<String,String> hgncToDrugBank;
	
	HashMap<String,String> keggToPdb ;
	HashMap<String,String> pdbToDrugBank;
	
	
	public PPSimilarityConvertor() throws IOException{
		
		keggToHgnc=new HashMap<>();
		hgncToDrugBank=new HashMap<>();
		keggToPdb=new HashMap<>();
		pdbToDrugBank=new HashMap<>();
		
		
		keggToUniprot=keggToUniprot() ;
		uniprotToDrugBank=uniprotToDrugBank();
		
		keggToHgnc=keggToHgnc() ;
		hgncToDrugBank=hgncToDrugBank();
		
		keggToPdb=keggToPdb() ;
		pdbToDrugBank=pdbToDrugBank();
		
		System.out.println("keggToUniprot: "+keggToUniprot.size());
		System.err.println("uniprotToDrugBank: "+uniprotToDrugBank.size());
		System.out.println("keggToHgnc: "+keggToHgnc.size());
		System.err.println("hgncToDrugBank: "+hgncToDrugBank.size());
		System.out.println("keggToPdb： "+keggToPdb.size());
		System.err.println("pdbToDrugBank: "+pdbToDrugBank.size());
		
	}
	public HashMap<String,String> uniprotToDrugBank() throws IOException{
		HashMap<String,String> map=new HashMap<>();
		BufferedReader br=new BufferedReader(new FileReader(new File("data/input/drugbank/drugbank_dump.nt")));
		String line=null;
		HashMap<String,HashSet<String>> map1=new HashMap<>();
		HashMap<String,HashSet<String>> map2=new HashMap<>();
		
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
						&o.startsWith("<http://")&p.equals("<http://www4.wiwiss.fu-berlin.de/drugbank/resource/drugbank/swissprotId>")){
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
		
		for(Entry<String,HashSet<String>> entry:map2.entrySet()){
			if(entry.getValue().size()==1){
				String value=(String) entry.getValue().toArray()[0];
				if(map1.get(value).size()==1){
					map.put(entry.getKey(), value);
				}
			}
		}
		
		return map;
	}
	
	
	public HashMap<String,String> keggToUniprot() throws IOException{
		HashMap<String,String> map=new HashMap<>();
		BufferedReader br=new BufferedReader(new FileReader(new File("D:/data/input/kgg/kegg-genes.nq")));
		String line=null;
		HashMap<String,HashSet<String>> map1=new HashMap<>();
		HashMap<String,HashSet<String>> map2=new HashMap<>();
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
						&o.startsWith("<http://bio2rdf.org/uniprot:")&p.equals("<http://bio2rdf.org/kegg_vocabulary:x-uniprot>")){
						String news=s.substring(s.lastIndexOf(":")+1, s.length()-1);
						news=news.replace("hsa_", "hsa");
						
						if(map1.containsKey(news)){
							map1.get(news).add(o);
						}else{
							HashSet<String> set=new HashSet<>();
							set.add(o);
							map1.put(news, set);
						}
						
						if(map2.containsKey(o)){
							map2.get(o).add(news);
						}else{
							HashSet<String> set=new HashSet<>();
							set.add(news);
							map2.put(o, set);
						}
					}
				}
		}
		
		for(Entry<String,HashSet<String>> entry:map1.entrySet()){
			if(entry.getValue().size()==1){
				String value=(String) entry.getValue().toArray()[0];
				if(map2.get(value).size()==1){
					map.put(entry.getKey(), value);
				}
			}
		}
		return map;
	}
	
	
	
	public HashMap<String,String> pdbToDrugBank() throws IOException{
		HashMap<String,String> map=new HashMap<>();
		BufferedReader br=new BufferedReader(new FileReader(new File("data/input/drugbank/drugbank_dump.nt")));
		String line=null;
		HashMap<String,HashSet<String>> map1=new HashMap<>();
		HashMap<String,HashSet<String>> map2=new HashMap<>();
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
		for(Entry<String,HashSet<String>> entry:map2.entrySet()){
			if(entry.getValue().size()==1){
				String value=(String) entry.getValue().toArray()[0];
				if(map1.get(value).size()==1){
					map.put(entry.getKey(), value);
				}
			}
		}
		
		return map;
	}
	
	
	public HashMap<String,String> keggToPdb() throws IOException{
		HashMap<String,String> map=new HashMap<>();
		BufferedReader br=new BufferedReader(new FileReader(new File("D:/data/input/kgg/kegg-genes.nq")));
		String line=null;
		HashMap<String,HashSet<String>> map1=new HashMap<>();
		HashMap<String,HashSet<String>> map2=new HashMap<>();
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
						String news=s.substring(s.lastIndexOf(":")+1, s.length()-1);
						news=news.replace("hsa_", "hsa");
						if(map1.containsKey(news)){
							map1.get(news).add(o);
						}else{
							HashSet<String> set=new HashSet<>();
							set.add(o);
							map1.put(news, set);
						}
						
						if(map2.containsKey(o)){
							map2.get(o).add(news);
						}else{
							HashSet<String> set=new HashSet<>();
							set.add(news);
							map2.put(o, set);
						}
					}
				}
		}
		
		for(Entry<String,HashSet<String>> entry:map1.entrySet()){
			if(entry.getValue().size()==1){
				String value=(String) entry.getValue().toArray()[0];
				if(map2.get(value).size()==1){
					map.put(entry.getKey(), value);
				}
			}
		}
		return map;
	}
	
	
	public HashMap<String,String> hgncToDrugBank() throws IOException{
		HashMap<String,String> map=new HashMap<>();
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
		for(Entry<String,HashSet<String>> entry:map2.entrySet()){
			if(entry.getValue().size()==1){
				String value=(String) entry.getValue().toArray()[0];
				if(map1.get(value).size()==1){
					map.put(entry.getKey(), value);
				}
			}
		}
		return map;
	}
	
	
	public HashMap<String,String> keggToHgnc() throws IOException{
		HashMap<String,String> map=new HashMap<>();
		BufferedReader br=new BufferedReader(new FileReader(new File("D:/data/input/kgg/kegg-genes.nq")));
		String line=null;
		HashMap<String,HashSet<String>> map1=new HashMap<>();
		HashMap<String,HashSet<String>> map2=new HashMap<>();
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
						String news=s.substring(s.lastIndexOf(":")+1, s.length()-1);
						news=news.replace("hsa_", "hsa");
						if(map1.containsKey(news)){
							map1.get(news).add(o);
						}else{
							HashSet<String> set=new HashSet<>();
							set.add(o);
							map1.put(news, set);
						}
						
						if(map2.containsKey(o)){
							map2.get(o).add(news);
						}else{
							HashSet<String> set=new HashSet<>();
							set.add(news);
							map2.put(o, set);
						}
					}
				}
		}
		for(Entry<String,HashSet<String>> entry:map1.entrySet()){
			if(entry.getValue().size()==1){
				String value=(String) entry.getValue().toArray()[0];
				if(map2.get(value).size()==1){
					map.put(entry.getKey(), value);
				}
			}
		}
		return map;
	}
	
	public void convert(String input,String output) throws IOException{
		
		BufferedReader br=new BufferedReader(new FileReader(new File(input)));
		BufferedWriter bw =new BufferedWriter(new FileWriter(new File(output)));
		String line=null;
		int i=0;
		while((line=br.readLine())!=null){
			i++;
			String[] elements=line.split("	");
			StringBuffer sb=new StringBuffer();
			if(i<2){
				sb.append("@mark ");
				for (int j = 1; j < elements.length; j++) {
					String value=elements[j]+" ";
					if(value.equals(elements[j]+" ")){
						if(keggToUniprot.containsKey(elements[j])){
							if(uniprotToDrugBank.containsKey(keggToUniprot.get(elements[j]))){
								value=uniprotToDrugBank.get(keggToUniprot.get(elements[j]))+" ";
							}
						}	
					}
					if(value.equals(elements[j]+" ")){
						if(keggToHgnc.containsKey(elements[j])){
							if(hgncToDrugBank.containsKey(keggToHgnc.get(elements[j]))){
								value=hgncToDrugBank.get(keggToHgnc.get(elements[j]))+" ";
							}
						}	
					}
					
					if(value.equals(elements[j]+" ")){
						if(keggToPdb.containsKey(elements[j])){
							if(pdbToDrugBank.containsKey(keggToPdb.get(elements[j]))){
								value=pdbToDrugBank.get(keggToPdb.get(elements[j]))+" ";
							}
						}	
					}
					
					
					sb.append(value);
				}
			}else{
				String name=elements[0];
				if(name.equals(elements[0])){
					if(keggToUniprot.containsKey(elements[0])){
						if(uniprotToDrugBank.containsKey(keggToUniprot.get(elements[0]))){
							name=uniprotToDrugBank.get(keggToUniprot.get(elements[0]));
						}
					}	
				}
				if(name.equals(elements[0])){
					if(keggToHgnc.containsKey(elements[0])){
						if(hgncToDrugBank.containsKey(keggToHgnc.get(elements[0]))){
							name=hgncToDrugBank.get(keggToHgnc.get(elements[0]));
						}
					}	
				}
				if(name.equals(elements[0])){
					if(keggToPdb.containsKey(elements[0])){
						if(pdbToDrugBank.containsKey(keggToPdb.get(elements[0]))){
							name=pdbToDrugBank.get(keggToPdb.get(elements[0]));
						}
					}	
				}
				
				sb.append(name+" ");
				for (int j = 1; j < elements.length; j++) {
					sb.append(elements[j]+" ");
				}
			}
			bw.write(sb.toString().trim()+"\n");
		}
		bw.flush();
		bw.close();
	}
	
	
}
