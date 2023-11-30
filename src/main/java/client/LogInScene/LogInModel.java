package client.LogInScene;

import client.HttpResponse.AuthResponse;
import client.HttpResponse.PingResponse;
import client.HttpResponse.ServerResponse;
import client.UserSettings;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

public class LogInModel {

  private final UserSettings USER_SETTINGS = UserSettings.getInstance();
  private static final String URL = "http://localhost:8100/";
  private Boolean autoLogIn;

  public ServerResponse<Boolean> pingServer() {
    ServerResponse<Boolean> res = new PingResponse();
    try {
      URL url = new URI(URL + "ping/").toURL();
      HttpURLConnection conn = (HttpURLConnection) url.openConnection();

      conn.setRequestMethod("GET");
      conn.setDoOutput(true);

      int responseCode = conn.getResponseCode();
      BufferedReader in = new BufferedReader(
        new InputStreamReader(conn.getInputStream())
      );
      String response = in.readLine();
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

  public ServerResponse<Boolean> checkUserPass(
    String username,
    String password
  ) {
    ServerResponse<Boolean> res = new AuthResponse();
    try {
      String query = "?username=" + username + "&password=" + password;
      URL url = new URI(URL + "auth" + query).toURL();
      HttpURLConnection conn = (HttpURLConnection) url.openConnection();

      conn.setRequestMethod("GET");
      conn.setDoOutput(true);

      int responseCode = conn.getResponseCode();
      BufferedReader in = new BufferedReader(
        new InputStreamReader(conn.getInputStream())
      );
      String response = in.readLine();
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
