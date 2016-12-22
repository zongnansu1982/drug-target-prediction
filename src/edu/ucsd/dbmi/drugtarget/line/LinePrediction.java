package edu.ucsd.dbmi.drugtarget.line;

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


public class LinePrediction {
	
	/**
	 * thinking of replace internal with all method
	 * @throws Exception
	 */
	public void internal()throws Exception {

//    	BufferedWriter bw =new BufferedWriter(new FileWriter(new File("data/output/experiment0630/prediction_internal_1.txt")));
		BufferedWriter bw =new BufferedWriter(new FileWriter(new File("experiment/prediction_internal_1.txt")));
		
//    	for (int i = 1; i < 11; i++) {
    		int i=1;
//    		String datafile="data/input/drugCloud/data_bi_"+i*100+".nt";
//        	String modelfile="outsource/windows/experiment0630/models/line_bi_"+i*100+"_500.txt";
//        	String removefile="data/input/drugCloud/removed_bi_"+i*100+".nt";
        	
        	String datafile="data/input/drugCloud/data_bi_"+i*100+".nt";
        	String modelfile="data/input/line/models/line_bi_"+i*100+"_500.txt";
        	String removefile="data/input/drugCloud/removed_bi_"+i*100+".nt";
        	
        	
        	int topN=10;
    		
        	TargetPrediction predict1=new TargetPrediction(modelfile);
    		predict1.feedGold(removefile);
    		
    		HashMap<String,HashSet<String>> queries1=predict1.getTargetAsQueries(datafile);
    		
    		HashMap<String,List<Map.Entry<String, Double>>> results1=predict1.predictBySimilarTarget(queries1, topN);	
    		Double recall1=predict1.recall(results1);
    		Double avgAUC1=AUCRoc.getArea(results1, predict1.getGoldmap());
    		
    		bw.write(i+",predictTargetBySimilarTarget,"+","+recall1+","+avgAUC1+"\n");
    		System.out.println(i+",predictTargetBySimilarTarget,"+recall1+","+avgAUC1);
    		bw.flush();
    		// drug-drug similar find test
    		
    		
    		TargetPrediction predict2=new TargetPrediction(modelfile);
    		predict2.feedGold(removefile);
    		
    		HashSet<String> queries2=predict2.getDrugAsQueries();
    		
    		HashMap<String,List<Map.Entry<String, Double>>> results2=predict2.predictBySimilarDrug(datafile,queries2, topN);	
    		
    		Double recall2=predict2.recall(results2);
    		Double avgAUC2=AUCRoc.getArea(results2, predict2.getGoldmap());
    		bw.write(i+",predictTargetBySimilarDrug,"+recall2+","+ avgAUC2+"\n");
    		System.out.println(i+",predictTargetBySimilarDrug,"+recall2+","+ avgAUC2);
    		bw.flush();
    		
    		
    		DrugPrediction predict3=new DrugPrediction(modelfile);
    		predict3.feedGold(removefile);
    		
    		HashSet<String> queries3=predict3.getTargetAsQueries();
    		
    		HashMap<String,List<Map.Entry<String, Double>>> results3=predict3.predictBySimilarTarget(datafile,queries3, topN);	
    		
    		Double recall3=predict3.recall(results3);
    		Double avgAUC3=AUCRoc.getArea(results3, predict3.getGoldmap());
    		bw.write(i+",predictDrugBySimilarTarget,"+recall3+","+ avgAUC3+"\n");
    		System.out.println(i+",predictDrugBySimilarTarget,"+recall3+","+ avgAUC3);
    		bw.flush();
    		
    		// drug-drug similar find test
    		
    		DrugPrediction predict4=new DrugPrediction(modelfile);
    		predict4.feedGold(removefile);
    		
    		HashMap<String,HashSet<String>> queries4=predict4.getDrugAsQueries(datafile);
    		
    		HashMap<String,List<Map.Entry<String, Double>>> results4=predict4.predictBySimilarDrug(queries4, topN);	
    		
    		Double recall4=predict4.recall(results4);
    		Double avgAUC4=AUCRoc.getArea(results4, predict4.getGoldmap());
    		bw.write(i+",predictDrugBySimilarDrug,"+recall4+","+ avgAUC4+"\n");
    		System.out.println(i+",predictDrugBySimilarDrug,"+recall4+","+ avgAUC4);
    		bw.flush();
//		}
    	
    	bw.flush();
    	bw.close();

	}
	
	public void external_bi()throws Exception {
		
			HashSet<String> allTarget=getAllTarget("data/input/drugbank/drugbank_dump.nt");
    		HashSet<String> allDrug=getAllDrug("data/input/drugbank/drugbank_dump.nt");
    	
			String datafile="data/input/drugCloud/data_bi.nt";
    		String modelfile="outsource/windows/experiment0630/models/line_bi_100.txt";
    		String externalfile="data/input/drugCloud/drugbank_predict_4.nt";
    	

    		HashMap<String,HashSet<String>> drugToTargetassociations=getDrugTargetAssociations(datafile);
    		HashMap<String,HashSet<String>> TargetToDrugassociations=getTargetDrugAssociations(datafile);
        	
//    		String datafile="data/input/drugCloud/data_bi_"+i*100+".nt";
//        	String modelfile="data/input/line/models/line_bi_"+i*100+"_500.txt";
//        	String removefile="data/input/drugCloud/removed_bi_"+i*100+".nt"; 	
        	
    		
//        	TargetPrediction predict1=new TargetPrediction(modelfile);
//    		predict1.feedGold(externalfile);
//    		
//    		HashMap<String,HashSet<String>> queries1=predict1.getTargetAsQueries(datafile);
//    		
//    		HashMap<String,HashMap<String, Double>> results1=predict1.predictBySimilarTarget(queries1,drugToTargetassociations, allTarget);
//    		
//    		EvaluationMetrics metric1=new EvaluationMetrics(results1, predict1.getGoldmap());
//    		metric1.getAreasByTopN("data/output/experiment0630/line_predictTargetByTarget_getAreasByTopN_"+"external"+".txt");
//    		metric1.getAreasByPercentage("data/output/experiment0630/line_predictTargetByTarget_getAreasByPercentage_"+"external"+".txt");
//    		metric1.getRecallByPercentage("data/output/experiment0630/line_predictTargetByTarget_getRecallByPercentage_"+"external"+".txt");
//    		metric1.getRecallByTopN("data/output/experiment0630/line_predictTargetByTarget_getRecallByTopN_"+"external"+".txt");
//    		metric1.getArea("data/output/experiment0630/line_predictTargetByTarget_getArea_"+"external"+".txt");
    		
    		// drug-drug similar find test
    		
    		
    		TargetPrediction predict2=new TargetPrediction(modelfile);
    		predict2.feedGold(externalfile);
    		
    		HashSet<String> queries2=predict2.getDrugAsQueries();
    		
    		HashMap<String,HashMap<String, Double>> results2=predict2.predictBySimilarDrug(queries2, drugToTargetassociations, allDrug,allTarget);	
    		
    		
    		
    		EvaluationMetrics metric2=new EvaluationMetrics(results2, predict2.getGoldmap());
    		metric2.getAreasByTopN("data/output/experiment0630/line_predictTargetByDrug_getAreasByTopN_"+"external"+".txt");
    		metric2.getAreasByPercentage("data/output/experiment0630/line_predictTargetByDrug_getAreasByPercentage_"+"external"+".txt");
    		metric2.getRecallByPercentage("data/output/experiment0630/line_predictTargetByDrug_getRecallByPercentage_"+"external"+".txt");
    		metric2.getRecallByTopN("data/output/experiment0630/line_predictTargetByDrug_getRecallByTopN_"+"external"+".txt");
    		metric2.getArea("D:/data/drug-target/results/bi/external/line_DBSI_area_"+"external"+".txt");
    		
    		DrugPrediction predict3=new DrugPrediction(modelfile);
    		predict3.feedGold(externalfile);
    		
    		HashSet<String> queries3=predict3.getTargetAsQueries();
    		
    		HashMap<String,HashMap<String, Double>> results3=predict3.predictBySimilarTarget(queries3, TargetToDrugassociations,allDrug,allTarget);	
    		
    		EvaluationMetrics metric3=new EvaluationMetrics(results3, predict3.getGoldmap());
    		metric3.getAreasByTopN("data/output/experiment0630/line_predictDrugByTarget_getAreasByTopN_"+"external"+".txt");
    		metric3.getAreasByPercentage("data/output/experiment0630/line_predictDrugByTarget_getAreasByPercentage_"+"external"+".txt");
    		metric3.getRecallByPercentage("data/output/experiment0630/line_predictDrugByTarget_getRecallByPercentage_"+"external"+".txt");
    		metric3.getRecallByTopN("data/output/experiment0630/line_predictDrugByTarget_getRecallByTopN_"+"external"+".txt");
    		metric3.getArea("data/output/experiment0630/line_predictDrugByTarget_getArea_"+"external"+".txt");
    		
    		// drug-drug similar find test
    		
//    		DrugPrediction predict4=new DrugPrediction(modelfile);
//    		predict4.feedGold(externalfile);
//    		
//    		HashMap<String,HashSet<String>> queries4=predict4.getDrugAsQueries(datafile);
//    		
//    		HashMap<String,HashMap<String, Double>> results4=predict4.predictBySimilarDrug(queries4, TargetToDrugassociations,allDrug);
//    	
//    		
//    		EvaluationMetrics metric4=new EvaluationMetrics(results4, predict4.getGoldmap());
//    		metric4.getAreasByTopN("data/output/experiment0630/line_predictDrugByDrug_getAreasByTopN_"+"external"+".txt");
//    		metric4.getAreasByPercentage("data/output/experiment0630/line_predictDrugByDrug_getAreasByPercentage_"+"external"+".txt");
//    		metric4.getRecallByPercentage("data/output/experiment0630/line_predictDrugByDrug_getRecallByPercentage_"+"external"+".txt");
//    		metric4.getRecallByTopN("data/output/experiment0630/line_predictDrugByDrug_getRecallByTopN_"+"external"+".txt");
//    		metric4.getArea("data/output/experiment0630/line_predictDrugByDrug_getArea_"+"external"+".txt");
    

	}

