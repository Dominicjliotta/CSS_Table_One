package com.tableone.ptsss.server;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ServerApiGetRouteStops extends ServerApi<String> {

    private String routeNumber;

    @Override
    protected void parseRequest(Object[] args) throws Exception {

        if (args.length != 1) {
            throw new Exception("ServerApiGetRouteStops received " + args.length + " arguments, expected 1");
        }

        if (!(args[0] instanceof String)) {
            throw new Exception("ServerApiGetRouteStops received invalid type for routeNumber");
        }

        this.routeNumber = (String) args[0];
    }

    @Override
    protected String completeRequest() throws Exception {

        // Reject any non-existent route numbers
        PreparedStatement ps = ServerDriver.query_routeExists();
        ps.setString(1, this.routeNumber);
        ResultSet Rnumber = ps.executeQuery();
        if (!Rnumber.next()) throw new Exception("Invalid Route Number!");

        StringBuilder out = new StringBuilder();
        out.append("Stops for route " + this.routeNumber.toUpperCase() + ":");

        // Fetch all stops and arrival times for this route
        PreparedStatement stops = ServerDriver.query_getRouteStops();
        stops.setString(1, this.routeNumber);
        ResultSet stopsRs = stops.executeQuery();

        boolean hasRows = false;
        String lastLocation = null;
        while (stopsRs.next()) {
            hasRows = true;
            String location = stopsRs.getString("locationName");
            String day = stopsRs.getString("day");
            String time = stopsRs.getString("time");

            // Print the stop header once, then list all day/time pairs under it
            if (lastLocation == null || !lastLocation.equals(location)) {
                out.append("\n- " + location);
                lastLocation = location;
            }

            out.append("\n    " + day + " @ " + time);
        }

        if (!hasRows) {
            out.append("\nNONE");
        }

        return out.toString();
    }
}