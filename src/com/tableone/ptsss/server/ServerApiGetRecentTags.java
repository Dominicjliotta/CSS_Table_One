package com.tableone.ptsss.server;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

/*----------------------------------------------------------*/
/* ServerApiGetRecentTags                                   */
/* Author: Melissa                                          */
/*                                                          */
/* getRecentTags(routeNumber)                               */
/* Returns a list of  tags from  incidents across all stops */
/* along a route from the last 6 months.                    */
/*----------------------------------------------------------*/

public class ServerApiGetRecentTags extends ServerApi<String> {

    private String routeNumber;

    @Override
    protected void parseRequest(Object[] args) throws Exception {

        //make sure the server received the correct number of arguments
        if (args.length != 1) {
            throw new Exception("ServerApiGetRecentTags received " + args.length + " arguments, expected 1");
        }

        //type-check route number
        if (!(args[0] instanceof String)) {
            throw new Exception("ServerApiGetRecentTags received invalid type for route number");
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

        //fetch the query for this api call and set the parameters
        PreparedStatement tagPs = ServerDriver.query_getRecentTags();
        tagPs.setString(1, this.routeNumber);

        //execute the query
        ResultSet rs = tagPs.executeQuery();

        //build the output string
        StringBuilder out = new StringBuilder();
        out.append("List of recent tags on route " + this.routeNumber.toUpperCase() + ":");

        boolean isEmpty = true;
        while (rs.next()) {
        	
            isEmpty = false;
            
            String contentName = rs.getString("name");
            String contentSeverity = rs.getString("severity");
            out.append("\nContent Tag: " + contentName + " (" + contentSeverity + ")");
            
        }

        //if the query returned nothing, say "NONE"
        if (isEmpty) out.append("\nNONE");
        
        return out.toString();

    }

}