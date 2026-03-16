package com.tableone.ptsss.client;

//import java.sql.PreparedStatement;
import java.util.Scanner;

import com.tableone.ptsss.server.ServerApiGetIncidents;

/*
 * Client API for retrieving incidents along a transit route.
 * getIncidents(routeNumber) returns a list of incidents for stops along the route.
 */

public class ClientApiGetIncidents extends ClientApi {

	private String routeNumber;

	// Name of the API function shown to the user
	@Override
	protected String getName() {
		return "getIncidents(routeNumber)";
	}

	// Prompt the user for route number and validate it
	@Override
	protected void parseRequest(Scanner scanner) throws Exception {

		System.out.print("Route Number: ");
		this.routeNumber = scanner.nextLine();

		// Validate input
        PreparedStatement ps = ServerDriver.query_routeExists();
        ps.setString(1, this.routeNumber);
        ResultSet rs = ps.executeQuery();
        if (!rs.next()) throw new Exception("Invalid Route Number!");
	}

	// Call the server API after input parsing
	@Override
	protected void performCall() throws Exception {

		// Call the server API that retrieves incidents
		ServerApiIncidents serverApi = new ServerApiGetIncidents();

		String output = serverApi.call(this.routeNumber);

		// Print the returned incidents
		printOutput(output);
	}
}