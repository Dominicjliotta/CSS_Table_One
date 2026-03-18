package com.tableone.ptsss.client;

import java.util.Scanner;

import com.tableone.ptsss.server.ServerApiGetWeeklyScore;

/*--------------------------------------------------------------------------------*/
/* ClientApiGetWeeklyScore                                                        */
/*                                                                                */
/* getWeeklyScore(routeNumber)                                                    */
/* Prompts the user for a route number and displays the weekly safety score       */
/* (last 7 days) for that route.                                                  */
/*--------------------------------------------------------------------------------*/

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

        if (this.routeNumber.isEmpty()) {
            throw new Exception("Route number cannot be blank!");
        }
    }

    @Override
    protected void performCall() throws Exception {

        ServerApiGetWeeklyScore serverApi = new ServerApiGetWeeklyScore();
        String output = serverApi.call(this.routeNumber);

        printOutput(output);
    }
}

