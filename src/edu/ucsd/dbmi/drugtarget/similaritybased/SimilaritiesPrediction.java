package edu.ucsd.dbmi.drugtarget.similaritybased;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;
import org.semanticweb.yars.nx.Node;
import org.semanticweb.yars.nx.parser.NxParser;

import edu.ucsd.domi.word2vec.dataProcess.dataGenerate.ExternalSourceGenerator;
import edu.ucsd.domi.word2vec.methods.chemicalsimilarities.DrugCollector;
import edu.ucsd.domi.word2vec.methods.experiment.DrugPrediction;
import edu.ucsd.domi.word2vec.methods.experiment.EvaluationMetrics;
import edu.ucsd.domi.word2vec.methods.experiment.TargetPrediction;
import edu.ucsd.domi.word2vec.methods.proteinsimilarities.ProteinCollector;

public class SimilaritiesPrediction {
	
//	System.out.println("jaccard: "+new Similarities().jaccardSimilarity(set1, set2));
//	System.out.println("simpson: "+new Similarities().simpsonSimilarity(set1, set2));
//	System.out.println("geometric: "+new Similarities().geometricSimilarity(set1, set2));
//	System.out.println("cosin: "+new Similarities().cosinSimilarity(set1, set2));
//	System.out.println("Hypergeometric: "+new Similarities().hypergeometricIndex(set1,set2, nodes));
//	System.out.println("pcc: "+new Similarities().pearsonCorrelationCoefficient(set1, set2, nodes));
	
	public static void main(String[] args) throws IOException{
		new SimilaritiesPrediction().getDrugSimilarityGraph(new File("D:/data/drug-target/models/sim/drugs_gold/enzyme.txt"));
		
	}
	public static String csi="CSI";
	public static String jaccard="Jaccard";
	public static String geometric="Geometric";
	public static String simpson="Simpson";
	public static String cosin="Cosin";
	public static String hypergeometric="Hypergeometric";
	public static String pcc="PCC";
	
	
	
