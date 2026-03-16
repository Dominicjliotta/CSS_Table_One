package com.tableone.ptsss.client;

import java.util.Scanner;

public class ClientDriver {

	public static void inputLoop() {

		System.out.println("Welcome to the Public Transportation Safety Scoring System (PTSSS)!");

		Scanner scanner = new Scanner(System.in);

		while (true) {

			System.out.print("\nPress enter to continue...");
			scanner.nextLine();

			System.out.println("\n\n\nAvailable actions:\n");
			System.out.println("##: Perform an API call");
			System.out.println("l: List available API calls");
			System.out.println("q: Quit");
			System.out.print("\n> ");

			String input = scanner.nextLine();

			if (input.equalsIgnoreCase("q"))
				break;
			if (input.equalsIgnoreCase("l")) {
				printApiList();
				continue;
			}

			// if the user types anything other than "q" or "l",
			// attempt to parse their input as an api call
			try {

				handleApiCall(scanner, input);

				// upon any error, try to print a helpful error message for the user,
				// and do not terminate the program
			} catch (Exception e) {

				if (e.getMessage() == null) {
					System.out.println("An unhandled error occurred while performing an API call!");
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
		System.out.println("0 - exampleApi(name, age)");
		System.out.println("1 - getSortedStopList(destination, weekDay)");
		System.out.println("2 - updateIncident(incidentUUID, newDescription, newTags[])");
		System.out.println("3 - getAllArrivals()");
		System.out.println("4 - getBuses(routeNumber)");
		System.out.println("5 - getRouteInfo(routeNumber)");
		System.out.println("6 - createTag(name, severity)");
		System.out.println("7 - getRecentTags(routeNumber");
		System.out.println("8 - submitIncident(stopLocation, timestamp, description, tags[])");
		System.out.println("9 - getIncidents(routeNumber)");
		System.out.println("10 - getContentTags()");

		System.out.println("etc...");

	}

	// try to parse the user input as an api call and handle it accordingly
	// each api call should handle its own exceptions,
	// throwing an exception with an appropriate error message
	private static void handleApiCall(Scanner scanner, String input) throws Exception {

		if (input.isEmpty())
			throw new Exception("No action provided!");

		switch (input) {
			case "0":
				// EXAMPLE API IMPLEMENTATION!
				// FOR TEAM'S REFERENCE, REMOVE BEFORE SUBMISSION
				new ClientApiExample().execute(scanner);
				return;
			case "1":
				new ClientApiGetSortedStopList().execute(scanner);
				return;
			case "2":
				new ClientApiUpdateIncident().execute(scanner);
				return;
			case "3":
				new ClientApiGetAllArrivals().execute(scanner);
				return;
			case "4":
				new ClientApiGetBuses().execute(scanner);
				return;
			case "5":
				new ClientApiGetRouteInfo().execute(scanner);
				return;
			case "6":
				new ClientApiCreateTag().execute(scanner);
				return;
			case "7":
				new ClientApiGetRecentTags().execute(scanner);
				return;
			case "8":
				new ClientApiSubmitIncident().execute(scanner);
				return;
			case "9":
				new ClientApiGetIncidents().execute(scanner);
				return;
			case "10":
				new ClientApiGetContentTags().execute(scanner);
				return;
		}

		throw new Exception("Unknown action: \"" + input + "\"");

	}

}