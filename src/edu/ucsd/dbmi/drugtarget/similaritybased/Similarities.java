package edu.ucsd.dbmi.drugtarget.similaritybased;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import org.apache.commons.math3.exception.MathArithmeticException;
import org.apache.commons.math3.util.CombinatoricsUtils;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

public class Similarities {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
//		HashSet<String> set1=new HashSet<>();
//		set1.add("5");
//		set1.add("6");
//		set1.add("7");
//		HashSet<String> set2=new HashSet<>();
//		set2.add("5");
//		set2.add("6");
//		HashSet<String> nodes=new HashSet<>();
//		nodes.add("1");
//		nodes.add("2");
//		nodes.add("3");
//		nodes.add("4");
//		nodes.add("5");
//		nodes.add("6");
//		nodes.add("7");
//		
//		System.out.println("jaccard: "+new Similarities().jaccardSimilarity(set1, set2));
//		System.out.println("simpson: "+new Similarities().simpsonSimilarity(set1, set2));
//		System.out.println("geometric: "+new Similarities().geometricSimilarity(set1, set2));
//		System.out.println("cosin: "+new Similarities().cosinSimilarity(set1, set2));
//		System.out.println("Hypergeometric: "+new Similarities().hypergeometricIndex(set1,set2, nodes));
//		System.out.println("pcc: "+new Similarities().pearsonCorrelationCoefficient(set1, set2, nodes));
		
		
		HashSet<String> set1=new HashSet<>();
		set1.add("d1");
		HashSet<String> set2=new HashSet<>();
		set2.add("d2");
		set2.add("d4");
		HashSet<String> set3=new HashSet<>();
		set3.add("d3");
		set3.add("d1");
		HashSet<String> set4=new HashSet<>();
		set4.add("d3");
		HashSet<String> set5=new HashSet<>();
		set5.add("d2");
		set5.add("d4");
		
		
		HashSet<String> ynodes=new HashSet<>();
		ynodes.add("d1");
		ynodes.add("d2");
		ynodes.add("d3");
		ynodes.add("d4");
		
		HashSet<String> xnodes=new HashSet<>();
		xnodes.add("t1");
		xnodes.add("t2");
		xnodes.add("t3");
		xnodes.add("t4");
		xnodes.add("t5");
		
		HashMap<String,HashSet<String>> data=new HashMap<>();
		data.put("t1", set1);
		data.put("t2", set2);
		data.put("t3", set3);
		data.put("t4", set4);
		data.put("t5", set5);
		
		PCCNetworkGenerate generate=new PCCNetworkGenerate(data, ynodes);
		generate.generate();
		SimpleWeightedGraph<String, DefaultWeightedEdge> pccgraph= generate.getGraph();
		
		new Similarities();
		System.out.println("jaccard: "+Similarities.jaccardSimilarity(set1, set2));
		new Similarities();
		System.out.println("jaccard: "+Similarities.jaccardSimilarity(set1, set4));
		new Similarities();
		System.out.println("jaccard: "+Similarities.jaccardSimilarity(set1, set5));
		new Similarities();
		System.out.println("jaccard: "+Similarities.jaccardSimilarity(set3, set2));
		new Similarities();
		System.out.println("jaccard: "+Similarities.jaccardSimilarity(set3, set4));
		new Similarities();
		System.out.println("jaccard: "+Similarities.jaccardSimilarity(set3, set5));
		

		
		new Similarities();
		System.out.println("simpson: "+Similarities.simpsonSimilarity(set1, set2));
		new Similarities();
		System.out.println("simpson: "+Similarities.simpsonSimilarity(set1, set4));
		new Similarities();
		System.out.println("simpson: "+Similarities.simpsonSimilarity(set1, set5));
		new Similarities();
		System.out.println("simpson: "+Similarities.simpsonSimilarity(set3, set2));
		new Similarities();
		System.out.println("simpson: "+Similarities.simpsonSimilarity(set3, set4));
		new Similarities();
		System.out.println("simpson: "+Similarities.simpsonSimilarity(set3, set5));
		
