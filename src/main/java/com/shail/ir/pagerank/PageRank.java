package com.shail.ir.pagerank;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;

class PageRank {
	static String file_path = "small_graph.txt";
	static String G1 = "03 BFS_graph.txt";
	static String G2 = "big_graph.txt";
	static HashMap<String, ArrayList> inlinks;
	static HashMap<String, Integer> n_outlinks;
	static HashMap<String, Integer> n_inlinks;
	static HashMap<String, Double> pr;
	static ArrayList<String> sinks;
	static int n;
	static int SINK = 0;
	static int MAX_REPEAT = 4;
	static double d = 0.85;
	static int sink_count = 0;
	static int source_count = 0;

	public static void main(String[] args) throws IOException{
		Scanner sc = new Scanner(System.in);
		while(true){
			sink_count = 0;
			source_count = 0;
			System.out.print("Select the file\n1. G1 2. G2 3. Exit: ");
			int choice = sc.nextInt();
			switch(choice){
				case 1:
					file_path = G1;
					break;
				case 2:
					file_path = G2;
					break;
				case 3:
					System.out.println("Exiting program...");
					return;

				default:
					System.out.println("Enter a number between 1 and 3.");
			}

			if(choice == 1 || choice == 2){
				long startTime = System.currentTimeMillis();

				System.out.println("Running PageRank untill convergence...");

				pageRank(file_path);

				print_to_file(pr, "pagerank-"+file_path, 50);
				n_inlinks = sortByValue(n_inlinks);
				print_to_file(n_inlinks, "inlink-count.txt", 5);

				long endTime   = System.currentTimeMillis();
				long totalTime = endTime - startTime;
				System.out.println("Sink Nodes: "+sink_count);
				System.out.println("Source Nodes: "+source_count);
				System.out.println("Computation time: "+totalTime+"ms");
				System.out.println("Top 50 PageRanks stored in .txt file.");
			}
		}
	}
	static void pageRank(String file_path) {
		sink_count = 0;
		source_count = 0;
		init_values(file_path);
		int cons_count = 0;
		double currPerp = 0;
		double prevPerp = 0;
		int iterations = 0;

		while (cons_count < MAX_REPEAT){
			double sinkPR = 0;
			for (String i: sinks) {
				sinkPR += pr.get(i);
			}

			Iterator pr_it = pr.entrySet().iterator();
			while (pr_it.hasNext()) {
				Map.Entry pair = (Map.Entry)pr_it.next();
				String key = (String)pair.getKey();
				Double value = (Double)pair.getValue();

				double new_pr = (1-d)/(double)n;
				new_pr += d * sinkPR/(double)n;
				ArrayList<String> in = inlinks.get(key);
				for (String i: in){
					new_pr += d*pr.get(i)/(double)n_outlinks.get(i);
				}
				pr.replace(key, new_pr);
			}

			prevPerp = currPerp;
			currPerp = perplexity();
			if(Math.abs(currPerp-prevPerp) < 1)
				cons_count++;
			else cons_count = 0;
			iterations++;
		}
		pr = (HashMap<String, Double>) sortByValue(pr);
		System.out.println(iterations+" taken for convergence.");
	}
	static int count_lines(String file_path){
		int count = 0;
		try{
			LineNumberReader lnr = new LineNumberReader(new FileReader(new File(file_path)));
			lnr.skip(Long.MAX_VALUE);
			count = lnr.getLineNumber() + 1; //+1 because line index starts at 0
			lnr.close();
		} catch (IOException io){io.printStackTrace();}
		return count;
	}

	static ArrayList<String> getSinks(HashMap n_outlinks){
		ArrayList<String> sinks = new ArrayList();

		Iterator it = n_outlinks.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry pair = (Map.Entry)it.next();
			if((int) pair.getValue() == SINK){
				sinks.add((String)pair.getKey());
				sink_count++;
			}
		}
		return sinks;
	}
	static void init_values(String file_path){
		try{
			n = count_lines(file_path);
		} catch(Exception io){io.printStackTrace();}

		BufferedReader br = null;
		String line;
		ArrayList<String> elements;
		ArrayList<String> file_content = new ArrayList<String>();

		inlinks = new HashMap<String, ArrayList>();
		n_outlinks = new HashMap<String, Integer>();
		n_inlinks = new HashMap<String, Integer>();
		pr= new HashMap<String, Double>();

		try {
			br = new BufferedReader(new FileReader(file_path));
			while ((line = br.readLine()) != null) {
				elements = new ArrayList<String>(Arrays.asList(line.split(" ")));
				String key = elements.get(0);
				elements.remove(0);
				if(elements.size() == 0)
					source_count++; // source node only has outgoing flow
				if(elements.contains(key))
					elements.remove(elements.indexOf(key));
				inlinks.put(key, elements);
				pr.put(key, 1/(double)n);           // initial page rank value of all nodes is 1/n
				n_outlinks.put(key, 0);    			// initialized it to 0
				n_inlinks.put(key, elements.size());
				file_content.add(line);
			}
			br.close();
		} catch (IOException io) {io.printStackTrace();}


		for (String i: file_content) {
			String[] sa = i.split(" ");
			for(int j = 1; j < sa.length; j++)
				n_outlinks.replace(sa[j], n_outlinks.get(sa[j])+1);
		}

		sinks = getSinks(n_outlinks);
	}

	static double perplexity(){
		double entropy = 0;
		Iterator it = pr.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry pair = (Map.Entry)it.next();
			double p = (double)pair.getValue();
			entropy = entropy+(p*log2(p));
		}
		entropy = -entropy;
		double perp = Math.pow(2, entropy);
		System.out.println(perp);
		return perp;
	}

	static double log2(double a) {
		return Math.log(a) / Math.log(2);
	}


	static <K, V extends Comparable<? super V>> HashMap<K, V> sortByValue(HashMap<K, V> unsortHashMap) {
		List<HashMap.Entry<K, V>> list = new LinkedList<HashMap.Entry<K, V>>(unsortHashMap.entrySet());
		Collections.sort(list, new Comparator<HashMap.Entry<K, V>>() {
			public int compare(HashMap.Entry<K, V> o1, HashMap.Entry<K, V> o2) {
				return (o2.getValue()).compareTo(o1.getValue());
			}
		});
		HashMap<K, V> result = new LinkedHashMap<K, V>();
		for (HashMap.Entry<K, V> entry : list)
			result.put(entry.getKey(), entry.getValue());
		return result;

	}
	static void print_to_file(HashMap hm, String path, int n) throws IOException{
		int count = 0;
		FileWriter fstream;
		BufferedWriter out;
		fstream = new FileWriter(path);
		out = new BufferedWriter(fstream);
		Iterator<Entry<String, Integer>> it = hm.entrySet().iterator();
		while (it.hasNext() && count < n) {
			Map.Entry<String, Integer> pairs = it.next();
			out.write(pairs.getKey()+"\t\t"+pairs.getValue() + "\n");
			count++;
		}
		out.close();
	}
}
