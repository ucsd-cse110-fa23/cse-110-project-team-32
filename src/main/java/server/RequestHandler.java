package server;
import com.sun.net.httpserver.*;
import java.io.*;
import java.net.*;
import java.util.*;

public class RequestHandler implements HttpHandler {


    private final Map<String, List<String>> data;


    public RequestHandler(Map<String, List<String>>data) {
        this.data = data;
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
                response = handlePut(httpExchange);
            } else if (method.equals("DELETE")) {
                response = handleDelete(httpExchange);
            } else {
                isRequestValid = false;
                throw new Exception("Invalid Request Method");
            }
        } catch (Exception e) {
            System.out.println("There Is An Error In Request Received.");
            response = e.toString();
            e.printStackTrace();
        } finally {
            httpExchange.sendResponseHeaders(isRequestValid ? 200 : 404, response.length());
            OutputStream outStream = httpExchange.getResponseBody();
            outStream.write(response.getBytes());
            outStream.close();
        }
    }

    private String handleGet(HttpExchange httpExchange) {
        String response = "";
        URI uri = httpExchange.getRequestURI();
        String query = uri.getRawQuery();
        // query is in the form: ...?userID=UID
        if (query != null) {
            String userID = query.substring(query.indexOf('=')+1);
            List<String> stringRecipeList = this.data.get(userID);
            if (stringRecipeList == null) {
                response = "No data found for userID=" + userID;
            } else {
                for (String r : stringRecipeList) {
                    response += (r + "#");
                }
            }
        }
        System.out.println("handleGet response: " + response);
        return response;
    }

    private String handlePost(HttpExchange httpExchange) {
        InputStream inStream = httpExchange.getRequestBody();
        Scanner scanner = new Scanner(inStream);
        String postData = "";
        while (scanner.hasNext()) {
            postData += scanner.nextLine() + "BREAK";
        }
        scanner.close();
        String userID = postData.substring(0, postData.indexOf(';'));
        String stringRecipe = postData.substring(postData.indexOf(';')+1);

        System.out.println("handlePost stringRecipe received: " + stringRecipe);

        List<String> recipeList = this.data.get(userID);
        if (recipeList == null) {
            recipeList = new ArrayList<>();
        }
        recipeList.add(stringRecipe);
        this.data.put(userID, recipeList);

        String response = "Received POST request on userID = " + userID;

        System.out.println(response);

        return response;
    }

    private String handlePut(HttpExchange httpExchange) {
        InputStream inStream = httpExchange.getRequestBody();
        Scanner scanner = new Scanner(inStream);
        String postData = "";
        while (scanner.hasNext()) {
            postData += scanner.nextLine() + "BREAK";
        }
        scanner.close();
        String userID = postData.substring(0, postData.indexOf(';'));
        String stringRecipe = postData.substring(postData.indexOf(';')+1);

        System.out.println("handlePost stringRecipe received: " + stringRecipe);

        String recipeID = stringRecipe.substring(0, stringRecipe.indexOf(';'));
        String response = "userID=" + userID + " didn't have a recipe list. One is created for them.";
        if (this.data.containsKey(userID)) {
            response = "Updated userID=" + userID + " recipeID=" + recipeID;
        }
        List<String> recipeList = this.data.get(userID);
        if (recipeList == null) {
            // this case should never happen
            recipeList = new ArrayList<>();
            recipeList.add(stringRecipe);
            return response;
        }
        for (int i = 0; i < recipeList.size(); i++) {
            if (recipeList.get(i).startsWith(recipeID)) {
                recipeList.set(i, stringRecipe);
                break;
            }
        }
        this.data.put(userID, recipeList);
        return response;
    }

    private String handleDelete(HttpExchange httpExchange) {
        // "?userID=" + userIdGetter.getUserID() + "recipeID=" + recipeID;
        
        URI uri = httpExchange.getRequestURI();
        String query = uri.getRawQuery();
        System.out.println("query: " + query);
        String dataArr[] = query.split("&");
        String userID = dataArr[0].split("=")[1];
        String recipeID = dataArr[1].split("=")[1];

        List<String> stringRecipeList = this.data.get(userID);
        int delInd = 0;
        for (; delInd < stringRecipeList.size(); delInd++) {
            if (stringRecipeList.get(delInd).startsWith(recipeID)) break;
        }
        stringRecipeList.remove(delInd);
        this.data.put(userID, stringRecipeList);

        return "removed userID=" + userID + " recipeID=" + recipeID;
    }
}
