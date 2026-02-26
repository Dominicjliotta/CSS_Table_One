package com.tableone.ptsss.client;

import java.util.Scanner;

/*
 * abstract client-side api implementation that all client api classes must extend!
 */

public abstract class ClientApi {
	
	protected ClientApi(Scanner scanner, String[] args) throws Exception {
		
		this.parseRequest(scanner, args);
		this.performCall();
		
	}
	
	//parse and validate user input
	protected abstract void parseRequest(Scanner scanner, String[] args) throws Exception;
	
	//make the api request to the server
	protected abstract void performCall() throws Exception;
	
	//used to print the final result to the user
	static protected void printOutput(String str) {
		System.out.println("\n" + str);
	}
	
}
