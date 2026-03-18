package com.tableone.ptsss.server;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

/*----------------------------------------------------------*/
/* ServerApiGetIncidents                                    */
/* Author: David                                            */
/*                                                          */
/* getIncidents(routeNumber)                                */
/* Returns a list of incidents for all stops along a route. */
/*----------------------------------------------------------*/

public class ServerApiGetIncidents extends ServerApi<String> {

	private String routeNumber;

	@Override
	protected void parseRequest(Object[] args) throws Exception {

		//check number of arguments
		if (args.length != 1) {
			throw new Exception("ServerApiGetIncidents received " + args.length + " arguments, expected 1");
		}

		//type-check route number
		if (!(args[0] instanceof String)) {
			throw new Exception("ServerApiGetIncidents received invalid type for route number");
		}
		
		this.routeNumber = (String)args[0];
		
	}
	
	@Override
	protected String completeRequest() throws Exception {

		//fetch the query for this api call and set the parameters
		PreparedStatement incidentPs = ServerDriver.query_getIncidents();
		incidentPs.setString(1, this.routeNumber);
		
		//execute the query
		ResultSet rs = incidentPs.executeQuery();

		//build the output string
		StringBuilder out = new StringBuilder();
		
		out.append("List of Incidents on Route " + this.routeNumber.toUpperCase() + ":\n");
		out.append("================================\n");
		
		//iterate through the list of incidents
		boolean isEmpty = true;
		while (rs.next()) {
			
			isEmpty = false;
			
			String incidentTime = rs.getString("timestamp");
			String incidentLoc = rs.getString("stop");
			String incidentTag = rs.getString("tag");
			out.append(incidentTag + " at stop " + incidentLoc + " at " + incidentTime);
			
		}

		//if the query returned nothing, say "NONE"
		if (isEmpty) out.append("NONE");
		
		return out.toString();
		
	}

}