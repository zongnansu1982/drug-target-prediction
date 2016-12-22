package edu.ucsd.dbmi.drugtarget.chemicstrc;

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

public class DrugCollector {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		new DrugCollector("data/input/drugbank/drugbank_dump.nt");
	}

	public HashMap<String, String> ids;
	public HashMap<String, String> getIds() {
		return ids;
	}

	public void setIds(HashMap<String, String> ids) {
		this.ids = ids;
	}

	public HashSet<String> getDrugs() {
		return drugs;
	}

	public void setDrugs(HashSet<String> drugs) {
		this.drugs = drugs;
	}

	public HashSet<String> drugs;
	public HashSet<String> sourceDrugs;

	public HashSet<String> getSourceDrugs() {
		return sourceDrugs;
	}

	public void setSourceDrugs(HashSet<String> sourceDrugs) {
		this.sourceDrugs = sourceDrugs;
	}

	public DrugCollector(String input) throws IOException {
		ids = new HashMap<>();
		drugs = new HashSet<>();
		getDrug(input);
	}
	
	public DrugCollector(String input1,String input2) throws IOException {
		ids = new HashMap<>();
		sourceDrugs=new HashSet<>();
		drugs=new HashSet<>();
		getDrug(input1,input2);
	}
	

	public void getDrug(String input) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(new File(input)));
		String line = null;
		HashMap<String, HashSet<String>> existing1 = new HashMap<>();
		HashMap<String, HashSet<String>> existing2 = new HashMap<>();

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
						& p.equals("<http://www4.wiwiss.fu-berlin.de/drugbank/resource/drugbank/keggDrugId>")) {
					String newo=o.substring(o.lastIndexOf(":")+1, o.lastIndexOf(">"));
					if (existing1.containsKey(s)) {
						existing1.get(s).add(newo);
					} else {
						HashSet<String> set = new HashSet<>();
						set.add(newo);
						existing1.put(s, set);
					}
				}
				if (s.startsWith("<http://www4.wiwiss.fu-berlin.de/drugbank/resource/drugs/")
						& p.equals("<http://www4.wiwiss.fu-berlin.de/drugbank/resource/drugbank/keggCompoundId>")) {
					String newo=o.substring(o.lastIndexOf(":")+1, o.lastIndexOf(">"));
					if (existing2.containsKey(s)) {
						existing2.get(s).add(newo);
					} else {
						HashSet<String> set = new HashSet<>();
						set.add(newo);
						existing2.put(s, set);
					}
				}

				if (s.startsWith("<http://www4.wiwiss.fu-berlin.de/drugbank/resource/drugs/")
						& p.equals("<http://www4.wiwiss.fu-berlin.de/drugbank/resource/drugbank/target>")
						& o.startsWith("<http://www4.wiwiss.fu-berlin.de/drugbank/resource/targets/")) {
					all.add(s);
				}
			}
		}

		HashSet<String> oneToOneDrug = new HashSet<>();
		HashSet<String> oneToOneCompound = new HashSet<>();
		for (Entry<String, HashSet<String>> entry : existing1.entrySet()) {
			if (entry.getValue().size() == 1) {
				oneToOneDrug.add(entry.getKey());
				for(String id:entry.getValue()){
					ids.put(entry.getKey(), id);
				}
			}
		}
		for (Entry<String, HashSet<String>> entry : existing2.entrySet()) {
			if (entry.getValue().size() == 1) {
				oneToOneCompound.add(entry.getKey());
				for(String id:entry.getValue()){
					ids.put(entry.getKey(), id);
				}
			}
		}
		System.out.println("all: " + all.size());
		System.out.println("existing1: " + existing1.size());
		System.out.println("oneToOneDrug: " + oneToOneDrug.size());
		System.out.println("existing2: " + existing2.size());
		System.out.println("oneToOneCompound: " + oneToOneCompound.size());
		
		oneToOneCompound.addAll(oneToOneDrug);
		
		System.out.println("all have : " + oneToOneCompound.size());
		for(String drug:oneToOneCompound){
			drugs.add(drug);
		}
		
	}
	
	
	public void getDrug(String input1, String input2) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(new File(input1)));
		String line = null;
		HashMap<String, HashSet<String>> existing1 = new HashMap<>();
		HashMap<String, HashSet<String>> existing2 = new HashMap<>();

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
						& p.equals("<http://www4.wiwiss.fu-berlin.de/drugbank/resource/drugbank/keggDrugId>")) {
					String newo=o.substring(o.lastIndexOf(":")+1, o.lastIndexOf(">"));
					if (existing1.containsKey(s)) {
						existing1.get(s).add(newo);
					} else {
						HashSet<String> set = new HashSet<>();
						set.add(newo);
						existing1.put(s, set);
					}
				}
				if (s.startsWith("<http://www4.wiwiss.fu-berlin.de/drugbank/resource/drugs/")
						& p.equals("<http://www4.wiwiss.fu-berlin.de/drugbank/resource/drugbank/keggCompoundId>")) {
					String newo=o.substring(o.lastIndexOf(":")+1, o.lastIndexOf(">"));
					if (existing2.containsKey(s)) {
						existing2.get(s).add(newo);
					} else {
						HashSet<String> set = new HashSet<>();
						set.add(newo);
						existing2.put(s, set);
					}
				}

				if (s.startsWith("<http://www4.wiwiss.fu-berlin.de/drugbank/resource/drugs/")
						& p.equals("<http://www4.wiwiss.fu-berlin.de/drugbank/resource/drugbank/target>")
						& o.startsWith("<http://www4.wiwiss.fu-berlin.de/drugbank/resource/targets/")) {
					all.add(s);
				}
			}
		}

		HashSet<String> oneToOneDrug = new HashSet<>();
		HashSet<String> oneToOneCompound = new HashSet<>();
		for (Entry<String, HashSet<String>> entry : existing1.entrySet()) {
			if (entry.getValue().size() == 1) {
				oneToOneDrug.add(entry.getKey());
				for(String id:entry.getValue()){
					ids.put(entry.getKey(), id);
				}
			}
		}
		for (Entry<String, HashSet<String>> entry : existing2.entrySet()) {
			if (entry.getValue().size() == 1) {
				oneToOneCompound.add(entry.getKey());
				for(String id:entry.getValue()){
					ids.put(entry.getKey(), id);
				}
			}
		}
		System.out.println("all: " + all.size());
		System.out.println("existing1: " + existing1.size());
		System.out.println("oneToOneDrug: " + oneToOneDrug.size());
		System.out.println("existing2: " + existing2.size());
		System.out.println("oneToOneCompound: " + oneToOneCompound.size());
		
		oneToOneCompound.addAll(oneToOneDrug);
		
		System.out.println("all have : " + oneToOneCompound.size());
		for(String drug:oneToOneCompound){
			if(all.contains(drug)){
				drugs.add(drug);	
			}
		}
		
		br = new BufferedReader(new FileReader(new File(input2)));
		line = null;

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
					if(drugs.contains(s)){
						this.sourceDrugs.add(s);	
					}else{
						System.err.println("erro........");
					}
					
				}
			}
		}
		
	}

}