	/**
	 * replacement method
	 * @throws Exception
	 */
	public void monteCarlo_bi()throws Exception {
		
			HashSet<String> allTarget=getAllTarget("data/input/drugbank/drugbank_dump.nt");
			HashSet<String> allDrug=getAllDrug("data/input/drugbank/drugbank_dump.nt");
    	
			BufferedWriter bw = new BufferedWriter(new FileWriter(new File("D:/data/drug-target/results/bi/monteCarlo/line_bi_monteCarloAreas.csv")));
			
    	    for (int i = 1; i < 11; i++) {
    	    String datafile = "D:/data/drug-target/input/bi/monteCarlo/bi_" + i * 100 + "_data.nt";
    	    String modelfile = "D:/data/drug-target/models/line/bi/monteCarlo/line_bi_" + i * 100 + "_100.txt";
        	String removefile = "D:/data/drug-target/input/bi/monteCarlo/bi_" + i * 100 + "_test.nt";
    		HashMap<String,HashSet<String>> drugToTargetassociations=getDrugTargetAssociations(datafile);
    		HashMap<String,HashSet<String>> TargetToDrugassociations=getTargetDrugAssociations(datafile);
        	
//    		String datafile="data/input/drugCloud/data_bi_"+i*100+".nt";
//        	String modelfile="data/input/line/models/line_bi_"+i*100+"_500.txt";
//        	String removefile="data/input/drugCloud/removed_bi_"+i*100+".nt"; 	
        	
    		
//        	TargetPrediction predict1=new TargetPrediction(modelfile);
//    		predict1.feedGold(removefile);
//    		
//    		HashMap<String,HashSet<String>> queries1=predict1.getTargetAsQueries(datafile);
//    		
//    		HashMap<String,HashMap<String, Double>> results1=predict1.predictBySimilarTarget(queries1,drugToTargetassociations, allTarget);
//    		
//    		EvaluationMetrics metric1=new EvaluationMetrics(results1, predict1.getGoldmap());
//    		metric1.getAreasByTopN("data/output/experiment0630/line_predictTargetByTarget_getAreasByTopN_"+i*100+".txt");
//    		metric1.getAreasByPercentage("data/output/experiment0630/line_predictTargetByTarget_getAreasByPercentage_"+i*100+".txt");
//    		metric1.getRecallByPercentage("data/output/experiment0630/line_predictTargetByTarget_getRecallByPercentage_"+i*100+".txt");
//    		metric1.getRecallByTopN("data/output/experiment0630/line_predictTargetByTarget_getRecallByTopN_"+i*100+".txt");
//    		metric1.getArea("data/output/experiment0630/line_predictTargetByTarget_getArea_"+i*100+".txt");
    		
    		// drug-drug similar find test
    		
    		TargetPrediction predict2=new TargetPrediction(modelfile);
    		predict2.feedGold(removefile);
    		
    		HashSet<String> queries2=predict2.getDrugAsQueries();
    		
    		HashMap<String,HashMap<String, Double>> results2=predict2.predictBySimilarDrug(queries2, drugToTargetassociations, allDrug,allTarget);	
    		
    		
    		
    		EvaluationMetrics metric2=new EvaluationMetrics(results2, predict2.getGoldmap());
    		metric2.getAreasByTopN("D:/data/drug-target/results/bi/monteCarlo/line_DBSI_areasByTopN_"+i*100+".txt");
    		metric2.getAreasByPercentage("D:/data/drug-target/results/bi/monteCarlo/line_DBSI_areasByPercentage_"+i*100+".txt");
    		metric2.getRecallByPercentage("D:/data/drug-target/results/bi/monteCarlo/line_DBSI_recallByPercentage_"+i*100+".txt");
    		metric2.getRecallByTopN("D:/data/drug-target/results/bi/monteCarlo/line_DBSI_recallByTopN_"+i*100+".txt");
    		metric2.getPrecisionByPercentage("D:/data/drug-target/results/bi/monteCarlo/line_DBSI_precisionByPercentage_" + i * 100
					+ ".txt");
			metric2.getPrecisionByTopN("D:/data/drug-target/results/bi/monteCarlo/line_DBSI_precisionByTopN_" + i * 100
					+ ".txt");
    		metric2.getArea(bw,"D:/data/drug-target/results/bi/monteCarlo/line_DBSI_area_"+i*100+".txt");
    		
    		DrugPrediction predict3=new DrugPrediction(modelfile);
    		predict3.feedGold(removefile);
    		
    		HashSet<String> queries3=predict3.getTargetAsQueries();
    		
    		HashMap<String,HashMap<String, Double>> results3=predict3.predictBySimilarTarget(queries3, TargetToDrugassociations,allDrug,allTarget);	
    		
    		EvaluationMetrics metric3=new EvaluationMetrics(results3, predict3.getGoldmap());
    		metric3.getAreasByTopN("D:/data/drug-target/results/bi/monteCarlo/line_TBSI_areasByTopN_"+i*100+".txt");
    		metric3.getAreasByPercentage("D:/data/drug-target/results/bi/monteCarlo/line_TBSI_areasByPercentage_"+i*100+".txt");
    		metric3.getRecallByPercentage("D:/data/drug-target/results/bi/monteCarlo/line_TBSI_recallByPercentage_"+i*100+".txt");
    		metric3.getRecallByTopN("D:/data/drug-target/results/bi/monteCarlo/line_TBSI_recallByTopN_"+i*100+".txt");
    		metric3.getPrecisionByPercentage("D:/data/drug-target/results/bi/monteCarlo/line_TBSI_precisionByPercentage_" + i * 100
					+ ".txt");
			metric3.getPrecisionByTopN("D:/data/drug-target/results/bi/monteCarlo/line_TBSI_precisionByTopN_" + i * 100
					+ ".txt");
    		metric3.getArea(bw,"D:/data/drug-target/results/bi/monteCarlo/line_TBSI_area_"+i*100+".txt");
    		
    		// drug-drug similar find test
    		
//    		DrugPrediction predict4=new DrugPrediction(modelfile);
//    		predict4.feedGold(removefile);
//    		
//    		HashMap<String,HashSet<String>> queries4=predict4.getDrugAsQueries(datafile);
//    		
//    		HashMap<String,HashMap<String, Double>> results4=predict4.predictBySimilarDrug(queries4, TargetToDrugassociations,allDrug);
//    	
//    		
//    		EvaluationMetrics metric4=new EvaluationMetrics(results4, predict4.getGoldmap());
//    		metric4.getAreasByTopN("data/output/experiment0630/line_predictDrugByDrug_getAreasByTopN_"+i*100+".txt");
//    		metric4.getAreasByPercentage("data/output/experiment0630/line_predictDrugByDrug_getAreasByPercentage_"+i*100+".txt");
//    		metric4.getRecallByPercentage("data/output/experiment0630/line_predictDrugByDrug_getRecallByPercentage_"+i*100+".txt");
//    		metric4.getRecallByTopN("data/output/experiment0630/line_predictDrugByDrug_getRecallByTopN_"+i*100+".txt");
//    		metric4.getArea("data/output/experiment0630/line_predictDrugByDrug_getArea_"+i*100+".txt");
    		bw.flush();
		}
    
    	    bw.flush();
    	    bw.close();
	}
	
	
	public void tenfolder_bi()throws Exception {
		
		HashSet<String> allTarget=getAllTarget("data/input/drugbank/drugbank_dump.nt");
		HashSet<String> allDrug=getAllDrug("data/input/drugbank/drugbank_dump.nt");
	
		BufferedWriter bw = new BufferedWriter(new FileWriter(new File("D:/data/drug-target/results/bi/10folder/line_bi_10folderAreas.csv")));
		
	    for (int i = 0; i < 10; i++) {
	    	
	    	String datafile = "D:/data/drug-target/input/bi/10folder/bi_folder-" + i + "_data.nt";
			String modelfile = "D:/data/drug-target/models/line/bi/10folder/line_bi_" + i + "_100.txt";
			String removefile = "D:/data/drug-target/input/bi/10folder/bi_folder-" + i + "_test.nt";
			
	    
		HashMap<String,HashSet<String>> drugToTargetassociations=getDrugTargetAssociations(datafile);
		HashMap<String,HashSet<String>> TargetToDrugassociations=getTargetDrugAssociations(datafile);
    	
//		String datafile="data/input/drugCloud/data_bi_"+i*100+".nt";
//    	String modelfile="data/input/line/models/line_bi_"+i*100+"_500.txt";
//    	String removefile="data/input/drugCloud/removed_bi_"+i*100+".nt"; 	
    	
		
//    	TargetPrediction predict1=new TargetPrediction(modelfile);
//		predict1.feedGold(removefile);
//		
//		HashMap<String,HashSet<String>> queries1=predict1.getTargetAsQueries(datafile);
//		
//		HashMap<String,HashMap<String, Double>> results1=predict1.predictBySimilarTarget(queries1,drugToTargetassociations, allTarget);
//		
//		EvaluationMetrics metric1=new EvaluationMetrics(results1, predict1.getGoldmap());
//		metric1.getAreasByTopN("data/output/experiment0630/line_predictTargetByTarget_getAreasByTopN_"+i*100+".txt");
//		metric1.getAreasByPercentage("data/output/experiment0630/line_predictTargetByTarget_getAreasByPercentage_"+i*100+".txt");
//		metric1.getRecallByPercentage("data/output/experiment0630/line_predictTargetByTarget_getRecallByPercentage_"+i*100+".txt");
//		metric1.getRecallByTopN("data/output/experiment0630/line_predictTargetByTarget_getRecallByTopN_"+i*100+".txt");
//		metric1.getArea("data/output/experiment0630/line_predictTargetByTarget_getArea_"+i*100+".txt");
		
		// drug-drug similar find test
		
		TargetPrediction predict2=new TargetPrediction(modelfile);
		predict2.feedGold(removefile);
		
		HashSet<String> queries2=predict2.getDrugAsQueries();
		
		HashMap<String,HashMap<String, Double>> results2=predict2.predictBySimilarDrug(queries2, drugToTargetassociations, allDrug,allTarget);	
		
		
		
		EvaluationMetrics metric2=new EvaluationMetrics(results2, predict2.getGoldmap());
		metric2.getAreasByTopN("D:/data/drug-target/results/bi/10folder/line_DBSI_areasByTopN_"+i+".txt");
		metric2.getAreasByPercentage("D:/data/drug-target/results/bi/10folder/line_DBSI_areasByPercentage_"+i+".txt");
		metric2.getRecallByPercentage("D:/data/drug-target/results/bi/10folder/line_DBSI_recallByPercentage_"+i+".txt");
		metric2.getRecallByTopN("D:/data/drug-target/results/bi/10folder/line_DBSI_recallByTopN_"+i+".txt");
		metric2.getPrecisionByPercentage("D:/data/drug-target/results/bi/10folder/line_DBSI_precisionByPercentage_" + i 
				+ ".txt");
		metric2.getPrecisionByTopN("D:/data/drug-target/results/bi/10folder/line_DBSI_precisionByTopN_" + i 
				+ ".txt");
		metric2.getArea(bw,"D:/data/drug-target/results/bi/10folder/line_DBSI_area_"+i+".txt");
		
		DrugPrediction predict3=new DrugPrediction(modelfile);
		predict3.feedGold(removefile);
		
		HashSet<String> queries3=predict3.getTargetAsQueries();
		
		HashMap<String,HashMap<String, Double>> results3=predict3.predictBySimilarTarget(queries3, TargetToDrugassociations,allDrug,allTarget);	
		
		EvaluationMetrics metric3=new EvaluationMetrics(results3, predict3.getGoldmap());
		metric3.getAreasByTopN("D:/data/drug-target/results/bi/10folder/line_TBSI_areasByTopN_"+i+".txt");
		metric3.getAreasByPercentage("D:/data/drug-target/results/bi/10folder/line_TBSI_areasByPercentage_"+i+".txt");
		metric3.getRecallByPercentage("D:/data/drug-target/results/bi/10folder/line_TBSI_recallByPercentage_"+i+".txt");
		metric3.getRecallByTopN("D:/data/drug-target/results/bi/10folder/line_TBSI_recallByTopN_"+i+".txt");
		metric3.getPrecisionByPercentage("D:/data/drug-target/results/bi/10folder/line_TBSI_precisionByPercentage_" + i 
				+ ".txt");
		metric3.getPrecisionByTopN("D:/data/drug-target/results/bi/10folder/line_TBSI_precisionByTopN_" + i 
				+ ".txt");
		metric3.getArea(bw,"D:/data/drug-target/results/bi/10folder/line_TBSI_area_"+i+".txt");
		
		// drug-drug similar find test
		
//		DrugPrediction predict4=new DrugPrediction(modelfile);
//		predict4.feedGold(removefile);
//		
//		HashMap<String,HashSet<String>> queries4=predict4.getDrugAsQueries(datafile);
//		
//		HashMap<String,HashMap<String, Double>> results4=predict4.predictBySimilarDrug(queries4, TargetToDrugassociations,allDrug);
//	
//		
//		EvaluationMetrics metric4=new EvaluationMetrics(results4, predict4.getGoldmap());
//		metric4.getAreasByTopN("data/output/experiment0630/line_predictDrugByDrug_getAreasByTopN_"+i*100+".txt");
//		metric4.getAreasByPercentage("data/output/experiment0630/line_predictDrugByDrug_getAreasByPercentage_"+i*100+".txt");
//		metric4.getRecallByPercentage("data/output/experiment0630/line_predictDrugByDrug_getRecallByPercentage_"+i*100+".txt");
//		metric4.getRecallByTopN("data/output/experiment0630/line_predictDrugByDrug_getRecallByTopN_"+i*100+".txt");
//		metric4.getArea("data/output/experiment0630/line_predictDrugByDrug_getArea_"+i*100+".txt");
		bw.flush();
		
	}
	    bw.flush();
	    bw.close();

}
	
	
	
