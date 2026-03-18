package com.tableone.ptsss.client;

import java.util.Scanner;

import com.tableone.ptsss.server.ServerApiGetWeeklyScore;

/*-----------------------------------------------------------------*/
/* ClientApiGetWeeklyScore                                         */
/* Author: Lily                                                    */
/*                                                                 */
/* getWeeklyScore(routeNumber)                                     */
/* Returns the weekly score of a given route from the last 7 days. */
/*-----------------------------------------------------------------*/

public class ClientApiGetWeeklyScore extends ClientApi {

    private String routeNumber;

    @Override
    protected String getName() {
        return "getWeeklyScore(routeNumber)";
    }

    @Override
    protected void parseRequest(Scanner scanner) throws Exception {

        System.out.print("Route number: ");
        this.routeNumber = scanner.nextLine().trim();

        if (this.routeNumber.isEmpty()) throw new Exception("Route number cannot be blank!");
        
    }

    @Override
    protected void performCall() throws Exception {

        ServerApiGetWeeklyScore serverApi = new ServerApiGetWeeklyScore();
        String output = serverApi.call(this.routeNumber);

        printOutput(output);
        
    }
    
}