	public void monteCarlo_bi(String similarityType) throws Exception {

		HashSet<String> allTarget = getAllTarget("data/input/drugbank/drugbank_dump.nt");
		HashSet<String> allDrug = getAllDrug("data/input/drugbank/drugbank_dump.nt");
		BufferedWriter bw = new BufferedWriter(new FileWriter(new File("D:/data/drug-target/results/bi/monteCarlo/"+similarityType+"_bi_monteCarloAreas.csv")));
		
		
		for (int i = 1; i < 11; i++) {
			
			String datafile = "D:/data/drug-target/input/bi/monteCarlo/bi_" + i * 100 + "_data.nt";
	        String removefile = "D:/data/drug-target/input/bi/monteCarlo/bi_" + i * 100 + "_test.nt";
		
			HashMap<String, HashSet<String>> drugTargetassociations = getDrugTargetAssociations(datafile);
			HashMap<String, HashSet<String>> targetDrugassociations = getTargetDrugAssociations(datafile);
			
			SimpleWeightedGraph<String, DefaultWeightedEdge> drugPPCGraph=null;
			SimpleWeightedGraph<String, DefaultWeightedEdge> targetPPCGraph=null;
			
			
			if(similarityType.equals(SimilaritiesPrediction.csi)){
				PCCNetworkGenerate generate1=new PCCNetworkGenerate(drugTargetassociations, allTarget);
				generate1.generate();
				drugPPCGraph= generate1.getGraph();	
				
				PCCNetworkGenerate generate2=new PCCNetworkGenerate(targetDrugassociations, allDrug);
				generate2.generate();
				targetPPCGraph= generate2.getGraph();	
				
			}
			

//			TargetPrediction predict1 = new TargetPrediction();
//			predict1.feedGold(removefile);
//
//			HashMap<String, HashSet<String>> queries1 = predict1.getTargetAsQueries(datafile);
//
//			
//			HashMap<String, HashMap<String, Double>> results1=predict1.predictBySimilarTarget(
//					similarityType,queries1, targetDrugassociations, targetPPCGraph,
//					allDrug,allTarget); 
//
//			EvaluationMetrics metric1 = new EvaluationMetrics(results1, predict1.getGoldmap());
//			metric1.getAreasByTopN(
//					"data/output/experiment_similarities/"+similarityType+"_predictTargetByTarget_getAreasByTopN_" + i * 100 + ".txt");
//			metric1.getAreasByPercentage(
//					"data/output/experiment_similarities/"+similarityType+"_predictTargetByTarget_getAreasByPercentage_" + i * 100
//							+ ".txt");
//			metric1.getRecallByPercentage(
//					"data/output/experiment_similarities/"+similarityType+"_predictTargetByTarget_getRecallByPercentage_" + i * 100
//							+ ".txt");
//			metric1.getRecallByTopN(
//					"data/output/experiment_similarities/"+similarityType+"_predictTargetByTarget_getRecallByTopN_" + i * 100 + ".txt");
//			metric1.getArea(bw,"data/output/experiment_similarities/"+similarityType+"_predictTargetByTarget_getArea_" + i * 100 + ".txt");

			// drug-drug similar find test

			
			TargetPrediction predict2 = new TargetPrediction();
			predict2.feedGold(removefile);

			HashSet<String> queries2 = predict2.getDrugAsQueries();

			HashMap<String, HashMap<String, Double>> results2=predict2.predictBySimilarDrug(
					similarityType, queries2, drugTargetassociations,
					drugPPCGraph,allDrug,allTarget);

			EvaluationMetrics metric2 = new EvaluationMetrics(results2, predict2.getGoldmap());
			metric2.getAreasByTopN(
					"D:/data/drug-target/results/bi/monteCarlo/"+similarityType+"_DBSI_areasByTopN_" + i * 100 + ".txt");
			metric2.getAreasByPercentage(
					"D:/data/drug-target/results/bi/monteCarlo/"+similarityType+"_DBSI_areasByPercentage_" + i * 100 + ".txt");
			metric2.getRecallByPercentage(
					"D:/data/drug-target/results/bi/monteCarlo/"+similarityType+"_DBSI_recallByPercentage_" + i * 100
							+ ".txt");
			metric2.getRecallByTopN(
					"D:/data/drug-target/results/bi/monteCarlo/"+similarityType+"_DBSI_recallByTopN_" + i * 100 + ".txt");
			
			metric2.getPrecisionByPercentage("D:/data/drug-target/results/bi/monteCarlo/"+similarityType+"_DBSI_precisionByPercentage_" + i * 100
					+ ".txt");
			metric2.getPrecisionByTopN("D:/data/drug-target/results/bi/monteCarlo/"+similarityType+"_DBSI_precisionByTopN_" + i * 100
					+ ".txt");
			
			metric2.getArea(bw,"D:/data/drug-target/results/bi/monteCarlo/"+similarityType+"_DBSI_area_" + i * 100 + ".txt");

			DrugPrediction predict3 = new DrugPrediction();
			predict3.feedGold(removefile);
			
			HashSet<String> queries3 = predict3.getTargetAsQueries();

			HashMap<String, HashMap<String, Double>> results3 = predict3.predictBySimilarTarget( similarityType, queries3,  targetDrugassociations,
					targetPPCGraph,allDrug, allTarget) ;

			EvaluationMetrics metric3 = new EvaluationMetrics(results3, predict3.getGoldmap());
			metric3.getAreasByTopN(
					"D:/data/drug-target/results/bi/monteCarlo/"+similarityType+"_TBSI_areasByTopN_" + i * 100 + ".txt");
			metric3.getAreasByPercentage(
					"D:/data/drug-target/results/bi/monteCarlo/"+similarityType+"_TBSI_areasByPercentage_" + i * 100 + ".txt");
			metric3.getRecallByPercentage(
					"D:/data/drug-target/results/bi/monteCarlo/"+similarityType+"_TBSI_recallByPercentage_" + i * 100
							+ ".txt");
			metric3.getRecallByTopN(
					"D:/data/drug-target/results/bi/monteCarlo/"+similarityType+"_TBSI_recallByTopN_" + i * 100 + ".txt");
			
			metric3.getPrecisionByPercentage("D:/data/drug-target/results/bi/monteCarlo/"+similarityType+"_TBSI_precisionByPercentage_" + i * 100
					+ ".txt");
			metric3.getPrecisionByTopN("D:/data/drug-target/results/bi/monteCarlo/"+similarityType+"_TBSI_precisionByTopN_" + i * 100
					+ ".txt");
			
			metric3.getArea(bw,"D:/data/drug-target/results/bi/monteCarlo/"+similarityType+"_TBSI_area_" + i * 100 + ".txt");
			// drug-drug similar find test

//			DrugPrediction predict4 = new DrugPrediction();
//			predict4.feedGold(removefile);
//
//			HashMap<String, HashSet<String>> queries4 = predict4.getDrugAsQueries(datafile);
//
//			HashMap<String, HashMap<String, Double>> results4 = predict4.predictBySimilarDrug(similarityType,queries4, drugTargetassociations, drugPPCGraph,
//						allDrug,allTarget);
//
//			EvaluationMetrics metric4 = new EvaluationMetrics(results4, predict4.getGoldmap());
//			metric4.getAreasByTopN(
//					"data/output/experiment_similarities/"+similarityType+"_predictDrugByDrug_getAreasByTopN_" + i * 100 + ".txt");
//			metric4.getAreasByPercentage(
//					"data/output/experiment_similarities/"+similarityType+"_predictDrugByDrug_getAreasByPercentage_" + i * 100 + ".txt");
//			metric4.getRecallByPercentage(
//					"data/output/experiment_similarities/"+similarityType+"_predictDrugByDrug_getRecallByPercentage_" + i * 100 + ".txt");
//			metric4.getRecallByTopN(
//					"data/output/experiment_similarities/"+similarityType+"_predictDrugByDrug_getRecallByTopN_" + i * 100 + ".txt");
//			metric4.getArea(bw,"data/output/experiment_similarities/"+similarityType+"_predictDrugByDrug_getArea_" + i * 100 + ".txt");
			bw.flush();
		}
		bw.flush();
		bw.close();
	}
	
	
	public void tenfolder_bi(String similarityType) throws Exception {

		HashSet<String> allTarget = getAllTarget("data/input/drugbank/drugbank_dump.nt");
		HashSet<String> allDrug = getAllDrug("data/input/drugbank/drugbank_dump.nt");
		BufferedWriter bw = new BufferedWriter(new FileWriter(new File("D:/data/drug-target/results/bi/all/10folder/"+similarityType+"_bi_10foldArea.csv")));
		
		for (int i = 0; i < 10; i++) {
			
			String datafile = "D:/data/drug-target/input/bi/all/10folder/bi_folder-" + i + "_data.nt";
			String removefile = "D:/data/drug-target/input/bi/all/10folder/bi_folder-" + i + "_test.nt";
			
			HashMap<String, HashSet<String>> drugTargetassociations = getDrugTargetAssociations(datafile);
			HashMap<String, HashSet<String>> targetDrugassociations = getTargetDrugAssociations(datafile);
			
			SimpleWeightedGraph<String, DefaultWeightedEdge> drugPPCGraph=null;
			SimpleWeightedGraph<String, DefaultWeightedEdge> targetPPCGraph=null;
			
			
			if(similarityType.equals(SimilaritiesPrediction.csi)){
				PCCNetworkGenerate generate1=new PCCNetworkGenerate(drugTargetassociations, allTarget);
				generate1.generate();
				drugPPCGraph= generate1.getGraph();	
				
				PCCNetworkGenerate generate2=new PCCNetworkGenerate(targetDrugassociations, allDrug);
				generate2.generate();
				targetPPCGraph= generate2.getGraph();	
				
			}
			

			// drug-drug similar find test

			
			TargetPrediction predict2 = new TargetPrediction();
			predict2.feedGold(removefile);

			HashSet<String> queries2 = predict2.getDrugAsQueries();

			HashMap<String, HashMap<String, Double>> results2=predict2.predictBySimilarDrug(
					similarityType, queries2, drugTargetassociations,
					drugPPCGraph,allDrug,allTarget);

			EvaluationMetrics metric2 = new EvaluationMetrics(results2, predict2.getGoldmap());
			metric2.getAreasByTopN(
					"D:/data/drug-target/results/bi/all/10folder/"+similarityType+"_DBSI_areasByTopN_" + i  + ".txt");
			metric2.getAreasByPercentage(
					"D:/data/drug-target/results/bi/all/10folder/"+similarityType+"_DBSI_areasByPercentage_" + i  + ".txt");
			metric2.getRecallByPercentage(
					"D:/data/drug-target/results/bi/all/10folder/"+similarityType+"_DBSI_recallByPercentage_" + i 
							+ ".txt");
			metric2.getRecallByTopN(
					"D:/data/drug-target/results/bi/all/10folder/"+similarityType+"_DBSI_recallByTopN_" + i  + ".txt");
			
			metric2.getPrecisionByPercentage("D:/data/drug-target/results/bi/all/10folder/"+similarityType+"_DBSI_precisionByPercentage_" + i 
					+ ".txt");
			metric2.getPrecisionByTopN("D:/data/drug-target/results/bi/all/10folder/"+similarityType+"_DBSI_precisionByTopN_" + i 
					+ ".txt");
			
			metric2.getArea(bw,"D:/data/drug-target/results/bi/all/10folder/"+similarityType+"_DBSI_area_" + i  + ".txt");

			DrugPrediction predict3 = new DrugPrediction();
			predict3.feedGold(removefile);
			
			HashSet<String> queries3 = predict3.getTargetAsQueries();

			HashMap<String, HashMap<String, Double>> results3 = predict3.predictBySimilarTarget( similarityType, queries3,  targetDrugassociations,
					targetPPCGraph,allDrug, allTarget) ;

			EvaluationMetrics metric3 = new EvaluationMetrics(results3, predict3.getGoldmap());
			metric3.getAreasByTopN(
					"D:/data/drug-target/results/bi/all/10folder/"+similarityType+"_TBSI_areasByTopN_" + i + ".txt");
			metric3.getAreasByPercentage(
					"D:/data/drug-target/results/bi/all/10folder/"+similarityType+"_TBSI_areasByPercentage_" + i  + ".txt");
			metric3.getRecallByPercentage(
					"D:/data/drug-target/results/bi/all/10folder/"+similarityType+"_TBSI_recallByPercentage_" + i 
							+ ".txt");
			metric3.getRecallByTopN(
					"D:/data/drug-target/results/bi/all/10folder/"+similarityType+"_TBSI_recallByTopN_" + i + ".txt");
			
			metric3.getPrecisionByPercentage("D:/data/drug-target/results/bi/all/10folder/"+similarityType+"_TBSI_precisionByPercentage_" + i 
					+ ".txt");
			metric3.getPrecisionByTopN("D:/data/drug-target/results/bi/all/10folder/"+similarityType+"_TBSI_precisionByTopN_" + i 
					+ ".txt");
			
			metric3.getArea(bw,"D:/data/drug-target/results/bi/all/10folder/"+similarityType+"_TBSI_area_" + i + ".txt");
			// drug-drug similar find test


		bw.flush();
		}
		bw.flush();
		bw.close();
	}
	
