package client.RecipeListScene;

import client.Recipe;
import client.UserSettings;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class RecipeListModel {

  private static final String urlStr = "http://localhost:8100/";
  // private final UserIdGetter userIdGetter = new UserIdGetter();
  private final UserSettings USER_SETTINGS = new UserSettings();

  public RecipeListModel() {}

  public List<Recipe> performGetRecipeListRequest() {
    try {
      // String urlString = urlStr + "?userID=" + userIdGetter.getUserID();
      String urlString = urlStr + "?username=" + USER_SETTINGS.getUsername();
      URL url = new URI(urlString).toURL();
      HttpURLConnection conn = (HttpURLConnection) url.openConnection();
      conn.setRequestMethod("GET");
      conn.setDoOutput(true);
      conn.setDoInput(true);

      BufferedReader in = new BufferedReader(
        new InputStreamReader(conn.getInputStream())
      );
      String response = in.readLine();
      in.close();

      List<Recipe> recipeList = new ArrayList<>();
      if (response == null || response.equals("")) return recipeList;

      String[] stringRecipeList = response.split("#");
      for (String recipeString : stringRecipeList) {
        String[] recipeComponents = recipeString.split(";");
        recipeList.add(
          new Recipe(
            recipeComponents[0],
            recipeComponents[1],
            recipeComponents[2],
            recipeComponents[3].replace("\\n", "\n")
          )
        );
      }
      return recipeList;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return new ArrayList<>();
  }
}
