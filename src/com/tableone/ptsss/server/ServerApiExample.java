package com.tableone.ptsss.server;

/*
 * This class shows you how to implement your server-side API classes!
 * This fake api call will simply take a name and age and print it back to the user
 */

//name it "ServerApi<Something>", and make sure to extend ServerApi
//when extending, include the data type that this api call returns
//in this example, the api call returns a String
//if your api call does not return anything, extend ServerApi<Void>
public class ServerApiExample extends ServerApi<String> {

	private String name;
	private int age;

	//here, you parse the arguments given to the server by the client
	//and save the data into instance variables.
	//the arguments should be given as instance data from the corresponding client api class
	//here we do error checking to make sure the arguments are valid.
	//
	//why is it designed this way? in a real-world client-server system, the client may
	//pass arbitrary data of any type to the server when making a call, and it is the
	//responsibility of the server to type-check and validate the data
	@Override
	protected void parseRequest(Object[] args) throws Exception {
		
		//check number of arguments
		if (args.length != 2) {
			throw new Exception("ServerApiExample received " + args.length + " arguments, expected 2");
		}
		
		//type check each argument
		if (!(args[0] instanceof String)) {
			throw new Exception("ServerApiExample received invalid type for name");
		}
		
		if (!(args[1] instanceof Integer)) {
			throw new Exception("ServerApiExample received invalid type for age");
		}
		
		//if all checks pass, cast the data and save to instance variables
		this.name = (String)args[0];
		this.age = (Integer)args[1];
		
	}
	
	//this is the function that will finally fulfil the api call.
	//you may return an object, not all apis will have to.
	//if nothing has to be returned, simply return null instead.
	//here, we will just return a string containing the name and age.
	@Override
	protected String completeRequest() throws Exception {
		return "NAME: " + this.name + ", AGE: " + this.age;
	}

}