	public void tenfolder_tri(String datatype)throws Exception {
		
		HashSet<String> allTarget=getAllTarget("data/input/drugbank/drugbank_dump.nt");
		HashSet<String> allDrug=getAllDrug("data/input/drugbank/drugbank_dump.nt");
	
		BufferedWriter bw = new BufferedWriter(new FileWriter(new File("D:/data/drug-target/results/tri/"+datatype+"/10folder/line_tri_10folderAreas.csv")));
		
	    for (int i = 0; i < 10; i++) {
	    	
	    	String datafile = "D:/data/drug-target/input/tri/"+datatype+"/10folder/tri_folder-" + i + "_data.nt";
			String modelfile = "D:/data/drug-target/models/line/tri/"+datatype+"/10folder/line_tri_" + i + "_100.txt";
			String removefile = "D:/data/drug-target/input/tri/"+datatype+"/10folder/tri_folder-" + i + "_test.nt";
			
	    
		HashMap<String,HashSet<String>> drugToTargetassociations=getDrugTargetAssociations(datafile);
		HashMap<String,HashSet<String>> TargetToDrugassociations=getTargetDrugAssociations(datafile);
    	
//		String datafile="data/input/drugCloud/data_bi_"+i*100+".nt";
//    	String modelfile="data/input/line/models/line_bi_"+i*100+"_500.txt";
//    	String removefile="data/input/drugCloud/removed_bi_"+i*100+".nt"; 	
    	
		
//    	TargetPrediction predict1=new TargetPrediction(modelfile);
//		predict1.feedGold(removefile);
//		
//		HashMap<String,HashSet<String>> queries1=predict1.getTargetAsQueries(datafile);
//		
//		HashMap<String,HashMap<String, Double>> results1=predict1.predictBySimilarTarget(queries1,drugToTargetassociations, allTarget);
//		
//		EvaluationMetrics metric1=new EvaluationMetrics(results1, predict1.getGoldmap());
//		metric1.getAreasByTopN("data/output/experiment0630/line_predictTargetByTarget_getAreasByTopN_"+i*100+".txt");
//		metric1.getAreasByPercentage("data/output/experiment0630/line_predictTargetByTarget_getAreasByPercentage_"+i*100+".txt");
//		metric1.getRecallByPercentage("data/output/experiment0630/line_predictTargetByTarget_getRecallByPercentage_"+i*100+".txt");
//		metric1.getRecallByTopN("data/output/experiment0630/line_predictTargetByTarget_getRecallByTopN_"+i*100+".txt");
//		metric1.getArea("data/output/experiment0630/line_predictTargetByTarget_getArea_"+i*100+".txt");
		
		// drug-drug similar find test
		
		TargetPrediction predict2=new TargetPrediction(modelfile);
		predict2.feedGold(removefile);
		
		HashSet<String> queries2=predict2.getDrugAsQueries();
		
		HashMap<String,HashMap<String, Double>> results2=predict2.predictBySimilarDrug(queries2, drugToTargetassociations, allDrug,allTarget);	
		
		
		
		EvaluationMetrics metric2=new EvaluationMetrics(results2, predict2.getGoldmap());
//		metric2.getAreasByTopN("D:/data/drug-target/results/bi/10folder/line_DBSI_areasByTopN_"+i+".txt");
//		metric2.getAreasByPercentage("D:/data/drug-target/results/bi/10folder/line_DBSI_areasByPercentage_"+i+".txt");
		metric2.getRecallByPercentage("D:/data/drug-target/results/tri/"+datatype+"/10folder/line_DBSI_recallByPercentage_"+i+".txt");
		metric2.getRecallByTopN("D:/data/drug-target/results/tri/"+datatype+"/10folder/line_DBSI_recallByTopN_"+i+".txt");
		metric2.getPrecisionByPercentage("D:/data/drug-target/results/tri/"+datatype+"/10folder/line_DBSI_precisionByPercentage_" + i 
				+ ".txt");
		metric2.getPrecisionByTopN("D:/data/drug-target/results/tri/"+datatype+"/10folder/line_DBSI_precisionByTopN_" + i 
				+ ".txt");
		metric2.getArea(bw,"D:/data/drug-target/results/tri/"+datatype+"/10folder/line_DBSI_area_"+i+".txt");
		
		DrugPrediction predict3=new DrugPrediction(modelfile);
		predict3.feedGold(removefile);
		
		HashSet<String> queries3=predict3.getTargetAsQueries();
		
		HashMap<String,HashMap<String, Double>> results3=predict3.predictBySimilarTarget(queries3, TargetToDrugassociations,allDrug,allTarget);	
		
		EvaluationMetrics metric3=new EvaluationMetrics(results3, predict3.getGoldmap());
//		metric3.getAreasByTopN("D:/data/drug-target/results/bi/10folder/line_TBSI_areasByTopN_"+i+".txt");
//		metric3.getAreasByPercentage("D:/data/drug-target/results/bi/10folder/line_TBSI_areasByPercentage_"+i+".txt");
		metric3.getRecallByPercentage("D:/data/drug-target/results/tri/"+datatype+"/10folder/line_TBSI_recallByPercentage_"+i+".txt");
		metric3.getRecallByTopN("D:/data/drug-target/results/tri/"+datatype+"/10folder/line_TBSI_recallByTopN_"+i+".txt");
		metric3.getPrecisionByPercentage("D:/data/drug-target/results/tri/"+datatype+"/10folder/line_TBSI_precisionByPercentage_" + i 
				+ ".txt");
		metric3.getPrecisionByTopN("D:/data/drug-target/results/tri/"+datatype+"/10folder/line_TBSI_precisionByTopN_" + i 
				+ ".txt");
		metric3.getArea(bw,"D:/data/drug-target/results/tri/"+datatype+"/10folder/line_TBSI_area_"+i+".txt");
		
		// drug-drug similar find test
		
//		DrugPrediction predict4=new DrugPrediction(modelfile);
//		predict4.feedGold(removefile);
//		
//		HashMap<String,HashSet<String>> queries4=predict4.getDrugAsQueries(datafile);
//		
//		HashMap<String,HashMap<String, Double>> results4=predict4.predictBySimilarDrug(queries4, TargetToDrugassociations,allDrug);
//	
//		
//		EvaluationMetrics metric4=new EvaluationMetrics(results4, predict4.getGoldmap());
//		metric4.getAreasByTopN("data/output/experiment0630/line_predictDrugByDrug_getAreasByTopN_"+i*100+".txt");
//		metric4.getAreasByPercentage("data/output/experiment0630/line_predictDrugByDrug_getAreasByPercentage_"+i*100+".txt");
//		metric4.getRecallByPercentage("data/output/experiment0630/line_predictDrugByDrug_getRecallByPercentage_"+i*100+".txt");
//		metric4.getRecallByTopN("data/output/experiment0630/line_predictDrugByDrug_getRecallByTopN_"+i*100+".txt");
//		metric4.getArea("data/output/experiment0630/line_predictDrugByDrug_getArea_"+i*100+".txt");
		bw.flush();
		
	}
	    bw.flush();
	    bw.close();

}

	
public void tenfolder_bi(String datatype)throws Exception {
		
		HashSet<String> allTarget=getAllTarget("data/input/drugbank/drugbank_dump.nt");
		HashSet<String> allDrug=getAllDrug("data/input/drugbank/drugbank_dump.nt");
	
		BufferedWriter bw = new BufferedWriter(new FileWriter(new File("D:/data/drug-target/results/bi/"+datatype+"/10folder/line_bi_10folderAreas.csv")));
		
	    for (int i = 0; i < 10; i++) {
	    	
	    	String datafile = "D:/data/drug-target/input/bi/"+datatype+"/10folder/bi_folder-" + i + "_data.nt";
			String modelfile = "D:/data/drug-target/models/line/bi/"+datatype+"/10folder/line_bi_" + i + "_100.txt";
			String removefile = "D:/data/drug-target/input/bi/"+datatype+"/10folder/bi_folder-" + i + "_test.nt";
			
	    
		HashMap<String,HashSet<String>> drugToTargetassociations=getDrugTargetAssociations(datafile);
		HashMap<String,HashSet<String>> TargetToDrugassociations=getTargetDrugAssociations(datafile);
    	

		// drug-drug similar find test
		
		TargetPrediction predict2=new TargetPrediction(modelfile);
		predict2.feedGold(removefile);
		
		HashSet<String> queries2=predict2.getDrugAsQueries();
		
		HashMap<String,HashMap<String, Double>> results2=predict2.predictBySimilarDrug(queries2, drugToTargetassociations, allDrug,allTarget);	
		
		
		
		EvaluationMetrics metric2=new EvaluationMetrics(results2, predict2.getGoldmap());
//		metric2.getAreasByTopN("D:/data/drug-target/results/bi/10folder/line_DBSI_areasByTopN_"+i+".txt");
//		metric2.getAreasByPercentage("D:/data/drug-target/results/bi/10folder/line_DBSI_areasByPercentage_"+i+".txt");
		metric2.getRecallByPercentage("D:/data/drug-target/results/bi/"+datatype+"/10folder/line_DBSI_recallByPercentage_"+i+".txt");
		metric2.getRecallByTopN("D:/data/drug-target/results/bi/"+datatype+"/10folder/line_DBSI_recallByTopN_"+i+".txt");
		metric2.getPrecisionByPercentage("D:/data/drug-target/results/bi/"+datatype+"/10folder/line_DBSI_precisionByPercentage_" + i 
				+ ".txt");
		metric2.getPrecisionByTopN("D:/data/drug-target/results/bi/"+datatype+"/10folder/line_DBSI_precisionByTopN_" + i 
				+ ".txt");
		metric2.getArea(bw,"D:/data/drug-target/results/bi/"+datatype+"/10folder/line_DBSI_area_"+i+".txt");
		
		DrugPrediction predict3=new DrugPrediction(modelfile);
		predict3.feedGold(removefile);
		
		HashSet<String> queries3=predict3.getTargetAsQueries();
		
		HashMap<String,HashMap<String, Double>> results3=predict3.predictBySimilarTarget(queries3, TargetToDrugassociations,allDrug,allTarget);	
		
		EvaluationMetrics metric3=new EvaluationMetrics(results3, predict3.getGoldmap());
//		metric3.getAreasByTopN("D:/data/drug-target/results/bi/10folder/line_TBSI_areasByTopN_"+i+".txt");
//		metric3.getAreasByPercentage("D:/data/drug-target/results/bi/10folder/line_TBSI_areasByPercentage_"+i+".txt");
		metric3.getRecallByPercentage("D:/data/drug-target/results/bi/"+datatype+"/10folder/line_TBSI_recallByPercentage_"+i+".txt");
		metric3.getRecallByTopN("D:/data/drug-target/results/bi/"+datatype+"/10folder/line_TBSI_recallByTopN_"+i+".txt");
		metric3.getPrecisionByPercentage("D:/data/drug-target/results/bi/"+datatype+"/10folder/line_TBSI_precisionByPercentage_" + i 
				+ ".txt");
		metric3.getPrecisionByTopN("D:/data/drug-target/results/bi/"+datatype+"/10folder/line_TBSI_precisionByTopN_" + i 
				+ ".txt");
		metric3.getArea(bw,"D:/data/drug-target/results/bi/"+datatype+"/10folder/line_TBSI_area_"+i+".txt");
		
		// drug-drug similar find test
		
		bw.flush();
		
	}
	    bw.flush();
	    bw.close();

}

public void external_bi(String datatype)throws Exception {
	
	HashSet<String> allTarget=getAllTarget("data/input/drugbank/drugbank_dump.nt");
	HashSet<String> allDrug=getAllDrug("data/input/drugbank/drugbank_dump.nt");

	BufferedWriter bw = new BufferedWriter(new FileWriter(new File("D:/data/drug-target/results/bi/external/"+datatype+"/line_externalAreas.csv")));
	
    	
	String datafile = "D:/data/drug-target/source/bi.nt";
	String modelfile = "D:/data/drug-target/models/line/bi/external/external_data.nt";
	String removefile = "D:/data/drug-target/input/bi/external/"+datatype+"/external_"+datatype+".nt";
   	
    
	HashMap<String,HashSet<String>> drugToTargetassociations=getDrugTargetAssociations(datafile);
	HashMap<String,HashSet<String>> TargetToDrugassociations=getTargetDrugAssociations(datafile);
	

	// drug-drug similar find test
	
	TargetPrediction predict2=new TargetPrediction(modelfile);
	predict2.feedGold(removefile);
	
	HashSet<String> queries2=predict2.getDrugAsQueries();
	
	HashMap<String,HashMap<String, Double>> results2=predict2.predictBySimilarDrug(queries2, drugToTargetassociations, allDrug,allTarget);	
	
	
	
	EvaluationMetrics metric2=new EvaluationMetrics(results2, predict2.getGoldmap());
//	metric2.getAreasByTopN("D:/data/drug-target/results/bi/10folder/line_DBSI_areasByTopN_"+i+".txt");
//	metric2.getAreasByPercentage("D:/data/drug-target/results/bi/10folder/line_DBSI_areasByPercentage_"+i+".txt");
	metric2.getRecallByPercentage("D:/data/drug-target/results/bi/external/"+datatype+"/line_DBSI_recallByPercentage.txt");
	metric2.getRecallByTopN("D:/data/drug-target/results/bi/external/"+datatype+"/line_DBSI_recallByTopN.txt");
	metric2.getPrecisionByPercentage("D:/data/drug-target/results/bi/external/"+datatype+"/line_DBSI_precisionByPercentage.txt");
	metric2.getPrecisionByTopN("D:/data/drug-target/results/bi/external/"+datatype+"/line_DBSI_precisionByTopN.txt");
	metric2.getArea(bw,"D:/data/drug-target/results/bi/external/"+datatype+"/line_DBSI_area.txt");
	
	DrugPrediction predict3=new DrugPrediction(modelfile);
	predict3.feedGold(removefile);
	
	HashSet<String> queries3=predict3.getTargetAsQueries();
	
	HashMap<String,HashMap<String, Double>> results3=predict3.predictBySimilarTarget(queries3, TargetToDrugassociations,allDrug,allTarget);	
	
	EvaluationMetrics metric3=new EvaluationMetrics(results3, predict3.getGoldmap());
//	metric3.getAreasByTopN("D:/data/drug-target/results/bi/10folder/line_TBSI_areasByTopN_"+i+".txt");
//	metric3.getAreasByPercentage("D:/data/drug-target/results/bi/10folder/line_TBSI_areasByPercentage_"+i+".txt");
	metric3.getRecallByPercentage("D:/data/drug-target/results/bi/external/"+datatype+"/line_TBSI_recallByPercentage.txt");
	metric3.getRecallByTopN("D:/data/drug-target/results/bi/external/"+datatype+"/line_TBSI_recallByTopN.txt");
	metric3.getPrecisionByPercentage("D:/data/drug-target/results/bi/external/"+datatype+"/line_TBSI_precisionByPercentage.txt");
	metric3.getPrecisionByTopN("D:/data/drug-target/results/bi/external/"+datatype+"/line_TBSI_precisionByTopN.txt");
	metric3.getArea(bw,"D:/data/drug-target/results/bi/external/"+datatype+"/line_TBSI_area.txt");
	
	// drug-drug similar find test
	
	
    bw.flush();
    bw.close();

}


public void external_tri(String datatype)throws Exception {
	
	HashSet<String> allTarget=getAllTarget("data/input/drugbank/drugbank_dump.nt");
	HashSet<String> allDrug=getAllDrug("data/input/drugbank/drugbank_dump.nt");

	BufferedWriter bw = new BufferedWriter(new FileWriter(new File("D:/data/drug-target/results/tri/external/"+datatype+"/line_externalAreas.csv")));
	
    	
	String datafile = "D:/data/drug-target/source/tri.nt";
	String modelfile = "D:/data/drug-target/models/line/tri/external/external_data.nt";
	String removefile = "D:/data/drug-target/input/tri/external/"+datatype+"/external_"+datatype+".nt";
   	
    
	HashMap<String,HashSet<String>> drugToTargetassociations=getDrugTargetAssociations(datafile);
	HashMap<String,HashSet<String>> TargetToDrugassociations=getTargetDrugAssociations(datafile);
	

	// drug-drug similar find test
	
	TargetPrediction predict2=new TargetPrediction(modelfile);
	predict2.feedGold(removefile);
	
	HashSet<String> queries2=predict2.getDrugAsQueries();
	
	HashMap<String,HashMap<String, Double>> results2=predict2.predictBySimilarDrug(queries2, drugToTargetassociations, allDrug,allTarget);	
	
	
	
	EvaluationMetrics metric2=new EvaluationMetrics(results2, predict2.getGoldmap());
//	metric2.getAreasByTopN("D:/data/drug-target/results/bi/10folder/line_DBSI_areasByTopN_"+i+".txt");
//	metric2.getAreasByPercentage("D:/data/drug-target/results/bi/10folder/line_DBSI_areasByPercentage_"+i+".txt");
	metric2.getRecallByPercentage("D:/data/drug-target/results/tri/external/"+datatype+"/line_DBSI_recallByPercentage.txt");
	metric2.getRecallByTopN("D:/data/drug-target/results/tri/external/"+datatype+"/line_DBSI_recallByTopN.txt");
	metric2.getPrecisionByPercentage("D:/data/drug-target/results/tri/external/"+datatype+"/line_DBSI_precisionByPercentage.txt");
	metric2.getPrecisionByTopN("D:/data/drug-target/results/tri/external/"+datatype+"/line_DBSI_precisionByTopN.txt");
	metric2.getArea(bw,"D:/data/drug-target/results/tri/external/"+datatype+"/line_DBSI_area.txt");
	
	DrugPrediction predict3=new DrugPrediction(modelfile);
	predict3.feedGold(removefile);
	
	HashSet<String> queries3=predict3.getTargetAsQueries();
	
	HashMap<String,HashMap<String, Double>> results3=predict3.predictBySimilarTarget(queries3, TargetToDrugassociations,allDrug,allTarget);	
	
	EvaluationMetrics metric3=new EvaluationMetrics(results3, predict3.getGoldmap());
//	metric3.getAreasByTopN("D:/data/drug-target/results/bi/10folder/line_TBSI_areasByTopN_"+i+".txt");
//	metric3.getAreasByPercentage("D:/data/drug-target/results/bi/10folder/line_TBSI_areasByPercentage_"+i+".txt");
	metric3.getRecallByPercentage("D:/data/drug-target/results/tri/external/"+datatype+"/line_TBSI_recallByPercentage.txt");
	metric3.getRecallByTopN("D:/data/drug-target/results/tri/external/"+datatype+"/line_TBSI_recallByTopN.txt");
	metric3.getPrecisionByPercentage("D:/data/drug-target/results/tri/external/"+datatype+"/line_TBSI_precisionByPercentage.txt");
	metric3.getPrecisionByTopN("D:/data/drug-target/results/tri/external/"+datatype+"/line_TBSI_precisionByTopN.txt");
	metric3.getArea(bw,"D:/data/drug-target/results/tri/external/"+datatype+"/line_TBSI_area.txt");
	
	// drug-drug similar find test
	
	
    bw.flush();
    bw.close();

}
	
public void monteCarlo_tri(String datatype)throws Exception {
		
		HashSet<String> allTarget=getAllTarget("data/input/drugbank/drugbank_dump.nt");
		HashSet<String> allDrug=getAllDrug("data/input/drugbank/drugbank_dump.nt");
	
		BufferedWriter bw = new BufferedWriter(new FileWriter(new File("D:/data/drug-target/results/tri/"+datatype+"/monteCarlo/line_tri_monteCarloAreas.csv")));
		
	    for (int i = 1; i < 11; i++) {
	    	
	    	String datafile = "D:/data/drug-target/input/tri/"+datatype+"/monteCarlo/tri_" + i*100 + "_data.nt";
			String modelfile = "D:/data/drug-target/models/line/tri/"+datatype+"/monteCarlo/line_tri_" + i*100 + "_100.txt";
			String removefile = "D:/data/drug-target/input/tri/"+datatype+"/monteCarlo/tri_" + i*100 + "_test.nt";
			
	    
		HashMap<String,HashSet<String>> drugToTargetassociations=getDrugTargetAssociations(datafile);
		HashMap<String,HashSet<String>> TargetToDrugassociations=getTargetDrugAssociations(datafile);
    	

		
		// drug-drug similar find test
		
		TargetPrediction predict2=new TargetPrediction(modelfile);
		predict2.feedGold(removefile);
		
		HashSet<String> queries2=predict2.getDrugAsQueries();
		
		HashMap<String,HashMap<String, Double>> results2=predict2.predictBySimilarDrug(queries2, drugToTargetassociations, allDrug,allTarget);	
		
		
		
		EvaluationMetrics metric2=new EvaluationMetrics(results2, predict2.getGoldmap());
//		metric2.getAreasByTopN("D:/data/drug-target/results/bi/10folder/line_DBSI_areasByTopN_"+i+".txt");
//		metric2.getAreasByPercentage("D:/data/drug-target/results/bi/10folder/line_DBSI_areasByPercentage_"+i+".txt");
		metric2.getRecallByPercentage("D:/data/drug-target/results/tri/"+datatype+"/monteCarlo/line_DBSI_recallByPercentage_"+i*100+".txt");
		metric2.getRecallByTopN("D:/data/drug-target/results/tri/"+datatype+"/monteCarlo/line_DBSI_recallByTopN_"+i*100+".txt");
		metric2.getPrecisionByPercentage("D:/data/drug-target/results/tri/"+datatype+"/monteCarlo/line_DBSI_precisionByPercentage_" + i*100 
				+ ".txt");
		metric2.getPrecisionByTopN("D:/data/drug-target/results/tri/"+datatype+"/monteCarlo/line_DBSI_precisionByTopN_" + i*100 
				+ ".txt");
		metric2.getArea(bw,"D:/data/drug-target/results/tri/"+datatype+"/monteCarlo/line_DBSI_area_"+i*100+".txt");
		
		DrugPrediction predict3=new DrugPrediction(modelfile);
		predict3.feedGold(removefile);
		
		HashSet<String> queries3=predict3.getTargetAsQueries();
		
		HashMap<String,HashMap<String, Double>> results3=predict3.predictBySimilarTarget(queries3, TargetToDrugassociations,allDrug,allTarget);	
		
		EvaluationMetrics metric3=new EvaluationMetrics(results3, predict3.getGoldmap());
//		metric3.getAreasByTopN("D:/data/drug-target/results/bi/10folder/line_TBSI_areasByTopN_"+i+".txt");
//		metric3.getAreasByPercentage("D:/data/drug-target/results/bi/10folder/line_TBSI_areasByPercentage_"+i+".txt");
		metric3.getRecallByPercentage("D:/data/drug-target/results/tri/"+datatype+"/monteCarlo/line_TBSI_recallByPercentage_"+i*100+".txt");
		metric3.getRecallByTopN("D:/data/drug-target/results/tri/"+datatype+"/monteCarlo/line_TBSI_recallByTopN_"+i*100+".txt");
		metric3.getPrecisionByPercentage("D:/data/drug-target/results/tri/"+datatype+"/monteCarlo/line_TBSI_precisionByPercentage_" + i*100 
				+ ".txt");
		metric3.getPrecisionByTopN("D:/data/drug-target/results/tri/"+datatype+"/monteCarlo/line_TBSI_precisionByTopN_" + i*100 
				+ ".txt");
		metric3.getArea(bw,"D:/data/drug-target/results/tri/"+datatype+"/monteCarlo/line_TBSI_area_"+i*100+".txt");
		
		bw.flush();
		
	}
	    bw.flush();
	    bw.close();

}

public void monteCarlo_bi(String datatype)throws Exception {
	
	HashSet<String> allTarget=getAllTarget("data/input/drugbank/drugbank_dump.nt");
	HashSet<String> allDrug=getAllDrug("data/input/drugbank/drugbank_dump.nt");

	BufferedWriter bw = new BufferedWriter(new FileWriter(new File("D:/data/drug-target/results/bi/"+datatype+"/monteCarlo/line_bi_monteCarloAreas.csv")));
	
    for (int i = 1; i < 11; i++) {
    	
    	String datafile = "D:/data/drug-target/input/bi/"+datatype+"/monteCarlo/bi_" + i*100 + "_data.nt";
		String modelfile = "D:/data/drug-target/models/line/bi/"+datatype+"/monteCarlo/line_bi_" + i*100 + "_100.txt";
		String removefile = "D:/data/drug-target/input/bi/"+datatype+"/monteCarlo/bi_" + i*100 + "_test.nt";
		
    
	HashMap<String,HashSet<String>> drugToTargetassociations=getDrugTargetAssociations(datafile);
	HashMap<String,HashSet<String>> TargetToDrugassociations=getTargetDrugAssociations(datafile);
	

	
	// drug-drug similar find test
	
	TargetPrediction predict2=new TargetPrediction(modelfile);
	predict2.feedGold(removefile);
	
	HashSet<String> queries2=predict2.getDrugAsQueries();
	
	HashMap<String,HashMap<String, Double>> results2=predict2.predictBySimilarDrug(queries2, drugToTargetassociations, allDrug,allTarget);	
	
	
	
	EvaluationMetrics metric2=new EvaluationMetrics(results2, predict2.getGoldmap());
//	metric2.getAreasByTopN("D:/data/drug-target/results/bi/10folder/line_DBSI_areasByTopN_"+i+".txt");
//	metric2.getAreasByPercentage("D:/data/drug-target/results/bi/10folder/line_DBSI_areasByPercentage_"+i+".txt");
	metric2.getRecallByPercentage("D:/data/drug-target/results/bi/"+datatype+"/monteCarlo/line_DBSI_recallByPercentage_"+i*100+".txt");
	metric2.getRecallByTopN("D:/data/drug-target/results/bi/"+datatype+"/monteCarlo/line_DBSI_recallByTopN_"+i*100+".txt");
	metric2.getPrecisionByPercentage("D:/data/drug-target/results/bi/"+datatype+"/monteCarlo/line_DBSI_precisionByPercentage_" + i*100 
			+ ".txt");
	metric2.getPrecisionByTopN("D:/data/drug-target/results/bi/"+datatype+"/monteCarlo/line_DBSI_precisionByTopN_" + i*100 
			+ ".txt");
	metric2.getArea(bw,"D:/data/drug-target/results/bi/"+datatype+"/monteCarlo/line_DBSI_area_"+i*100+".txt");
	
	DrugPrediction predict3=new DrugPrediction(modelfile);
	predict3.feedGold(removefile);
	
	HashSet<String> queries3=predict3.getTargetAsQueries();
	
	HashMap<String,HashMap<String, Double>> results3=predict3.predictBySimilarTarget(queries3, TargetToDrugassociations,allDrug,allTarget);	
	
	EvaluationMetrics metric3=new EvaluationMetrics(results3, predict3.getGoldmap());
//	metric3.getAreasByTopN("D:/data/drug-target/results/bi/10folder/line_TBSI_areasByTopN_"+i+".txt");
//	metric3.getAreasByPercentage("D:/data/drug-target/results/bi/10folder/line_TBSI_areasByPercentage_"+i+".txt");
	metric3.getRecallByPercentage("D:/data/drug-target/results/bi/"+datatype+"/monteCarlo/line_TBSI_recallByPercentage_"+i*100+".txt");
	metric3.getRecallByTopN("D:/data/drug-target/results/bi/"+datatype+"/monteCarlo/line_TBSI_recallByTopN_"+i*100+".txt");
	metric3.getPrecisionByPercentage("D:/data/drug-target/results/bi/"+datatype+"/monteCarlo/line_TBSI_precisionByPercentage_" + i*100 
			+ ".txt");
	metric3.getPrecisionByTopN("D:/data/drug-target/results/bi/"+datatype+"/monteCarlo/line_TBSI_precisionByTopN_" + i*100 
			+ ".txt");
	metric3.getArea(bw,"D:/data/drug-target/results/bi/"+datatype+"/monteCarlo/line_TBSI_area_"+i*100+".txt");
	
	bw.flush();
	
}
    bw.flush();
    bw.close();

}

