
//Programmer: Courtney Brown
//Date: 2017-23
//Class: MOrderMarkovGenerator
//Desc: Markov Generator that uses a m-order markov chain, Class Project 3 Template

package com.example;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class MOrderMarkovGenerator<E> extends MarkovChainGenerator<E> {

	int mOrder = 2;
	ArrayList<ArrayList<E>> uniqueAlphabetSequences = new ArrayList<ArrayList<E>>();

	// nested convenience class to return two arrays from sortTransitionTable()
	// method
	// students do not need to use this class
	protected class SortTTOutputSequences {
		public ArrayList<E> symbolsListSorted;
		public ArrayList<ArrayList<E>> sequencesSorted;
		ArrayList<ArrayList<Float>> ttSorted;
	}

	MOrderMarkovGenerator(int _mOrder) {
		mOrder = _mOrder;
	}

	void trainM(ArrayList<E> newSequences) {
		// call constrructor in app

		if (alpha == null) {
			alpha = new ArrayList<E>();
		}
		// stop it early
		// step by one and start at morder so it more correctly steps through
		for (int i = mOrder - 1; i < newSequences.size() - 1; i++) {

			E nextToken = newSequences.get(i + 1);
			// System.out.print(mOrder);

			// container array
			ArrayList<E> curSequence = new ArrayList<E>();
			// loop to fill up to order
			// insures that it doesn't go negative
			// System.out.println(i - mOrder - 1);
			for (int j = i - (mOrder - 1); j <= i; j++) {
				// add two tokens at a time
				curSequence.add(newSequences.get(j));
			}
			// System.out.println(curSequence);
			int rowIndex = uniqueAlphabetSequences.indexOf(curSequence);

			if (rowIndex == -1) {

				rowIndex = uniqueAlphabetSequences.size();
				uniqueAlphabetSequences.add(curSequence);
				ArrayList<Float> newRow = new ArrayList<Float>();
				// add a new row to the transition table the size of the alphabet
				for (int j = 0; j < alpha.size(); j++) {

					// create a row and fil with zeros
					newRow.add(0f);

				}
				probabilityTable.add(newRow);
				// add row
			}

			// finds which collum has this token
			int tokenIndex = alpha.indexOf(nextToken);

			// if it doesn't exist in the alpha bet
			if (tokenIndex == -1) {
				// set tokenindex to be the so
				tokenIndex = alpha.size();

				alpha.add(nextToken);

				for (int j = 0; j < probabilityTable.size(); j++) {
					probabilityTable.get(j).add(0f);

				}

			} // if statement for if it doesn't exist in alpha
			listofcounts.add((float) 0);

			listofcounts.set(rowIndex, listofcounts.get(rowIndex) + 1f);
			if (rowIndex > -1) // that is, we have a previous token so its not the 1st time thru
			{

				Float hold = probabilityTable.get(rowIndex).get(tokenIndex);// grabs the current token its on
				probabilityTable.get(rowIndex).set(tokenIndex, hold + 1.0f);// add 1 to it

			}
		}

		// listofcounts.set(rowIndex, listofcounts.get(rowIndex) + 1f);

	} // big loop

	ArrayList<E> generate(int numberofTokens) {

		double random = Math.random() * uniqueAlphabetSequences.size();

		ArrayList<E> init = uniqueAlphabetSequences.get((int) random);

		ArrayList<E> outlist = new ArrayList<E>();

		for (int i = 0; i < init.size(); i++) {

			outlist.add(init.get((i)));

		}

		return generate(outlist, numberofTokens);

	}

	E generate(ArrayList<E> initSeq) {
		int curSeqIndex = uniqueAlphabetSequences.indexOf(initSeq);
		// System.out.println("hello is this printing??");
		if (curSeqIndex == -1) {
			// There are many possibilities for this. For instance, you could train a Markov
			// Chain of order 1 & generate with those. However, we will instead return a
			// null, as we are going to rollback our previous generated token (eg. erase it
			// and generate a new).
			// train(initSeq);
			return null;
		} else {
			// 1. find the row in the transition table using curSeqIndex
			// 2. generate from that row using your Probability Generator
			// System.out.println(("hello" + curSeqIndex));
			ArrayList<Float> row = probabilityTable.get(curSeqIndex);
			return super.returnToken(row);
			// System.out.println(curtoken);

			// return tokenidk; //note: remember to handle 0% probability across all tokens
		}
	}

	ArrayList<E> generate(ArrayList<E> initSeq, int numTokensToGen) {

		ArrayList<E> outputMelody = new ArrayList<E>(initSeq);
		//initSeq.size() = initSeq.size() - numTokensToGen;

		for (int i = 1; i < numTokensToGen ; i++) {
			// 1. call your single generate using your initSeq
			E genToken = generate(initSeq);
			// System.out.println(genToken);

			// if you receive a null from generate,
			if (genToken == null) {
				// you should remove the last token generated from outputMelody and initSeq,
				outputMelody.remove(outputMelody.size() - 1);
				initSeq.remove(initSeq.size() - 1);
				

				// decrement the index,
				i--;
				// and return to step 1.
				initSeq.add(outputMelody.get(outputMelody.size() - 1));


				genToken = generate(initSeq);
				//initSeq.add(genToken);

			}

			// 3. remove the first token you added from your initSeq
			initSeq.remove(0);
			//// 4. add the generated token to your initSeq
			initSeq.add(genToken);
			// 5. add the generated token to outputMelody
			outputMelody.add(genToken);

		}
		return outputMelody;
	}

	// sort ]the symbols list and the counts list, so that we can easily print the
	// probability distribution for testing
	// symbols -- your alphabet or list of symbols (input)
	// sequences -- your list of sequences of symbols found (input)
	// tt -- the unsorted transition table (input)
	// symbolsListSorted -- your SORTED alphabet or list of symbols (output)
	// ttSorted -- the transition table that changes reflecting the symbols sorting
	// to remain accurate (output)
	public SortTTOutputSequences sortTTSequences(ArrayList<ArrayList<E>> sequences, ArrayList<E> symbols,
			ArrayList<ArrayList<Float>> tt) {

		SortTTOutputSequences sortArraysOutput = new SortTTOutputSequences();

		sortArraysOutput.symbolsListSorted = new ArrayList<E>(symbols);
		sortArraysOutput.ttSorted = new ArrayList<ArrayList<Float>>();
		sortArraysOutput.sequencesSorted = new ArrayList<ArrayList<E>>(sequences);

		// sort the symbols list
		Collections.sort(sortArraysOutput.symbolsListSorted, new Comparator<E>() {
			@Override
			public int compare(E o1, E o2) {
				return o1.toString().compareTo(o2.toString());
			}
		});

		// sort the sequences list
		Collections.sort(sortArraysOutput.sequencesSorted, new Comparator<ArrayList<E>>() {
			@Override
			public int compare(ArrayList<E> o1, ArrayList<E> o2) {
				return o1.toString().compareTo(o2.toString());
			}
		});

		// use the current sorted lists to reference the counts & sequences and get the
		// sorted counts
		for (int i = 0; i < sortArraysOutput.sequencesSorted.size(); i++) {
			int index = sequences.indexOf(sortArraysOutput.sequencesSorted.get(i));
			sortArraysOutput.ttSorted.add(new ArrayList<Float>());
			for (int j = 0; j < tt.get(index).size(); j++) {
				int index2 = symbols.indexOf(sortArraysOutput.symbolsListSorted.get(j));
				sortArraysOutput.ttSorted.get(i).add(tt.get(index).get(index2));
			}
		}

		return sortArraysOutput;
	}

	// by default, don't round. See below for the other printProbabilityDistribution
	// method functionality
	public void printProbabilityDistribution(ArrayList<ArrayList<E>> sequences, ArrayList<E> symbols,
			ArrayList<ArrayList<Float>> tt, int mOrder) {
		printProbabilityDistribution(false, sequences, symbols, tt, mOrder);
	}

	// this prints the transitionTable
	// round -- whether to round the probabilities to 2 decimal places (input) --
	// Training unit tests should be run with round = false, Generate, with round =
	// true
	// sequences -- your list of sequences of symbols found (input)
	// symbols -- your alphabet or list of symbols (input)
	// tt -- the unsorted transition table -- NOTE!!!: expects **probabilities** NOT
	// counts in the table (input)
	// mOrder -- the order of the markov chain (input)
	public void printProbabilityDistribution(boolean round, ArrayList<ArrayList<E>> sequences, ArrayList<E> symbols,
			ArrayList<ArrayList<Float>> tt, int mOrder) {

		SortTTOutputSequences sortArraysOutput = sortTTSequences(sequences, symbols, tt);
		ArrayList<ArrayList<Float>> ttSorted = sortArraysOutput.ttSorted;
		ArrayList<E> symbolsSorted = sortArraysOutput.symbolsListSorted;
		ArrayList<ArrayList<E>> sequencesSorted = sortArraysOutput.sequencesSorted;

		System.out.println("-----Transition Table for Order " + mOrder + "-----");

		if (sequencesSorted.size() > 0) {
			String sym = sequencesSorted.get(0).toString();
			for (int k = 0; k < sym.length(); k++) {
				System.out.print(" ");
			}
		}

		System.out.println(symbolsSorted);

		for (int i = 0; i < ttSorted.size(); i++) {
			System.out.print(sequencesSorted.get(i) + " ");

			for (int j = 0; j < ((ArrayList) ttSorted.get(i)).size(); j++) {

				Float out = ((ArrayList<Float>) ttSorted.get(i)).get(j);

				// send to my summing method
				double sum = addRows(ttSorted.get(i));
				// divide given element
				double divided = 0;

				if (sum != 0) {
					divided = out / sum;
				}

				if (round) {
					DecimalFormat df = new DecimalFormat("#.##");
					System.out.print(df.format(divided) + " ");
				} else {
					DecimalFormat df = new DecimalFormat("#.####");
					System.out.print(df.format(divided) + " ");
				}

			}
			System.out.println();
		}

		System.out.println("------------");
	}

	public void printProbabilityDistribution(boolean round) {// overrides
		printProbabilityDistribution(uniqueAlphabetSequences, alpha, probabilityTable, mOrder);

	}
}
