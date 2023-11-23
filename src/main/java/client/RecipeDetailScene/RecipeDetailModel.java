package client.RecipeDetailScene;

import client.Recipe;
import client.UserSettings;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

public class RecipeDetailModel {

  private static final String urlStr = "http://localhost:8100/";
  private final UserSettings USER_SETTINGS = new UserSettings();

  public void performPostRecipeRequest(Recipe newRecipe) {
    try {
      URL url = new URI(urlStr).toURL();
      HttpURLConnection conn = (HttpURLConnection) url.openConnection();
      conn.setRequestMethod("POST");
      conn.setDoOutput(true);
      conn.setDoInput(true);

      OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream());
      // request body format: "userID;recipeID;title;mealType;recipeDetail"
      out.write(
        USER_SETTINGS.getUsername() +
        ";" +
        newRecipe.getRecipeID() +
        newRecipe.getRecipeID() +
        ";" +
        newRecipe.getTitle() +
        ";" +
        newRecipe.getMealType() +
        ";" +
        newRecipe.getRecipeDetail().replace("\n", "\\n")
      );
      out.flush();
      out.close();

      BufferedReader in = new BufferedReader(
        new InputStreamReader(conn.getInputStream())
      );
      String response = in.readLine();
      System.out.println("Post request response: " + response);
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
      out.write(
        USER_SETTINGS.getUsername() +
        ";" +
        updateRecipe.getRecipeID() +
        ";" +
        updateRecipe.getRecipeDetail().replace("\n", "\\n")
      );
      out.flush();
      out.close();

      BufferedReader in = new BufferedReader(
        new InputStreamReader(conn.getInputStream())
      );
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
      out.write(
        USER_SETTINGS.getUsername() +
        ";" +
        recipeToDelete.getRecipeID() +
        ";" +
        recipeToDelete.getTitle() +
        ";" +
        recipeToDelete.getMealType() +
        ";" +
        recipeToDelete.getRecipeDetail().replace("\n", "\\n")
      );
      out.flush();
      out.close();

      BufferedReader in = new BufferedReader(
        new InputStreamReader(conn.getInputStream())
      );
      String response = in.readLine();
      in.close();
      System.out.println("Delete request response: " + response);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
