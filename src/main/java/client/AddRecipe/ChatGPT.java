package client.AddRecipe;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import org.json.JSONArray;
import org.json.JSONObject;

import client.Recipe;

public class ChatGPT implements API{
    private String API_ENDPOINT;// = "https://api.openai.com/v1/completions";
    private String API_KEY;// = "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";
    private String MODEL;// = "text-davinci-003";
    private String recipeTitle;

    public ChatGPT(){
        API_ENDPOINT = "https://api.openai.com/v1/completions";
        API_KEY = "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";
        MODEL = "text-davinci-003";
    }

    public API initializeAPI(String API_ENDPOINT, String API_KEY, String MODEL){
        this.API_ENDPOINT = API_ENDPOINT;
        this.API_KEY = API_KEY;
        this.MODEL = MODEL;
        return this;
    }

    public String getInfo(){
        return this.recipeTitle;
    }

    public Recipe generate(String mealType, String ingredients) throws IOException, InterruptedException, URISyntaxException {
// Set request parameters
        int maxTokens = 300;
        String prompt = "Give me a simple " + mealType + " recipe with the ingredients " + ingredients + ". Give me the title of the dish on the first line";

        System.out.println(maxTokens + ", " + prompt);

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

        JSONObject responseJson = new JSONObject(responseBody);
        JSONArray choices = responseJson.getJSONArray("choices");
        String generatedText = choices.getJSONObject(0).getString("text").strip();

        System.out.println("==========");
        System.out.println(generatedText);
        System.out.println("==========");
        // int firstLineBreakPos = generatedText.indexOf('\n');
        String title = generatedText.substring(0, generatedText.indexOf('\n'));
        String recipeDetail = generatedText.substring(generatedText.indexOf('\n')+1);
        Recipe createdRecipe = new Recipe(title, mealType, recipeDetail);
        recipeTitle = title;
        System.out.println("ChatGPT.java line 61:" + "Title: " + title + "Meal Type: " + mealType + ", recipe detail: " + recipeDetail);
        return createdRecipe;
    }

}
