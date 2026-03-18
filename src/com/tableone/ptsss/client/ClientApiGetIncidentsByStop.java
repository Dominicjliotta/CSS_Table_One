package com.tableone.ptsss.client;

import java.util.Scanner;

import com.tableone.ptsss.server.ServerApiGetIncidentsByStop;

public class ClientApiGetIncidentsByStop extends ClientApi {

    private String locationName;

    @Override
    protected String getName() {
        return "getIncidentsByStop()";
    }

    @Override
    protected void parseRequest(Scanner scanner) throws Exception {

        System.out.print("Stop Location: ");
        this.locationName = scanner.nextLine();

        if (this.locationName.isEmpty()) {
            throw new Exception("Stop location cannot be empty.");
        }
    }

    @Override
    protected void performCall() throws Exception {

        ServerApiGetIncidentsByStop serverApi = new ServerApiGetIncidentsByStop();
        String output = serverApi.call(this.locationName);
        printOutput(output);
    }
}