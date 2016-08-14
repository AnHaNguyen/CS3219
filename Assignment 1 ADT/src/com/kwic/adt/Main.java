package com.kwic.adt;

import java.util.*;
import java.io.*;

public class Main {
	public static void main(String[] args) {
		Input ip = new Input();
		CircularShift cs = new CircularShift(ip);
		AlphabeticShift as = new AlphabeticShift(cs);
		for (int i =0; i<as.getAlphabetic().size();i++) {
			System.out.println(as.getAlphabetic().get(i));
		}
	}
}

class Input {
	private static ArrayList<String> lineList = new ArrayList<String>();
	private static ArrayList<String> ignoreLine = new ArrayList<String>();
	private static ArrayList<String> ignoreList = new ArrayList<String>();

	public Input() {
		readData("Input.txt", lineList);
		readData("Ignore.txt", ignoreLine);
		
		String currentIgnoreLine;
		for (int i = 0; i<ignoreLine.size(); i++){
			currentIgnoreLine = ignoreLine.get(i).replaceAll(",", "");
			StringTokenizer st = new StringTokenizer(currentIgnoreLine);
			while (st.hasMoreTokens()) {
				ignoreList.add(st.nextToken().toLowerCase());
			}
		}
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

	public ArrayList<String> getLine() {
		return lineList;
	}

	public ArrayList<String> getIgnore() {
		return ignoreList;
	}
}

class CircularShift {
	private static ArrayList<String> circularLineList = new ArrayList<String>();
	private static ArrayList<String> ignoreList = new ArrayList<String>();
	
	public CircularShift (Input ip) {
		ArrayList<String> lineList = ip.getLine();
		ignoreList = ip.getIgnore();
		
		for (int i =0; i<lineList.size(); i++) {
			Shift(lineList.get(i));
		}
	}
	private void Shift(String line) {
		StringTokenizer st = new StringTokenizer(line);
		String currentToken;
		while (st.hasMoreElements()) {
			currentToken = st.nextToken();
			if (!ignoreList.contains(currentToken.toLowerCase())) {
				circularLineList.add(line);
			}
			line = line.substring(line.indexOf(currentToken)+currentToken.length()+1)+" "+currentToken;
		}
	}
	public ArrayList<String> getCircular() {
		return circularLineList;
	}

}

class AlphabeticShift {
	private static ArrayList<String> alphabeticLineList = new ArrayList<String>();
	public AlphabeticShift(CircularShift cs){
		alphabeticLineList = cs.getCircular();
		Collections.sort(alphabeticLineList);
	}
	public ArrayList<String> getAlphabetic() {
		return alphabeticLineList;
	}
}
