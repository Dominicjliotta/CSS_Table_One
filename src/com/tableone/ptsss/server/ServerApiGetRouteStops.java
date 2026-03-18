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

        // Fetch route info
        PreparedStatement info = ServerDriver.query_getRouteInfo();
        info.setString(1, this.routeNumber);
        ResultSet rs = info.executeQuery();

        StringBuilder out = new StringBuilder();
        out.append("Information on route: " + this.routeNumber.toUpperCase() + ":");

        if (!rs.next()) {
            out.append("\nNONE");
        } else {
            out.append("\nRoute Number: " + this.routeNumber.toUpperCase());

            // Safety score
            PreparedStatement score = ServerDriver.query_getSemiYearlyScore();
            score.setString(1, this.routeNumber);
            ResultSet scoreRs = score.executeQuery();

            if (scoreRs.next()) {
                double doubleScore = scoreRs.getDouble("score");
                out.append("\nSafety Score (Last 6 months): " + doubleScore);
            } else {
                out.append("\nSafety Score: N/A");
            }

            // Fetch all stops for this route
            PreparedStatement stops = ServerDriver.query_getRouteStops();
            stops.setString(1, this.routeNumber);
            ResultSet stopsRs = stops.executeQuery();

            out.append("\nStops:");
            boolean hasStops = false;
            while (stopsRs.next()) {
                hasStops = true;
                out.append("\n  - " + stopsRs.getString("locationName"));
            }
            if (!hasStops) {
                out.append("\n  NONE");
            }
        }

        return out.toString();
    }
}