package search;

import java.io.*;
import java.util.*;

/**
 * This class encapsulates an occurrence of a keyword in a document. It stores the
 * document name, and the frequency of occurrence in that document. Occurrences are
 * associated with keywords in an index hash table.
 * 
 * @author Sesh Venugopal
 * 
 */
class Occurrence {
	/**
	 * Document in which a keyword occurs.
	 */
	String document;
	
	/**
	 * The frequency (number of times) the keyword occurs in the above document.
	 */
	int frequency;
	
	/**
	 * Initializes this occurrence with the given document,frequency pair.
	 * 
	 * @param doc Document name
	 * @param freq Frequency
	 */
	public Occurrence(String doc, int freq) {
		document = doc;
		frequency = freq;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return "(" + document + "," + frequency + ")";
	}
}

/**
 * This class builds an index of keywords. Each keyword maps to a set of documents in
 * which it occurs, with frequency of occurrence in each document. Once the index is built,
 * the documents can searched on for keywords.
 *
 */
public class LittleSearchEngine {
	
	/**
	 * This is a hash table of all keywords. The key is the actual keyword, and the associated value is
	 * an array list of all occurrences of the keyword in documents. The array list is maintained in descending
	 * order of occurrence frequencies.
	 */
	HashMap<String,ArrayList<Occurrence>> keywordsIndex;
	
	/**
	 * The hash table of all noise words - mapping is from word to itself.
	 */
	HashMap<String,String> noiseWords;
	
	/**
	 * Creates the keyWordsIndex and noiseWords hash tables.
	 */
	public LittleSearchEngine() {
		keywordsIndex = new HashMap<String,ArrayList<Occurrence>>(1000,2.0f);
		noiseWords = new HashMap<String,String>(100,2.0f);
	}
	
	/**
	 * This method indexes all keywords found in all the input documents. When this
	 * method is done, the keywordsIndex hash table will be filled with all keywords,
	 * each of which is associated with an array list of Occurrence objects, arranged
	 * in decreasing frequencies of occurrence.
	 * 
	 * @param docsFile Name of file that has a list of all the document file names, one name per line
	 * @param noiseWordsFile Name of file that has a list of noise words, one noise word per line
	 * @throws FileNotFoundException If there is a problem locating any of the input files on disk
	 */
	public void makeIndex(String docsFile, String noiseWordsFile) 
	throws FileNotFoundException {
		// load noise words to hash table
		Scanner sc = new Scanner(new File(noiseWordsFile));
		while (sc.hasNext()) {
			String word = sc.next();
			noiseWords.put(word,word);
		}
		
		// index all keywords
		sc = new Scanner(new File(docsFile));
		while (sc.hasNext()) {
			String docFile = sc.next();
			HashMap<String,Occurrence> kws = loadKeyWords(docFile);
			mergeKeyWords(kws);
		}
		
	}

	/**
	 * Scans a document, and loads all keywords found into a hash table of keyword occurrences
	 * in the document. Uses the getKeyWord method to separate keywords from other words.
	 * 
	 * @param docFile Name of the document file to be scanned and loaded
	 * @return Hash table of keywords in the given document, each associated with an Occurrence object
	 * @throws FileNotFoundException If the document file is not found on disk
	 */
	public HashMap<String,Occurrence> loadKeyWords(String docFile) 
	throws FileNotFoundException {
		HashMap<String, Occurrence> newTable = new HashMap<String, Occurrence>();
		Scanner sc = new Scanner(new File(docFile));
		
		while (sc.hasNext()) {
			String tempWord = getKeyWord(sc.next());
			if (tempWord != null) {
				if (newTable.containsKey(tempWord) == false) {
					Occurrence first = new Occurrence(docFile, 1);
					newTable.put(tempWord, first);
				} else {
					newTable.get(tempWord).frequency++;
				}
			}
		}
		sc.close();
		return newTable;
	}
	
