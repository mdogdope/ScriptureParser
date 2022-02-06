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
		
		File infoFile = new File("book_info.dat");
		if(!infoFile.exists()) {
			makeInfoFile();
		}
		
		BufferedReader bookInfoReader = new BufferedReader(new FileReader(infoFile));
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
	
	private void makeInfoFile() throws IOException{
		
		String lines = """
				bom:1-ne:1 Nephi:22
				bom:2-ne:2 Nephi:33
				bom:jacob:Jacob:7
				bom:enos:Enos:1
				bom:jarom:Jarom:1
				bom:omni:Omni:1
				bom:w-of-m:Words of Mormon:1
				bom:mosiah:Mosiah:29
				bom:alma:Alma:63
				bom:hel:Helaman:16
				bom:3-ne:3 Nephi:30
				bom:4-ne:4 Nephi:1
				bom:morm:Mormon:9
				bom:ether:Ether:15
				bom:moro:Moroni:10
				dc:dc:Doctrine and Covenants:138
				nt:matt:Matthew:28
				nt:mark:Mark:16
				nt:luke:Luke:24
				nt:john:John:21
				nt:acts:Acts:28
				nt:rom:Romans:16
				nt:1-cor:1 Corinthians:16
				nt:2-cor:2 Corinthians:13
				nt:gal:Galatians:6
				nt:eph:Ephesians:6
				nt:philip:Philippians:4
				nt:col:Colossians:4
				nt:1-thes:1 Thessalonians:5
				nt:2-thes:2 Thessalonians:3
				nt:1-tim:1 Timothy:6
				nt:2-tim:2 Timothy:4
				nt:titus:Titus:3
				nt:philem:Philemon:1
				nt:heb:Hebrews:13
				nt:james:James:5
				nt:1-pet:1 Peter:5
				nt:2-pet:2 Peter:3
				nt:1-jn:1 John:5
				nt:2-jn:2 John:1
				nt:3-jn:3 John:1
				nt:jude:Jude:1
				nt:rev:Revelation:22
				ot:gen:Genesis:50
				ot:ex:Exodus:40
				ot:lev:Leviticus:27
				ot:num:Numbers:36
				ot:deut:Deuteronomy:34
				ot:josh:Joshua:24
				ot:judg:Judges:21
				ot:ruth:Ruth:4
				ot:1-sam:1 Samuel:31
				ot:2-sam:2 Samuel:24
				ot:1-kgs:1 Kings:22
				ot:2-kgs:2 Kings:25
				ot:1-chr:1 Chronicles:29
				ot:2-chr:2 Chronicles:36
				ot:ezra:Ezra:10
				ot:neh:Nehemiah:13
				ot:esth:Esther:10
				ot:job:Job:42
				ot:ps:Psalms:150
				ot:prov:Proverbs:31
				ot:eccl:Ecclesiastes:12
				ot:song:Song of Solomon:8
				ot:isa:Isaiah:66
				ot:jer:Jeremiah:52
				ot:lam:Lamentations:5
				ot:ezek:Ezekiel:48
				ot:dan:Daniel:12
				ot:hosea:Hosea:14
				ot:joel:Joel:3
				ot:amos:Amos:9
				ot:obad:Obadiah:1
				ot:jonah:Jonah:4
				ot:micah:Micah:7
				ot:nahum:Nahum:3
				ot:hab:Habakkuk:3
				ot:zeph:Zephaniah:3
				ot:hag:Haggai:2
				ot:zech:Zechariah:14
				ot:mal:Malachi:4
				pgp:moses:Moses:8
				pgp:abr:Abraham:5
				pgp:js-m:Joseph Smith-Matthew:1
				pgp:js-h:Joseph Smith-History:1
				""";
		
		File infoFile = new File("book_info.dat");
		
		BufferedWriter ofile = new BufferedWriter(new FileWriter(infoFile));
		
		ofile.write(lines);
		
		ofile.close();
		
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
