package com.tableone.ptsss.common;

public class Bus {

	/*
	 * object representation of the Bus table
	 * will we actually use this? i dont know! but its here just in case!
	 * if we dont use this, it should be deleted before submission
	 */
	
	private int id;
	private String name;
	private String routeNumber;
	
	public Bus(int idIn, String nameIn, String routeNumberIn) {
		this.id = idIn;
		this.name = nameIn;
		this.routeNumber = routeNumberIn;
	}
	
	public int getID() {
		return this.id;
	}
	
	public String getName() {
		return this.name;
	}
	
	public String getRouteNumber() {
		return this.routeNumber;
	}
	
}