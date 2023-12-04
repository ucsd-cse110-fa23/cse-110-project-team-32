package server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;

class AuthReqHandler implements HttpHandler {

  private final MongoDbOps MONGO_DB_OPS = MongoDbOps.getInstance();
  private final Helper HELPER = Helper.getInstance();
  private int statusCode = 200; //How do get status code from CreateAccModel?

  @Override
  public void handle(HttpExchange httpExchange) throws IOException {
    // should only handle POST request
    String method = httpExchange.getRequestMethod();
    String response = "";
    statusCode = 200;
    try {
      if (method.equals(Constants.GET)) {
        response = handleGet(httpExchange);
      } else if (method.equals(Constants.POST)) {
        response = handlePost(httpExchange);
      } else {
        statusCode = 404;
        System.out.println("LINE 28 AUTHREQHANDLER");
        response = Constants.INVALID_REQ_TO_ROUTE + "/auth";
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
    // decode request
    try {
      URI uri = httpExchange.getRequestURI();
      String query = uri.getRawQuery(); 
      String[] usernameAndPassword = query.split("&"); //?=Java, ?=
      String username = usernameAndPassword[0].split("=")[1];
      String password = usernameAndPassword[1].split("=")[1];
      if (
        username == null ||
        username.isEmpty() ||
        password == null ||
        password.isEmpty()
      ) {
        System.out.println("User: " + username);
        System.out.println("Password: " + password);
        statusCode = 404;
        return Constants.INVALID_GET_TO_ROUTE + "/auth";
      }
      if(MONGO_DB_OPS.getRecipesByUserID(username) == null){
        statusCode = 501;
        return "Incorrect Username/Password!";
      }

      String mongoPassword = MONGO_DB_OPS.getUserPasswordByUsername(username);
      if (mongoPassword.equals(password)) {
        return Constants.TRUE;
      } else {
        statusCode = 501;
        return "Incorrect Username/Password!";
      }


    } catch (Exception e) {
      statusCode = 404;
      System.out.println("LINE 80 AUTHREQHANDLER");
      return Constants.INVALID_GET_TO_ROUTE + "/auth";
    }
  }

  public String handlePost(HttpExchange httpExchange) {
    // read the request body
    try {
      String reqBody = HELPER.readReqBody(httpExchange);
      int parserInd = reqBody.indexOf("&");
      String username = reqBody.substring(0, parserInd).split("=")[1];
      String password = reqBody.substring(parserInd + 1).split("=")[1];
      if (username.isEmpty()) return Constants.FALSE;
      if (password.isEmpty()) return Constants.FALSE;
      // String dbPassword = MONGO_DB_OPS.getUserPasswordByUsername(username);
      boolean isCreateUserSuccessful = MONGO_DB_OPS.createUser(
        username,
        password
      );
      if (isCreateUserSuccessful) return Constants.TRUE;
      statusCode = 501;
      return "Username Already Exists!";
    } catch (Exception e) {
      e.printStackTrace();
      statusCode = 404;
      return Constants.INVALID_POST_TO_ROUTE + "/auth";
    }
  }

}
