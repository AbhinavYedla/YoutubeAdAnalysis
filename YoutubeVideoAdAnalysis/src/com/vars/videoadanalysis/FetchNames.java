package com.vars.videoadanalysis;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Abhinav
 *
 */
public class FetchNames {
	List<ArrayList<String>> outputList;
	String speakerNew = new String(""), otherCandidates = new String("");

	/**
	 * Parameterized constructor. Used to get candidatesList from video text
	 * 
	 * @param videoTextToArrayList
	 *            , candidatesList
	 */
	protected FetchNames(ArrayList<String> videoTextToArrayList,
			ArrayList<String> candidatesList) {

		try {

			outputList = name(videoTextToArrayList, candidatesList);
			speakerNew = Speaker(outputList);
			ArrayList<String> other = otherNames(outputList);

			for (int g = 0; g < other.size(); ++g) {
				otherCandidates += other.get(g) + " ";
			}

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Parameterized constructor. Used to get candidatesList from video text
	 * 
	 * @param file
	 *            , dictionary
	 */
	public static List<ArrayList<String>> name(List<String> file,
			List<String> dictionary) throws FileNotFoundException {
		int m = 0;
		List<ArrayList<String>> dictionaryWords = new ArrayList<ArrayList<String>>();
		List<ArrayList<String>> outputList = new ArrayList<ArrayList<String>>();
		ArrayList<String> speakerList = new ArrayList<String>();
		for (int i = 0; i < dictionary.size(); ++i) {
			int k = 0;

			ArrayList<String> list1 = new ArrayList<String>();
			for (int j = 0; j < dictionary.get(i).length(); ++j) {
				char letter = dictionary.get(i).charAt(j);
				if ((letter == ' ')) {

					list1.add(dictionary.get(i).substring(k, j));

					k = j + 1;
				}
			}

			list1.add(dictionary.get(i)
					.substring(k, dictionary.get(i).length()));
			dictionaryWords.add(list1);

		}

		while (m < file.size()) {
			String fileInput = file.get(m);

			for (int h = 0; h < dictionaryWords.size(); ++h) {
				if (dictionaryWords.get(h).contains(fileInput)) {

					if (file.get(m + 1).equals("approve")) {
						// System.out.println("The speaker is" +
						// dictionaryWords.get(h).toString());
						speakerList = dictionaryWords.get(h);

					} else
						for (int f = 0; f < dictionaryWords.get(h).size(); ++f) {
							if (!(dictionaryWords.get(h).get(f)
									.equals(fileInput))) {
								String otherWord = dictionaryWords.get(h)
										.get(f);
								if (file.contains(otherWord)) {

									outputList.add(dictionaryWords.get(h));
								}
							}
						}

				}

			}
			m++;
		}
		outputList.add(speakerList);
		return outputList;

	}

	public static String Speaker(List<ArrayList<String>> input) {
		ArrayList<String> speaker = new ArrayList<String>();
		speaker = input.get(input.size() - 1);
		String Speaker = "";
		// System.out.print("Speaker is ");
		for (int o = 0; o < speaker.size(); ++o) {
			Speaker = Speaker + speaker.get(o) + " ";
		}

		return Speaker;

	}

	public static ArrayList<String> otherNames(List<ArrayList<String>> input) {
		ArrayList<String> output = new ArrayList<String>();
		for (int i = 0; i < input.size() - 1; ++i) {
			String otherperson = "";
			ArrayList<String> othername = input.get(i);
			for (int o = 0; o < othername.size(); ++o) {
				otherperson = otherperson + othername.get(o) + " ";
			}
			output.add(otherperson);
		}

		Set<String> foo = new HashSet<String>(output);

		ArrayList<String> list = new ArrayList<String>(foo);

		return list;
	}

	public static void main(String[] args) {

	}
}