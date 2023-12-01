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
// Showcase Project: Discord and Twitterbot
//Lizzy Mackenroth
//THIS IS THE MAIN CLASS FOR THE Twitter BOT, RUN THIS FOR Twitter BOT; 

public class TwitterBot {

	// make cross-platform
	static FileSystem sys = FileSystems.getDefault();

	// potential sources for data -- text file or web page/google search
	static String filePath = "data" + sys.getSeparator() + "phil (1).txt"; // path to the midi file -- you can
																			// change this to your file //
																			// location/nam
	static Scraper scraper = new Scraper(); // for scraping webpages

	private static int TWITTER_CHAR_LIMIT = 200; // CB: I don't remember the current char limit. You can change this if
													// you want to reflect reality.

	// these constant strings containing types of characters for filtering out, etc.
	// in the text
	private static final String fPUNCTUATION = "\",.!?;:(){}*[]/\\";
	private static final String fENDPUNCTUATION = ".!?;,";
	private static final String fREALENDPUNCTUATION = ".!?";// location/name
	private static final String fWHITESPACE = "\t\r\n ";
	private static final String fNUMBERS = "1234567890 ";


	private static final TwitterInteraction twitter = new TwitterInteraction(); // handles twitter api
	private static final MyListener philBot = new MyListener();	
	

	public static void main(String[] args) {

		// TODO: comment if you are not training via a text file
		// loads the novel referenced by filePath -- remember to change variable names
		// if you are not training on a novel....
		ArrayList<String> novelTokens = parseTextFile(); // turns the text (a String) into an ArrayList<String> of words
															// (that is, it parses the text)
		System.out.println("Token size:" + novelTokens.size());// how many words/punctuation (i.e., tokens) in the novel
		// System.out.println(novelTokens);
		// TODO: import, create and train an your Markov Chain to generate text for
		



			MOrderMarkovGenerator<String> word = new MOrderMarkovGenerator<String>(3);
			word.trainM(novelTokens);			
			//MarkovChainGenerator<String> trainWords = new MarkovChainGenerator<String>();
			ArrayList<String> newWords= new ArrayList<String>();
			newWords = word.generate(30); 
			//(newWords);
			//cleanText(newWords);

			String words = "";
				
			for(int i = 0; i < newWords.size(); i++){
				
				
				String oneWord = newWords.get(i);
				oneWord.replaceAll("[^\\sa-zA-Z0-9]", "") ;
				// if(oneWord == "("){
				oneWord.replaceAll(fNUMBERS, "") ;

				// 	System.out.print(newWords.get(i));
				// }
				// if(oneWord == ")"){
				// 	System.out.print(newWords.get(i-1) + newWords.get(i) + " ");
				// }
				// if(oneWord == "."){
				// 	System.out.print(newWords.get(i-1) + newWords.get(i) + " ");
				// }
				
				//System.out.print(newWords.get(i) + " ");
				words += newWords.get(i) + " ";
				


				
			}
			System.out.print(words);
			//MyListener Listen = new MyListener(words);
		

		//Heres what I want to do!!
		//if there is white space before punctuation, delete white space
		


		// trains the array list
		// maybe make a for loop the length of novelTokens and delete a token if funky
		

		// 1.things to make better
		// delete words after the last period from newSentence
		// delete quotations before
		// make sure sentence isn't too short? maybe set to certain amount of tokens
		// produced
		// make my markov of M work

		// NOTE: In the end, text should be more or less intelligible. It does not have
		// to be perfect, but it should be somewhat coherent.
		// Feel free to generate a lot and then delete the tweets that don't make sense
		// and keep ones that are amusing/surprising, etc. on your page.

		// I am NOT doing webscrapping

		// TODO: uncomment the code before to post to twitter
		// Make sure within Twitter limits (used to be 140 but now is more?)
		//String status = "I am testing again with new auth and app. Sweet!";
		twitter.updateTwitter(words); //uncomment this to update twitter
		
		
		
		

	}
	

	// private static String removePunctuation(String input) {
	// // Using regular expression to remove punctuation
	// return input.replaceAll("\\p{Punct}", "");
	// }

	// parses the text file into tokens -- does not necessarily need to be altered
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

		try { // NOTE: this is a try-catch block. It's a way to handle errors. You can read
				// about it online. It's not necessary for you to understand it for this class.
				// Basically, the following code might fail, so we need to catch the errors and
				// handle it.

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