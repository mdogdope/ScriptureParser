package com.github.mdogdope.scriptureparser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;
import java.util.Vector;

public class CLI {
	
	private ScriptureParser sp = new ScriptureParser();
	
	public CLI() throws IOException {
		File titleFile = new File("title.txt");
		if(titleFile.exists()) {
			BufferedReader titleReader = new BufferedReader(new FileReader(titleFile));
			while(titleReader.ready()) {
				System.out.println(titleReader.readLine());
				if(!titleReader.ready()) {
					System.out.println("\n"); // Adds an extra line below title.
				}
			}
			titleReader.close();	
		}else {
			System.out.println("    Scripture Parser");
			System.out.println("========================");
		}
		
		System.out.println("Commands:");
		System.out.println("    setup all|download|install|delete");
		System.out.println("    <book|bookCode> <chapter> <first verse> <last verse>");
		System.out.println("    break");
		
		Scanner userInput = new Scanner(System.in);
		
		while(true) {
			String cmd = "";
			System.out.print(">>");
			cmd = userInput.nextLine();
			
			if(cmd.toLowerCase().equals("q")) {
				break;
			}
			
			String[] args = cmd.toLowerCase().split(" ");
			
			switch(args[0]) {
			case "setup":
				runSetup(args);
				break;
			case "export":
				runExport(args);
				break;
			case "break":
				this.sp.addBreak();
				break;
			default:
				addBlock(args);
			}
			
		}
		userInput.close();
		System.out.println("Quit scripture parser");
	}
	
	private void runSetup(String[] args) throws IOException {
		Setup s = new Setup();
		if(args.length == 1) {
			s.setupData();
		}
	}
	
	private void addBlock(String[] args) throws IOException {
		Vector<String> possCheck = this.sp.checkBook(args[0]);
		if(possCheck.size() >= 2) {
			String baseText = "Multiple matches:";
			
			for(int i = 0; i < possCheck.size(); i++) {
				baseText += " " + possCheck.elementAt(i);
			}
			
			System.out.println(baseText);
			return;
		}
		boolean success = false;
		if(args.length == 2) {
			success = this.sp.addBlock(args[0], Integer.parseInt(args[1]), 1, 1000);
		}else if(args.length == 3) {
			success = this.sp.addBlock(args[0], Integer.parseInt(args[1]), Integer.parseInt(args[2]), Integer.parseInt(args[2]));
		}else if(args.length == 4){
			success = this.sp.addBlock(args[0], Integer.parseInt(args[1]), Integer.parseInt(args[2]), Integer.parseInt(args[3]));
		}
		if(success) {
			
		}else {
			System.out.println("Error");
		}
	}
	
	private void runExport(String[] args) throws IOException {
		String fileName = "export.txt";
		if(args.length >= 2) {
			fileName = args[1];
			if(!fileName.endsWith(".txt")) {
				fileName += ".txt";
			}
		}
		
		this.sp.generate(fileName);
		
	}
}
