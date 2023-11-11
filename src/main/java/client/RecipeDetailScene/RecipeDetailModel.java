package client.RecipeDetailScene;

import client.Recipe;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URI;

public class RecipeDetailModel {
    private static final String urlString = "http://localhost:8100/";
    // POST PUT and DELETE requests in Recipe Detail Model
    public String performRequest(String method, Recipe recipe) {
        try {
            URL url = new URI(urlString).toURL();
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod(method);
            conn.setDoOutput(true);

            if (method.equals("POST") || method.equals("PUT")) {
                OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream());
                out.write(recipe.getTitle() + ";" + recipe.getMealType() + ";" + recipe.getRecipeDetail());
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
