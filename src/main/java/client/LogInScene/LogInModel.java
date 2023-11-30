package client.LogInScene;

import client.HttpResponse.AuthResponse;
import client.HttpResponse.CreateRecipeResponse;
import client.HttpResponse.ModifyRecipeResponse;
import client.HttpResponse.ServerResponse;
import client.HttpResponse.WhisperResponse;
import client.Recipe;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.http.HttpResponse;
import org.bson.io.OutputBuffer;
import org.json.JSONArray;
import org.json.JSONObject;

public class LogInModel {

  private static final String URL = "http://localhost:8100/";
  private Boolean autoLogIn;

  public ServerResponse<Boolean> checkUserPass(
    String username,
    String password
  ) {
    ServerResponse<Boolean> res = new AuthResponse();
    try {
      URL url = new URI(URL + "auth/").toURL();
      HttpURLConnection conn = (HttpURLConnection) url.openConnection();
      conn.setRequestMethod("POST");
      conn.setDoOutput(true);
      conn.setDoInput(true);

      OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream());
      // request body format: "userID;recipeID;title;mealType;recipeDetail"
      out.write(username + ";" + password);
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

  public Boolean checkRememberMe(Boolean checked) {
    // TODO: Use "checked" variable in LogInView to check if box is checked
    autoLogIn = checked;
    return autoLogIn;
  }

  public void autoLogIn() {
    autoLogIn = true;
    // TODO:
  }

  public void serverStatusCheck() {
    //TODO
  }
}
