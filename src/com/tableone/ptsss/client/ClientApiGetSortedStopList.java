package com.tableone.ptsss.client;

import java.util.Scanner;

import com.tableone.ptsss.server.ServerApiGetSortedStopList;

/*--------------------------------------------------------------------------*/
/* ClientApiGetSortedStopList                                               */
/* Author: Kareem                                                           */
/*                                                                          */
/* getSortedStopList(destination, weekDay)                                  */
/* Returns a list of bus stop times (Showing route number, arrival time,    */
/* and semi-yearly score for route) for a given destination on a given day  */
/* of the week. The list is sorted by time of arrival at the destination.   */
/*--------------------------------------------------------------------------*/

public class ClientApiGetSortedStopList extends ClientApi {

	private String weekDay;
	private String destination;
	
	@Override
	protected String getName() {
		return "getSortedStopList(destination, weekDay)";
	}

	@Override
	protected void parseRequest(Scanner scanner) throws Exception {
		
		//prompt the user for a destination
		System.out.print("Destination: ");
		this.destination = scanner.nextLine().trim();
		
		//make sure destination isnt blank
		if (this.destination.isEmpty()) throw new Exception("Destination cannot be blank!");
		
		//prompt the user for a week day
		System.out.print("Day of the week: ");
		this.weekDay = scanner.nextLine().toLowerCase().trim();
		
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
