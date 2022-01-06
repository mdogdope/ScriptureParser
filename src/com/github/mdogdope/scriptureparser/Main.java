package com.github.mdogdope.scriptureparser;

public class Main {
	
	public static void main(String[] args) {
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
	
	private static void setupCLI() {
		try {
		CLI cli = new CLI();
		}catch (Exception e) {
			
		}
		
	}
}
