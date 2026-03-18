package com.tableone.ptsss.client;

import java.util.Scanner;

import com.tableone.ptsss.server.ServerApiGetRecentTags;

/*----------------------------------------------------------*/
/* ClientApiGetRecentTags                                   */
/* Author: Melissa                                          */
/*                                                          */
/* getRecentTags(routeNumber)                               */
/* Returns a list of  tags from  incidents across all stops */
/* along a route from the last 6 months.                    */
/*----------------------------------------------------------*/

public class ClientApiGetRecentTags extends ClientApi {

	private String routeNumber;

	@Override
	protected String getName() {
		return "getRecentTags(routeNumber)";
	}

	@Override
	protected void parseRequest(Scanner scanner) throws Exception {

		//prompt the user for a route number
		System.out.print("Route number: ");
		this.routeNumber = scanner.nextLine().trim();
		
		if (this.routeNumber.isEmpty()) throw new Exception("Route number cannot be blank!");
      
	}

	@Override
	protected void performCall() throws Exception {

		//make the call to the server and get the output
		ServerApiGetRecentTags serverApi = new ServerApiGetRecentTags();
		String output = serverApi.call(this.routeNumber);

		//print the output
		printOutput(output);
		
	}

}