	public void external_bi2(String similarityType) throws Exception {

		HashSet<String> allTarget = getAllTarget("data/input/drugbank/drugbank_dump.nt");
		HashSet<String> allDrug = getAllDrug("data/input/drugbank/drugbank_dump.nt");
		
		BufferedWriter bw = new BufferedWriter(new FileWriter(new File("D:/data/drug-target/results/bi/external/all/"+similarityType+"_bi_externalAreas.csv")));
		
		
		String datafile = "D:/data/drug-target/source/bi.nt";
		String removefile = "D:/data/drug-target/input/bi/external/all/external_all.nt";
		
			
			HashMap<String, HashSet<String>> drugTargetassociations = getDrugTargetAssociations(datafile);
			HashMap<String, HashSet<String>> targetDrugassociations = getTargetDrugAssociations(datafile);
			
			SimpleWeightedGraph<String, DefaultWeightedEdge> drugPPCGraph=null;
			SimpleWeightedGraph<String, DefaultWeightedEdge> targetPPCGraph=null;
			
			
			if(similarityType.equals(SimilaritiesPrediction.csi)){
				PCCNetworkGenerate generate1=new PCCNetworkGenerate(drugTargetassociations, allTarget);
				generate1.generate();
				drugPPCGraph= generate1.getGraph();	
				
				PCCNetworkGenerate generate2=new PCCNetworkGenerate(targetDrugassociations, allDrug);
				generate2.generate();
				targetPPCGraph= generate2.getGraph();	
				
			}

			// drug-drug similar find test

			
			TargetPrediction predict2 = new TargetPrediction();
			predict2.feedGold(removefile);

			HashSet<String> queries2 = predict2.getDrugAsQueries();

			HashMap<String, HashMap<String, Double>> results2=predict2.predictBySimilarDrug(
					similarityType, queries2, drugTargetassociations,
					drugPPCGraph,allDrug,allTarget);

			EvaluationMetrics metric2 = new EvaluationMetrics(results2, predict2.getGoldmap());
			metric2.getAreasByTopN(
					"D:/data/drug-target/results/bi/external/all/"+similarityType+"_DBSI_areasByTopN_external.txt");
			metric2.getAreasByPercentage(
					"D:/data/drug-target/results/bi/external/all/"+similarityType+"_DBSI_areasByPercentage_external.txt");
			metric2.getRecallByPercentage(
					"D:/data/drug-target/results/bi/external/all/"+similarityType+"_DBSI_recallByPercentage_external.txt");
			metric2.getRecallByTopN(
					"D:/data/drug-target/results/bi/external/all/"+similarityType+"_DBSI_recallByTopN_external.txt");
			
			metric2.getPrecisionByPercentage("D:/data/drug-target/results/bi/external/all/"+similarityType+"_DBSI_precisionByPercentage_external.txt");
			metric2.getPrecisionByTopN("D:/data/drug-target/results/bi/external/all/"+similarityType+"_DBSI_precisionByTopN_external.txt");
			
			metric2.getArea(bw,"D:/data/drug-target/results/bi/external/all/"+similarityType+"_DBSI_area_external.txt");

			DrugPrediction predict3 = new DrugPrediction();
			predict3.feedGold(removefile);
			
			HashSet<String> queries3 = predict3.getTargetAsQueries();

			HashMap<String, HashMap<String, Double>> results3 = predict3.predictBySimilarTarget( similarityType, queries3,  targetDrugassociations,
					targetPPCGraph,allDrug, allTarget) ;

			EvaluationMetrics metric3 = new EvaluationMetrics(results3, predict3.getGoldmap());
			metric3.getAreasByTopN(
					"D:/data/drug-target/results/bi/external/all/"+similarityType+"_TBSI_areasByTopN_external.txt");
			metric3.getAreasByPercentage(
					"D:/data/drug-target/results/bi/external/all/"+similarityType+"_TBSI_areasByPercentage_external.txt");
			metric3.getRecallByPercentage(
					"D:/data/drug-target/results/bi/external/all/"+similarityType+"_TBSI_recallByPercentage_external.txt");
			metric3.getRecallByTopN(
					"D:/data/drug-target/results/bi/external/all/"+similarityType+"_TBSI_recallByTopN_external.txt");
			
			metric3.getPrecisionByPercentage("D:/data/drug-target/results/bi/external/all/"+similarityType+"_TBSI_precisionByPercentage_external.txt");
			metric3.getPrecisionByTopN("D:/data/drug-target/results/bi/external/all/"+similarityType+"_TBSI_precisionByTopN_external.txt");
			
			metric3.getArea(bw,"D:/data/drug-target/results/bi/external/all/"+similarityType+"_TBSI_area_external.txt");
			// drug-drug similar find test


		bw.flush();
		bw.flush();
		bw.close();
	}
	
