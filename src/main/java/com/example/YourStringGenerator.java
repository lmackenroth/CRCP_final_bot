/*
 * c2017-2023 Courtney Brown 
 * 2023 update: Discontinued Jaunt & replaced w Jsoup for web-scraping since Jaunt is not supported by Maven 
 * Class: Twitterbot Project Template
 * Description: This is a template for a Twitterbot that uses machine learning (a Markov Chain) to generate text. Allows for training via text file and web-scraping.
 *
 * 
 * For an example see: https://twitter.com/GeorgetteRomanc
 * It was trained with searching "passive aggressive" for tweets plus the included Georgette Heyer regency romance novel. The posts this year do not use the tweets since I do not want to pay $100 for that priviledge, though.
 */

package com.example;

import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class YourStringGenerator {

	// make cross-platform
	static FileSystem sys = FileSystems.getDefault();

	// potential sources for data -- text file or web page/google search
	static String filePath = "data" + sys.getSeparator() + "phil (1).txt"; // path to the midi file -- you can
																			// change this to your file //
																			// location/nam
	 // CB: I don't remember the current char limit. You can change this if
													// you want to reflect reality.

	// these constant strings containing types of characters for filtering out, etc.
	// in the text
	private static final String fPUNCTUATION = "\",.!?;:(){}*[]/\\";
	private static final String fENDPUNCTUATION = ".!?;,";
	private static final String fREALENDPUNCTUATION = ".!?";// location/name
	private static final String fWHITESPACE = "\t\r\n ";
	private static final String fNUMBERS = "1234567890 ";

    public static String generateString() {
    	
		ArrayList<String> novelTokens = parseTextFile(); 
		System.out.println("Token size:" + novelTokens.size());



			MOrderMarkovGenerator<String> word = new MOrderMarkovGenerator<String>(3);
			word.trainM(novelTokens);			
			//MarkovChainGenerator<String> trainWords = new MarkovChainGenerator<String>();
			ArrayList<String> newWords= new ArrayList<String>();
            System.out.println("start generate");
			newWords = word.generate(30); 
            System.out.println("End generate");
			//(newWords);
			//cleanText(newWords);

			String words = "";
				
			for(int i = 0; i < newWords.size(); i++){
				
				
				String oneWord = newWords.get(i);
				oneWord.replaceAll("[^\\sa-zA-Z0-9]", "") ;
				// if(oneWord == "("){
				oneWord.replaceAll(fNUMBERS, "") ;

				words += newWords.get(i) + " ";
				


				
			}
			System.out.print(words);
        return words;
    }
	

	
	static ArrayList<String> cleanText(ArrayList<String> cleanThis){
			for(int i = 0; i < cleanThis.size(); i++){
				cleanThis.get(i).replaceAll(fPUNCTUATION, "");

			}

			return cleanThis;




	}
	// but you can if you want more functionality
	static ArrayList<String> parseTextFile() {
		Path path = Paths.get(filePath);
		ArrayList<String> tokens = new ArrayList<String>();

		try { 

			List<String> lines = Files.readAllLines(path);

			for (int i = 0; i < lines.size(); i++) {

				TextTokenizer tokenizer = new TextTokenizer(lines.get(i));
				tokens.addAll(tokenizer.parseText());
				


			}

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Oopsie! We had a problem reading a file!");
		}
		return tokens;
	}
}