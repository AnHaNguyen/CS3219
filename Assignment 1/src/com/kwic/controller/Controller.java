package com.kwic.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


public class Controller {
	public static final String SPACE = " ";
	
	/**
	 * default constructor
	 */
	public Controller(){}

	/**
	 * generate kwic index lines from input lines given the ignore words
	 * @param ignoreWords
	 * @param inputLines
	 * @return List of KWIC Index Lines
	 */
	public List<String> generateKWICIndexLinesByPipelineAndFilters(
			List<String> ignoreWords, List<String> inputLines){	
		List<String> shiftedInputLines = circularShift(inputLines);
		List<String> unorderedKWICIndexLines = removeIgnoredLines(shiftedInputLines, ignoreWords);
		List<String> KWICIndexLines = alphabetize(unorderedKWICIndexLines);
		return KWICIndexLines;
	}
	
	/** 
	 * preprocess the input lines to trim and remove unnecessary spaces
	 * @param inputLines
	 * @return list of input lines after preprocessing
	 */
	public List<String> preprocessInputLines(List<String> inputLines) {
		return inputLines.stream().map(line -> preprocessLine(line)).collect(Collectors.toList());
	}
	
	private String preprocessLine(String line) {
		return line.trim().replaceAll(" +", SPACE);
	}
	/**
	 * preprocess ignore words to remove invalid words and trim
	 * @param ignoreWords
	 * @return list of ignore words after preprocessing
	 */
	public List<String> preprocessIgnoreWords(List<String> ignoreWords) {
		return ignoreWords.stream()
				.map(word -> word.trim())
				.filter(word -> word.split(SPACE).length == 1)
				.collect(Collectors.toList());
	}
	
	/**
	 * circular shift each member of a list
	 * @param list
	 * @return a list containing circular shifts of all members
	 */
	private List<String> circularShift(List<String> list) {
		List<String> circularShiftedList = new ArrayList<>();
		for (int i = 0; i < list.size(); i++) {
			String line = list.get(i);
			int tokenNum = line.split(SPACE).length;
			for (int j = 0; j < tokenNum; j++) {
				circularShiftedList.add(line);
				line = circularShiftLine(line);
			}
		}
		return circularShiftedList;
	}
	
	/**
	 * circularShift the first word of a line to last position
	 * assume the lines are trimmed and has 1 space between words
	 * @param line
	 * @return the line after circular shifted the first word
	 */
	private static String circularShiftLine(String line) {
		int firstWordIndex = line.indexOf(SPACE);
		if (firstWordIndex == -1) { 		// only 1 word
			return line;
		}
		String firstWord = SPACE + line.substring(0, firstWordIndex);
		return line.substring(firstWordIndex + 1).concat(firstWord);
	}
	
	/**
	 * remove all circular shifted lines that start with a ignore word
	 * @param inputLines
	 * @param ignoreWords
	 * @return the list after ignored lines have been removed
	 */
	private static List<String> removeIgnoredLines(
			List<String> inputLines, List<String> ignoreWords) {
		List<String> KWICIndexesLines = new ArrayList<>();
		for (int i = 0; i < inputLines.size(); i++) {
			String firstWord = inputLines.get(i).split(SPACE)[0];
			if (ignoreWords.stream()
					.filter(word -> word.equalsIgnoreCase(firstWord))
					.count() == 0) { 		//if firstWord is not in ignore words
				KWICIndexesLines.add(inputLines.get(i));
			}
		}
		return KWICIndexesLines;
	}
	
	/**
	 * alphabetize a list of String
	 * @param list
	 * @return list after sorted
	 */
	private static List<String> alphabetize(List<String> list) {
		Collections.sort(list);
		return list;
	}
}
