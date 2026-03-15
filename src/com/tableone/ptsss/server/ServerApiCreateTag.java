package com.tableone.ptsss.server;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import static java.awt.SystemColor.info;

public class ServerApiCreateTag extends ServerApi<Boolean> {

    private String name;
    private int severity;


    @Override
    protected void parseRequest(Object[] args) throws Exception {

        //make sure the server received the correct number of arguments
        if (args.length != 2) {
            throw new Exception("ServerApiCreateTag received " + args.length + " arguments, expected 2");
        }

        //type-check tag name
        if (!(args[0] instanceof String)) {
            throw new Exception("ServerApiCreateTag received invalid type for tag name");
        }

        //type-check severity
        if (!(args[1] instanceof Integer)) {
            throw new Exception("ServerApiCreateTag received invalid type for severity");
        }

        //cast and store the arguments into variables
        this.name = (String) args[0];
        this.severity = (int) args[0];
    }

    @Override
    protected Boolean completeRequest() throws Exception {

        try {
            //begin a transaction because this performs CRUD operations
            ServerDriver.beginTX();

            //fetch the query for this api call
            PreparedStatement ContentTag = ServerDriver.query_addContentTag();
            //set the parameters
            ContentTag.setString(1, this.name);
            ContentTag.setInt(2, this.severity);


            //execute the query
            ContentTag.executeQuery();

            //commit the transaction
            ServerDriver.commitTX();

            return true;

        } catch  (Exception e) {
            return false;
        }
    }


}



