package com.tableone.ptsss.client;
import java.util.Scanner;

import com.tableone.ptsss.server.ServerApiGetBuses;

public class ClientApiGetBuses extends ClientApi {

	/*-----------------------------------------------*/
	/* ClientApiGetBuses                             */
	/* Author: Dom                                   */
	/*                                               */
	/* getBuses(routeNumber)                         */
	/* Returns a list of bus names on a given route. */
	/*-----------------------------------------------*/
	
    private String routeNumber;

    @Override
    protected String getName() {
        return "getBuses(routeNumber)";
    }

    @Override
    protected void parseRequest(Scanner scanner) throws Exception {
    	
    	//prompt the user for a route number
        System.out.print("Route number: ");
        this.routeNumber = scanner.nextLine().trim();
        
        if (this.routeNumber.isEmpty()) throw new Exception("Route number cannot be blank!");
        
    }

    @Override
    protected void performCall() throws Exception {
    	
    	//make the call to the server and get the output
        ServerApiGetBuses serverApi = new ServerApiGetBuses();
        String output = serverApi.call(this.routeNumber);
        
        //output the message
        printOutput(output);
        
    }
    
}
