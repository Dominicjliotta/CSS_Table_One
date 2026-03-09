package com.tableone.ptsss.server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

import org.postgresql.util.ExpressionProperties;

public class ServerDriver {

	private static Connection connection;
	private static final String DB_URL = "jdbc:postgresql://localhost/ptsssdb";
	private static final String PASSWORD = "***"; //YOUR PASSWORD
	
	/* global prepared query objects */
	private static PreparedStatement getSortedStopList;
	
	//connects to the database, sets up prepared statements, etc.
	public static boolean setup() {
		
		//connect to the database
		ExpressionProperties connectionProps = new ExpressionProperties();
		connectionProps.put("user", "postgres");
		connectionProps.put("password", PASSWORD);
		
		//save the connection to a global object
		try {
			connection = DriverManager.getConnection(DB_URL, connectionProps);
		} catch (Exception e) {
			System.out.println("Failed to connect to the database!");
			e.printStackTrace();
			return false;
		}
		
		//prepare all global queries
		try {
			prepareQueries();
		} catch (Exception e) {
			System.out.println("Failed to prepare queries!");
			e.printStackTrace();
			return false;
		}
		
		return true;
		
	}
	
	//close resources
	public static void tearDown() {
		
		try {
			connection.close();
		} catch (Exception e) {
			System.out.println("Error closing the database connection!");
			e.printStackTrace();
		}
		
	}
	
	//get the database connection object
	public static Connection getConnection() {
		return connection;
	}
	
	//calculates the score for a route given its route number
	public static double getScore(String routeNumber) {
		//TODO: score calculation implementation
		return 0;
	}
	
	//runs all PreparedStatement functions once to initialize them
	private static void prepareQueries() throws Exception {
		
		query_getSortedStopList();
		
	}
	
	/*
	 * query for getSortedStopList
	 *
	 * PARAMS:
	 * 1 - locatoinName (string)
	 * 2 - weekDay (string)
	 */
	public static PreparedStatement query_getSortedStopList() throws Exception {
		
		if (getSortedStopList != null) return getSortedStopList;
		
		getSortedStopList = connection.prepareStatement(
				"WITH WantedRouteStops AS (\r\n"
				+ "	SELECT routeStopID, routeNumber\r\n"
				+ "	FROM RouteStops\r\n"
				+ "		JOIN Stop ON (Stop.ID = RouteStops.stopID)\r\n"
				+ "	WHERE Stop.locationName ILIKE ?\r\n"
				+ ")\r\n"
				+ "\r\n"
				+ "SELECT DISTINCT WantedRouteStops.routeNumber, ArrivalTime.time\r\n"
				+ "FROM WantedRouteStops\r\n"
				+ "	JOIN ArrivalTime ON (ArrivalTime.routeStopID = WantedRouteStops.routeStopID)\r\n"
				+ "	JOIN WeekDay ON (WeekDay.ID = ArrivalTime.weekDayID)\r\n"
				+ "WHERE WeekDay.day ILIKE ?\r\n"
				+ "ORDER BY ArrivalTime.time;");
		
		return getSortedStopList;
		
	}
	
}