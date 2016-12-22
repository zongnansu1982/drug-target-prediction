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
import java.util.Map.Entry;

import org.semanticweb.yars.nx.Node;
import org.semanticweb.yars.nx.parser.NxParser;

import edu.ucsd.domi.word2vec.dataProcess.dataGenerate.ExternalSourceGenerator;
import edu.ucsd.domi.word2vec.methods.chemicalsimilarities.DrugCollector;
import edu.ucsd.domi.word2vec.methods.experiment.DrugPrediction;
import edu.ucsd.domi.word2vec.methods.experiment.EvaluationMetrics;
import edu.ucsd.domi.word2vec.methods.experiment.TargetPrediction;
import edu.ucsd.domi.word2vec.methods.proteinsimilarities.ProteinCollector;

public class DeepWalkPredictionNew {
 
	
	

public void monteCarlo_tri(String type) throws Exception {
		
		
		HashSet<String> allTarget = getAllTarget("data/input/drugbank/drugbank_dump.nt");
		HashSet<String> allDrug = getAllDrug("data/input/drugbank/drugbank_dump.nt");
		
		BufferedWriter bw = new BufferedWriter(new FileWriter(new File("D:/data/drug-target/results/tri/"+type+"/monteCarlo/deepwalk_tri_monteCarloAreas.csv")));
		for (int i = 1; i < 11; i++) {
			
			String datafile = "D:/data/drug-target/input/tri/"+type+"/monteCarlo/tri_" + i*100 + "_data.nt";
			String removefile = "D:/data/drug-target/input/tri/"+type+"/monteCarlo/tri_" + i*100 + "_test.nt";
			String modelfile = "D:/data/drug-target/models/deepwalk/tri/"+type+"/monteCarlo/deepwalk_tri_" + i*100 + "_100.txt";
		 	String idxfile="D:/data/drug-target/models/deepwalk/tri/"+type+"/monteCarlo/deepwalkidx_tri_" + i*100 + "_100.txt";

			HashMap<String, HashSet<String>> drugToTargetassociations = getDrugTargetAssociations(datafile);
			HashMap<String, HashSet<String>> TargetToDrugassociations = getTargetDrugAssociations(datafile);


			TargetPrediction predict2 = new TargetPrediction(modelfile);
			predict2.feedGold(removefile);

			HashSet<String> queries2 = predict2.getDrugAsQueries();

			HashMap<String, HashMap<String, Double>> results2 = predict2.predictBySimilarDrug(idxfile, queries2,
					drugToTargetassociations, allDrug, allTarget);

			EvaluationMetrics metric2 = new EvaluationMetrics(results2, predict2.getGoldmap());
//			metric2.getAreasByTopN(
//					"D:/data/drug-target/results/tri/"+type+"/10folder/deepwalk_DBSI_areasByTopN_" + i  + ".txt");
//			metric2.getAreasByPercentage(
//					"D:/data/drug-target/results/tri/"+type+"/10folder/deepwalk_DBSI_areasByPercentage_" + i  + ".txt");
			metric2.getRecallByPercentage(
					"D:/data/drug-target/results/tri/"+type+"/monteCarlo/deepwalk_DBSI_recallByPercentage_" + i*100 
							+ ".txt");
			metric2.getRecallByTopN(
					"D:/data/drug-target/results/tri/"+type+"/monteCarlo/deepwalk_DBSI_recallByTopN_" + i*100 + ".txt");
			metric2.getPrecisionByPercentage("D:/data/drug-target/results/tri/"+type+"/monteCarlo/deepwalk_DBSI_precisionByPercentage_" + i*100 
					+ ".txt");
			metric2.getPrecisionByTopN("D:/data/drug-target/results/tri/"+type+"/monteCarlo/deepwalk_DBSI_precisionByTopN_" + i*100 
					+ ".txt");
			metric2.getArea( bw, "D:/data/drug-target/results/tri/"+type+"/monteCarlo/deepwalk_DBSI_area_" + i*100  + ".txt");

			DrugPrediction predict3 = new DrugPrediction(modelfile);
			predict3.feedGold(removefile);

			HashSet<String> queries3 = predict3.getTargetAsQueries();

			HashMap<String, HashMap<String, Double>> results3 = predict3.predictBySimilarTarget(idxfile, queries3,
					TargetToDrugassociations, allDrug, allTarget);

			EvaluationMetrics metric3 = new EvaluationMetrics(results3, predict3.getGoldmap());
//			metric3.getAreasByTopN(
//					"D:/data/drug-target/results/bi/10folder/deepwalk_TBSI_areasByTopN_" + i  + ".txt");
//			metric3.getAreasByPercentage(
//					"D:/data/drug-target/results/bi/10folder/deepwalk_TBSI_areasByPercentage_" + i  + ".txt");
			metric3.getRecallByPercentage(
					"D:/data/drug-target/results/tri/"+type+"/monteCarlo/deepwalk_TBSI_recallByPercentage_" + i*100 
							+ ".txt");
			metric3.getRecallByTopN(
					"D:/data/drug-target/results/tri/"+type+"/monteCarlo/deepwalk_TBSI_recallByTopN_" + i * 100 + ".txt");
			metric3.getPrecisionByPercentage("D:/data/drug-target/results/tri/"+type+"/monteCarlo/deepwalk_TBSI_precisionByPercentage_" + i*100 
					+ ".txt");
			metric3.getPrecisionByTopN("D:/data/drug-target/results/tri/"+type+"/monteCarlo/deepwalk_TBSI_precisionByTopN_" + i*100 
					+ ".txt");
			metric3.getArea( bw, "D:/data/drug-target/results/tri/"+type+"/monteCarlo/deepwalk_TBSI_area_" + i*100  + ".txt");

			bw.flush();
			
		}
		bw.close();
		bw.close();
	}


public void multipleMonteCarlo_tri(String type) throws Exception {
	
	
	HashSet<String> allTarget = getAllTarget("data/input/drugbank/drugbank_dump.nt");
	HashSet<String> allDrug = getAllDrug("data/input/drugbank/drugbank_dump.nt");
	
	BufferedWriter bw = new BufferedWriter(new FileWriter(new File("D:/data/drug-target/results/tri/connected/triMonteCarlo/"+type+"/deepwalk_tri_monteCarloAreas.csv")));
	for (int i = 1; i < 11; i++) {
		
		
		String datafile = "D:/data/drug-target/input/tri/connected/triMonteCarlo/"+type+"/tri_" + i*10 + "_data.nt";
		String removefile = "D:/data/drug-target/input/tri/external/connected/external_connected.nt";
		
		String modelfile = "D:/data/drug-target/models/deepwalk/tri/connected/triMonteCarlo/"+type+"/deepwalk_tri_" + i*10 + "_100.txt";
	 	String idxfile="D:/data/drug-target/models/deepwalk/tri/connected/triMonteCarlo/"+type+"/deepwalkidx_tri_" + i*10 + "_100.txt";

		HashMap<String, HashSet<String>> drugToTargetassociations = getDrugTargetAssociations(datafile);
		HashMap<String, HashSet<String>> TargetToDrugassociations = getTargetDrugAssociations(datafile);
		
		TargetPrediction predict2 = new TargetPrediction(modelfile);
		predict2.feedGold(removefile);

		HashSet<String> queries2 = predict2.getDrugAsQueries();
		HashMap<String, HashMap<String, Double>> results2 = predict2.predictBySimilarDrug(idxfile, queries2,
				drugToTargetassociations, allDrug, allTarget);

		EvaluationMetrics metric2 = new EvaluationMetrics(results2, predict2.getGoldmap());
//		metric2.getAreasByTopN(
//				"D:/data/drug-target/results/tri/"+type+"/10folder/deepwalk_DBSI_areasByTopN_" + i  + ".txt");
//		metric2.getAreasByPercentage(
//				"D:/data/drug-target/results/tri/"+type+"/10folder/deepwalk_DBSI_areasByPercentage_" + i  + ".txt");
		metric2.getRecallByPercentage(
				"D:/data/drug-target/results/tri/connected/triMonteCarlo/"+type+"/deepwalk_DBSI_recallByPercentage_" + i*10 
						+ ".txt");
		metric2.getRecallByTopN(
				"D:/data/drug-target/results/tri/connected/triMonteCarlo/"+type+"/deepwalk_DBSI_recallByTopN_" + i*10 + ".txt");
		metric2.getPrecisionByPercentage("D:/data/drug-target/results/tri/connected/triMonteCarlo/"+type+"/deepwalk_DBSI_precisionByPercentage_" + i*10 
				+ ".txt");
		metric2.getPrecisionByTopN("D:/data/drug-target/results/tri/connected/triMonteCarlo/"+type+"/deepwalk_DBSI_precisionByTopN_" + i*10 
				+ ".txt");
		metric2.getArea( bw, "D:/data/drug-target/results/tri/connected/triMonteCarlo/"+type+"/deepwalk_DBSI_area_" + i*10  + ".txt");

		DrugPrediction predict3 = new DrugPrediction(modelfile);
		predict3.feedGold(removefile);

		HashSet<String> queries3 = predict3.getTargetAsQueries();

		HashMap<String, HashMap<String, Double>> results3 = predict3.predictBySimilarTarget(idxfile, queries3,
				TargetToDrugassociations, allDrug, allTarget);

		EvaluationMetrics metric3 = new EvaluationMetrics(results3, predict3.getGoldmap());
//		metric3.getAreasByTopN(
//				"D:/data/drug-target/results/bi/10folder/deepwalk_TBSI_areasByTopN_" + i  + ".txt");
//		metric3.getAreasByPercentage(
//				"D:/data/drug-target/results/bi/10folder/deepwalk_TBSI_areasByPercentage_" + i  + ".txt");
		metric3.getRecallByPercentage(
				"D:/data/drug-target/results/tri/connected/triMonteCarlo/"+type+"/deepwalk_TBSI_recallByPercentage_" + i*10 
						+ ".txt");
		metric3.getRecallByTopN(
				"D:/data/drug-target/results/tri/connected/triMonteCarlo/"+type+"/deepwalk_TBSI_recallByTopN_" + i*10 + ".txt");
		metric3.getPrecisionByPercentage("D:/data/drug-target/results/tri/connected/triMonteCarlo/"+type+"/deepwalk_TBSI_precisionByPercentage_" + i*10 
				+ ".txt");
		metric3.getPrecisionByTopN("D:/data/drug-target/results/tri/connected/triMonteCarlo/"+type+"/deepwalk_TBSI_precisionByTopN_" + i*10 
				+ ".txt");
		metric3.getArea( bw, "D:/data/drug-target/results/tri/connected/triMonteCarlo/"+type+"/deepwalk_TBSI_area_" + i*10  + ".txt");

		bw.flush();
		
	}
	bw.close();
	bw.close();
}

public void monteCarlo_bi(String type) throws Exception {
	
	
	HashSet<String> allTarget = getAllTarget("data/input/drugbank/drugbank_dump.nt");
	HashSet<String> allDrug = getAllDrug("data/input/drugbank/drugbank_dump.nt");
	
	BufferedWriter bw = new BufferedWriter(new FileWriter(new File("D:/data/drug-target/results/bi/"+type+"/monteCarlo/deepwalk_bi_monteCarloAreas.csv")));
	for (int i = 1; i < 11; i++) {
		
		String datafile = "D:/data/drug-target/input/bi/"+type+"/monteCarlo/bi_" + i*100 + "_data.nt";
		String removefile = "D:/data/drug-target/input/bi/"+type+"/monteCarlo/bi_" + i*100 + "_test.nt";
		String modelfile = "D:/data/drug-target/models/deepwalk/bi/"+type+"/monteCarlo/deepwalk_bi_" + i*100 + "_100.txt";
	 	String idxfile="D:/data/drug-target/models/deepwalk/bi/"+type+"/monteCarlo/deepwalkidx_bi_" + i*100 + "_100.txt";
	   

		HashMap<String, HashSet<String>> drugToTargetassociations = getDrugTargetAssociations(datafile);
		HashMap<String, HashSet<String>> TargetToDrugassociations = getTargetDrugAssociations(datafile);


		TargetPrediction predict2 = new TargetPrediction(modelfile);
		predict2.feedGold(removefile);

		HashSet<String> queries2 = predict2.getDrugAsQueries();

		HashMap<String, HashMap<String, Double>> results2 = predict2.predictBySimilarDrug(idxfile, queries2,
				drugToTargetassociations, allDrug, allTarget);

		EvaluationMetrics metric2 = new EvaluationMetrics(results2, predict2.getGoldmap());
//		metric2.getAreasByTopN(
//				"D:/data/drug-target/results/tri/"+type+"/10folder/deepwalk_DBSI_areasByTopN_" + i  + ".txt");
//		metric2.getAreasByPercentage(
//				"D:/data/drug-target/results/tri/"+type+"/10folder/deepwalk_DBSI_areasByPercentage_" + i  + ".txt");
		metric2.getRecallByPercentage(
				"D:/data/drug-target/results/bi/"+type+"/monteCarlo/deepwalk_DBSI_recallByPercentage_" + i*100 
						+ ".txt");
		metric2.getRecallByTopN(
				"D:/data/drug-target/results/bi/"+type+"/monteCarlo/deepwalk_DBSI_recallByTopN_" + i*100 + ".txt");
		metric2.getPrecisionByPercentage("D:/data/drug-target/results/bi/"+type+"/monteCarlo/deepwalk_DBSI_precisionByPercentage_" + i*100 
				+ ".txt");
		metric2.getPrecisionByTopN("D:/data/drug-target/results/bi/"+type+"/monteCarlo/deepwalk_DBSI_precisionByTopN_" + i*100 
				+ ".txt");
		metric2.getArea( bw, "D:/data/drug-target/results/bi/"+type+"/monteCarlo/deepwalk_DBSI_area_" + i*100  + ".txt");

		DrugPrediction predict3 = new DrugPrediction(modelfile);
		predict3.feedGold(removefile);

		HashSet<String> queries3 = predict3.getTargetAsQueries();

		HashMap<String, HashMap<String, Double>> results3 = predict3.predictBySimilarTarget(idxfile, queries3,
				TargetToDrugassociations, allDrug, allTarget);

		EvaluationMetrics metric3 = new EvaluationMetrics(results3, predict3.getGoldmap());
//		metric3.getAreasByTopN(
//				"D:/data/drug-target/results/bi/10folder/deepwalk_TBSI_areasByTopN_" + i  + ".txt");
//		metric3.getAreasByPercentage(
//				"D:/data/drug-target/results/bi/10folder/deepwalk_TBSI_areasByPercentage_" + i  + ".txt");
		metric3.getRecallByPercentage(
				"D:/data/drug-target/results/bi/"+type+"/monteCarlo/deepwalk_TBSI_recallByPercentage_" + i*100 
						+ ".txt");
		metric3.getRecallByTopN(
				"D:/data/drug-target/results/bi/"+type+"/monteCarlo/deepwalk_TBSI_recallByTopN_" + i * 100 + ".txt");
		metric3.getPrecisionByPercentage("D:/data/drug-target/results/bi/"+type+"/monteCarlo/deepwalk_TBSI_precisionByPercentage_" + i*100 
				+ ".txt");
		metric3.getPrecisionByTopN("D:/data/drug-target/results/bi/"+type+"/monteCarlo/deepwalk_TBSI_precisionByTopN_" + i*100 
				+ ".txt");
		metric3.getArea( bw, "D:/data/drug-target/results/bi/"+type+"/monteCarlo/deepwalk_TBSI_area_" + i*100  + ".txt");

		bw.flush();
		
	}
	bw.close();
	bw.close();
}

public void tenfolder_tri(String type) throws Exception {
		
		
		HashSet<String> allTarget = getAllTarget("data/input/drugbank/drugbank_dump.nt");
		HashSet<String> allDrug = getAllDrug("data/input/drugbank/drugbank_dump.nt");
		
		BufferedWriter bw = new BufferedWriter(new FileWriter(new File("D:/data/drug-target/results/tri/"+type+"/10folder/deepwalk_tri_10folderAreas.csv")));
		for (int i = 0; i < 10; i++) {
			
			String datafile = "D:/data/drug-target/input/tri/"+type+"/10folder/tri_folder-" + i + "_data.nt";
			String modelfile = "D:/data/drug-target/models/deepwalk/tri/"+type+"/10folder/deepwalk_tri_" + i + "_100.txt";
		 	String idxfile="D:/data/drug-target/models/deepwalk/tri/"+type+"/10folder/deepwalkidx_tri_" + i + "_100.txt";
			String removefile = "D:/data/drug-target/input/tri/"+type+"/10folder/tri_folder-" + i + "_test.nt";

			HashMap<String, HashSet<String>> drugToTargetassociations = getDrugTargetAssociations(datafile);
			HashMap<String, HashSet<String>> TargetToDrugassociations = getTargetDrugAssociations(datafile);


			TargetPrediction predict2 = new TargetPrediction(modelfile);
			predict2.feedGold(removefile);

			HashSet<String> queries2 = predict2.getDrugAsQueries();

			HashMap<String, HashMap<String, Double>> results2 = predict2.predictBySimilarDrug(idxfile, queries2,
					drugToTargetassociations, allDrug, allTarget);

			EvaluationMetrics metric2 = new EvaluationMetrics(results2, predict2.getGoldmap());
//			metric2.getAreasByTopN(
//					"D:/data/drug-target/results/tri/"+type+"/10folder/deepwalk_DBSI_areasByTopN_" + i  + ".txt");
//			metric2.getAreasByPercentage(
//					"D:/data/drug-target/results/tri/"+type+"/10folder/deepwalk_DBSI_areasByPercentage_" + i  + ".txt");
			metric2.getRecallByPercentage(
					"D:/data/drug-target/results/tri/"+type+"/10folder/deepwalk_DBSI_recallByPercentage_" + i 
							+ ".txt");
			metric2.getRecallByTopN(
					"D:/data/drug-target/results/tri/"+type+"/10folder/deepwalk_DBSI_recallByTopN_" + i + ".txt");
			metric2.getPrecisionByPercentage("D:/data/drug-target/results/tri/"+type+"/10folder/deepwalk_DBSI_precisionByPercentage_" + i 
					+ ".txt");
			metric2.getPrecisionByTopN("D:/data/drug-target/results/tri/"+type+"/10folder/deepwalk_DBSI_precisionByTopN_" + i 
					+ ".txt");
			metric2.getArea( bw, "D:/data/drug-target/results/tri/"+type+"/10folder/deepwalk_DBSI_area_" + i  + ".txt");

			DrugPrediction predict3 = new DrugPrediction(modelfile);
			predict3.feedGold(removefile);

			HashSet<String> queries3 = predict3.getTargetAsQueries();

			HashMap<String, HashMap<String, Double>> results3 = predict3.predictBySimilarTarget(idxfile, queries3,
					TargetToDrugassociations, allDrug, allTarget);

			EvaluationMetrics metric3 = new EvaluationMetrics(results3, predict3.getGoldmap());
//			metric3.getAreasByTopN(
//					"D:/data/drug-target/results/bi/10folder/deepwalk_TBSI_areasByTopN_" + i  + ".txt");
//			metric3.getAreasByPercentage(
//					"D:/data/drug-target/results/bi/10folder/deepwalk_TBSI_areasByPercentage_" + i  + ".txt");
			metric3.getRecallByPercentage(
					"D:/data/drug-target/results/tri/"+type+"/10folder/deepwalk_TBSI_recallByPercentage_" + i 
							+ ".txt");
			metric3.getRecallByTopN(
					"D:/data/drug-target/results/tri/"+type+"/10folder/deepwalk_TBSI_recallByTopN_" + i * 100 + ".txt");
			metric3.getPrecisionByPercentage("D:/data/drug-target/results/tri/"+type+"/10folder/deepwalk_TBSI_precisionByPercentage_" + i 
					+ ".txt");
			metric3.getPrecisionByTopN("D:/data/drug-target/results/tri/"+type+"/10folder/deepwalk_TBSI_precisionByTopN_" + i 
					+ ".txt");
			metric3.getArea( bw, "D:/data/drug-target/results/tri/"+type+"/10folder/deepwalk_TBSI_area_" + i  + ".txt");

			bw.flush();
			
		}
		bw.close();
		bw.close();
	}

public void tenfolder_bi(String type) throws Exception {
	
	
	HashSet<String> allTarget = getAllTarget("data/input/drugbank/drugbank_dump.nt");
	HashSet<String> allDrug = getAllDrug("data/input/drugbank/drugbank_dump.nt");
	
	BufferedWriter bw = new BufferedWriter(new FileWriter(new File("D:/data/drug-target/results/bi/"+type+"/10folder/deepwalk_bi_10folderAreas.csv")));
	for (int i = 0; i < 10; i++) {
		
		String datafile = "D:/data/drug-target/input/bi/"+type+"/10folder/bi_folder-" + i + "_data.nt";
		String modelfile = "D:/data/drug-target/models/deepwalk/bi/"+type+"/10folder/deepwalk_bi_" + i + "_100.txt";
	 	String idxfile="D:/data/drug-target/models/deepwalk/bi/"+type+"/10folder/deepwalkidx_bi_" + i + "_100.txt";
		String removefile = "D:/data/drug-target/input/bi/"+type+"/10folder/bi_folder-" + i + "_test.nt";

		HashMap<String, HashSet<String>> drugToTargetassociations = getDrugTargetAssociations(datafile);
		HashMap<String, HashSet<String>> TargetToDrugassociations = getTargetDrugAssociations(datafile);


		TargetPrediction predict2 = new TargetPrediction(modelfile);
		predict2.feedGold(removefile);

		HashSet<String> queries2 = predict2.getDrugAsQueries();

		HashMap<String, HashMap<String, Double>> results2 = predict2.predictBySimilarDrug(idxfile, queries2,
				drugToTargetassociations, allDrug, allTarget);

		EvaluationMetrics metric2 = new EvaluationMetrics(results2, predict2.getGoldmap());
//		metric2.getAreasByTopN(
//				"D:/data/drug-target/results/tri/"+type+"/10folder/deepwalk_DBSI_areasByTopN_" + i  + ".txt");
//		metric2.getAreasByPercentage(
//				"D:/data/drug-target/results/tri/"+type+"/10folder/deepwalk_DBSI_areasByPercentage_" + i  + ".txt");
		metric2.getRecallByPercentage(
				"D:/data/drug-target/results/bi/"+type+"/10folder/deepwalk_DBSI_recallByPercentage_" + i 
						+ ".txt");
		metric2.getRecallByTopN(
				"D:/data/drug-target/results/bi/"+type+"/10folder/deepwalk_DBSI_recallByTopN_" + i + ".txt");
		metric2.getPrecisionByPercentage("D:/data/drug-target/results/bi/"+type+"/10folder/deepwalk_DBSI_precisionByPercentage_" + i 
				+ ".txt");
		metric2.getPrecisionByTopN("D:/data/drug-target/results/bi/"+type+"/10folder/deepwalk_DBSI_precisionByTopN_" + i 
				+ ".txt");
		metric2.getArea( bw, "D:/data/drug-target/results/bi/"+type+"/10folder/deepwalk_DBSI_area_" + i  + ".txt");

		DrugPrediction predict3 = new DrugPrediction(modelfile);
		predict3.feedGold(removefile);

		HashSet<String> queries3 = predict3.getTargetAsQueries();

		HashMap<String, HashMap<String, Double>> results3 = predict3.predictBySimilarTarget(idxfile, queries3,
				TargetToDrugassociations, allDrug, allTarget);

		EvaluationMetrics metric3 = new EvaluationMetrics(results3, predict3.getGoldmap());
//		metric3.getAreasByTopN(
//				"D:/data/drug-target/results/bi/10folder/deepwalk_TBSI_areasByTopN_" + i  + ".txt");
//		metric3.getAreasByPercentage(
//				"D:/data/drug-target/results/bi/10folder/deepwalk_TBSI_areasByPercentage_" + i  + ".txt");
		metric3.getRecallByPercentage(
				"D:/data/drug-target/results/bi/"+type+"/10folder/deepwalk_TBSI_recallByPercentage_" + i 
						+ ".txt");
		metric3.getRecallByTopN(
				"D:/data/drug-target/results/bi/"+type+"/10folder/deepwalk_TBSI_recallByTopN_" + i * 100 + ".txt");
		metric3.getPrecisionByPercentage("D:/data/drug-target/results/bi/"+type+"/10folder/deepwalk_TBSI_precisionByPercentage_" + i 
				+ ".txt");
		metric3.getPrecisionByTopN("D:/data/drug-target/results/bi/"+type+"/10folder/deepwalk_TBSI_precisionByTopN_" + i 
				+ ".txt");
		metric3.getArea( bw, "D:/data/drug-target/results/bi/"+type+"/10folder/deepwalk_TBSI_area_" + i  + ".txt");

		bw.flush();
		
	}
	bw.close();
	bw.close();
}



public void tenfolder_tri_fourcategories(String category) throws Exception {
	

	HashSet<String> validDrug=new ExternalSourceGenerator().getDrugsFromFourCetegories("D:/data/drug-target/models/sim/drugs_gold/"+category+".txt");
	HashSet<String>	validTarget=new ExternalSourceGenerator().getTargetsFromFourCetegories("D:/data/drug-target/models/sim/targets_gold/"+category+".txt");
	
	
	HashSet<String> allTarget = getAllTarget("data/input/drugbank/drugbank_dump.nt");
	HashSet<String> allDrug = getAllDrug("data/input/drugbank/drugbank_dump.nt");
	
	BufferedWriter bw = new BufferedWriter(new FileWriter(new File("D:/data/drug-target/results/tri/connected/4categories/"+category+"/deepwalk_tri_10folderAreas.csv")));
	for (int i = 0; i < 10; i++) {
		
		String datafile = "D:/data/drug-target/input/tri/connected/4categories/"+category+"/tri_folder-" + i + "_data.nt";
		String modelfile = "D:/data/drug-target/models/deepwalk/tri/connected/4categories/" + category + "/deepwalk_tri_" + i
				+ "_100.txt";
		String idxfile = "D:/data/drug-target/models/deepwalk/tri/connected/4categories/" + category + "/deepwalkidx_tri_" + i
				+ "_100.txt";
		String removefile = "D:/data/drug-target/input/tri/connected/4categories/"+category+"/tri_folder-" + i + "_test.nt";

		HashMap<String, HashSet<String>> drugToTargetassociations = getDrugTargetAssociations(datafile);
		HashMap<String, HashSet<String>> TargetToDrugassociations = getTargetDrugAssociations(datafile);

		TargetPrediction predict2 = new TargetPrediction(modelfile);
		predict2.feedGold(removefile);

		HashSet<String> queries2 = predict2.getDrugAsQueries();

		HashMap<String, HashMap<String, Double>> results2 = predict2.predictBySimilarDrug(idxfile, queries2,
				drugToTargetassociations, allDrug, validTarget);
		
		HashMap<String, HashSet<String>> all= getAllPotentials( predict2.getGoldmap(), validTarget, 
				drugToTargetassociations);

		EvaluationMetrics metric2 = new EvaluationMetrics(results2, predict2.getGoldmap(),all);
		metric2.getRecallByPercentage(
				"D:/data/drug-target/results/tri/connected/4categories/"+category+"/deepwalk_DBSI_recallByPercentage_"+i+".txt");
		metric2.getRecallByTopN(
				"D:/data/drug-target/results/tri/connected/4categories/"+category+"/deepwalk_DBSI_recallByTopN_"+i+".txt");
		metric2.getPrecisionByPercentage("D:/data/drug-target/results/tri/connected/4categories/"+category+"/deepwalk_DBSI_precisionByPercentage_"+i+".txt");
		metric2.getPrecisionByTopN("D:/data/drug-target/results/tri/connected/4categories/"+category+"/deepwalk_DBSI_precisionByTopN_"+i+".txt");
		metric2.getArea( bw, "D:/data/drug-target/results/tri/connected/4categories/"+category+"/deepwalk_DBSI_area_"+i+".txt");
		
		DrugPrediction predict3 = new DrugPrediction(modelfile);
		predict3.feedGold(removefile);

		HashSet<String> queries3 = predict3.getTargetAsQueries();

		HashMap<String, HashMap<String, Double>> results3 = predict3.predictBySimilarTarget(idxfile, queries3,
				TargetToDrugassociations, validDrug, allTarget);
		
		 all= getAllPotentials( predict3.getGoldmap(), validDrug, 
				 TargetToDrugassociations);
		
		EvaluationMetrics metric3 = new EvaluationMetrics(results3, predict3.getGoldmap(),all);
		
		metric3.getRecallByPercentage(
				"D:/data/drug-target/results/tri/connected/4categories/"+category+"/deepwalk_TBSI_recallByPercentage_"+i+".txt");
		metric3.getRecallByTopN(
				"D:/data/drug-target/results/tri/connected/4categories/"+category+"/deepwalk_TBSI_recallByTopN_"+i+".txt");
		metric3.getPrecisionByPercentage("D:/data/drug-target/results/tri/connected/4categories/"+category+"/deepwalk_TBSI_precisionByPercentage_"+i+".txt");
		metric3.getPrecisionByTopN("D:/data/drug-target/results/tri/connected/4categories/"+category+"/deepwalk_TBSI_precisionByTopN_"+i+".txt");
		metric3.getArea( bw, "D:/data/drug-target/results/tri/connected/4categories/"+category+"/deepwalk_TBSI_area_"+i+".txt");
		
	}
	bw.flush();
	bw.close();
}


public void tenfolder_tri_fourcategories_svm(String category) throws Exception {
	

	HashSet<String> validDrug=new ExternalSourceGenerator().getDrugsFromFourCetegories("D:/data/drug-target/models/sim/drugs_gold/"+category+".txt");
	HashSet<String>	validTarget=new ExternalSourceGenerator().getTargetsFromFourCetegories("D:/data/drug-target/models/sim/targets_gold/"+category+".txt");
	
	
	HashSet<String> allTarget = getAllTarget("data/input/drugbank/drugbank_dump.nt");
	HashSet<String> allDrug = getAllDrug("data/input/drugbank/drugbank_dump.nt");
	
	BufferedWriter bw = new BufferedWriter(new FileWriter(new File("D:/data/drug-target/results/tri/connected/4categories_svm/"+category+"/deepwalk_tri_10folderAreas.csv")));
	for (int i = 0; i < 10; i++) {
		
		String datafile = "D:/data/drug-target/input/tri/connected/4categories_svm/"+category+"/tri_folder-" + i + "_data.nt";
		String modelfile = "D:/data/drug-target/models/deepwalk/tri/connected/4categories_svm/" + category + "/deepwalk_tri_" + i
				+ "_100.txt";
		String idxfile = "D:/data/drug-target/models/deepwalk/tri/connected/4categories_svm/" + category + "/deepwalkidx_tri_" + i
				+ "_100.txt";
		String removefile = "D:/data/drug-target/input/tri/connected/4categories_svm/"+category+"/tri_folder-" + i + "_test.nt";
		
		HashMap<String, HashSet<String>> drugToTargetassociations = getDrugTargetAssociations(datafile);
		HashMap<String, HashSet<String>> TargetToDrugassociations = getTargetDrugAssociations(datafile);

		TargetPrediction predict2 = new TargetPrediction(modelfile);
		predict2.feedGold(removefile);

		HashSet<String> queries2 = predict2.getDrugAsQueries();

		HashMap<String, HashMap<String, Double>> results2 = predict2.predictBySimilarDrug(idxfile, queries2,
				drugToTargetassociations, allDrug, validTarget);
		
		HashMap<String, HashSet<String>> all= getAllPotentials( predict2.getGoldmap(), validTarget, 
				drugToTargetassociations);

		EvaluationMetrics metric2 = new EvaluationMetrics(results2, predict2.getGoldmap(),all);
		metric2.getRecallByPercentage(
				"D:/data/drug-target/results/tri/connected/4categories_svm/"+category+"/deepwalk_DBSI_recallByPercentage_"+i+".txt");
		metric2.getRecallByTopN(
				"D:/data/drug-target/results/tri/connected/4categories_svm/"+category+"/deepwalk_DBSI_recallByTopN_"+i+".txt");
		metric2.getPrecisionByPercentage("D:/data/drug-target/results/tri/connected/4categories_svm/"+category+"/deepwalk_DBSI_precisionByPercentage_"+i+".txt");
		metric2.getPrecisionByTopN("D:/data/drug-target/results/tri/connected/4categories_svm/"+category+"/deepwalk_DBSI_precisionByTopN_"+i+".txt");
		metric2.getArea( bw, "D:/data/drug-target/results/tri/connected/4categories_svm/"+category+"/deepwalk_DBSI_area_"+i+".txt");
		
		DrugPrediction predict3 = new DrugPrediction(modelfile);
		predict3.feedGold(removefile);

		HashSet<String> queries3 = predict3.getTargetAsQueries();

		HashMap<String, HashMap<String, Double>> results3 = predict3.predictBySimilarTarget(idxfile, queries3,
				TargetToDrugassociations, validDrug, allTarget);
		
		 all= getAllPotentials( predict3.getGoldmap(), validDrug, 
				 TargetToDrugassociations);
		
		EvaluationMetrics metric3 = new EvaluationMetrics(results3, predict3.getGoldmap(),all);
		
		metric3.getRecallByPercentage(
				"D:/data/drug-target/results/tri/connected/4categories_svm/"+category+"/deepwalk_TBSI_recallByPercentage_"+i+".txt");
		metric3.getRecallByTopN(
				"D:/data/drug-target/results/tri/connected/4categories_svm/"+category+"/deepwalk_TBSI_recallByTopN_"+i+".txt");
		metric3.getPrecisionByPercentage("D:/data/drug-target/results/tri/connected/4categories_svm/"+category+"/deepwalk_TBSI_precisionByPercentage_"+i+".txt");
		metric3.getPrecisionByTopN("D:/data/drug-target/results/tri/connected/4categories_svm/"+category+"/deepwalk_TBSI_precisionByTopN_"+i+".txt");
		metric3.getArea( bw, "D:/data/drug-target/results/tri/connected/4categories_svm/"+category+"/deepwalk_TBSI_area_"+i+".txt");
		
	}
	bw.flush();
	bw.close();
}

public void external_bi(String type) throws Exception {
	
	
	HashSet<String> allTarget = getAllTarget("data/input/drugbank/drugbank_dump.nt");
	HashSet<String> allDrug = getAllDrug("data/input/drugbank/drugbank_dump.nt");
	
	BufferedWriter bw = new BufferedWriter(new FileWriter(new File("D:/data/drug-target/results/bi/external/"+type+"/deepwalk_bi_externalAreas.csv")));
		
		String datafile = "D:/data/drug-target/source/bi.nt";
		String modelfile = "D:/data/drug-target/models/deepwalk/bi/external/deepwalk_external_100.txt";
		String idxfile="D:/data/drug-target/models/deepwalk/bi/external/deepwalkidx_external_100.txt";
		String removefile = "D:/data/drug-target/input/bi/external/"+type+"/external_"+type+".nt";

		HashMap<String, HashSet<String>> drugToTargetassociations = getDrugTargetAssociations(datafile);
		HashMap<String, HashSet<String>> TargetToDrugassociations = getTargetDrugAssociations(datafile);


		TargetPrediction predict2 = new TargetPrediction(modelfile);
		predict2.feedGold(removefile);

		HashSet<String> queries2 = predict2.getDrugAsQueries();

		HashMap<String, HashMap<String, Double>> results2 = predict2.predictBySimilarDrug(idxfile, queries2,
				drugToTargetassociations, allDrug, allTarget);

		EvaluationMetrics metric2 = new EvaluationMetrics(results2, predict2.getGoldmap());
//		metric2.getAreasByTopN(
//				"D:/data/drug-target/results/tri/"+type+"/10folder/deepwalk_DBSI_areasByTopN_" + i  + ".txt");
//		metric2.getAreasByPercentage(
//				"D:/data/drug-target/results/tri/"+type+"/10folder/deepwalk_DBSI_areasByPercentage_" + i  + ".txt");
		metric2.getRecallByPercentage(
				"D:/data/drug-target/results/bi/external/"+type+"/deepwalk_DBSI_recallByPercentage.txt");
		metric2.getRecallByTopN(
				"D:/data/drug-target/results/bi/external/"+type+"/deepwalk_DBSI_recallByTopN.txt");
		metric2.getPrecisionByPercentage("D:/data/drug-target/results/bi/external/"+type+"/deepwalk_DBSI_precisionByPercentage.txt");
		metric2.getPrecisionByTopN("D:/data/drug-target/results/bi/external/"+type+"/deepwalk_DBSI_precisionByTopN.txt");
		metric2.getArea( bw, "D:/data/drug-target/results/bi/external/"+type+"/deepwalk_DBSI_area.txt");

		DrugPrediction predict3 = new DrugPrediction(modelfile);
		predict3.feedGold(removefile);

		HashSet<String> queries3 = predict3.getTargetAsQueries();

		HashMap<String, HashMap<String, Double>> results3 = predict3.predictBySimilarTarget(idxfile, queries3,
				TargetToDrugassociations, allDrug, allTarget);

		EvaluationMetrics metric3 = new EvaluationMetrics(results3, predict3.getGoldmap());
//		metric3.getAreasByTopN(
//				"D:/data/drug-target/results/bi/10folder/deepwalk_TBSI_areasByTopN_" + i  + ".txt");
//		metric3.getAreasByPercentage(
//				"D:/data/drug-target/results/bi/10folder/deepwalk_TBSI_areasByPercentage_" + i  + ".txt");
		metric3.getRecallByPercentage(
				"D:/data/drug-target/results/bi/external/"+type+"/deepwalk_TBSI_recallByPercentage.txt");
		metric3.getRecallByTopN(
				"D:/data/drug-target/results/bi/external/"+type+"/deepwalk_TBSI_recallByTopN.txt");
		metric3.getPrecisionByPercentage("D:/data/drug-target/results/bi/external/"+type+"/deepwalk_TBSI_precisionByPercentage.txt");
		metric3.getPrecisionByTopN("D:/data/drug-target/results/bi/external/"+type+"/deepwalk_TBSI_precisionByTopN.txt");
		metric3.getArea( bw, "D:/data/drug-target/results/bi/external/"+type+"/deepwalk_TBSI_area.txt");

		bw.flush();
		
	bw.close();
	bw.close();
}



public void external_tri(String type) throws Exception {
	
	
	HashSet<String> allTarget = getAllTarget("data/input/drugbank/drugbank_dump.nt");
	HashSet<String> allDrug = getAllDrug("data/input/drugbank/drugbank_dump.nt");
	
	BufferedWriter bw = new BufferedWriter(new FileWriter(new File("D:/data/drug-target/results/tri/external/"+type+"/deepwalk_externalAreas.csv")));
		
	String datafile = "D:/data/drug-target/source/tri.nt";
	String modelfile = "D:/data/drug-target/models/deepwalk/tri/external/deepwalk_external_100.txt";
	String idxfile="D:/data/drug-target/models/deepwalk/tri/external/deepwalkidx_external_100.txt";
	String removefile = "D:/data/drug-target/input/tri/external/"+type+"/external_"+type+".nt";

		HashMap<String, HashSet<String>> drugToTargetassociations = getDrugTargetAssociations(datafile);
		HashMap<String, HashSet<String>> TargetToDrugassociations = getTargetDrugAssociations(datafile);


		TargetPrediction predict2 = new TargetPrediction(modelfile);
		predict2.feedGold(removefile);

		HashSet<String> queries2 = predict2.getDrugAsQueries();

		HashMap<String, HashMap<String, Double>> results2 = predict2.predictBySimilarDrug(idxfile, queries2,
				drugToTargetassociations, allDrug, allTarget);

		EvaluationMetrics metric2 = new EvaluationMetrics(results2, predict2.getGoldmap());
//		metric2.getAreasByTopN(
//				"D:/data/drug-target/results/tri/"+type+"/10folder/deepwalk_DBSI_areasByTopN_" + i  + ".txt");
//		metric2.getAreasByPercentage(
//				"D:/data/drug-target/results/tri/"+type+"/10folder/deepwalk_DBSI_areasByPercentage_" + i  + ".txt");
		metric2.getRecallByPercentage(
				"D:/data/drug-target/results/tri/external/"+type+"/deepwalk_DBSI_recallByPercentage.txt");
		metric2.getRecallByTopN(
				"D:/data/drug-target/results/tri/external/"+type+"/deepwalk_DBSI_recallByTopN.txt");
		metric2.getPrecisionByPercentage("D:/data/drug-target/results/tri/external/"+type+"/deepwalk_DBSI_precisionByPercentage.txt");
		metric2.getPrecisionByTopN("D:/data/drug-target/results/tri/external/"+type+"/deepwalk_DBSI_precisionByTopN.txt");
		metric2.getArea( bw, "D:/data/drug-target/results/tri/external/"+type+"/deepwalk_DBSI_area.txt");

		DrugPrediction predict3 = new DrugPrediction(modelfile);
		predict3.feedGold(removefile);

		HashSet<String> queries3 = predict3.getTargetAsQueries();

		HashMap<String, HashMap<String, Double>> results3 = predict3.predictBySimilarTarget(idxfile, queries3,
				TargetToDrugassociations, allDrug, allTarget);

		EvaluationMetrics metric3 = new EvaluationMetrics(results3, predict3.getGoldmap());
//		metric3.getAreasByTopN(
//				"D:/data/drug-target/results/bi/10folder/deepwalk_TBSI_areasByTopN_" + i  + ".txt");
//		metric3.getAreasByPercentage(
//				"D:/data/drug-target/results/bi/10folder/deepwalk_TBSI_areasByPercentage_" + i  + ".txt");
		metric3.getRecallByPercentage(
				"D:/data/drug-target/results/tri/external/"+type+"/deepwalk_TBSI_recallByPercentage.txt");
		metric3.getRecallByTopN(
				"D:/data/drug-target/results/tri/external/"+type+"/deepwalk_TBSI_recallByTopN.txt");
		metric3.getPrecisionByPercentage("D:/data/drug-target/results/tri/external/"+type+"/deepwalk_TBSI_precisionByPercentage.txt");
		metric3.getPrecisionByTopN("D:/data/drug-target/results/tri/external/"+type+"/deepwalk_TBSI_precisionByTopN.txt");
		metric3.getArea( bw, "D:/data/drug-target/results/tri/external/"+type+"/deepwalk_TBSI_area.txt");

		bw.flush();
		
	bw.close();
	bw.close();
}



public HashMap<String, HashMap<String, Double>> external_similarity_DBSI_tri() throws Exception {
	
	DrugCollector collector=new DrugCollector("data/input/drugbank/drugbank_dump.nt"
			,"D:/data/drug-target/input/tri/external/connected/external_connected_filtered.nt");
	HashSet<String> validDrug=collector.getDrugs();
	HashSet<String>	validTarget=new ProteinCollector().getSourceProteinWithInvalidSeq();
	
	
	HashSet<String> allTarget = getAllTarget("data/input/drugbank/drugbank_dump.nt");
	HashSet<String> allDrug = getAllDrug("data/input/drugbank/drugbank_dump.nt");
	
	BufferedWriter bw = new BufferedWriter(new FileWriter(new File("D:/data/drug-target/results/tri/external/similarity_connected/deepwalk_externalAreas.csv")));
		
	String datafile = "D:/data/drug-target/source/tri.nt";
	String modelfile = "D:/data/drug-target/models/deepwalk/tri/external/deepwalk_external_100.txt";
	String idxfile="D:/data/drug-target/models/deepwalk/tri/external/deepwalkidx_external_100.txt";
	
	String removefile = "D:/data/drug-target/input/tri/external/connected/external_connected_filtered.nt";

		HashMap<String, HashSet<String>> drugToTargetassociations = getDrugTargetAssociations(datafile);
		HashMap<String, HashSet<String>> TargetToDrugassociations = getTargetDrugAssociations(datafile);


		TargetPrediction predict2 = new TargetPrediction(modelfile);
		predict2.feedGold(removefile);

		HashSet<String> queries2 = predict2.getDrugAsQueries();

		HashMap<String, HashMap<String, Double>> results2 = predict2.predictBySimilarDrug(idxfile, queries2,
				drugToTargetassociations, validDrug, validTarget);

		EvaluationMetrics metric2 = new EvaluationMetrics(results2, predict2.getGoldmap());
//		metric2.getAreasByTopN(
//				"D:/data/drug-target/results/tri/"+type+"/10folder/deepwalk_DBSI_areasByTopN_" + i  + ".txt");
//		metric2.getAreasByPercentage(
//				"D:/data/drug-target/results/tri/"+type+"/10folder/deepwalk_DBSI_areasByPercentage_" + i  + ".txt");
		metric2.getRecallByPercentage(
				"D:/data/drug-target/results/tri/external/similarity_connected/deepwalk_DBSI_recallByPercentage.txt");
		metric2.getRecallByTopN(
				"D:/data/drug-target/results/tri/external/similarity_connected/deepwalk_DBSI_recallByTopN.txt");
		metric2.getPrecisionByPercentage("D:/data/drug-target/results/tri/external/similarity_connected/deepwalk_DBSI_precisionByPercentage.txt");
		metric2.getPrecisionByTopN("D:/data/drug-target/results/tri/external/similarity_connected/deepwalk_DBSI_precisionByTopN.txt");
		metric2.getArea( bw, "D:/data/drug-target/results/tri/external/similarity_connected/deepwalk_DBSI_area.txt");
		
	bw.close();
	bw.close();
	return results2;
}


public HashMap<String, HashMap<String, Double>> external_similarity_DBSI_tri(String category) throws Exception {
	
	DrugCollector collector=new DrugCollector("data/input/drugbank/drugbank_dump.nt"
			,"D:/data/drug-target/input/tri/external/connected/external_connected_"+category+".nt");
	
	HashSet<String> validDrug=new ExternalSourceGenerator().getDrugsFromFourCetegories("D:/data/drug-target/models/sim/drugs_gold/"+category+".txt");
	HashSet<String>	validTarget=new ExternalSourceGenerator().getTargetsFromFourCetegories("D:/data/drug-target/models/sim/targets_gold/"+category+".txt");
	
	
	HashSet<String> allTarget = getAllTarget("data/input/drugbank/drugbank_dump.nt");
	HashSet<String> allDrug = getAllDrug("data/input/drugbank/drugbank_dump.nt");
	
	BufferedWriter bw = new BufferedWriter(new FileWriter(new File("D:/data/drug-target/results/tri/external/similarity_connected/deepwalk_"+category+"_externalAreas.csv")));
		
	String datafile = "D:/data/drug-target/source/tri.nt";
	String modelfile = "D:/data/drug-target/models/deepwalk/tri/external/deepwalk_external_100.txt";
	String idxfile="D:/data/drug-target/models/deepwalk/tri/external/deepwalkidx_external_100.txt";
	
	String removefile = "D:/data/drug-target/input/tri/external/connected/external_connected_"+category+".nt";

		HashMap<String, HashSet<String>> drugToTargetassociations = getDrugTargetAssociations(datafile);
		HashMap<String, HashSet<String>> TargetToDrugassociations = getTargetDrugAssociations(datafile);


		TargetPrediction predict2 = new TargetPrediction(modelfile);
		predict2.feedGold(removefile);

		HashSet<String> queries2 = predict2.getDrugAsQueries();

		HashMap<String, HashMap<String, Double>> results2 = predict2.predictBySimilarDrug(idxfile, queries2,
				drugToTargetassociations, allDrug, validTarget);
		
		
		HashMap<String, HashSet<String>> all= getAllPotentials( predict2.getGoldmap(), validTarget, 
				drugToTargetassociations);
		
		
		
		EvaluationMetrics metric2 = new EvaluationMetrics(results2, predict2.getGoldmap(),all);
//		metric2.getAreasByTopN(
//				"D:/data/drug-target/results/tri/"+type+"/10folder/deepwalk_DBSI_areasByTopN_" + i  + ".txt");
//		metric2.getAreasByPercentage(
//				"D:/data/drug-target/results/tri/"+type+"/10folder/deepwalk_DBSI_areasByPercentage_" + i  + ".txt");
		metric2.getRecallByPercentage(
				"D:/data/drug-target/results/tri/external/similarity_connected/deepwalk_DBSI_+"+category+"_recallByPercentage.txt");
		metric2.getRecallByTopN(
				"D:/data/drug-target/results/tri/external/similarity_connected/deepwalk_DBSI_"+category+"_recallByTopN.txt");
		metric2.getPrecisionByPercentage("D:/data/drug-target/results/tri/external/similarity_connected/deepwalk_DBSI_"+category+"_precisionByPercentage.txt");
		metric2.getPrecisionByTopN("D:/data/drug-target/results/tri/external/similarity_connected/deepwalk_DBSI_"+category+"_precisionByTopN.txt");
		metric2.getArea( bw, "D:/data/drug-target/results/tri/external/similarity_connected/deepwalk_DBSI"+category+"_area.txt");
		
	bw.close();
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
	HashSet<String>	validTarget=new ProteinCollector().getSourceProteinWithInvalidSeq();
	
	HashSet<String> allTarget = getAllTarget("data/input/drugbank/drugbank_dump.nt");
	HashSet<String> allDrug = getAllDrug("data/input/drugbank/drugbank_dump.nt");
	
	BufferedWriter bw = new BufferedWriter(new FileWriter(new File("D:/data/drug-target/results/tri/external/similarity_connected/deepwalk_externalAreas.csv")));
		
	String datafile = "D:/data/drug-target/source/tri.nt";
	String modelfile = "D:/data/drug-target/models/deepwalk/tri/external/deepwalk_external_100.txt";
	String idxfile="D:/data/drug-target/models/deepwalk/tri/external/deepwalkidx_external_100.txt";
	String removefile = "D:/data/drug-target/input/tri/external/connected/external_connected_filtered.nt";

		HashMap<String, HashSet<String>> drugToTargetassociations = getDrugTargetAssociations(datafile);
		HashMap<String, HashSet<String>> TargetToDrugassociations = getTargetDrugAssociations(datafile);

		DrugPrediction predict3 = new DrugPrediction(modelfile);
		predict3.feedGold(removefile);

		HashSet<String> queries3 = predict3.getTargetAsQueries();

		HashMap<String, HashMap<String, Double>> results3 = predict3.predictBySimilarTarget(idxfile, queries3,
				TargetToDrugassociations, validDrug, validTarget);

		EvaluationMetrics metric3 = new EvaluationMetrics(results3, predict3.getGoldmap());
//		metric3.getAreasByTopN(
//				"D:/data/drug-target/results/bi/10folder/deepwalk_TBSI_areasByTopN_" + i  + ".txt");
//		metric3.getAreasByPercentage(
//				"D:/data/drug-target/results/bi/10folder/deepwalk_TBSI_areasByPercentage_" + i  + ".txt");
		metric3.getRecallByPercentage(
				"D:/data/drug-target/results/tri/external/similarity_connected/deepwalk_TBSI_recallByPercentage.txt");
		metric3.getRecallByTopN(
				"D:/data/drug-target/results/tri/external/similarity_connected/deepwalk_TBSI_recallByTopN.txt");
		metric3.getPrecisionByPercentage("D:/data/drug-target/results/tri/external/similarity_connected/deepwalk_TBSI_precisionByPercentage.txt");
		metric3.getPrecisionByTopN("D:/data/drug-target/results/tri/external/similarity_connected/deepwalk_TBSI_precisionByTopN.txt");
		metric3.getArea( bw, "D:/data/drug-target/results/tri/external/similarity_connected/deepwalk_TBSI_area.txt");

		bw.close();
		bw.close();
		return results3;
}


public HashMap<String, HashMap<String, Double>> external_similarity_TBSI_tri(String category) throws Exception {
	
	HashSet<String> validDrug=new ExternalSourceGenerator().getDrugsFromFourCetegories("D:/data/drug-target/models/sim/drugs_gold/"+category+".txt");
	HashSet<String>	validTarget=new ExternalSourceGenerator().getTargetsFromFourCetegories("D:/data/drug-target/models/sim/targets_gold/"+category+".txt");
	
	HashSet<String> allTarget = getAllTarget("data/input/drugbank/drugbank_dump.nt");
	HashSet<String> allDrug = getAllDrug("data/input/drugbank/drugbank_dump.nt");
	
	BufferedWriter bw = new BufferedWriter(new FileWriter(new File("D:/data/drug-target/results/tri/external/similarity_connected/deepwalk_"+category+"_externalAreas.csv")));
		
	String datafile = "D:/data/drug-target/source/tri.nt";
	String modelfile = "D:/data/drug-target/models/deepwalk/tri/external/deepwalk_external_100.txt";
	String idxfile="D:/data/drug-target/models/deepwalk/tri/external/deepwalkidx_external_100.txt";
	String removefile = "D:/data/drug-target/input/tri/external/connected/external_connected_"+category+".nt";

		HashMap<String, HashSet<String>> drugToTargetassociations = getDrugTargetAssociations(datafile);
		HashMap<String, HashSet<String>> TargetToDrugassociations = getTargetDrugAssociations(datafile);

		DrugPrediction predict3 = new DrugPrediction(modelfile);
		predict3.feedGold(removefile);

		HashSet<String> queries3 = predict3.getTargetAsQueries();

		HashMap<String, HashMap<String, Double>> results3 = predict3.predictBySimilarTarget(idxfile, queries3,
				TargetToDrugassociations, validDrug, allTarget);
		
		HashMap<String, HashSet<String>> all= getAllPotentials( predict3.getGoldmap(), validDrug,
				TargetToDrugassociations);
		
		EvaluationMetrics metric3 = new EvaluationMetrics(results3, predict3.getGoldmap(),all);
//		metric3.getAreasByTopN(
//				"D:/data/drug-target/results/bi/10folder/deepwalk_TBSI_areasByTopN_" + i  + ".txt");
//		metric3.getAreasByPercentage(
//				"D:/data/drug-target/results/bi/10folder/deepwalk_TBSI_areasByPercentage_" + i  + ".txt");
		metric3.getRecallByPercentage(
				"D:/data/drug-target/results/tri/external/similarity_connected/deepwalk_TBSI_"+category+"_recallByPercentage.txt");
		metric3.getRecallByTopN(
				"D:/data/drug-target/results/tri/external/similarity_connected/deepwalk_TBSI_"+category+"_recallByTopN.txt");
		metric3.getPrecisionByPercentage("D:/data/drug-target/results/tri/external/similarity_connected/deepwalk_TBSI_"+category+"_precisionByPercentage.txt");
		metric3.getPrecisionByTopN("D:/data/drug-target/results/tri/external/similarity_connected/deepwalk_TBSI_"+category+"_precisionByTopN.txt");
		metric3.getArea( bw, "D:/data/drug-target/results/tri/external/similarity_connected/deepwalk_TBSI_"+category+"_area.txt");
		bw.flush();
		bw.close();
		return results3;
}


