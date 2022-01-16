package com.github.mdogdope.scriptureparser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class CLI {
	
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
				runExport();
				break;
			default:
				addBlock(args);
			}
			
		}
		userInput.close();
		System.out.println("Quit scripture parser");
	}
	
	private void runSetup(String[] args) {
		
	}
	
	private void addBlock(String[] args) {
		
	}
	
	private void runExport() {
		
	}
}
