package com.tableone.ptsss.server;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;

public class ServerApiGetIncidentsByStop extends ServerApi<String> {

    private String locationName;

    @Override
    protected void parseRequest(Object[] args) throws Exception {

    	//make sure the server received the correct number of arguments
        if (args.length != 1) {
            throw new Exception("ServerApiGetIncidentsByStop received " + args.length + " arguments, expected 1");
        }

        //type-check location name
        if (!(args[0] instanceof String)) {
            throw new Exception("ServerApiGetIncidentsByStop received invalid type for locationName");
        }

        this.locationName = (String)args[0];
    }

    @SuppressWarnings("unchecked")
	@Override
    protected String completeRequest() throws Exception {

    	//fetch the query for this api call and set the parameters
        PreparedStatement ps = ServerDriver.query_getIncidentsByStop();
        ps.setString(1, this.locationName);
        
        //execute the query
        ResultSet rs = ps.executeQuery();

        //build the output string
        StringBuilder out = new StringBuilder();
        out.append("Incidents at stop: " + this.locationName + ":");
        out.append("\n================================");
        
        //used to store data for all incidents
      	HashMap<String, Object[]> incidentMap = new HashMap<>();

      	//iterate through the list of incidents
        boolean isEmpty = true;
        while (rs.next()) {
        	
            isEmpty = false;
            
            //collect data for this row
            String time = rs.getString("incidentTime");
            String description = rs.getString("description");
            String tag = rs.getString("tag");
            String stop = rs.getString("locationName");
            String uuid = rs.getString("uuid");

            //create an entry for this incident if it has not already been created
			if (!incidentMap.containsKey(uuid)) {
				incidentMap.put(uuid, new Object[] {time, stop, description, new ArrayList<String>()});
			}
			
			//add this tag to a list of tags for this incident
			((ArrayList<String>)(incidentMap.get(uuid)[3])).add(tag);
            
        }
        
        //iterate through each incident
  		for (String uuid : incidentMap.keySet()) {
  			
  			Object[] values = incidentMap.get(uuid);
  			
  			//get the values for this incident
  			String time = (String)values[0];
  			String stop = (String)values[1];
  			String description = (String)values[2];
  			ArrayList<String> tags = (ArrayList<String>)values[3];
  			
  			//print the values
  			out.append("\n\nStop: ").append(stop).append(" | Time: ").append(time).append(" | Description: ")
  			.append(description).append("\nTags:");
  			
  			for (String tag : tags) out.append("\n- ").append(tag);
  			
  		}

        if (isEmpty) out.append("\nNONE");

        return out.toString();
        
    }
    
}