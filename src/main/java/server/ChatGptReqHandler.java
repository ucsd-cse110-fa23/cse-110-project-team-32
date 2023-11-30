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

public class ChatGptReqHandler implements HttpHandler {

  private static final String API_ENDPOINT =
    "https://api.openai.com/v1/completions";
  private static final String API_KEY =
    "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";
  private static final String MODEL = "text-davinci-003";
  private static final int MAX_TOKENS = 300;

  public ChatGptReqHandler() {}

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

  public String handlePost(HttpExchange httpExchange)
    throws IOException, URISyntaxException, InterruptedException {
    BufferedReader in = new BufferedReader(
      new InputStreamReader(httpExchange.getRequestBody())
    );
    String inputLine;
    StringBuilder req = new StringBuilder();
    while ((inputLine = in.readLine()) != null) {
      req.append(inputLine);
    }
    in.close();

    String chatGptPrompt = req.toString();
    JSONObject requestBody = new JSONObject();
    requestBody.put("model", MODEL);
    requestBody.put("prompt", chatGptPrompt);
    requestBody.put("max_tokens", MAX_TOKENS);
    requestBody.put("temperature", 1.0);
    // Create the HTTP Client
    HttpClient client = HttpClient.newHttpClient();
    // Create the request object
    HttpRequest request = HttpRequest
      .newBuilder()
      .uri(new URI(API_ENDPOINT))
      .header("Content-Type", "application/json")
      .header("Authorization", String.format("Bearer %s", API_KEY))
      .POST(HttpRequest.BodyPublishers.ofString(requestBody.toString()))
      .build();

    // Send the request and receive the response
    HttpResponse<String> response = client.send(
      request,
      HttpResponse.BodyHandlers.ofString()
    );
    // Process the response
    String responseBody = response.body();
    // System.out.println("ChatGPT gives server: " + responseBody);
    JSONObject responseJson = new JSONObject(responseBody);
    JSONArray choices = responseJson.getJSONArray("choices");
    String generatedText = choices.getJSONObject(0).getString("text").strip();
    System.out.println("ChatGPT gives server: " + generatedText);
    return generatedText;
  }
}
