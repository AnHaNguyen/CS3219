package com.kwic.controller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


public class Controller {
	public static final String SPACE = " ";
	public static final String pathToIgnoreWordsFile = "ignore-words.txt";
	public static final String pathToInputLinesFile = "input-lines.txt";
	public static final String pathToOutputFile = "output.txt";
	
	/**
	 * default constructor
	 */
	public Controller(){}
	
	public static void main(String[] args) {
		List<String> ignoreWords = Controller.importFromFile(pathToIgnoreWordsFile);
		ignoreWords = Controller.preprocessIgnoreWords(ignoreWords);
		List<String> inputLines = Controller.importFromFile(pathToInputLinesFile);
		inputLines = Controller.preprocessInputLines(inputLines);
		List<String> KWICIndexes = Controller
				.generateKWICIndexLinesByPipelineAndFilters(
						ignoreWords, inputLines);
		KWICIndexes.forEach(System.out::println);
		Controller.outputToFile(KWICIndexes, pathToOutputFile);
	}
	
	/**
	 * generate kwic index lines from input lines given the ignore words
	 * @param ignoreWords
	 * @param inputLines
	 * @return List of KWIC Index Lines
	 */
	public static List<String> generateKWICIndexLinesByPipelineAndFilters(
			List<String> ignoreWords, List<String> inputLines){	
		List<String> shiftedInputLines = circularShift(inputLines);
		List<String> unorderedKWICIndexLines = removeIgnoredLines(shiftedInputLines, ignoreWords);
		List<String> KWICIndexLines = alphabetize(unorderedKWICIndexLines);
		return KWICIndexLines;
	}
	
	/**
	 * circular shift each member of a list
	 * @param list
	 * @return a list containing circular shifts of all members
	 */
	private static List<String> circularShift(List<String> list) {
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
			String curLine = inputLines.get(i);
			String[] tokens = curLine.split(SPACE);
			boolean isRemove = false;
			for (int j = 0; j < tokens.length; j++) {
				if (isIgnoreWord(tokens[j], ignoreWords)) {
					if (j != 0) {
						tokens[j] = tokens[j].toLowerCase();
					} else {
						isRemove = true;
					}
				} else {
					tokens[j] = tokens[j].substring(0, 1).toUpperCase() 
							+ tokens[j].substring(1).toLowerCase();
				}
			}
			if (!isRemove) {
				KWICIndexesLines.add(Arrays
						.stream(tokens).collect(Collectors.joining(SPACE)));
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
	
	/**
	 * read Data from a file
	 * @param source
	 * @return data in a file as List of String
	 */
	public static List<String> importFromFile(String source) {
		List<String> data = new ArrayList<>();
		try {
			BufferedReader br = new BufferedReader(new FileReader(source));
			String curLine;
			while ((curLine = br.readLine()) != null) {
				data.add(curLine);
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Cannot import from files");
		}
		return data;
	}
	
	/** 
	 * preprocess the input lines to trim and remove unnecessary spaces
	 * @param inputLines
	 * @return list of input lines after preprocessing
	 */
	public static List<String> preprocessInputLines(List<String> inputLines) {
		return inputLines.stream().map(line -> preprocessLine(line)).collect(Collectors.toList());
	}
	
	private static String preprocessLine(String line) {
		return line.trim().replaceAll(" +", SPACE);
	}
	
	/**
	 * preprocess ignore words to remove invalid words and trim
	 * @param ignoreWords
	 * @return list of ignore words after preprocessing
	 */
	public static List<String> preprocessIgnoreWords(List<String> ignoreWords) {
		return ignoreWords.stream()
				.map(word -> word.trim())
				.filter(word -> word.split(SPACE).length == 1)
				.collect(Collectors.toList());
	}
	
	/**
	 * output the result to a file
	 */
	public static void outputToFile(List<String> kwicIndexes, String filePath) {
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(filePath));
			for (int i = 0; i < kwicIndexes.size(); i++) {
				bw.write(kwicIndexes.get(i));
				bw.newLine();
			}
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * check if a word is in list of ignore words
	 */
	public static boolean isIgnoreWord(String word, List<String> ignoreWords) {
		return (ignoreWords.stream()
		.filter(ignoreWord -> ignoreWord.equalsIgnoreCase(word))
		.count() != 0);
	}
}
