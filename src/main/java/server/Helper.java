package server;

import com.sun.javafx.scene.control.skin.InputFieldSkin;
import com.sun.net.httpserver.HttpExchange;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Helper {

  private static final Helper HELPER = new Helper();

  private Helper() {}

  public static Helper getInstance() {
    return HELPER;
  }

  public String readReqBody(HttpExchange httpExchange) throws IOException {
    // read the request body
    return readInputStream(httpExchange.getRequestBody());
  }

  public String readInputStream(InputStream inStream) throws IOException {
    BufferedReader in = new BufferedReader(new InputStreamReader(inStream));
    String inputLine;
    StringBuilder reqBody = new StringBuilder();
    while ((inputLine = in.readLine()) != null) {
      reqBody.append(inputLine + '\n');
    }
    reqBody.deleteCharAt(reqBody.length() - 1);
    in.close();
    return reqBody.toString();
  }
}
