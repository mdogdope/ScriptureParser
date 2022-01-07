package com.github.mdogdope.scriptureparser;

import java.io.IOException;
import java.util.Scanner;

public class CLI {
	
	public CLI() throws IOException {
		Runtime.getRuntime().exec("cls");
		System.out.println("==== Scripture Parser ====");
		
		while(true) {
			System.out.println("<book> <ch> <verse> (<end_verse>)");
			System.out.println("setup - Run the setup utility.");
			
			while(true) {
				Scanner input = new Scanner(System.in);
				System.out.print(">>");
				String cmd = input.nextLine();
				input.close();
				if(cmd.equals("setup")) {
					Setup setup = new Setup();
				}else if(cmd.equals("q")) {
					break;
				}
			}
		}
	}

}
