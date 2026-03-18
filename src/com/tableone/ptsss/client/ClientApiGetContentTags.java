package com.tableone.ptsss.client;

import java.util.Scanner;

import com.tableone.ptsss.server.ServerApiGetContentTags;

/*-------------------------------------------*/
/* ClientApiGetContentTags                   */
/* Author: David                             */
/*                                           */
/* getContentTags()                          */
/* Returns a list of available content tags. */
/*-------------------------------------------*/

public class ClientApiGetContentTags extends ClientApi {
	
	@Override
	protected String getName() {
		return "getContentTags()";
	}
	
	@Override
	protected void parseRequest(Scanner scanner) throws Exception {
		//no arguments to parse
	}
	
	@Override
	protected void performCall() throws Exception {

		//call the server api that retrieves recent tags
		ServerApiGetContentTags serverApi = new ServerApiGetContentTags();
		String output = serverApi.call();

		//print the returned recent tags
		printOutput(output);
		
	}
	
}