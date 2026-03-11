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
		
		//ask the user for a uuid
		System.out.print("Enter the UUID for the incident to update: ");
		this.uuid = scanner.nextLine();
		
		//reject any malformed uuid
		if (!isValidUUID(this.uuid)) throw new Exception("Invalid UUID!");
		
		//ask the user if they want to update the description
		System.out.print("Update description? (Y/N): ");
		String yn = scanner.nextLine().toLowerCase();
		while (!yn.equals("y") && !yn.equals("n")) {
			System.out.println("Invalid input. Please type Y or N.");
			System.out.print("Update description? (Y/N): ");
			yn = scanner.nextLine().toLowerCase();
		}
		
		//if they want to update the description...
		if (yn.equals("y")) {
			//prompt them for a new description
			System.out.print("New description: ");
			this.description = scanner.nextLine().trim();
		}
		
		//make sure the description isnt blank
		if (this.description != null && this.description.isEmpty()) throw new Exception("Description cnanot be blank!");
		
		//ask the user if they want to update the tags
		System.out.print("Update tags? (Y/N): ");
		yn = scanner.nextLine().toLowerCase();
		while (!yn.equals("y") && !yn.equals("n")) {
			System.out.println("Invalid input. Please type Y or N.");
			System.out.print("Update tags? (Y/N): ");
			yn = scanner.nextLine().toLowerCase();
		}
		
		//if they dont want to update the tags, we are done
		if (yn.equals("n")) return;
		
		//make a list to store new tags
		List<String> tagList = new ArrayList<>();
		
		//prompt them for a tag
		System.out.print("Tag name: ");
		String input = scanner.nextLine().trim();
		
		//continue prompting until they exit
		while (!input.equalsIgnoreCase("q")) {
			//make sure the tag isnt blank
			if (input.isEmpty()) throw new Exception("Tag cannot be blank!");
			tagList.add(input);
			System.out.print("Tag name (or type 'q' to finish): ");
			input = scanner.nextLine().trim();
		}
		
		//convert the tag list to a primitive array and save
		this.tags = tagList.toArray(new String[tagList.size()]);
		
	}
	
	//helper function, checks if a string is a valid uuid
	private static boolean isValidUUID(String s) {
		
		//uuid should always be 36 characters long
		if (s.length() != 36) return false;
		
		try {
			//try to use java's built-in string-to-uuid method
			UUID.fromString(s);
		} catch (Exception e) {
			//any error should mean that the string is not formatted correctly
			return false;
		}
		
		return true;
		
	}

	@Override
	protected void performCall() throws Exception {
		
		//make the call to the server and get the output
		ServerApiUpdateIncident serverApi = new ServerApiUpdateIncident();
		boolean success = serverApi.call(this.uuid, this.description, this.tags);
		
		//server output is true or false, use that to make a simple friendly message
		String output = success ? ("Updated incident " + this.uuid) : "Could not update incident";
		
		//output the message
		printOutput(output);
		
	}

}