	/**
	 * Merges the keywords for a single document into the master keywordsIndex
	 * hash table. For each keyword, its Occurrence in the current document
	 * must be inserted in the correct place (according to descending order of
	 * frequency) in the same keyword's Occurrence list in the master hash table. 
	 * This is done by calling the insertLastOccurrence method.
	 * 
	 * @param kws Keywords hash table for a document
	 */
	public void mergeKeyWords(HashMap<String,Occurrence> kws) {
		for (String keyWord : kws.keySet()) {
			String tempWord = keyWord;
			Occurrence stuff = kws.get(keyWord);
			
			if (keywordsIndex.containsKey(tempWord) == true) {
				keywordsIndex.get(tempWord).add(stuff);
				insertLastOccurrence(keywordsIndex.get(tempWord));
			} else {
				ArrayList<Occurrence> newOccurrences = new ArrayList<Occurrence>();
				newOccurrences.add(stuff);
				keywordsIndex.put(tempWord, newOccurrences);
			}
		}
	}
	
	/**
	 * Given a word, returns it as a keyword if it passes the keyword test,
	 * otherwise returns null. A keyword is any word that, after being stripped of any
	 * TRAILING punctuation, consists only of alphabetic letters, and is not
	 * a noise word. All words are treated in a case-INsensitive manner.
	 * 
	 * Punctuation characters are the following: '.', ',', '?', ':', ';' and '!'
	 * 
	 * @param word Candidate word
	 * @return Keyword (word without trailing punctuation, LOWER CASE)
	 */
	public String getKeyWord(String word) {
		
		if (word == null || word.length() <= 0) {
			return null;
		}
		
		if (Character.isLetter(word.charAt(0)) == false) {
			return null;
		}
		
		int letter = 0;
		int last = 0;
		int less;
		
		for (int i = 0; i < word.length(); i++) {
			char c = word.charAt(i);
			if (Character.isLetter(c) == true) {
				letter = word.indexOf(c);
				break;
			} else {
				continue;
			}
		}
		
		String backwards = "";
		for (int i = word.length() - 1; i >= 0; i--) {
			backwards += word.charAt(i);
		}
		
		for (int i = 0; i < word.length(); i++) {
			char c = backwards.charAt(i);
			if (Character.isLetter(c) == true) {
				last = backwards.indexOf(c);
				break;
			} else {
				continue;
			}
		}
		
		less = word.length() - last;
		word = word.substring(letter, less);
		
		for (int i = 0; i < word.length(); i++) {
			char c = word.charAt(i);
			if (!Character.isLetter(c)) {
				return null;
			}
		}
		
		word = word.toLowerCase();
		if (noiseWords.containsKey(word) == true) {
			return null;
		}
		
		return word;
	}
	
	/**
	 * Inserts the last occurrence in the parameter list in the correct position in the
	 * same list, based on ordering occurrences on descending frequencies. The elements
	 * 0..n-2 in the list are already in the correct order. Insertion of the last element
	 * (the one at index n-1) is done by first finding the correct spot using binary search, 
	 * then inserting at that spot.
	 * 
	 * @param occs List of Occurrences
	 * @return Sequence of mid point indexes in the input list checked by the binary search process,
	 *         null if the size of the input list is 1. This returned array list is only used to test
	 *         your code - it is not used elsewhere in the program.
	 */
	public ArrayList<Integer> insertLastOccurrence(ArrayList<Occurrence> occs) {
		if (occs.size() == 1) {
			return null;
		}
		
		ArrayList<Integer> points = new ArrayList<Integer>();
		int left = 0;
		int right = occs.size() - 2;
		int insert = occs.get(occs.size() - 1).frequency;
		int midFreq = 0;
		int mid = 0;
		
		while (left <= right) {
			mid = (left + right)/2;
			points.add(mid);
			midFreq = occs.get(mid).frequency;
			
			if (midFreq == insert) {
				Occurrence occsInsert = occs.get(occs.size() - 1);
				occs.add(mid + 1, occsInsert);
				occs.remove(occs.size() - 1);
				break;
			}
			
			if (midFreq > insert) {
				left = mid + 1;
			} else {
				right = mid - 1;
			}
		}
		
		if (midFreq < insert) {
			Occurrence occsInsert = occs.get(occs.size() - 1);
			occs.add(mid, occsInsert);
			occs.remove(occs.size() - 1);
		} else {
			if (midFreq > insert) {
				Occurrence occsInsert = occs.get(occs.size() - 1);
				occs.add(mid + 1, occsInsert);
				occs.remove(occs.size() - 1);
			}
		}
		
		return points;
	}
	
