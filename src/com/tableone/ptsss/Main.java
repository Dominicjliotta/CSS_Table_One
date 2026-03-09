package com.tableone.ptsss;

import com.tableone.ptsss.client.ClientDriver;
import com.tableone.ptsss.server.ServerDriver;

public class Main {

	/*
	 * main class, starts up server and client drivers
	 */
	
	public static void main(String[] args) {
		
		//set up resources on the server side
		if (!ServerDriver.setup()) return;
		
		//begin client loop to continuously process user input
		ClientDriver.inputLoop();
		
		//once the user terminates the process, clean up server side resources
		ServerDriver.tearDown();
		
	}
	
}