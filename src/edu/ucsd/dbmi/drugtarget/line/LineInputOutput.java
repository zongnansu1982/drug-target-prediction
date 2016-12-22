package edu.ucsd.dbmi.drugtarget.line;



import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;

import org.semanticweb.yars.nx.Node;
import org.semanticweb.yars.nx.parser.NxParser;

public class LineInputOutput {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		
//		String datafile = "D:/data/drug-target/input/bi/monteCarlo/bi_" + i * 100 + "_data.nt";
// 	    String modelfile = "D:/data/drug-target/models/line/bi/monteCarlo/line_bi_" + i * 100 + "_100.txt";
//     	String removefile = "D:/data/drug-target/input/bi/monteCarlo/bi_" + i * 100 + "_test.nt";
//     	
     	/**
     	 *  bi, monteCarlo
     	 */
//    	for (int i = 1; i < 11; i++) {
//		new LineInputOutput().buildInput("D:/data/drug-target/input/bi/monteCarlo/bi_" + i * 100 + "_data.nt",
//				"D:/data/drug-target/models/line/bi/monteCarlo/input/data_bi_"+i*100+".data");
//    	}
		
//    	for (int i = 1; i < 11; i++) {
//		new LineInputOutput().buildOutput("D:/data/drug-target/models/line/bi/monteCarlo/embedding/embedding_data_bi_"+i*100+".txt",
//				"D:/data/drug-target/models/line/bi/monteCarlo/line_bi_" + i * 100 + "_100.txt");
//	}
    	
    	
//    	for (int i = 0; i < 10; i++) {
//		new LineInputOutput().buildInput("D:/data/drug-target/input/bi/10folder/bi_folder-" + i + "_data.nt",
//				"D:/data/drug-target/models/line/bi/10folder/input/data_bi_"+i+".data");
//    	}
		
//    	for (int i = 0; i < 10; i++) {
//		new LineInputOutput().buildOutput("D:/data/drug-target/models/line/bi/10folder/embedding/embedding_data_bi_"+i+".txt",
//				"D:/data/drug-target/models/line/bi/10folder/line_bi_" + i + "_100.txt");
//	}

    	
//    	for (int i = 0; i < 10; i++) {
//		new LineInputOutput().buildInput("D:/data/drug-target/input/tri/all/10folder/tri_folder-" + i + "_data.nt",
//				"D:/data/drug-target/models/line/tri/all/10folder/input/data_tri_"+i+".data");
//		new LineInputOutput().buildInput("D:/data/drug-target/input/tri/connected/10folder/tri_folder-" + i + "_data.nt",
//				"D:/data/drug-target/models/line/tri/connected/10folder/input/data_tri_"+i+".data");
//		new LineInputOutput().buildInput("D:/data/drug-target/input/tri/unconnected/10folder/tri_folder-" + i + "_data.nt",
//				"D:/data/drug-target/models/line/tri/unconnected/10folder/input/data_tri_"+i+".data");
//    	}
    	
//    	for (int i = 0; i < 10; i++) {
//		new LineInputOutput().buildOutput("D:/data/drug-target/models/line/tri/all/10folder/embedding/embedding_data_tri_"+i+".txt",
//				"D:/data/drug-target/models/line/tri/all/10folder/line_tri_" + i + "_100.txt");
//		new LineInputOutput().buildOutput("D:/data/drug-target/models/line/tri/connected/10folder/embedding/embedding_data_tri_"+i+".txt",
//				"D:/data/drug-target/models/line/tri/connected/10folder/line_tri_" + i + "_100.txt");
//		new LineInputOutput().buildOutput("D:/data/drug-target/models/line/tri/unconnected/10folder/embedding/embedding_data_tri_"+i+".txt",
//				"D:/data/drug-target/models/line/tri/unconnected/10folder/line_tri_" + i + "_100.txt");
//	}
    	
		
		
//    	for (int i = 1; i < 11; i++) {
//		new LineInputOutput().buildInput("D:/data/drug-target/input/tri/all/monteCarlo/tri_" + i*100 + "_data.nt",
//				"D:/data/drug-target/models/line/tri/all/monteCarlo/input/data_tri_"+i*100+".data");
//		new LineInputOutput().buildInput("D:/data/drug-target/input/tri/connected/monteCarlo/tri_" + i*100 + "_data.nt",
//				"D:/data/drug-target/models/line/tri/connected/monteCarlo/input/data_tri_"+i*100+".data");
//		new LineInputOutput().buildInput("D:/data/drug-target/input/tri/unconnected/monteCarlo/tri_" + i*100 + "_data.nt",
//				"D:/data/drug-target/models/line/tri/unconnected/monteCarlo/input/data_tri_"+i*100+".data");
//    	}
    	
//    	for (int i = 1; i < 11; i++) {
//		new LineInputOutput().buildOutput("D:/data/drug-target/models/line/tri/all/monteCarlo/embedding/embedding_data_tri_"+i*100+".txt",
//				"D:/data/drug-target/models/line/tri/all/monteCarlo/line_tri_" + i*100 + "_100.txt");
//		new LineInputOutput().buildOutput("D:/data/drug-target/models/line/tri/connected/monteCarlo/embedding/embedding_data_tri_"+i*100+".txt",
//				"D:/data/drug-target/models/line/tri/connected/monteCarlo/line_tri_" + i*100 + "_100.txt");
//		new LineInputOutput().buildOutput("D:/data/drug-target/models/line/tri/unconnected/monteCarlo/embedding/embedding_data_tri_"+i*100+".txt",
//				"D:/data/drug-target/models/line/tri/unconnected/monteCarlo/line_tri_" + i*100 + "_100.txt");
//	}
		
		
//    	for (int i = 0; i < 10; i++) {
//		new LineInputOutput().buildInput("D:/data/drug-target/input/bi/all/10folder/bi_folder-" + i + "_data.nt",
//				"D:/data/drug-target/models/line/bi/all/10folder/input/data_bi_"+i+".data");
//		new LineInputOutput().buildInput("D:/data/drug-target/input/bi/connected/10folder/bi_folder-" + i + "_data.nt",
//				"D:/data/drug-target/models/line/bi/connected/10folder/input/data_bi_"+i+".data");
//		new LineInputOutput().buildInput("D:/data/drug-target/input/bi/unconnected/10folder/bi_folder-" + i + "_data.nt",
//				"D:/data/drug-target/models/line/bi/unconnected/10folder/input/data_bi_"+i+".data");
//    	}
    	
//    	for (int i = 0; i < 10; i++) {
//		new LineInputOutput().buildOutput("D:/data/drug-target/models/line/bi/all/10folder/embedding/embedding_data_bi_"+i+".txt",
//				"D:/data/drug-target/models/line/bi/all/10folder/line_bi_" + i + "_100.txt");
//		new LineInputOutput().buildOutput("D:/data/drug-target/models/line/bi/connected/10folder/embedding/embedding_data_bi_"+i+".txt",
//				"D:/data/drug-target/models/line/bi/connected/10folder/line_bi_" + i + "_100.txt");
//		new LineInputOutput().buildOutput("D:/data/drug-target/models/line/bi/unconnected/10folder/embedding/embedding_data_bi_"+i+".txt",
//				"D:/data/drug-target/models/line/bi/unconnected/10folder/line_bi_" + i + "_100.txt");
//	}
		
//    	for (int i = 1; i < 11; i++) {
//		new LineInputOutput().buildInput("D:/data/drug-target/input/bi/all/monteCarlo/bi_" + i*100 + "_data.nt",
//				"D:/data/drug-target/models/line/bi/all/monteCarlo/input/data_bi_"+i*100+".data");
//		new LineInputOutput().buildInput("D:/data/drug-target/input/bi/connected/monteCarlo/bi_" + i*100 + "_data.nt",
//				"D:/data/drug-target/models/line/bi/connected/monteCarlo/input/data_bi_"+i*100+".data");
//		new LineInputOutput().buildInput("D:/data/drug-target/input/bi/unconnected/monteCarlo/bi_" + i*100 + "_data.nt",
//				"D:/data/drug-target/models/line/bi/unconnected/monteCarlo/input/data_bi_"+i*100+".data");
//    	}
    	
//    	for (int i = 1; i < 11; i++) {
//		new LineInputOutput().buildOutput("D:/data/drug-target/models/line/bi/all/monteCarlo/embedding/embedding_data_bi_"+i*100+".txt",
//				"D:/data/drug-target/models/line/bi/all/monteCarlo/line_bi_" + i*100 + "_100.txt");
//		new LineInputOutput().buildOutput("D:/data/drug-target/models/line/bi/connected/monteCarlo/embedding/embedding_data_bi_"+i*100+".txt",
//				"D:/data/drug-target/models/line/bi/connected/monteCarlo/line_bi_" + i*100 + "_100.txt");
//		new LineInputOutput().buildOutput("D:/data/drug-target/models/line/bi/unconnected/monteCarlo/embedding/embedding_data_bi_"+i*100+".txt",
//				"D:/data/drug-target/models/line/bi/unconnected/monteCarlo/line_bi_" + i*100 + "_100.txt");
//	}
		
//		new LineInputOutput().buildInput("D:/data/drug-target/source/bi.nt",
//				"D:/data/drug-target/models/line/bi/external/external_data.data");
//		
//		new LineInputOutput().buildInput("D:/data/drug-target/source/tri.nt",
//				"D:/data/drug-target/models/line/tri/external/external_data.data");
		
