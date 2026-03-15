package com.tableone.ptsss.server;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ServerApiGetRecentTags extends ServerApi<String> {

    private String routeNumber;

    @Override
    protected void parseRequest(Object[] args) throws Exception {

        //make sure the server received the correct number of arguments
        if (args.length != 1) {
            throw new Exception("ServerApiGetRecentTags received " + args.length + " arguments, expected 1");
        }

        //type-check destination
        if (!(args[0] instanceof String)) {
            throw new Exception("ServerApiGetRecentTags received invalid type for Route Number");
        }

        //cast and store the arguments into variables
        this.routeNumber = (String)args[0];

    }

    @Override
    protected String completeRequest() throws Exception {

        //fetch the query for this api call
        PreparedStatement tagPs = ServerDriver.query_getRecentTags();
        //set the parameters
        tagPs.setString(1, this.routeNumber);

        //execute the query and get the results
        ResultSet rs = tagPs.executeQuery();

        //build the output string
        StringBuilder out = new StringBuilder();
        out.append("List of recent tags on route " + this.routeNumber.toUpperCase() + ":");

        boolean isEmpty = true;

        //iterate through each row of the ResultSet
        while (rs.next()) {
            isEmpty = false;

            //get each tag from query
            String contentName = rs.getString("name");
            String contentSeverity = rs.getString("severity");
            out.append("\nContent Tag: " + contentName);
        }

        //if the query returned nothing, say "NONE"
        if (isEmpty) out.append("\nNONE");

        //return the output string
        return out.toString();

    }


}



