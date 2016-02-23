/**
 * 
 */
package com.vars.videoadanalysis;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.ListIterator;
import java.util.Set;

/**
 * @author Abhinav
 *
 */
public class WeightedJaccardSimilarity {

	/**
	 * Dictionary global variables
	 */
	static HashMap<String, String> attackWordList;
	static HashMap<String, String> defendWordList;
	static ArrayList<String> republicanNameList;
	static ArrayList<String> democratNameList;

	static HashMap<String, String> videoDetails;

	static String otherCandidates;

	/**
	 * Default constructor
	 */
	public WeightedJaccardSimilarity() {
		attackWordList = new HashMap<String, String>();
		defendWordList = new HashMap<String, String>();
		republicanNameList = new ArrayList<String>();
		democratNameList = new ArrayList<String>();
		videoDetails = new HashMap<String, String>();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) throws FileNotFoundException {

		long startTime = System.nanoTime();

		FetchFromDB objFetchFromDB = new FetchFromDB("root", "admin",
				"jdbc:mysql://localhost/ad_analysis");

		String query = null, type = null, speaker;

		Double simIndex_attack = null, simIndex_defense = null;

		type = new String("word");

		// Fetch Attacking word list
		query = new String("SELECT word, weight FROM Attack;");
		attackWordList = objFetchFromDB.retrieve(query);

		// Fetch Defending word list
		query = new String("SELECT word, weight FROM Defend;");
		defendWordList = objFetchFromDB.retrieve(query);

		type = new String("Name");

		// Fetch Republican Name list
		query = new String("SELECT Name FROM Republic_Senator;");
		republicanNameList = objFetchFromDB.retrieve(query, type);

		// Fetch Democrat Name list
		query = new String("SELECT Name FROM Democrat_Senator;");
		democratNameList = objFetchFromDB.retrieve(query, type);

		// Fetch Video Text
		videoDetails = objFetchFromDB.retrieve();

		for (String key : videoDetails.keySet()) {

			String VideoText = videoDetails.get(key);

			// Convert to Array List
			ArrayList<String> videoTextToArrayList = new ArrayList<String>();
			for (String word : VideoText.split(" ")) {
				videoTextToArrayList.add(word);
			}

			// Get the similarity between the attack and video text, defend and
			// video text
			simIndex_attack = similarity(videoTextToArrayList, attackWordList);
			simIndex_defense = similarity(videoTextToArrayList, defendWordList);

			// Find the speaker in the video
			speaker = findPersons(videoTextToArrayList, key);

			// Output according to the similarity measure
			if (simIndex_attack < simIndex_defense) {
				if (speaker != null && !speaker.isEmpty()) {
					//System.out.print(speaker + " In the Video: \"" + key
						//	+ "\" is Defending/Taking about self");
					System.out.print( key);
					if (otherCandidates != null && !otherCandidates.isEmpty()
							&& !otherCandidates.contains(speaker))
						//System.out.print(" and mentioned about "
							//	+ otherCandidates);
						System.out.print( " ");

				} else
					//System.out.print("Video: \"" + key
						//	+ "\" is an Defending video with");
					System.out.print( key);
			} else {
				if (speaker != null && !speaker.isEmpty()) {
					//System.out.print(speaker + " In the Video: \"" + key
						//	+ "\" is Attacking ");
					System.out.print( key);

					if (otherCandidates != null && !otherCandidates.isEmpty()
							&& !otherCandidates.contains(speaker))
					//	System.out.print(" and mentioned about "
						//		+ otherCandidates);
						System.out.print( " ");

				} else
					//System.out.print("Video: \"" + key
						//	+ "\" is an attacking video with");
					System.out.print( key);

			}
			System.out.print("\t"+"simIndex_attack: " + simIndex_attack);
			System.out.print("\t"+"simIndex_defense: " + simIndex_defense + "\n");

		}

		// Calculate the runtime of the program
		long endTime = System.nanoTime();
		//System.out.println("Took " + (endTime - startTime) + " ns");

	}

	/**
	 * Find all the person in the video text by comparing to the dictionary
	 * 
	 * @param videoTextToArrayList
	 *            , key
	 * @return speakerNew
	 */
	private static String findPersons(ArrayList<String> videoTextToArrayList,
			String key) {

		String speaker = null;
		ArrayList<String> candidatesList = new ArrayList<String>();
		candidatesList.addAll(republicanNameList);
		candidatesList.addAll(democratNameList);

		FetchNames objFetchNames = new FetchNames(videoTextToArrayList,
				candidatesList);

		speaker = objFetchNames.speakerNew;
		otherCandidates = objFetchNames.otherCandidates;
		if (speaker.isEmpty()) {

			ListIterator<String> itrRep = republicanNameList.listIterator();
			while (itrRep.hasNext()) {
				speaker = itrRep.next();
				String temp[] = speaker.split(" ");
				for (int i = 0; i < temp.length; i++) {
					if (key.toLowerCase()
							.indexOf("by " + temp[i].toLowerCase()) != -1) {
						return speaker;
					}
				}

			}

			ListIterator<String> itrDem = democratNameList.listIterator();
			while (itrDem.hasNext()) {
				speaker = itrDem.next();
				String temp[] = speaker.split(" ");
				for (int i = 0; i < temp.length; i++) {
					if (key.toLowerCase()
							.indexOf("by " + temp[i].toLowerCase()) != -1) {
						return speaker;
					}
				}

			}
		}
		return objFetchNames.speakerNew;
	}

	/**
	 * Weighted jaccard Similarity measure between the documents
	 * 
	 * @param videoTextToArrayList
	 *            ,dictionary
	 * @return modified jaccardSimilarity
	 */

	private static double similarity(ArrayList<String> videoTextToArrayList,
			HashMap<String, String> attackWordList) {

		int jaccardWeight = 0;
		String key = null;
		Set<String> unionXY = new HashSet<String>(attackWordList.keySet());
		unionXY.addAll(videoTextToArrayList);

		if (videoTextToArrayList.size() == 0 || attackWordList.size() == 0)
			return 0.0;

		ListIterator<String> listIter = videoTextToArrayList.listIterator();

		while (listIter.hasNext()) {
			key = listIter.next();
			if (attackWordList.containsKey(key))
				jaccardWeight += Integer.parseInt(attackWordList.get(key));
		}

		return (double) jaccardWeight / (double) unionXY.size();
	}

}
