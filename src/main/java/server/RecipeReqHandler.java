package server;

import com.sun.net.httpserver.*;
import java.io.*;
import java.net.*;

public class RecipeReqHandler implements HttpHandler {

  private final MongoDbOps MONGO_DB_OPS = MongoDbOps.getInstance();
  private final Helper HELPER = Helper.getInstance();
  private int statusCode = 200;

  @Override
  public void handle(HttpExchange httpExchange) throws IOException {
    String method = httpExchange.getRequestMethod();
    String response = "";
    statusCode = 200;
    try {
      if (method.equals(Constants.GET)) {
        response = handleGet(httpExchange);
      } else if (method.equals(Constants.POST)) {
        response = handlePost(httpExchange);
      } else if (method.equals(Constants.PUT)) {
        response = handlePut(httpExchange);
      } else if (method.equals(Constants.DELETE)) {
        response = handleDelete(httpExchange);
      } else {
        statusCode = 404;
        throw new Exception(Constants.INVALID_REQ_TO_ROUTE + " /recipe");
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
        String imgBase64Str = recipeContents[4];
        html
          .append("<h1>" + title + "</h1>\n")
          .append("<p>Meal Type: " + mealType + "</p>\n")
          .append(
            "<img src=\"data:image/[format];base64,%s\" alt=\"recipe image\" style=\"width: 150px; height: 150px\" >".formatted(
                imgBase64Str
              )
          )
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

  private String handlePost(HttpExchange httpExchange) {
    try {
      String[] dataComponents = HELPER.readReqBody(httpExchange).split(";");
      String userID = dataComponents[0];
      String recipeID = dataComponents[1];
      String title = dataComponents[2];
      String mealType = dataComponents[3];
      String recipeDetail = dataComponents[4];
      String imgBase64Str = dataComponents[5];

      boolean isSuccessful = MONGO_DB_OPS.createRecipeByUserId(
        userID,
        recipeID,
        title,
        mealType,
        recipeDetail,
        imgBase64Str
      );
      if (isSuccessful) {
        return "Succesfully created recipe: " + recipeID;
      }
      statusCode = 501;
      return "Failed to create recipe: " + recipeID;
    } catch (IOException ioExcep) {
      System.out.println("IO Exception");
      ioExcep.printStackTrace();
      statusCode = 501;
      return Constants.INVALID_POST_TO_ROUTE + " /recipe";
    } catch (Exception e) {
      statusCode = 501;
      e.printStackTrace();
      return Constants.INVALID_POST_TO_ROUTE + " /recipe";
    }
  }

  private String handlePut(HttpExchange httpExchange) {
    try {
      String[] dataComponents = HELPER.readReqBody(httpExchange).split(";");
      String username = dataComponents[0];
      String recipeID = dataComponents[1];
      String recipeDetail = dataComponents[2];
      System.out.println("username: " + username);
      System.out.println("recipeID: " + recipeID);
      System.out.println("recipeDetail: " + recipeDetail);
      boolean isSuccessful = MONGO_DB_OPS.updateRecipeByUsername(
        username,
        recipeID,
        recipeDetail
      );
      if (isSuccessful) {
        return "Succesfully updated recipe: " + recipeID;
      } else {
        return "Failed to updated recipe: " + recipeID;
      }
    } catch (IOException ioExcep) {
      System.out.println("IO Exception");
      ioExcep.printStackTrace();
      statusCode = 501;
      return Constants.INVALID_PUT_TO_ROUTE + " /recipe";
    } catch (Exception e) {
      statusCode = 501;
      e.printStackTrace();
      return Constants.INVALID_PUT_TO_ROUTE + " /recipe";
    }
  }

  private String handleDelete(HttpExchange httpExchange) {
    try {
      String reqBody = HELPER.readReqBody(httpExchange);
      String[] components = reqBody.split(";");
      String username = components[0];
      String recipeID = components[1];
      String title = components[2];
      String mealType = components[3];
      String recipeDetail = components[4];
      boolean isSuccessful = MONGO_DB_OPS.deleteRecipeByUserIdRecipeId(
        username,
        recipeID,
        title,
        mealType,
        recipeDetail
      );
      if (isSuccessful) {
        return (
          "Successfully removed username=" + username + " recipeID=" + recipeID
        );
      } else {
        return "Oops... There was something wrong with the database.";
      }
    } catch (Exception e) {
      e.printStackTrace();
      statusCode = 501;
      return Constants.INVALID_DELETE_TO_ROUTE + " /recipe";
    }
  }
}
