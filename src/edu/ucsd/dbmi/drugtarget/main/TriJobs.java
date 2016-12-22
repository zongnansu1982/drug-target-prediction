package edu.ucsd.dbmi.drugtarget.main;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;

import edu.ucsd.domi.word2vec.methods.deepwalk.DeepWalkMethod;
import edu.ucsd.domi.word2vec.methods.deepwalk.DeepWalkPredictionNew;
import edu.ucsd.domi.word2vec.methods.kernel.KernelBasedMethod;
import edu.ucsd.domi.word2vec.methods.kernel.KernelBasedPrediction;
import edu.ucsd.domi.word2vec.methods.line.LinePrediction;
import edu.ucsd.domi.word2vec.methods.similarities.SimilaritiesPrediction;

public class TriJobs {
	
	
	
	
	
	public void external_similarityComparision_Job() throws Exception{
		 
		String datafile = "D:/data/drug-target/source/tri.nt";
		String modelfile = "D:/data/drug-target/models/deepwalk/tri/external/deepwalk_external_100.txt";
		String idxfile="D:/data/drug-target/models/deepwalk/tri/external/deepwalkidx_external_100.txt";
		String removefile = "D:/data/drug-target/input/tri/external/connected/external_connected_filtered.nt";
		
		TargetPrediction predict2 = new TargetPrediction(modelfile);
		predict2.feedGold(removefile);
		HashMap<String, HashSet<String>> goldFromDrug=predict2.goldmap;
		
		DrugPrediction predict3 = new DrugPrediction(modelfile);
		predict3.feedGold(removefile);
		HashMap<String, HashSet<String>> goldFromTarget=predict3.goldmap;
		
		HashMap<String, HashMap<String, Double>> tmp1= new DeepWalkPredictionNew().external_similarity_DBSI_tri();
		HashMap<String, HashMap<String, Double>> tmp2= new DeepWalkPredictionNew().external_similarity_TBSI_tri();	
		
		EvaluationMetrics metric1 = new EvaluationMetrics(tmp1, predict2.getGoldmap());
		HashMap<String, HashMap<String, Double>> results1=metric1.getSortedMap();
		EvaluationMetrics metric2 = new EvaluationMetrics(tmp2, predict3.getGoldmap());
		HashMap<String, HashMap<String, Double>> results2=metric2.getSortedMap();
		
		HashMap<String, HashMap<String, Double>> tmp3=new SimilaritiesPrediction().external_similarity_DBSI_tri();
		HashMap<String, HashMap<String, Double>> tmp4=new SimilaritiesPrediction().external_similarity_TBSI_tri();
	
		EvaluationMetrics metric3 = new EvaluationMetrics(tmp3, predict2.getGoldmap());
		HashMap<String, HashMap<String, Double>> results3=metric3.getSortedMap();
		EvaluationMetrics metric4 = new EvaluationMetrics(tmp4, predict3.getGoldmap());
		HashMap<String, HashMap<String, Double>> results4=metric4.getSortedMap();
		
		
		BufferedWriter bw1=new BufferedWriter(new FileWriter(new File("D:/data/drug-target/results/tri/external/similarity_connected/auc_DBSI_test.data")));
//		bw1.write("Gold,DeepWalk-DBSI,Chemial-DBSI\n");
		bw1.write("Gold,DeepWalk-DBSI\n");
		for(Entry<String,HashMap<String,Double>> entry1:results1.entrySet()){
			for(Entry<String,Double> entry2:entry1.getValue().entrySet()){
					int gold=0;
					if(goldFromDrug.get(entry1.getKey()).contains(entry2.getKey())){
						gold=1;
					}
					Double value1=entry2.getValue();
					
					Double value3=0.0;
					if(results3.get(entry1.getKey()).containsKey(entry2.getKey())){
						value3=results3.get(entry1.getKey()).get(entry2.getKey());
					}
					
				bw1.write(gold+","+value1+","+value3+"\n");
			}
		}
		bw1.flush();
		bw1.close();
		
		
		BufferedWriter bw2=new BufferedWriter(new FileWriter(new File("D:/data/drug-target/results/tri/external/similarity_connected/auc_TBSI.data")));
		bw2.write("Gold,DeepWalk-TBSI,GenomicSqs-DBSI\n");
		for(Entry<String,HashMap<String,Double>> entry1:results2.entrySet()){
			for(Entry<String,Double> entry2:entry1.getValue().entrySet()){
					int gold=0;
					if(goldFromTarget.get(entry1.getKey()).contains(entry2.getKey())){
						gold=1;
					}
					Double value2=entry2.getValue();
					Double value4=0.0;
					if(results4.get(entry1.getKey()).containsKey(entry2.getKey())){
						value4=results4.get(entry1.getKey()).get(entry2.getKey());
					}
					
				bw2.write(gold+","+value2+","+value4+"\n");
			}
		}
		bw2.flush();
		bw2.close();
		
	}
	
	
	public void external_fourCategories_Job() throws Exception{
		HashSet<String> categories=new HashSet<>();
		categories.add("nuclear_receptor");
		categories.add("gpcr");
		categories.add("ion_channel");
		categories.add("enzyme");
		
		for(String category:categories){
			String datafile = "D:/data/drug-target/source/tri.nt";
			String modelfile = "D:/data/drug-target/models/deepwalk/tri/external/deepwalk_external_100.txt";
			String idxfile="D:/data/drug-target/models/deepwalk/tri/external/deepwalkidx_external_100.txt";
			String removefile = "D:/data/drug-target/input/tri/external/connected/external_connected_"+category+".nt";
			
			TargetPrediction predict2 = new TargetPrediction(modelfile);
			predict2.feedGold(removefile);
			HashMap<String, HashSet<String>> goldFromDrug=predict2.goldmap;
			
			DrugPrediction predict3 = new DrugPrediction(modelfile);
			predict3.feedGold(removefile);
			HashMap<String, HashSet<String>> goldFromTarget=predict3.goldmap;
			
			HashMap<String, HashMap<String, Double>> tmp1= new DeepWalkPredictionNew().external_similarity_DBSI_tri(category);
			HashMap<String, HashMap<String, Double>> tmp2= new DeepWalkPredictionNew().external_similarity_TBSI_tri(category);	
			
//			EvaluationMetrics metric1 = new EvaluationMetrics(tmp1, predict2.getGoldmap());
//			HashMap<String, HashMap<String, Double>> results1=metric1.getSortedMap();
//			EvaluationMetrics metric2 = new EvaluationMetrics(tmp2, predict3.getGoldmap());
//			HashMap<String, HashMap<String, Double>> results2=metric2.getSortedMap();
//			
			HashMap<String, HashMap<String, Double>> tmp3=new SimilaritiesPrediction().external_similarity_DBSI_tri(category);
			HashMap<String, HashMap<String, Double>> tmp4=new SimilaritiesPrediction().external_similarity_TBSI_tri(category);
//		
//			EvaluationMetrics metric3 = new EvaluationMetrics(tmp3, predict2.getGoldmap());
//			HashMap<String, HashMap<String, Double>> results3=metric3.getSortedMap();
//			EvaluationMetrics metric4 = new EvaluationMetrics(tmp4, predict3.getGoldmap());
//			HashMap<String, HashMap<String, Double>> results4=metric4.getSortedMap();
//			
//			
//			BufferedWriter bw1=new BufferedWriter(new FileWriter(new File("D:/data/drug-target/results/tri/external/similarity_connected/auc_DBSI_"+category+".data")));
//			bw1.write("Gold,DeepWalk-DBSI,Chemial-DBSI\n");
//			for(Entry<String,HashMap<String,Double>> entry1:results1.entrySet()){
//				for(Entry<String,Double> entry2:entry1.getValue().entrySet()){
//						int gold=0;
//						if(goldFromDrug.get(entry1.getKey()).contains(entry2.getKey())){
//							gold=1;
//						}
//						Double value1=entry2.getValue();
//						
//						Double value3=0.0;
//						if(results3.get(entry1.getKey()).containsKey(entry2.getKey())){
//							value3=results3.get(entry1.getKey()).get(entry2.getKey());
//						}
//						
//					bw1.write(gold+","+value1+","+value3+"\n");
//				}
//			}
//			bw1.flush();
//			bw1.close();
//			
//			
//			BufferedWriter bw2=new BufferedWriter(new FileWriter(new File("D:/data/drug-target/results/tri/external/similarity_connected/auc_TBSI_"+category+".data")));
//			bw2.write("Gold,DeepWalk-TBSI,GenomicSqs-DBSI\n");
//			for(Entry<String,HashMap<String,Double>> entry1:results2.entrySet()){
//				for(Entry<String,Double> entry2:entry1.getValue().entrySet()){
//						int gold=0;
//						if(goldFromTarget.get(entry1.getKey()).contains(entry2.getKey())){
//							gold=1;
//						}
//						Double value2=entry2.getValue();
//						Double value4=0.0;
//						if(results4.get(entry1.getKey()).containsKey(entry2.getKey())){
//							value4=results4.get(entry1.getKey()).get(entry2.getKey());
//						}
//						
//					bw2.write(gold+","+value2+","+value4+"\n");
//				}
//			}
//			bw2.flush();
//			bw2.close();
		}
		
	}
	
	
	
	
	public void triExternalJob() throws Exception{
		new LinePrediction().external_tri();
			   	
	   	String datafile="data/input/drugCloud/data_tri.nt";
	   	String modelfile="data/input/drugCloud/deepwalk_tri_100.txt";
	   	String idxfile="data/input/drugCloud/deepwalkidx_tri_100.txt";
	   	    	
	   	new DeepWalkMethod().training(datafile,  modelfile,  idxfile,  100, 5);
	   	 
		new DeepWalkPredictionNew().external_tri();
	}
	
	public void areaJob() throws Exception{
		new LinePrediction().external_bi_area();
		new LinePrediction().internal_bi_area();
		new LinePrediction().external_tri_area();
		new LinePrediction().internal_tri_area();
	   	 
		new DeepWalkPredictionNew().external_bi_area();
		new DeepWalkPredictionNew().internal_bi_area();
		new DeepWalkPredictionNew().external_tri_area();
		new DeepWalkPredictionNew().internal_tri_area();
	}
	

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub

		new TriJobs().tenfolder_fourCategories_svm_Job();
		
	}
}
