package com.tableone.ptsss.server;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

/*--------------------------------------------------------------------*/
/* ServerApiGetIncidentsByStop                                        */
/* Author: Lily                                                       */
/*                                                                    */
/* getIncidentsByStop(locationName)                                   */
/* Returns a list of incident information based on the stop location. */
/*--------------------------------------------------------------------*/

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

      	//iterate through the list of incidents
        boolean isEmpty = true;
        //keep track of the current incident as we iterate,
        //since each incident may have multiple rows containing incident tags
        String prevUUID = null;
        Incident incident = null;
        while (rs.next()) {
        	
            isEmpty = false;
            
            //collect data for this row
            String time = rs.getString("incidentTime");
            String description = rs.getString("description");
            String tag = rs.getString("tag");
            String stop = rs.getString("locationName");
            String uuid = rs.getString("uuid");

            //if this is the first row, or this is a new incident
            if (prevUUID == null || !(prevUUID.equals(uuid))) {
            	//append the previous incident to the output if it exists
            	if (incident != null) out.append("\n").append(incident);
            	//replace the old incident with a new incident with the new row data
            	incident = new Incident(time, description, stop);
            }
            
            //add the current tag to the incident
            incident.tags.add(tag);
            
        }
        
        //print the last incident if it exists
        if (incident != null) out.append("\n").append(incident);

      //if the query returned nothing, say "NONE"
        if (isEmpty) out.append("\nNONE");

        return out.toString();
        
    }
	
	//inner incident class, used in this api for storing incident data
	private static class Incident {
		
		//information needed to print
		public String time;
		public String description;
		public String stop;
		public ArrayList<String> tags;
		
		public Incident(String time, String description, String stop) {
			this.time = time;
			this.description = description;
			this.stop = stop;
			this.tags = new ArrayList<>();
		}
		
		//build a clean string representation of this object to print
		@Override
		public String toString() {
			
			StringBuilder s = new StringBuilder();
			s.append("\nStop: ").append(this.stop);
			s.append(" | Time: ").append(this.time);
			s.append(" | Description: ").append(this.description);
			s.append("\nTags:");
			
			for (String tag : this.tags) s.append("\n- ").append(tag);
			
			return s.toString();
			
		}
		
	}
    
}