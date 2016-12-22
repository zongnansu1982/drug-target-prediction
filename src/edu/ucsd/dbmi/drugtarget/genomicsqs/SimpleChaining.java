package edu.ucsd.dbmi.drugtarget.genomicsqs;

import java.util.*;
import java.io.*;

public class SimpleChaining
{

	public static Comparator FROMA_COMPARATOR = new Comparator() {public int compare(Object o1, Object o2) { return ((Match) o1).getFromA()-((Match) o2).getFromA();}};

     /**
     *  Match class defintion
     *  
     */
     public static class Match 
     {
		int fromA;
		int fromB;
		int toA;
		int toB;
		double score;
		
		
	public Match(int fA, int tA, int fB, int tB, double s)
	{
		fromA=fA;
		fromB=fB;
		toA=tA;
		toB=tB;
		score =s;
	}/**
	 * Returns the value of fromA.
	 */
	public int getFromA()
	{
		return fromA;
	}

	/**
	 * Returns the value of fromB.
	 */
	public int getFromB()
	{
		return fromB;
	}

	/**
	 * Returns the value of toA.
	 */
	public int getToA()
	{
		return toA;
	}

	/**
	 * Returns the value of toB.
	 */
	public int getToB()
	{
		return toB;
	}

	/**
	 * Returns the value of score.
	 */
	public double getScore()
	{
		return score;
	}
	
	//check whether this Match onecjt overlap with input Match m;
	//return true if two objects do not overlap
	public boolean notOverlap(Match m)
	{	
		return  (m.getFromA()>toA || fromA>m.getToA()) && ( m.getFromB()>toB || fromB>m.getToB());
	}
	
	public boolean isChainable(Match m)
	{
		return (m.getFromA()>toA && m.getFromB()> toB );
	}
	public String toString()
	{
		StringBuffer buffer = new StringBuffer();
		buffer.append("["+fromA+", "+ toA+", "+ fromB+",  "+toB+", "+ score+",]");
		return buffer.toString();
	}
     }


   /**
    * Identify the best chain from given list of match
    * 
    * @param matches a list of match
    * @param debug  if true, print list of input match, adjacency, score matrix, best chain found.
    * @return the optimal chain as a list of match
    */
   public static List chaining(List matches, boolean debug)
   {
	   int size =matches.size();
	   // Hold adjaency matrix as a double [] the (i,j)= i*(i-1)/2+j
	   //with sink
	   double [] adjMatrix = new double[size*(size-1)/2+size-1];
	   // Hold score matrix as a double [] the (i,j)= i*(i-1)/2+j
          double [] sMatrix = new double[size*(size-1)/2+size-1];
	  //Hold max score of chain end at match i
	  double [] sMax = new double [size];
	  // Hold the previous match index point to match i
	  int [] prevIndex = new int [size];
	  for (int i=0; i< size; i++) prevIndex[i]=-1;
	  
	  //sort the matches based on the occurance in sequence A
	  Collections.sort(matches, SimpleChaining.FROMA_COMPARATOR);
	  if (debug)
	  {
		  System.out.println("The list of Matches {[fromA, toA, fromB, toB, score]...}");
		   System.out.println(matches);
	  }
	  //initialize the adjancey matrix and scre matrx from top left to bottom right
	  //for each match i=1..size-1
	  // 	compare to rest match j= 0,...i-1
	    Match mr=null;
	  for (int i=1; i<size; i++)
	  {
		
		  //if ( i !=size-1)
			  mr= (Match) matches.get(i);
		  for (int j=0; j< i; j++)
		  {
			 Match mc =(Match) matches.get(j);
			 int i_j = i*(i-1)/2+j;
		         if (mc.isChainable(mr))
			 {			
				 adjMatrix[i_j] = mc.getScore();
				 //update score matrix
				 sMatrix[i_j] = adjMatrix [i_j]+ sMax[j];
			 	 //update sMax if necessary
				 if (sMatrix[i_j]>sMax[i])
				 {
					 sMax[i] = sMatrix[i_j];
					 prevIndex[i]=j;
				 }
			 }
		  }
	  }
	  //now backtrace to construct the chain	  
	  //get the max score
	  double max=0;
	  int maxIndex=0;
	  for (int i=0; i< size; i++)
	  {
		  sMax[i]+=((Match) matches.get(i)).getScore();
		  if (sMax[i]>max)
		  {
			  max=sMax[i];
			  maxIndex=i;
		  }
	  }
	  if (debug)
	  {
		  System.out.println("The adjacency matrix is:");
		  printLowerMatrix(adjMatrix, size);
		  System.out.print("sink\t");
		  for (int i=0; i< size; i++)
			  System.out.print( ((Match) matches.get(i)).getScore()+"\t");
		  System.out.println();
		  System.out.println("The score matrix is:");
		  printLowerMatrix(sMatrix, size);
		  System.out.print("sink\t");
		  for (int i=0; i< size; i++)
			  System.out.print((float) sMax[i]+"\t");
		  System.out.println();
	  }
	  
	  //now the chain end with match at maxIndex
	  //the score is max;
	  //trace back to the begining of the chain;
	  int [] chainIndex = new int [maxIndex];
	  for (int i=0; i<chainIndex.length; i++) chainIndex[i]=-1;
	  chainIndex [0]=maxIndex;
	  for (int i=1; prevIndex[chainIndex[i-1]]>=0;i++)
	  	  chainIndex[i]=prevIndex[chainIndex[i-1]];
	 //now revese the chain 
	 //and put the matches in a list;	 
	 List chain = new ArrayList();
	 for (int i=chainIndex.length-1; i>=0; i--)
	 {
		 if (chainIndex[i]>=0)
			 chain.add(matches.get(chainIndex[i]));
	 }
	 
	 if (debug)
	 {
		 System.out.println("The best chain with score "+max);
		 for (int i=chainIndex.length-1; i>=0; i--)
		 {
			 if (chainIndex[i]>=0)
				 System.out.print(chainIndex[i]+"---->");
		 }
		 System.out.println("sink");
	 }
	 return chain;
   }
   
