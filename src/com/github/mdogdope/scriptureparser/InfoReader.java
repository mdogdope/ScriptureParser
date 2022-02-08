package com.github.mdogdope.scriptureparser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Vector;

public class InfoReader {
	
	Vector<Info> infos = new Vector<>();
	
	public InfoReader() {
		try {
			BufferedReader infoReader = new BufferedReader(new FileReader(new File("book_info.dat")));
			while(infoReader.ready()) {
				Info info = new Info(infoReader.readLine());
				this.infos.add(info);
			}
			infoReader.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public Vector<Info> getInfo() {
		return this.infos;
	}
}