	public void external_bi_area()throws Exception {
		
		HashSet<String> allTarget=getAllTarget("data/input/drugbank/drugbank_dump.nt");
		HashSet<String> allDrug=getAllDrug("data/input/drugbank/drugbank_dump.nt");
	
		String datafile="data/input/drugCloud/data_bi.nt";
		String modelfile="outsource/windows/experiment0630/models/line_bi_100.txt";
		String externalfile="data/input/drugCloud/drugbank_predict_4.nt";
		BufferedWriter bw =new BufferedWriter(new FileWriter(new File("data/output/experiment0630/line_bi_areas_external.txt")));

		HashMap<String,HashSet<String>> drugToTargetassociations=getDrugTargetAssociations(datafile);
		HashMap<String,HashSet<String>> TargetToDrugassociations=getTargetDrugAssociations(datafile);
    	
//		String datafile="data/input/drugCloud/data_bi_"+i*100+".nt";
//    	String modelfile="data/input/line/models/line_bi_"+i*100+"_500.txt";
//    	String removefile="data/input/drugCloud/removed_bi_"+i*100+".nt"; 	
    	
		
    	TargetPrediction predict1=new TargetPrediction(modelfile);
		predict1.feedGold(externalfile);
		
		HashMap<String,HashSet<String>> queries1=predict1.getTargetAsQueries(datafile);
		
		HashMap<String,HashMap<String, Double>> results1=predict1.predictBySimilarTarget(queries1,drugToTargetassociations, allTarget);
		
		EvaluationMetrics metric1=new EvaluationMetrics(results1, predict1.getGoldmap());
		
		metric1.writeArea("predictTargetByTarget_external",bw);
		// drug-drug similar find test
		
		
		TargetPrediction predict2=new TargetPrediction(modelfile);
		predict2.feedGold(externalfile);
		
		HashSet<String> queries2=predict2.getDrugAsQueries();
		
		HashMap<String,HashMap<String, Double>> results2=predict2.predictBySimilarDrug(queries2, drugToTargetassociations, allDrug,allTarget);	
		
		
		
		EvaluationMetrics metric2=new EvaluationMetrics(results2, predict2.getGoldmap());
		metric2.writeArea("predictTargetByDrug_external",bw);
		
		DrugPrediction predict3=new DrugPrediction(modelfile);
		predict3.feedGold(externalfile);
		
		HashSet<String> queries3=predict3.getTargetAsQueries();
		
		HashMap<String,HashMap<String, Double>> results3=predict3.predictBySimilarTarget(queries3, TargetToDrugassociations,allDrug,allTarget);	
		
		EvaluationMetrics metric3=new EvaluationMetrics(results3, predict3.getGoldmap());
		metric3.writeArea("predictDrugByTarget_external",bw);
		
		// drug-drug similar find test
		
		DrugPrediction predict4=new DrugPrediction(modelfile);
		predict4.feedGold(externalfile);
		
		HashMap<String,HashSet<String>> queries4=predict4.getDrugAsQueries(datafile);
		
		HashMap<String,HashMap<String, Double>> results4=predict4.predictBySimilarDrug(queries4, TargetToDrugassociations,allDrug);
	
		
		EvaluationMetrics metric4=new EvaluationMetrics(results4, predict4.getGoldmap());
		metric4.writeArea("predictDrugByDrug_external",bw);


}
	
public void internal_bi_area()throws Exception {
		
		HashSet<String> allTarget=getAllTarget("data/input/drugbank/drugbank_dump.nt");
    	HashSet<String> allDrug=getAllDrug("data/input/drugbank/drugbank_dump.nt");
    	BufferedWriter bw =new BufferedWriter(new FileWriter(new File("data/output/experiment0630/line_bi_areas_internal.txt")));
    	
    	    for (int i = 1; i < 11; i++) {
    		String datafile="data/input/drugCloud/data_bi_"+i*100+".nt";
        	String modelfile="outsource/windows/experiment0630/models/line_bi_"+i*100+"_100.txt";
        	String removefile="data/input/drugCloud/removed_bi_"+i*100+".nt";
    		HashMap<String,HashSet<String>> drugToTargetassociations=getDrugTargetAssociations(datafile);
    		HashMap<String,HashSet<String>> TargetToDrugassociations=getTargetDrugAssociations(datafile);
    		
        	TargetPrediction predict1=new TargetPrediction(modelfile);
    		predict1.feedGold(removefile);
    		
    		HashMap<String,HashSet<String>> queries1=predict1.getTargetAsQueries(datafile);
    		
    		HashMap<String,HashMap<String, Double>> results1=predict1.predictBySimilarTarget(queries1,drugToTargetassociations, allTarget);
    		
    		EvaluationMetrics metric1=new EvaluationMetrics(results1, predict1.getGoldmap());
    		
    		metric1.writeArea("predictTargetByTarget_"+i*100,bw);
    		// drug-drug similar find test
    		
    		
    		TargetPrediction predict2=new TargetPrediction(modelfile);
    		predict2.feedGold(removefile);
    		
    		HashSet<String> queries2=predict2.getDrugAsQueries();
    		
    		HashMap<String,HashMap<String, Double>> results2=predict2.predictBySimilarDrug(queries2, drugToTargetassociations, allDrug,allTarget);	
    		
    		
    		
    		EvaluationMetrics metric2=new EvaluationMetrics(results2, predict2.getGoldmap());
    		metric2.writeArea("predictTargetByDrug_"+i*100,bw);
    		
    		DrugPrediction predict3=new DrugPrediction(modelfile);
    		predict3.feedGold(removefile);
    		
    		HashSet<String> queries3=predict3.getTargetAsQueries();
    		
    		HashMap<String,HashMap<String, Double>> results3=predict3.predictBySimilarTarget(queries3, TargetToDrugassociations,allDrug,allTarget);	
    		
    		EvaluationMetrics metric3=new EvaluationMetrics(results3, predict3.getGoldmap());
    		metric3.writeArea("predictDrugByTarget_"+i*100,bw);
    		
    		// drug-drug similar find test
    		
    		DrugPrediction predict4=new DrugPrediction(modelfile);
    		predict4.feedGold(removefile);
    		
    		HashMap<String,HashSet<String>> queries4=predict4.getDrugAsQueries(datafile);
    		
    		HashMap<String,HashMap<String, Double>> results4=predict4.predictBySimilarDrug(queries4, TargetToDrugassociations,allDrug);
    	
    		
    		EvaluationMetrics metric4=new EvaluationMetrics(results4, predict4.getGoldmap());
    		metric4.writeArea("predictDrugByDrug_"+i*100,bw);
		}
    

	}
	
