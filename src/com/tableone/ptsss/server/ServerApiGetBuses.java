package com.tableone.ptsss.server;

import java.sql.PreparedStatement;

import java.sql.ResultSet;

/*-----------------------------------------------*/
/* ServerApiGetBuses                             */
/* Author: Dom                                   */
/*                                               */
/* getBuses(routeNumber)                         */
/* Returns a list of bus names on a given route. */
/*-----------------------------------------------*/

public class ServerApiGetBuses extends ServerApi<String> {
	
    private String routeNumber;
   
    @Override
    protected void parseRequest(Object[] args) throws Exception {
    	
        //make sure the server received the correct number of arguments
		if (args.length != 1) {
			throw new Exception("ServerApiGetBuses received " + args.length + " arguments, expected 1");
		}
		
        //type-check routeNumber
        if (!(args[0] instanceof String)) {
			throw new Exception("ServerApiGetBuses received invalid type for route number");
		}
        
        this.routeNumber =  (String)args[0];
        
    }

    @Override
    protected String completeRequest() throws Exception {
    	
        //reject any non-existent route numbers
        PreparedStatement ps = ServerDriver.query_routeExists();
        ps.setString(1, this.routeNumber);
        
        ResultSet routeExists = ps.executeQuery();
        if (!routeExists.next()) throw new Exception("Invalid route number!");
    	
        //fetch the query for this api call and set the parameters
        ps = ServerDriver.query_getBuses();
        ps.setString(1, this.routeNumber);
        
        //execute the query
        ResultSet rs = ps.executeQuery();
        
        //build the result string
        StringBuilder result = new StringBuilder();
        
        while (rs.next()) {
        	
        	//get the needed information
            String id = rs.getString("ID");
            String name = rs.getString("name");

            //skip any results with null values
            if (id == null || name == null) {
                System.out.println("Warning: Null value found in database for bus with route number " + this.routeNumber);
                continue;
            }
            
            result.append("ID: ").append(id).append(", Name: ").append(name).append("\n");
            
        }

        if (result.length() == 0) result.append("NONE");
        
        return result.toString();

    }
    
}