		new Similarities();
		System.out.println("geometric: "+Similarities.geometricSimilarity(set1, set2));
		new Similarities();
		System.out.println("geometric: "+Similarities.geometricSimilarity(set1, set4));
		new Similarities();
		System.out.println("geometric: "+Similarities.geometricSimilarity(set1, set5));
		new Similarities();
		System.out.println("geometric: "+Similarities.geometricSimilarity(set3, set2));
		new Similarities();
		System.out.println("geometric: "+Similarities.geometricSimilarity(set3, set4));
		new Similarities();
		System.out.println("geometric: "+Similarities.geometricSimilarity(set3, set5));
		
		new Similarities();
		System.out.println("cosin: "+Similarities.cosinSimilarity(set1, set2));
		new Similarities();
		System.out.println("cosin: "+Similarities.cosinSimilarity(set1, set4));
		new Similarities();
		System.out.println("cosin: "+Similarities.cosinSimilarity(set1, set5));
		new Similarities();
		System.out.println("cosin: "+Similarities.cosinSimilarity(set3, set2));
		new Similarities();
		System.out.println("cosin: "+Similarities.cosinSimilarity(set3, set4));
		new Similarities();
		System.out.println("cosin: "+Similarities.cosinSimilarity(set3, set5));
		
		new Similarities();
		System.out.println("Hypergeometric: "+Similarities.hypergeometricIndex(set1,set2, ynodes));
		new Similarities();
		System.out.println("Hypergeometric: "+Similarities.hypergeometricIndex(set1,set4, ynodes));
		new Similarities();
		System.out.println("Hypergeometric: "+Similarities.hypergeometricIndex(set1,set5, ynodes));
		new Similarities();
		System.out.println("Hypergeometric: "+Similarities.hypergeometricIndex(set3,set2, ynodes));
		new Similarities();
		System.out.println("Hypergeometric: "+Similarities.hypergeometricIndex(set3,set4, ynodes));
		new Similarities();
		System.out.println("Hypergeometric: "+Similarities.hypergeometricIndex(set3,set5, ynodes));
		
		new Similarities();
		System.out.println("pcc: "+Similarities.pearsonCorrelationCoefficient(set1, set2, ynodes));
		new Similarities();
		System.out.println("pcc: "+Similarities.pearsonCorrelationCoefficient(set1, set4, ynodes));
		new Similarities();
		System.out.println("pcc: "+Similarities.pearsonCorrelationCoefficient(set1, set5, ynodes));
		new Similarities();
		System.out.println("pcc: "+Similarities.pearsonCorrelationCoefficient(set3, set2, ynodes));
		new Similarities();
		System.out.println("pcc: "+Similarities.pearsonCorrelationCoefficient(set3, set4, ynodes));
		new Similarities();
		System.out.println("pcc: "+Similarities.pearsonCorrelationCoefficient(set3, set5, ynodes));
		
