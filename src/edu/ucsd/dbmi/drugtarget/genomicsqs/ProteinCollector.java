package edu.ucsd.dbmi.drugtarget.genomicsqs;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;

import org.semanticweb.yars.nx.Node;
import org.semanticweb.yars.nx.parser.NxParser;

public class ProteinCollector {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub

	}
	
	public HashMap<String, String>  getProtein(String input) throws IOException{
		BufferedReader br = new BufferedReader(new FileReader(new File(input)));
		String line=null;
		HashMap<String, String> existing=new HashMap<>();
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
						&p.equals("<http://www4.wiwiss.fu-berlin.de/drugbank/resource/drugbank/proteinSequence>")){
						String newo=o.substring(o.indexOf("\"")+1, o.lastIndexOf("\"")).trim();
						existing.put(s, newo);
					}
				}
		}
		
		System.out.println("existing: "+existing.size());
		
		return existing;
	}
	
	
	public HashSet<String> getSourceProtein(String input) throws IOException{
		BufferedReader br = new BufferedReader(new FileReader(new File(input)));
		String line = null;

		HashSet<String> all = new HashSet<>();
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
					all.add(o);
				}
			}
		}
		return all;
	}
	
	
	public HashSet<String> getSourceProteinWithInvalidSeq() throws IOException{
		BufferedReader br = new BufferedReader(new FileReader(new File("data/input/drugbank/drugbank_dump.nt")));
		String line = null;

		HashSet<String> all = new HashSet<>();
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
					all.add(o);
				}
			}
		}
		HashSet<String> drugs = new HashSet<>();
		HashMap<String, String> idx = new ProteinCollector().getProtein("data/input/drugbank/drugbank_dump.nt");
		
		for(Entry<String,String> entry:idx.entrySet()){
			if(all.contains(entry.getKey())){
				drugs.add(entry.getKey());		
			}
		
		}
		
		HashSet<String> invad=new HashSet<>();
		for(String protein:drugs){
			String tmp1 =new  String(idx.get(protein).getBytes(),"UTF-8");
			StringBuffer sb1 =new StringBuffer();
			for(String e:tmp1.split("\\\\n")){
				sb1.append(e+" \n");
			}
			
			String protein1=sb1.toString();
			String str1 = (new FastaSequence(protein1)).getSequence();
			if(!detectValidSeq(str1)){
				invad.add(protein);
			}
		}
		System.out.println(invad);
		System.out.println("all: "+all.size());
		System.out.println("invad: "+invad.size());
		all.remove(invad);
		System.out.println("remain: "+all.size());
		
		return all;
		
	}
	
	public boolean detectValidSeq(String seq){
		boolean isLeg=true;
		for(char c:seq.toCharArray()){
			boolean dec=isLeg(c);
			if(!dec){
				isLeg=false;
				break;
			}
		}
		
		return isLeg;
		
	}
	
	
	private static boolean isLeg(char a) {
    	// check for upper and lowercase characters
		
    	switch ((String.valueOf(a)).toUpperCase().charAt(0)) {
	    	case 'A': return true;
	    	case 'R': return true;
	    	case 'N': return true;
	    	case 'D': return true;
	    	case 'C': return true;
	    	case 'Q': return true;
	    	case 'E': return true;
	    	case 'G': return true;
	    	case 'H': return true;
	    	case 'I': return true;
	    	case 'L': return true;
	    	case 'K': return true;
	    	case 'M': return true;
	    	case 'F': return true;
	    	case 'P': return true;
	    	case 'S': return true;
	    	case 'T': return true;
	    	case 'W': return true;
	    	case 'Y': return true;
	    	case 'V': return true;
	    	default: return false ;
    	}
    }

}
