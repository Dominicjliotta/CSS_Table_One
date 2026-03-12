package com.tableone.ptsss.server;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ServerApiGetAllArrivals extends ServerApi<String> {

    @Override
    protected void parseRequest(Object[] args) throws Exception {
        //no arguments to parse for this api call
        if (args.length != 0) {
            throw new Exception("ServerApiGetAllArrivals received " + args.length + " arguments, expected 0");
        }
    }

    @Override
    protected String completeRequest() throws Exception {

        
        //fetch the query for this api call
        PreparedStatement ps = ServerDriver.query_getAllArrivals();
        
        //execute the query and get the results
        ResultSet rs = ps.executeQuery();
        
        //build the output string
        StringBuilder out = new StringBuilder();
        out.append("List of all bus arrivals:\n");
        
        //iterate through each row of the ResultSet
        while (rs.next()) {
            String routeNumber = rs.getString("routeNumber");
            String stopLocation = rs.getString("Stop Location");
            String bus = rs.getString("bus");
            String arrivalTime = rs.getString("arrivalTime");
            String day = rs.getString("day");
            
            out.append("Route " + routeNumber + " - " + "Bus " + bus + " Stop Location " + stopLocation + " on " + day + " at " + arrivalTime + "\n");
        }
        
        return out.toString();
    }
}