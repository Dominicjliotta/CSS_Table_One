package com.tableone.ptsss.client;

import java.util.Scanner;

import com.tableone.ptsss.server.ServerApiCreateTag;

public class ClientApiCreateTag extends ClientApi {

    private String name;
    private int severity;

    @Override
    protected String getName() {
        return "createTag(name, severity)";
    }

    @Override
    protected void parseRequest(Scanner scanner) throws Exception {

        //ask the user for a uuid
        System.out.print("Enter the name of your tag: ");
        this.name = scanner.nextLine();

        //check for blank
        if (this.name != null && this.name.isEmpty()) throw new Exception("Name cannot be blank!");


        //ask the user if they want to update the description
        System.out.print("What would you rate the severity out of 10: ");
        String severityString = scanner.nextLine();
        if (severityString != null && severityString.isEmpty())
            throw new Exception("Severity cannot be blank!");

        try {
            this.severity = Integer.parseInt(severityString);
        } catch (Exception e) {
            //replace java's exception with a more user-friendly error message
            throw new Exception("Invalid type for severity: \"" + severityString + "\"");
        }

        if (this.severity < 1 || this.severity > 10)
            throw new Exception("Severity needs to be between 1-10");

    }

    @Override
    protected void performCall() throws Exception {

        //make the call to the server and get the output
        ServerApiCreateTag serverApi = new ServerApiCreateTag();
        boolean success = serverApi.call(this.name, this.severity);

        //server output is true or false, use that to make a simple friendly message
        String output = success ? ("Created Tag " + this.name + " with severity " + this.severity) : "Could not add tag";

        //output the message
        printOutput(output);

    }

}