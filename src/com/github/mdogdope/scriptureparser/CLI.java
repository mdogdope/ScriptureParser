package com.github.mdogdope.scriptureparser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class CLI {
	
	private ScriptureParser sp = new ScriptureParser();
	
	public CLI() throws IOException {
		BufferedReader titleReader = new BufferedReader(new FileReader(new File("title.txt")));
		while(titleReader.ready()) {
			System.out.println(titleReader.readLine());
			if(!titleReader.ready()) {
				System.out.println("\n"); // Adds an extra line below title.
			}
		}
		
		
		System.out.println("setup all|download|install|delete");
		System.out.println("<book|bookCode> <chapter> <first verse> <last verse>");
		
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
			s.fetchData();
			s.parseData();
		}
	}
	
	private void addBlock(String[] args) throws IOException {
		if(args.length == 3) {
			this.sp.addBlock(args[0], Integer.parseInt(args[1]), Integer.parseInt(args[2]), Integer.parseInt(args[2]));
		}else if(args.length == 2) {
			this.sp.addBlock(args[0], Integer.parseInt(args[1]), 1, 1000);
		}else if(args.length == 4){
			this.sp.addBlock(args[0], Integer.parseInt(args[1]), Integer.parseInt(args[2]), Integer.parseInt(args[3]));
		}
	}
	
	private void runExport(String[] args) throws IOException {
		String fileName = "export.txt";
		if(args.length >= 2) {
			fileName = args[1];
		}
		
		this.sp.generate(fileName);
		
	}
}