   /**
    *System out the input array as an strict lower diagonal matrix
    */
   public static void printLowerMatrix(double [] m, int size)
   {
	   System.out.print("\t");
	   for (int i=0; i< size; i++)
		   System.out.print(i+"\t");
	   System.out.println();
   	  for (int i=0; i<size; i++)
	  {
		  System.out.print(i+"\t");
		  for (int j=0; j< i; j++)
		  {
			 int i_j = i*(i-1)/2+j;
			 System.out.print((float) m[i_j]+"\t");
		  }
		 System.out.println();
	  }

   }
   
   //tester code
   public static void test()
   {
	   //bettybitter
	   //peterpiper
	/*   List matches = new ArrayList();
	   matches.add(new SimpleChaining.Match(1,  2,  1,  2,  2.0)); //"et-et"
	   matches.add(new SimpleChaining.Match(3, 3, 1, 1, 1.0)); //"e-e"
	   matches.add(new SimpleChaining.Match(8, 8, 1, 1, 1.0)); //"e-e"
	   matches.add(new SimpleChaining.Match(2, 2, 7, 7, 1.0)); //"t-t"
	   matches.add(new SimpleChaining.Match(2, 4, 8, 10, 3.0)); //"ter-ter"
	   matches.add(new SimpleChaining.Match(8, 9, 9, 10, 2.0)); //"er-er"
	   matches.add(new SimpleChaining.Match(1, 1, 9, 9, 1.0)); //"e-e"
	   matches.add(new SimpleChaining.Match(6, 6, 6, 6, 1.0)); //"i-i"
	   SimpleChaining.chaining(matches, true);	   
	   */
	try {
		   System.out.println("Compute the local alignments between two strings\n");
	
		    // accept two strings from the standard input
		    BufferedReader in =
			new BufferedReader(new InputStreamReader(System.in));
		    System.out.print("Enter string 1 [bettybitter]: ");
		    String str1 = in.readLine();
		    System.out.print("Enter string 2 [peterpiper]: ");
		    String str2 = in.readLine();
		    if (str1.length()==0) str1 ="bettybitter";
		    if (str2.length()==0) str2 ="peterpiper";
		    // compute and output the score and alignments
		    //SmithWaterman sw = new SmithWaterman(str1, str2);
		    SmithWaterman sw = new SmithWaterman(str1,str2);
		    sw.printDPMatrix();
		    List ms = sw.getMatches();
		    SimpleChaining.chaining(ms, true);
		} catch (IOException e) {
		    System.err.println("Error: " + e);
		}   
   }
   
   
   public static void main (String args[])
   {
	   test();
   }

}