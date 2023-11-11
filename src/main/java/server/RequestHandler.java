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
                   response += r + "#";
                }
            }
        }
        return response;
    }

    private String handlePost(HttpExchange httpExchange) {
        InputStream inStream = httpExchange.getRequestBody();
        Scanner scanner = new Scanner(inStream);
        String postData = scanner.nextLine();
        String userID = postData.substring(0, postData.indexOf(';'));
        String stringRecipe = postData.substring(postData.indexOf(';')+1);
        List<String> recipeList = this.data.get(userID);
        if (recipeList == null) {
            recipeList = new ArrayList<>();
        }
        recipeList.add(stringRecipe);
        this.data.put(userID, recipeList);

        String response = "Received POST request on userID = " + userID;
        
        scanner.close();

        System.out.println(response);

        return response;
    }

    private String handlePut(HttpExchange httpExchange) {
        
        System.out.println("handle put request");
        return "";
        /*
        InputStream inStream = httpExchange.getRequestBody();
        Scanner scanner = new Scanner(inStream);
        String putData = scanner.nextLine();
        scanner.close();
        String languageKey = putData.substring(0, putData.indexOf(','));
        String languageYear = putData.substring(putData.indexOf(',') + 1);

        String response = "Added entry {" + languageKey + ", " + languageYear + "}";

        if (this.data.containsKey(languageKey)) {
            response = "Updated entry {" + languageKey + ", " + languageYear + "} (previous year: " + this.data.get(languageKey) + ")";
        }
        this.data.put(languageKey, languageYear);
        return response;
        */
    }

    private String handleDelete(HttpExchange httpExchange) {
        
        System.out.println("handle post request");
        return "";
        /*
        String response = "Invalid DELETE Request";
        URI uri = httpExchange.getRequestURI();
        String query = uri.getRawQuery();
        String languageKey = query.substring(query.indexOf('=')+1);

        response = "No data found for " + languageKey;
        if (this.data.containsKey(languageKey)) {
            response = "Deleted entry {" + languageKey + ", " + this.data.get(languageKey) + "}";
            this.data.remove(languageKey);
        }
        return response;
        */
    }
}
