package client.CreateAccountScene;

import client.HttpResponse.CreateAccountResponse;
import client.HttpResponse.CreateRecipeResponse;
import client.HttpResponse.RecipeListResponse;
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
import java.util.List;

import org.bson.io.OutputBuffer;
import org.json.JSONArray;
import org.json.JSONObject;

public class CreateAccountModel {
    
    private static final String URL = "http://localhost:8100/";
  private final UserSettings USER_SETTINGS = new UserSettings();

    public ServerResponse<String> performStoreDetails(String username, String password) {
        ServerResponse<String> res = new CreateAccountResponse();
        try {
            String urlString = URL + "?username=" + username +"&password=" +password;
            //URL: localhost...?username=<username>&password=<password>;
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
            res.setErrorResponse(501, "Oops... The Server is Down!");
            return res;
          }
    }

    // public Boolean checkValidUsername(String username) {
    // try{
    //     String urlString = URL + "?username=" + username;
    //   URL url = new URI(urlString).toURL();
    //   HttpURLConnection conn = (HttpURLConnection) url.openConnection();
    //   conn.setRequestMethod("GET");
    //   conn.setDoOutput(true);
    //   conn.setDoInput(true);

    //   int responseCode = conn.getResponseCode();
    //   BufferedReader in = new BufferedReader(
    //     new InputStreamReader(conn.getInputStream())
    //   );
    //   String response = in.readLine();
    //   in.close();

    // } catch (Exception e) {
    //   e.printStackTrace();
    // //   res.setErrorResponse(501, "Oops... The Server is Down!");

    // }

    //     return false;
    // }
}
