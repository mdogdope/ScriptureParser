package com.github.mdogdope.scriptureparser;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.WindowConstants;

public class GUI {
	private ScriptureParser sp = new ScriptureParser();
	
	private JFrame f = new JFrame("Scripture Parser");
	
	private JButton export = new JButton("Export");
	private JList<String> bookList = null;
	
	
	public GUI() throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException {
		UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		SwingUtilities.updateComponentTreeUI(this.f);
		
		this.f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		
		setupBookList();
		
		setupExport();
		
		this.f.setSize(600, 600);
		this.f.setLayout(null);
		this.f.setLocationRelativeTo(null);
		this.f.setVisible(true);
	}
	
	private void setupBookList() throws IOException {
		
		DefaultListModel<String> listModel = new DefaultListModel<>();
		
		BufferedReader reader = new BufferedReader(new FileReader(new File("book_info.dat")));
		while(reader.ready()) {
			String[] data = reader.readLine().split(":");
			listModel.addElement(data[2]);
		}
		
		reader.close();
		
		this.bookList = new JList<>(listModel);
		this.bookList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.bookList.setLayoutOrientation(JList.VERTICAL);
		this.bookList.setVisibleRowCount(-1);
		
		JScrollPane scroller = new JScrollPane(this.bookList);
		scroller.setBounds(10, 10, 150, 500);
		this.f.add(scroller);
	}
	
	private void setupExport() {

		this.export.setBounds(200, 0, 250, 150);
		
		this.export.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
			}
		});
		this.f.add(this.export);
	}
}
