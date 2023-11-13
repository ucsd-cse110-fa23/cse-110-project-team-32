package client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URI;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

public class Model {
    private static final String urlStr = "http://localhost:8100/";
    private UserIdGetter userIdGetter;

    public Model() {
        userIdGetter = new UserIdGetter();
    }

    // POST PUT and DELETE requests in Recipe Detail Model
    // argument String recipeID only used on DELETE request, null otherwise
    // public String performRequest(String method, Recipe recipe, String recipeID) {
    //     try {
    //         String urlString = urlStr;
    //         if (method.equals("GET")) {
    //             urlString += "?userID=" + userIdGetter.getUserID();
    //         } else if (method.equals("DELETE")) {
    //             urlString += "?userID=" + userIdGetter.getUserID() + "&recipeID=" + recipeID;
    //         }
    //         URL url = new URI(urlString).toURL();
    //         HttpURLConnection conn = (HttpURLConnection) url.openConnection();
    //         conn.setRequestMethod(method);
    //         conn.setDoOutput(true);

    //         if (method.equals("POST") || method.equals("PUT")) {
    //             OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream());
    //             // request body format: "userID;recipeID;title;mealType;recipeDetail"
    //             out.write(userIdGetter.getUserID() + ";" + recipe.getRecipeID() + ";" + recipe.getTitle() + ";" + recipe.getMealType() + ";" + recipe.getRecipeDetail());
    //             out.flush();
    //             out.close();
    //         }
    //         BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
    //         String response = in.readLine();
    //         in.close();
    //         System.out.println("Model.java line 42, HTTP response: " + response);
    //         return response;
    //     } catch (Exception e) {
    //         e.printStackTrace();
    //         return "Error: " + e.getMessage();
    //     }
    // }

    public List<Recipe> performGetRecipeListRequest() {
        try {
            String urlString = urlStr + "?userID=" + userIdGetter.getUserID();
            URL url = new URI(urlString).toURL();
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setDoOutput(true);
            conn.setDoInput(true);

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String response = in.readLine();
            in.close();

            List<Recipe> recipeList = new ArrayList<>();
            if (response == null || response.equals("")) return recipeList;

            String[] stringRecipeList = response.split("#");
            for (String recipeString : stringRecipeList) {
                String[] recipeComponents = recipeString.split(";");
                recipeList.add(new Recipe(recipeComponents[0], recipeComponents[1], recipeComponents[2],recipeComponents[3].replace("\\n", "\n")));
            }
            return recipeList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public void performPostRecipeRequest(Recipe newRecipe) {
        try {
            URL url = new URI(urlStr).toURL();
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setDoInput(true);

            OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream());
            // request body format: "userID;recipeID;title;mealType;recipeDetail"
            out.write(userIdGetter.getUserID() + ";" + newRecipe.getRecipeID() +  newRecipe.getRecipeID() + ";" + newRecipe.getTitle() + ";" + newRecipe.getMealType() + ";" + newRecipe.getRecipeDetail().replace("\n", "\\n"));
            out.flush();
            out.close();
        
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String response = in.readLine();
            // System.out.println("Post request response: " + response);
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void performUpdateRecipeRequest(Recipe updateRecipe) {
        try {
            URL url = new URI(urlStr).toURL();
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("PUT");
            conn.setDoOutput(true);
            conn.setDoInput(true);

            OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream());
            // request body format: "userID;recipeID;recipeDetail"
            out.write(userIdGetter.getUserID() + ";" + updateRecipe.getRecipeID() + ";" + updateRecipe.getRecipeDetail().replace("\n", "\\n"));
            out.flush();
            out.close();

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String response = in.readLine();
            System.out.println("Put request response: " + response);
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    } 

    public void performDeleteRequest(Recipe recipeToDelete) {
        try {
            URL url = new URI(urlStr).toURL();
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("DELETE");
            conn.setDoOutput(true);
            conn.setDoInput(true);

            OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream());
            // request body format: "userID;recipeID;recipeDetail"
            out.write(userIdGetter.getUserID() + ";" + recipeToDelete.getRecipeID() + ";" + recipeToDelete.getTitle() + ";" + recipeToDelete.getMealType() + ";" + recipeToDelete.getRecipeDetail().replace("\n", "\\n"));
            out.flush();
            out.close();
            
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream())); 
            String response = in.readLine();
            in.close();
            System.out.println("Delete request response: " + response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