	public void tenfolder_bi() throws Exception {
		
		
		HashSet<String> allTarget = getAllTarget("data/input/drugbank/drugbank_dump.nt");
		HashSet<String> allDrug = getAllDrug("data/input/drugbank/drugbank_dump.nt");
		
		BufferedWriter bw = new BufferedWriter(new FileWriter(new File("D:/data/drug-target/results/bi/10folder/deepwalk_bi_10folderAreas.csv")));
		for (int i = 0; i < 10; i++) {
			
			
			String datafile = "D:/data/drug-target/input/bi/10folder/bi_folder-" + i + "_data.nt";
			String modelfile = "D:/data/drug-target/models/deepwalk/bi/10folder/deepwalk_bi_" + i + "_100.txt";
			String idxfile = "D:/data/drug-target/models/deepwalk/bi/10folder/deepwalkidx_bi_" + i  + "_100.txt";
			String removefile = "D:/data/drug-target/input/bi/10folder/bi_folder-" + i + "_test.nt";

			HashMap<String, HashSet<String>> drugToTargetassociations = getDrugTargetAssociations(datafile);
			HashMap<String, HashSet<String>> TargetToDrugassociations = getTargetDrugAssociations(datafile);

			// String datafile="data/input/drugCloud/data_bi_"+i*100+".nt";
			// String
			// modelfile="data/input/line/models/line_bi_"+i*100+"_500.txt";
			// String removefile="data/input/drugCloud/removed_bi_"+i*100+".nt";

//			TargetPrediction predict1 = new TargetPrediction(modelfile);
//			predict1.feedGold(removefile);
//
//			HashMap<String, HashSet<String>> queries1 = predict1.getTargetAsQueries(datafile);
//
//			HashMap<String, HashMap<String, Double>> results1 = predict1.predictBySimilarTarget(idxfile, queries1,
//					drugToTargetassociations, allTarget);
//
//			EvaluationMetrics metric1 = new EvaluationMetrics(results1, predict1.getGoldmap());
//			metric1.getAreasByTopN(
//					"data/output/experiment0630/deepwalk_predictTargetByTarget_getAreasByTopN_" + i * 100 + ".txt");
//			metric1.getAreasByPercentage(
//					"data/output/experiment0630/deepwalk_predictTargetByTarget_getAreasByPercentage_" + i * 100
//							+ ".txt");
//			metric1.getRecallByPercentage(
//					"data/output/experiment0630/deepwalk_predictTargetByTarget_getRecallByPercentage_" + i * 100
//							+ ".txt");
//			metric1.getRecallByTopN(
//					"data/output/experiment0630/deepwalk_predictTargetByTarget_getRecallByTopN_" + i * 100 + ".txt");
//			metric1.getArea("data/output/experiment0630/deepwalk_predictTargetByTarget_getArea_" + i * 100 + ".txt");

			// drug-drug similar find test

			TargetPrediction predict2 = new TargetPrediction(modelfile);
			predict2.feedGold(removefile);

			HashSet<String> queries2 = predict2.getDrugAsQueries();

			HashMap<String, HashMap<String, Double>> results2 = predict2.predictBySimilarDrug(idxfile, queries2,
					drugToTargetassociations, allDrug, allTarget);

			EvaluationMetrics metric2 = new EvaluationMetrics(results2, predict2.getGoldmap());
			metric2.getAreasByTopN(
					"D:/data/drug-target/results/bi/10folder/deepwalk_DBSI_areasByTopN_" + i  + ".txt");
			metric2.getAreasByPercentage(
					"D:/data/drug-target/results/bi/10folder/deepwalk_DBSI_areasByPercentage_" + i  + ".txt");
			metric2.getRecallByPercentage(
					"D:/data/drug-target/results/bi/10folder/deepwalk_DBSI_recallByPercentage_" + i 
							+ ".txt");
			metric2.getRecallByTopN(
					"D:/data/drug-target/results/bi/10folder/deepwalk_DBSI_recallByTopN_" + i + ".txt");
			metric2.getPrecisionByPercentage("D:/data/drug-target/results/bi/10folder/deepwalk_DBSI_precisionByPercentage_" + i 
					+ ".txt");
			metric2.getPrecisionByTopN("D:/data/drug-target/results/bi/10folder/deepwalk_DBSI_precisionByTopN_" + i 
					+ ".txt");
			metric2.getArea( bw, "D:/data/drug-target/results/bi/10folder/deepwalk_DBSI_area_" + i  + ".txt");

			DrugPrediction predict3 = new DrugPrediction(modelfile);
			predict3.feedGold(removefile);

			HashSet<String> queries3 = predict3.getTargetAsQueries();

			HashMap<String, HashMap<String, Double>> results3 = predict3.predictBySimilarTarget(idxfile, queries3,
					TargetToDrugassociations, allDrug, allTarget);

			EvaluationMetrics metric3 = new EvaluationMetrics(results3, predict3.getGoldmap());
			metric3.getAreasByTopN(
					"D:/data/drug-target/results/bi/10folder/deepwalk_TBSI_areasByTopN_" + i  + ".txt");
			metric3.getAreasByPercentage(
					"D:/data/drug-target/results/bi/10folder/deepwalk_TBSI_areasByPercentage_" + i  + ".txt");
			metric3.getRecallByPercentage(
					"D:/data/drug-target/results/bi/10folder/deepwalk_TBSI_recallByPercentage_" + i 
							+ ".txt");
			metric3.getRecallByTopN(
					"D:/data/drug-target/results/bi/10folder/deepwalk_TBSI_recallByTopN_" + i * 100 + ".txt");
			metric3.getPrecisionByPercentage("D:/data/drug-target/results/bi/10folder/deepwalk_TBSI_precisionByPercentage_" + i 
					+ ".txt");
			metric3.getPrecisionByTopN("D:/data/drug-target/results/bi/10folder/deepwalk_TBSI_precisionByTopN_" + i 
					+ ".txt");
			metric3.getArea( bw, "D:/data/drug-target/results/bi/10folder/deepwalk_TBSI_area_" + i  + ".txt");

			// drug-drug similar find test

//			DrugPrediction predict4 = new DrugPrediction(modelfile);
//			predict4.feedGold(removefile);
//
//			HashMap<String, HashSet<String>> queries4 = predict4.getDrugAsQueries(datafile);
//
//			HashMap<String, HashMap<String, Double>> results4 = predict4.predictBySimilarDrug(idxfile, queries4,
//					TargetToDrugassociations, allDrug);
//
//			EvaluationMetrics metric4 = new EvaluationMetrics(results4, predict4.getGoldmap());
//			metric4.getAreasByTopN(
//					"data/output/experiment0630/deepwalk_predictDrugByDrug_getAreasByTopN_" + i * 100 + ".txt");
//			metric4.getAreasByPercentage(
//					"data/output/experiment0630/deepwalk_predictDrugByDrug_getAreasByPercentage_" + i * 100 + ".txt");
//			metric4.getRecallByPercentage(
//					"data/output/experiment0630/deepwalk_predictDrugByDrug_getRecallByPercentage_" + i * 100 + ".txt");
//			metric4.getRecallByTopN(
//					"data/output/experiment0630/deepwalk_predictDrugByDrug_getRecallByTopN_" + i * 100 + ".txt");
//			metric4.getArea("data/output/experiment0630/deepwalk_predictDrugByDrug_getArea_" + i * 100 + ".txt");
			bw.flush();
			
		}
		bw.close();
		bw.close();
	}

	
public void monteCarlo_bi() throws Exception {
		
		
		HashSet<String> allTarget = getAllTarget("data/input/drugbank/drugbank_dump.nt");
		HashSet<String> allDrug = getAllDrug("data/input/drugbank/drugbank_dump.nt");
		
		BufferedWriter bw = new BufferedWriter(new FileWriter(new File("D:/data/drug-target/results/bi/monteCarlo/deepwalk_bi_monteCarloAreas.csv")));
		for (int i = 1; i < 11; i++) {

			String datafile = "D:/data/drug-target/input/bi/monteCarlo/bi_" + i * 100 + "_data.nt";
			String modelfile = "D:/data/drug-target/models/deepwalk/bi/monteCarlo/deepwalk_bi_" + i * 100 + "_100.txt";
			String idxfile = "D:/data/drug-target/models/deepwalk/bi/monteCarlo/deepwalkidx_bi_" + i * 100 + "_100.txt";
			String removefile = "D:/data/drug-target/input/bi/monteCarlo/bi_" + i * 100 + "_test.nt";

			HashMap<String, HashSet<String>> drugToTargetassociations = getDrugTargetAssociations(datafile);
			HashMap<String, HashSet<String>> TargetToDrugassociations = getTargetDrugAssociations(datafile);


			TargetPrediction predict2 = new TargetPrediction(modelfile);
			predict2.feedGold(removefile);

			HashSet<String> queries2 = predict2.getDrugAsQueries();

			HashMap<String, HashMap<String, Double>> results2 = predict2.predictBySimilarDrug(idxfile, queries2,
					drugToTargetassociations, allDrug, allTarget);

			EvaluationMetrics metric2 = new EvaluationMetrics(results2, predict2.getGoldmap());
			metric2.getAreasByTopN(
					"D:/data/drug-target/results/bi/monteCarlo/deepwalk_DBSI_areasByTopN_" + i * 100 + ".txt");
			metric2.getAreasByPercentage(
					"D:/data/drug-target/results/bi/monteCarlo/deepwalk_DBSI_areasByPercentage_" + i * 100 + ".txt");
			metric2.getRecallByPercentage(
					"D:/data/drug-target/results/bi/monteCarlo/deepwalk_DBSI_recallByPercentage_" + i * 100
							+ ".txt");
			metric2.getRecallByTopN(
					"D:/data/drug-target/results/bi/monteCarlo/deepwalk_DBSI_recallByTopN_" + i * 100 + ".txt");
			metric2.getPrecisionByPercentage("D:/data/drug-target/results/bi/monteCarlo/deepwalk_DBSI_precisionByPercentage_" + i * 100
					+ ".txt");
			metric2.getPrecisionByTopN("D:/data/drug-target/results/bi/monteCarlo/deepwalk_DBSI_precisionByTopN_" + i * 100
					+ ".txt");
			metric2.getArea( bw, "D:/data/drug-target/results/bi/monteCarlo/deepwalk_DBSI_area_" + i * 100 + ".txt");

			DrugPrediction predict3 = new DrugPrediction(modelfile);
			predict3.feedGold(removefile);

			HashSet<String> queries3 = predict3.getTargetAsQueries();

			HashMap<String, HashMap<String, Double>> results3 = predict3.predictBySimilarTarget(idxfile, queries3,
					TargetToDrugassociations, allDrug, allTarget);

			EvaluationMetrics metric3 = new EvaluationMetrics(results3, predict3.getGoldmap());
			metric3.getAreasByTopN(
					"D:/data/drug-target/results/bi/monteCarlo/deepwalk_TBSI_areasByTopN_" + i * 100 + ".txt");
			metric3.getAreasByPercentage(
					"D:/data/drug-target/results/bi/monteCarlo/deepwalk_TBSI_areasByPercentage_" + i * 100 + ".txt");
			metric3.getRecallByPercentage(
					"D:/data/drug-target/results/bi/monteCarlo/deepwalk_TBSI_recallByPercentage_" + i * 100
							+ ".txt");
			metric3.getRecallByTopN(
					"D:/data/drug-target/results/bi/monteCarlo/deepwalk_TBSI_recallByTopN_" + i * 100 + ".txt");
			metric3.getPrecisionByPercentage("D:/data/drug-target/results/bi/monteCarlo/deepwalk_TBSI_precisionByPercentage_" + i * 100
					+ ".txt");
			metric3.getPrecisionByTopN("D:/data/drug-target/results/bi/monteCarlo/deepwalk_TBSI_precisionByTopN_" + i * 100
					+ ".txt");
			metric3.getArea( bw, "D:/data/drug-target/results/bi/monteCarlo/deepwalk_TBSI_area_" + i * 100 + ".txt");

			bw.flush();
			
		}
		bw.flush();
		bw.close();
	}


