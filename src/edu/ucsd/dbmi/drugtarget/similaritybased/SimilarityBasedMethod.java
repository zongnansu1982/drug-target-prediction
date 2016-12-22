package edu.ucsd.dbmi.drugtarget.similaritybased;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;

import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

public class SimilarityBasedMethod {
	
	
	/**
	 * all the similarity metrics, except csi
	 * @param similarityType
	 * @param queries
	 * @param drugTagetAssociation
	 * @param allDrugs
	 * @param allTarget
	 * @return
	 * @throws Exception
	 */
	 public HashMap<String,HashMap<String, Double>> getSimilarTarget(String similarityType, HashMap<String,HashSet<String>> queries, 
	    		HashMap<String,HashSet<String>> drugTargetAssociation, HashSet<String> allDrugs, HashSet<String> allTarget) throws Exception {
	  		// TODO Auto-generated method stub
		 HashMap<String,HashMap<String, Double>> map=new HashMap<>();
		 
		 for(Entry<String,HashSet<String>> entry:queries.entrySet()){
			 HashMap<String, Double> scores= new HashMap<>();
			 for(String query:entry.getValue()){
				 Double sim=0.0;
				 for(String target:allTarget){
					 if(!entry.getValue().contains(target)){
						 	if(similarityType.equals(SimilaritiesPrediction.jaccard)){
								new Similarities();
								sim=Similarities.jaccardSimilarity(drugTargetAssociation.get(query), drugTargetAssociation.get(target));
							}
							if(similarityType.equals(SimilaritiesPrediction.simpson)){
								new Similarities();
								sim=Similarities.simpsonSimilarity(drugTargetAssociation.get(query), drugTargetAssociation.get(target));
							}
							if(similarityType.equals(SimilaritiesPrediction.geometric)){
								new Similarities();
								sim=Similarities.geometricSimilarity(drugTargetAssociation.get(query), drugTargetAssociation.get(target));
							}
							if(similarityType.equals(SimilaritiesPrediction.cosin)){
								new Similarities();
								sim=Similarities.cosinSimilarity(drugTargetAssociation.get(query), drugTargetAssociation.get(target));
							}
							if(similarityType.equals(SimilaritiesPrediction.pcc)){
								new Similarities();
								sim=Similarities.pearsonCorrelationCoefficient(drugTargetAssociation.get(query), drugTargetAssociation.get(target),allDrugs);
							}
							if(similarityType.equals(SimilaritiesPrediction.hypergeometric)){
								new Similarities();
								sim=Similarities.hypergeometricIndex(drugTargetAssociation.get(query), drugTargetAssociation.get(target),allDrugs);
							}
							if(scores.containsKey(target)){
								scores.put(target, scores.get(target)+sim);
							}else{
								scores.put(target,sim);
							}
					 }
				 }
				 
			 }
			 map.put(entry.getKey(), scores);
		 }
	  		return map;
	  	}
	 
	 public HashMap<String,HashMap<String, Double>> getSimilarTarget(String similarityType, HashMap<String,HashSet<String>> queries, 
			 SimpleWeightedGraph<String, DefaultWeightedEdge> pccgraph, HashSet<String> allDrugs, HashSet<String> allTarget) throws Exception {
	  		// TODO Auto-generated method stub
		 HashMap<String,HashMap<String, Double>> map=new HashMap<>();
		 
		 for(Entry<String,HashSet<String>> entry:queries.entrySet()){
			 HashMap<String, Double> scores= new HashMap<>();
			 for(String query:entry.getValue()){
				 Double sim=0.0;
				 for(String target:allTarget){
					 if(!entry.getValue().contains(target)){
						 	if(similarityType.equals(SimilaritiesPrediction.csi)){
								new Similarities();
								sim=Similarities.CSI(pccgraph, query, target, allTarget);
							}
							if(scores.containsKey(target)){
								scores.put(target, scores.get(target)+sim);
							}else{
								scores.put(target,sim);
							}
					 }
				 }
				 
			 }
			 map.put(entry.getKey(), scores);
		 }
	  		return map;
	  	}
	 
	 public HashMap<String,HashMap<String,Double>> getSimilarDrug(String similarityType, HashSet<String> queries, 
			 SimpleWeightedGraph<String, DefaultWeightedEdge> pccgraph, HashSet<String> allDrugs, HashSet<String> allTarget) throws Exception {
			// TODO Auto-generated method stub
		 HashMap<String,HashMap<String,Double>> map=new HashMap<>();
		 for(String query:queries){
			 HashMap<String,Double> scores=new HashMap<>();
			 for(String drug:allDrugs){
				 if(!drug.equals(query)){
					 Double sim=0.0;
					 	if(similarityType.equals(SimilaritiesPrediction.csi)){
							new Similarities();
							sim=Similarities.CSI(pccgraph, query, drug, allDrugs);
						}
						scores.put(drug,sim);
				 }
			 }
			 map.put(query, scores);
		 }
			return map;
		}
	 
