/* Programmer: Courtney Brown and anonymous webcoder whose name I lost.
 * Date: Created Fall 2018, Modified 2019, 2020 (removed Set & used ArrayList instead), 2023
 * Desc:  Turns input text into tokens. Removes whitespace, some punctuation, etc. 
 *  The original code was lifted from an online example and then modified. The link got lost in the internet and time. mea culpa. I know, so unacceptable.
 */

package com.example;

import java.util.*;

public class TextTokenizer {

	/**
	  * @param aSearchText is non-null, but may have no content,
	  * and represents what the user has input in a search box.
	  */
	  //CB note: this is not used in the current application
	  public TextTokenizer(String aSearchText) {
	    if (aSearchText == null) {
	      throw new IllegalArgumentException("Search Text cannot be null.");
	    } 
	    fSearchText = aSearchText;
	  } 

	/**
	 * Orginally, Parse the user's search box input into a Set of String tokens.
	 * But it parses any string now -- modified by Courtney Brown
	 * 
	 * @return ArrayList of Strings, one for each word in fSearchText; here "word" is
	 *         defined as either a lone word surrounded by whitespace, or as a
	 *         series of words surrounded by double quotes, "like this"; 
	 *         
	 *         also the code may be modified so that very
	 *         common words (and, the, etc.) do not qualify as possible search
	 *         targets, but this was bah-leted as it is not necessarily useful for training markov chains
	 */
	public ArrayList<String> parseText() {
		ArrayList<String> result = new ArrayList<>();

		boolean returnTokens = true;
		String currentDelims = fWHITESPACE_AND_QUOTES_PUNCTUATION;
		StringTokenizer parser = new StringTokenizer(fSearchText, currentDelims, true);

		String token = null;
		while (parser.hasMoreTokens()) {
			token = parser.nextToken(currentDelims);
			if (!isWhitespace(token) && !fQUOTES_ONLY.contains(token)) {// && !(isHash(token))) {
				addNonTrivialWordToResult(token, result); //totally misnomer bc this adds ALL words now
			} 
		}
		return result;
	}

	// PRIVATE
	private String fSearchText; //the text for search for
	private static final Set<String> fCOMMON_WORDS = new LinkedHashSet<>(); //words to ignore in searches -- I don't use this, though, since I want common words -- this was originally for another use case

	// the parser flips between these two sets of delimiters
	private static final String fWHITESPACE_AND_QUOTES_PUNCTUATION = " \t\r\n\",.!?;:()/\\";
	private static final String fWHITESPACE = " \t\r\n";
	private static final String fQUOTES_ONLY = "\"";
	
	private static final String fHASHES = "#@"; 

	/** Very common words to be excluded from searches. Note: but NOT excluded from training a bot! */
	static {
		fCOMMON_WORDS.add("a");
		fCOMMON_WORDS.add("and");
		fCOMMON_WORDS.add("be");
		fCOMMON_WORDS.add("for");
		fCOMMON_WORDS.add("from");
		fCOMMON_WORDS.add("has");
		fCOMMON_WORDS.add("i");
		fCOMMON_WORDS.add("in");
		fCOMMON_WORDS.add("is");
		fCOMMON_WORDS.add("it");
		fCOMMON_WORDS.add("of");
		fCOMMON_WORDS.add("on");
		fCOMMON_WORDS.add("to");
		fCOMMON_WORDS.add("the");
	}

	/**
	 * Use to determine if a particular word entered in the search box should be
	 * discarded from the search.
	 */
	private boolean isCommonWord(String aSearchTokenCandidate) {
		return fCOMMON_WORDS.contains(aSearchTokenCandidate);
	}

	private boolean textHasContent(String aText) {
		return (aText != null) && (!aText.trim().equals(""));
	}

	private void addNonTrivialWordToResult(String aToken, ArrayList<String> aResult) {
			aResult.add(aToken.trim());
	}
	
	private boolean isWhitespace(String aToken) {
		return fWHITESPACE.contains(aToken);
	}
	
	private boolean isHash(String aToken)
	{
		String first = aToken.substring(0, 1);
		return fHASHES.contains(first);
	}

}
