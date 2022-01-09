package com.github.mdogdope.scriptureparser;

import java.io.IOException;

public class CLI {
	
	public CLI() throws IOException {
		Setup setup = new Setup();
//		setup.fetchData();
		setup.parseData();
	}

}
