package server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Paths;
import java.util.Base64;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ChatGptReqHandler implements HttpHandler {

  private static final String API_ENDPOINT =
    "https://api.openai.com/v1/completions";
  private static final String API_KEY =
  "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";
  private static final String MODEL = "text-davinci-003";
  private static final int MAX_TOKENS = 300;

  private static final String DALLE_MODEL = "dall-e-2";
  private static final String DALLE_API_ENDPOINT =
    "https://api.openai.com/v1/images/generations";
  private static final String DALLE_API_KEY =
  "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";

  private final Helper HELPER = Helper.getInstance();
  private int statusCode = 200;

  public ChatGptReqHandler() {}

  public void handle(HttpExchange httpExchange) throws IOException {
    statusCode = 200;
    String response = "Request Received";
    String method = httpExchange.getRequestMethod();
    try {
      if (method.equals(Constants.POST)) {
        response = handlePost(httpExchange);
      } else {
        statusCode = 404;
        response = Constants.INVALID_REQ_TO_ROUTE + "/genrecipe";
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

  public String handlePost(HttpExchange httpExchange) {
    // returns recipe from chatGPT and image URL from DALL E
    try {
      // 1. read request body
      String chatGptPrompt = HELPER.readReqBody(httpExchange);

      // 2. write request body for ChatGPT endpoint
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

      // 3. Send the request to ChatGPT and receive the response
      HttpResponse<String> response = client.send(
        request,
        HttpResponse.BodyHandlers.ofString()
      );
      // 4. Process the response
      String responseBody = response.body();
      JSONObject responseJson = new JSONObject(responseBody);
      JSONArray choices = responseJson.getJSONArray("choices");
      String generatedText = choices.getJSONObject(0).getString("text").strip();
      // System.out.println("ChatGPT gives server: " + generatedText);
      String recipeTitle = generatedText.substring(
        0,
        generatedText.indexOf("#") - 1
      );
      String imageURL = requestImageFromDallE(recipeTitle);

      InputStream in = new URI(imageURL).toURL().openStream();
      String imgInBase64Str = Base64
        .getEncoder()
        .encodeToString(in.readAllBytes());
      // System.out.println("==================");
      // System.out.println(imgInBase64Str);
      // System.out.println("==================");
      return generatedText + '#' + imgInBase64Str;
    } catch (Exception e) {
      statusCode = 501;
      return "An Unexpected Error Happened with Our AI";
    }
  }

  private String requestImageFromDallE(String recipeTitle)
    throws IOException, InterruptedException, JSONException, URISyntaxException {
    // Create a request body which you will pass into request object
    JSONObject requestBody = new JSONObject();
    requestBody.put("model", DALLE_MODEL);
    requestBody.put("prompt", recipeTitle);
    requestBody.put("n", 1);
    requestBody.put("size", "256x256");

    // Create the HTTP client
    HttpClient client = HttpClient.newHttpClient();

    // Create the request object
    HttpRequest request = HttpRequest
      .newBuilder()
      .uri(URI.create(DALLE_API_ENDPOINT))
      .header("Content-Type", "application/json")
      .header("Authorization", String.format("Bearer %s", DALLE_API_KEY))
      .POST(HttpRequest.BodyPublishers.ofString(requestBody.toString()))
      .build();

    // Send the request and receive the response
    HttpResponse<String> response = client.send(
      request,
      HttpResponse.BodyHandlers.ofString()
    );

    // Process the response
    String responseBody = response.body();

    JSONObject responseJson = new JSONObject(responseBody);
    // System.out.println(responseJson);
    // TODO: Process the response
    String generatedImageURL = responseJson
      .getJSONArray("data")
      .getJSONObject(0)
      .getString("url");
    // System.out.println("DALL-E Response:");
    // System.out.println(generatedImageURL);
    return generatedImageURL;
  }
}
