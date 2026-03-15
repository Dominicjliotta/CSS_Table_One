package com.tableone.ptsss.client;

import java.util.Scanner;

import com.tableone.ptsss.server.ServerApiGetRecentTags;

public class ClientApiGetRecentTags extends ClientApi {

  private String routeNumber;

  @Override
  protected String getName() {
    return "getRecentTags(routeNumber)";
  }

  @Override
  protected void parseRequest(Scanner scanner) throws Exception {

    //ask the user for a route Number and save the input
    System.out.print("Route Number: ");
    this.routeNumber = scanner.nextLine().trim();

    //make sure destination isnt blank
    if (this.routeNumber.isEmpty()) throw new Exception("Route Number cannot be blank!");
  }

  @Override
  protected void performCall() throws Exception {

    //make the call to the server and get the output
    ServerApiGetRecentTags serverApi = new ServerApiGetRecentTags();
    String output = serverApi.call(this.routeNumber);

    //print the output
    printOutput(output);
  }

}