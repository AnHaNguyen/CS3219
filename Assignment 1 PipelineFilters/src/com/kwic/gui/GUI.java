package com.kwic.gui;

import java.awt.EventQueue;
import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JTextArea;

import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.ActionEvent;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import com.kwic.controller.Controller;

public class GUI extends JFrame {
	/**
	 * default serialUID
	 */
	private static final long serialVersionUID = 1L;
	public static final String NEW_LINES = System.getProperty("line.separator");
	
	private JPanel contentPane;
	private static List<String> ignoreWords;
	private static List<String> inputLines;
	private static List<String> KWICLines;
	
	/**
	 * Launch the application.
	 * @throws FileNotFoundException 
	 */
	public static void main(String[] args) throws FileNotFoundException {
		ignoreWords = new ArrayList<>();
		inputLines = new ArrayList<>();
		KWICLines = new ArrayList<>();
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GUI frame = new GUI();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame by default constructor
	 */
	public GUI() {
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		
		setContentPane(contentPane);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 500, 650);
		contentPane.setLayout(null);
		
		JTextArea ignoreWordsDisplay = new JTextArea();
		ignoreWordsDisplay.setEditable(false);
		ignoreWordsDisplay.setBounds(0, 210, 500, 50);
		contentPane.add(ignoreWordsDisplay);
		
		JTextArea inputLinesDisplay = new JTextArea();
		inputLinesDisplay.setEditable(false);
		inputLinesDisplay.setBounds(0, 260, 500, 50);
		contentPane.add(inputLinesDisplay);
		
		JTextArea resultDisplay = new JTextArea();
		resultDisplay.setFont(new Font("Result", Font.BOLD, 18));
		resultDisplay.setEditable(false);
		resultDisplay.setBackground(java.awt.Color.lightGray);
		resultDisplay.setBounds(0, 310, 500, 340);
		contentPane.add(resultDisplay);
	
		JTextArea ignoreWordArea = new JTextArea("Input Ignore Words below:");
		ignoreWordArea.setBounds(0, 0, 500, 20);
		ignoreWordArea.setEditable(false);
		ignoreWordArea.setBackground(java.awt.Color.lightGray);
		contentPane.add(ignoreWordArea);

		JTextField ignoreWordText = new JTextField();
		ignoreWordText.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent event) {
				if (event.getKeyCode() == KeyEvent.VK_ENTER) {
					String input = ignoreWordText.getText();
					if (input != null && !input.equals("")) {
						ignoreWords.add(input);
					}
					displayIgnoreWords(ignoreWordsDisplay);
					ignoreWordText.setText("");
					submit(resultDisplay);
				}
			}
		});
		ignoreWordText.setBounds(0, 20, 500, 50);
		contentPane.add(ignoreWordText);
		
		JTextArea inputLineArea = new JTextArea("Input Lines below:");
		inputLineArea.setEditable(false);
		inputLineArea.setBounds(0, 80, 500, 20);
		inputLineArea.setBackground(java.awt.Color.lightGray);
		contentPane.add(inputLineArea);
		
		JTextField inputLineText = new JTextField();
		inputLineText.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent event) {
				if (event.getKeyCode() == KeyEvent.VK_ENTER) {
					String input = inputLineText.getText();
					if (input != null && !input.equals("")) {
						inputLines.add(input);
					}
					displayInputLines(inputLinesDisplay);
					inputLineText.setText("");
					submit(resultDisplay);
				}
			}
		});
		inputLineText.setBounds(0, 100, 500, 50);
		contentPane.add(inputLineText);
	
		JButton resetButton = new JButton("Reset");
		resetButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				ignoreWords.clear();
				inputLines.clear();
				displayIgnoreWords(ignoreWordsDisplay);
				displayInputLines(inputLinesDisplay);
				submit(resultDisplay);
			}
		});
		resetButton.setBounds(350,170, 100, 30);
		contentPane.add(resetButton);
		
		JButton importButton = new JButton("Import from files");
		importButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				ignoreWords = Controller.importFromFile(
						Controller.pathToIgnoreWordsFile);
				inputLines = Controller.importFromFile(
						Controller.pathToInputLinesFile);
				displayIgnoreWords(ignoreWordsDisplay);
				displayInputLines(inputLinesDisplay);
				submit(resultDisplay);
			}
		});
		importButton.setBounds(160, 170, 170, 30);
		contentPane.add(importButton);
	}
	
	private static void displayIgnoreWords(JTextArea area) {
		ignoreWords = Controller.preprocessIgnoreWords(ignoreWords);
		String displayStr = "Ignore words: " + NEW_LINES;
		for (int i = 0; i < ignoreWords.size(); i++) {
			displayStr += ignoreWords.get(i) + ", ";
		}
		area.setText(displayStr);
	}
	
	private static void displayInputLines(JTextArea area) {
		inputLines = Controller.preprocessInputLines(inputLines);
		String displayStr = "Input Lines: " + NEW_LINES;
		for (int i = 0; i < inputLines.size(); i++) {
			displayStr += inputLines.get(i) + ", ";
		}
		area.setText(displayStr);
	}
	
	private void submit(JTextArea resultDisplay) {
		KWICLines = Controller.generateKWICIndexLinesByPipelineAndFilters(
				ignoreWords, inputLines);
		Controller.outputToFile(KWICLines, Controller.pathToOutputFile);
		String outputStr = "KWIC Lines generated:" + NEW_LINES + NEW_LINES;
		for (int i = 0; i < KWICLines.size(); i++) {
			outputStr = outputStr.concat(KWICLines.get(i) + NEW_LINES);
		}
		resultDisplay.setText(outputStr);
	}
}
