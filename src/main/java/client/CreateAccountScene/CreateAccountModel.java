package client.CreateAccountScene;

import client.HttpResponse.AuthResponse;
import client.HttpResponse.CreateRecipeResponse;
import client.HttpResponse.ServerResponse;
import client.HttpResponse.WhisperResponse;
import client.Recipe;
import client.UserSettings;

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

public class CreateAccountModel {

    private final UserSettings USER_SETTINGS = UserSettings.getInstance();
    private static final String URL = "http://localhost:8100/";
    
    public void storeAccountDetails(String user, String pass) {
         ServerResponse<Boolean> res = new AuthResponse();
    try {
      String query = "?username=" + user + "&password=" + pass;
      URL url = new URI(URL + "auth" + query).toURL();
      HttpURLConnection conn = (HttpURLConnection) url.openConnection();

      conn.setRequestMethod("POST");
      conn.setDoOutput(true);

      int responseCode = conn.getResponseCode();
      String response = "No response";
      if (responseCode == 200) {
        BufferedReader in = new BufferedReader(
          new InputStreamReader(conn.getInputStream())
        );
        response = in.readLine();
        in.close();
        res.setValidResponse("");
      } else {
        BufferedReader in = new BufferedReader(
          new InputStreamReader(conn.getErrorStream())
        );
        response = in.readLine();
        in.close();
        res.setErrorResponse(responseCode, response);
      }
      
    } catch (Exception e) {
      e.printStackTrace();
      res.setErrorResponse(503, "Oops... The Server is Down!");
      
    }
    
    
    
    
    }

    public Boolean checkValidUsername(String username, String password) {
        ServerResponse<Boolean> res = new AuthResponse();
    try {
      String query = "?username=" + username + "&password=" + password;
      URL url = new URI(URL + "auth" + query).toURL();
      HttpURLConnection conn = (HttpURLConnection) url.openConnection();

      conn.setRequestMethod("GET");
      conn.setDoOutput(true);

      int responseCode = conn.getResponseCode();
      String response = "No response";
      if (responseCode == 200) {
        BufferedReader in = new BufferedReader(
          new InputStreamReader(conn.getInputStream())
        );
        response = in.readLine();
        in.close();
        res.setValidResponse("");
      } else {
        BufferedReader in = new BufferedReader(
          new InputStreamReader(conn.getErrorStream())
        );
        response = in.readLine();
        in.close();
        res.setErrorResponse(responseCode, response);
      }
    //   return res;
    } catch (Exception e) {
      e.printStackTrace();
      res.setErrorResponse(503, "Oops... The Server is Down!");
    //   return res;
    }
        return false;
    }
}
