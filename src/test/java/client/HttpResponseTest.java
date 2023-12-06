package client;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import client.HttpResponse.AuthResponse;
import client.HttpResponse.CreateAccountResponse;
import client.HttpResponse.CreateRecipeResponse;
import client.HttpResponse.PingResponse;
import client.HttpResponse.RecipeListResponse;
import client.HttpResponse.ServerResponse;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

public class HttpResponseTest {

  @Test
  void createRecipeResponseTest() {
    ServerResponse<Recipe> createResponse = new CreateRecipeResponse();
    String mealType = "lunch";
    String ingredients = "DUMMY INGREDIENTS";
    String title = "DUMMY TITLE";
    String recipeDetail = "DUMMY DETAIL";
    String imgBase64Str =
      "iVBORw0KGgoAAAANSUhEUgAAAAgAAAAIAQMAAAD+wSzIAAAABlBMVEX///+/v7+jQ3Y5AAAADklEQVQI12P4AIX8EAgALgAD/aNpbtEAAAAASUVORK5CYII";
    createResponse.setValidResponse(
      "%s # %s # %s # %s # %s".formatted(
          mealType,
          ingredients,
          title,
          recipeDetail,
          imgBase64Str
        )
    );
    assertEquals(createResponse.getStatusCode(), 200);
    assertTrue(createResponse.getResponse() instanceof Recipe);
  }

  @Test
  void RecipeListResponseTest() {
    ServerResponse<List<Recipe>> recipeListResponse = new RecipeListResponse();

    recipeListResponse.setValidResponse("");

    assertEquals(recipeListResponse.getStatusCode(), 200);
    assertTrue(recipeListResponse.getResponse() instanceof List<Recipe>);
  }

  @Test
  void PingResponseSuccTest() {
    ServerResponse<Boolean> pingRes = new PingResponse();

    pingRes.setValidResponse("");

    assertEquals(pingRes.getStatusCode(), 200);
    assertTrue(pingRes.getResponse());
  }

  @Test
  void PingResponseFailTest() {
    ServerResponse<Boolean> pingRes = new PingResponse();
    String errorMsg = "Error Message!";
    pingRes.setErrorResponse(501, errorMsg);

    assertEquals(pingRes.getStatusCode(), 501);
    assertFalse(pingRes.getResponse());
    assertEquals(pingRes.getErrorMsg(), errorMsg);
  }

  @Test
  void ServerDownResponseTest(){
      //Server is Down when opening Application
      ServerResponse<Boolean> logInResponse = new AuthResponse();
      logInResponse.setServerDownResponse();
      assertEquals("The server is Down!", logInResponse.getErrorMsg());
      assertEquals(503, logInResponse.getStatusCode());
  }

  @Test 
  void ServerDownCreateAccTest(){
          //Server is Down when creating your own account
      ServerResponse<String> createAccResponse = new CreateAccountResponse();
      createAccResponse.setServerDownResponse();
      assertEquals("The server is Down!", createAccResponse.getErrorMsg());
      assertEquals(503, createAccResponse.getStatusCode());
  }

  
}
