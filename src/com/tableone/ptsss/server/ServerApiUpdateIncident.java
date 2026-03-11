package com.tableone.ptsss.server;

import java.sql.PreparedStatement;

public class ServerApiUpdateIncident extends ServerApi<String> {

	private String uuid;
	private String description;
	private String[] tags;
	
	@Override
	protected void parseRequest(Object[] args) throws Exception {
		
		if (args.length != 3) {
			throw new Exception("ServerApiUpdateIncident received " + args.length + " arguments, expected 3");
		}
		
		if (!(args[0] instanceof String)) {
			throw new Exception("ServerApiUpdateIncident received invalid type for uuid");
		}
		
		if (args[1] != null && !(args[1] instanceof String)) {
			throw new Exception("ServerApiUpdateIncident received invalid type for newDescription");
		}
		
		if (args[2] != null && !(args[2] instanceof String[])) {
			throw new Exception("ServerApiUpdateIncident received invalid type for newTags");
		}
		
		this.uuid = (String)args[0];
		this.description = (String)args[1];
		this.tags = (String[])args[2];
		
	}

	@Override
	protected String completeRequest() throws Exception {
		
		try {
		
			ServerDriver.beginTX();
			
			if (this.description != null) {
				PreparedStatement setIncidentDescription = ServerDriver.query_setIncidentDescription();
				setIncidentDescription.setString(1, this.description);
				setIncidentDescription.setString(2, this.uuid);
				setIncidentDescription.execute();
			}
			
			if (this.tags != null) {
				
				PreparedStatement deleteIncidentTags = ServerDriver.query_deleteIncidentTags();
				deleteIncidentTags.setString(1, this.uuid);
				deleteIncidentTags.execute();
				
				PreparedStatement addIncidentTag = ServerDriver.query_addIncidentTag();
				addIncidentTag.setString(1, this.uuid);
				
				for (String tag : this.tags) {
					addIncidentTag.setString(2, tag);
					addIncidentTag.execute();
				}
				
			}
			
			ServerDriver.commitTX();
			
			String out = "Updated incident " + this.uuid + "!";
			if (this.description != null) out += "\nNew description: " + this.description;
			if (this.tags == null) return out;
			
			out += "\nNew tags: ";
			
			for (int i = 0; i < this.tags.length; i++) {
				if (i > 0) out += ", ";
				out += this.tags[i];
			}
			
			return out;
		
		} catch (Exception e) {
			throw new Exception("Could not update incident");
		}
		
	}

}
