package com.tableone.ptsss.server;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ServerApiGetIncidentsByStop extends ServerApi<String> {

    private String locationName;

    @Override
    protected void parseRequest(Object[] args) throws Exception {

        if (args.length != 1) {
            throw new Exception("ServerApiGetIncidentsByStop received " + args.length + " arguments, expected 1");
        }

        if (!(args[0] instanceof String)) {
            throw new Exception("ServerApiGetIncidentsByStop received invalid type for locationName");
        }

        this.locationName = (String) args[0];
    }

    @Override
    protected String completeRequest() throws Exception {

        PreparedStatement ps = ServerDriver.query_getIncidentsByStop();
        ps.setString(1, this.locationName);
        ResultSet rs = ps.executeQuery();

        StringBuilder out = new StringBuilder();
        out.append("Incidents at stop: " + this.locationName + ":");
        out.append("\n================================");

        boolean isEmpty = true;

        while (rs.next()) {
            isEmpty = false;
            String time        = rs.getString("incidentTime");
            String description = rs.getString("description");
            String tag         = rs.getString("tag");
            String location    = rs.getString("locationName");

            out.append("\nStop: "        + location);
            out.append("\nTime: "        + time);
            out.append("\nTag: "         + tag);
            out.append("\nDescription: " + description);
            out.append("\n--------------------------------");
        }

        if (isEmpty)
            out.append("\nNONE");

        return out.toString();
    }
}