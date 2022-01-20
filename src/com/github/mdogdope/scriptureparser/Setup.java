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
	
	public void setupData() throws IOException {
		BufferedReader bookInfoReader = new BufferedReader(new FileReader(new File("book_info.dat")));
		while(bookInfoReader.ready()) {
//			info[collection|code|name|chapters]
			String[] info = bookInfoReader.readLine().split(":");
			for(int i = 1; i <= Integer.parseInt(info[3]); i++) {
				
				String uri = "book_content/%s/%d.sdat";
				File checkFile = new File(String.format(uri, info[1], i));
				if(checkFile.exists()) {
					continue;
				}
				
				setupChapter(info[0], info[1], i);
			}
		}
		bookInfoReader.close();
	}
	
	public void setupBook(String code) throws IOException {
		BufferedReader bookInfoReader = new BufferedReader(new FileReader(new File("book_info.dat")));
		
		while(bookInfoReader.ready()) {
			String[] info = bookInfoReader.readLine().split(":");
			if(info[1].equals(code)) {
				for(int i = 1; i <= Integer.parseInt(info[3]); i++) {
					setupChapter(info[0], info[1], i);
				}
			}
		}
		
		bookInfoReader.close();
	}
	
	public void setupChapter(String collection, String code, int chapter) throws IOException {
		Document doc = fetchChapter(collection, code, chapter);
		writeChapter(code, chapter, parseChapter(doc));
	}
	
	private Document fetchChapter(String coll, String code, int chapter) throws IOException {
		String status = "Downloading %s %s %d";
		System.out.println(String.format(status, coll, code, chapter));
		
		@SuppressWarnings("serial")
		Map<String, String> URLS = new HashMap<String, String>(){{
			put("ot", "https://www.churchofjesuschrist.org/study/scriptures/ot/%s/%d?lang=eng");
			put("nt", "https://www.churchofjesuschrist.org/study/scriptures/nt/%s/%d?lang=eng");
			put("dc", "https://www.churchofjesuschrist.org/study/scriptures/dc-testament/%s/%d?lang=eng");
			put("pgp", "https://www.churchofjesuschrist.org/study/scriptures/pgp/%s/%d?lang=eng");
			put("bom", "https://www.churchofjesuschrist.org/study/scriptures/bofm/%s/%d?lang=eng");
		}};
		
		String url = String.format(URLS.get(coll), code, chapter);
		
		Document rawData = Jsoup.connect(url).timeout(0).get();
		
		return rawData;
	}
	
	private Vector<String> parseChapter(Document data){
		
		System.out.println("Parsing content.");
		
		Vector<String> ret = new Vector<String>();
		
		Vector<String> lines = new Vector<>();
		Collections.addAll(lines, data.html().split("\\n"));
		
		for(String line : lines) {
			
			if(!line.contains("class=\"verse-number\"")) {
				continue;
			}
			
			line = line.replaceAll("^(.*?(<\\/span>))", ""); // Remove beginning junk;
			line = line.replace("</p>", ""); // Remove end tags
			line = line.replaceAll("</a>", ""); // Remove end of hyperlink tag
			line = line.replaceAll("(<a).{1,200}(up>)", ""); // Remove all superscript links
			line = line.replaceAll("</span>", ""); // Remove end of all span tags.
			line = line.replaceAll("(<s).{1,200}(\">)", ""); // Remove all span tags.
			line = line.replaceAll("¶ ", ""); // Remove all paragraph symbols.
			line = line.replaceAll("–", "-"); // Replace long dash with normal dash.
			ret.add(line);
		}
		
		return ret;
	}
	
	private void writeChapter(String book, int chapter, Vector<String> lines) throws IOException {
		System.out.println("Saving to file.");
		
		File uri = new File("book_content");
		if(!uri.exists()) {
			uri.mkdir();
		}
		
		uri = new File(uri, book);
		if(!uri.exists()) {
			uri.mkdir();
		}
		
		uri = new File(uri, Integer.toString(chapter) + ".sdat");
		
		BufferedWriter contentWriter = new BufferedWriter(new FileWriter(uri));
		
		for(String line : lines) {
			contentWriter.write(line + "\n");
		}
		
		contentWriter.close();
	}
}