	public SimpleWeightedGraph<String, DefaultWeightedEdge> getDrugSimilarityGraph(String inputDir) throws IOException{
		SimpleWeightedGraph<String, DefaultWeightedEdge> graph =new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		for(File file:new File(inputDir).listFiles()){
			BufferedReader br=new BufferedReader(new FileReader(file));	
			String line=null;
			while((line=br.readLine())!=null){
				String[] elements=line.split(" ");
				graph.addVertex(elements[0]);
				graph.addVertex(elements[2]);
				DefaultWeightedEdge edge=new DefaultWeightedEdge();
				graph.addEdge(elements[0], elements[2], edge);
				if(elements[4].contains("NaN")||elements[4].contains("error")){
					graph.setEdgeWeight(edge, 0.0);
				}else{
					graph.setEdgeWeight(edge, Double.valueOf(elements[4]));
				}
				
			}
		}
		return graph;
	}
	
	public SimpleWeightedGraph<String, DefaultWeightedEdge> getDrugSimilarityGraph(File file) throws IOException{
		SimpleWeightedGraph<String, DefaultWeightedEdge> graph =new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		BufferedReader br=new BufferedReader(new FileReader(file));	
		String line=null;
				int i=0;
				ArrayList<String> idx=new ArrayList<>(); 
				while((line=br.readLine())!=null){
					i++;
					String[] elements=line.split(" ");
					if(i<2){
					for (int j = 0; j < elements.length; j++) {
						idx.add(j, elements[j]);
						}
					}else{
						String v1=elements[0];
						for (int j = 1; j < elements.length; j++) {
							String v2=idx.get(j);
							
							if(v1.startsWith("<http://www4.wiwiss.fu-berlin.de/drugbank/resource/")&
									v2.startsWith("<http://www4.wiwiss.fu-berlin.de/drugbank/resource/")&!v1.equals(v2)){
								graph.addVertex(v1);
								graph.addVertex(v2);
								DefaultWeightedEdge edge=new DefaultWeightedEdge();
								graph.addEdge(v1, v2, edge);
								graph.setEdgeWeight(edge, Double.valueOf(elements[j]));
							}
						}
						
					}
				}
		return graph;
	}
	
	
	public SimpleWeightedGraph<String, DefaultWeightedEdge> getTargetSimilarityGraph(String inputDir) throws IOException{
		SimpleWeightedGraph<String, DefaultWeightedEdge> graph =new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		for(File file:new File(inputDir).listFiles()){
			BufferedReader br=new BufferedReader(new FileReader(file));	
			String line=null;
			while((line=br.readLine())!=null){
				String[] elements=line.split(" ");
				graph.addVertex(elements[0]);
				graph.addVertex(elements[1]);
				DefaultWeightedEdge edge=new DefaultWeightedEdge();
				if(!elements[0].equals(elements[1])){
					graph.addEdge(elements[0], elements[1], edge);
					if(elements[2].contains("NaN")||elements[2].contains("edu.ucsd.domi.")){
						graph.setEdgeWeight(edge, 0.0);
					}else{
						graph.setEdgeWeight(edge, Double.valueOf(elements[2]));
					}	
				}
			}
		}
		return graph;
	}
	
	public SimpleWeightedGraph<String, DefaultWeightedEdge> getTargetSimilarityGraph(File file) throws IOException{
		SimpleWeightedGraph<String, DefaultWeightedEdge> graph =new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		BufferedReader br=new BufferedReader(new FileReader(file));	
		String line=null;
				int i=0;
				ArrayList<String> idx=new ArrayList<>(); 
				while((line=br.readLine())!=null){
					i++;
					String[] elements=line.split(" ");
					if(i<2){
					for (int j = 0; j < elements.length; j++) {
						idx.add(j, elements[j]);
						}
					}else{
						String v1=elements[0];
						for (int j = 1; j < elements.length; j++) {
							String v2=idx.get(j);
							
							if(v1.startsWith("<http://www4.wiwiss.fu-berlin.de/drugbank/resource/")&
									v2.startsWith("<http://www4.wiwiss.fu-berlin.de/drugbank/resource/")&!v1.equals(v2)){
								graph.addVertex(v1);
								graph.addVertex(v2);
								DefaultWeightedEdge edge=new DefaultWeightedEdge();
								graph.addEdge(v1, v2, edge);
								graph.setEdgeWeight(edge, Double.valueOf(elements[j]));
							}
						}
						
					}
				}
		return graph;
	}
	
