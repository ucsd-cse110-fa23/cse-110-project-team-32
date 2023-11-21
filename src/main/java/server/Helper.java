package server;

import com.sun.net.httpserver.HttpExchange;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Helper {

  private static final Helper HELPER = new Helper();

  private Helper() {}

  public static Helper getInstance() {
    return HELPER;
  }

  public String readReqBody(HttpExchange httpExchange) throws IOException {
    // read the request body
    BufferedReader in = new BufferedReader(
      new InputStreamReader(httpExchange.getRequestBody())
    );
    String inputLine;
    StringBuilder reqBody = new StringBuilder();
    while ((inputLine = in.readLine()) != null) {
      reqBody.append('\n' + inputLine);
    }
    in.close();
    return reqBody.toString();
  }
}
