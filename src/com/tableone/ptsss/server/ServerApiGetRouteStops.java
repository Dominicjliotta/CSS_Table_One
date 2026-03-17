package com.tableone.ptsss.server;

/*
 * This class shows you how to implement your server-side API classes!
 * This fake api call will simply take a name and age and print it back to the user
 */

//name it "ServerApi<Something>", and make sure to extend ServerApi
//when extending, include the data type that this api call returns
//in this example, the api call returns a String
//if your api call does not return anything, extend ServerApi<Void>
public class ServerApiGetRouteStops extends ServerApi<String> {

private String routeNumber

	//here, you parse the arguments given to the server by the client
	//and save the data into instance variables.
	//the arguments should be given as instance data from the corresponding client api class
	//here we do error checking to make sure the arguments are valid.
	//
	//why is it designed this way? in a real-world client-server system, the client may
	//pass arbitrary data of any type to the server when making a call, and it is the
	//responsibility of the server to type-check and validate the data
	@Override
	protected void parseRequest(Object[] args) throws Exception {

		//check number of arguments
		if (args.length != 1) {
			throw new Exception("ServerApiGetRouteStops received " + args.length + " arguments, expected 1");
		}

		//type check each argument
		if (!(args[0] instanceof String)) {
			throw new Exception("ServerApiGetRouteStops received invalid type for routeNumber");
		}

		//if all checks pass, cast the data and save to instance variables
		this.routeNumber = (String)args[0];

	}

	//this is the function that will finally fulfil the api call.
	//you may return an object, not all apis will have to.
	//if nothing has to be returned, simply return null instead.
	//here, we will just return a string containing the name and age.
	@Override
	@Override
protected String completeRequest() throws Exception {
    // Reject any non-existent route numbers
    PreparedStatement ps = ServerDriver.query_routeExists();
    ps.setString(1, this.routeNumber);
    ResultSet Rnumber = ps.executeQuery();
    if (!Rnumber.next()) throw new Exception("Invalid Route Number!");

    // Fetch the query for this api call
    PreparedStatement info = ServerDriver.query_getRouteInfo();
    info.setString(1, this.routeNumber);
    ResultSet rs = info.executeQuery();

    // Build the output string
    StringBuilder out = new StringBuilder();
    out.append("Information on route: " + this.routeNumber.toUpperCase() + ":");

    if (!rs.next()) {
        out.append("\nNONE");
    } else {
        out.append("\nRoute Number: " + rs.getString("Route Number"));

        // Fetch the query for calculating semi-yearly route scores
        PreparedStatement score = ServerDriver.query_getSemiYearlyScore();
        score.setString(1, routeNumber);
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

    // Return the output string
    return out.toString();
}
}