	 public HashMap<String,HashMap<String,Double>> getSimilarDrug(String similarityType, HashSet<String> queries, 
	    		HashMap<String,HashSet<String>> drugTargetAssociation, HashSet<String> allDrugs, HashSet<String> allTarget) throws Exception {
			// TODO Auto-generated method stub
		 HashMap<String,HashMap<String,Double>> map=new HashMap<>();
		 for(String query:queries){
			 HashMap<String,Double> scores=new HashMap<>();
			 for(String drug:allDrugs){
				 if(!drug.equals(query)){
					 Double sim=0.0;
					 if(similarityType.equals(SimilaritiesPrediction.jaccard)){
							new Similarities();
							sim=Similarities.jaccardSimilarity(drugTargetAssociation.get(query), drugTargetAssociation.get(drug));
						}
						if(similarityType.equals(SimilaritiesPrediction.simpson)){
							new Similarities();
							sim=Similarities.simpsonSimilarity(drugTargetAssociation.get(query), drugTargetAssociation.get(drug));
						}
						if(similarityType.equals(SimilaritiesPrediction.geometric)){
							new Similarities();
							sim=Similarities.geometricSimilarity(drugTargetAssociation.get(query), drugTargetAssociation.get(drug));
						}
						if(similarityType.equals(SimilaritiesPrediction.cosin)){
							new Similarities();
							sim=Similarities.cosinSimilarity(drugTargetAssociation.get(query), drugTargetAssociation.get(drug));
						}
						if(similarityType.equals(SimilaritiesPrediction.pcc)){
							new Similarities();
							sim=Similarities.pearsonCorrelationCoefficient(drugTargetAssociation.get(query), drugTargetAssociation.get(drug),allTarget);
						}
						if(similarityType.equals(SimilaritiesPrediction.hypergeometric)){
							new Similarities();
							sim=Similarities.hypergeometricIndex(drugTargetAssociation.get(query), drugTargetAssociation.get(drug),allTarget);
						}
						scores.put(drug,sim);
				 }
			 }
			 map.put(query, scores);
		 }
			return map;
		}
	 
	 public HashMap<String,HashMap<String,Double>> getSimilarTarget(String similarityType, HashSet<String> queries, 
			 SimpleWeightedGraph<String, DefaultWeightedEdge> pccgraph, HashSet<String> allDrugs, HashSet<String> allTarget) throws Exception {
			// TODO Auto-generated method stub
		 HashMap<String,HashMap<String,Double>> map=new HashMap<>();
		 for(String query:queries){
			 HashMap<String,Double> scores=new HashMap<>();
			 for(String target:allTarget){
				 if(!target.equals(query)){
					 Double sim=0.0;
					 	if(similarityType.equals(SimilaritiesPrediction.csi)){
							new Similarities();
							sim=Similarities.CSI(pccgraph, query, target, allDrugs);
						}
						scores.put(target,sim);
				 }
			 }
			 map.put(query, scores);
		 }
			return map;
		}
	 
