package server;

import com.sun.net.httpserver.*;
import java.io.*;
import java.net.*;

public class RecipeReqHandler implements HttpHandler {

  private final MongoDbOps MONGO_DB_OPS = MongoDbOps.getInstance();

  @Override
  public void handle(HttpExchange httpExchange) throws IOException {
    String method = httpExchange.getRequestMethod();
    String response = "";
    int responseCode = 200;
    try {
      if (method.equals("GET")) {
        response = handleGet(httpExchange);
        //   } else if (method.equals("POST")) {
        //     response = handlePost(httpExchange);
        //   } else if (method.equals("PUT")) {
        //     response = handlePut(httpExchange);
        //   } else if (method.equals("DELETE")) {
        //     response = handleDelete(httpExchange);
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
    StringBuilder html = new StringBuilder();
    html
      .append("<!DOCTYPE html>\n")
      .append("<html>\n")
      .append("<head>\n")
      .append("<meta charset=\"UTF-8\">\n")
      .append("<title>PantryPal Recipe</title>\n")
      .append("</head>\n")
      .append("<body>\n");
    URI uri = httpExchange.getRequestURI();
    // query is in the form: username=<username>&password=<password>
    String query = uri.getRawQuery();
    String[] usernameAndRecipeID = query.split("&");
    String[] usernamePair = usernameAndRecipeID[0].split("=");
    String[] recipeIDPair = usernameAndRecipeID[1].split("=");
    String username = "", recipeID = "";
    if (usernamePair.length != 2 || recipeIDPair.length != 2) {
      //   throw new Exception("Invalid Get Request");
      html.append(
        "<h1>Oops, url does not exist! Did you enter the correct url sent by your friend?</h1>\n"
      );
    } else {
      username = usernamePair[1];
      recipeID = recipeIDPair[1];
      System.out.println(
        "GET HTML page for recipeID=" + recipeID + ", username=" + username
      );
      String recipeString = MONGO_DB_OPS.getRecipeByUsernameAndRecipeID(
        username,
        recipeID
      );

      if (recipeString == null) {
        html.append(
          "<h1>Sorry, the recipe shared to you is deleted by the sharer :< </h1>\n"
        );
      } else {
        String[] recipeContents = recipeString.split(";");
        String title = recipeContents[1];
        String mealType = recipeContents[2];
        String recipeDetail = recipeContents[3];
        html
          .append("<h1>" + title + "</h1>\n")
          .append("<p>Meal Type: " + mealType + "</p>\n")
          .append(
            "<p>Recipe Detail: " +
            recipeDetail.replace("\\n", "<br>") +
            "</p>\n"
          );
      }
    }

    html.append("</body></html>");
    return html.toString();
  }
}