	public void internal_tri() throws Exception {

		HashSet<String> allTarget = getAllTarget("data/input/drugbank/drugbank_dump.nt");
		HashSet<String> allDrug = getAllDrug("data/input/drugbank/drugbank_dump.nt");

		for (int i = 1; i < 11; i++) {

			String datafile = "data/input/drugCloud/data_tri_" + i * 100 + ".nt";
			String modelfile = "data/input/drugCloud/deepwalk_tri_" + i + 100 + "_100.txt";
			String idxfile = "data/input/drugCloud/deepwalkidx_tri_" + i + 100 + "_100.txt";
			String removefile = "data/input/drugCloud/removed_bi_" + i * 100 + ".nt";

			HashMap<String, HashSet<String>> drugToTargetassociations = getDrugTargetAssociations(datafile);
			HashMap<String, HashSet<String>> TargetToDrugassociations = getTargetDrugAssociations(datafile);

			// String datafile="data/input/drugCloud/data_bi_"+i*100+".nt";
			// String
			// modelfile="data/input/line/models/line_bi_"+i*100+"_500.txt";
			// String removefile="data/input/drugCloud/removed_bi_"+i*100+".nt";

			TargetPrediction predict1 = new TargetPrediction(modelfile);
			predict1.feedGold(removefile);

			HashMap<String, HashSet<String>> queries1 = predict1.getTargetAsQueries(datafile);

			HashMap<String, HashMap<String, Double>> results1 = predict1.predictBySimilarTarget(idxfile, queries1,
					drugToTargetassociations, allTarget);

			EvaluationMetrics metric1 = new EvaluationMetrics(results1, predict1.getGoldmap());
			metric1.getAreasByTopN(
					"data/output/experiment_tri/deepwalk_predictTargetByTarget_getAreasByTopN_" + i * 100 + ".txt");
			metric1.getAreasByPercentage(
					"data/output/experiment_tri/deepwalk_predictTargetByTarget_getAreasByPercentage_" + i * 100
							+ ".txt");
			metric1.getRecallByPercentage(
					"data/output/experiment_tri/deepwalk_predictTargetByTarget_getRecallByPercentage_" + i * 100
							+ ".txt");
			metric1.getRecallByTopN(
					"data/output/experiment_tri/deepwalk_predictTargetByTarget_getRecallByTopN_" + i * 100 + ".txt");
			metric1.getArea("data/output/experiment_tri/deepwalk_predictTargetByTarget_getArea_" + i * 100 + ".txt");

			// drug-drug similar find test

			TargetPrediction predict2 = new TargetPrediction(modelfile);
			predict2.feedGold(removefile);

			HashSet<String> queries2 = predict2.getDrugAsQueries();

			HashMap<String, HashMap<String, Double>> results2 = predict2.predictBySimilarDrug(idxfile, queries2,
					drugToTargetassociations, allDrug, allTarget);

			EvaluationMetrics metric2 = new EvaluationMetrics(results2, predict2.getGoldmap());
			metric2.getAreasByTopN(
					"data/output/experiment_tri/deepwalk_predictTargetByDrug_getAreasByTopN_" + i * 100 + ".txt");
			metric2.getAreasByPercentage(
					"data/output/experiment_tri/deepwalk_predictTargetByDrug_getAreasByPercentage_" + i * 100 + ".txt");
			metric2.getRecallByPercentage(
					"data/output/experiment_tri/deepwalk_predictTargetByDrug_getRecallByPercentage_" + i * 100
							+ ".txt");
			metric2.getRecallByTopN(
					"data/output/experiment_tri/deepwalk_predictTargetByDrug_getRecallByTopN_" + i * 100 + ".txt");
			metric2.getArea("data/output/experiment_tri/deepwalk_predictTargetByDrug_getArea_" + i * 100 + ".txt");

			DrugPrediction predict3 = new DrugPrediction(modelfile);
			predict3.feedGold(removefile);

			HashSet<String> queries3 = predict3.getTargetAsQueries();

			HashMap<String, HashMap<String, Double>> results3 = predict3.predictBySimilarTarget(idxfile, queries3,
					TargetToDrugassociations, allDrug, allTarget);

			EvaluationMetrics metric3 = new EvaluationMetrics(results3, predict3.getGoldmap());
			metric3.getAreasByTopN(
					"data/output/experiment_tri/deepwalk_predictDrugByTarget_getAreasByTopN_" + i * 100 + ".txt");
			metric3.getAreasByPercentage(
					"data/output/experiment_tri/deepwalk_predictDrugByTarget_getAreasByPercentage_" + i * 100 + ".txt");
			metric3.getRecallByPercentage(
					"data/output/experiment_tri/deepwalk_predictDrugByTarget_getRecallByPercentage_" + i * 100
							+ ".txt");
			metric3.getRecallByTopN(
					"data/output/experiment_tri/deepwalk_predictDrugByTarget_getRecallByTopN_" + i * 100 + ".txt");
			metric3.getArea("data/output/experiment_tri/deepwalk_predictDrugByTarget_getArea_" + i * 100 + ".txt");

			// drug-drug similar find test

			DrugPrediction predict4 = new DrugPrediction(modelfile);
			predict4.feedGold(removefile);

			HashMap<String, HashSet<String>> queries4 = predict4.getDrugAsQueries(datafile);

			HashMap<String, HashMap<String, Double>> results4 = predict4.predictBySimilarDrug(idxfile, queries4,
					TargetToDrugassociations, allDrug);

			EvaluationMetrics metric4 = new EvaluationMetrics(results4, predict4.getGoldmap());
			metric4.getAreasByTopN(
					"data/output/experiment_tri/deepwalk_predictDrugByDrug_getAreasByTopN_" + i * 100 + ".txt");
			metric4.getAreasByPercentage(
					"data/output/experiment_tri/deepwalk_predictDrugByDrug_getAreasByPercentage_" + i * 100 + ".txt");
			metric4.getRecallByPercentage(
					"data/output/experiment_tri/deepwalk_predictDrugByDrug_getRecallByPercentage_" + i * 100 + ".txt");
			metric4.getRecallByTopN(
					"data/output/experiment_tri/deepwalk_predictDrugByDrug_getRecallByTopN_" + i * 100 + ".txt");
			metric4.getArea("data/output/experiment_tri/deepwalk_predictDrugByDrug_getArea_" + i * 100 + ".txt");
		}

	}

