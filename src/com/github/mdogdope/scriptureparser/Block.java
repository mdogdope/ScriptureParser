package com.github.mdogdope.scriptureparser;

public class Block {
	
	private String book = "";
	private int chapter = 0;
	private int start = 0;
	private int end = 0;
	
	public Block(String book, int chapter, int start, int end) {
		this.book = book;
		this.chapter = chapter;
		this.start = start;
		this.end = end;
	}
	
	public String book() {
		return this.book;
	}
	
	public int chapter() {
		return this.chapter;
	}
	
	public int start() {
		return this.start;
	}
	
	public int end() {
		return this.end;
	}
}
