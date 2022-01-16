package com.github.mdogdope.scriptureparser;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Vector;

public class SciptureParser {
	private Vector<Block> blocks = new Vector<>();
	
	public boolean addBlock(String book, int chapter, int start, int end) throws IOException {
		String bookCode = encodeBookName(book);
		if(bookCode.isEmpty()) {
			return false;
		}
		//TODO: Add check for verifying chapter validity.
		blocks.add(new Block(bookCode, chapter, start, end));
		
		return true;
	}
	
	public boolean addBreak() {
		blocks.add(new Block("break", 0, 0, 0));
		return true;
	}
	
	private String encodeBookName(String book) throws IOException {
		String ret = "";
		BufferedReader codeReader = new BufferedReader(new FileReader(new File("book_codes.txt")));
		while(codeReader.ready()) {
			String[] splitData = codeReader.readLine().split(":");
			String code = splitData[0];
			String name = splitData[1].replace(" ", "").toLowerCase();
			if(book.equals(name) || book.equals(code)) {
				ret = code;
			}
		}
		codeReader.close();
		return ret;
	}
	
	public void generate(String exportName) throws IOException{
		BufferedWriter ofile = new BufferedWriter(new FileWriter(new File(exportName)));
		
		for(Block block : blocks) {
			
			if(block.book().equals("break")) {
				ofile.write("\n");
				continue;
			}
			
			File content = new File(String.format("book_content/%s/CH%s.sdat", block.book(), block.chapter()));
			if(!content.exists()) {
				continue;
			}
			
			BufferedReader reader = new BufferedReader(new FileReader(content));
			int counter = 1;
			while(reader.ready()) {
				String line = reader.readLine();
				if(counter >= block.start() && counter <= block.end()) {
					ofile.write(line);
				}
				counter++;
			}
			reader.close();
		}
		
		ofile.close();
	}
}