	public void external_tri()throws Exception {
		
		HashSet<String> allTarget=getAllTarget("data/input/drugbank/drugbank_dump.nt");
		HashSet<String> allDrug=getAllDrug("data/input/drugbank/drugbank_dump.nt");
	
		String datafile="data/input/drugCloud/data_tri.nt";
		String modelfile="outsource/windows/experiment0630/models/line_tri_100.txt";
		String externalfile="data/input/drugCloud/drugbank_predict_4.nt";
		
    	
    		HashMap<String,HashSet<String>> drugToTargetassociations=getDrugTargetAssociations(datafile);
    		HashMap<String,HashSet<String>> TargetToDrugassociations=getTargetDrugAssociations(datafile);
        	
//    		String datafile="data/input/drugCloud/data_bi_"+i*100+".nt";
//        	String modelfile="data/input/line/models/line_bi_"+i*100+"_500.txt";
//        	String removefile="data/input/drugCloud/removed_bi_"+i*100+".nt"; 	
        	
    		
        	TargetPrediction predict1=new TargetPrediction(modelfile);
    		predict1.feedGold(externalfile);
    		
    		HashMap<String,HashSet<String>> queries1=predict1.getTargetAsQueries(datafile);
    		
    		HashMap<String,HashMap<String, Double>> results1=predict1.predictBySimilarTarget(queries1,drugToTargetassociations, allTarget);
    		
    		EvaluationMetrics metric1=new EvaluationMetrics(results1, predict1.getGoldmap());
    		metric1.getAreasByTopN("data/output/experiment_tri/line_predictTargetByTarget_getAreasByTopN_"+"external"+".txt");
    		metric1.getAreasByPercentage("data/output/experiment_tri/line_predictTargetByTarget_getAreasByPercentage_"+"external"+".txt");
    		metric1.getRecallByPercentage("data/output/experiment_tri/line_predictTargetByTarget_getRecallByPercentage_"+"external"+".txt");
    		metric1.getRecallByTopN("data/output/experiment_tri/line_predictTargetByTarget_getRecallByTopN_"+"external"+".txt");
    		metric1.getArea("data/output/experiment_tri/line_predictTargetByTarget_getArea_"+"external"+".txt");
    		
    		// drug-drug similar find test
    		
    		
    		TargetPrediction predict2=new TargetPrediction(modelfile);
    		predict2.feedGold(externalfile);
    		
    		HashSet<String> queries2=predict2.getDrugAsQueries();
    		
    		HashMap<String,HashMap<String, Double>> results2=predict2.predictBySimilarDrug(queries2, drugToTargetassociations, allDrug,allTarget);	
    		
    		
    		
    		EvaluationMetrics metric2=new EvaluationMetrics(results2, predict2.getGoldmap());
    		metric2.getAreasByTopN("data/output/experiment_tri/line_predictTargetByDrug_getAreasByTopN_"+"external"+".txt");
    		metric2.getAreasByPercentage("data/output/experiment_tri/line_predictTargetByDrug_getAreasByPercentage_"+"external"+".txt");
    		metric2.getRecallByPercentage("data/output/experiment_tri/line_predictTargetByDrug_getRecallByPercentage_"+"external"+".txt");
    		metric2.getRecallByTopN("data/output/experiment_tri/line_predictTargetByDrug_getRecallByTopN_"+"external"+".txt");
    		metric2.getArea("data/output/experiment_tri/line_predictTargetByDrug_getArea_"+"external"+".txt");
    		
    		DrugPrediction predict3=new DrugPrediction(modelfile);
    		predict3.feedGold(externalfile);
    		
    		HashSet<String> queries3=predict3.getTargetAsQueries();
    		
    		HashMap<String,HashMap<String, Double>> results3=predict3.predictBySimilarTarget(queries3, TargetToDrugassociations,allDrug,allTarget);	
    		
    		EvaluationMetrics metric3=new EvaluationMetrics(results3, predict3.getGoldmap());
    		metric3.getAreasByTopN("data/output/experiment_tri/line_predictDrugByTarget_getAreasByTopN_"+"external"+".txt");
    		metric3.getAreasByPercentage("data/output/experiment_tri/line_predictDrugByTarget_getAreasByPercentage_"+"external"+".txt");
    		metric3.getRecallByPercentage("data/output/experiment_tri/line_predictDrugByTarget_getRecallByPercentage_"+"external"+".txt");
    		metric3.getRecallByTopN("data/output/experiment_tri/line_predictDrugByTarget_getRecallByTopN_"+"external"+".txt");
    		metric3.getArea("data/output/experiment_tri/line_predictDrugByTarget_getArea_"+"external"+".txt");
    		
    		// drug-drug similar find test
    		
    		DrugPrediction predict4=new DrugPrediction(modelfile);
    		predict4.feedGold(externalfile);
    		
    		HashMap<String,HashSet<String>> queries4=predict4.getDrugAsQueries(datafile);
    		
    		HashMap<String,HashMap<String, Double>> results4=predict4.predictBySimilarDrug(queries4, TargetToDrugassociations,allDrug);
    	
    		
    		EvaluationMetrics metric4=new EvaluationMetrics(results4, predict4.getGoldmap());
    		metric4.getAreasByTopN("data/output/experiment_tri/line_predictDrugByDrug_getAreasByTopN_"+"external"+".txt");
    		metric4.getAreasByPercentage("data/output/experiment_tri/line_predictDrugByDrug_getAreasByPercentage_"+"external"+".txt");
    		metric4.getRecallByPercentage("data/output/experiment_tri/line_predictDrugByDrug_getRecallByPercentage_"+"external"+".txt");
    		metric4.getRecallByTopN("data/output/experiment_tri/line_predictDrugByDrug_getRecallByTopN_"+"external"+".txt");
    		metric4.getArea("data/output/experiment_tri/line_predictDrugByDrug_getArea_"+"external"+".txt");

	}
	