	public HashMap<String, HashMap<String, Double>> external_similarity_DBSI_tri() throws Exception {

		DrugCollector collector=new DrugCollector("data/input/drugbank/drugbank_dump.nt"
				,"D:/data/drug-target/input/tri/external/connected/external_connected_filtered.nt");
		HashSet<String> validDrug=collector.getDrugs();
		HashSet<String>	validTarget=new ProteinCollector().getSourceProteinWithInvalidSeq
				();
		
		
		HashSet<String> allTarget = getAllTarget("data/input/drugbank/drugbank_dump.nt");
		HashSet<String> allDrug = getAllDrug("data/input/drugbank/drugbank_dump.nt");
		
		BufferedWriter bw = new BufferedWriter(new FileWriter(new File("D:/data/drug-target/results/tri/external/similarity_connected/chemialBased_externalAreas.csv")));
			
		String datafile = "D:/data/drug-target/source/tri.nt";
		String removefile = "D:/data/drug-target/input/tri/external/connected/external_connected_filtered.nt";

			HashMap<String, HashSet<String>> drugToTargetassociations = getDrugTargetAssociations(datafile);
			HashMap<String, HashSet<String>> TargetToDrugassociations = getTargetDrugAssociations(datafile);

			
			SimpleWeightedGraph<String, DefaultWeightedEdge> drugSimilarityGraph=getDrugSimilarityGraph("D:/data/drug-target/models/sim/drugs");
			
			// drug-drug similar find test

			TargetPrediction predict2 = new TargetPrediction(null);
			predict2.feedGold(removefile);

			HashSet<String> queries2 = predict2.getDrugAsQueries();

			HashMap<String, HashMap<String, Double>> results2 = predict2.predictBySimilarDrug(drugSimilarityGraph, queries2,
					drugToTargetassociations, validDrug, validTarget);

			EvaluationMetrics metric2 = new EvaluationMetrics(results2, predict2.getGoldmap());
			metric2.getRecallByPercentage(
					"D:/data/drug-target/results/tri/external/similarity_connected/chemialBased_DBSI_recallByPercentage.txt");
			metric2.getRecallByTopN(
					"D:/data/drug-target/results/tri/external/similarity_connected/chemialBased_DBSI_recallByTopN.txt");
			metric2.getPrecisionByPercentage("D:/data/drug-target/results/tri/external/similarity_connected/chemialBased_DBSI_precisionByPercentage.txt");
			metric2.getPrecisionByTopN("D:/data/drug-target/results/tri/external/similarity_connected/chemialBased_DBSI_precisionByTopN.txt");
			metric2.getArea( bw, "D:/data/drug-target/results/tri/external/similarity_connected/chemialBased_DBSI_area.txt");


		bw.flush();
		bw.flush();
		bw.close();
		return results2;
	}
	
	public void tenfolder_tri_fourcategories(String category) throws Exception {
		
		HashSet<String> validDrug=new ExternalSourceGenerator().getDrugsFromFourCetegories("D:/data/drug-target/models/sim/drugs_gold/"+category+".txt");
		HashSet<String>	validTarget=new ExternalSourceGenerator().getTargetsFromFourCetegories("D:/data/drug-target/models/sim/targets_gold/"+category+".txt");
		SimpleWeightedGraph<String, DefaultWeightedEdge> drugSimilarityGraph=getDrugSimilarityGraph(new File("D:/data/drug-target/models/sim/drugs_gold/"+category+".txt"));

		SimpleWeightedGraph<String, DefaultWeightedEdge> targetSimilarityGraph=getTargetSimilarityGraph(new File("D:/data/drug-target/models/sim/targets_gold/"+category+".txt"));

		
		HashSet<String> allTarget = getAllTarget("data/input/drugbank/drugbank_dump.nt");
		HashSet<String> allDrug = getAllDrug("data/input/drugbank/drugbank_dump.nt");
		
		BufferedWriter bw = new BufferedWriter(new FileWriter(new File("D:/data/drug-target/results/tri/connected/4categories/"+category+"/similarityBased_tri_10folderAreas.csv")));
		for (int i = 0; i < 10; i++) {
			String datafile = "D:/data/drug-target/input/tri/connected/4categories/"+category+"/tri_folder-" + i + "_data.nt";
			String modelfile = "D:/data/drug-target/models/deepwalk/tri/connected/4categories/" + category + "/deepwalk_tri_" + i
					+ "_100.txt";
			String idxfile = "D:/data/drug-target/models/deepwalk/tri/connected/4categories/" + category + "/deepwalkidx_tri_" + i
					+ "_100.txt";
			String removefile = "D:/data/drug-target/input/tri/connected/4categories/"+category+"/tri_folder-" + i + "_test.nt";

			HashMap<String, HashSet<String>> drugToTargetassociations = getDrugTargetAssociations(datafile);
			HashMap<String, HashSet<String>> TargetToDrugassociations = getTargetDrugAssociations(datafile);

			
			TargetPrediction predict2 = new TargetPrediction(null);
			predict2.feedGold(removefile);

			HashSet<String> queries2 = predict2.getDrugAsQueries();

			HashMap<String, HashMap<String, Double>> results2 = predict2.predictBySimilarDrug(drugSimilarityGraph, queries2,
					drugToTargetassociations, validDrug, validTarget);
			
			HashMap<String, HashSet<String>> all= getAllPotentials( predict2.getGoldmap(), validTarget, 
					drugToTargetassociations);

			EvaluationMetrics metric2 = new EvaluationMetrics(results2, predict2.getGoldmap(),all);
			metric2.getRecallByPercentage(
					"D:/data/drug-target/results/tri/connected/4categories/"+category+"/chemicalBased_DBSI_recallByPercentage_"+i+".txt");
			metric2.getRecallByTopN(
					"D:/data/drug-target/results/tri/connected/4categories/"+category+"/chemicalBased_DBSI_recallByTopN_"+i+".txt");
			metric2.getPrecisionByPercentage("D:/data/drug-target/results/tri/connected/4categories/"+category+"/chemicalBased_DBSI_precisionByPercentage_"+i+".txt");
			metric2.getPrecisionByTopN("D:/data/drug-target/results/tri/connected/4categories/"+category+"/chemicalBased_DBSI_precisionByTopN_"+i+".txt");
			metric2.getArea( bw, "D:/data/drug-target/results/tri/connected/4categories/"+category+"/chemicalBased_DBSI_area_"+i+".txt");
			
			
			DrugPrediction predict3 = new DrugPrediction(null);
			predict3.feedGold(removefile);

			HashSet<String> queries3 = predict3.getTargetAsQueries();

			HashMap<String, HashMap<String, Double>> results3 = predict3.predictBySimilarTarget(targetSimilarityGraph, queries3,
					TargetToDrugassociations, validDrug, validTarget);
			
			 all= getAllPotentials( predict3.getGoldmap(), validDrug, 
					 TargetToDrugassociations);
			 
			EvaluationMetrics metric3 = new EvaluationMetrics(results3, predict3.getGoldmap(),all);
			metric3.getRecallByPercentage(
					"D:/data/drug-target/results/tri/connected/4categories/"+category+"/genomicSeq_TBSI_recallByPercentage_"+i+".txt");
			metric3.getRecallByTopN(
					"D:/data/drug-target/results/tri/connected/4categories/"+category+"/genomicSeq_TBSI_recallByTopN_"+i+".txt");
			metric3.getPrecisionByPercentage("D:/data/drug-target/results/tri/connected/4categories/"+category+"/genomicSeq_TBSI_precisionByPercentage_"+i+".txt");
			metric3.getPrecisionByTopN("D:/data/drug-target/results/tri/connected/4categories/"+category+"/genomicSeq_TBSI_precisionByTopN_"+i+".txt");
			metric3.getArea( bw, "D:/data/drug-target/results/tri/connected/4categories/"+category+"/genomicSeq_TBSI_area_"+i+".txt");
			
		}
		bw.flush();
		bw.close();
	}
	
