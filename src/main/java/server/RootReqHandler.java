package server;

import com.sun.net.httpserver.*;
import java.io.*;
import java.net.*;

public class RootReqHandler implements HttpHandler {

  private final MongoDbOps MONGO_DB_OPS = MongoDbOps.getInstance();
  private int statusCode = 200;

  @Override
  public void handle(HttpExchange httpExchange) throws IOException {
    String method = httpExchange.getRequestMethod();
    String response = "";
    statusCode = 200;
    try {
      if (method.equals(Constants.GET)) {
        response = handleGet(httpExchange);
      } else {
        statusCode = 404;
        throw new Exception(Constants.INVALID_REQ_TO_ROUTE + " /");
      }
    } catch (Exception e) {
      statusCode = 501;
      response = e.toString();
      e.printStackTrace();
    } finally {
      httpExchange.sendResponseHeaders(statusCode, response.length());
      OutputStream outStream = httpExchange.getResponseBody();
      outStream.write(response.getBytes());
      outStream.flush();
      outStream.close();
    }
  }

  private String handleGet(HttpExchange httpExchange) {
    URI uri = httpExchange.getRequestURI();
    // query is in the form: username=<username>
    String query = uri.getRawQuery();
    System.out.println("query to get all recipes: " + query);
    String username = query.substring(query.indexOf("=") + 1);
    System.out.println("Getting recipes of username: " + username);
    if (username == null || username.isEmpty()) {
      statusCode = 501;
      return Constants.INVALID_GET_TO_ROUTE + " /";
    }
    String mongoResponse = MONGO_DB_OPS.getRecipesByUserID(username);
    // System.out.println("Request Handler's response: " + mongoResponse);
    if (mongoResponse == null) {
      statusCode = 501;
      return Constants.INVALID_GET_TO_ROUTE + " /";
    }
    return mongoResponse;
  }
}
