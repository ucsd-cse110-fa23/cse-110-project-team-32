package client;

import static org.junit.jupiter.api.Assertions.*;

import client.HttpResponse.CreateRecipeResponse;
import client.HttpResponse.ServerResponse;
import java.io.File;
import java.util.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class HttpResponseTest {

  @Test
  void createRecipeResponse() {
    ServerResponse<Recipe> createRecipeRes = new CreateRecipeResponse();
    String mealType = "lunch";
    String ingredients = "chicken and eggs";
    String template = "%s # %s # DUMMY TITLE # DUMMY DESCRIPTION # ";
    createRecipeRes.setValidResponse(
      String.format(template, mealType, ingredients) +
      "https://www.allrecipes.com/thmb/iXKYAl17eIEnvhLtb4WxM7wKqTc=/1500x0/filters:no_upscale():max_bytes(150000):strip_icc()/240376-homemade-pepperoni-pizza-Beauty-3x4-1-6ae54059c23348b3b9a703b6a3067a44.jpg"
    );
    assertTrue(createRecipeRes.getResponse() instanceof Recipe);
    // check parsing (all fields are filled)
    Recipe recipe = createRecipeRes.getResponse();
    String recipeID = recipe.getRecipeID();
    assertEquals(recipe.getTitle(), "DUMMY TITLE");
    assertEquals(recipe.getRecipeDetail(), "DUMMY DESCRIPTION");
    assertEquals(recipe.getMealType(), "lunch");
    assertEquals(recipe.getImgPath(), "images/" + recipeID + ".img");

    File img = new File(recipe.getImgPath());
    // check if there is a new image file
    assertTrue(img.exists());
    assertFalse(img.isDirectory());
    // delete that file
    img.deleteOnExit();
  }
}
