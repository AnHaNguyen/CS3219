package com.kwic.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class Controller {
	public static final String SPACE = " ";
	public static final String EXIT = "exit";
	
	/**
	 * default constructor
	 */
	public Controller(){
		
	}

	/**
	 * generate kwic index lines from input lines given the ignore words
	 * @param ignoreWords
	 * @param inputLines
	 * @return List of KWIC Index Lines
	 */
	public List<String> generateKWICIndexLines(
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