	public void external_tri_area()throws Exception {
		
		HashSet<String> allTarget=getAllTarget("data/input/drugbank/drugbank_dump.nt");
		HashSet<String> allDrug=getAllDrug("data/input/drugbank/drugbank_dump.nt");
		BufferedWriter bw =new BufferedWriter(new FileWriter(new File("data/output/experiment_tri/line_tri_areas_external.txt")));
		String datafile="data/input/drugCloud/data_tri.nt";
		String modelfile="outsource/windows/experiment0630/models/line_tri_100.txt";
		String externalfile="data/input/drugCloud/drugbank_predict_4.nt";
		
    	
    		HashMap<String,HashSet<String>> drugToTargetassociations=getDrugTargetAssociations(datafile);
    		HashMap<String,HashSet<String>> TargetToDrugassociations=getTargetDrugAssociations(datafile);
        	
//    		String datafile="data/input/drugCloud/data_bi_"+i*100+".nt";
//        	String modelfile="data/input/line/models/line_bi_"+i*100+"_500.txt";
//        	String removefile="data/input/drugCloud/removed_bi_"+i*100+".nt"; 	
        	
    		
        	TargetPrediction predict1=new TargetPrediction(modelfile);
    		predict1.feedGold(externalfile);
    		
    		HashMap<String,HashSet<String>> queries1=predict1.getTargetAsQueries(datafile);
    		
    		HashMap<String,HashMap<String, Double>> results1=predict1.predictBySimilarTarget(queries1,drugToTargetassociations, allTarget);
    		
    		EvaluationMetrics metric1=new EvaluationMetrics(results1, predict1.getGoldmap());
    		metric1.writeArea("predictTargetByTarget_external", bw);
    		
    		// drug-drug similar find test
    		
    		
    		TargetPrediction predict2=new TargetPrediction(modelfile);
    		predict2.feedGold(externalfile);
    		
    		HashSet<String> queries2=predict2.getDrugAsQueries();
    		
    		HashMap<String,HashMap<String, Double>> results2=predict2.predictBySimilarDrug(queries2, drugToTargetassociations, allDrug,allTarget);	
    		
    		
    		
    		EvaluationMetrics metric2=new EvaluationMetrics(results2, predict2.getGoldmap());
    		metric2.writeArea("predictTargetByDrug_external", bw);
    		
    		DrugPrediction predict3=new DrugPrediction(modelfile);
    		predict3.feedGold(externalfile);
    		
    		HashSet<String> queries3=predict3.getTargetAsQueries();
    		
    		HashMap<String,HashMap<String, Double>> results3=predict3.predictBySimilarTarget(queries3, TargetToDrugassociations,allDrug,allTarget);	
    		
    		EvaluationMetrics metric3=new EvaluationMetrics(results3, predict3.getGoldmap());
    		metric3.writeArea("predictDrugByTarget_external", bw);
    		
    		// drug-drug similar find test
    		
    		DrugPrediction predict4=new DrugPrediction(modelfile);
    		predict4.feedGold(externalfile);
    		
    		HashMap<String,HashSet<String>> queries4=predict4.getDrugAsQueries(datafile);
    		
    		HashMap<String,HashMap<String, Double>> results4=predict4.predictBySimilarDrug(queries4, TargetToDrugassociations,allDrug);
    	
    		
    		EvaluationMetrics metric4=new EvaluationMetrics(results4, predict4.getGoldmap());
    		metric4.writeArea("predictDrugByDrug_external", bw);

	}

public void internal_tri_area()throws Exception {
		
		HashSet<String> allTarget=getAllTarget("data/input/drugbank/drugbank_dump.nt");
    	HashSet<String> allDrug=getAllDrug("data/input/drugbank/drugbank_dump.nt");
    	BufferedWriter bw =new BufferedWriter(new FileWriter(new File("data/output/experiment_tri/line_tri_areas_internal.txt")));
    	
    	    for (int i = 1; i < 11; i++) {
    		String datafile="data/input/drugCloud/data_tri_"+i*100+".nt";
        	String modelfile="outsource/windows/experiment0630/models/line_tri_"+i*100+"_100.txt";
        	String removefile="data/input/drugCloud/removed_bi_"+i*100+".nt";
    		HashMap<String,HashSet<String>> drugToTargetassociations=getDrugTargetAssociations(datafile);
    		HashMap<String,HashSet<String>> TargetToDrugassociations=getTargetDrugAssociations(datafile);
        	
//    		String datafile="data/input/drugCloud/data_bi_"+i*100+".nt";
//        	String modelfile="data/input/line/models/line_bi_"+i*100+"_500.txt";
//        	String removefile="data/input/drugCloud/removed_bi_"+i*100+".nt"; 	
        	
    		
        	TargetPrediction predict1=new TargetPrediction(modelfile);
    		predict1.feedGold(removefile);
    		
    		HashMap<String,HashSet<String>> queries1=predict1.getTargetAsQueries(datafile);
    		
    		HashMap<String,HashMap<String, Double>> results1=predict1.predictBySimilarTarget(queries1,drugToTargetassociations, allTarget);
    		
    		EvaluationMetrics metric1=new EvaluationMetrics(results1, predict1.getGoldmap());
    		metric1.writeArea("predictTargetByTarget_"+i*100, bw);
    		
    		// drug-drug similar find test
    		
    		
    		TargetPrediction predict2=new TargetPrediction(modelfile);
    		predict2.feedGold(removefile);
    		
    		HashSet<String> queries2=predict2.getDrugAsQueries();
    		
    		HashMap<String,HashMap<String, Double>> results2=predict2.predictBySimilarDrug(queries2, drugToTargetassociations, allDrug,allTarget);	
    		
    		
    		
    		EvaluationMetrics metric2=new EvaluationMetrics(results2, predict2.getGoldmap());
    		metric2.writeArea("predictTargetByDrug_"+i*100, bw);
    		
    		DrugPrediction predict3=new DrugPrediction(modelfile);
    		predict3.feedGold(removefile);
    		
    		HashSet<String> queries3=predict3.getTargetAsQueries();
    		
    		HashMap<String,HashMap<String, Double>> results3=predict3.predictBySimilarTarget(queries3, TargetToDrugassociations,allDrug,allTarget);	
    		
    		EvaluationMetrics metric3=new EvaluationMetrics(results3, predict3.getGoldmap());
    		metric3.writeArea("predictDrugByTarget_"+i*100, bw);
    		
    		// drug-drug similar find test
    		
    		DrugPrediction predict4=new DrugPrediction(modelfile);
    		predict4.feedGold(removefile);
    		
    		HashMap<String,HashSet<String>> queries4=predict4.getDrugAsQueries(datafile);
    		
    		HashMap<String,HashMap<String, Double>> results4=predict4.predictBySimilarDrug(queries4, TargetToDrugassociations,allDrug);
    	
    		
    		EvaluationMetrics metric4=new EvaluationMetrics(results4, predict4.getGoldmap());
    		metric4.writeArea("predictDrugByDrug_"+i*100, bw);
		}
    

	}


public void internal_tri()throws Exception {
		
		HashSet<String> allTarget=getAllTarget("data/input/drugbank/drugbank_dump.nt");
    	HashSet<String> allDrug=getAllDrug("data/input/drugbank/drugbank_dump.nt");
    	
    	
    	    for (int i = 1; i < 11; i++) {
    		String datafile="data/input/drugCloud/data_tri_"+i*100+".nt";
        	String modelfile="outsource/windows/experiment0630/models/line_tri_"+i*100+"_100.txt";
        	String removefile="data/input/drugCloud/removed_bi_"+i*100+".nt";
    		HashMap<String,HashSet<String>> drugToTargetassociations=getDrugTargetAssociations(datafile);
    		HashMap<String,HashSet<String>> TargetToDrugassociations=getTargetDrugAssociations(datafile);
        	
//    		String datafile="data/input/drugCloud/data_bi_"+i*100+".nt";
//        	String modelfile="data/input/line/models/line_bi_"+i*100+"_500.txt";
//        	String removefile="data/input/drugCloud/removed_bi_"+i*100+".nt"; 	
        	
    		
        	TargetPrediction predict1=new TargetPrediction(modelfile);
    		predict1.feedGold(removefile);
    		
    		HashMap<String,HashSet<String>> queries1=predict1.getTargetAsQueries(datafile);
    		
    		HashMap<String,HashMap<String, Double>> results1=predict1.predictBySimilarTarget(queries1,drugToTargetassociations, allTarget);
    		
    		EvaluationMetrics metric1=new EvaluationMetrics(results1, predict1.getGoldmap());
    		metric1.getAreasByTopN("data/output/experiment_tri/line_predictTargetByTarget_getAreasByTopN_"+i*100+".txt");
    		metric1.getAreasByPercentage("data/output/experiment_tri/line_predictTargetByTarget_getAreasByPercentage_"+i*100+".txt");
    		metric1.getRecallByPercentage("data/output/experiment_tri/line_predictTargetByTarget_getRecallByPercentage_"+i*100+".txt");
    		metric1.getRecallByTopN("data/output/experiment_tri/line_predictTargetByTarget_getRecallByTopN_"+i*100+".txt");
    		metric1.getArea("data/output/experiment_tri/line_predictTargetByTarget_getArea_"+i*100+".txt");
    		
    		// drug-drug similar find test
    		
    		
    		TargetPrediction predict2=new TargetPrediction(modelfile);
    		predict2.feedGold(removefile);
    		
    		HashSet<String> queries2=predict2.getDrugAsQueries();
    		
    		HashMap<String,HashMap<String, Double>> results2=predict2.predictBySimilarDrug(queries2, drugToTargetassociations, allDrug,allTarget);	
    		
    		
    		
    		EvaluationMetrics metric2=new EvaluationMetrics(results2, predict2.getGoldmap());
    		metric2.getAreasByTopN("data/output/experiment_tri/line_predictTargetByDrug_getAreasByTopN_"+i*100+".txt");
    		metric2.getAreasByPercentage("data/output/experiment_tri/line_predictTargetByDrug_getAreasByPercentage_"+i*100+".txt");
    		metric2.getRecallByPercentage("data/output/experiment_tri/line_predictTargetByDrug_getRecallByPercentage_"+i*100+".txt");
    		metric2.getRecallByTopN("data/output/experiment_tri/line_predictTargetByDrug_getRecallByTopN_"+i*100+".txt");
    		metric2.getArea("data/output/experiment_tri/line_predictTargetByDrug_getArea_"+i*100+".txt");
    		
    		DrugPrediction predict3=new DrugPrediction(modelfile);
    		predict3.feedGold(removefile);
    		
    		HashSet<String> queries3=predict3.getTargetAsQueries();
    		
    		HashMap<String,HashMap<String, Double>> results3=predict3.predictBySimilarTarget(queries3, TargetToDrugassociations,allDrug,allTarget);	
    		
    		EvaluationMetrics metric3=new EvaluationMetrics(results3, predict3.getGoldmap());
    		metric3.getAreasByTopN("data/output/experiment_tri/line_predictDrugByTarget_getAreasByTopN_"+i*100+".txt");
    		metric3.getAreasByPercentage("data/output/experiment_tri/line_predictDrugByTarget_getAreasByPercentage_"+i*100+".txt");
    		metric3.getRecallByPercentage("data/output/experiment_tri/line_predictDrugByTarget_getRecallByPercentage_"+i*100+".txt");
    		metric3.getRecallByTopN("data/output/experiment_tri/line_predictDrugByTarget_getRecallByTopN_"+i*100+".txt");
    		metric3.getArea("data/output/experiment_tri/line_predictDrugByTarget_getArea_"+i*100+".txt");
    		
    		// drug-drug similar find test
    		
    		DrugPrediction predict4=new DrugPrediction(modelfile);
    		predict4.feedGold(removefile);
    		
    		HashMap<String,HashSet<String>> queries4=predict4.getDrugAsQueries(datafile);
    		
    		HashMap<String,HashMap<String, Double>> results4=predict4.predictBySimilarDrug(queries4, TargetToDrugassociations,allDrug);
    	
    		
    		EvaluationMetrics metric4=new EvaluationMetrics(results4, predict4.getGoldmap());
    		metric4.getAreasByTopN("data/output/experiment_tri/line_predictDrugByDrug_getAreasByTopN_"+i*100+".txt");
    		metric4.getAreasByPercentage("data/output/experiment_tri/line_predictDrugByDrug_getAreasByPercentage_"+i*100+".txt");
    		metric4.getRecallByPercentage("data/output/experiment_tri/line_predictDrugByDrug_getRecallByPercentage_"+i*100+".txt");
    		metric4.getRecallByTopN("data/output/experiment_tri/line_predictDrugByDrug_getRecallByTopN_"+i*100+".txt");
    		metric4.getArea("data/output/experiment_tri/line_predictDrugByDrug_getArea_"+i*100+".txt");
		}
    

	}
//	public void internalALL()throws Exception {
//		
//        	
////    	BufferedWriter bw =new BufferedWriter(new FileWriter(new File("data/output/experiment0630/prediction_internal_all.txt")));
//        BufferedWriter bw =new BufferedWriter(new FileWriter(new File("experiment/prediction_internal_all.txt")));
//        	
//        HashSet<String> allTarget=getAllTarget("data/input/drugbank/drugbank_dump.nt");
//    	HashSet<String> allDrug=getAllDrug("data/input/drugbank/drugbank_dump.nt");
//    	
////    	for (int i = 1; i < 11; i++) {
//    		int i=1;
////    		String datafile="data/input/drugCloud/data_bi_"+i*100+".nt";
////        	String modelfile="outsource/windows/experiment0630/models/line_bi_"+i*100+"_100.txt";
////        	String removefile="data/input/drugCloud/removed_bi_"+i*100+".nt";
//    		
//        	String datafile="data/input/drugCloud/data_bi_"+i*100+".nt";
//        	String modelfile="data/input/line/models/line_bi_"+i*100+"_500.txt";
//        	String removefile="data/input/drugCloud/removed_bi_"+i*100+".nt";
//        	
//    		TargetPrediction predict1=new TargetPrediction(modelfile);
//    		predict1.feedGold(removefile);
//    		
//    		HashMap<String,HashSet<String>> queries1=predict1.getTargetAsQueries(datafile);
//    		
//    		List<Map.Entry<String, Double>> results1=predict1.predictBySimilarTarget(queries1,allTarget);	
//    		
//    		Double area1=AUCRoc.getArea(results1, predict1.getGoldmap());
//    		
//    		bw.write(i+",predictTargetBySimilarTarget,"+area1+"\n");
//    		System.out.println(i+",predictTargetBySimilarTarget,"+area1);
//    		// drug-drug similar find test
//    		bw.flush();
//    		
//    		TargetPrediction predict2=new TargetPrediction(modelfile);
//    		predict2.feedGold(removefile);
//    		
//    		HashSet<String> queries2=predict2.getDrugAsQueries();
//    		
//    		List<Map.Entry<String, Double>> results2=predict2.predictBySimilarDrug(datafile,queries2, allDrug, allTarget);	
//    		
//    		Double area2=AUCRoc.getArea(results2, predict2.getGoldmap());
//    		
//    		bw.write(i+",predictTargetBySimilarDrug,"+area2+"\n");
//    		System.out.println(i+",predictTargetBySimilarDrug,"+area2);
//    		bw.flush();
//    		
//    		DrugPrediction predict3=new DrugPrediction(modelfile);
//    		predict3.feedGold(removefile);
//    		
//    		HashSet<String> queries3=predict3.getTargetAsQueries();
//    		
//    		List<Map.Entry<String, Double>> results3=predict3.predictBySimilarTarget(datafile,queries3, allDrug, allTarget);	
//    		
//    		Double area3=AUCRoc.getArea(results3, predict3.getGoldmap());
//    		
//    		bw.write(i+",predictDrugBySimilarTarget,"+area3+"\n");
//    		System.out.println(i+",predictDrugBySimilarTarget,"+area3);
//    		bw.flush();
//    		
//    		// drug-drug similar find test
//    		
//    		DrugPrediction predict4=new DrugPrediction(modelfile);
//    		predict4.feedGold(removefile);
//    		
//    		HashMap<String,HashSet<String>> queries4=predict4.getDrugAsQueries(datafile);
//    		
//    		List<Map.Entry<String, Double>> results4=predict4.predictBySimilarDrug(queries4, allTarget);	
//    		
//    		Double area4=AUCRoc.getArea(results4, predict4.getGoldmap());
//    		
//    		bw.write(i+",predictDrugBySimilarDrug,"+area4+"\n");
//    		System.out.println(i+",predictDrugBySimilarDrug,"+area4);
//    		bw.flush();
////		}
//    	
//    	bw.flush();
//    	bw.close();
//
//	}
	
