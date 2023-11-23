package server;

import com.sun.net.httpserver.*;
import java.io.*;
import java.net.*;

public class RootReqHandler implements HttpHandler {

  private final MongoDbOps MONGO_DB_OPS = MongoDbOps.getInstance();

  @Override
  public void handle(HttpExchange httpExchange) throws IOException {
    String method = httpExchange.getRequestMethod();
    String response = "";
    int responseCode = 200;
    try {
      if (method.equals("GET")) {
        response = handleGet(httpExchange);
      } else {
        responseCode = 404;
        throw new Exception("ERROR: Invalid Request Method to Route /");
      }
    } catch (Exception e) {
      responseCode = 501;
      response = e.toString();
      e.printStackTrace();
    } finally {
      httpExchange.sendResponseHeaders(responseCode, response.length());
      OutputStream outStream = httpExchange.getResponseBody();
      outStream.write(response.getBytes());
      outStream.flush();
      outStream.close();
    }
  }

  private String handleGet(HttpExchange httpExchange) throws Exception {
    URI uri = httpExchange.getRequestURI();
    // query is in the form: username=<username>

    String query = uri.getRawQuery();
    String username = query.substring(query.indexOf("=") + 1);
    System.out.println("Getting recipes of username: " + username);
    if (username == null || username.isEmpty()) {
      throw new Exception("Invalid Get Request");
    }
    String mongoResponse = MONGO_DB_OPS.getRecipesByUserID(username);
    // System.out.println("Request Handler's response: " + mongoResponse);
    if (mongoResponse == null) {
      throw new Exception("user does not exist!");
    }
    return mongoResponse;
  }
}
