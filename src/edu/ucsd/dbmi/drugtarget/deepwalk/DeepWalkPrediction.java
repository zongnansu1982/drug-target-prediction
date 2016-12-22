package edu.ucsd.dbmi.drugtarget.deepwalk;

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
import java.util.List;
import java.util.Map;

import org.semanticweb.yars.nx.Node;
import org.semanticweb.yars.nx.parser.NxParser;

import edu.ucsd.domi.word2vec.methods.experiment.DrugPrediction;
import edu.ucsd.domi.word2vec.methods.experiment.TargetPrediction;

public class DeepWalkPrediction {
	
	
	public void internal() throws Exception{
		
		BufferedWriter bw =new BufferedWriter(new FileWriter(new File("data/output/experiment0630/Deepprediction_internal.txt")));
    	for (int i = 1; i < 11; i++) {
//    	int i=1;
    	
		String datafile="data/input/drugCloud/data_bi_"+i*100+".nt";
    	String modelfile="data/input/drugCloud/deepwalk_bi_"+i+100+"_100.txt";
    	String idxfile="data/input/drugCloud/deepwalkidx_bi_"+i+100+"_100.txt";
    	String removefile="data/input/drugCloud/removed_bi_"+i*100+".nt";
    	int topN=10;
		
//		String datafile="data/input/drugCloud/data_small_01.nt";
//    	String modelfile="data/input/drugCloud/deepwalk_small_01_100.txt";
//    	String idxfile="data/input/drugCloud/deepwalkidx_small_01_100.txt";
//    	String removefile="data/input/drugCloud/removed_small_01.nt";
//    	int topN=1;
    	
    	
    	System.err.println("========== GET TARGET FROM SIMILAR TARGET ================");
    	
    	// target-target similar finding test, 0630,2016
    	TargetPrediction predict1=new TargetPrediction(modelfile);
		predict1.feedGold(removefile);
		
		
		HashMap<String,HashSet<String>> queries1=predict1.getTargetAsQueries(datafile);
		
		HashMap<String,List<Map.Entry<String, Double>>> results1=predict1.predictBySimilarTarget(idxfile,queries1, topN);	
		Double recall1=predict1.recall(results1);
		bw.write(i+",predictTargetBySimilarTarget,"+","+recall1+"\n");
		System.out.println(i+",predictTargetBySimilarTarget,"+recall1);
		bw.flush();

		
		// drug-drug similar find test
		
		System.err.println("========== GET TARGET FROM SIMILAR ASSOCIATED DRUG ================");
		
		TargetPrediction predict2=new TargetPrediction(modelfile);
		predict2.feedGold(removefile);
		
		HashSet<String> queries2=predict2.getDrugAsQueries();
		
		HashMap<String,List<Map.Entry<String, Double>>> results2=predict2.predictBySimilarDrug(idxfile,datafile,queries2, topN);	
		
		Double recall2=predict2.recall(results2);
		bw.write(i+",predictTargetBySimilarDrug,"+recall2+"\n");
		System.out.println(i+",predictTargetBySimilarDrug,"+recall2);
		bw.flush();
		
		
		
		
		System.err.println("========== GET DRUG FROM SIMILAR ASSOCIATED TARGET ================");
    	DrugPrediction predict3=new DrugPrediction(modelfile);
		predict3.feedGold(removefile);
		
		
		HashSet<String> queries3=predict3.getTargetAsQueries();
		
		HashMap<String,List<Map.Entry<String, Double>>> results3=predict3.predictBySimilarTarget(idxfile,datafile,queries3, topN);	
		Double recall3=predict3.recall(results3);
		bw.write(i+",predictDrugBySimilarTarget,"+recall3+"\n");
		System.out.println(i+",predictDrugBySimilarTarget,"+recall3);
		bw.flush();
		
		// drug-drug similar find test
		System.err.println("========== GET DRUG FROM SIMILAR DRUG ================");
		
		DrugPrediction predict4=new DrugPrediction(modelfile);
		predict4.feedGold(removefile);
		
		HashMap<String,HashSet<String>> queries4=predict4.getDrugAsQueries(datafile);
		
		HashMap<String,List<Map.Entry<String, Double>>> results4=predict4.predictBySimilarDrug(idxfile,queries4, topN);	
		
		Double recall4=predict4.recall(results4);
		bw.write(i+",predictDrugBySimilarTarget,"+recall4+"\n");
		System.out.println(i+",predictDrugBySimilarTarget,"+recall4);
		bw.flush();
		
    	}
    	bw.flush();
    	bw.close();
	}
	
