package com.tableone.ptsss.client;

import java.util.Scanner;

import com.tableone.ptsss.server.ServerApiGetAllArrivals;

public class ClientApiGetAllArrivals extends ClientApi {

    public void execute(Scanner scanner) throws Exception {
		
		System.out.println("\n--- " + this.getName() + " ---\n");
		
		this.parseRequest(scanner);
		this.performCall();
		
	}
    
    @Override
    protected String getName() {
        return "getAllArrivals()";
    }

    @Override
    protected void parseRequest(Scanner scanner) throws Exception {
        //no arguments to parse for this api call
    }

    @Override
    protected void performCall() throws Exception {
        
        //make the call to the server and get the output
        ServerApiGetAllArrivals serverApi = new ServerApiGetAllArrivals();
        String output = serverApi.call();
        
        //print the output
        printOutput(output);
    }
    
}