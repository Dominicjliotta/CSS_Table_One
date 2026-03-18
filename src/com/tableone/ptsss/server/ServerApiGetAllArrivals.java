package com.tableone.ptsss.server;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/*------------------------------------------------------------------*/
/* ServerApiGetAllArrivals                                          */
/* Author: Dom                                                      */
/*                                                                  */
/* getAllArrivals()                                                 */
/* Returns a list of all arrival times for all stops on all routes. */
/*------------------------------------------------------------------*/

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
        //return the first 50 items
    	return this.nextFifty(0);
    }

    //gets 50 items from the long list, starting from a given index (buffer)
    public String nextFifty(int buffer) throws Exception {
    	
        //fetch the query for this api call
        PreparedStatement ps = ServerDriver.query_getAllArrivals();
        
        //execute the query and get the results
        ResultSet rs = ps.executeQuery();
        
        //build the output string
        StringBuilder out = new StringBuilder();

        //skip the first [buffer] results
        int offset = 0;
        while(offset < buffer && rs.next()) offset++;
        
        //get the next 50 results
        int count = 0;
        while (count < 50 && rs.next()) {
        	
        	//get the needed information
            String routeNumber = rs.getString("routeNumber");
            String stopLocation = rs.getString("Stop Location");
            String bus = rs.getString("bus");
            String arrivalTime = rs.getString("arrivalTime");
            String day = rs.getString("day");
            
            //append the information in one line
            out.append("Route " + routeNumber + " - " + "Bus " + bus + " Stop Location " + stopLocation + " on " + day + " at " + arrivalTime +  "\n");
            count++;
            
        }

        //tell the user if the end of the list as been reached
        if (!rs.next()) out.append("No more arrivals to display.\n");
        
        return out.toString();
        
    }
    
}