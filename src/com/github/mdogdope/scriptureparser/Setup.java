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
	
	public void fetchData() throws IOException {
		String baseDataPath = "book_data/%s_chapters.dat";
		
		for(String key : URL_KEYS) { // Loop through collections of books.
			BufferedReader bookDataReader = new BufferedReader(new FileReader(new File(String.format(baseDataPath, key))));	
			
			// Checks if directory exists and creates it if not.
			File rawDir = new File(key + "Raw");
			if(!rawDir.exists()) {
				rawDir.mkdir();
			}
			while(bookDataReader.ready()) {
				String[] rawData = bookDataReader.readLine().strip().split(":");
				int maxCh = Integer.parseInt(rawData[1]);
				String bookName = rawData[0];
				
				File bookDir = new File(rawDir.toString() + "/" + bookName);
				if(!bookDir.exists()) {
					bookDir.mkdir();
				}
				for(int i = 1; i <= maxCh; i++) {
					File f = new File(bookDir, "CH" + i + ".sdat");
					if(f.exists()) {
						System.out.println("Found " + key + " " + bookName + " " + i + "/" + maxCh);
						continue;
					}
					System.out.println("Starting " + key + " " + bookName + " " + i + "/" + maxCh);
					BufferedWriter sourceWriter = new BufferedWriter(new FileWriter(f));
					Document rawSource = Jsoup.connect(String.format(URLS.get(key), bookName, i)).timeout(0).get();
					
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
	
	public void parseData() throws IOException {
		// Check for and make directory for parsed book data.
		File baseDirBookData = new File("book_content");
		if(!baseDirBookData.exists()) {
			baseDirBookData.mkdir();
		}
		String baseBookData = "book_data/%s_chapters.dat";
		// Loop through collections of books.
		for(String key : URL_KEYS) {
			BufferedReader bookDataReader = new BufferedReader(new FileReader(new File(String.format(baseBookData, key))));
			File rawCollectionDir = new File(key + "Raw");
			while(bookDataReader.ready()) {
				String[] rawBookData = bookDataReader.readLine().split(":"); // Gets code and chapter count for a book.
				String bookName = rawBookData[0];
				int maxCh = Integer.parseInt(rawBookData[1]);
				
				File rawBookDir = new File(rawCollectionDir, bookName);
				
				for(int i = 1; i <= maxCh; i++) {
					File rawChDir = new File(rawBookDir, "CH" + i + ".sdat");
					System.out.println("Starting " + key + " " + bookName + " " + i + "/" + maxCh);
					BufferedReader rawChReader = new BufferedReader(new FileReader(rawChDir));
					
					File bookContentDir = new File(baseDirBookData, bookName);
					if(!bookContentDir.exists()) {
						bookContentDir.mkdir();
					}
					BufferedWriter chWriter = new BufferedWriter(new FileWriter(new File(bookContentDir, "CH" + i + ".sdat")));
					
					while(rawChReader.ready()) {
						String line = rawChReader.readLine();
						// Clean verse data and write to file.
						
						//Remove beginning junk.
						String toFind = "</span>";
						int end = line.indexOf(toFind) + toFind.length();
						line = line.substring(end);
						
						line = line.replace("</p>", "");
						line = line.replaceAll("</a>", "");
						line = line.replaceAll("(<a).{1,200}(up>)", "");
						line = line.replaceAll("</span>", "");
						line = line.replaceAll("(<s).{1,200}(\">)", "");
						line = line.replaceAll("¶ ", "");
						
						chWriter.write(line + "\n");
						
					}
					chWriter.close();
					rawChReader.close();
				}	
			}
		}
	}
}
