package com.kwic.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;


public class Controller {
	public static final String SPACE = " ";
	public static final String EXIT = "exit";
	
	public static void main(String args[]){
		Scanner sc = new Scanner(System.in);
		//receive words to ignore
		List<String> ignoreWords = new ArrayList<String>();
		String input = null;
		while (sc.hasNext()){
			input = sc.nextLine();
			//input has more than 1 word => end of words to ignore input
			if (input.split(SPACE).length == 1){
				ignoreWords.add(input);
			} else {
				break;
			}
		}
		//receive input lines
		List<String> inputLines = new ArrayList<>(Arrays.asList(input));
		while (sc.hasNext()){
			input = sc.nextLine();
			if (!input.equalsIgnoreCase(EXIT)){
				inputLines.add(input);
			} else {
				break;
			}
		}
		List<String> indexLines = generateKWICIndexLines(ignoreWords, inputLines);	
//		print(ignoreWords);
//		print(inputLines);
		print(indexLines);
		sc.close();
	}
	
	// used to print out a list of String
	private static void print(List<String> list){
		list.stream().forEach(System.out::println);
		System.out.println();
	}
	
	/**
	 * generate kwic index lines from input lines given the ignore words
	 * @param ignoreWords
	 * @param inputLines
	 * @return List of KWIC Index Lines
	 */
	private static List<String> generateKWICIndexLines(
			List<String> ignoreWords, List<String> inputLines){
		List<String> kwicIndexLines = new ArrayList<>();
		for (int i = 0; i < inputLines.size(); i++) {
			kwicIndexLines.addAll(
					generateKWICIndexes(ignoreWords, inputLines.get(i)));
		}
		Collections.sort(kwicIndexLines);
		return kwicIndexLines;
	}
	
	/**
	 * generate all indexes from each line given the ignore words
	 * @param ignoreWords
	 * @param line
	 * @return List of KWIC indexes from a input line
	 */
	private static List<String> generateKWICIndexes(
			List<String> ignoreWords, String line){
		List<String> indexes = new ArrayList<>();
		String[] tokens = line.split(SPACE);
		for (int i = 0; i < tokens.length; i++) {
			String startWord = tokens[i];
			if (ignoreWords.stream()
					.filter(word -> word.equalsIgnoreCase(startWord))
					.count() == 0) { 		//if startWord is not in ignore words
				indexes.add(line);
			}
			line = circularShift(line);
		}
		return indexes;
	}
	
	/**
	 * circularShift the first word of a line to last position
	 * assume the lines are trimmed and has 1 space between words
	 * @param line
	 * @return the line after circular shifted the first word
	 */
	private static String circularShift(String line) {
		int firstWordIndex = line.indexOf(SPACE);
		if (firstWordIndex == -1) { 		// only 1 word
			return line;
		}
		String firstWord = SPACE + line.substring(0, firstWordIndex);
		return line.substring(firstWordIndex + 1).concat(firstWord);
	}
}
