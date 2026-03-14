package com.tableone.ptsss.server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.Properties;

public class ServerDriver {

	private static Connection connection;
	private static final String DB_URL = "jdbc:postgresql://localhost/ptsssdb";
	private static final String PASSWORD = "Claudio77!"; //YOUR PASSWORD
	
	/* global prepared query objects */
	private static PreparedStatement getSemiYearlyScore;
	private static PreparedStatement getWeeklyScore;
	
	private static PreparedStatement getSortedStopList;
	private static PreparedStatement deleteIncidentTags;
	private static PreparedStatement setIncidentDescription;
	private static PreparedStatement addIncidentTag;
	private static PreparedStatement getAllArrivals;
	
	//connects to the database, sets up prepared statements, etc.
	public static boolean setup() {
		
		//connect to the database
		Properties connectionProps = new Properties();
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
	
	//begin a transaction
	public static void beginTX() throws Exception {
		if (connection.getAutoCommit() == false) return;
		connection.setAutoCommit(false);
	}
	
	//commit a transaction
	public static void commitTX() throws Exception {
		if (connection.getAutoCommit() == true) return;
		connection.commit();
		connection.setAutoCommit(true);
	}
	
	//runs all PreparedStatement functions once to initialize them
	private static void prepareQueries() throws Exception {
		
		query_getSemiYearlyScore();
		query_getWeeklyScore();
		query_getSortedStopList();
		query_deleteIncidentTags();
		query_addIncidentTag();
		query_setIncidentDescription();
		query_getAllArrivals();
	}
	
	/*
	 * query for getting a route's semi-yearly score (past 6 months)
	 *
	 * PARAMS:
	 * 1 - routeNumber (string)
	 */
	public static PreparedStatement query_getSemiYearlyScore() throws Exception {
		
		if (getSemiYearlyScore != null) return getSemiYearlyScore;
		
		getSemiYearlyScore = connection.prepareStatement(
				"SELECT AVG(ContentTag.severity) AS \"score\"\r\n"
				+ "FROM Route\r\n"
				+ "	JOIN RouteStops ON (RouteStops.routeNumber = Route.number)\r\n"
				+ "	JOIN Stop ON (Stop.ID = RouteStops.stopID)\r\n"
				+ "	JOIN Incident ON (Incident.stopID = Stop.ID)\r\n"
				+ "	JOIN IncidentTags ON (IncidentTags.incidentID = Incident.ID)\r\n"
				+ "	JOIN ContentTag ON (ContentTag.ID = IncidentTags.tagID)\r\n"
				+ "WHERE Route.number = ?\r\n"
				+ "AND (now() - Incident.createdAt) < '6month';");
		
		return getSemiYearlyScore;
		
	}
	
	/*
	 * query for getting a route's weekly score (past 7 days)
	 *
	 * PARAMS:
	 * 1 - routeNumber (string)
	 */
	public static PreparedStatement query_getWeeklyScore() throws Exception {
		
		if (getWeeklyScore != null) return getWeeklyScore;
		
		getWeeklyScore = connection.prepareStatement(
				"SELECT AVG(ContentTag.severity) AS \"score\"\r\n"
				+ "FROM Route\r\n"
				+ "	JOIN RouteStops ON (RouteStops.routeNumber = Route.number)\r\n"
				+ "	JOIN Stop ON (Stop.ID = RouteStops.stopID)\r\n"
				+ "	JOIN Incident ON (Incident.stopID = Stop.ID)\r\n"
				+ "	JOIN IncidentTags ON (IncidentTags.incidentID = Incident.ID)\r\n"
				+ "	JOIN ContentTag ON (ContentTag.ID = IncidentTags.tagID)\r\n"
				+ "WHERE Route.number = ?\r\n"
				+ "AND (now() - Incident.createdAt) < '7day';");
		
		return getWeeklyScore;
		
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
	
	/*
	 * deletes all tags for an incident
	 * 
	 * PARAMS:
	 * 1 - incidentUUID (string)
	 */
	public static PreparedStatement query_deleteIncidentTags() throws Exception {
		
		if (deleteIncidentTags != null) return deleteIncidentTags;
		
		deleteIncidentTags = connection.prepareStatement(
				"DELETE FROM IncidentTags\r\n"
				+ "WHERE incidentID = (SELECT id FROM Incident WHERE uuid ILIKE ?);");
		
		return deleteIncidentTags;
		
	}
	
	/*
	 * adds a tag to an incident
	 * 
	 * PARAMS:
	 * 1 - incidentUUID (string)
	 * 2 - tag (string)
	 */
	public static PreparedStatement query_addIncidentTag() throws Exception {
		
		if (addIncidentTag != null) return addIncidentTag;
		
		addIncidentTag = connection.prepareStatement(
				"INSERT INTO IncidentTags (incidentid, tagid)\r\n"
				+ "VALUES ((SELECT id FROM Incident WHERE uuid ILIKE ?), (SELECT id FROM contenttag WHERE name ILIKE ?));");
		
		return addIncidentTag;
		
	}
	
	/*
	 * change the description of an incident
	 * 
	 * PARAMS:
	 * 1 - description (string)
	 * 2 - incidentUUID (string)
	 */
	public static PreparedStatement query_setIncidentDescription() throws Exception {
		
		if (setIncidentDescription != null) return setIncidentDescription;
		
		setIncidentDescription = connection.prepareStatement(
				"UPDATE Incident\r\n"
				+ "SET description = ?\r\n"
				+ "WHERE uuid ILIKE ?;");
		
		return setIncidentDescription;
		
	}
	
	public static PreparedStatement query_getAllArrivals() throws Exception {
		getAllArrivals = connection.prepareStatement(
				"SELECT routeStops.routeNumber, locationName AS \"Stop Location\", bus.name AS bus, time AS arrivalTime, day\r\n"
				+ "FROM route\r\n"
				+ "	JOIN bus ON (bus.routeNumber = route.number)\r\n"
				+ "	JOIN routeStops ON (route.number = routeStops.routeNumber)\r\n"
				+ "	JOIN arrivalTime ON (arrivalTime.routeStopID = routeStops.routestopID)\r\n"
				+ "	JOIN weekday ON (weekday.ID = arrivalTime.weekdayID)\r\n"
				+ "	JOIN stop ON (stop.ID = routeStops.stopID)\r\n"
				+ "ORDER BY routeNumber, day, arrivalTime;");

		return getAllArrivals;
	}
}