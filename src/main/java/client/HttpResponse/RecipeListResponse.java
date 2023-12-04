package client.HttpResponse;

import client.Recipe;
import client.RecipeBuilder;
import java.util.ArrayList;
import java.util.List;

public class RecipeListResponse implements ServerResponse<List<Recipe>> {

  private String errorMsg;
  private int statusCode;
  private List<Recipe> recipeList;

  public RecipeListResponse() {
    recipeList = new ArrayList<>();
    errorMsg = null;
  }

  @Override
  public int getStatusCode() {
    return statusCode;
  }

  @Override
  public String getErrorMsg() {
    return this.errorMsg;
  }

  @Override
  public List<Recipe> getResponse() {
    return recipeList;
  }

  @Override
  public void setValidResponse(String res) {
    statusCode = 200;
    errorMsg = null;
    if (res == null || res.isEmpty()) return;
    String[] stringRecipeList = res.split("#");
    for (String recipeString : stringRecipeList) {
      // System.out.println(recipeString);
      String[] recipeComponents = recipeString.split(";");
      recipeList.add(
        new RecipeBuilder()
          .addRecipeID(recipeComponents[0])
          .addTitle(recipeComponents[1])
          .addMealType(recipeComponents[2])
          .addRecipeDetail(recipeComponents[3].replace("\\n", "\n"))
          .addImgBase64Str(recipeComponents[4])
          .getRecipe()
      );
    }
  }

  @Override
  public void setErrorResponse(int statusCode, String err) {
    this.statusCode = statusCode;
    this.errorMsg = err;
  }

  @Override
  public void setServerDownResponse() {
    this.statusCode = 503;
    this.errorMsg = "The server is Down!";
  }

  @Override
  public String toString() {
    String temp =
      "Read All Recipes Response: \nStatus Code: %d \nError Msg: %s \nLoaded Recipes: %s";
    return String.format(
      temp,
      statusCode,
      errorMsg,
      recipeList.isEmpty() ? "Empty Recipe List" : "Non-empty Recipe List"
    );
  }
}
