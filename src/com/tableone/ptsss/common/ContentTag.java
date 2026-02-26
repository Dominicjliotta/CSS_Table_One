package com.tableone.ptsss.common;

public class ContentTag {

	/*
	 * object representation of the ContentTag table
	 * will we actually use this? i dont know! but its here just in case!
	 * if we dont use this, it should be deleted before submission
	 */
	
	private String id;
	private String name;
	private int severity;
	
	public ContentTag(String idIn, String nameIn, int severityIn) {
		this.id = idIn;
		this.name = nameIn;
		this.severity = severityIn;
	}
	
	public String getID() {
		return this.id;
	}
	
	public String getName() {
		return this.name;
	}
	
	public int getSeverity() {
		return this.severity;
	}
	
}
