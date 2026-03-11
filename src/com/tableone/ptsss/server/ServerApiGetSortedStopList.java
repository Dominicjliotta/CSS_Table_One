package com.tableone.ptsss.server;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Time;

public class ServerApiGetSortedStopList extends ServerApi<String> {

	private String destination;
	private String weekDay;
	
	@Override
	protected void parseRequest(Object[] args) throws Exception {
		
		//make sure the server received the correct number of arguments
		if (args.length != 2) {
			throw new Exception("ServerApiGetSortedStopList received " + args.length + " arguments, expected 2");
		}
		
		//type-check destination
		if (!(args[0] instanceof String)) {
			throw new Exception("ServerApiGetSortedStopList received invalid type for destination");
		}
		
		//type-check weekDay
		if (!(args[1] instanceof String)) {
			throw new Exception("ServerApiGetSortedStopList received invalid type for weekDay");
		}
		
		//cast and store the arguments into variables
		this.destination = (String)args[0];
		this.weekDay = (String)args[1];
		
	}

	@Override
	protected String completeRequest() throws Exception {
		
		//fetch the query for calculating overall route scores
		PreparedStatement scorePs = ServerDriver.query_getOverallScore();
		
		//fetch the query for this api call
		PreparedStatement ps = ServerDriver.query_getSortedStopList();
		//set the parameters
		ps.setString(1, this.destination);
		ps.setString(2, this.weekDay);
		
		//execute the query and get the results
		ResultSet rs = ps.executeQuery();
		
		//build the output string
		StringBuilder out = new StringBuilder();
		out.append("List of buses arriving at " + this.destination.toUpperCase() + " on " + this.weekDay.toUpperCase() + ":");
		
		boolean isEmpty = true;
		
		//iterate through each row of the ResultSet
		while (rs.next()) {
			
			isEmpty = false;
			
			//get column values
			String routeNumber = rs.getString("routeNumber");
			Time time = rs.getTime("time");
			
			//get the score for the route
			scorePs.setString(1, routeNumber);
			ResultSet scoreRs = scorePs.executeQuery();
			scoreRs.next();
			double score = scoreRs.getDouble("score");
			
			//add to the output string
			out.append("\n");
			out.append("Route number: " + routeNumber + " | Arrival time: " + time + " | Safety rating: " + score);
			
		}
		
		//if the query returned nothing, say "NONE"
		if (isEmpty) out.append("\nNONE");
		
		//return the output string
		return out.toString();
		
	}

}
