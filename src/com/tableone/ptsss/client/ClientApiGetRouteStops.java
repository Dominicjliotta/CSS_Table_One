package com.tableone.ptsss.client;

import java.util.Scanner;

import com.tableone.ptsss.server.ServerApiExample;
getRouteStops(routeNumber);
/*
 * This class shows you how to implement your client-side API classes!
 * This fake api call will simply take a name and age and print it back to the user
 */

//name it "ClientApi<Something>", and make sure to extend ClientApi
public class ClientApiGetRouteStops extends ClientApi {

	private int routeNumber;

	//name of the api function, used as a visual header when calling
	@Override
	protected String getName() {
		return "getRouteStops()";
	}

	//here, you may prompt the user for any parameters that the api requires.
	//parse the user's input, and save the necessary data into instance variables
	@Override
	protected void parseRequest(Scanner scanner) throws Exception {

		//ALWAYS be sure to error check everything and handle all exceptions yourself!
		//throw exceptions with meaningful messages

		//prompt the user for a name
		System.out.print("Route Number: ");

		//although age is type int, we do NOT use scanner.nextInt(). using anything
		//except for nextLine() will break the program. we need to take inputs as strings
		//using nextLine() and parse them appropriately
		this.routeNumber= scanner.nextLine();
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
