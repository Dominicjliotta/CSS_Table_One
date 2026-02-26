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

	//name of the api function, used as a visual header when calling
	@Override
	protected String getName() {
		return "exampleApi()";
	}
	
	//here, you may prompt the user for any parameters that the api requires.
	//parse the user's input, and save the necessary data into instance variables
	@Override
	protected void parseRequest(Scanner scanner) throws Exception {
		
		//ALWAYS be sure to error check everything and handle all exceptions yourself!
		//throw exceptions with meaningful messages
		
		//prompt the user for a name
		System.out.print("Name: ");
		
		//save the user's input into an instance variable
		this.name = scanner.nextLine();
		
		//make sure the user really typed something!
		if (this.name.isEmpty()) throw new Exception("Name cannot be blank!");
		
		//prompt the user for an age
		System.out.print("Age: ");
		
		//although age is type int, we do NOT use scanner.nextInt(). using anything
		//except for nextLine() will break the program. we need to take inputs as strings
		//using nextLine() and parse them appropriately
		String ageParam = scanner.nextLine();
		
		//parse the string input to an int, and save it into an instance variable!
		try {
			this.age = Integer.parseInt(ageParam);
		} catch (Exception e) {
			//replace java's exception with a more user-friendly error message
			throw new Exception("Invalid value for age: \"" + ageParam + "\"");
		}
		
		//and a bounds check!
		if (this.age < 0) throw new Exception("Age cannot be negative!");
		
	}

	//this function is called *after* the user input is parsed, and the necessary data
	//is already saved into instance variables. it is designed this way for organization,
	//separating the actions of parsing and execution
	@Override
	protected void performCall() throws Exception {
		
		//make a call to ServerApiExample and save the output
		ServerApiExample serverApi = new ServerApiExample();
		String output = serverApi.call(this.name, this.age);
		
		//now that we have what we need, we may print the result using printOutput
		printOutput(output);
		
	}

}