	public void external() throws Exception{
//			BufferedWriter bw =new BufferedWriter(new FileWriter(new File("data/output/experiment0630/prediction_external.txt")));
//    		
//			String datafile="data/input/drugCloud/data_bi.nt";
//        	String modelfile="outsource/windows/experiment0630/models/line_bi_full_100.txt";
//        	String externalfile="data/input/drugCloud/drugbank_predict_4.nt";
        	
		
		 	BufferedWriter bw =new BufferedWriter(new FileWriter(new File("experiment/prediction_external.txt")));
			String datafile="data/input/drugCloud/data_bi.nt";
        	String modelfile="data/input/line/models/line_bi_full_100.txt";
        	String externalfile="data/input/drugCloud/drugbank_predict_4.nt";
		 
        	int topN=10;
    		
        	TargetPrediction predict1=new TargetPrediction(modelfile);
    		predict1.feedGold(externalfile);
    		
    		HashMap<String,HashSet<String>> queries1=predict1.getTargetAsQueries(datafile);
    		
    		HashMap<String,List<Map.Entry<String, Double>>> results1=predict1.predictBySimilarTarget(queries1, topN);	
    		Double recall1=predict1.recall(results1);
    		Double avgAUC1=AUCRoc.getArea(results1, predict1.getGoldmap());
    		Double avgAUC1_1=AUCRoc.getArea1(results1, predict1.getGoldmap());
    		
    		bw.write("full,predictTargetBySimilarTarget,"+","+recall1+","+avgAUC1+","+avgAUC1_1+"\n");
    		System.out.println("full,predictTargetBySimilarTarget,"+recall1+","+avgAUC1+","+avgAUC1_1);
    		// drug-drug similar find test
    		
    		
    		TargetPrediction predict2=new TargetPrediction(modelfile);
    		predict2.feedGold(externalfile);
    		
    		HashSet<String> queries2=predict2.getDrugAsQueries();
    		
    		HashMap<String,List<Map.Entry<String, Double>>> results2=predict2.predictBySimilarDrug(datafile,queries2, topN);	
    		
    		Double recall2=predict2.recall(results2);
    		Double avgAUC2=AUCRoc.getArea(results2, predict2.getGoldmap());
    		Double avgAUC2_2=AUCRoc.getArea1(results2, predict2.getGoldmap());
    		bw.write("full,predictTargetBySimilarDrug,"+recall2+","+ avgAUC2+","+avgAUC2_2+"\n");
    		System.out.println("full,predictTargetBySimilarDrug,"+recall2+","+ avgAUC2+","+avgAUC2_2);
    		
    		
    		
    		DrugPrediction predict3=new DrugPrediction(modelfile);
    		predict3.feedGold(externalfile);
    		
    		
    		HashSet<String> queries3=predict3.getTargetAsQueries();
    		
    		HashMap<String,List<Map.Entry<String, Double>>> results3=predict3.predictBySimilarTarget(datafile,queries3, topN);	
    		Double recall3=predict3.recall(results3);
    		Double avgAUC3=AUCRoc.getArea(results3, predict3.getGoldmap());
    		Double avgAUC3_3=AUCRoc.getArea(results3, predict3.getGoldmap());
    		bw.write("full,predictDrugBySimilarTarget,"+recall3+","+ avgAUC3+","+avgAUC3_3+"\n");
    		System.out.println("full,predictDrugBySimilarTarget,"+recall3+","+ avgAUC3+","+avgAUC3_3);
    		
    		// drug-drug similar find test
    		
    		DrugPrediction predict4=new DrugPrediction(modelfile);
    		predict4.feedGold(externalfile);
    		
    		HashMap<String,HashSet<String>> queries4=predict4.getDrugAsQueries(datafile);
    		
    		HashMap<String,List<Map.Entry<String, Double>>> results4=predict4.predictBySimilarDrug(queries4, topN);	
    		
    		Double recall4=predict4.recall(results4);
    		Double avgAUC4=AUCRoc.getArea(results4, predict4.getGoldmap());
    		Double avgAUC4_4=AUCRoc.getArea(results4, predict4.getGoldmap());
    		bw.write("full,predictDrugBySimilarDrug,"+recall4+","+ avgAUC4+","+avgAUC4_4+"\n");
    		System.out.println("full,predictDrugBySimilarDrug,"+recall4+","+ avgAUC4+","+avgAUC4_4);
    		
    	bw.flush();
    	bw.close();
	}
	
	
//	public void externalAll() throws Exception{
////		BufferedWriter bw =new BufferedWriter(new FileWriter(new File("data/output/experiment0630/prediction_externalAll.txt")));
////		HashSet<String> allTarget=getAllTarget("data/input/drugbank/drugbank_dump.nt");
////    	HashSet<String> allDrug=getAllDrug("data/input/drugbank/drugbank_dump.nt");
////		String datafile="data/input/drugCloud/data_bi.nt";
////    	String modelfile="outsource/windows/experiment0630/models/line_bi_full_500.txt";
////    	String externalfile="data/input/drugCloud/drugbank_predict_4.nt";
//    	
//    	
//     	BufferedWriter bw =new BufferedWriter(new FileWriter(new File("experiment/prediction_external_all.txt")));
//		HashSet<String> allTarget=getAllTarget("data/input/drugbank/drugbank_dump.nt");
//    	HashSet<String> allDrug=getAllDrug("data/input/drugbank/drugbank_dump.nt");
//		String datafile="data/input/drugCloud/data_bi.nt";
//    	String modelfile="data/input/line/models/line_bi_full_100.txt";
//    	String externalfile="data/input/drugCloud/drugbank_predict_4.nt";
//    	
//    	
//		
//    	TargetPrediction predict1=new TargetPrediction(modelfile);
//		predict1.feedGold(externalfile);
//		
//		HashMap<String,HashSet<String>> queries1=predict1.getTargetAsQueries(datafile);
//		
//		List<Map.Entry<String, Double>>  results1=predict1.predictBySimilarTarget(queries1,allTarget);	
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
//		List<Map.Entry<String, Double>> results2=predict2.predictBySimilarDrug(datafile,queries2, allDrug, allTarget);	
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
//		List<Map.Entry<String, Double>> results3=predict3.predictBySimilarTarget(datafile,queries3, allDrug, allTarget);	
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
//		List<Map.Entry<String, Double>> results4=predict4.predictBySimilarDrug(queries4, allTarget);	
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
	