	public HashMap<String, HashMap<String, Double>> external_similarity_DBSI_tri(String category) throws Exception {
		
		
		HashSet<String> validDrug=new ExternalSourceGenerator().getDrugsFromFourCetegories("D:/data/drug-target/models/sim/drugs_gold/"+category+".txt");
		HashSet<String>	validTarget=new ExternalSourceGenerator().getTargetsFromFourCetegories("D:/data/drug-target/models/sim/targets_gold/"+category+".txt");
		
		
		HashSet<String> allTarget = getAllTarget("data/input/drugbank/drugbank_dump.nt");
		HashSet<String> allDrug = getAllDrug("data/input/drugbank/drugbank_dump.nt");
		
		BufferedWriter bw = new BufferedWriter(new FileWriter(new File("D:/data/drug-target/results/tri/external/similarity_connected/chemialBased_"+category+"_externalAreas.csv")));
			
		String datafile = "D:/data/drug-target/source/tri.nt";
		String removefile = "D:/data/drug-target/input/tri/external/connected/external_connected_"+category+".nt";

			HashMap<String, HashSet<String>> drugToTargetassociations = getDrugTargetAssociations(datafile);
			HashMap<String, HashSet<String>> TargetToDrugassociations = getTargetDrugAssociations(datafile);

			
			SimpleWeightedGraph<String, DefaultWeightedEdge> drugSimilarityGraph=getDrugSimilarityGraph(new File("D:/data/drug-target/models/sim/drugs_gold/"+category+".txt"));
			
			// drug-drug similar find test

			TargetPrediction predict2 = new TargetPrediction(null);
			predict2.feedGold(removefile);

			HashSet<String> queries2 = predict2.getDrugAsQueries();

			HashMap<String, HashMap<String, Double>> results2 = predict2.predictBySimilarDrug(drugSimilarityGraph, queries2,
					drugToTargetassociations, validDrug, validTarget);
			
			
			HashMap<String, HashSet<String>> all= getAllPotentials( predict2.getGoldmap(), validTarget,
					drugToTargetassociations);

			EvaluationMetrics metric2 = new EvaluationMetrics(results2, predict2.getGoldmap(),all);
			metric2.getRecallByPercentage(
					"D:/data/drug-target/results/tri/external/similarity_connected/chemialBased_DBSI_"+category+"_recallByPercentage.txt");
			metric2.getRecallByTopN(
					"D:/data/drug-target/results/tri/external/similarity_connected/chemialBased_DBSI_"+category+"_recallByTopN.txt");
			metric2.getPrecisionByPercentage("D:/data/drug-target/results/tri/external/similarity_connected/chemialBased_DBSI_"+category+"_precisionByPercentage.txt");
			metric2.getPrecisionByTopN("D:/data/drug-target/results/tri/external/similarity_connected/chemialBased_DBSI_"+category+"_precisionByTopN.txt");
			metric2.getArea( bw, "D:/data/drug-target/results/tri/external/similarity_connected/chemialBased_DBSI_"+category+"_area.txt");


		bw.flush();
		bw.flush();
		bw.close();
		return results2;
	}
	
	public HashMap<String, HashSet<String>> getAllPotentials( HashMap<String, HashSet<String>> gold, HashSet<String> allpotentials, 
			HashMap<String, HashSet<String>> exsistings){
		
		HashMap<String, HashSet<String>> all=new HashMap<>();
		
		for(Entry<String,HashSet<String>> entry:gold.entrySet()){
			HashSet<String> set=new HashSet<>();
			for(String string:entry.getValue()){
				set.add(string);
			}
			for(String string:allpotentials){
				if(!exsistings.get(entry.getKey()).contains(string)){
					set.add(string);
				}
			}
			all.put(entry.getKey(),set);
		}
		
		return all;
	}
	