	 public HashMap<String,HashMap<String,Double>> getSimilarTarget(String similarityType, HashSet<String> queries, 
	    		HashMap<String,HashSet<String>> tagetDrugAssociation, HashSet<String> allDrugs, HashSet<String> allTarget) throws Exception {
			// TODO Auto-generated method stub
		 HashMap<String,HashMap<String,Double>> map=new HashMap<>();
		 for(String query:queries){
			 HashMap<String,Double> scores=new HashMap<>();
			 for(String target:allTarget){
				 if(!target.equals(query)){
					 Double sim=0.0;
					 if(similarityType.equals(SimilaritiesPrediction.jaccard)){
							new Similarities();
							sim=Similarities.jaccardSimilarity(tagetDrugAssociation.get(query), tagetDrugAssociation.get(target));
						}
						if(similarityType.equals(SimilaritiesPrediction.simpson)){
							new Similarities();
							sim=Similarities.simpsonSimilarity(tagetDrugAssociation.get(query), tagetDrugAssociation.get(target));
						}
						if(similarityType.equals(SimilaritiesPrediction.geometric)){
							new Similarities();
							sim=Similarities.geometricSimilarity(tagetDrugAssociation.get(query), tagetDrugAssociation.get(target));
						}
						if(similarityType.equals(SimilaritiesPrediction.cosin)){
							new Similarities();
							sim=Similarities.cosinSimilarity(tagetDrugAssociation.get(query), tagetDrugAssociation.get(target));
						}
						if(similarityType.equals(SimilaritiesPrediction.pcc)){
							new Similarities();
							sim=Similarities.pearsonCorrelationCoefficient(tagetDrugAssociation.get(query), tagetDrugAssociation.get(target),allDrugs);
						}
						if(similarityType.equals(SimilaritiesPrediction.hypergeometric)){
							new Similarities();
							sim=Similarities.hypergeometricIndex(tagetDrugAssociation.get(query), tagetDrugAssociation.get(target),allDrugs);
						}
						scores.put(target,sim);
				 }
			 }
			 map.put(query, scores);
		 }
			return map;
		}
	 
	 
	 public HashMap<String,HashMap<String, Double>> getSimilarDrug(String similarityType, HashMap<String,HashSet<String>> queries, 
	    		HashMap<String,HashSet<String>> drugTargetAssociation, HashSet<String> allDrugs, HashSet<String> allTarget) throws Exception {
	  		// TODO Auto-generated method stub
		 HashMap<String,HashMap<String, Double>> map=new HashMap<>();
		 
		 for(Entry<String,HashSet<String>> entry:queries.entrySet()){
			 HashMap<String, Double> scores= new HashMap<>();
			 for(String query:entry.getValue()){
				 Double sim=0.0;
				 for(String drug:allDrugs){
					 if(!entry.getValue().contains(drug)){
						 	if(similarityType.equals(SimilaritiesPrediction.jaccard)){
								new Similarities();
								sim=Similarities.jaccardSimilarity(drugTargetAssociation.get(query), drugTargetAssociation.get(drug));
							}
							if(similarityType.equals(SimilaritiesPrediction.simpson)){
								new Similarities();
								sim=Similarities.simpsonSimilarity(drugTargetAssociation.get(query), drugTargetAssociation.get(drug));
							}
							if(similarityType.equals(SimilaritiesPrediction.geometric)){
								new Similarities();
								sim=Similarities.geometricSimilarity(drugTargetAssociation.get(query), drugTargetAssociation.get(drug));
							}
							if(similarityType.equals(SimilaritiesPrediction.cosin)){
								new Similarities();
								sim=Similarities.cosinSimilarity(drugTargetAssociation.get(query), drugTargetAssociation.get(drug));
							}
							if(similarityType.equals(SimilaritiesPrediction.pcc)){
								new Similarities();
								sim=Similarities.pearsonCorrelationCoefficient(drugTargetAssociation.get(query), drugTargetAssociation.get(drug),allTarget);
							}
							if(similarityType.equals(SimilaritiesPrediction.hypergeometric)){
								new Similarities();
								sim=Similarities.hypergeometricIndex(drugTargetAssociation.get(query), drugTargetAssociation.get(drug),allTarget);
							}
							if(scores.containsKey(drug)){
								scores.put(drug, scores.get(drug)+sim);
							}else{
								scores.put(drug,sim);
							}
					 }
				 }
				 
			 }
			 map.put(entry.getKey(), scores);
		 }
	  		return map;
	  	}
	 
	 public HashMap<String,HashMap<String, Double>> getSimilarDrug(String similarityType, HashMap<String,HashSet<String>> queries, 
			 SimpleWeightedGraph<String, DefaultWeightedEdge> pccgraph, HashSet<String> allDrugs, HashSet<String> allTarget) throws Exception {
	  		// TODO Auto-generated method stub
		 HashMap<String,HashMap<String, Double>> map=new HashMap<>();
		 
		 for(Entry<String,HashSet<String>> entry:queries.entrySet()){
			 HashMap<String, Double> scores= new HashMap<>();
			 for(String query:entry.getValue()){
				 Double sim=0.0;
				 for(String drug:allDrugs){
					 if(!entry.getValue().contains(drug)){
						 	if(similarityType.equals(SimilaritiesPrediction.csi)){
								new Similarities();
								sim=Similarities.CSI(pccgraph, query, drug, allDrugs);
							}
							if(scores.containsKey(drug)){
								scores.put(drug, scores.get(drug)+sim);
							}else{
								scores.put(drug,sim);
							}
					 }
				 }
				 
			 }
			 map.put(entry.getKey(), scores);
		 }
	  		return map;
	  	}
}
