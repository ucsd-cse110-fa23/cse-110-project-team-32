package client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URI;

public class Model {
    private static final String urlStr = "http://localhost:8100/";
    private UserIdGetter userIdGetter;

    public Model() {
        userIdGetter = new UserIdGetter();
    }
    // POST PUT and DELETE requests in Recipe Detail Model
    // argument Strin recipeID only used on DELETE request
    public String performRequest(String method, Recipe recipe, String recipeID) {
        try {
            String urlString = this.urlStr;
            if (method.equals("GET")) {
                urlString += "?userID=" + userIdGetter.getUserID();
            } else if (method.equals("DELETE")) {
                urlString += "?userID=" + userIdGetter.getUserID() + "recipeID=" + recipeID;
            }
            URL url = new URI(urlString).toURL();
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod(method);
            conn.setDoOutput(true);

            if (method.equals("POST") || method.equals("PUT")) {
                OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream());
                out.write(userIdGetter.getUserID() + ";" + recipe.getRecipeID() + ";" + recipe.getTitle() + ";" + recipe.getMealType() + ";" + recipe.getRecipeDetail());
                out.flush();
                out.close();
            }
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String response = in.readLine();
            in.close();
            return response;
        } catch (Exception e) {
            e.printStackTrace();
            return "Error: " + e.getMessage();
        }
    }
}
