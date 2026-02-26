package com.tableone.ptsss.client;

import java.util.Scanner;

public class ClientDriver {

	public static void inputLoop() {
		
		System.out.println("Welcome to the Public Transportation Safety Scoring System (PTSSS)!");
		
		Scanner scanner = new Scanner(System.in);
		
		while (true) {
			
			System.out.print("\nPress enter to continue...");
			scanner.nextLine();
			
			System.out.println("\n\n\nAvailable actions:");
			System.out.println("l: List available API calls");
			System.out.println("## <params>: Perform an API call");
			System.out.println("q: Quit");
			System.out.print("\n> ");
			
			String input = scanner.nextLine();
			
			if (input.equalsIgnoreCase("q")) break;
			if (input.equalsIgnoreCase("l")) {
				printApiList();
				continue;
			}
			
			//if the user types anything other than "q" or "l",
			//attempt to parse their input as an api call attempt
			try {
				
				handleApiCall(scanner, input);
				
			//upon any error, try to print a helpful error message for the user,
			//and do not terminate the program
			} catch (Exception e) {
				
				if (e.getMessage() == null) {
					System.out.println("An unhandled fatal error occurred while performing an API call!");
					e.printStackTrace();
					continue;
				}
				
				System.out.println("[ERROR] " + e.getMessage());
				
			}
			
		}
		
		scanner.close();
		
	}
	
	private static void printApiList() {
		
		System.out.println("\nAll avaliable API calls:\n");
		
		System.out.println("pretend theres an actual list here");
		System.out.println("0 - example(name, age)");
		System.out.println("1 - call1()");
		System.out.println("2 - call2()");
		System.out.println("etc...");
		
		System.out.println("\nUsage: ## <params>");
		
	}
	
	//try to parse the user input as an api call and handle it accordingly
	//each api call should handle its own exceptions,
	//throwing an exception with an appropriate error message
	private static void handleApiCall(Scanner scanner, String input) throws Exception {
		
		if (input.isEmpty()) throw new Exception("No action provided!");
		
		String[] args = input.split(" ");
		
		switch (args[0]) {
		case "0":
			//EXAMPLE API IMPLEMENTATION!
			//FOR TEAM'S REFERENCE, REMOVE BEFORE SUBMISSION
			new ClientApiExample(scanner, args);
			return;
		case "1":
			//run call1
			return;
		case "2":
			//run call2
			return;
		}
		
		throw new Exception("Unknown action: \"" + args[0] + "\"");
		
	}
	
}