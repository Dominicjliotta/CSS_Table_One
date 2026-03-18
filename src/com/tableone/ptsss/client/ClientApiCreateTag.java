package com.tableone.ptsss.client;

import java.util.Scanner;

import com.tableone.ptsss.server.ServerApiCreateTag;

/*--------------------------------------------------------------------------------*/
/* ClientApiCreateTag                                                             */
/* Author: Melissa                                                                */
/*                                                                                */
/* createTag(name, severity)                                                      */
/* Creates a new incident content tag. Returns true on success, false on failure. */
/*--------------------------------------------------------------------------------*/

public class ClientApiCreateTag extends ClientApi {

    private String name;
    private int severity;

    @Override
    protected String getName() {
        return "createTag(name, severity)";
    }

    @Override
    protected void parseRequest(Scanner scanner) throws Exception {

        //ask the user for a name
        System.out.print("Enter the name of your tag: ");
        this.name = scanner.nextLine().trim();

        //check for blank
        if (this.name.isEmpty()) throw new Exception("Name cannot be blank!");

        //ask the user for a severity rating
        System.out.print("Enter a severity rating from 1 to 10: ");
        String severityString = scanner.nextLine().trim();

        //parse the severity
        try {
            this.severity = Integer.parseInt(severityString);
        } catch (Exception e) {
            throw new Exception("Invalid input for severity: \"" + severityString + "\"");
        }

        //bounds check for severity
        if (this.severity < 1 || this.severity > 10) throw new Exception("Severity must be between 1 and 10");

    }

    @Override
    protected void performCall() throws Exception {
    	
        //make the call to the server and get the output
        ServerApiCreateTag serverApi = new ServerApiCreateTag();
        boolean success = serverApi.call(this.name, this.severity);

        //server output is true or false, use that to make a simple friendly message
        String output = success ? ("Created Tag \"" + this.name + "\" with severity " + this.severity) : "Could not add tag";

        //output the message
        printOutput(output);

    }

}