package server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import org.json.JSONArray;
import org.json.JSONObject;

class AuthReqHandler implements HttpHandler {

  private final MongoDbOps MONGO_DB_OPS = MongoDbOps.getInstance();
  private final Helper HELPER = Helper.getInstance();

  @Override
  public void handle(HttpExchange httpExchange) throws IOException {
    // should only handle POST request
    String method = httpExchange.getRequestMethod();
    boolean isRequestValid = true;
    String response = "";
    try {
      if (method.equals("POST")) {
        response = handlePost(httpExchange);
      } else {
        isRequestValid = false;
        throw new Exception("ERROR: Invalid Request Method to Route /auth");
      }
    } catch (Exception e) {
      System.out.println("There Is An Error In Request Received.");
      isRequestValid = false;
      response = e.toString();
      e.printStackTrace();
    } finally {
      httpExchange.sendResponseHeaders(
        isRequestValid ? 200 : 404,
        response.length()
      );
      OutputStream outStream = httpExchange.getResponseBody();
      outStream.write(response.getBytes());
      outStream.flush();
      outStream.close();
    }
  }

  public String handlePost(HttpExchange httpExchange) throws IOException {
    // read the request body
    String reqBody = HELPER.readReqBody(httpExchange);
    int parserInd = reqBody.indexOf("#");
    String username = reqBody.substring(0, parserInd).split("=")[1];
    String password = reqBody.substring(parserInd + 1).split("=")[1];
    if (password.isEmpty()) return "false";
    String dbPassword = MONGO_DB_OPS.getUserPasswordByUsername(username);
    if (!password.equals(dbPassword)) return "false";
    return "true";
  }
}