	public void external_bi() throws Exception {

		HashSet<String> allTarget = getAllTarget("data/input/drugbank/drugbank_dump.nt");
		HashSet<String> allDrug = getAllDrug("data/input/drugbank/drugbank_dump.nt");

		String datafile = "data/input/drugCloud/data_bi.nt";
		String modelfile = "data/input/drugCloud/deepwalk_bi_100.txt";
		String idxfile = "data/input/drugCloud/deepwalkidx_bi_100.txt";
		String externalfile = "data/input/drugCloud/drugbank_predict_4.nt";

		HashMap<String, HashSet<String>> drugToTargetassociations = getDrugTargetAssociations(datafile);
		HashMap<String, HashSet<String>> TargetToDrugassociations = getTargetDrugAssociations(datafile);

		// String datafile="data/input/drugCloud/data_bi_"+i*100+".nt";
		// String modelfile="data/input/line/models/line_bi_"+i*100+"_500.txt";
		// String removefile="data/input/drugCloud/removed_bi_"+i*100+".nt";

		TargetPrediction predict1 = new TargetPrediction(modelfile);
		predict1.feedGold(externalfile);

		HashMap<String, HashSet<String>> queries1 = predict1.getTargetAsQueries(datafile);

		HashMap<String, HashMap<String, Double>> results1 = predict1.predictBySimilarTarget(idxfile, queries1,
				drugToTargetassociations, allTarget);

		EvaluationMetrics metric1 = new EvaluationMetrics(results1, predict1.getGoldmap());
		metric1.getAreasByTopN(
				"data/output/experiment0630/deepwalk_predictTargetByTarget_getAreasByTopN_" + "external" + ".txt");
		metric1.getAreasByPercentage("data/output/experiment0630/deepwalk_predictTargetByTarget_getAreasByPercentage_"
				+ "external" + ".txt");
		metric1.getRecallByPercentage("data/output/experiment0630/deepwalk_predictTargetByTarget_getRecallByPercentage_"
				+ "external" + ".txt");
		metric1.getRecallByTopN(
				"data/output/experiment0630/deepwalk_predictTargetByTarget_getRecallByTopN_" + "external" + ".txt");
		metric1.getArea("data/output/experiment0630/deepwalk_predictTargetByTarget_getArea_" + "external" + ".txt");

		// drug-drug similar find test

		TargetPrediction predict2 = new TargetPrediction(modelfile);
		predict2.feedGold(externalfile);

		HashSet<String> queries2 = predict2.getDrugAsQueries();

		HashMap<String, HashMap<String, Double>> results2 = predict2.predictBySimilarDrug(idxfile, queries2,
				drugToTargetassociations, allDrug, allTarget);

		EvaluationMetrics metric2 = new EvaluationMetrics(results2, predict2.getGoldmap());
		metric2.getAreasByTopN(
				"data/output/experiment0630/deepwalk_predictTargetByDrug_getAreasByTopN_" + "external" + ".txt");
		metric2.getAreasByPercentage(
				"data/output/experiment0630/deepwalk_predictTargetByDrug_getAreasByPercentage_" + "external" + ".txt");
		metric2.getRecallByPercentage(
				"data/output/experiment0630/deepwalk_predictTargetByDrug_getRecallByPercentage_" + "external" + ".txt");
		metric2.getRecallByTopN(
				"data/output/experiment0630/deepwalk_predictTargetByDrug_getRecallByTopN_" + "external" + ".txt");
		metric2.getArea("data/output/experiment0630/deepwalk_predictTargetByDrug_getArea_" + "external" + ".txt");

		DrugPrediction predict3 = new DrugPrediction(modelfile);
		predict3.feedGold(externalfile);

		HashSet<String> queries3 = predict3.getTargetAsQueries();

		HashMap<String, HashMap<String, Double>> results3 = predict3.predictBySimilarTarget(idxfile, queries3,
				TargetToDrugassociations, allDrug, allTarget);

		EvaluationMetrics metric3 = new EvaluationMetrics(results3, predict3.getGoldmap());
		metric3.getAreasByTopN(
				"data/output/experiment0630/deepwalk_predictDrugByTarget_getAreasByTopN_" + "external" + ".txt");
		metric3.getAreasByPercentage(
				"data/output/experiment0630/deepwalk_predictDrugByTarget_getAreasByPercentage_" + "external" + ".txt");
		metric3.getRecallByPercentage(
				"data/output/experiment0630/deepwalk_predictDrugByTarget_getRecallByPercentage_" + "external" + ".txt");
		metric3.getRecallByTopN(
				"data/output/experiment0630/deepwalk_predictDrugByTarget_getRecallByTopN_" + "external" + ".txt");
		metric3.getArea("data/output/experiment0630/deepwalk_predictDrugByTarget_getArea_" + "external" + ".txt");

		// drug-drug similar find test

		DrugPrediction predict4 = new DrugPrediction(modelfile);
		predict4.feedGold(externalfile);

		HashMap<String, HashSet<String>> queries4 = predict4.getDrugAsQueries(datafile);

		HashMap<String, HashMap<String, Double>> results4 = predict4.predictBySimilarDrug(idxfile, queries4,
				TargetToDrugassociations, allDrug);

		EvaluationMetrics metric4 = new EvaluationMetrics(results4, predict4.getGoldmap());
		metric4.getAreasByTopN(
				"data/output/experiment0630/deepwalk_predictDrugByDrug_getAreasByTopN_" + "external" + ".txt");
		metric4.getAreasByPercentage(
				"data/output/experiment0630/deepwalk_predictDrugByDrug_getAreasByPercentage_" + "external" + ".txt");
		metric4.getRecallByPercentage(
				"data/output/experiment0630/deepwalk_predictDrugByDrug_getRecallByPercentage_" + "external" + ".txt");
		metric4.getRecallByTopN(
				"data/output/experiment0630/deepwalk_predictDrugByDrug_getRecallByTopN_" + "external" + ".txt");
		metric4.getArea("data/output/experiment0630/deepwalk_predictDrugByDrug_getArea_" + "external" + ".txt");

	}

