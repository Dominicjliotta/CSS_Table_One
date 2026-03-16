package com.tableone.ptsss.client;

import java.util.Scanner;

import com.tableone.ptsss.server.ServerApiSubmitIncident;

/*
 * Client API for submitting a transit incident.
 * submitIncident(stopLocation, time, description, tags[])
*/

public class ClientApiSubmitIncident extends ClientApi {

    private String stopLocation;
    private String time;
    private String description;
    private String[] tags;

    @Override
    protected String getName() {
        return "submitIncident(stopLocation, time, description, tags[])";
    }

    @Override
    protected void parseRequest(Scanner scanner) throws Exception {

        // stopLocation validation
        // runs query to check if location exists (possibly)
        System.out.print("Stop Location: ");
        this.stopLocation = scanner.nextLine();

        if (this.stopLocation.isEmpty()) {
            throw new Exception("Stop location cannot be blank!");
        }

        // TIME INPUT (year, month, day, time)
        System.out.print("Year (YYYY): ");
        String yearStr = scanner.nextLine();

        System.out.print("Month (1-12): ");
        String monthStr = scanner.nextLine();

        System.out.print("Day (1-31): ");
        String dayStr = scanner.nextLine();

        System.out.print("Time (HHMM military): ");
        String timeStr = scanner.nextLine();

        int year;
        int month;
        int day;

        try {
            year = Integer.parseInt(yearStr);
            month = Integer.parseInt(monthStr);
            day = Integer.parseInt(dayStr);
        } catch (Exception e) {
            throw new Exception("Date values must be valid numbers.");
        }

        // Basic bounds checks
        if (month < 1 || month > 12)
            throw new Exception("Month must be between 1 and 12.");

        if (day < 1 || day > 31)
            throw new Exception("Day must be between 1 and 31.");

        // Validate time format
        if (!timeStr.matches("\\d{4}"))
            throw new Exception("Time must be in HHMM military format (example: 1830).");

        int hour = Integer.parseInt(timeStr.substring(0, 2));
        int minute = Integer.parseInt(timeStr.substring(2, 4));

        if (hour < 0 || hour > 23)
            throw new Exception("Hour must be between 00 and 23.");

        if (minute < 0 || minute > 59)
            throw new Exception("Minutes must be between 00 and 59.");

        // Build final timestamp string
        this.time = String.format("%04d-%02d-%02d %02d:%02d",
                year, month, day, hour, minute);

        // DESCRIPTION
        System.out.print("Description: ");
        this.description = scanner.nextLine();

        if (this.description.isEmpty()) {
            throw new Exception("Description cannot be blank!");
        }

        // TAGS
        System.out.print("Tags (comma separated): ");
        String tagsInput = scanner.nextLine();

        if (tagsInput.isEmpty()) {
            throw new Exception("At least one tag must be provided!");
        }

        // Convert comma-separated tags into array
        this.tags = tagsInput.split(",");

        // Trim whitespace from tags
        for (int i = 0; i < this.tags.length; i++) {
            this.tags[i] = this.tags[i].trim();

            if (this.tags[i].isEmpty()) {
                throw new Exception("Tags cannot be blank!");
            }
        }
    }

    @Override
    protected void performCall() throws Exception {

        // Call the server API
        ServerApiSubmitIncident serverApi = new ServerApiSubmitIncident();

        String uuid = serverApi.call(
                this.stopLocation,
                this.time,
                this.description,
                this.tags);

        // Print returned UUID
        printOutput("Incident submitted successfully.\nUUID: " + uuid);
    }
}