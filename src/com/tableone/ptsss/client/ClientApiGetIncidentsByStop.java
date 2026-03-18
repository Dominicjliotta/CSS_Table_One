package com.tableone.ptsss.client;

import java.util.Scanner;

import com.tableone.ptsss.server.ServerApiGetIncidentsByStop;

/*--------------------------------------------------------------------*/
/* ClientApiGetIncidentsByStop                                        */
/* Author: Lily                                                       */
/*                                                                    */
/* getIncidentsByStop(locationName)                                   */
/* Returns a list of incident information based on the stop location. */
/*--------------------------------------------------------------------*/

public class ClientApiGetIncidentsByStop extends ClientApi {

    private String locationName;

    @Override
    protected String getName() {
        return "getIncidentsByStop(locationName)";
    }

    @Override
    protected void parseRequest(Scanner scanner) throws Exception {

    	//prompt the user for a stop location
        System.out.print("Stop location: ");
        this.locationName = scanner.nextLine().trim();

        if (this.locationName.isEmpty()) throw new Exception("Stop location cannot be empty!");
        
    }

    @Override
    protected void performCall() throws Exception {
    	
        ServerApiGetIncidentsByStop serverApi = new ServerApiGetIncidentsByStop();
        String output = serverApi.call(this.locationName);
        
        printOutput(output);
        
    }
    
}