	public void external() throws Exception{
		BufferedWriter bw =new BufferedWriter(new FileWriter(new File("data/output/experiment0630/Deepprediction_external.txt")));
		
		String datafile="data/input/drugCloud/data_bi.nt";
    	String modelfile="outsource/windows/experiment0630/models/deepwalk_bi_full_100.txt";
    	String externalfile="data/input/drugCloud/drugbank_predict_4.nt";
    	String idxfile="data/input/drugCloud/deepwalkidx_bi_full_100.txt";
    	
    	int topN=10;
		
    	TargetPrediction predict1=new TargetPrediction(modelfile);
		predict1.feedGold(externalfile);
		
		HashMap<String,HashSet<String>> queries1=predict1.getTargetAsQueries(datafile);
		
		HashMap<String,List<Map.Entry<String, Double>>> results1=predict1.predictBySimilarTarget(idxfile,queries1, topN);	
		Double recall1=predict1.recall(results1);
		
		bw.write("full,predictTargetBySimilarTarget,"+","+recall1+"\n");
		System.out.println("full,predictTargetBySimilarTarget,"+recall1);
		// drug-drug similar find test
		
		
		TargetPrediction predict2=new TargetPrediction(modelfile);
		predict2.feedGold(externalfile);
		
		HashSet<String> queries2=predict2.getDrugAsQueries();
		
		HashMap<String,List<Map.Entry<String, Double>>> results2=predict2.predictBySimilarDrug(idxfile,datafile,queries2, topN);	
		
		Double recall2=predict2.recall(results2);
		bw.write("full,predictTargetBySimilarDrug,"+recall2+"\n");
		System.out.println("full,predictTargetBySimilarDrug,"+recall2);
		
		
		
		DrugPrediction predict3=new DrugPrediction(modelfile);
		predict3.feedGold(externalfile);
		
		
		HashSet<String> queries3=predict3.getTargetAsQueries();
		
		HashMap<String,List<Map.Entry<String, Double>>> results3=predict3.predictBySimilarTarget(idxfile,datafile,queries3, topN);	
		Double recall3=predict3.recall(results3);
		bw.write("full,predictDrugBySimilarTarget,"+recall3+"\n");
		System.out.println("full,predictDrugBySimilarTarget,"+recall3);
		
		// drug-drug similar find test
		
		DrugPrediction predict4=new DrugPrediction(modelfile);
		predict4.feedGold(externalfile);
		
		HashMap<String,HashSet<String>> queries4=predict4.getDrugAsQueries(datafile);
		
		HashMap<String,List<Map.Entry<String, Double>>> results4=predict4.predictBySimilarDrug(idxfile,queries4, topN);	
		
		Double recall4=predict4.recall(results4);
		bw.write("full,predictDrugBySimilarDrug,"+recall4+"\n");
		System.out.println("full,predictDrugBySimilarDrug,"+recall4);
		
	bw.flush();
	bw.close();
	}
//	public void internalAll() throws Exception{
//			BufferedWriter bw =new BufferedWriter(new FileWriter(new File("data/output/experiment0630/prediction_internalAll.txt")));
//	    	HashSet<String> allTarget=getAllTarget("data/input/drugbank/drugbank_dump.nt");
//	    	HashSet<String> allDrug=getAllDrug("data/input/drugbank/drugbank_dump.nt");
//	    	
//	    	for (int i = 1; i < 11; i++) {
//	    		String datafile="data/input/drugCloud/data_bi_"+i*100+".nt";
//	        	String modelfile="data/input/drugCloud/deepwalk_bi_"+i+100+"_100.txt";
//	        	String idxfile="data/input/drugCloud/deepwalkidx_bi_"+i+100+"_100.txt";
//	        	String removefile="data/input/drugCloud/removed_bi_"+i*100+".nt";
//	        	int topN=10;
//	    		
//	        	TargetPrediction predict1=new TargetPrediction(modelfile);
//	    		predict1.feedGold(removefile);
//	    		
//	    		HashMap<String,HashSet<String>> queries1=predict1.getTargetAsQueries(datafile);
//	    		
//	    		List<Map.Entry<String, Double>> results1=predict1.predictBySimilarTarget(idxfile,queries1,allTarget);	
//	    		
//	    		Double area1=AUCRoc.getArea(results1, predict1.getGoldmap());
//	    		
//	    		bw.write(i+",predictTargetBySimilarTarget,"+area1+"\n");
//	    		System.out.println(i+",predictTargetBySimilarTarget,"+area1);
//	    		// drug-drug similar find test
//	    		bw.flush();
//	    		
//	    		TargetPrediction predict2=new TargetPrediction(modelfile);
//	    		predict2.feedGold(removefile);
//	    		
//	    		HashSet<String> queries2=predict2.getDrugAsQueries();
//	    		
//	    		List<Map.Entry<String, Double>> results2=predict2.predictBySimilarDrug(idxfile,datafile,queries2, allDrug, allTarget);	
//	    		
//	    		Double area2=AUCRoc.getArea(results2, predict2.getGoldmap());
//	    		
//	    		bw.write(i+",predictTargetBySimilarDrug,"+area2+"\n");
//	    		System.out.println(i+",predictTargetBySimilarDrug,"+area2);
//	    		bw.flush();
//	    		
//	    		DrugPrediction predict3=new DrugPrediction(modelfile);
//	    		predict3.feedGold(removefile);
//	    		
//	    		HashSet<String> queries3=predict3.getTargetAsQueries();
//	    		
//	    		List<Map.Entry<String, Double>> results3=predict3.predictBySimilarTarget(idxfile,datafile,queries3, allDrug, allTarget);	
//	    		
//	    		Double area3=AUCRoc.getArea(results3, predict3.getGoldmap());
//	    		
//	    		bw.write(i+",predictDrugBySimilarTarget,"+area3+"\n");
//	    		System.out.println(i+",predictDrugBySimilarTarget,"+area3);
//	    		bw.flush();
//	    		
//	    		// drug-drug similar find test
//	    		
//	    		DrugPrediction predict4=new DrugPrediction(modelfile);
//	    		predict4.feedGold(removefile);
//	    		
//	    		HashMap<String,HashSet<String>> queries4=predict4.getDrugAsQueries(datafile);
//	    		
//	    		List<Map.Entry<String, Double>> results4=predict4.predictBySimilarDrug(idxfile,queries4, allTarget);	
//	    		
//	    		Double area4=AUCRoc.getArea(results4, predict4.getGoldmap());
//	    		
//	    		bw.write(i+",predictDrugBySimilarDrug,"+area4+"\n");
//	    		System.out.println(i+",predictDrugBySimilarDrug,"+area4);
//	    		bw.flush();
//			}
//	    	
//	    	bw.flush();
//	    	bw.close();
//	}
//	
//	
//	public void externalAll() throws Exception{
//		BufferedWriter bw =new BufferedWriter(new FileWriter(new File("data/output/experiment0630/prediction_externalAll.txt")));
//		HashSet<String> allTarget=getAllTarget("data/input/drugbank/drugbank_dump.nt");
//    	HashSet<String> allDrug=getAllDrug("data/input/drugbank/drugbank_dump.nt");
//    	String datafile="data/input/drugCloud/data_bi.nt";
//    	String modelfile="outsource/windows/experiment0630/models/deepwalk_bi_full_100.txt";
//    	String externalfile="data/input/drugCloud/drugbank_predict_4.nt";
//    	String idxfile="data/input/drugCloud/deepwalkidx_bi_full_100.txt";
//    	
//		
//    	TargetPrediction predict1=new TargetPrediction(modelfile);
//		predict1.feedGold(externalfile);
//		
//		HashMap<String,HashSet<String>> queries1=predict1.getTargetAsQueries(datafile);
//		
//		List<Map.Entry<String, Double>>  results1=predict1.predictBySimilarTarget(idxfile, queries1,allTarget);	
//		Double area1=AUCRoc.getArea(results1, predict1.getGoldmap());
//		
//		bw.write("full,predictTargetBySimilarTarget,"+area1+"\n");
//		System.out.println("full,predictTargetBySimilarTarget,"+area1);
//		
//		// drug-drug similar find test
//		
//		
//		TargetPrediction predict2=new TargetPrediction(modelfile);
//		predict2.feedGold(externalfile);
//		
//		HashSet<String> queries2=predict2.getDrugAsQueries();
//		
//		List<Map.Entry<String, Double>> results2=predict2.predictBySimilarDrug(idxfile,datafile,queries2, allDrug, allTarget);	
//		
//		Double area2=AUCRoc.getArea(results2, predict2.getGoldmap());
//		
//		bw.write("full,predictTargetBySimilarDrug,"+area2+"\n");
//		System.out.println("full,predictTargetBySimilarDrug,"+area2);
//		
//		
//		DrugPrediction predict3=new DrugPrediction(modelfile);
//		predict3.feedGold(externalfile);
//		
//		
//		HashSet<String> queries3=predict3.getTargetAsQueries();
//		
//		List<Map.Entry<String, Double>> results3=predict3.predictBySimilarTarget(idxfile,datafile,queries3, allDrug, allTarget);	
//		Double area3=AUCRoc.getArea(results3, predict3.getGoldmap());
//		
//		bw.write("full,predictDrugBySimilarTarget,"+area3+"\n");
//		System.out.println("full,predictDrugBySimilarTarget,"+area3);
//		
//		// drug-drug similar find test
//		
//		DrugPrediction predict4=new DrugPrediction(modelfile);
//		predict4.feedGold(externalfile);
//		
//		HashMap<String,HashSet<String>> queries4=predict4.getDrugAsQueries(datafile);
//		
//		List<Map.Entry<String, Double>> results4=predict4.predictBySimilarDrug(idxfile,queries4, allTarget);	
//		
//		Double area4=AUCRoc.getArea(results4, predict4.getGoldmap());
//		
//		bw.write("full,predictDrugBySimilarDrug,"+area4+"\n");
//		System.out.println("full,predictDrugBySimilarDrug,"+area4);
//		
//	bw.flush();
//	bw.close();
//}
	
	public HashSet<String> getAllTarget(String datafile) throws IOException{
		BufferedReader br = new BufferedReader(new FileReader(new File(datafile)));
		String line = null;
		HashSet<String> set=new HashSet<>();
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
						&p.equals("<http://www4.wiwiss.fu-berlin.de/drugbank/resource/drugbank/target>")
						& o.startsWith("<http://www4.wiwiss.fu-berlin.de/drugbank/resource/targets/")) {
					set.add(o);
				}
			}
		}
		return set;
	}
	
	public HashSet<String> getAllDrug(String datafile) throws IOException{
		BufferedReader br = new BufferedReader(new FileReader(new File(datafile)));
		String line = null;
		HashSet<String> set=new HashSet<>();
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
						&p.equals("<http://www4.wiwiss.fu-berlin.de/drugbank/resource/drugbank/target>")
						& o.startsWith("<http://www4.wiwiss.fu-berlin.de/drugbank/resource/targets/")) {
					set.add(s);
				}
			}
		}
		return set;
	}

}
