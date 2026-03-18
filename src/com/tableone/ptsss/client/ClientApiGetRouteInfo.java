package com.tableone.ptsss.client;

import java.util.Scanner;
import com.tableone.ptsss.server.ServerApiGetRouteInfo;

/*--------------------------------------------------------------------------------*/
/* ClientApiGetRouteInfo                                                          */
/* Author: Melissa                                                                */
/*                                                                                */
/* getRouteInfo(routeNumber)                                                      */
/* Returns route information including start and end locations, and current score */
/* calculated on the last 6 months.                                               */
/*--------------------------------------------------------------------------------*/

public class ClientApiGetRouteInfo extends ClientApi {

    private String routeNumber;

    @Override
    protected String getName() {
        return "getRouteInfo(routeNumber)";
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
        ServerApiGetRouteInfo serverApi = new ServerApiGetRouteInfo();
        String output = serverApi.call(this.routeNumber);

        //output the message
        printOutput(output);

    }

}