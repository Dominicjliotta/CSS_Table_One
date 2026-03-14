package com.tableone.ptsss.server;

import java.sql.PreparedStatement;

import java.sql.ResultSet;

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
        this.routeNumber =  (String) args[0];
    }

    @Override
    protected String completeRequest() throws Exception {
        PreparedStatement ps = ServerDriver.query_getBuses();
        ps.setString(1, this.routeNumber);
        
        ResultSet rs = ps.executeQuery();
        StringBuilder result = new StringBuilder();
        while (rs.next()) {
            String id = rs.getString("ID");
            String name = rs.getString("name");

            if (id == null || name == null) {
                System.out.println("Warning: Null value found in database for bus with route number " + this.routeNumber);
                continue; // skip this record
            }
            result.append("ID: ").append(id).append(", Name: ").append(name).append("\n");
        }

        if (result.length() == 0) {
            result.append("\nNONE");
        }
        
        return result.toString();

    }
    
}
