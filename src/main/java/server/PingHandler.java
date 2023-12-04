package server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.io.OutputStream;

class PingHandler implements HttpHandler {

  // private final Helper HELPER = Helper.getInstance();
  private int statusCode = 200;

  @Override
  public void handle(HttpExchange httpExchange) throws IOException {
    // should only handle POST request
    String method = httpExchange.getRequestMethod();
    String response = "";
    statusCode = 200;
    try {
      if (method.equals(Constants.GET)) {
        response = handleGet(httpExchange);
      } else {
        // this will never happen,
        // the GET /ping route is only requested once in the beginning of the app
        statusCode = 404;
        response = Constants.INVALID_REQ_TO_ROUTE + "/ping";
      }
    } catch (Exception e) {
      // theoritically unreachable, all errors are returned gracefully in handlers
      System.out.println("There Is An Error In Request Received.");
      statusCode = 501;
      response = e.toString();
      e.printStackTrace();
    } finally {
      httpExchange.sendResponseHeaders(statusCode, response.length());
      OutputStream outStream = httpExchange.getResponseBody();
      outStream.write(response.getBytes());
      outStream.flush();
      outStream.close();
    }
  }

  public String handleGet(HttpExchange httpExchange) {
    try {
      return Constants.TRUE;
    } catch (Exception e) {
      statusCode = 404;
      return Constants.INVALID_GET_TO_ROUTE + "/auth";
    }
  }
}
