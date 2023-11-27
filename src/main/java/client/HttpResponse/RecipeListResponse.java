package client.HttpResponse;

import client.Recipe;
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
    String[] stringRecipeList = res.split("#");
    for (String recipeString : stringRecipeList) {
      String[] recipeComponents = recipeString.split(";");
      recipeList.add(
        new Recipe(
          recipeComponents[0],
          recipeComponents[1],
          recipeComponents[2],
          recipeComponents[3].replace("\\n", "\n")
        )
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
}
