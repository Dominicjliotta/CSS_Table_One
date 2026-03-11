package com.tableone.ptsss.client;

import java.util.Scanner;

/*
 * abstract client-side api implementation that all client api classes must extend!
 */

public abstract class ClientApi {
	
	public void execute(Scanner scanner) throws Exception {
		
		System.out.println("\n--- " + this.getName() + " ---\n");
		
		
		this.parseRequest(scanner);
		this.performCall();
		
	}
	
	//name of the api function, used as a visual header when calling
	protected abstract String getName();
	
	//parse and validate user input
	protected abstract void parseRequest(Scanner scanner) throws Exception;
	
	//make the api request to the server
	protected abstract void performCall() throws Exception;
	
	//used to print the final result to the user
	protected static void printOutput(String str) {
		System.out.println("\n" + str);
	}
	
}
