/**
 * 
 */
package com.vars.videoadanalysis;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Abhinav
 *
 */
public class JaccardSimilarityPolitical {

	/**
	 * Dictionary global variables
	 */
	static ArrayList<String> wordList = new ArrayList<String>();
	static ArrayList<String> defendWordList;
	static ArrayList<String> republicanNameList;
	static ArrayList<String> democratNameList;
	static ArrayList<String> completeNameList = new ArrayList<String>();

	static HashMap<String, String> videoDetails = new HashMap<String, String>();
	static HashMap<String, Integer> countNames = new HashMap<String, Integer>();

	static String otherCandidates;
	static FetchFromDB objFetchFromDB;

	/**
	 * Default constructor
	 */
	protected JaccardSimilarityPolitical() {

		defendWordList = new ArrayList<String>();
		republicanNameList = new ArrayList<String>();
		democratNameList = new ArrayList<String>();
		// videoDetails = new HashMap<Integer, String>();
		otherCandidates = null;
	}

	static void getText() {
		File f = null;
		String[] paths;
		String dir = "C:\\Users\\yedla\\Downloads\\transcripts (1)\\transcripts\\withprofile_3";
		f = new File(dir);

		// array of files and directory
		paths = f.list();

		
		for (String path : paths) {
			// prints filename and directory name
			System.out.println(path);
			
			String com = "";
			try {
				BufferedReader br = new BufferedReader(new FileReader(dir + "\\" + path));
				String line;

				while ((line = br.readLine()) != null) {
					com = com + line;
				}
				br.close();

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			
			
			
			System.out.println(com);
			videoDetails.put(path, com.toLowerCase());

		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		long startTime = System.nanoTime();

		// Create object for fetching from database
		FetchFromDB objFetchFromDB = new FetchFromDB("root", "admin",
				"jdbc:mysql://localhost/ad_analysis");

		String query = null, type = null;

		// Attack and defend similarity index
		Double simIndex = null;

		// Column name
		type = new String("word");

		// Fetch Attacking word list
		query = new String("SELECT word FROM Attack;");
		wordList = objFetchFromDB.retrieve(query, type);

		// Fetch Defending word list
		query = new String("SELECT word FROM Defend;");
		wordList.addAll(objFetchFromDB.retrieve(query, type));

		type = new String("Name");

		// Fetch Republican Name list
		query = new String("SELECT Name FROM Republicans;");
		completeNameList = objFetchFromDB.retrieve(query, type);

		// Fetch Democrat Name list
		query = new String("SELECT Name FROM Democrates;");
		completeNameList.addAll(objFetchFromDB.retrieve(query, type));

		// Fetch Video Text
		getText();

		for (String key : videoDetails.keySet()) {
			otherCandidates = null;
			String VideoText = videoDetails.get(key);

			// Convert to Array List
			ArrayList<String> videoTextToArrayList = new ArrayList<String>();
			for (String word : VideoText.split(" ")) {
				videoTextToArrayList.add(word);
			}

			String name;
			for (int i = 0; i < completeNameList.size(); i++) {
				name = completeNameList.get(i);

				if (VideoText.contains(name)) {
					if (countNames.containsKey(name))
						countNames.put(name, countNames.get(name) + 1);
					else
						countNames.put(name, 1);
				}
			}

			// Get the similarity between the attack and video text, defend and
			// video text
			simIndex = similarity(videoTextToArrayList, wordList);

			// Output according to the similarity measure

			// System.out.print("\t" + "simIndex_attack: " + simIndex);

		}

		// Calculate the runtime of the program
		long endTime = System.nanoTime();
		System.out.println("Took " + (endTime - startTime) + " ns");

		for (String key : countNames.keySet()) {
			System.out.println(key + "  " + countNames.get(key));
		}
	}

	/**
	 * Find all the person in the video text by comparing to the dictionary
	 * 
	 * @param videoTextToArrayList
	 *            , key
	 * @return speakerNew
	 */

	/**
	 * Similarity measure between the documents
	 * 
	 * @param videoTextToArrayList
	 *            ,dictionary
	 * @return modified jaccardSimilarity
	 */

	protected static double similarity(ArrayList<String> videoTextToArrayList,
			ArrayList<String> dictionary) {

		if (videoTextToArrayList.size() == 0 || dictionary.size() == 0) {
			return 0.0;
		}

		Set<String> unionXY = new HashSet<String>(videoTextToArrayList);
		unionXY.addAll(dictionary);

		Set<String> intersectionXY = new HashSet<String>(videoTextToArrayList);
		intersectionXY.retainAll(dictionary);

		return (double) intersectionXY.size() / (double) unionXY.size();
	}

}