		new Similarities();
		System.out.println("csi: "+Similarities.CSI(pccgraph, "t1", "t2", xnodes));
		new Similarities();
		System.out.println("csi: "+Similarities.CSI(pccgraph, "t1", "t4", xnodes));
		new Similarities();
		System.out.println("csi: "+Similarities.CSI(pccgraph, "t1", "t5", xnodes));
		new Similarities();
		System.out.println("csi: "+Similarities.CSI(pccgraph, "t3", "t2", xnodes));
		new Similarities();
		System.out.println("csi: "+Similarities.CSI(pccgraph, "t3", "t4", xnodes));
		new Similarities();
		System.out.println("csi: "+Similarities.CSI(pccgraph, "t3", "t5", xnodes));
		
		
//		System.out.println("PPC A B: "+new Similarities().pearsonCorrelationCoefficient(set1,set2, nodes));
//		System.out.println("PPC A C: "+new Similarities().pearsonCorrelationCoefficient(set1,set3, nodes));
//		System.out.println("PPC A D: "+new Similarities().pearsonCorrelationCoefficient(set1,set4, nodes));
//		System.out.println("PPC A E: "+new Similarities().pearsonCorrelationCoefficient(set1,set5, nodes));
//		System.out.println("PPC A F: "+new Similarities().pearsonCorrelationCoefficient(set1,set6, nodes));
//		System.out.println("PPC B C: "+new Similarities().pearsonCorrelationCoefficient(set2,set3, nodes));
//		System.out.println("PPC B D: "+new Similarities().pearsonCorrelationCoefficient(set2,set4, nodes));
//		System.out.println("PPC B E: "+new Similarities().pearsonCorrelationCoefficient(set2,set5, nodes));
//		System.out.println("PPC B F: "+new Similarities().pearsonCorrelationCoefficient(set2,set6, nodes));
//		System.out.println("PPC C D: "+new Similarities().pearsonCorrelationCoefficient(set3,set4, nodes));
//		System.out.println("PPC C E: "+new Similarities().pearsonCorrelationCoefficient(set3,set5, nodes));
//		System.out.println("PPC C F: "+new Similarities().pearsonCorrelationCoefficient(set3,set6, nodes));
//		System.out.println("PPC D E: "+new Similarities().pearsonCorrelationCoefficient(set4,set5, nodes));
//		System.out.println("PPC D F: "+new Similarities().pearsonCorrelationCoefficient(set4,set6, nodes));
//		System.out.println("PPC E F: "+new Similarities().pearsonCorrelationCoefficient(set5,set6, nodes));
//		
		
		
	}
	
	
	public static Double jaccardSimilarity(HashSet<String> set1,HashSet<String> set2){
		HashSet<String> tmp1=new HashSet<>();
		HashSet<String> tmp2=new HashSet<>();
		for (String string:set1) {
			tmp1.add(string);
			tmp2.add(string);
		}
		tmp1.retainAll(set2);
		tmp2.addAll(set2);
		
		return (double)tmp1.size()/tmp2.size();
	}
	
	public static Double simpsonSimilarity(HashSet<String> set1,HashSet<String> set2){
		HashSet<String> tmp1=new HashSet<>();
		for (String string:set1) {
			tmp1.add(string);
		}
		tmp1.retainAll(set2);
		int size=0;
		
		if(set1.size()>set2.size()){
			size=set2.size();
		}else{
			size=set1.size();
		}
		
		return (double)tmp1.size()/size;
	}
	
	public static Double geometricSimilarity(HashSet<String> set1,HashSet<String> set2){
		HashSet<String> tmp1=new HashSet<>();
		for (String string:set1) {
			tmp1.add(string);
		}
		tmp1.retainAll(set2);
		
		return (double)(tmp1.size()*tmp1.size())/(set1.size()*set2.size());
	}
	
	public static Double cosinSimilarity(HashSet<String> set1,HashSet<String> set2){
		HashSet<String> tmp1=new HashSet<>();
		for (String string:set1) {
			tmp1.add(string);
		}
		tmp1.retainAll(set2);
		
		return tmp1.size()/Math.sqrt(set1.size()*set2.size());
	}

	public static Double pearsonCorrelationCoefficient(HashSet<String> set1,HashSet<String> set2, HashSet<String> nodes){
		HashSet<String> tmp1=new HashSet<>();
		for (String string:set1) {
			tmp1.add(string);
		}
		tmp1.retainAll(set2);
		
		return (tmp1.size()*nodes.size()-set1.size()*set2.size())/Math.sqrt(set1.size()*set2.size()*(nodes.size()-set1.size())*(nodes.size()-set2.size()));
	}
	
	public static Double hypergeometricIndex(HashSet<String> set1,HashSet<String> set2, HashSet<String> nodes){
		
		HashSet<String> tmp1=new HashSet<>();
		for (String string:set1) {
			tmp1.add(string);
		}
		tmp1.retainAll(set2);
		
		int size=0;
		if(set1.size()>set2.size()){
			size=set2.size();
		}else{
			size=set1.size();
		}
		Double value=0.0;
		if(tmp1.size()>0){
			for (int i = tmp1.size(); i < (size+1); i++) {
				
//				try{
				value+=binomial(set1.size(), i).multiply(binomial(nodes.size()-set1.size(), set2.size()-i)).divide(binomial(nodes.size(),set2.size())).doubleValue();
				
//				}catch(MathArithmeticException e){
//					System.out.println(tmp1.size()+" "+nodes.size()+" "+set2.size()+" "+set1.size()+" "+i);
//					System.out.println(set1.size()+" "+i+" "+CombinatoricsUtils.binomialCoefficient(set1.size(), i));
//					System.err.println((nodes.size()-set1.size())+" "+(set2.size()-i));
//					System.out.println(CombinatoricsUtils.binomialCoefficient((nodes.size()-set1.size()), (set2.size()-i)));
//					System.out.println(CombinatoricsUtils.binomialCoefficient(nodes.size(),set2.size()));
//				}
				
			}
			return -Math.log10(value);	
		}else{
			return Double.MIN_VALUE;
		}
		
	}
	
	
	
	public static Integer getCombinations(int n, int k){
		if(k==0){
			return 1;
		}else{
//			return factorial(n).divide(factorial(k)).divide(factorial(n-k)).intValue();	
			return factorial(n,k).divide(factorial(k)).intValue();
		}
		
	}
	
	public static int cnk(int n, int k)
	 {
	   BigInteger fenzi = new BigInteger("1");
	   BigInteger fenmu = new BigInteger("1");
	  for (int i = n - k + 1; i <= n; i++)
	  {
	   String s = Integer.toString(i);
	   BigInteger stobig = new BigInteger(s);
	    fenzi = fenzi.multiply(stobig);
	  }
	  for (int j = 1; j <= k; j++)
	  {
	   String ss = Integer.toString(j);
	   BigInteger stobig2 = new BigInteger(ss);
	    fenmu = fenmu.multiply(stobig2);
	  }
	   BigInteger result = fenzi.divide(fenmu);
//	  int result = fenzi / fenmu;
	  return result.intValue();
	 }
	
	public static BigInteger factorial(int x){
		ArrayList table = new ArrayList();
		table.add(BigInteger.valueOf(1));
	    
		for(int size=table.size();size<=x;size++){
	            BigInteger lastfact= (BigInteger)table.get(size-1);
	            BigInteger nextfact= lastfact.multiply(BigInteger.valueOf(size));
	            table.add(nextfact);
	        }
	        return (BigInteger) table.get(x);
	}
	
	public static BigInteger factorial(int x, int y){
		
		BigInteger value1=BigInteger.valueOf(x-y+1);
		System.out.println(value1);
		for(int size=0;size<y;size++){
			System.out.println(value1);
			value1= value1.multiply(value1.add(BigInteger.ONE));
	        }
		return value1;
	}
	
	public static  BigInteger binomial(final int N, final int K) {
	    BigInteger ret = BigInteger.ONE;
	    for (int k = 0; k < K; k++) {
	        ret = ret.multiply(BigInteger.valueOf(N-k))
	                 .divide(BigInteger.valueOf(k+1));
	    }
	    return ret;
	}
	
	public static Double CSI(SimpleWeightedGraph<String, DefaultWeightedEdge> pccgraph, String node1, String node2, HashSet<String> xnodes){
		int counter=0;
		Double simab=0.0;
		
		if(pccgraph.containsEdge(node1, node2)){
			simab=pccgraph.getEdgeWeight(pccgraph.getEdge(node1, node2));
		}
		for(DefaultWeightedEdge edge:pccgraph.edgesOf(node1)){
			if(Double.valueOf(pccgraph.getEdgeWeight(edge)).compareTo(simab-0.05)>=0){
				counter++;
			}
		}
		
		for(DefaultWeightedEdge edge:pccgraph.edgesOf(node2)){
			if(Double.valueOf(pccgraph.getEdgeWeight(edge)).compareTo(simab-0.05)>=0){
				counter++;
			}
		}
		return (double) (xnodes.size()-counter)/xnodes.size();
	}
	
}
