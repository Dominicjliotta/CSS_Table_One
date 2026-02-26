package com.tableone.ptsss.common;

public class Stop {

	/*
	 * object representation of the Stop table
	 * will we actually use this? i dont know! but its here just in case!
	 * if we dont use this, it should be deleted before submission
	 */
	
	private int id;
	private String locationName;
	
	public Stop(int idIn, String locationNameIn) {
		this.id = idIn;
		this.locationName = locationNameIn;
	}
	
	public int getID() {
		return this.id;
	}
	
	public String getLocationName() {
		return this.locationName;
	}
	
}
