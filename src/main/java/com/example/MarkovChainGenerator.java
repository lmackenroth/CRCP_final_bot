package com.example;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


public class MarkovChainGenerator<E> extends ProbabilityGenerator<E>
{
	//start coding here for unit tests
	boolean round;
	ArrayList<ArrayList<Float>> probabilityTable = new ArrayList<ArrayList<Float>>();

void train(ArrayList<E> newTokens){
		int lastIndex = -1;

		if(alpha == null){
			alpha = new ArrayList<E>();
		}
		
		for(int i = 0; i < newTokens.size(); i++)
		//for each token in the input array 
		{
			E token =  newTokens.get(i);
			//tokenIndex = the index of the token in the alphabet
			int index = alpha.indexOf(token);
			if(index == -1)
			//if the current token is not found in the alphabet
			{
				//1. tokenIndex = size of alphabet
				index = alpha.size();
				ArrayList<Float> newRow = new ArrayList<Float>();
				//token doesnt exist so add a zero in place in the row
				for(int j = 0; j < alpha.size(); j++){
					//alpha.add(0);
					newRow.add( 0f);//fills with 0's but then doesn't fill with anything else
				}
				//Then add to your transition table (the array of arrays)
				probabilityTable.add(newRow);
                //That is, for each array in the transition table add 0.
				for(int j = 0; j < probabilityTable.size(); j++){
					probabilityTable.get(j).add(0f);
					//System.out.print(probabilityTable);
				}
				//4. add the token to the alphabet array 
				alpha.add(token);
				listofcounts.add((float) 0);

			}
			
            //ok, now add the counts to the transition table
			if(lastIndex > -1) //that is, we have a previous token so its not the 1st time thru
			{
			  Float hold = probabilityTable.get(lastIndex ).get(index);//grabs the current token its on
			  probabilityTable.get(lastIndex).set(index, hold + 1.0f);// add 1 to it
				
		
			}
			listofcounts.set(index, listofcounts.get(index) + 1f);//set its self +1			
			lastIndex = index; //setting current to previous for next round
			//System.out.print(probabilityTable);
		} 

	}
	E generateSingle(E initToken){

		ArrayList<Float> row = probabilityTable.get(alpha.indexOf(initToken));//if doesnt work initialize correctly

		float sumOfRow = addRows(row);

		if(sumOfRow == 0){
			row = listofcounts;
		}

		E aNote = returnToken(row);


		return aNote;

	}                          

	ArrayList<E> generate(E initToken, int numberOfTokensToGenerate){
		
		ArrayList<E> tokensGenerated = new ArrayList<E>();

		E lastToken = initToken;

		for(int i = 0; i < numberOfTokensToGenerate; i++){
			lastToken = generateSingle(lastToken);

			tokensGenerated.add(lastToken);

		}
		//this will be new song
		return tokensGenerated;
		
	} //this calls the above.

	ArrayList<E> generate(int numberOfTokensToGenerate){

		E initalToken = returnToken(listofcounts);

		ArrayList<E>  outList  = generate(initalToken, numberOfTokensToGenerate);

		return outList;
		//find initToken in the alphabet (ie, get the index of where it is)
		//Use that index to access the row (ie one array from the array of arrays) of probabilities in transitionTable
		//This array is the beginning of a probability distribution. It has all the counts. It is exactly like the array of counts that you had in Project 1. Thus, You already have a function which generates from a probability distribution.

	} //this calls the above with a random initToken
	

  	//nested convenience class to return two arrays from sortTransitionTable() method
	//students do not need to use this class
	protected class SortTTOutput
	{
		public ArrayList<E> symbolsListSorted;
		ArrayList<ArrayList<Float>> ttSorted;
	}

	//sort the symbols list and the counts list, so that we can easily print the probability distribution for testing
	//symbols -- your alphabet or list of symbols (input)
	//tt -- the unsorted transition table (input)
	//symbolsListSorted -- your SORTED alphabet or list of symbols (output)
	//ttSorted -- the transition table that changes reflecting the symbols sorting to remain accurate  (output)
	public SortTTOutput sortTT(ArrayList<E> symbols, ArrayList<ArrayList<Float>> tt)	{

		SortTTOutput sortArraysOutput = new SortTTOutput(); 
		
		sortArraysOutput.symbolsListSorted = new ArrayList<E>(symbols);
		sortArraysOutput.ttSorted = new ArrayList<ArrayList<Float>>();
	
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
			sortArraysOutput.ttSorted.add(new ArrayList<Float>());
			for( int j=0; j<tt.get(index).size(); j++)
			{
				int index2 = symbols.indexOf(sortArraysOutput.symbolsListSorted.get(j));
				sortArraysOutput.ttSorted.get(i).add(tt.get(index).get(index2));
			}
		}

		return sortArraysOutput;

	}
	
	//this prints the transition table
	//symbols - the alphabet or list of symbols found in the data
	//tt -- the transition table of probabilities (not COUNTS!) for each symbol coming after another
	public void printProbabilityDistribution(boolean round, ArrayList<E> symbols, ArrayList<ArrayList<Float>> tt)
	{
		//sort the transition table
		SortTTOutput sorted = sortTT(symbols, tt);
		symbols = sorted.symbolsListSorted;
		tt = sorted.ttSorted;

		System.out.println("-----Transition Table -----");
		
		System.out.println(symbols);
		
		for (int i=0; i<tt.size(); i++)
		{

			System.out.print("["+symbols.get(i) + "] ");
			for(int j=0; j<tt.get(i).size(); j++)
			{
				//store the element as a variable
				double elementA = tt.get(i).get(j);
				//send to my summing method
				double sum = addRows(tt.get(i));
				//divide given element
				double divided = 0;
				
				if(sum != 0){
					divided = elementA / sum;
				}
				
				if(round)
				{
					DecimalFormat df = new DecimalFormat("#.##");
					System.out.print(df.format(divided) + " ");
				}
				else
				{
					System.out.print(divided + " ");
					//(double)tt.get(i).get(j)
				}
			
			}
			System.out.println();


		}
		System.out.println();
		
		System.out.println("------------");

	}


	public void printProbabilityDistribution(boolean round){//overrides
		printProbabilityDistribution(round, alpha, probabilityTable);

	}

	public float addRows(ArrayList<Float> toAdd){
		//take in array
		float sum = 0.0f;
		for(int i = 0; i < toAdd.size(); i++){

			sum = toAdd.get(i) + sum;

		}
		
		return sum;	
		//add up each row and divide each subject by sum
		//if less than 0 return a 1 for sum



	}
	
}