	public HashMap<String, HashMap<String, Double>> external_similarity_TBSI_tri() throws Exception {

		DrugCollector collector=new DrugCollector("data/input/drugbank/drugbank_dump.nt"
				,"D:/data/drug-target/input/tri/external/connected/external_connected_filtered.nt");
		HashSet<String> validDrug=collector.getDrugs();
		HashSet<String>	validTarget=new ProteinCollector().getSourceProteinWithInvalidSeq
				();
		
		
		HashSet<String> allTarget = getAllTarget("data/input/drugbank/drugbank_dump.nt");
		HashSet<String> allDrug = getAllDrug("data/input/drugbank/drugbank_dump.nt");
		
		BufferedWriter bw = new BufferedWriter(new FileWriter(new File("D:/data/drug-target/results/tri/external/similarity_connected/genomicSeqBased_externalAreas.csv")));
			
		String datafile = "D:/data/drug-target/source/tri.nt";
		String removefile = "D:/data/drug-target/input/tri/external/connected/external_connected_filtered.nt";

			HashMap<String, HashSet<String>> drugToTargetassociations = getDrugTargetAssociations(datafile);
			HashMap<String, HashSet<String>> TargetToDrugassociations = getTargetDrugAssociations(datafile);
			

			SimpleWeightedGraph<String, DefaultWeightedEdge> targetSimilarityGraph=getTargetSimilarityGraph("D:/data/drug-target/models/sim/target");
			
			
			// drug-drug similar find test

			
			DrugPrediction predict3 = new DrugPrediction(null);
			predict3.feedGold(removefile);

			HashSet<String> queries3 = predict3.getTargetAsQueries();

			HashMap<String, HashMap<String, Double>> results3 = predict3.predictBySimilarTarget(targetSimilarityGraph, queries3,
					TargetToDrugassociations, validDrug, validTarget);

			EvaluationMetrics metric3 = new EvaluationMetrics(results3, predict3.getGoldmap());
//			metric3.getAreasByTopN(
//					"D:/data/drug-target/results/bi/10folder/deepwalk_TBSI_areasByTopN_" + i  + ".txt");
//			metric3.getAreasByPercentage(
//					"D:/data/drug-target/results/bi/10folder/deepwalk_TBSI_areasByPercentage_" + i  + ".txt");
			metric3.getRecallByPercentage(
					"D:/data/drug-target/results/tri/external/similarity_connected/genomicSeqBased_TBSI_recallByPercentage.txt");
			metric3.getRecallByTopN(
					"D:/data/drug-target/results/tri/external/similarity_connected/genomicSeqBased_TBSI_recallByTopN.txt");
			metric3.getPrecisionByPercentage("D:/data/drug-target/results/tri/external/similarity_connected/genomicSeqBased_TBSI_precisionByPercentage.txt");
			metric3.getPrecisionByTopN("D:/data/drug-target/results/tri/external/similarity_connected/genomicSeqBased_TBSI_precisionByTopN.txt");
			metric3.getArea( bw, "D:/data/drug-target/results/tri/external/similarity_connected/genomicSeqBased_TBSI_area.txt");
			// drug-drug similar find test


		bw.flush();
		bw.flush();
		bw.close();
		return results3;
	}
	
	
	public HashMap<String, HashMap<String, Double>> external_similarity_TBSI_tri(String category) throws Exception {

		HashSet<String> validDrug=new ExternalSourceGenerator().getDrugsFromFourCetegories("D:/data/drug-target/models/sim/drugs_gold/"+category+".txt");
		HashSet<String>	validTarget=new ExternalSourceGenerator().getTargetsFromFourCetegories("D:/data/drug-target/models/sim/targets_gold/"+category+".txt");
		
		
		
		HashSet<String> allTarget = getAllTarget("data/input/drugbank/drugbank_dump.nt");
		HashSet<String> allDrug = getAllDrug("data/input/drugbank/drugbank_dump.nt");
		
		BufferedWriter bw = new BufferedWriter(new FileWriter(new File("D:/data/drug-target/results/tri/external/similarity_connected/genomicSeqBased_"+category+"_externalAreas.csv")));
			
		String datafile = "D:/data/drug-target/source/tri.nt";
		String removefile = "D:/data/drug-target/input/tri/external/connected/external_connected_"+category+".nt";

			HashMap<String, HashSet<String>> drugToTargetassociations = getDrugTargetAssociations(datafile);
			HashMap<String, HashSet<String>> TargetToDrugassociations = getTargetDrugAssociations(datafile);
			

			SimpleWeightedGraph<String, DefaultWeightedEdge> targetSimilarityGraph=getTargetSimilarityGraph(new File("D:/data/drug-target/models/sim/targets_gold/"+category+".txt"));
			
			
			// drug-drug similar find test

			
			DrugPrediction predict3 = new DrugPrediction(null);
			predict3.feedGold(removefile);

			HashSet<String> queries3 = predict3.getTargetAsQueries();

			HashMap<String, HashMap<String, Double>> results3 = predict3.predictBySimilarTarget(targetSimilarityGraph, queries3,
					TargetToDrugassociations, validDrug, validTarget);
			
			HashMap<String, HashSet<String>> all= getAllPotentials( predict3.getGoldmap(), validDrug,
					TargetToDrugassociations);

			EvaluationMetrics metric3 = new EvaluationMetrics(results3, predict3.getGoldmap(),all);
//			metric3.getAreasByTopN(
//					"D:/data/drug-target/results/bi/10folder/deepwalk_TBSI_areasByTopN_" + i  + ".txt");
//			metric3.getAreasByPercentage(
//					"D:/data/drug-target/results/bi/10folder/deepwalk_TBSI_areasByPercentage_" + i  + ".txt");
			metric3.getRecallByPercentage(
					"D:/data/drug-target/results/tri/external/similarity_connected/genomicSeqBased_TBSI_"+category+"_recallByPercentage.txt");
			metric3.getRecallByTopN(
					"D:/data/drug-target/results/tri/external/similarity_connected/genomicSeqBased_TBSI_"+category+"_recallByTopN.txt");
			metric3.getPrecisionByPercentage("D:/data/drug-target/results/tri/external/similarity_connected/genomicSeqBased_TBSI_"+category+"_precisionByPercentage.txt");
			metric3.getPrecisionByTopN("D:/data/drug-target/results/tri/external/similarity_connected/genomicSeqBased_TBSI_"+category+"_precisionByTopN.txt");
			metric3.getArea( bw, "D:/data/drug-target/results/tri/external/similarity_connected/genomicSeqBased_TBSI_"+category+"_area.txt");
			// drug-drug similar find test


		bw.flush();
		bw.close();
		return results3;
	}
	
	
	public void external_bi(String similarityType) throws Exception {

		HashSet<String> allTarget = getAllTarget("data/input/drugbank/drugbank_dump.nt");
		HashSet<String> allDrug = getAllDrug("data/input/drugbank/drugbank_dump.nt");
		BufferedWriter bw=new BufferedWriter(new FileWriter(new File("data/output/experiment_similarities/similarities_"+similarityType+"_external_auc.txt")));
		
		String datafile = "data/input/drugCloud/data_bi.nt";
		String externalfile = "data/input/drugCloud/drugbank_predict_4.nt";
			

			HashMap<String, HashSet<String>> drugTargetassociations = getDrugTargetAssociations(datafile);
			HashMap<String, HashSet<String>> targetDrugassociations = getTargetDrugAssociations(datafile);
			
			SimpleWeightedGraph<String, DefaultWeightedEdge> drugPPCGraph=null;
			SimpleWeightedGraph<String, DefaultWeightedEdge> targetPPCGraph=null;
			
			
			if(similarityType.equals(SimilaritiesPrediction.csi)){
				System.out.println("ppc graph building starts ...");
				PCCNetworkGenerate generate1=new PCCNetworkGenerate(drugTargetassociations, allTarget);
				generate1.generate();
				drugPPCGraph= generate1.getGraph();	
				System.out.println("graph 1 finished ...");
				PCCNetworkGenerate generate2=new PCCNetworkGenerate(targetDrugassociations, allDrug);
				generate2.generate();
				targetPPCGraph= generate2.getGraph();	
				System.out.println("graph 2 finished ...");
			}
			

			TargetPrediction predict1 = new TargetPrediction();
			predict1.feedGold(externalfile);

			HashMap<String, HashSet<String>> queries1 = predict1.getTargetAsQueries(datafile);

			
			HashMap<String, HashMap<String, Double>> results1=predict1.predictBySimilarTarget(
					similarityType,queries1, targetDrugassociations, targetPPCGraph,
					allDrug,allTarget); 

			EvaluationMetrics metric1 = new EvaluationMetrics(results1, predict1.getGoldmap());
			metric1.getAreasByTopN(
					"data/output/experiment_similarities/"+similarityType+"_predictTargetByTarget_getAreasByTopN_" + "external" + ".txt");
			metric1.getAreasByPercentage(
					"data/output/experiment_similarities/"+similarityType+"_predictTargetByTarget_getAreasByPercentage_" + "external" + ".txt");
			metric1.getRecallByPercentage(
					"data/output/experiment_similarities/"+similarityType+"_predictTargetByTarget_getRecallByPercentage_" + "external" + ".txt");
			metric1.getRecallByTopN(
					"data/output/experiment_similarities/"+similarityType+"_predictTargetByTarget_getRecallByTopN_" + "external" + ".txt");
			metric1.getArea(bw,"data/output/experiment_similarities/"+similarityType+"_predictTargetByTarget_getArea_" + "external" + ".txt");

			// drug-drug similar find test

			TargetPrediction predict2 = new TargetPrediction();
			predict2.feedGold(externalfile);

			HashSet<String> queries2 = predict2.getDrugAsQueries();

			HashMap<String, HashMap<String, Double>> results2=predict2.predictBySimilarDrug(
					similarityType, queries2, drugTargetassociations,
					drugPPCGraph,allDrug,allTarget);

			EvaluationMetrics metric2 = new EvaluationMetrics(results2, predict2.getGoldmap());
			metric2.getAreasByTopN(
					"data/output/experiment_similarities/"+similarityType+"_predictTargetByDrug_getAreasByTopN_" + "external" + ".txt");
			metric2.getAreasByPercentage(
					"data/output/experiment_similarities/"+similarityType+"_predictTargetByDrug_getAreasByPercentage_" + "external" + ".txt");
			metric2.getRecallByPercentage(
					"data/output/experiment_similarities/"+similarityType+"_predictTargetByDrug_getRecallByPercentage_" + "external"
							+ ".txt");
			metric2.getRecallByTopN(
					"data/output/experiment_similarities/"+similarityType+"_predictTargetByDrug_getRecallByTopN_" + "external" + ".txt");
			metric2.getArea(bw,"data/output/experiment_similarities/"+similarityType+"_predictTargetByDrug_getArea_" + "external" + ".txt");

			DrugPrediction predict3 = new DrugPrediction();
			predict3.feedGold(externalfile);
			
			HashSet<String> queries3 = predict3.getTargetAsQueries();

			HashMap<String, HashMap<String, Double>> results3 = predict3.predictBySimilarTarget( similarityType, queries3,  targetDrugassociations,
					targetPPCGraph,allDrug, allTarget) ;

			EvaluationMetrics metric3 = new EvaluationMetrics(results3, predict3.getGoldmap());
			metric3.getAreasByTopN(
					"data/output/experiment_similarities/"+similarityType+"_predictDrugByTarget_getAreasByTopN_" + "external" + ".txt");
			metric3.getAreasByPercentage(
					"data/output/experiment_similarities/"+similarityType+"_predictDrugByTarget_getAreasByPercentage_" + "external" + ".txt");
			metric3.getRecallByPercentage(
					"data/output/experiment_similarities/"+similarityType+"_predictDrugByTarget_getRecallByPercentage_" + "external"
							+ ".txt");
			metric3.getRecallByTopN(
					"data/output/experiment_similarities/"+similarityType+"_predictDrugByTarget_getRecallByTopN_" + "external" + ".txt");
			metric3.getArea(bw,"data/output/experiment_similarities/"+similarityType+"_predictDrugByTarget_getArea_" + "external" + ".txt");

			// drug-drug similar find test

			DrugPrediction predict4 = new DrugPrediction();
			predict4.feedGold(externalfile);

			HashMap<String, HashSet<String>> queries4 = predict4.getDrugAsQueries(datafile);

			HashMap<String, HashMap<String, Double>> results4 = predict4.predictBySimilarDrug(similarityType,queries4, drugTargetassociations, drugPPCGraph,
						allDrug,allTarget);

			EvaluationMetrics metric4 = new EvaluationMetrics(results4, predict4.getGoldmap());
			metric4.getAreasByTopN(
					"data/output/experiment_similarities/"+similarityType+"_predictDrugByDrug_getAreasByTopN_" + "external" + ".txt");
			metric4.getAreasByPercentage(
					"data/output/experiment_similarities/"+similarityType+"_predictDrugByDrug_getAreasByPercentage_" + "external" + ".txt");
			metric4.getRecallByPercentage(
					"data/output/experiment_similarities/"+similarityType+"_predictDrugByDrug_getRecallByPercentage_" + "external" + ".txt");
			metric4.getRecallByTopN(
					"data/output/experiment_similarities/"+similarityType+"_predictDrugByDrug_getRecallByTopN_" + "external" + ".txt");
			metric4.getArea(bw,"data/output/experiment_similarities/"+similarityType+"_predictDrugByDrug_getArea_" + "external" + ".txt");

	}