	public void external_tri() throws Exception {

		HashSet<String> allTarget = getAllTarget("data/input/drugbank/drugbank_dump.nt");
		HashSet<String> allDrug = getAllDrug("data/input/drugbank/drugbank_dump.nt");

		String datafile = "data/input/drugCloud/data_tri.nt";
		String modelfile = "data/input/drugCloud/deepwalk_tri_100.txt";
		String idxfile = "data/input/drugCloud/deepwalkidx_tri_100.txt";
		String externalfile = "data/input/drugCloud/drugbank_predict_4.nt";

		HashMap<String, HashSet<String>> drugToTargetassociations = getDrugTargetAssociations(datafile);
		HashMap<String, HashSet<String>> TargetToDrugassociations = getTargetDrugAssociations(datafile);

		// String datafile="data/input/drugCloud/data_bi_"+i*100+".nt";
		// String modelfile="data/input/line/models/line_bi_"+i*100+"_500.txt";
		// String removefile="data/input/drugCloud/removed_bi_"+i*100+".nt";

		TargetPrediction predict1 = new TargetPrediction(modelfile);
		predict1.feedGold(externalfile);

		HashMap<String, HashSet<String>> queries1 = predict1.getTargetAsQueries(datafile);

		HashMap<String, HashMap<String, Double>> results1 = predict1.predictBySimilarTarget(idxfile, queries1,
				drugToTargetassociations, allTarget);

		EvaluationMetrics metric1 = new EvaluationMetrics(results1, predict1.getGoldmap());
		metric1.getAreasByTopN(
				"data/output/experiment_tri/deepwalk_predictTargetByTarget_getAreasByTopN_" + "external" + ".txt");
		metric1.getAreasByPercentage("data/output/experiment_tri/deepwalk_predictTargetByTarget_getAreasByPercentage_"
				+ "external" + ".txt");
		metric1.getRecallByPercentage("data/output/experiment_tri/deepwalk_predictTargetByTarget_getRecallByPercentage_"
				+ "external" + ".txt");
		metric1.getRecallByTopN(
				"data/output/experiment_tri/deepwalk_predictTargetByTarget_getRecallByTopN_" + "external" + ".txt");
		metric1.getArea("data/output/experiment_tri/deepwalk_predictTargetByTarget_getArea_" + "external" + ".txt");

		// drug-drug similar find test

		TargetPrediction predict2 = new TargetPrediction(modelfile);
		predict2.feedGold(externalfile);

		HashSet<String> queries2 = predict2.getDrugAsQueries();

		HashMap<String, HashMap<String, Double>> results2 = predict2.predictBySimilarDrug(idxfile, queries2,
				drugToTargetassociations, allDrug, allTarget);

		EvaluationMetrics metric2 = new EvaluationMetrics(results2, predict2.getGoldmap());
		metric2.getAreasByTopN(
				"data/output/experiment_tri/deepwalk_predictTargetByDrug_getAreasByTopN_" + "external" + ".txt");
		metric2.getAreasByPercentage(
				"data/output/experiment_tri/deepwalk_predictTargetByDrug_getAreasByPercentage_" + "external" + ".txt");
		metric2.getRecallByPercentage(
				"data/output/experiment_tri/deepwalk_predictTargetByDrug_getRecallByPercentage_" + "external" + ".txt");
		metric2.getRecallByTopN(
				"data/output/experiment_tri/deepwalk_predictTargetByDrug_getRecallByTopN_" + "external" + ".txt");
		metric2.getArea("data/output/experiment_tri/deepwalk_predictTargetByDrug_getArea_" + "external" + ".txt");

		DrugPrediction predict3 = new DrugPrediction(modelfile);
		predict3.feedGold(externalfile);

		HashSet<String> queries3 = predict3.getTargetAsQueries();

		HashMap<String, HashMap<String, Double>> results3 = predict3.predictBySimilarTarget(idxfile, queries3,
				TargetToDrugassociations, allDrug, allTarget);

		EvaluationMetrics metric3 = new EvaluationMetrics(results3, predict3.getGoldmap());
		metric3.getAreasByTopN(
				"data/output/experiment_tri/deepwalk_predictDrugByTarget_getAreasByTopN_" + "external" + ".txt");
		metric3.getAreasByPercentage(
				"data/output/experiment_tri/deepwalk_predictDrugByTarget_getAreasByPercentage_" + "external" + ".txt");
		metric3.getRecallByPercentage(
				"data/output/experiment_tri/deepwalk_predictDrugByTarget_getRecallByPercentage_" + "external" + ".txt");
		metric3.getRecallByTopN(
				"data/output/experiment_tri/deepwalk_predictDrugByTarget_getRecallByTopN_" + "external" + ".txt");
		metric3.getArea("data/output/experiment_tri/deepwalk_predictDrugByTarget_getArea_" + "external" + ".txt");

		// drug-drug similar find test

		DrugPrediction predict4 = new DrugPrediction(modelfile);
		predict4.feedGold(externalfile);

		HashMap<String, HashSet<String>> queries4 = predict4.getDrugAsQueries(datafile);

		HashMap<String, HashMap<String, Double>> results4 = predict4.predictBySimilarDrug(idxfile, queries4,
				TargetToDrugassociations, allDrug);

		EvaluationMetrics metric4 = new EvaluationMetrics(results4, predict4.getGoldmap());
		metric4.getAreasByTopN(
				"data/output/experiment_tri/deepwalk_predictDrugByDrug_getAreasByTopN_" + "external" + ".txt");
		metric4.getAreasByPercentage(
				"data/output/experiment_tri/deepwalk_predictDrugByDrug_getAreasByPercentage_" + "external" + ".txt");
		metric4.getRecallByPercentage(
				"data/output/experiment_tri/deepwalk_predictDrugByDrug_getRecallByPercentage_" + "external" + ".txt");
		metric4.getRecallByTopN(
				"data/output/experiment_tri/deepwalk_predictDrugByDrug_getRecallByTopN_" + "external" + ".txt");
		metric4.getArea("data/output/experiment_tri/deepwalk_predictDrugByDrug_getArea_" + "external" + ".txt");

	}

