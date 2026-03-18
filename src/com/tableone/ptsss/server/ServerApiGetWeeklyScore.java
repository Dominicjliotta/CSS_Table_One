package com.tableone.ptsss.server;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

/*-----------------------------------------------------------------*/
/* ServerApiGetWeeklyScore                                         */
/* Author: Lily                                                    */
/*                                                                 */
/* getWeeklyScore(routeNumber)                                     */
/* Returns the weekly score of a given route from the last 7 days. */
/*-----------------------------------------------------------------*/

public class ServerApiGetWeeklyScore extends ServerApi<String> {

    private String routeNumber;

    @Override
    protected void parseRequest(Object[] args) throws Exception {

    	//make sure the server received the correct number of arguments
        if (args.length != 1) {
            throw new Exception("ServerApiGetWeeklyScore received " + args.length + " arguments, expected 1");
        }

        //type-check route number
        if (!(args[0] instanceof String)) {
            throw new Exception("ServerApiGetWeeklyScore received invalid type for route number");
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

        //fetch the weekly score for this route
        PreparedStatement scorePs = ServerDriver.query_getWeeklyScore();
        scorePs.setString(1, this.routeNumber);
        ResultSet scoreRs = scorePs.executeQuery();

        //build the output string
        StringBuilder out = new StringBuilder();
        out.append("Weekly safety score for route " + this.routeNumber.toUpperCase() + " (last 7 days): ");

        if (scoreRs.next()) {
            double score = scoreRs.getDouble("score");
            out.append(score);
        } else {
            out.append("N/A");
        }

        return out.toString();
        
    }
    
}

