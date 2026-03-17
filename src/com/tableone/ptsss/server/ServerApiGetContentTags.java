package com.tableone.ptsss.server;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ServerApiGetContentTags extends ServerApi<String> {

	@Override
	protected void parseRequest(Object[] args) throws Exception {

		// no arguments needed
		if (args.length != 0) {
			throw new Exception("ServerApiGetIncidents received " + args.length + " arguments, expected 0");
		}
	}

	@Override
	protected String completeRequest() throws Exception {

		PreparedStatement tagPs = ServerDriver.query_getContentTags();
		ResultSet rs = tagPs.executeQuery();

		System.out.println("List of possible Content Tags:");
		System.out.println("================================");

		boolean isEmpty = true;

		while (rs.next()) {
			isEmpty = false;

			// get each tag from query
			String tagName = rs.getString("name");
			String tagSeverity = rs.getString("severity");
			out.append(tagName + " | Severity = " + tagSeverity);
		}

		// if the query returned nothing, say "NONE"
		if (isEmpty)
			out.append("\nNONE");

		// return the output string
		return out.toString();
	}

}