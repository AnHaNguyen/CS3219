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
	private static Controller controller;
	private static List<String> ignoreWords;
	private static List<String> inputLines;
	private static List<String> KWICLines;
	
	/**
	 * Launch the application.
	 * @throws FileNotFoundException 
	 */
	public static void main(String[] args) throws FileNotFoundException {
		controller = new Controller();
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
		
		final JTextArea ignoreWordArea = new JTextArea("Input Ignore Words below:");
		ignoreWordArea.setBounds(0, 0, 500, 20);
		ignoreWordArea.setEditable(false);
		ignoreWordArea.setBackground(java.awt.Color.lightGray);
		contentPane.add(ignoreWordArea);

		final JTextField ignoreWordText = new JTextField();
		ignoreWordText.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent event) {
				if (event.getKeyCode() == KeyEvent.VK_ENTER) {
					String input = ignoreWordText.getText();
					if (input != null && !input.equals("")) {
						ignoreWords.add(input);
					}
					ignoreWordText.setText("");
				}
			}
		});
		ignoreWordText.setBounds(0, 20, 350, 50);
		contentPane.add(ignoreWordText);
		
		final JTextArea inputLineArea = new JTextArea("Input Lines below:");
		inputLineArea.setEditable(false);
		inputLineArea.setBounds(0, 80, 500, 20);
		inputLineArea.setBackground(java.awt.Color.lightGray);
		contentPane.add(inputLineArea);
		
		final JTextField inputLineText = new JTextField();
		inputLineText.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent event) {
				if (event.getKeyCode() == KeyEvent.VK_ENTER) {
					String input = inputLineText.getText();
					if (input != null && !input.equals("")) {
						inputLines.add(input);
					}
					inputLineText.setText("");
				}
			}
		});
		inputLineText.setBounds(0, 100, 350, 50);
		contentPane.add(inputLineText);
		
		JButton ignoreWordButton = new JButton("Add Ignore Word");
		ignoreWordButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				String input = ignoreWordText.getText();
				if (input != null && !input.equals("")) {
					ignoreWords.add(input);
				}
				ignoreWordText.setText("");
			}
		});
		ignoreWordButton.setBounds(350, 20, 150, 50);
		contentPane.add(ignoreWordButton);
		
		JButton inputLineButton = new JButton("Add Input Lines");
		inputLineButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				String input = inputLineText.getText();
				if (input != null && !input.equals("")) {
					inputLines.add(input);
				}
				inputLineText.setText("");
			}
		});
		inputLineButton.setBounds(350, 100, 150, 50);
		contentPane.add(inputLineButton);
		
		final JTextArea resultDisplay = new JTextArea();
		resultDisplay.setFont(new Font("Result", Font.BOLD, 20));
		resultDisplay.setEditable(false);
		resultDisplay.setBackground(java.awt.Color.lightGray);
		resultDisplay.setBounds(0, 210, 500, 390);
		contentPane.add(resultDisplay);
		
		JButton submitButton = new JButton("Find KWIC Lines");
		submitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				KWICLines = controller.generateKWICIndexLines(
						ignoreWords, inputLines);
				String outputStr = "";
				for (int i = 0; i < KWICLines.size(); i++) {
					outputStr = outputStr.concat(KWICLines.get(i) + NEW_LINES);
				}
				resultDisplay.setText(outputStr);
			}
		});
		submitButton.setBounds(160, 170, 170, 30);
		contentPane.add(submitButton);
	}
}
