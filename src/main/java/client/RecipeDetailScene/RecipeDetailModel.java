package client.RecipeDetailScene;

import client.HttpResponse.ModifyRecipeResponse;
import client.HttpResponse.ServerResponse;
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
  private final UserSettings USER_SETTINGS = UserSettings.getInstance();

  public ServerResponse<Boolean> performPostRecipeRequest(Recipe newRecipe) {
    ServerResponse<Boolean> res = new ModifyRecipeResponse();
    try {
      URL url = new URI(urlStr + "recipe/").toURL();
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
        newRecipe.getRecipeDetail().replace("\n", "\\n") +
        ";" +
        newRecipe.getImgURL()
      );
      out.flush();
      out.close();

      int responseCode = conn.getResponseCode();
      BufferedReader in = new BufferedReader(
        new InputStreamReader(conn.getInputStream())
      );
      String response = in.readLine();
      System.out.println("Post request response: " + response);
      in.close();
      if (responseCode == 200) {
        res.setValidResponse("");
      } else {
        res.setErrorResponse(responseCode, response);
      }
      return res;
    } catch (Exception e) {
      e.printStackTrace();
      res.setErrorResponse(503, "Oops... The Server is Down!");
      return res;
    }
  }

  public ServerResponse<Boolean> performUpdateRecipeRequest(
    Recipe updateRecipe
  ) {
    ServerResponse<Boolean> res = new ModifyRecipeResponse();
    try {
      URL url = new URI(urlStr + "recipe/").toURL();
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

      int responseCode = conn.getResponseCode();
      BufferedReader in = new BufferedReader(
        new InputStreamReader(conn.getInputStream())
      );
      String response = in.readLine();
      System.out.println("Put request response: " + response);
      in.close();
      if (responseCode == 200) {
        res.setValidResponse("");
      } else {
        res.setErrorResponse(responseCode, response);
      }
      return res;
    } catch (Exception e) {
      e.printStackTrace();
      res.setErrorResponse(503, "Oops... The Server is Down!");
      return res;
    }
  }

  public ServerResponse<Boolean> performDeleteRequest(Recipe recipeToDelete) {
    ServerResponse<Boolean> res = new ModifyRecipeResponse();
    try {
      URL url = new URI(urlStr + "recipe/").toURL();
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

      int responseCode = conn.getResponseCode();
      BufferedReader in = new BufferedReader(
        new InputStreamReader(conn.getInputStream())
      );
      String response = in.readLine();
      System.out.println("Delete request response: " + response);
      in.close();
      if (responseCode == 200) {
        res.setValidResponse("");
      } else {
        res.setErrorResponse(responseCode, response);
      }
      return res;
    } catch (Exception e) {
      e.printStackTrace();
      res.setErrorResponse(503, "Oops... The Server is Down!");
      return res;
    }
  }
}
