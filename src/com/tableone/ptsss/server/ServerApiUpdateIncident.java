package com.tableone.ptsss.server;

import java.sql.PreparedStatement;

public class ServerApiUpdateIncident extends ServerApi<Boolean> {

	private String uuid;
	private String description;
	private String[] tags;
	
	@Override
	protected void parseRequest(Object[] args) throws Exception {
		
		//make sure the server received the correct number of arguments
		if (args.length != 3) {
			throw new Exception("ServerApiUpdateIncident received " + args.length + " arguments, expected 3");
		}
		
		//type-check uuid
		if (!(args[0] instanceof String)) {
			throw new Exception("ServerApiUpdateIncident received invalid type for uuid");
		}
		
		//type-check description
		if (args[1] != null && !(args[1] instanceof String)) {
			throw new Exception("ServerApiUpdateIncident received invalid type for newDescription");
		}
		
		//type-check tags
		if (args[2] != null && !(args[2] instanceof String[])) {
			throw new Exception("ServerApiUpdateIncident received invalid type for newTags");
		}
		
		//cast and store the arguments into variables
		this.uuid = (String)args[0];
		this.description = (String)args[1];
		this.tags = (String[])args[2];
		
	}

	@Override
	protected Boolean completeRequest() throws Exception {
		
		try {
		
			//begin a transaction because this performs CRUD operations
			ServerDriver.beginTX();
			
			//if the user wants to change the description...
			if (this.description != null) {
				//then change it
				PreparedStatement setIncidentDescription = ServerDriver.query_setIncidentDescription();
				setIncidentDescription.setString(1, this.description);
				setIncidentDescription.setString(2, this.uuid);
				setIncidentDescription.execute();
			}
			
			//if the user wants to change the tags...
			if (this.tags != null) {
				
				//delete all incident tags
				PreparedStatement deleteIncidentTags = ServerDriver.query_deleteIncidentTags();
				deleteIncidentTags.setString(1, this.uuid);
				deleteIncidentTags.execute();
				
				//and add the new tags one at a time
				PreparedStatement addIncidentTag = ServerDriver.query_addIncidentTag();
				addIncidentTag.setString(1, this.uuid);
				
				for (String tag : this.tags) {
					addIncidentTag.setString(2, tag);
					addIncidentTag.execute();
				}
				
			}
			
			//commit the transaction
			ServerDriver.commitTX();
			
			//no errors occurred, return true
			return true;
		
		} catch (Exception e) {
			//something didnt work! return false
			return false;
		}
		
	}

}
