package com.tableone.ptsss.client;

import java.util.Scanner;

import com.tableone.ptsss.server.ServerApiGetRouteStops;

/*--------------------------------------------------------------------------*/
/* ClientApiGetRouteStops                                                   */
/* Author: Lily                                                             */
/*                                                                          */
/* getRouteInfo(routeNumber)                                                */
/* Returns a list of stops on a route including location and arrival times. */
/*--------------------------------------------------------------------------*/

public class ClientApiGetRouteStops extends ClientApi {

    private String routeNumber;

    @Override
    protected String getName() {
        return "getRouteStops(routeNumber)";
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

        ServerApiGetRouteStops serverApi = new ServerApiGetRouteStops();
        String output = serverApi.call(this.routeNumber);

        printOutput(output);
        
    }

}