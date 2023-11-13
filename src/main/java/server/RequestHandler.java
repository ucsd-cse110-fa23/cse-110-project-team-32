package server;
import com.google.common.util.concurrent.ExecutionError;
import com.sun.net.httpserver.*;

import client.Recipe;

import java.io.*;
import java.net.*;
import java.util.*;

public class RequestHandler implements HttpHandler {
    private MongoDbOps mongoDbOps = new MongoDbOps();

    public RequestHandler() {
    }

    public void handle(HttpExchange httpExchange) throws IOException {
        String response = "Request Received";
        String method = httpExchange.getRequestMethod();
        boolean isRequestValid = true;
        try {
            if (method.equals("POST")) {
                response = handlePost(httpExchange);
            } else if (method.equals("GET")) {
                response = handleGet(httpExchange);
            } else if (method.equals("PUT")) {
                response = handlePutRecipe(httpExchange);
            } else if (method.equals("DELETE")) {
                response = handleDelete(httpExchange);
            } else {
                isRequestValid = false;
                throw new Exception("Invalid Request Method");
            }
        } catch (Exception e) {
            System.out.println("There Is An Error In Request Received.");
            isRequestValid = false;
            response = e.toString();
            e.printStackTrace();
        } finally {
            httpExchange.sendResponseHeaders(isRequestValid ? 200 : 404, response.length());
            OutputStream outStream = httpExchange.getResponseBody();
            outStream.write(response.getBytes());
            outStream.flush();
            outStream.close();
        }
    }

    private String handleGet(HttpExchange httpExchange) throws Exception {
        URI uri = httpExchange.getRequestURI();
        String query = uri.getRawQuery();
        // query is in the form: ...?userID=UID
        if (query == null) {
            throw new Exception("Invalid Get Request");
        }
        String userID = query.substring(query.indexOf('=')+1);
        String mongoResponse = mongoDbOps.getRecipesByUserID(userID);
        // System.out.println("Request Handler's response: " + mongoResponse);
        return mongoResponse;
    }

    private String handlePost(HttpExchange httpExchange) {
        InputStream inStream = httpExchange.getRequestBody();
        Scanner scanner = new Scanner(inStream);
        String postData = scanner.nextLine();
        scanner.close();
        String[] dataComponents = postData.split(";");
        String userID = dataComponents[0];
        String recipeID = dataComponents[1];
        String title = dataComponents[2];
        String mealType = dataComponents[3];
        String recipeDetail = dataComponents[4];
        boolean isSuccessful = mongoDbOps.createRecipeByUserId(userID, recipeID, title, mealType, recipeDetail);
        if (isSuccessful) {
            return "Succesfully created recipe: " + recipeID;
        } else {
            return "Failed to create recipe: " + recipeID;
        }
    }

    private String handlePutRecipe(HttpExchange httpExchange) {
        InputStream inStream = httpExchange.getRequestBody();
        Scanner scanner = new Scanner(inStream);
        String postData = scanner.nextLine();
        scanner.close();
        String[] dataComponents = postData.split(";");
        String userID = dataComponents[0];
        String recipeID = dataComponents[1];
        String recipeDetail = dataComponents[2];
        boolean isSuccessful = mongoDbOps.updateRecipeByUserId(userID, recipeID, recipeDetail);
        if (isSuccessful) {
            return "Succesfully updated recipe: " + recipeID;
        } else {
            return "Failed to update recipe: " + recipeID;
        }
    }

    private String handleDelete(HttpExchange httpExchange) throws Exception {
        InputStream inStream = httpExchange.getRequestBody();
        Scanner scanner = new Scanner(inStream);
        String postData = scanner.nextLine();
        scanner.close();
        String[] dataComponents = postData.split(";");
        String userID = dataComponents[0];
        String recipeID = dataComponents[1];
        String title = dataComponents[2];
        String mealType = dataComponents[3];
        String recipeDetail = dataComponents[4];
        boolean isSuccessful = mongoDbOps.deleteRecipeByUserIdRecipeId(userID, recipeID, title, mealType, recipeDetail);
        if (isSuccessful) {
            return "Successfully removed userID=" + userID + " recipeID=" + recipeID;
        } else {
            return "Oops... There was something wrong with the database.";
        }
    }
}
