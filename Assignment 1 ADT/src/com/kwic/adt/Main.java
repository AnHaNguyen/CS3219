package com.kwic.adt;

import java.util.*;
import java.io.*;

//Main Class where the high-level components are called
public class Main {
	public static void main(String[] args) {
		Input ip = new Input();
		CircularShift cs = new CircularShift(ip);
		AlphabeticShift as = new AlphabeticShift(cs);
		new Output(as);
	}
}

// This class processes the input and populates the data structures
class Input {
	private static ArrayList<String> lineList = new ArrayList<String>();
	private static ArrayList<String> ignoreLine = new ArrayList<String>();
	private static ArrayList<String> ignoreList = new ArrayList<String>();
	private Scanner sc = new Scanner(System.in);
	private String ignoreFile = "ignore.txt";
	private String lineFile = "line.txt";

	public Input() {
	
		System.out.println("Defaul input files should be stored in the program's directory!");
		System.out.println("Would you like to specify input paths? (Y/N)");
		if (sc.nextLine().toUpperCase().equals("Y")) {
			ignoreFile = setIgnorePath(ignoreFile);
			lineFile = setLinePath(lineFile);
		}
		
		readData(ignoreFile, ignoreLine);
		readData(lineFile, lineList);

		String currentIgnoreLine;
		for (int i = 0; i < ignoreLine.size(); i++) {
			currentIgnoreLine = ignoreLine.get(i).replaceAll(",", "");
			StringTokenizer st = new StringTokenizer(currentIgnoreLine);
			while (st.hasMoreTokens()) {
				ignoreList.add(st.nextToken().toLowerCase());
			}
		}
	}

	private String setIgnorePath(String ignoreFile) {
		while (true) {
			System.out.println("Please specify path for ignore words file:");
			if (sc.hasNext()) {
				ignoreFile = sc.nextLine();
				break;
			}
		}
		return ignoreFile;
	}
	
	private String setLinePath(String lineFile) {
		while (true) {
			System.out.println("Please specify path for line file:");
			if (sc.hasNext()) {
				lineFile = sc.nextLine();
				break;
			}
		}
		return lineFile;
	}

	private void readData(String path, ArrayList<String> array) {
		try {
			BufferedReader br = new BufferedReader(new FileReader(path));
			String currentLine;
			while ((currentLine = br.readLine()) != null) {
				array.add(currentLine);
			}
			br.close();
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	public ArrayList<String> getLineList() {
		return lineList;
	}

	public ArrayList<String> getIgnoreList() {
		return ignoreList;
	}
}

// This class conducts the circular shift of the lines
class CircularShift {
	private static ArrayList<String> circularLineList = new ArrayList<String>();
	private static ArrayList<String> ignoreList = new ArrayList<String>();

	public CircularShift(Input ip) {
		ArrayList<String> lineList = ip.getLineList();
		ignoreList = ip.getIgnoreList();

		for (int i = 0; i < lineList.size(); i++) {
			Shift(lineList.get(i));
		}
	}

	private void Shift(String line) {
		StringTokenizer st = new StringTokenizer(line);
		String currentToken;
		ArrayList<String> keyWordList = new ArrayList<String>();
		int startIndex = 0;
		if (!circularLineList.isEmpty()) {
			startIndex = circularLineList.size() - 1;
		}
		while (st.hasMoreElements()) {
			currentToken = st.nextToken();
			if (!ignoreList.contains(currentToken.toLowerCase())) {
				circularLineList.add(line);
				keyWordList.add(currentToken);
			}
			line = line.substring(line.indexOf(currentToken) + currentToken.length() + 1) + " " + currentToken;
		}
		for (int i = startIndex; i < circularLineList.size(); i++) {
			for (int j = 0; j < keyWordList.size(); j++) {
				String currentKeyWord = keyWordList.get(j);
				String capFirstLetter = currentKeyWord.substring(0, 1).toUpperCase() + currentKeyWord.substring(1);
				circularLineList.set(i, circularLineList.get(i).replaceAll(currentKeyWord, capFirstLetter));
			}
		}
	}

	public ArrayList<String> getCircularList() {
		return circularLineList;
	}

}

// This class conducts the alphabetical sort of the lines
class AlphabeticShift {
	private static ArrayList<String> alphabeticLineList = new ArrayList<String>();

	public AlphabeticShift(CircularShift cs) {
		alphabeticLineList = cs.getCircularList();
		Collections.sort(alphabeticLineList);
	}

	public ArrayList<String> getAlphabeticList() {
		return alphabeticLineList;
	}
}

// This class outputs the lines
class Output {
	private String outputFile = "output.txt";
	ArrayList<String> outputList = new ArrayList<String>();
	public Output(AlphabeticShift as) {
		System.out.println("The output is below:");
		System.out.println();
		outputList = as.getAlphabeticList();
		writeOutput();
		System.out.println("____________________________________________________");
		System.out.println("The output is written to Output.txt. Task completed!");
	}
	
	private void writeOutput() {
		PrintWriter writer;
		try {
			writer = new PrintWriter(outputFile, "UTF-8");
			for (int i = 0; i < outputList.size(); i++) {
				System.out.println(outputList.get(i));
				writer.println(outputList.get(i));
			}
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
