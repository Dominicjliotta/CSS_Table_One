package com.tableone.ptsss.server;

import java.security.Timestamp;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Time;
import java.util.UUID;

/*
 * This ServerAPI is used to submit an incident
   submitIncident(string stopLocation);
 */

public class ServerApiSubmitIncident extends ServerApi<String> {

	private String stopLocation;
	private Timestamp time;
	private String description;
	private String[] tags;

	@Override
	protected void parseRequest(Object[] args) throws Exception {

		// check number of arguments
		if (args.length != 4) {
			throw new Exception("ServerApiSubmitIncidents received " + args.length + " arguments, expected 4");
		}

		// type check each argument
		if (!(args[0] instanceof String)) {
			throw new Exception("ServerApiSubmitIncidents received invalid type for Stop Location");
		}
		if (!(args[1] instanceof Timestamp)) {
			throw new Exception("ServerApiSubmitIncidents received invalid type for Time");
		}
		if (!(args[2] instanceof String)) {
			throw new Exception("ServerApiSubmitIncident received invalid type for Description");
		}
		if (!(args[3] instanceof String[])) {
			throw new Exception("ServerApiSubmitIncident received invalid type for Tags");
		}

		this.stopLocation = (String) args[0];
		this.time = (Timestamp) args[1];
		this.description = (String) args[2];
		this.tags = (String[]) args[3];

	}

	// this is the function that will finally fulfil the api call.
	// you may return an object, not all apis will have to.
	// if nothing has to be returned, simply return null instead.
	// here, we will just return a string containing the name and age.
	@Override
	protected String completeRequest() throws Exception {
		
		try {
			// use transaction as this is CRUD operation
			ServerDriver.beginTX();

			PreparedStatement incidentPs = ServerDriver.query_submitIncident();
			incidentPs.setString(1, this.stopLocation);
			incidentPs.setTimestamp(2, this.time);
			incidentPs.setString(3, this.description);


			String UUID = UUID.randomUUID().toString();
			incidentPs.setString(4, UUID);
			ResultSet rs = incidentPs.executeQuery();

			// for each tag in tags[], add it 
			for (String tag : this.tags) {
				addIncidentTag.setString(1, UUID);
				addIncidentTag.setString(2, tag);
				addIncidentTag.execute();
			}

			ServerDriver.commitTX();

			return UUID;
		} catch (Exception e) {
			return false;
		}
	}
}