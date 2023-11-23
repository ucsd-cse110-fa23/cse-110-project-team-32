package client.CreateRecipeScene;

import client.HttpResponse.ServerResponse;
import client.HttpResponse.WhisperResponse;
import client.Recipe;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.http.HttpResponse;
import org.bson.io.OutputBuffer;
import org.json.JSONArray;
import org.json.JSONObject;

public class CreateRecipeModel {

  private static final String URL = "http://localhost:8100/";

  public CreateRecipeModel() {}

  public String translateByWhisper(String filePath) {
    ServerResponse res = new WhisperResponse();
    try {
      // Create file object from file path
      File file = new File(filePath);
      // Set up HTTP connection
      URL url = new URI(URL + "whisper").toURL();
      HttpURLConnection connection = (HttpURLConnection) url.openConnection();
      connection.setRequestMethod("POST");
      connection.setDoOutput(true);
      connection.setDoInput(true);
      // Set up output stream to write request body
      OutputStream outputStream = connection.getOutputStream();
      writeFileToOutputStream(outputStream, file);
      outputStream.close();

      int responseCode = connection.getResponseCode();
      BufferedReader in = new BufferedReader(
        new InputStreamReader(connection.getInputStream())
      );
      String inputLine;
      StringBuilder responseStr = new StringBuilder();
      while ((inputLine = in.readLine()) != null) {
        responseStr.append(inputLine);
      }
      in.close();

      if (responseCode == 200) {
        res.setValidResponse(responseStr.toString());
      } else {
        res.setErrorResponse(responseCode, responseStr.toString());
      }
      //   return res;
      return responseStr.toString();
    } catch (ConnectException connectionException) {
      System.out.println("[CreateRecipeModel.java] line 57");
      connectionException.printStackTrace();
      res.setServerDownResponse();
      //   return res;
      return "Server is Down";
    } catch (Exception e) {
      System.out.println("[CreateRecipeModel.java] line 82");
      e.printStackTrace();
      res.setErrorResponse(
        0,
        "[CreateRecipeModel.java line 71] This error is not handled properly"
      );
      //   return res;
      return "";
    }
  }

  // Helper method to write a file to the output stream in multipart form data format
  private static void writeFileToOutputStream(
    OutputStream outputStream,
    File file
    // String boundary
  ) throws IOException {
    // outputStream.write(("--" + boundary + "\r\n").getBytes());
    outputStream.write(
      (
        "Content-Disposition: form-data; name=\"file\"; filename=\"" +
        file.getName() +
        "\"\r\n"
      ).getBytes()
    );
    outputStream.write(("Content-Type: audio/mpeg\r\n\r\n").getBytes());
    FileInputStream fileInputStream = new FileInputStream(file);
    byte[] buffer = new byte[1024];
    int bytesRead;
    while ((bytesRead = fileInputStream.read(buffer)) != -1) {
      outputStream.write(buffer, 0, bytesRead);
    }
    fileInputStream.close();
  }

  public Recipe generateByChatGPT(String mealType, String ingredients) {
    try {
      // Set up HTTP connection
      URL url = new URI(URL + "chatgpt").toURL();
      HttpURLConnection connection = (HttpURLConnection) url.openConnection();
      connection.setRequestMethod("POST");
      connection.setDoOutput(true);
      connection.setDoInput(true);
      // Set up output stream to write request body
      String chatGptPrompt = createPrompt(mealType, ingredients);
      OutputStream outputStream = connection.getOutputStream();
      outputStream.write(chatGptPrompt.getBytes());
      outputStream.close();

      int responseCode = connection.getResponseCode();
      BufferedReader in = new BufferedReader(
        new InputStreamReader(connection.getInputStream())
      );
      String inputLine;
      StringBuilder response = new StringBuilder();
      while ((inputLine = in.readLine()) != null) {
        response.append(inputLine).append("\n");
      }
      in.close();

      // parse the response string
      String generatedText = response.toString();
      // System.out.println("[CreateRecipeModel.java ] line 117, server returns to model chatGPT output: " + generatedText);
      String[] colonSeparatedResultArray = generatedText.split("#");
      String title = colonSeparatedResultArray[0].strip();
      title = title.substring(0, title.length() - 1); // remove the ending comma
      String recipeDetail = colonSeparatedResultArray[1].strip();

      Recipe createdRecipe = new Recipe(title, mealType, recipeDetail);
      return createdRecipe;
    } catch (Exception e) {
      System.out.println("[CreateRecipeModel.java] line 126");
      e.printStackTrace();
      return null;
    }
  }

  public String createPrompt(String mealType, String ingredients) {
    return (
      "Give me a simple " +
      mealType +
      " recipe with the ingredients " +
      ingredients +
      ". In the form of: <Title>, character: '#' Ingredients: <Line broken ingredient list>, Instructions: <Line broken instruction list>" +
      ", do not include any chars over a byte long in size, and replace bullet points by '-'"
    );
  }

  public Recipe generateByChatGPT(
    String mealType,
    String ingredients,
    boolean isDummyRecipe
  ) {
    if (isDummyRecipe) {
      return generateByChatGPT(mealType, ingredients);
      // return new Recipe("Title", "Meal Type", "Ingredients");
    } else {
      return generateByChatGPT(mealType, ingredients);
    }
  }
}
