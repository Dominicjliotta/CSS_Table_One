package com.tableone.ptsss.server;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;

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
	
	@SuppressWarnings("unchecked")
	@Override
	protected String completeRequest() throws Exception {

		//reject any non-existent route numbers
        PreparedStatement ps = ServerDriver.query_routeExists();
        ps.setString(1, this.routeNumber);
        
        ResultSet routeExists = ps.executeQuery();
        if (!routeExists.next()) throw new Exception("Invalid route number!");
		
		//fetch the query for this api call and set the parameters
		PreparedStatement incidentPs = ServerDriver.query_getIncidents();
		incidentPs.setString(1, this.routeNumber);
		
		//execute the query
		ResultSet rs = incidentPs.executeQuery();

		//build the output string
		StringBuilder out = new StringBuilder();
		
		out.append("List of Incidents on Route " + this.routeNumber.toUpperCase() + ":\n");
		out.append("================================");
		
		//used to store data for all incidents
		HashMap<String, Object[]> incidentMap = new HashMap<>();
		
		//iterate through the list of incidents
		boolean isEmpty = true;
		while (rs.next()) {
			
			isEmpty = false;
			
			//collect data for this row
			String time = rs.getString("incidentTime");
			String stop = rs.getString("locationName");
			String description = rs.getString("description");
			String uuid = rs.getString("uuid");
			String tag = rs.getString("name");
			
			//create an entry for this incident if it has not already been created
			if (!incidentMap.containsKey(uuid)) {
				incidentMap.put(uuid, new Object[] {time, stop, description, new ArrayList<String>()});
			}
			
			//add this tag to a list of tags for this incident
			((ArrayList<String>)(incidentMap.get(uuid)[3])).add(tag);
			
		}
		
		//iterate through each incident
		for (String uuid : incidentMap.keySet()) {
			
			Object[] values = incidentMap.get(uuid);
			
			//get the values for this incident
			String time = (String)values[0];
			String stop = (String)values[1];
			String description = (String)values[2];
			ArrayList<String> tags = (ArrayList<String>)values[3];
			
			//print the values
			out.append("\n\nStop: ").append(stop).append(" | Time: ").append(time).append(" | Description: ")
			.append(description).append("\nTags:");
			
			for (String tag : tags) out.append("\n- ").append(tag);
			
		}

		//if the query returned nothing, say "NONE"
		if (isEmpty) out.append("\nNONE");
		
		return out.toString();
		
	}

}