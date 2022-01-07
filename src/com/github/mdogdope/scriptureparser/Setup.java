package com.github.mdogdope.scriptureparser;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Vector;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class Setup {
	
	@SuppressWarnings("serial")
	private final Map<String, String> URLS = new HashMap<String, String>(){{
		put("ot", "https://www.churchofjesuschrist.org/study/scriptures/ot/%s/%d?lang=eng");
		put("nt", "https://www.churchofjesuschrist.org/study/scriptures/nt/%s/%d?lang=eng");
		put("dc", "https://www.churchofjesuschrist.org/study/scriptures/dc-testament/%s/%d?lang=eng");
		put("pgp", "https://www.churchofjesuschrist.org/study/scriptures/pgp/%s/%d?lang=eng");
		put("bom", "https://www.churchofjesuschrist.org/study/scriptures/bofm/%s/%d?lang=eng");
	}};
	
	private final String URL_KEYS[] = {"ot", "nt", "dc", "pgp", "bom"};
	
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
	
	private void fetchData() throws IOException {
		String baseDataPath = "book_data/%s_chapters.dat";
		for(String key : URL_KEYS) {
			BufferedReader dataReader = new BufferedReader(new FileReader(new File(String.format(baseDataPath, key))));	
			File rawDir = new File(key + "Raw");
			if(!rawDir.exists()) {
				rawDir.mkdir();
			}
			while(dataReader.ready()) {
				String[] rawData = dataReader.readLine().strip().split(":");
				
//				System.out.println(rawData);
				
				int maxCh = Integer.parseInt(rawData[1]);
				String bookName = rawData[0];
				
				File bookDir = new File(rawDir.toString() + "/" + bookName);
				if(!bookDir.exists()) {
					bookDir.mkdir();
				}
				for(int i = 1; i <= maxCh; i++) {
					BufferedWriter sourceWriter = new BufferedWriter(new FileWriter(new File(bookDir, "CH" + i + ".sdat")));
					Document rawSource = Jsoup.connect(buildUrl(key, bookName, i)).get();
					
					Vector<String> lines = new Vector<>();
					Collections.addAll(lines, rawSource.html().split("\\n"));
					
					for(int ii = 0; ii < lines.size(); ii++) {
						if(lines.get(ii).contains("class=\"verse-number\"")) {
							sourceWriter.write(lines.get(ii).strip() + "\n");
						}
					}
					
					sourceWriter.close();
				}
			}
		}
	}
	
	private String buildUrl(String baseKey, String book, int ch) {
		String ret = String.format(URLS.get(baseKey), book, ch);
		
		
		return ret;
	}
	
	private void parseData() {
		
	}
	
}
