package com.tableone.ptsss.server;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

/*-------------------------------------------*/
/* ServerApiGetContentTags                   */
/* Author: David                             */
/*                                           */
/* getContentTags()                          */
/* Returns a list of available content tags. */
/*-------------------------------------------*/

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

		//fetch the query for this api call
		PreparedStatement tagPs = ServerDriver.query_getContentTags();
		//execute the query
		ResultSet rs = tagPs.executeQuery();

		//build the output string
		StringBuilder out = new StringBuilder();
		
		out.append("List of possible Content Tags:\n");
		out.append("================================\n");
		
		//iterate through the list of tags
		boolean isEmpty = true;
		while (rs.next()) {
			
			isEmpty = false;
			
			String tagName = rs.getString("name");
			String tagSeverity = rs.getString("severity");
			out.append(tagName + " | Severity = " + tagSeverity);
			
		}

		//if the query returned nothing, say "NONE"
		if (isEmpty) out.append("NONE");
		
		return out.toString();
		
	}

}