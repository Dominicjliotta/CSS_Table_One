package com.tableone.ptsss.server;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

/*--------------------------------------------------------------------------------*/
/* ServerApiGetRouteInfo                                                          */
/* Author: Melissa                                                                */
/*                                                                                */
/* getRouteInfo(routeNumber)                                                      */
/* Returns route information including start and end locations, and current score */
/* calculated on the last 6 months.                                               */
/*--------------------------------------------------------------------------------*/

public class ServerApiGetRouteInfo extends ServerApi<String> {

    private String routeNumber;

    @Override
    protected void parseRequest(Object[] args) throws Exception {

        //make sure the server received the correct number of arguments
        if (args.length != 1) {
            throw new Exception("ServerApiGetRouteInfo received " + args.length + " arguments, expected 1");
        }

        //type-check route number
        if (!(args[0] instanceof String)) {
            throw new Exception("ServerApiGetRouteInfo received invalid type for route number");
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
        PreparedStatement info = ServerDriver.query_getRouteInfo();
        info.setString(1, this.routeNumber);

        //execute the query and get the results
        ResultSet rs = info.executeQuery();

        //build the output string
        StringBuilder out = new StringBuilder();
        out.append("Information on route: " + this.routeNumber.toUpperCase() + ":");
        
        if (rs.next()) {
        	
            out.append("\nStart Location: " + rs.getString("Start Location"));
            out.append("\nEnd Location: " + rs.getString("End Location"));
            
            //fetch the query for calculating semi-yearly route scores
            PreparedStatement scoreQuery = ServerDriver.query_getSemiYearlyScore();
            scoreQuery.setString(1, routeNumber);
            
            //get the score for the route
            ResultSet scoreRs = scoreQuery.executeQuery();

            if (scoreRs.next()) {
                double score = scoreRs.getDouble("score");
                out.append("\nSafety Score (Last 6 months): " + score);
            } else {
                out.append("\nSafety Score (Last 6 months): N/A");
            }
            
        }
        
        if (out.length() == 0) out.append("NONE");
        
        return out.toString();

    }

}