		new LineInputOutput().buildOutput("D:/data/drug-target/models/line/bi/external/embedding_data_100.txt",
				"D:/data/drug-target/models/line/bi/external/external_data.nt");
		
		new LineInputOutput().buildOutput("D:/data/drug-target/models/line/tri/external/embedding_data_100.txt",
				"D:/data/drug-target/models/line/tri/external/external_data.nt");
		
		
		/**
		 * ******************************************************************
		 */
//		new LineInputOutput().buildInput("data/input/drugCloud/data_bi.nt", "outsource/windows/data_bi.data");
//		new LineInputOutput().buildInput("data/input/drugCloud/data_tri.nt", "outsource/windows/data_tri.data");
		
//		new LineInputOutput();
//		LineInputOutput.buildOutput("outsource/windows/experiment0630/embeddings/embedding_data_tri_100.txt",
//				"outsource/windows/experiment0630/models/line_tri_100.txt");
//				new LineInputOutput();
//				LineInputOutput.buildOutput("outsource/windows/experiment0630/embeddings/embedding_data_bi_100.txt",
//						"outsource/windows/experiment0630/models/line_bi_100.txt");
	}
	
	public static  void buildInput(String input, String output) throws IOException{
    	BufferedWriter bw = new BufferedWriter(new FileWriter(new File(output)));
    	BufferedReader br = new BufferedReader(new FileReader(new File(input)));
		String line=null;
		while((line=br.readLine())!=null){
			InputStream inputStream = new ByteArrayInputStream(line.getBytes());
			NxParser nxp = new NxParser();
			nxp.parse(inputStream);
			
			while (nxp.hasNext()) {
				Node[] quard = nxp.next();
				String s = quard[0].toString().trim();
				String p = quard[1].toString().trim();
				String o = quard[2].toString().trim();
				
				if(s.startsWith("<http://")&o.startsWith("<http://")){
					bw.write(s+" "+o+" "+1+"\n");
				}
			}
		}
    	bw.flush();
    	bw.close();
    	br.close();
    }
    
    public static void buildOutput(String input,String outputfile) throws Exception{
    	BufferedWriter bw = new BufferedWriter(new FileWriter(new File(outputfile)));
    	BufferedReader br = new BufferedReader(new FileReader(new File(input)));
		String line=null;
		while((line=br.readLine())!=null){
			if(line.startsWith("<http://")){
			     bw.write(line.trim()+"\n");
			}
		}
    	bw.flush();
    	bw.close();
    	br.close();
    }
}
