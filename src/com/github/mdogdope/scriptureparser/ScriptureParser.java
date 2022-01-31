package com.github.mdogdope.scriptureparser;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Vector;

public class ScriptureParser {
	
	private Vector<Block> blocks = new Vector<>();
	
	public boolean addBlock(String book, int chapter, int start, int end) throws IOException {
		// Check if book is valid and convert to code version if needed.
		String code = bookToCode(book);
		if(code.isEmpty()) {
			return false;
		}
		
		// Check if chapter is valid.
		if(!checkChapter(code, chapter)) {
			return false;
		}
		
		// Check if verse is in correct order.
		if(start > end) {
			return false;
		}
		
		// If all checks pass add new block to blocks list.
		this.blocks.add(new Block(code, chapter, start, end));
		
		return true;
	}
	
	public boolean addBreak() {
		this.blocks.add(new Block("", 0, 0, 0));
		return true;
	}
	
	public Vector<String> checkBook(String book) throws IOException{
		BufferedReader infoReader = new BufferedReader(new FileReader(new File("book_info.dat")));
		Vector<String> ret = new Vector<>();
		
		while(infoReader.ready()) {
			String[] info = infoReader.readLine().split(":");
			String fullName = info[2];
			String name = fullName.toLowerCase().replaceAll(" ", "");
			
			book = book.toLowerCase();
			
			if(book.length() > name.length()) {
				continue;
			}
			
			boolean passed = true;
			for(int i = 0; i < book.length(); i++) {
				if(book.charAt(i) != name.charAt(i)) {
					passed = false;
				}
			}
			if(passed) {
				ret.add(fullName);
			}
			
		}
		
		infoReader.close();
		
		return ret;
	}
	
	public void generate(String fname) throws IOException {
		BufferedWriter ofile = new BufferedWriter(new FileWriter(fname));
		for(Block block : this.blocks) {
			
			if(block.book().isEmpty()) {
				ofile.write("\n\n");
				continue;
			}
			
			String URI = "book_content/%s/%d.sdat";
			File cFile = new File(String.format(URI, block.book(), block.chapter()));
			BufferedReader contentReader = new BufferedReader(new FileReader(cFile));
			int counter = 1;
			while(contentReader.ready()) {
				String verse = contentReader.readLine().stripTrailing();
				if(counter >= block.start() && counter <= block.end()) {
					ofile.write(verse + "\n");
				}
				counter++;
			}
			
			contentReader.close();
		}
		
		ofile.close();
	}
	
	protected String testCode() throws IOException {
		return this.blocks.lastElement().book();
	}
	
	private boolean checkChapter(String code, int ch) throws IOException {
		BufferedReader infoReader = new BufferedReader(new FileReader(new File("book_info.dat")));
		
		while(infoReader.ready()) {
			String[] info = infoReader.readLine().split(":");
			if(code.equals(info[1]) && ch <= Integer.parseInt(info[3])) {
				infoReader.close();
				return true;
			}
		}
		
		infoReader.close();
		return false;
	}
	
	private String findCode(String raw) {
		//TODO: Make a better pattern recognition method to replace bookToCode()
		
		
		return "";
	}
	
	private String bookToCode(String book) throws IOException {
		
		BufferedReader infoReader = new BufferedReader(new FileReader(new File("book_info.dat")));
		
		while(infoReader.ready()) {
			String[] info = infoReader.readLine().split(":");
			String name = info[2].toLowerCase().replaceAll(" ", "");
			String code = info[1];
			book = book.toLowerCase();
			
			if(book.equals(code)) {
				infoReader.close();
				return code;
			}
			
			if(book.length() > name.length()) {
				continue;
			}
			
			boolean passed = true;
			for(int i = 0; i < book.length(); i++) {
				if(book.charAt(i) != name.charAt(i)) {
					passed = false;
				}
			}
			if(passed) {
				infoReader.close();
				return code;
			}
			
		}
		
		infoReader.close();
		return "";
	}
	
	
//	####### ####### ####### OLD CODE ####### ####### #######
	
//	private final String KEYS[] = {"ot", "nt", "dc", "pgp", "bom"};
//	
//	public boolean addBlock(String book, int chapter, int start, int end) throws IOException {
//		String bookCode = encodeBookName(book);
//		if(bookCode.isEmpty()) {
//			return false;
//		}
//				
//		for(String key : KEYS) {
//			File file = new File("book_data/" + key + "_chapters.dat");
//			BufferedReader br = new BufferedReader(new FileReader(file));
//			
//			while(br.ready()) {
//				String[] line = br.readLine().split(":");
//				if(line[0].equals(bookCode)) {
//					if(chapter > Integer.parseInt(line[1])) {
//						br.close();
//						return false;
//					}
//				}
//			}
//			
//			
//			br.close();
//		}
//		
//		
//		blocks.add(new Block(bookCode, chapter, start, end));
//		
//		return true;
//	}
//	
//	public boolean addBreak() {
//		blocks.add(new Block("break", 0, 0, 0));
//		return true;
//	}
//	
//	private String encodeBookName(String book) throws IOException {
//		String ret = "";
//		BufferedReader codeReader = new BufferedReader(new FileReader(new File("book_data/book_codes.dat")));
//		while(codeReader.ready()) {
//			String[] splitData = codeReader.readLine().split(":");
//			String code = splitData[0];
//			String name = splitData[1].replace(" ", "").toLowerCase();
//			book = book.replaceAll(" ", "").toLowerCase();
//			if(book.equals(name) || book.equals(code)) {
//				ret = code;
//			}
//		}
//		codeReader.close();
//		return ret;
//	}
//	
//	public void generate(String exportName) throws IOException{
//		BufferedWriter ofile = new BufferedWriter(new FileWriter(new File(exportName + ".txt")));
//		
//		for(Block block : blocks) {
//			
//			if(block.book().equals("break")) {
//				ofile.write("\n");
//				continue;
//			}
//			
//			File content = new File(String.format("book_content/%s/CH%s.sdat", block.book(), block.chapter()));
//			if(!content.exists()) {
//				continue;
//			}
//			
//			BufferedReader reader = new BufferedReader(new FileReader(content));
//			int counter = 1;
//			while(reader.ready()) {
//				String line = reader.readLine();
//				if(counter >= block.start() && counter <= block.end()) {
//					ofile.write(line);
//				}
//				counter++;
//			}
//			reader.close();
//		}
//		
//		ofile.close();
//	}
}
