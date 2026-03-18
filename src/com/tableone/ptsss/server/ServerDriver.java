package com.tableone.ptsss.server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.Properties;

public class ServerDriver {

	private static Connection connection;
	private static final String DB_URL = "jdbc:postgresql://ptsss-server.postgres.database.azure.com:5432/ptsssdb";
	private static final String PASSWORD = "Tableone475";

	/* global prepared query objects */
	private static PreparedStatement getSemiYearlyScore;
	private static PreparedStatement getWeeklyScore;

	private static PreparedStatement getSortedStopList;
	private static PreparedStatement deleteIncidentTags;
	private static PreparedStatement setIncidentDescription;
	private static PreparedStatement addIncidentTag;
	private static PreparedStatement getAllArrivals;
	private static PreparedStatement getBuses;
	private static PreparedStatement getRecentTags;
	private static PreparedStatement addContentTag;
	private static PreparedStatement getRouteInfo;
	private static PreparedStatement routeExists;
	private static PreparedStatement contentTagExists;
	private static PreparedStatement submitIncident;
	private static PreparedStatement getIncidents;
	private static PreparedStatement getContentTags;
	private static PreparedStatement getRouteStops;

	// connects to the database, sets up prepared statements, etc.
	public static boolean setup() {

		// connect to the database
		Properties connectionProps = new Properties();
		connectionProps.put("sslmode", "require");
		connectionProps.put("user", "dbadmin");
		connectionProps.put("password", PASSWORD);

		// save the connection to a global object
		try {
			connection = DriverManager.getConnection(DB_URL, connectionProps);
		} catch (Exception e) {
			System.out.println("Failed to connect to the database!");
			e.printStackTrace();
			return false;
		}

		// prepare all global queries
		try {
			prepareQueries();
		} catch (Exception e) {
			System.out.println("Failed to prepare queries!");
			e.printStackTrace();
			return false;
		}

		return true;

	}

	// close resources
	public static void tearDown() {

		try {
			connection.close();
		} catch (Exception e) {
			System.out.println("Error closing the database connection!");
			e.printStackTrace();
		}

	}

	// get the database connection object
	public static Connection getConnection() {
		return connection;
	}

	// begin a transaction
	public static void beginTX() throws Exception {
		if (connection.getAutoCommit() == false)
			return;
		connection.setAutoCommit(false);
	}

	// commit a transaction
	public static void commitTX() throws Exception {
		if (connection.getAutoCommit() == true)
			return;
		connection.commit();
		connection.setAutoCommit(true);
	}

	// runs all PreparedStatement functions once to initialize them
	private static void prepareQueries() throws Exception {

		query_getSemiYearlyScore();
		query_getWeeklyScore();
		query_getSortedStopList();
		query_deleteIncidentTags();
		query_addIncidentTag();
		query_setIncidentDescription();
		query_getAllArrivals();
		query_getBuses();
		query_getRouteStops();
	}
	
	/*===================================================*/
	/* /!\ ALL PREPARED STATEMENTS ARE DEFINED BELOW /!\ */
	/*===================================================*/

	/*
	 * query for getting a route's semi-yearly score (past 6 months)
	 *
	 * PARAMS:
	 * 1 - routeNumber (string)
	 */
	public static PreparedStatement query_getSemiYearlyScore() throws Exception {

		if (getSemiYearlyScore != null)
			return getSemiYearlyScore;

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

		if (getWeeklyScore != null)
			return getWeeklyScore;

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

		if (getSortedStopList != null)
			return getSortedStopList;

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

		if (deleteIncidentTags != null)
			return deleteIncidentTags;

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

		if (addIncidentTag != null)
			return addIncidentTag;

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

		if (setIncidentDescription != null)
			return setIncidentDescription;

		setIncidentDescription = connection.prepareStatement(
				"UPDATE Incident\r\n"
						+ "SET description = ?\r\n"
						+ "WHERE uuid ILIKE ?;");

		return setIncidentDescription;

	}

