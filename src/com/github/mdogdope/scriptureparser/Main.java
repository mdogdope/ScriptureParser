package com.github.mdogdope.scriptureparser;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {
	
	public static void main(String[] args) {
		boolean testMode = false;
		if(testMode) {
			runTestCode();
		}else {
//			setupLogger();
			Boolean GUIMode = false;
			for(int i=0; i < args.length; i++) {
				switch(args[i]) {
				case "--gui":
					GUIMode = true;
					break;
				}
			}
			if(GUIMode) {
				
			}else {
				setupCLI();
			}
		}
		
	}
	
	public static void runTestCode() {
		try {
			
			System.out.println("Running test");
			Setup test = new Setup();
			test.setupChapter("bom", "1-ne", 1);
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static void setupLogger() {
		Logger logger = Logger.getLogger(Main.class.getName());
		FileHandler fh = null;
		try {
			fh = new FileHandler("debug.log");
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		logger.addHandler(fh);
		logger.log(Level.INFO, "Example Info");
	}
	
	private static void setupCLI() {
		try {
		CLI cli = new CLI();
		}catch (Exception e) {
			System.out.println(e.toString());
		}
		
	}
}
