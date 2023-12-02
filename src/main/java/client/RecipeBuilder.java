package client;

import java.util.UUID;

public class RecipeBuilder {

  private Recipe recipe;

  public RecipeBuilder() {
    recipe = new Recipe();
  }

  public RecipeBuilder addRecipeID(String ID) {
    recipe.setRecipeID(ID);
    return this;
  }

  public RecipeBuilder addTitle(String title) {
    recipe.setTitle(title);
    return this;
  }

  public RecipeBuilder addMealType(String mealType) {
    recipe.setMealType(mealType);
    return this;
  }

  public RecipeBuilder addIngredients(String ingredients) {
    recipe.setIngredients(ingredients);
    return this;
  }

  public RecipeBuilder addRecipeDetail(String recipeDetail) {
    recipe.setRecipeDetail(recipeDetail);
    return this;
  }

  public RecipeBuilder addImgBase64Str(String imgBase64Str) {
    recipe.setImgBase64Str(imgBase64Str);
    return this;
  }

  public Recipe getRecipe() {
    if (recipe.getRecipeID() == null) {
      recipe.setRecipeID("recipe_" + UUID.randomUUID().toString());
    }
    return this.recipe;
  }
}
