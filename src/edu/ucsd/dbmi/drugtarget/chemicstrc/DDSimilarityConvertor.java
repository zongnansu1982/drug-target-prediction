package edu.ucsd.dbmi.drugtarget.chemicstrc;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import org.semanticweb.yars.nx.Node;
import org.semanticweb.yars.nx.parser.NxParser;

public class DDSimilarityConvertor {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		 DDSimilarityConvertor convert=new DDSimilarityConvertor();
		convert.convert("D:/data/drug-target/models/sim/drugs_gold/enzyme_old.txt",
				"D:/data/drug-target/models/sim/drugs_gold/enzyme.txt") ;
		convert.convert("D:/data/drug-target/models/sim/drugs_gold/gpcr_old.txt",
				"D:/data/drug-target/models/sim/drugs_gold/gpcr.txt") ;
		convert.convert("D:/data/drug-target/models/sim/drugs_gold/ion_channel_old.txt",
				"D:/data/drug-target/models/sim/drugs_gold/ion_channel.txt") ;
		convert.convert("D:/data/drug-target/models/sim/drugs_gold/nuclear_receptor_old.txt",
				"D:/data/drug-target/models/sim/drugs_gold/nuclear_receptor.txt") ;
	}
	
	HashMap<String,String> keggToDrugBank;
	
	
	public DDSimilarityConvertor() throws IOException{
		keggToDrugBank=keggToDrugBank() ;
//		System.out.println(keggToUniprot);
//		System.err.println(uniprotToDrugBank);
	}
	public HashMap<String,String> keggToDrugBank() throws IOException{
		HashMap<String,String> map=new HashMap<>();
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
					if(p.equals("<http://www4.wiwiss.fu-berlin.de/drugbank/resource/drugbank/keggDrugId>")
							& s.startsWith("<http://www4.wiwiss.fu-berlin.de/drugbank/resource/drugs/")&
							o.startsWith("<http://bio2rdf.org/kegg:")){
						String newo=o.substring(o.lastIndexOf(":")+1, o.length()-1);
						map.put(newo, s);
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
					if(keggToDrugBank.containsKey(elements[j])){
						value=keggToDrugBank.get(elements[j])+" ";
					}
					sb.append(value);
				}
			}else{
				String name=elements[0];
				if(keggToDrugBank.containsKey(elements[0])){
						name=keggToDrugBank.get(elements[0]);
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
