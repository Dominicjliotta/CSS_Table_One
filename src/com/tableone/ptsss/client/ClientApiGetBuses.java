package com.tableone.ptsss.client;
import java.util.Scanner;

import com.tableone.ptsss.server.ServerApiGetBuses;

public class ClientApiGetBuses extends ClientApi {

    private String routeNumber;

    public void execute(Scanner scanner) throws Exception {
		
		System.out.println("\n--- " + this.getName() + " ---\n");
		
		this.parseRequest(scanner);
		this.performCall();

		
	}

    @Override
    protected String getName() {
        return "getBuses()";
    }

    @Override
    protected void parseRequest(Scanner scanner) throws Exception {
        System.out.print("Route number: ");
        String input = scanner.nextLine().trim();
        try {
            this.routeNumber = input; 
        } catch (NumberFormatException e) {
            throw new Exception("Invalid route number! Please enter a valid integer.");
        }
    }

    @Override
    protected void performCall() throws Exception {
        ServerApiGetBuses serverApi = new ServerApiGetBuses();
        String output = serverApi.call(this.routeNumber);
        System.out.println(output);
    }
    
}
