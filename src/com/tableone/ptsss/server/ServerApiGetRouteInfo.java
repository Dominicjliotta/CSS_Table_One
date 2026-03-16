package com.tableone.ptsss.server;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
public class ServerApiGetRouteInfo extends ServerApi<String> {

    private String routeNumber;

    @Override
    protected void parseRequest(Object[] args) throws Exception {

        //make sure the server received the correct number of arguments
        if (args.length != 1) {
            throw new Exception("ServerApiGetRouteInfo received " + args.length + " arguments, expected 1");
        }

        //type-check destination
        if (!(args[0] instanceof String)) {
            throw new Exception("ServerApiGetRouteInfo received invalid type for Route Number");
        }

        //cast and store the arguments into variables
        this.routeNumber = (String)args[0];

    }

    @Override
    protected String completeRequest() throws Exception {

        //reject any nonexsistant route numbers
        PreparedStatement ps = ServerDriver.query_routeExists();
        ps.setString(1, this.routeNumber);
        ResultSet Rnumber = ps.executeQuery();
        if (!Rnumber.next()) throw new Exception("Invalid Route Number!");

        //fetch the query for this api call
        PreparedStatement info = ServerDriver.query_getRouteInfo();
        //set the parameters
        info.setString(1, this.routeNumber);

        //execute the query and get the results
        ResultSet rs = info.executeQuery();

        //build the output string
        StringBuilder out = new StringBuilder();
        out.append("Information on route: " + this.routeNumber.toUpperCase() + ":");

        if (!rs.next()) {
            out.append("\nNONE");
        } else {
            out.append("\nStart Location: " + rs.getString("Start Location"));
            out.append("\nEnd Location: " + rs.getString("End Location"));


            //fetch the query for calculating semi-yearly route scores
            PreparedStatement score = ServerDriver.query_getSemiYearlyScore();
            //get the score for the route
            score.setString(1, routeNumber);
            ResultSet scoreRs = score.executeQuery();

            if (scoreRs.next()) {
                double doubleScore = scoreRs.getDouble("score");
                out.append("\nSafety Score (Last 6 months): " + doubleScore);
            } else {
                out.append("\nSafety Score: N/A");
            }
        }
            //return the output string
            return out.toString();

    }


}



