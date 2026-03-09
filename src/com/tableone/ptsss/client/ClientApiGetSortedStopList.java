package com.tableone.ptsss.client;

import java.util.Scanner;

import com.tableone.ptsss.server.ServerApiGetSortedStopList;

public class ClientApiGetSortedStopList extends ClientApi {

	private String weekDay;
	private String destination;
	
	@Override
	protected String getName() {
		return "getSortedStopList(weekDay, destination)";
	}

	@Override
	protected void parseRequest(Scanner scanner) throws Exception {
		
		//ask the user for a destination and save the input
		System.out.print("Destination: ");
		this.destination = scanner.nextLine();
		
		//make sure destination isnt blank
		if (this.destination.isEmpty()) throw new Exception("Destination cannot be blank!");
		
		//ask the user for a week day and save the input
		System.out.print("Day of the week: ");
		this.weekDay = scanner.nextLine().toLowerCase();
		
		//make sure the user gave a valid week day
		if (!isValidWeekDay(this.weekDay)) throw new Exception("Invalid day of the week!");
		
	}
	
	//helper function, checks if a lowercase string is a valid day of the week
	private static boolean isValidWeekDay(String s) {
		return s.equals("sunday") || s.equals("monday") || s.equals("tuesday") || s.equals("wednesday")
				|| s.equals("thursday") || s.equals("friday") || s.equals("saturday");
	}

	@Override
	protected void performCall() throws Exception {
		
		//make the call to the server and get the output
		ServerApiGetSortedStopList serverApi = new ServerApiGetSortedStopList();
		String output = serverApi.call(this.destination, this.weekDay);
		
		//print the output
		printOutput(output);
		
	}

}