	public void find_external_tri() throws Exception {

		HashSet<String> allTarget = getAllTarget("data/input/drugbank/drugbank_dump.nt");
		HashSet<String> allDrug = getAllDrug("data/input/drugbank/drugbank_dump.nt");

			
		String datafile = "D:/data/drug-target/source/tri.nt";
		String modelfile = "D:/data/drug-target/models/deepwalk/tri/external/deepwalk_external_100.txt";
		String idxfile="D:/data/drug-target/models/deepwalk/tri/external/deepwalkidx_external_100.txt";
		String externalfile = "D:/data/drug-target/input/tri/external/connected/external_connected.nt";
		
		
		HashMap<String, HashSet<String>> drugToTargetassociations = getDrugTargetAssociations(datafile);
		HashMap<String, HashSet<String>> TargetToDrugassociations = getTargetDrugAssociations(datafile);

		TargetPrediction predict2 = new TargetPrediction(modelfile);
		predict2.feedGold(externalfile);

		HashSet<String> queries2 = predict2.getDrugAsQueries();

		HashMap<String, HashMap<String, Double>> results2 = predict2.predictBySimilarDrug(idxfile, queries2,
				drugToTargetassociations, allDrug, allTarget);

		EvaluationMetrics metric2 = new EvaluationMetrics(results2, predict2.getGoldmap());
		
		metric2.getRecallByTopN(
				"D:/data/drug-target/examples/deepwalk_DBSI_getRecallByTopN_" + "external" + ".txt",3);
		
		DrugPrediction predict3 = new DrugPrediction(modelfile);
		predict3.feedGold(externalfile);

		HashSet<String> queries3 = predict3.getTargetAsQueries();

		HashMap<String, HashMap<String, Double>> results3 = predict3.predictBySimilarTarget(idxfile, queries3,
				TargetToDrugassociations, allDrug, allTarget);

		EvaluationMetrics metric3 = new EvaluationMetrics(results3, predict3.getGoldmap());
		metric3.getRecallByTopN(
				"D:/data/drug-target/examples/deepwalk_TBSI_getRecallByTopN_" + "external" + ".txt",3);
		
	}
	
	
	public void internal_bi_area() throws Exception {

		HashSet<String> allTarget = getAllTarget("data/input/drugbank/drugbank_dump.nt");
		HashSet<String> allDrug = getAllDrug("data/input/drugbank/drugbank_dump.nt");
		BufferedWriter bw = new BufferedWriter(
				new FileWriter(new File("data/output/experiment0630/deepwalk_bi_areas_internal.txt")));

		for (int i = 1; i < 11; i++) {

			String datafile = "data/input/drugCloud/data_bi_" + i * 100 + ".nt";
			String modelfile = "data/input/drugCloud/deepwalk_bi_" + i + 100 + "_100.txt";
			String idxfile = "data/input/drugCloud/deepwalkidx_bi_" + i + 100 + "_100.txt";
			String removefile = "data/input/drugCloud/removed_bi_" + i * 100 + ".nt";

			HashMap<String, HashSet<String>> drugToTargetassociations = getDrugTargetAssociations(datafile);
			HashMap<String, HashSet<String>> TargetToDrugassociations = getTargetDrugAssociations(datafile);

			// String datafile="data/input/drugCloud/data_bi_"+i*100+".nt";
			// String
			// modelfile="data/input/line/models/line_bi_"+i*100+"_500.txt";
			// String removefile="data/input/drugCloud/removed_bi_"+i*100+".nt";

			TargetPrediction predict1 = new TargetPrediction(modelfile);
			predict1.feedGold(removefile);

			HashMap<String, HashSet<String>> queries1 = predict1.getTargetAsQueries(datafile);

			HashMap<String, HashMap<String, Double>> results1 = predict1.predictBySimilarTarget(idxfile, queries1,
					drugToTargetassociations, allTarget);

			EvaluationMetrics metric1 = new EvaluationMetrics(results1, predict1.getGoldmap());

			metric1.writeArea("predictTargetByTarget_" + i * 100, bw);
			// drug-drug similar find test

			TargetPrediction predict2 = new TargetPrediction(modelfile);
			predict2.feedGold(removefile);

			HashSet<String> queries2 = predict2.getDrugAsQueries();

			HashMap<String, HashMap<String, Double>> results2 = predict2.predictBySimilarDrug(idxfile, queries2,
					drugToTargetassociations, allDrug, allTarget);

			EvaluationMetrics metric2 = new EvaluationMetrics(results2, predict2.getGoldmap());
			metric2.writeArea("predictTargetByDrug_" + i * 100, bw);

			DrugPrediction predict3 = new DrugPrediction(modelfile);
			predict3.feedGold(removefile);

			HashSet<String> queries3 = predict3.getTargetAsQueries();

			HashMap<String, HashMap<String, Double>> results3 = predict3.predictBySimilarTarget(idxfile, queries3,
					TargetToDrugassociations, allDrug, allTarget);

			EvaluationMetrics metric3 = new EvaluationMetrics(results3, predict3.getGoldmap());
			metric3.writeArea("predictDrugByTarget_" + i * 100, bw);

			// drug-drug similar find test

			DrugPrediction predict4 = new DrugPrediction(modelfile);
			predict4.feedGold(removefile);

			HashMap<String, HashSet<String>> queries4 = predict4.getDrugAsQueries(datafile);

			HashMap<String, HashMap<String, Double>> results4 = predict4.predictBySimilarDrug(idxfile, queries4,
					TargetToDrugassociations, allDrug);

			EvaluationMetrics metric4 = new EvaluationMetrics(results4, predict4.getGoldmap());
			metric4.writeArea("predictDrugByDrug_" + i * 100, bw);
		}

	}

