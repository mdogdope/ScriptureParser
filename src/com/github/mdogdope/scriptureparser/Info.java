package com.github.mdogdope.scriptureparser;

public class Info {
//	info[collection|code|name|chapters]
	private String[] infoLine = {};
	
	public Info(String rawLine) {
		this.infoLine = rawLine.split(":");
	}
	
	public String collection() {
		return this.infoLine[0];
	}
	
	public String code() {
		return this.infoLine[1];
	}
	
	public String fullName() {
		return this.infoLine[2];
	}
	
	public Integer chapters() {
		return Integer.parseInt(this.infoLine[3]);
	}
}
