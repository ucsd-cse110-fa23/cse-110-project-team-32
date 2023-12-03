package client.RecipeListScene;

import client.HttpResponse.RecipeListResponse;
import client.HttpResponse.ServerResponse;
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
  private final UserSettings USER_SETTINGS = UserSettings.getInstance();

  public RecipeListModel() {}

  public ServerResponse<List<Recipe>> performGetRecipeListRequest() {
    ServerResponse<List<Recipe>> res = new RecipeListResponse();
    try {
      // String urlString = urlStr + "?userID=" + userIdGetter.getUserID();
      String urlString = urlStr + "?username=" + USER_SETTINGS.getUsername();
      URL url = new URI(urlString).toURL();
      HttpURLConnection conn = (HttpURLConnection) url.openConnection();
      conn.setRequestMethod("GET");
      conn.setDoOutput(true);
      conn.setDoInput(true);

      int responseCode = conn.getResponseCode();
      BufferedReader in = new BufferedReader(
        new InputStreamReader(conn.getInputStream())
      );
      String response = in.readLine();
      in.close();

      if (responseCode == 200) {
        res.setValidResponse(response);
      } else {
        res.setErrorResponse(responseCode, response);
      }
      return res;
    } catch (Exception e) {
      e.printStackTrace();
      res.setServerDownResponse();
      return res;
    }
  }
}