	public void internal_tri_area() throws Exception {

		HashSet<String> allTarget = getAllTarget("data/input/drugbank/drugbank_dump.nt");
		HashSet<String> allDrug = getAllDrug("data/input/drugbank/drugbank_dump.nt");
		BufferedWriter bw = new BufferedWriter(
				new FileWriter(new File("data/output/experiment_tri/deepwalk_tri_areas_internal.txt")));

		for (int i = 1; i < 11; i++) {

			String datafile = "data/input/drugCloud/data_tri_" + i * 100 + ".nt";
			String modelfile = "data/input/drugCloud/deepwalk_tri_" + i + 100 + "_100.txt";
			String idxfile = "data/input/drugCloud/deepwalkidx_tri_" + i + 100 + "_100.txt";
			String removefile = "data/input/drugCloud/removed_bi_" + i * 100 + ".nt";

			HashMap<String, HashSet<String>> drugToTargetassociations = getDrugTargetAssociations(datafile);
			HashMap<String, HashSet<String>> TargetToDrugassociations = getTargetDrugAssociations(datafile);

			// String datafile="data/input/drugCloud/data_bi_"+i*100+".nt";
			// String
			// modelfile="data/input/line/models/line_bi_"+i*100+"_500.txt";
			// String removefile="data/input/drugCloud/removed_bi_"+i*100+".nt";

			TargetPrediction predict1 = new TargetPrediction(modelfile);
			predict1.feedGold(removefile);

			HashMap<String, HashSet<String>> queries1 = predict1.getTargetAsQueries(datafile);

			HashMap<String, HashMap<String, Double>> results1 = predict1.predictBySimilarTarget(idxfile, queries1,
					drugToTargetassociations, allTarget);

			EvaluationMetrics metric1 = new EvaluationMetrics(results1, predict1.getGoldmap());
			metric1.writeArea("predictTargetByTarget_" + i * 100, bw);

			// drug-drug similar find test

			TargetPrediction predict2 = new TargetPrediction(modelfile);
			predict2.feedGold(removefile);

			HashSet<String> queries2 = predict2.getDrugAsQueries();

			HashMap<String, HashMap<String, Double>> results2 = predict2.predictBySimilarDrug(idxfile, queries2,
					drugToTargetassociations, allDrug, allTarget);

			EvaluationMetrics metric2 = new EvaluationMetrics(results2, predict2.getGoldmap());
			metric2.writeArea("predictTargetByDrug_" + i * 100, bw);

			DrugPrediction predict3 = new DrugPrediction(modelfile);
			predict3.feedGold(removefile);

			HashSet<String> queries3 = predict3.getTargetAsQueries();

			HashMap<String, HashMap<String, Double>> results3 = predict3.predictBySimilarTarget(idxfile, queries3,
					TargetToDrugassociations, allDrug, allTarget);

			EvaluationMetrics metric3 = new EvaluationMetrics(results3, predict3.getGoldmap());
			metric3.writeArea("predictDrugByTarget_" + i * 100, bw);

			// drug-drug similar find test

			DrugPrediction predict4 = new DrugPrediction(modelfile);
			predict4.feedGold(removefile);

			HashMap<String, HashSet<String>> queries4 = predict4.getDrugAsQueries(datafile);

			HashMap<String, HashMap<String, Double>> results4 = predict4.predictBySimilarDrug(idxfile, queries4,
					TargetToDrugassociations, allDrug);

			EvaluationMetrics metric4 = new EvaluationMetrics(results4, predict4.getGoldmap());
			metric4.writeArea("predictDrugByDrug_" + i * 100, bw);
		}

	}

