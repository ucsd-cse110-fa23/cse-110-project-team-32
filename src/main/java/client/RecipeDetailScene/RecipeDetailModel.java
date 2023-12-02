package client.RecipeDetailScene;

import client.HttpResponse.CreateRecipeResponse;
import client.HttpResponse.ModifyRecipeResponse;
import client.HttpResponse.ServerResponse;
import client.Recipe;
import client.UserSettings;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
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
        ";" +
        newRecipe.getTitle() +
        ";" +
        newRecipe.getMealType() +
        ";" +
        newRecipe.getRecipeDetail().replace("\n", "\\n") +
        ";" +
        newRecipe.getImgBase64Str()
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

  public ServerResponse<Recipe> generateByChatGPT(
    String mealType,
    String ingredients
  ) {
    ServerResponse<Recipe> res = new CreateRecipeResponse();
    try {
      // Set up HTTP connection
      URL url = new URI(urlStr + "genrecipe").toURL();
      HttpURLConnection connection = (HttpURLConnection) url.openConnection();
      connection.setRequestMethod("POST");
      connection.setDoOutput(true);
      connection.setDoInput(true);
      // Set up output stream to write request body
      String chatGptPrompt = createPrompt(mealType, ingredients);
      OutputStream outputStream = connection.getOutputStream();
      outputStream.write(chatGptPrompt.getBytes());
      outputStream.close();

      int responseCode = connection.getResponseCode();
      if (responseCode == 200) {
        BufferedReader in = new BufferedReader(
          new InputStreamReader(connection.getInputStream())
        );
        String inputLine;
        StringBuilder response = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
          response.append(inputLine).append("\n");
        }
        in.close();
        // response contains # separated title AND recipe Detail AND image as file content in String type
        String responseText =
          mealType + "#" + ingredients + "#" + response.toString();
        // System.out.println("response text from server: " + responseText);
        res.setValidResponse(responseText);
      } else {
        BufferedReader in = new BufferedReader(
          new InputStreamReader(connection.getErrorStream())
        );
        String inputLine;
        StringBuilder response = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
          response.append(inputLine).append("\n");
        }
        in.close();
        res.setErrorResponse(responseCode, response.toString());
      }
      return res;
    } catch (Exception e) {
      System.out.println("[RecipeDetailModel.java] line 126");
      e.printStackTrace();
      return null;
    }
  }

  public String createPrompt(String mealType, String ingredients) {
    return (
      "Give me a simple " +
      mealType +
      " recipe with the ingredients " +
      ingredients +
      ". In the form of: <Title> followed by a single '#', Ingredients: <Line broken ingredient list>, Instructions: <Line broken instruction list>" +
      ", do not include any chars over a byte long in size, and replace bullet points by '-'"
    );
  }
}
