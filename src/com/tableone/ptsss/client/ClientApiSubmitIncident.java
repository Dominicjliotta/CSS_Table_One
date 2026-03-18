package com.tableone.ptsss.client;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Scanner;

import com.tableone.ptsss.server.ServerApiSubmitIncident;

/*------------------------------------------------------------------------------*/
/* ClientApiSubmitIncident                                                      */
/* Author: David                                                                */
/*                                                                              */
/* submitIncident(stopLocation, time, description, tags[])                      */
/* Submits an incident for a stop containing content tags, stop location,       */
/* description, and time of incident. Returns the UUID of the created incident. */
/*------------------------------------------------------------------------------*/

public class ClientApiSubmitIncident extends ClientApi {

    private String stopLocation;
    private Timestamp time;
    private String description;
    private String[] tags;

    @Override
    protected String getName() {
        return "submitIncident(stopLocation, time, description, tags[])";
    }
    
    @Override
    protected void parseRequest(Scanner scanner) throws Exception {

        //prompt the user for a stop location
        System.out.print("Stop Location: ");
        this.stopLocation = scanner.nextLine().trim();

        //check for blank
        if (this.stopLocation.isEmpty()) throw new Exception("Stop location cannot be blank!");

        //prompt the user for a time
        System.out.print("Time of incident (ex. MM/DD/yyyy HH:MM [AM|PM]): ");
        this.time = stringToTimestamp(scanner.nextLine());
        
        //prompt the user for a description
        System.out.print("Description: ");
        this.description = scanner.nextLine().trim();
        
        //check for blank
        if (this.description.isEmpty()) throw new Exception("Description cannot be blank!");

        //prompt the user for tags
        System.out.print("Tags (comma separated): ");
        String tagsInput = scanner.nextLine().trim();
        
        //check for blank
        if (tagsInput.isEmpty()) throw new Exception("At least one tag must be provided!");

        //convert comma-separated tags into array
        this.tags = tagsInput.split(",");

        if (this.tags.length == 0) throw new Exception("At least one tag must be provided!");
        
        //trim whitespace from tags
        for (int i = 0; i < this.tags.length; i++) {
        	
            this.tags[i] = this.tags[i].trim();
            
            //check for blank
            if (this.tags[i].isEmpty()) throw new Exception("Tags cannot be blank!");
            
        }
        
    }

    @Override
    protected void performCall() throws Exception {

        //call the server api
        ServerApiSubmitIncident serverApi = new ServerApiSubmitIncident();
        //the server returns a uuid that must be given to the user
        String uuid = serverApi.call(this.stopLocation, this.time, this.description, this.tags);
        
        //this api returns null on failure
        //print an error message if uuid is null
        //otherwise, print a success message with the uuid
        if (uuid == null) printOutput("Could not submit incident.");
        else printOutput("Incident submitted successfully!\nUUID: " + uuid);
        
    }

    //helper function to convert a properly formatted string into a sql timestamp object
    private static Timestamp stringToTimestamp(String s) throws Exception {
        
        String formatError = "Timestamp must be in the format: MM/DD/yyyy HH:MM [AM|PM]";
        
        s = s.toLowerCase().trim();
        
        //format checks
        String[] args = s.split(" ");
        if (args.length != 3) throw new Exception(formatError);
        
        String[] dateArgs = args[0].split("/");
        if (dateArgs.length != 3) throw new Exception(formatError);
        if (dateArgs[0].length() > 2) throw new Exception(formatError);
        if (dateArgs[1].length() > 2) throw new Exception(formatError);
        if (dateArgs[2].length() < 4) throw new Exception(formatError);
        
        String[] timeArgs = args[1].split(":");
        if (timeArgs.length != 2) throw new Exception(formatError);
        if (timeArgs[0].length() > 2) throw new Exception(formatError);
        if (timeArgs[1].length() != 2) throw new Exception(formatError);
        
        //parse day/month/year
        int day = 0;
        int month = 0;
        int year = 0;
        
        try {
            day = Integer.parseInt(dateArgs[1]);
            month = Integer.parseInt(dateArgs[0]);
            year = Integer.parseInt(dateArgs[2]);
        } catch (Exception e) {
            throw new Exception("Invalid values in MM/DD/yyyy");
        }
        
        //day/month bound checks
        if (day < 1 || day > 31) throw new Exception("Day must be between 1 and 31");
        if (month < 1 || month > 12) throw new Exception("Month must be between 1 and 12");
        
        //check if a valid am/pm was given
        String ampm = args[2];
        if (!ampm.equals("am") && !ampm.equals("pm")) throw new Exception("AM or PM must be specified for the time");
        
        //parse hour/minute
        int hour = 0;
        int minute = 0;
        
        try {
            hour = Integer.parseInt(timeArgs[0]);
            minute = Integer.parseInt(timeArgs[1]);
        } catch (Exception e) {
            throw new Exception("Invalid values in HH:MM");
        }
        
        //hour/minute bound checks
        if (hour < 1 || hour > 12) throw new Exception("Hour must be between 1 and 12");
        if (minute < 0 || minute > 59) throw new Exception("Minute must be between 0 and 59");
        
        //convert hour to 0-23 range
        if (hour == 12) hour = 0;
        if (ampm.equals("pm")) hour += 12;
        
        //convert everything to a LocalDateTime
        LocalDateTime dateTime = LocalDateTime.of(year, month, day, hour, minute, 0);
        //get the unix timestamp in milliseconds from the LocalDateTime
        long unixTimestamp = dateTime.atZone(ZoneId.systemDefault()).toEpochSecond() * 1000;
        
        //construct and return a new sql timestamp with the unix timestamp
        return new Timestamp(unixTimestamp);
        
    }
    
}