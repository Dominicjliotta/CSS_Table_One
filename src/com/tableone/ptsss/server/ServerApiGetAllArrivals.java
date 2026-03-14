package com.tableone.ptsss.server;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ServerApiGetAllArrivals extends ServerApi<String> {

    ResultSet resultSet;
    int offset = 0;
    int buffer = 50; // number of results to return per call

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
        resultSet = ps.executeQuery();
        
        //build the output string
        StringBuilder out = new StringBuilder();
        out.append("List of all bus arrivals:\n");
        
        //iterate through each row of the ResultSet
        int count = 1;
        while (resultSet.next() && count <= 50) { // limit to 50 results for readability
            String routeNumber = resultSet.getString("routeNumber");
            String stopLocation = resultSet.getString("Stop Location");
            String bus = resultSet.getString("bus");
            String arrivalTime = resultSet.getString("arrivalTime");
            String day = resultSet.getString("day");
            
            out.append("Route " + routeNumber + " - " + "Bus " + bus + " Stop Location " + stopLocation + " on " + day + " at " + arrivalTime + "\n");
            count++;
        }
        
        return out.toString();
    }

    public String nextFifty(int buffer) throws Exception {

      
        //fetch the query for this api call
        PreparedStatement ps = ServerDriver.query_getAllArrivals();
        
        //execute the query and get the results
        resultSet = ps.executeQuery();
        
        //build the output string
        StringBuilder out = new StringBuilder();

        while(resultSet.next() && offset < buffer) { // skip the first 50 results
            offset++;
        }
        
        //iterate through each row of the ResultSet
        int count = 1;
        while (resultSet.next() && count <= 50) { // limit to 50 results for readability
            String routeNumber = resultSet.getString("routeNumber");
            String stopLocation = resultSet.getString("Stop Location");
            String bus = resultSet.getString("bus");
            String arrivalTime = resultSet.getString("arrivalTime");
            String day = resultSet.getString("day");
            
            out.append("Route " + routeNumber + " - " + "Bus " + bus + " Stop Location " + stopLocation + " on " + day + " at " + arrivalTime +  "\n");
            count++;
        }

        // buffer += 50; // increment buffer for next call

        if (count <= 50) {
            
            out.append("No more arrivals to display.\n");
        }
        
        return out.toString();
    }
}