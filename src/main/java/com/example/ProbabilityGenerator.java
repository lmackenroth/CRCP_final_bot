/*
 * Name: Lizzy Mackenroth
 * Date: 9/13/2023
 * Description: Project 1;p this is the Probabliity Generator and for this part just the method 
 * "Test" and "Generate" were built. This takes in the stored pitches and rythemes and uses them to create more songs
 * based on the probabilities generated
 * 
 * Class: ProbabliityGenerator
 * 
 */


package com.example;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class ProbabilityGenerator <E>
{
	ArrayList <E> alpha = new ArrayList<E>(); //list of tokens
	ArrayList <Float> listofcounts = new ArrayList<Float>(); //list of instances of tokens
	double numberofsymbols = 0; //the number of tokens in total

	//nested convenience class to return two arrays from sortArrays() method
	//students do not need to use this class
	void train(ArrayList<E> tokens){ //is sent an array list of general types generated in main from midi
		//make train here somehow
		//see if it appears
		
		//add to array if it does
		// this is not done!! still alot of problems!!
		for (int i = 0; i <= tokens.size() - 1; i++)
		{
				//index = find index of tokens[i] in alphabet
				int index = alpha.indexOf(tokens.get(i));
		
				if (index == -1) //i need to see how to make this a boolean
				{
						index = alpha.size();//make index the size of the alpha array list
						alpha.add(tokens.get(i));//add the new tokens to the main alphabet array list (alpha)
						listofcounts.add((float) 0); //add zero to array list of counts if index is 0
				} 
				
				listofcounts.set(index, listofcounts.get(index) + 1); //adds to the list of countd if it already exsists

			
				numberofsymbols++; //counts every times theres an instance
		}
	}

	protected class SortArraysOutput
	{
		public ArrayList<E> symbolsListSorted;
		public ArrayList<Float> symbolsCountSorted;
	}

	//sort the symbols list and the counts list, so that we can easily print the probability distribution for testing
	//symbols -- your alphabet or list of symbols (input)
	//counts -- the number of times each symbol occurs (input)
	//symbolsListSorted -- your SORTED alphabet or list of symbols (output)
	//symbolsCountSorted -- list of the number of times each symbol occurs inorder of symbolsListSorted  (output)
	public SortArraysOutput sortArrays(ArrayList<E> symbols, ArrayList<Float> counts)	{
		SortArraysOutput sortArraysOutput = new SortArraysOutput(); 
		
		sortArraysOutput.symbolsListSorted = new ArrayList<E>(symbols);
		sortArraysOutput.symbolsCountSorted = new ArrayList<Float>();
		//sort the symbols list
		Collections.sort(sortArraysOutput.symbolsListSorted, new Comparator<E>() {
			@Override
			public int compare(E o1, E o2) {
				return o1.toString().compareTo(o2.toString());
			}
		});
		//use the current sorted list to reference the counts and get the sorted counts
		for(int i=0; i<sortArraysOutput.symbolsListSorted.size(); i++)
		{
			int index = symbols.indexOf(sortArraysOutput.symbolsListSorted.get(i));
			sortArraysOutput.symbolsCountSorted.add(counts.get(index));
		}
		return sortArraysOutput;
	}

	ArrayList<E> generate(int numberofNotes){
		ArrayList<Double> probability = new ArrayList<Double>();// initial probability when listofcounts is divided by the double
		ArrayList<Double> sumProb = new ArrayList<Double>();// added up probabilities for distrabution
		ArrayList<E> storedNotes = new ArrayList<E>(); //generated pitches or rythems
		double randomIndex;//random number stored
		double divideInst = 0;//divide probability by whole
		
		for(int i = 0; i < listofcounts.size(); i++ )
		{
			//create your distribution array -- division loop through listofcounts from train and divide by number
			//of instances
			divideInst = listofcounts.get(i) / numberofsymbols;
			probability.add(divideInst);
			
		}
		for(int i = 0; i < numberofNotes; i++){//loops number of notes we want to generate
			
			double probSum = 0;//range of probaility distrabution
			randomIndex = Math.random();
			for(int j = 0; j < listofcounts.size(); j++){
		
				probSum += probability.get(j);//adds up to one eventually, but adds each proability to the last sum
				//sumProb.add(probSum);//adds new probability to the sum probability array list
				//gernerates between 0 and 1
				if(randomIndex < probSum){
					storedNotes.add(alpha.get(j));//adds the note at i to the idex of stored notes
					break;//don't want an infinite loop
				}
			}
			
		}
		//System.out.println(storedNotes);
		return storedNotes;
	}


	E returnToken (ArrayList<Float> inList){// returns token using probability generator/ distrabution
		
		double probSum = 0;//range of probaility distrabution
		
		float sum = 0.0f;
		for(int i = 0; i < inList.size(); i++){

			sum = inList.get(i) + sum;

		}
		double randomIndex = Math.random()*sum;
		
			for(int j = 0; j < inList.size(); j++){
				
				probSum += inList.get(j);
				if(randomIndex < probSum){
					return alpha.get(j);
		
				}
			}
		return alpha.get(alpha.size() - 1);
			

	}
	//Students should USE this method in your unit tests to print the probability distribution
	//HINT: you can overload this function so that it uses your class variables instead of taking in parameters
	//boolean is FALSE to test train() method & TRUE to test generate() method
	//symbols -- your alphabet or list of symbols (input)
	//counts -- the number of times each symbol occurs (input)
	//sumSymbols -- the count of how many tokens we have encountered (input)
	public void printProbabilityDistribution(boolean round, ArrayList<E> symbols, ArrayList<Float> counts, double sumSymbols)
	{
		//sort the arrays so that elements appear in the same order every time and it is easy to test.
		SortArraysOutput sortResult = sortArrays(symbols, counts);
		ArrayList<E> symbolsListSorted = sortResult.symbolsListSorted;
		ArrayList<Float> symbolsCountSorted = sortResult.symbolsCountSorted;

		System.out.println("-----Probability Distribution-----");
		for (int i = 0; i < symbols.size(); i++)
		{
			if (round){
				DecimalFormat df = new DecimalFormat("#.##");
				System.out.println("Data: " + symbolsListSorted.get(i) + " | Probability: " + df.format((double)symbolsCountSorted.get(i) / sumSymbols));
			}
			else
			{
				System.out.println("Data: " + symbolsListSorted.get(i) + " | Probability: " + (double)symbolsCountSorted.get(i) / sumSymbols);
			}
		}		
		System.out.println("------------");
	}
	public void printProbabilityDistribution(boolean round) //is sent a boolean 
	{
		printProbabilityDistribution(round, alpha, listofcounts,numberofsymbols);

	}
}
	//overloads the generator 
