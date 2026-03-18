package com.tableone.ptsss.server;

import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.util.UUID;

/*------------------------------------------------------------------------------*/
/* ServerApiSubmitIncident                                                      */
/* Author: David                                                                */
/*                                                                              */
/* submitIncident(stopLocation, time, description, tags[])                      */
/* Submits an incident for a stop containing content tags, stop location,       */
/* description, and time of incident. Returns the UUID of the created incident. */
/*------------------------------------------------------------------------------*/

public class ServerApiSubmitIncident extends ServerApi<String> {

	private String stopLocation;
	private Timestamp time;
	private String description;
	private String[] tags;

	@Override
	protected void parseRequest(Object[] args) throws Exception {

		//check number of arguments
		if (args.length != 4) {
			throw new Exception("ServerApiSubmitIncidents received " + args.length + " arguments, expected 4");
		}

		//type-check stop location
		if (!(args[0] instanceof String)) {
			throw new Exception("ServerApiSubmitIncidents received invalid type for stop location");
		}
		
		//type-check time
		if (!(args[1] instanceof Timestamp)) {
			throw new Exception("ServerApiSubmitIncidents received invalid type for time");
		}
		
		//type-check description
		if (!(args[2] instanceof String)) {
			throw new Exception("ServerApiSubmitIncident received invalid type for description");
		}
		
		//type-check tags
		if (!(args[3] instanceof String[])) {
			throw new Exception("ServerApiSubmitIncident received invalid type for tags");
		}

		this.stopLocation = (String)args[0];
		this.time = (Timestamp)args[1];
		this.description = (String)args[2];
		this.tags = (String[])args[3];

	}
	
	@Override
	protected String completeRequest() throws Exception {
		
		try {
		
			//use transaction as this does CRUD operations
			ServerDriver.beginTX();
			
			//create the uuid for this incident
			String uuid = UUID.randomUUID().toString();
	
			//fetch the query for this api call and set the parameters
			PreparedStatement incidentPs = ServerDriver.query_submitIncident();
			incidentPs.setString(1, this.stopLocation);
			incidentPs.setTimestamp(2, this.time);
			incidentPs.setString(3, this.description);
			incidentPs.setString(4, uuid);
			
			//execute the query
			incidentPs.execute();
			
			//fetch the query for adding tags to the incident
			PreparedStatement addIncidentTag = ServerDriver.query_addIncidentTag();
			addIncidentTag.setString(1, uuid);
	
			//for each tag in tags[], add it
			for (String tag : this.tags) {
				addIncidentTag.setString(1, uuid);
				addIncidentTag.setString(2, tag);
				addIncidentTag.execute();
			}
			
			ServerDriver.commitTX();
	
			return uuid;
		
		} catch (Exception E) {
			ServerDriver.rollbackTX();
			return null;
		}
					
	}
	
}