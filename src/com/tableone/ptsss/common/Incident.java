package com.tableone.ptsss.common;

import java.time.Instant;

public class Incident {

	/*
	 * object representation of the Incident table
	 * will we actually use this? i dont know! but its here just in case!
	 * if we dont use this, it should be deleted before submission
	 */
	
	private int id;
	private String description;
	private int stopID;
	private Instant incidentTime;
	private Instant creationTime;
	
	public Incident(int idIn, String descriptionIn, int stopIDin, Instant incidentTimeIn, Instant creationTimeIn) {
		this.id = idIn;
		this.description = descriptionIn;
		this.stopID = stopIDin;
		this.incidentTime = incidentTimeIn;
		this.creationTime = creationTimeIn;
	}
	
	public int getID() {
		return this.id;
	}
	
	public String getDescription() {
		return this.description;
	}
	
	public int getStopID() {
		return this.stopID;
	}
	
	public Instant getIncidentTime() {
		return this.incidentTime;
	}
	
	public Instant getCreationTime() {
		return this.creationTime;
	}
	
}
