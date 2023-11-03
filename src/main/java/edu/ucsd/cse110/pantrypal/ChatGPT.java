package main.java.edu.ucsd.cse110.pantrypal;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.IOException;

public class ChatGPT {

private static final String API_ENDPOINT = "https://api.openai.com/v1/completions";
private static final String API_KEY = "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";
private static final String MODEL = "text-davinci-003";

public static void main(String[] args) throws IOException, InterruptedException, URISyntaxException {


    // Set request parameters
    
    int maxTokens = Integer.parseInt(args[0]);
    String prompt = args[1];
    // Create a request body which you will pass into request object
    JSONObject requestBody = new JSONObject();
    requestBody.put("model", MODEL);
    requestBody.put("prompt", prompt);
    requestBody.put("max_tokens", maxTokens);
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

    //System.out.println(responseBody);

    JSONObject responseJson = new JSONObject(responseBody);

    JSONArray choices = responseJson.getJSONArray("choices");
    String generatedText = choices.getJSONObject(0).getString("text");

    System.out.println(generatedText);

    }

    


}