	/**
	 * Search result for "kw1 or kw2". A document is in the result set if kw1 or kw2 occurs in that
	 * document. Result set is arranged in descending order of occurrence frequencies. (Note that a
	 * matching document will only appear once in the result.) Ties in frequency values are broken
	 * in favor of the first keyword. (That is, if kw1 is in doc1 with frequency f1, and kw2 is in doc2
	 * also with the same frequency f1, then doc1 will appear before doc2 in the result. 
	 * The result set is limited to 5 entries. If there are no matching documents, the result is null.
	 * 
	 * @param kw1 First keyword
	 * @param kw1 Second keyword
	 * @return List of NAMES of documents in which either kw1 or kw2 occurs, arranged in descending order of
	 *         frequencies. The result size is limited to 5 documents. If there are no matching documents,
	 *         the result is null.
	 */
	public ArrayList<String> top5search(String kw1, String kw2) {
		ArrayList<String> docs = new ArrayList<String>();
		int numDocs = docs.size();
		
		if (!keywordsIndex.containsKey(kw1) && !keywordsIndex.containsKey(kw2)) {
			return null;
		}
		
		ArrayList<Occurrence> occ1 = null;
		ArrayList<Occurrence> occ2 = null;
		
		if (keywordsIndex.containsKey(kw1) == true) {
			occ1 = keywordsIndex.get(kw1);
		}
		
		if (keywordsIndex.containsKey(kw2) == true) {
			occ2 = keywordsIndex.get(kw2);
		}
		
		if (occ2 == null) {
			for (int i = 0; i < occ1.size(); i++) {
				docs.add(occ1.get(i).document);
				numDocs++;
				if (numDocs == 5) {
					break;
				}
			}
		} else if (occ1 == null) {
			for (int j = 0; j < occ2.size(); j++) {
				docs.add(occ2.get(j).document);
				numDocs++;
				if (numDocs == 5) {
					break;
				}
			}
		} else {
			int num1 = 0;
			int num2 = 0;
			
			while (num1 < occ1.size() && num2 < occ2.size()) {
				int freq1 = occ1.get(num1).frequency;
				int freq2 = occ2.get(num2).frequency;
				
				if (freq1 >= freq2) {
					if (docs.contains(occ1.get(num1).document)) {
						num1++;
					} else {
						docs.add(occ1.get(num1).document);
						numDocs++;
						num1++;
					}
				} else {
					if (docs.contains(occ2.get(num2).document)) {
						num2++;
					} else {
						docs.add(occ2.get(num2).document);
						numDocs++;
						num2++;
					}
				}
				
				if (numDocs == 5) {
					break;
				}
			}
			
			if (numDocs < 5) {
				if (num1 < occ1.size()) {
					while (numDocs < 5 && num1 < occ1.size()) {
						if (docs.contains(occ1.get(num1).document)) {
							num1++;
						} else {
							docs.add(occ1.get(num1).document);
							numDocs++;
							num1++;
						}
					}
				} else {
					while (numDocs < 5 && num2 < occ2.size()) {
						if (docs.contains(occ2.get(num2).document)) {
							num2++;
						} else {
							docs.add(occ2.get(num2).document);
							numDocs++;
							num2++;
						}
					}
				}
			}
		}
		return docs;
	}
}