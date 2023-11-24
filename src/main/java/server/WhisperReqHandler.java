package server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.http.HttpRequest;
import java.util.Scanner;
import org.json.JSONException;
import org.json.JSONObject;

public class WhisperReqHandler implements HttpHandler {

  private static final String API_ENDPOINT =
    "https://api.openai.com/v1/audio/transcriptions";
  private static final String TOKEN =
    "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";
  private static final String MODEL = "whisper-1";

  public WhisperReqHandler() {}

  public void handle(HttpExchange httpExchange) throws IOException {
    String response = "Request Received";
    String method = httpExchange.getRequestMethod();
    boolean isRequestValid = true;
    try {
      if (method.equals("POST")) {
        response = handlePost(httpExchange);
      } else {
        isRequestValid = false;
        throw new Exception("Invalid Request Method");
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
