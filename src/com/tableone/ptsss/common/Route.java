package com.tableone.ptsss.common;

public class Route {

	/*
	 * object representation of the Route table
	 * will we actually use this? i dont know! but its here just in case!
	 * if we dont use this, it should be deleted before submission
	 */
	
	private String number;
	private String startPoint;
	private String endPoint;
	
	public Route(String numberIn, String startPointIn, String endPointIn) {
		this.number = numberIn;
		this.startPoint = startPointIn;
		this.endPoint = endPointIn;
	}
	
	public String getNumber() {
		return this.number;
	}
	
	public String getStartPoint() {
		return this.startPoint;
	}
	
	public String getEndPoint() {
		return this.endPoint;
	}
	
}