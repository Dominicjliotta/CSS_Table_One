package com.tableone.ptsss.client;

import java.util.Scanner;
import com.tableone.ptsss.server.ServerApiGetRouteInfo;


public class ClientApiGetRouteInfo extends ClientApi {

    private String routeNumber;

    @Override
    protected String getName() {
        return "getRouteInfo(routeNumber)";
    }

    @Override
    protected void parseRequest(Scanner scanner) throws Exception {

        //ask the user for a route Number
        System.out.print("Enter Route Number: ");
        this.routeNumber = scanner.nextLine().trim();

        //reject any nonexsistant route numbers
        PreparedStatement ps = ServerDriver.query_routeExists();
        ps.setString(1, this.routeNumber);
        ResultSet rs = ps.executeQuery();
        if (!rs.next()) throw new Exception("Invalid Route Number!");

    }


    @Override
    protected void performCall() throws Exception {

        //make the call to the server and get the output
        ServerApiGetRouteInfo serverApi = new ServerAPIGetRouteInfo();
        String output = serverApi.call(this.routeNumber);

        //output the message
        printOutput(output);

    }

}