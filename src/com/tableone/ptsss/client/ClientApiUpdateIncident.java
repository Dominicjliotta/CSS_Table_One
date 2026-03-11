package com.tableone.ptsss.client;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

import com.tableone.ptsss.server.ServerApiUpdateIncident;

public class ClientApiUpdateIncident extends ClientApi {

	private String uuid;
	private String description;
	private String[] tags;
	
	@Override
	protected String getName() {
		return "updateIncident(incidentUUID, newDescription?, newTags[]?)";
	}

	@Override
	protected void parseRequest(Scanner scanner) throws Exception {
		
		System.out.print("Enter the UUID for the incident to update: ");
		this.uuid = scanner.nextLine();
		
		if (!isValidUUID(this.uuid)) throw new Exception("Invalid UUID!");
		
		System.out.print("Update description? (Y/N): ");
		String yn = scanner.nextLine().toLowerCase();
		while (!yn.equals("y") && !yn.equals("n")) {
			System.out.println("Invalid input. Please type Y or N.");
			System.out.print("Update description? (Y/N): ");
			yn = scanner.nextLine().toLowerCase();
		}
		
		if (yn.equals("y")) {
			System.out.print("New description: ");
			this.description = scanner.nextLine();
		}
		
		System.out.print("Update tags? (Y/N): ");
		yn = scanner.nextLine().toLowerCase();
		while (!yn.equals("y") && !yn.equals("n")) {
			System.out.println("Invalid input. Please type Y or N.");
			System.out.print("Update tags? (Y/N): ");
			yn = scanner.nextLine().toLowerCase();
		}
		
		if (yn.equals("n")) return;
		
		List<String> tagList = new ArrayList<>();
		
		System.out.print("Tag name: ");
		
		String input = scanner.nextLine();
		while (!input.equalsIgnoreCase("q")) {
			tagList.add(input);
			System.out.print("Tag name (or type 'q' to finish): ");
			input = scanner.nextLine();
		}
		
		this.tags = tagList.toArray(new String[tagList.size()]);
		
	}
	
	private static boolean isValidUUID(String s) {
		
		if (s.length() != 36) return false;
		
		try {
			UUID.fromString(s);
		} catch (Exception e) {
			return false;
		}
		
		return true;
		
	}

	@Override
	protected void performCall() throws Exception {
		
		ServerApiUpdateIncident serverApi = new ServerApiUpdateIncident();
		boolean success = serverApi.call(this.uuid, this.description, this.tags);
		
		String output = success ? ("Updated incident " + this.uuid) : "Could not update incident";
		
		printOutput(output);
		
	}

}