	public HashMap<String,HashSet<String>> getDrugTargetAssociations(String datafile) throws IOException{
		BufferedReader br = new BufferedReader(new FileReader(new File(datafile)));
		HashMap<String,List<Map.Entry<String, Double>>> results=new HashMap<>();
		String line=null;
		HashMap<String,HashSet<String>> associations=new HashMap<>();
		while((line=br.readLine())!=null){
			if(!line.contains("\"")){
				InputStream inputStream = new ByteArrayInputStream(line.getBytes());
				NxParser nxp = new NxParser();
				nxp.parse(inputStream);
				while (nxp.hasNext()) {
				
					Node[] quard = nxp.next();
					String s = quard[0].toString().trim();
					String p = quard[1].toString().trim();
					String o = quard[2].toString().trim();
					if(s.startsWith("<http://www4.wiwiss.fu-berlin.de/drugbank/resource/drugs/")
							&p.equals("<http://www4.wiwiss.fu-berlin.de/drugbank/resource/drugbank/target>")
							&o.startsWith("<http://www4.wiwiss.fu-berlin.de/drugbank/resource/targets/")){
						
							if(associations.containsKey(s)){
								associations.get(s).add(o);
							}else{
								HashSet<String> set= new HashSet<>();
								set.add(o);
								associations.put(s, set);
							}
							
						
					}
				}
			}
		}
		return associations;
	}
	
	public HashMap<String,HashSet<String>> getTargetDrugAssociations(String datafile) throws IOException{
		BufferedReader br = new BufferedReader(new FileReader(new File(datafile)));
		HashMap<String,List<Map.Entry<String, Double>>> results=new HashMap<>();
		String line=null;
		HashMap<String,HashSet<String>> associations=new HashMap<>();
		while((line=br.readLine())!=null){
			if(!line.contains("\"")){
				InputStream inputStream = new ByteArrayInputStream(line.getBytes());
				NxParser nxp = new NxParser();
				nxp.parse(inputStream);
				while (nxp.hasNext()) {
				
					Node[] quard = nxp.next();
					String s = quard[0].toString().trim();
					String p = quard[1].toString().trim();
					String o = quard[2].toString().trim();
					if(s.startsWith("<http://www4.wiwiss.fu-berlin.de/drugbank/resource/drugs/")
							&p.equals("<http://www4.wiwiss.fu-berlin.de/drugbank/resource/drugbank/target>")
							&o.startsWith("<http://www4.wiwiss.fu-berlin.de/drugbank/resource/targets/")){
						
							if(associations.containsKey(o)){
								associations.get(o).add(s);
							}else{
								HashSet<String> set= new HashSet<>();
								set.add(s);
								associations.put(o, set);
							}
							
						
					}
				}
			}
		}
		return associations;
	}

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
	
		
		
//		new LinePrediction().internal();
//		new LinePrediction().internalALL();
//		new LinePrediction().external();
//		new LinePrediction().externalAll();
//		new LinePrediction().internal_bi();
		
    	
	}

}
