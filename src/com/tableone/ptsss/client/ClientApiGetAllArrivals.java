package com.tableone.ptsss.client;

import java.util.Scanner;

import com.tableone.ptsss.server.ServerApiGetAllArrivals;

/*------------------------------------------------------------------*/
/* ClientApiGetAllArrivals                                          */
/* Author: Dom                                                      */
/*                                                                  */
/* getAllArrivals()                                                 */
/* Returns a list of all arrival times for all stops on all routes. */
/*------------------------------------------------------------------*/

public class ClientApiGetAllArrivals extends ClientApi {

    @Override
    protected String getName() {
        return "getAllArrivals()";
    }

    @Override
    protected void parseRequest(Scanner scanner) throws Exception {
        //no arguments to parse for this api call
    }
	
    //overriding execute(), so that continueFetching() can be called with the scanner object
    //after performCall() is called. this is a special case for this API since it returns
    //a long list
    @Override
    public void execute(Scanner scanner) throws Exception {
    	
    	//do the regular execute logic
		super.execute(scanner);
		
		//then continue fetching
        this.continueFetching(scanner);
        
	}
    
    //continuously prompt the user to ask if they want to get the next 50 items in the list,
    //until there are no more items left
    protected void continueFetching(Scanner scanner) {
    	
        int buffer = 50; //current buffer, increases by 50 each iteration
        
        while (true) {
        	
            System.out.println("Get next 50? (y/n)");
            String input = scanner.nextLine().toLowerCase().trim();
            
            if (input.equals("y")) {
            	
                try {
                	
                	//print the next 50 items, and get the result as (output)
                    String output = this.nextFifty(buffer);
                    
                    //break out of the loop if we have reached the end
                    if (output.contains("No more arrivals to display.")) break;
                    
                    //increment the buffer by 50
                    buffer += 50;
                    
                } catch (Exception e) {
                    System.out.println("Could not fetch the next 50 arrivals.");
                }
                
            } else if (input.equals("n")) break;
            else System.out.println("Invalid input. Please enter 'y' or 'n'.");
            
        }
        
    }
    
    @Override
    protected void performCall() throws Exception {
    	//get the first 50 items from the list
        this.nextFifty(0);
    }

    //gets 50 items from the long list, starting from a given index (buffer)
    protected String nextFifty(int buffer) throws Exception {
        
        //make the call to the server and get the output
        ServerApiGetAllArrivals serverApi = new ServerApiGetAllArrivals();
        String output = serverApi.nextFifty(buffer);
        
        //print and return the output
        printOutput(output);
        return output;
        
    }
    
}