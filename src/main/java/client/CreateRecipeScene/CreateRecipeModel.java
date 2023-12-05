package client.CreateRecipeScene;

import client.HttpResponse.CreateRecipeResponse;
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

  public ServerResponse<String> translateByWhisper(String filePath) {
    ServerResponse<String> res = new WhisperResponse();
    try {
      // Create file object from file path
      File file = new File(filePath);
      // Set up HTTP connection
      URL url = new URI(URL + "translate").toURL();
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
      return res;
    } catch (ConnectException connectionException) {
      System.out.println("[CreateRecipeModel.java] line 57");
      connectionException.printStackTrace();
      res.setServerDownResponse();
      return res;
    } catch (Exception e) {
      System.out.println("[CreateRecipeModel.java] line 82");
      e.printStackTrace();
      res.setErrorResponse(
        0,
        "[CreateRecipeModel.java line 71] This error is not handled properly"
      );
      return res;
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

  public ServerResponse<Recipe> generateByChatGPT(
    String mealType,
    String ingredients
  ) {
    ServerResponse<Recipe> res = new CreateRecipeResponse();
    try {
      // Set up HTTP connection
      URL url = new URI(URL + "genrecipe").toURL();
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
      if (responseCode == 200) {
        BufferedReader in = new BufferedReader(
          new InputStreamReader(connection.getInputStream())
        );
        String inputLine;
        StringBuilder response = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
          response.append(inputLine).append("\n");
        }
        in.close();
        // response contains # separated title AND recipe Detail AND image as file content in String type
        String responseText =
          mealType + "#" + ingredients + "#" + response.toString();
        // System.out.println("response text from server: " + responseText);
        res.setValidResponse(responseText);
      } else {
        BufferedReader in = new BufferedReader(
          new InputStreamReader(connection.getErrorStream())
        );
        String inputLine;
        StringBuilder response = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
          response.append(inputLine).append("\n");
        }
        in.close();
        res.setErrorResponse(responseCode, response.toString());
      }
      return res;
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
      ". In the form of: <Title> followed by a single '#', Ingredients: <Line broken ingredient list>, Instructions: <Line broken instruction list>" +
      ", do not include any chars over a byte long in size, and replace bullet points by '-'. Don't include any ; characters"
    );
  }

  public ServerResponse<Recipe> generateByChatGPT(
    String mealType,
    String ingredients,
    boolean isDummyRecipe
  ) {
    if (isDummyRecipe) {
      CreateRecipeResponse createRecipeRes = new CreateRecipeResponse();
      // mealType, ingredients, title, detail, url
      String template = "%s # %s # DUMMY TITLE # DUMMY DESCRIPTION # ";
      createRecipeRes.setValidResponse(
        String.format(template, mealType, ingredients) +
        "iVBORw0KGgoAAAANSUhEUgAAAAgAAAAIAQMAAAD+wSzIAAAABlBMVEX///+/v7+jQ3Y5AAAADklEQVQI12P4AIX8EAgALgAD/aNpbtEAAAAASUVORK5CYII"
      );
      return createRecipeRes;
    }
    return generateByChatGPT(mealType, ingredients);
  }
}