	/*
	 * gets all arrivals at all stops, sorted by route number, day, and time
	 *
	 * PARAMS: none
	 */
	public static PreparedStatement query_getAllArrivals() throws Exception {

		if (getAllArrivals != null)
			return getAllArrivals;

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

	/*
	 * gets all buses for a specific route number
	 *
	 * PARAMS:
	 * 1 - routeNumber (string)
	 */
	public static PreparedStatement query_getBuses() throws Exception {

		if (getBuses != null)
			return getBuses;

		getBuses = connection.prepareStatement(
				"SELECT ID, name FROM Bus WHERE routeNumber = ?;");

		return getBuses;

	}

	/*
	 * lists all tags from incidents from the last 6 month
	 * 
	 * PARAMS:
	 * 1 - routeNumber (string)
	 */
	public static PreparedStatement query_getRecentTags() throws Exception {

		if (getRecentTags != null)
			return getRecentTags;

		getRecentTags = connection.prepareStatement(
				"SELECT name\r\n"
						+ "FROM contentTag\r\n"
						+ "\tJOIN IncidentTags ON (contentTag.id = IncidentTags.tagID)\r\n"
						+ "\tJOIN Incident ON (IncidentTags.incidentID = Incident.ID)\r\n"
						+ "\tJOIN Stop ON (Incident.stopID = Stop.ID)\r\n"
						+ "\tJOIN RouteStops ON (Stop.ID = RouteStops.RouteStopId)\r\n"
						+ "WHERE RouteStops.routeNumber ILIKE ? "
						+ "AND Incident.incidentTime >= CURRENT_DATE - INTERVAL '6 months';");

		return getRecentTags;

	}

	/*
	 * add new incident content tag
	 *
	 * PARAMS:
	 * 1 - name (string)
	 * 2 - severity (int)
	 */
	public static PreparedStatement query_addContentTag() throws Exception {

		if (addContentTag != null)
			return addContentTag;

		addContentTag = connection.prepareStatement(
				"INSERT INTO ContentTag (name, severity)\r\n"
						+ "VALUES (?, ?);");

		return addContentTag;

	}

	/*
	 * get route info
	 *
	 * PARAMS:
	 * 1 - routeNumber (string)
	 */
	public static PreparedStatement query_getRouteInfo() throws Exception {

		if (getRouteInfo != null)
			return getRouteInfo;

		getRouteInfo = connection.prepareStatement(
				"SELECT startPoint AS \"Start Location\", endPoint AS \"End Location\" \r\n"
						+ "FROM Route\r\n"
						+ "WHERE number = ?;"

		);

		return getRouteInfo;

	}

	/*
	 * does route number exist
	 *
	 * PARAMS:
	 * 1 - routeNumber (string)
	 */

	public static PreparedStatement query_routeExists() throws Exception {
		if (routeExists != null)
			return routeExists;

		routeExists = connection.prepareStatement(
				"SELECT 1 FROM Route WHERE number ILIKE ?;");
		return routeExists;
	}

	/*
	 * does content tag exist
	 *
	 * PARAMS:
	 * 1 - name (string)
	 */

	public static PreparedStatement query_contentTagExists() throws Exception {
		if (contentTagExists != null)
			return contentTagExists;

		contentTagExists = connection.prepareStatement(
				"SELECT 1 FROM ContentTag WHERE name ILIKE ?;");
		return contentTagExists;
	}

	/*
	 * submit incident
	 *
	 * PARAMS:
	 * 1 - stopLocation
	 * 2 - timestamp
	 * 3 - description
	 * 4 - tags[]
	 * 5 - UUID
	 */

	// TO-DO add support for adding multiple tags to an incident
	public static PreparedStatement query_submitIncident() throws Exception {
		if (submitIncident != null)
		return submitIncident;

		submitIncident = connection.prepareStatement(
			"INSERT INTO incident (stopid, incidenttime, description, createdat, uuid) " +
			"VALUES " + 
			"((SELECT id FROM stop WHERE locationname ILIKE ? LIMIT 1), " + // stopid
			"?, " + // time
			"?,  " + // description
			"NOW(), " + // createdAt
			"?, " + // adding UUID
			" RETURNING ?;" + //  returning uuid
			""
		);

		return submitIncident;
	}

	/*
	 * getIncidents
	 *
	 * PARAMS:
	 * 1 - routeNumber
	 */

	public static PreparedStatement query_getIncidents() throws Exception {
		if (getIncidents != null)
			return getIncidents;

		getIncidents = connection.prepareStatement(
    		"SELECT " +
    			"s.locationname AS location, " +
    			"ct.name AS tag, " +
    			"i.incidenttime AS time " +
    		"FROM route r " +
    			"JOIN routestops rs ON r.number = rs.routenumber " +
    			"JOIN stop s ON rs.stopid = s.id " +
    			"JOIN incident i ON s.id = i.stopid " +
    			"JOIN incidenttags it ON i.id = it.incidentid " +
    			"JOIN contenttag ct ON it.tagid = ct.id " +
    		"WHERE r.number = ? " +
    		"ORDER BY i.incidenttime DESC;"
		);

		return getIncidents;
	}

	/*
	 * getContentTags
	 *
	 * PARAMS:
	 * none
	 */
	public static PreparedStatement query_getContentTags() throws Exception {
		if (getContentTags != null)
			return getContentTags;

		getContentTags = connection.prepareStatement(
    		"SELECT name, severity AS Severity FROM contenttag ORDER BY name;"
		);

		return getContentTags;
	}
	
	/*
	 * gets all stops given a route number
	 *
	 * PARAMS:
	 * 1 - routeNumber (string)
	 */
	public static PreparedStatement query_getRouteStops() throws Exception {

		if (getRouteStops != null) return getRouteStops;

		getRouteStops = connection.prepareStatement(
			"SELECT locationName\r\n"
			+ "FROM Route\r\n"
			+ "	JOIN RouteStops ON (RouteStops.routeNumber = Route.number)\r\n"
			+ "	JOIN Stop ON (Stop.ID = RouteStops.stopID)\r\n"
			+ "WHERE Route.number = ?;"
		);

		return getRouteStops;

	}

}
