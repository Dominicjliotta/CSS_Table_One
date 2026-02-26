package com.tableone.ptsss.client;

import java.util.Scanner;

import com.tableone.ptsss.server.ServerApiExample;

/*
 * This class shows you how to implement your client-side API classes!
 * This fake api call will simply take a name and age and print it back to the user
 */

//name it "ClientApi<Something>", and make sure to extend ClientApi
public class ClientApiExample extends ClientApi {

	private String name;
	private int age;
	
	//dont edit the constructor, keep it as-is
	protected ClientApiExample(Scanner scanner, String[] args) throws Exception {
		super(scanner, args);
	}

	//here, you parse the user's input (given by args), and save the necessary data
	//into instance variables. you are also given the scanner in case you'd like to
	//prompt the user for extra information
	@Override
	protected void parseRequest(Scanner scanner, String[] args) throws Exception {
		
		//ALWAYS be sure to error check everything and handle all exceptions yourself!
		//throw exceptions with meaningful messages
		if (args.length < 3) {
			throw new Exception("Invalid inputs for example()\nParameters: name, age");
		}
		
		//saving a parameter to an instance variable, simple example
		this.name = args[1];
		
		//saving a parameter to an instance variable, now with error checking!
		try {
			this.age = Integer.parseInt(args[2]);
		} catch (Exception E) {
			//replace java's exception with a more user-friendly error message
			throw new Exception("Invalid value for age: \"" + args[2] + "\"");
		}
		
		//and a bounds check!
		if (this.age < 0) throw new Exception("Age cannot be negative!");
		
	}

	//this function is called *after* the user input is parsed, and the necessary data
	//is already saved into instance variables. it is designed this way for organization,
	//separating the actions of parsing and execution
	@Override
	protected void performCall() throws Exception {
		
		ServerApiExample serverApi = new ServerApiExample(this.name, this.age);
		Object returnValue = serverApi.call();
		
		//we expect a string, but serverApi.get() returns type Object,
		//so make sure to type check the return value
		if (!(returnValue instanceof String)) {
			throw new Exception("Received invalid type from ServerApiExample");
		}
		
		//return type has been validated, we can cast and save to a properly-typed variable
		String output = (String)returnValue;
		
		//now that we finally have what we need, we may print the result using printOutput
		printOutput(output);
		
	}

}
