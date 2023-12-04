package client.HttpResponse;

import client.Recipe;
import client.RecipeBuilder;
import java.util.UUID;

public class CreateRecipeResponse implements ServerResponse<Recipe> {

  private String errorMsg;
  private int statusCode;
  private Recipe recipe;

  public CreateRecipeResponse() {
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
  public Recipe getResponse() {
    return recipe;
  }

  @Override
  public void setValidResponse(String res) {
    statusCode = 200;
    errorMsg = null;
    String[] hashSeparatedResultArray = res.split("#");
    String mealType = hashSeparatedResultArray[0].strip();
    String ingredients = hashSeparatedResultArray[1].strip();
    String title = hashSeparatedResultArray[2].strip();
    String recipeDetail = hashSeparatedResultArray[3].strip();
    String imgBase64Str = hashSeparatedResultArray[4].strip();

    String recipeID = "recipe_" + UUID.randomUUID().toString();

    // // ensures a images folder exists (for new users only)
    // new File("images").mkdir();
    // String imgPath = "images/" + recipeID + ".jpg";
    // try (InputStream in = new URI(imgURL).toURL().openStream()) {
    //   Files.copy(in, Paths.get(imgPath));
    // } catch (Exception e) {}
    System.out.println(
      "RecipeID: " +
      recipeID +
      '\n' +
      "mealType: " +
      mealType +
      '\n' +
      "Ingredients: " +
      ingredients +
      '\n' +
      "title: " +
      title +
      '\n' +
      "recipeDetail: " +
      recipeDetail +
      '\n' +
      "imgBase64Str: " +
      imgBase64Str.substring(0, 30) +
      '\n'
    );
    recipe =
      new RecipeBuilder()
        .addRecipeID(recipeID)
        .addTitle(title)
        .addMealType(mealType)
        .addIngredients(ingredients)
        .addRecipeDetail(recipeDetail)
        .addImgBase64Str(imgBase64Str)
        .getRecipe();
  }

  @Override
  public void setErrorResponse(int statusCode, String err) {
    this.statusCode = statusCode;
    this.errorMsg = err;
    this.recipe = null;
  }

  @Override
  public void setServerDownResponse() {
    this.statusCode = 503;
    this.errorMsg = "The server is Down!";
  }

  @Override
  public String toString() {
    String temp = "Create Recipe Response: \nStatus Code: %d \nError Msg: %s";
    return String.format(temp, statusCode, errorMsg);
  }
}
