package server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import org.json.JSONException;
import org.json.JSONObject;

public class WhisperReqHandler implements HttpHandler {

  private static final String API_ENDPOINT =
    "https://api.openai.com/v1/audio/transcriptions";
  private static final String TOKEN =
  "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";
  private static final String MODEL = "whisper-1";
  private int statusCode = 200;

  public WhisperReqHandler() {}

  public void handle(HttpExchange httpExchange) throws IOException {
    String response = "Request Received";
    statusCode = 200;
    String method = httpExchange.getRequestMethod();
    try {
      if (method.equals(Constants.POST)) {
        response = handlePost(httpExchange);
      } else {
        statusCode = 404;
        response = Constants.INVALID_REQ_TO_ROUTE + " /translate";
      }
    } catch (Exception e) {
      System.out.println("There Is An Error In Request Received.");
      statusCode = 503;
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

  private String handlePost(HttpExchange httpExchange)
    throws IOException, URISyntaxException {
    // Set up HTTP connection
    URL url = new URI(API_ENDPOINT).toURL();
    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
    connection.setRequestMethod("POST");
    connection.setDoOutput(true);

    // set header
    String boundary = "Boundary-" + System.currentTimeMillis();
    connection.setRequestProperty(
      "Content-Type",
      "multipart/form-data; boundary=" + boundary
    );
    connection.setRequestProperty("Authorization", "Bearer " + TOKEN);

    // Set up output stream to write request body
    OutputStream outputStream = connection.getOutputStream();
    // write body part 1
    // Write model parameter to request body
    writeParameterToOutputStream(outputStream, "model", MODEL, boundary);

    outputStream.write(("--" + boundary + "\r\n").getBytes());
    // write body part 2: the req body sent to this server
    InputStream inStream = httpExchange.getRequestBody();
    printInStreamAndLeaveOpen(inStream);
    inStream.transferTo(outputStream);
    inStream.close();
    // write body part 3:
    outputStream.write(("\r\n--" + boundary + "--\r\n").getBytes());
    // Flush and close output stream
    outputStream.flush();
    outputStream.close();

    // Get response code
    int responseCode = connection.getResponseCode();
    // Check response code and handle response accordingly
    String resultText;
    if (responseCode == HttpURLConnection.HTTP_OK) {
      resultText = handleSuccessResponse(connection);
    } else {
      // resultText = "Error result! Please try again :>"; // handleErrorResponse(connection);
      resultText = handleErrorResponse(connection);
    }
    // Disconnect connection
    connection.disconnect();
    System.out.println(
      "[WhisperReqHandler.java] whisper sends to server: " + resultText
    );
    return resultText;
  }

  private static void writeParameterToOutputStream(
    OutputStream outputStream,
    String parameterName,
    String parameterValue,
    String boundary
  ) throws IOException {
    outputStream.write(("--" + boundary + "\r\n").getBytes());
    outputStream.write(
      (
        "Content-Disposition: form-data; name=\"" + parameterName + "\"\r\n\r\n"
      ).getBytes()
    );
    outputStream.write((parameterValue + "\r\n").getBytes());
  }

  // Helper method to handle a successful response
  private String handleSuccessResponse(HttpURLConnection connection)
    throws IOException, JSONException {
    BufferedReader in = new BufferedReader(
      new InputStreamReader(connection.getInputStream())
    );
    String inputLine;
    StringBuilder response = new StringBuilder();
    while ((inputLine = in.readLine()) != null) {
      response.append(inputLine);
    }
    in.close();
    JSONObject responseJson = new JSONObject(response.toString());

    String generatedText = responseJson.getString("text").toLowerCase();
    generatedText =
      generatedText.charAt(generatedText.length() - 1) == '.'
        ? generatedText.substring(0, generatedText.indexOf('.'))
        : generatedText;
    System.out.println("Whisper output: |" + generatedText + "|");
    return generatedText;
  }

  // Helper method to handle an error response
  private String handleErrorResponse(HttpURLConnection connection)
    throws IOException, JSONException {
    BufferedReader errorReader = new BufferedReader(
      new InputStreamReader(connection.getErrorStream())
    );
    String errorLine;
    StringBuilder errorResponse = new StringBuilder();
    while ((errorLine = errorReader.readLine()) != null) {
      errorResponse.append(errorLine);
    }
    errorReader.close();
    String errorResult = errorResponse.toString();
    return "Error Result: " + errorResult;
  }

  private void printInStreamAndLeaveOpen(InputStream inStream) {
    System.out.println(inStream);
  }
}
