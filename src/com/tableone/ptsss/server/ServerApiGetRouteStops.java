package com.tableone.ptsss.server;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

/*--------------------------------------------------------------------------*/
/* ServerApiGetRouteStops                                                   */
/* Author: Lily                                                             */
/*                                                                          */
/* getRouteStops(routeNumber)                                               */
/* Returns a list of stops on a route including location and arrival times. */
/*--------------------------------------------------------------------------*/

public class ServerApiGetRouteStops extends ServerApi<String> {

	private String routeNumber;
	
	@Override
	protected void parseRequest(Object[] args) throws Exception {

		//check number of arguments
		if (args.length != 1) {
			throw new Exception("ServerApiGetRouteStops received " + args.length + " arguments, expected 1");
		}

		//type-check route number
		if (!(args[0] instanceof String)) {
			throw new Exception("ServerApiGetRouteStops received invalid type for route number");
		}
		
		this.routeNumber = (String)args[0];

	}
	
	@Override
	protected String completeRequest() throws Exception {
	    
		//reject any non-existent route numbers
        PreparedStatement ps = ServerDriver.query_routeExists();
        ps.setString(1, this.routeNumber);
        
        ResultSet routeExists = ps.executeQuery();
        if (!routeExists.next()) throw new Exception("Invalid route number!");
	
	    //fetch the query for this api call
        PreparedStatement stops = ServerDriver.query_getRouteStops();
        stops.setString(1, this.routeNumber);
        
        //execute the query
        ResultSet stopsRs = stops.executeQuery();
	
	    //build the output string
	    StringBuilder out = new StringBuilder();
	    out.append("Stops on route: " + this.routeNumber.toUpperCase() + ":");
	    
        boolean hasStops = false;
        while (stopsRs.next()) {
            hasStops = true;
            out.append("\n- " + stopsRs.getString("locationName"));
        }
        
        if (!hasStops) out.append("\nNONE");
        
	    return out.toString();
	    
	}
	
}

