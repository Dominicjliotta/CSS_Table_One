package com.tableone.ptsss.server;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

/*
 * This class shows you how to implement your server-side API classes!
 * This fake api call will simply take a name and age and print it back to the user
 */

public class ServerApiGetIncidents extends ServerApi<String> {

	private String routeNumber;

	@Override
	protected void parseRequest(Object[] args) throws Exception {

		// check number of arguments
		if (args.length != 1) {
			throw new Exception("ServerApiGetIncidents received " + args.length + " arguments, expected 1");
		}

		// type check each argument
		if (!(args[0] instanceof String)) {
			throw new Exception("ServerApiGetIncidents received invalid type for Route Number");
		}

		// if all checks pass, cast the data and save to instance variables
		this.routeNumber = (String) args[0];
	}

	// this is the function that will finally fulfil the api call.
	// you may return an object, not all apis will have to.
	// if nothing has to be returned, simply return null instead.
	// here, we will just return a string containing the name and age.
	@Override
	protected String completeRequest() throws Exception {

		PreparedStatement incidentPs = ServerDriver.query_getIncidents();
		incidentPs.setString(1, this.routeNumber);
		ResultSet rs = incidentPs.executeQuery();

		System.out.println("List of Incidents on Route " + this.routeNumber.toUpperCase() + ":");
		System.out.println("================================");
		
		boolean isEmpty = true;

		while (rs.next()) {
			isEmpty = false;

			// get each tag from query
			String incidentTime = rs.getString("timestamp");
			String incidentLoc = rs.getString("stop");
			String incidentTag = rs.getString("tag");
			out.append(incidentTag + " at stop " + incidentLoc + " at " + incidentTime);
		}

		// if the query returned nothing, say "NONE"
		if (isEmpty)
			out.append("\nNONE");

		// return the output string
		return out.toString();
	}

}