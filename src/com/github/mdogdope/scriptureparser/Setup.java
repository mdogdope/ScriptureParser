package com.github.mdogdope.scriptureparser;

import java.io.IOException;
import java.util.Scanner;

public class Setup {
	public Setup() throws IOException {
		Scanner in = new Scanner(System.in);
		String cmd = "";
		while(!cmd.toLowerCase().equals("q")) {
			Runtime.getRuntime().exec("cls");
			System.out.println("==== Setup Utility ====");
			String[] options = {
					"1. Full setup. (download and install)",
					"2. Just download books.",
					"3. Just install books.",
					"Q. Exit setup utility."
					};
			for(String option : options) {
			System.out.println(option);
			}
			
			System.out.print(">>");
			cmd = in.nextLine();
			
			switch(cmd) {
			case ("1"):
				fetchData();
				parseData();
				break;
			case ("2"):
				fetchData();
				break;
			case ("3"):
				parseData();
				break;
			}
		}
		in.close();
	}
	
	private void fetchData() {
		
	}
	
	private void parseData() {
		
	}
}