	public HashSet<String> getAllTarget(String datafile) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(new File(datafile)));
		String line = null;
		HashSet<String> set = new HashSet<>();
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
					set.add(o);
				}
			}
		}
		return set;
	}

	public HashSet<String> getAllDrug(String datafile) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(new File(datafile)));
		String line = null;
		HashSet<String> set = new HashSet<>();
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
					set.add(s);
				}
			}
		}
		return set;
	}

	public HashMap<String, HashSet<String>> getDrugTargetAssociations(String datafile) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(new File(datafile)));
		HashMap<String, List<Map.Entry<String, Double>>> results = new HashMap<>();
		String line = null;
		HashMap<String, HashSet<String>> associations = new HashMap<>();
		while ((line = br.readLine()) != null) {
			if (!line.contains("\"")) {
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

						if (associations.containsKey(s)) {
							associations.get(s).add(o);
						} else {
							HashSet<String> set = new HashSet<>();
							set.add(o);
							associations.put(s, set);
						}

					}
				}
			}
		}
		return associations;
	}

	public HashMap<String, HashSet<String>> getTargetDrugAssociations(String datafile) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(new File(datafile)));
		HashMap<String, List<Map.Entry<String, Double>>> results = new HashMap<>();
		String line = null;
		HashMap<String, HashSet<String>> associations = new HashMap<>();
		while ((line = br.readLine()) != null) {
			if (!line.contains("\"")) {
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

						if (associations.containsKey(o)) {
							associations.get(o).add(s);
						} else {
							HashSet<String> set = new HashSet<>();
							set.add(s);
							associations.put(o, set);
						}

					}
				}
			}
		}
		return associations;
	}
}