	public void external_bi_area() throws Exception {

		HashSet<String> allTarget = getAllTarget("data/input/drugbank/drugbank_dump.nt");
		HashSet<String> allDrug = getAllDrug("data/input/drugbank/drugbank_dump.nt");
		BufferedWriter bw = new BufferedWriter(
				new FileWriter(new File("data/output/experiment0630/deepwalk_bi_areas_external.txt")));

		String datafile = "data/input/drugCloud/data_bi.nt";
		String modelfile = "data/input/drugCloud/deepwalk_bi_100.txt";
		String idxfile = "data/input/drugCloud/deepwalkidx_bi_100.txt";
		String externalfile = "data/input/drugCloud/drugbank_predict_4.nt";

		HashMap<String, HashSet<String>> drugToTargetassociations = getDrugTargetAssociations(datafile);
		HashMap<String, HashSet<String>> TargetToDrugassociations = getTargetDrugAssociations(datafile);

		// String datafile="data/input/drugCloud/data_bi_"+i*100+".nt";
		// String modelfile="data/input/line/models/line_bi_"+i*100+"_500.txt";
		// String removefile="data/input/drugCloud/removed_bi_"+i*100+".nt";

		TargetPrediction predict1 = new TargetPrediction(modelfile);
		predict1.feedGold(externalfile);

		HashMap<String, HashSet<String>> queries1 = predict1.getTargetAsQueries(datafile);

		HashMap<String, HashMap<String, Double>> results1 = predict1.predictBySimilarTarget(idxfile, queries1,
				drugToTargetassociations, allTarget);

		EvaluationMetrics metric1 = new EvaluationMetrics(results1, predict1.getGoldmap());
		metric1.writeArea("predictTargetByTarget_external", bw);

		// drug-drug similar find test

		TargetPrediction predict2 = new TargetPrediction(modelfile);
		predict2.feedGold(externalfile);

		HashSet<String> queries2 = predict2.getDrugAsQueries();

		HashMap<String, HashMap<String, Double>> results2 = predict2.predictBySimilarDrug(idxfile, queries2,
				drugToTargetassociations, allDrug, allTarget);

		EvaluationMetrics metric2 = new EvaluationMetrics(results2, predict2.getGoldmap());
		metric2.writeArea("predictTargetByDrug_external", bw);

		DrugPrediction predict3 = new DrugPrediction(modelfile);
		predict3.feedGold(externalfile);

		HashSet<String> queries3 = predict3.getTargetAsQueries();

		HashMap<String, HashMap<String, Double>> results3 = predict3.predictBySimilarTarget(idxfile, queries3,
				TargetToDrugassociations, allDrug, allTarget);

		EvaluationMetrics metric3 = new EvaluationMetrics(results3, predict3.getGoldmap());
		metric3.writeArea("predictDrugByTarget_external", bw);

		// drug-drug similar find test

		DrugPrediction predict4 = new DrugPrediction(modelfile);
		predict4.feedGold(externalfile);

		HashMap<String, HashSet<String>> queries4 = predict4.getDrugAsQueries(datafile);

		HashMap<String, HashMap<String, Double>> results4 = predict4.predictBySimilarDrug(idxfile, queries4,
				TargetToDrugassociations, allDrug);

		EvaluationMetrics metric4 = new EvaluationMetrics(results4, predict4.getGoldmap());
		metric4.writeArea("predictDrugByDrug_external", bw);

	}

	public void external_tri_area() throws Exception {

		HashSet<String> allTarget = getAllTarget("data/input/drugbank/drugbank_dump.nt");
		HashSet<String> allDrug = getAllDrug("data/input/drugbank/drugbank_dump.nt");
		BufferedWriter bw = new BufferedWriter(
				new FileWriter(new File("data/output/experiment_tri/deepwalk_tri_areas_external.txt")));

		String datafile = "data/input/drugCloud/data_tri.nt";
		String modelfile = "data/input/drugCloud/deepwalk_tri_100.txt";
		String idxfile = "data/input/drugCloud/deepwalkidx_tri_100.txt";
		String externalfile = "data/input/drugCloud/drugbank_predict_4.nt";

		HashMap<String, HashSet<String>> drugToTargetassociations = getDrugTargetAssociations(datafile);
		HashMap<String, HashSet<String>> TargetToDrugassociations = getTargetDrugAssociations(datafile);

		// String datafile="data/input/drugCloud/data_bi_"+i*100+".nt";
		// String modelfile="data/input/line/models/line_bi_"+i*100+"_500.txt";
		// String removefile="data/input/drugCloud/removed_bi_"+i*100+".nt";

		TargetPrediction predict1 = new TargetPrediction(modelfile);
		predict1.feedGold(externalfile);

		HashMap<String, HashSet<String>> queries1 = predict1.getTargetAsQueries(datafile);

		HashMap<String, HashMap<String, Double>> results1 = predict1.predictBySimilarTarget(idxfile, queries1,
				drugToTargetassociations, allTarget);

		EvaluationMetrics metric1 = new EvaluationMetrics(results1, predict1.getGoldmap());
		metric1.writeArea("predictTargetByTarget_external", bw);

		// drug-drug similar find test

		TargetPrediction predict2 = new TargetPrediction(modelfile);
		predict2.feedGold(externalfile);

		HashSet<String> queries2 = predict2.getDrugAsQueries();

		HashMap<String, HashMap<String, Double>> results2 = predict2.predictBySimilarDrug(idxfile, queries2,
				drugToTargetassociations, allDrug, allTarget);

		EvaluationMetrics metric2 = new EvaluationMetrics(results2, predict2.getGoldmap());
		metric2.writeArea("predictTargetByDrug_external", bw);

		DrugPrediction predict3 = new DrugPrediction(modelfile);
		predict3.feedGold(externalfile);

		HashSet<String> queries3 = predict3.getTargetAsQueries();

		HashMap<String, HashMap<String, Double>> results3 = predict3.predictBySimilarTarget(idxfile, queries3,
				TargetToDrugassociations, allDrug, allTarget);

		EvaluationMetrics metric3 = new EvaluationMetrics(results3, predict3.getGoldmap());
		metric3.writeArea("predictDrugByTarget_external", bw);

		// drug-drug similar find test

		DrugPrediction predict4 = new DrugPrediction(modelfile);
		predict4.feedGold(externalfile);

		HashMap<String, HashSet<String>> queries4 = predict4.getDrugAsQueries(datafile);

		HashMap<String, HashMap<String, Double>> results4 = predict4.predictBySimilarDrug(idxfile, queries4,
				TargetToDrugassociations, allDrug);

		EvaluationMetrics metric4 = new EvaluationMetrics(results4, predict4.getGoldmap());
		metric4.writeArea("predictDrugByDrug_external", bw);

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
