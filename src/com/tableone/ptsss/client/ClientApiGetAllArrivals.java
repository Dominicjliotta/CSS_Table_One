package com.tableone.ptsss.client;

import java.util.Scanner;

import com.tableone.ptsss.server.ServerApiGetAllArrivals;

public class ClientApiGetAllArrivals extends ClientApi {

    public void execute(Scanner scanner) throws Exception {
		
		System.out.println("\n--- " + this.getName() + " ---\n");
		
		this.parseRequest(scanner);
		this.performCall();

        continueFetching(scanner);
		
	}

    protected void continueFetching(Scanner scanner) {
        int buffer = 50; // number of results to return per call
        boolean continueLoop = true;
        while (continueLoop) {
            System.out.println("Next 50? (y/n)");
            String input = scanner.nextLine().trim().toLowerCase();
            if (input.equals("y")) {
                try {
                    String output = this.nextFifty(buffer);
                    if (output.contains("No more arrivals to display.")) {
                        break;
                    }
                    buffer += 50;
                } catch (Exception e) {
                    System.out.println("An error occurred while fetching the next 50 arrivals.");
                    e.printStackTrace();
                }
            } else if (input.equals("n")) {
                continueLoop = false;
                System.out.println("Returning to main menu...");
            } else {
                System.out.println("Invalid input. Please enter 'y' or 'n'.");
            }
        }       
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

    protected String nextFifty(int buffer) throws Exception {
        
        //make the call to the server and get the output
        ServerApiGetAllArrivals serverApi = new ServerApiGetAllArrivals();
        String output = serverApi.nextFifty(buffer);
        
        //print the output
        printOutput(output);
        return output;
    }
}