package com.tableone.ptsss.client;

import java.util.Scanner;

import com.tableone.ptsss.server.ServerApiGetIncidents;

/*----------------------------------------------------------*/
/* ClientApiGetIncidents                                    */
/* Author: David                                            */
/*                                                          */
/* getIncidents(routeNumber)                                */
/* Returns a list of incidents for all stops along a route. */
/*----------------------------------------------------------*/

public class ClientApiGetIncidents extends ClientApi {

	private String routeNumber;
	
	@Override
	protected String getName() {
		return "getIncidents(routeNumber)";
	}
	
	@Override
	protected void parseRequest(Scanner scanner) throws Exception {

		//prompt the user for a route number
		System.out.print("Route Number: ");
		this.routeNumber = scanner.nextLine().trim();

		if (this.routeNumber.isEmpty()) throw new Exception("Route number cannot be blank!");
		
	}
	
	@Override
	protected void performCall() throws Exception {

		//call the server api that retrieves incidents
		ServerApiGetIncidents serverApi = new ServerApiGetIncidents();
		String output = serverApi.call(this.routeNumber);

		//print the returned incidents
		printOutput(output);
		
	}
	
}