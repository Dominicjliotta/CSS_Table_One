package com.tableone.ptsss.client;

import java.util.Scanner;

import com.tableone.ptsss.server.ServerApiGetRouteStops;

public class ClientApiGetRouteStops extends ClientApi {

    private String routeNumber;

    @Override
    protected String getName() {
        return "getRouteStops()";
    }

    @Override
    protected void parseRequest(Scanner scanner) throws Exception {

        System.out.print("Route Number: ");
        this.routeNumber = scanner.nextLine();

        if (this.routeNumber.isEmpty()) {
            throw new Exception("Route number cannot be empty.");
        }
    }

    @Override
    protected void performCall() throws Exception {

        ServerApiGetRouteStops serverApi = new ServerApiGetRouteStops();
        String output = serverApi.call(this.routeNumber);
        printOutput(output);
    }
}