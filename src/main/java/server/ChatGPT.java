package server;

import client.API;
import client.Recipe;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import org.json.JSONArray;
import org.json.JSONObject;

public class ChatGPT implements API {

  private String API_ENDPOINT; // = "https://api.openai.com/v1/completions";
  private String API_KEY; // = "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";
  private String MODEL; // = "text-davinci-003";
  private String recipeTitle;
  private String prompt;
  private String text;

  public ChatGPT() {
    API_ENDPOINT = "https://api.openai.com/v1/completions";
    API_KEY = "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";
    MODEL = "text-davinci-003";
  }

  public String createPrompt(String mealType, String ingredients) {
    return (
      "Give me a simple " +
      mealType +
      " recipe with the ingredients " +
      ingredients +
      ". Please give the result in the form of: <Title>, # Ingredients: <Line broken ingredient list>, Instructions: <Line broken instruction list>" +
      "do not include any chars over a byte long in size"
    );
  }

  public void setInput(String input) {
    this.prompt = input;
  }

  public String getOutput() {
    return this.text;
  }

  public Recipe generate(String mealType, String ingredients) {
    // Set request parameters
    int maxTokens = 300;
    String prompt =
      "Give me a simple " +
      mealType +
      " recipe with the ingredients " +
      ingredients +
      ". Please give the result in the form of: <Title>, # Ingredients: <Line broken ingredient list>, Instructions: <Line broken instruction list>" +
      "do not include any chars over a byte long in size";

    System.out.println(maxTokens + ", " + prompt);

    // Create a request body which you will pass into request object
    JSONObject requestBody = new JSONObject();
    requestBody.put("model", MODEL);
    requestBody.put("prompt", prompt);
    requestBody.put("max_tokens", maxTokens);
    requestBody.put("temperature", 1.0);

    Recipe createdRecipe = null;
    try {
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

      JSONObject responseJson = new JSONObject(responseBody);
      JSONArray choices = responseJson.getJSONArray("choices");
      String generatedText = choices.getJSONObject(0).getString("text").strip();
      this.text = generatedText;
      // System.out.println("==========");
      // System.out.println(generatedText);
      // System.out.println("==========");
      // int firstLineBreakPos = generatedText.indexOf('\n');
      String[] colonSeparatedResultArray = generatedText.split("#");
      String title = colonSeparatedResultArray[0].strip();
      title = title.substring(0, title.length() - 1); // remove the ending comma
      String recipeDetail = colonSeparatedResultArray[1].strip();
      createdRecipe = new Recipe(title, mealType, recipeDetail);
      // recipeTitle = title;
      System.out.println(
        "ChatGPT.java line 86:" +
        "Title: " +
        title +
        "\n, Meal Type: " +
        mealType +
        "\n, Recipe Detail: " +
        recipeDetail
      );
    } catch (Exception e) {
      System.out.println(
        "ChatGPT.java line 88. SOMETHING WENT WRONG ON GENERATE"
      );
    }
    return createdRecipe;
  }

  public Recipe generate(String mealType, String ingredients, boolean isDummy) {
    if (isDummy) {
      return new Recipe("This is a fake recipe", mealType, ingredients);
    } else {
      return generate(mealType, ingredients);
    }
  }
}
