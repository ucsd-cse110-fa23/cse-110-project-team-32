package server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Helper {

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
