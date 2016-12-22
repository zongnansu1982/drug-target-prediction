package edu.ucsd.dbmi.drugtarget.chemicstrc;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;

public class SimilarityRunable implements Runnable {

	BufferedWriter bw;
	HashMap<String, String> idx;
	HashSet<String> pairs;
	String name;
	File file;

	public SimilarityRunable(HashSet<String> pairs, HashMap<String, String> idx, File file, String name) {
		this.bw = bw;
		this.idx = idx;
		this.pairs = pairs;
		this.name = name;
		this.file = file;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		BufferedWriter bw = null;
		try {
			bw = new BufferedWriter(new FileWriter(file));
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		System.out.println(name + " is running now, size: " + pairs.size());
		for (String pair : pairs) {
			String[] elements = pair.split(" ");
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
			try {
				String str = "http://rest.genome.jp/simcomp2/" + idx.get(elements[0]) + "/" + idx.get(elements[1])
						+ "/cutoff=0.0001";
				URL myURL = new URL(str);
				HttpURLConnection conn = (HttpURLConnection) myURL.openConnection();
				InputStream is = conn.getInputStream();
				InputStreamReader isr = new InputStreamReader(is);
				BufferedReader br = new BufferedReader(isr);
				StringBuffer buffer = new StringBuffer();
				String line = null;
				while (null != (line = br.readLine())) {
					buffer.append(line);
				}
				br.close();
				isr.close();
				is.close();
				if (buffer.toString() == null || buffer.toString() == "" || buffer.toString().isEmpty()) {
					bw.write(elements[0] + " " + idx.get(elements[0]) + " " + elements[1] + " " + idx.get(elements[1])
							+ " NaN\n");
				} else {
					String[] values = buffer.toString().split("	");
					bw.write(elements[0] + " " + idx.get(elements[0]) + " " + elements[1] + " " + idx.get(elements[1])
							+ " " + values[2] + "\n");
				}
			} catch (Exception e) {
				try {
					bw.write(elements[0] + " " + idx.get(elements[0]) + " " + elements[1] + " " + idx.get(elements[1])
							+ " error=>" + e + "\n");
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				try {
					bw.flush();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}

		}
		try {
			bw.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			bw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
