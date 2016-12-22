package edu.ucsd.dbmi.drugtarget.deepwalk;

import org.deeplearning4j.graph.api.IGraph;
import org.deeplearning4j.graph.api.NoEdgeHandling;
import org.deeplearning4j.graph.api.Vertex;
import org.deeplearning4j.graph.api.IVertexSequence;
import org.deeplearning4j.graph.exception.NoEdgesException;
import org.deeplearning4j.graph.graph.VertexSequence;
import org.deeplearning4j.graph.iterator.GraphWalkIterator;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Random;

/**Given a graph, iterate through random walks on that graph of a specified length.
 * Random walks are generated starting at every node in the graph exactly once, though the order
 * of the starting nodes is randomized.
 * @author Alex Black
 */
public class DeepWalkIterator<V> implements GraphWalkIterator<V> {

    private final ImprovedGraph<V,?> graph;
    private final int walkLength;
    private final NoEdgeHandling mode;
    private final int firstVertex;
    private final int lastVertex;


    private int position;
    private Random rng;
    private int[] order;

    public DeepWalkIterator(ImprovedGraph<V,?> graph, int walkLength ){
        this(graph,walkLength,System.currentTimeMillis(), NoEdgeHandling.EXCEPTION_ON_DISCONNECTED);
    }

    /**Construct a RandomWalkIterator for a given graph, with a specified walk length and random number generator seed.<br>
     * Uses {@code NoEdgeHandling.EXCEPTION_ON_DISCONNECTED} - hence exception will be thrown when generating random
     * walks on graphs with vertices containing having no edges, or no outgoing edges (for directed graphs)
     * @see #RandomWalkIterator(IGraph, int, long, NoEdgeHandling)
     */
    public DeepWalkIterator(ImprovedGraph<V,?> graph, int walkLength, long rngSeed ){
        this(graph, walkLength, rngSeed, NoEdgeHandling.EXCEPTION_ON_DISCONNECTED);
    }

    /**
     * @param graph IGraph to conduct walks on
     * @param walkLength length of each walk. Walk of length 0 includes 1 vertex, walk of 1 includes 2 vertices etc
     * @param rngSeed seed for randomization
     * @param mode mode for handling random walks from vertices with either no edges, or no outgoing edges (for directed graphs)
     */
    public DeepWalkIterator(ImprovedGraph<V,?> graph, int walkLength, long rngSeed, NoEdgeHandling mode ){
        this(graph,walkLength,rngSeed,mode,0,graph.numVertices());
    }

    /**Constructor used to generate random walks starting at a subset of the vertices in the graph. Order of starting
     * vertices is randomized within this subset
     * @param graph IGraph to conduct walks on
     * @param walkLength length of each walk. Walk of length 0 includes 1 vertex, walk of 1 includes 2 vertices etc
     * @param rngSeed seed for randomization
     * @param mode mode for handling random walks from vertices with either no edges, or no outgoing edges (for directed graphs)
     * @param firstVertex first vertex index (inclusive) to start random walks from
     * @param lastVertex last vertex index (exclusive) to start random walks from
     */
    public DeepWalkIterator(ImprovedGraph<V,?> graph, int walkLength, long rngSeed, NoEdgeHandling mode, int firstVertex,
                              int lastVertex ){
        this.graph = graph;
        this.walkLength = walkLength;
        this.rng = new Random(rngSeed);
        this.mode = mode;
        this.firstVertex = firstVertex;
        this.lastVertex = lastVertex;
        HashSet<Integer> validNods= new HashSet<>();
       
        for( int i=firstVertex; i<lastVertex; i++ ){
        	if(graph.getFromEdges()[i]!=null){
        		if(graph.getFromEdges()[i].size()>0){
        			validNods.add(i);
        		}
        	}
        }
        order = new int[validNods.size()];
        int j=0;
        for(int nodeIdx:validNods){
        	order[j]=nodeIdx;
        	j++;
        	
        }
        reset();
    }

    @Override
    public IVertexSequence<V> next() {
    	
        if(!hasNext()) throw new NoSuchElementException();
        //Generate a random walk starting at at vertex order[current]
        int currVertexIdx = order[position++];
        ArrayList<Integer> indexlist=new ArrayList<>(); 
        
        Vertex<V> next = null;

             for( int i=0; i<=walkLength; i++ ){ //<= walk length: i.e., if walk length = 2, it contains 3 vertices etc
            	 if(i==0){
            		 indexlist.add(currVertexIdx);
            	 }else{
            		 try{
                         next = graph.getRandomConnectedVertex(currVertexIdx,rng);
                     	}catch (NoEdgesException e){
                     		 switch(mode){
                              case SELF_LOOP_ON_DISCONNECTED:
                                  for(int j=1; j<walkLength; j++) indexlist.add(currVertexIdx);
                              case EXCEPTION_ON_DISCONNECTED:
                            	  indexlist.add(currVertexIdx); break;
//                              default:indexlist.add(currVertexIdx); break;
                           }	
                     	}
                   currVertexIdx = next.vertexID();
                   indexlist.add(currVertexIdx);
            	 }
             }
        
        int[] indices = new int[indexlist.size()];
        for (int i = 0; i < indexlist.size(); i++) {
        	indices[i]=indexlist.get(i);
		}
       
        return new VertexSequence<>(graph,indices);
    }

    @Override
    public boolean hasNext() {
        return position < order.length;
    }

    @Override
    public void reset() {
        position = 0;
        //https://en.wikipedia.org/wiki/Fisher%E2%80%93Yates_shuffle#The_modern_algorithm
        for(int i=order.length-1; i>0; i-- ){
            int j = rng.nextInt(i+1);
            int temp = order[j];
            order[j] = order[i];
            order[i] = temp;
        }
    }

    @Override
    public int walkLength(){
        return walkLength;
    }
}
