package com.tableone.ptsss.client;

//import java.sql.PreparedStatement;
import java.util.Scanner;

import com.tableone.ptsss.server.ServerApiGetContentTags;

/*
 * Client API for retrieving the list of incident tags
   getContentTags()
 */

public class ClientApiGetContentTags extends ClientApi {

	// Name of the API function shown to the user
	@Override
	protected String getName() {
		return "getContentTags()";
	}

	// Prompt the user for route number and validate it
	@Override
	protected void parseRequest(Scanner scanner) throws Exception {
		// no arguments to parse
	}

	// Call the server API after input parsing
	@Override
	protected void performCall() throws Exception {

		// Call the server API that retrieves recent tags
		ServerApiGetContentTags serverApi = new ServerApiGetContentTags();

		String output = serverApi.call();

		// Print the returned recent tags
		printOutput(output);
	}
}