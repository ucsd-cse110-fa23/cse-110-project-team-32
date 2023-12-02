package client;

import java.io.Serializable;
import java.util.UUID;

public class Recipe implements Serializable {

  private String title;
  private String mealType;
  private String ingredients;
  private String recipeDetail;
  private String recipeID;
  private String imgPath;

  public Recipe() {}

  public Recipe(String title, String mealType, String recipe) {
    this.title = title;
    this.recipeDetail = recipe;
    this.mealType = mealType;
    this.recipeID = "recipe_" + UUID.randomUUID().toString();
  }

  public Recipe(String title, String mealType, String recipe, String imgPath) {
    this.title = title;
    this.recipeDetail = recipe;
    this.mealType = mealType;
    this.recipeID = "recipe_" + UUID.randomUUID().toString();
    this.imgPath = imgPath;
  }

  public Recipe(
    String recipeID,
    String title,
    String mealType,
    String recipe,
    String imgPath
  ) {
    this.title = title;
    this.recipeDetail = recipe;
    this.mealType = mealType;
    this.recipeID = recipeID;
    this.imgPath = imgPath;
  }

  public String getRecipeID() {
    return recipeID;
  }

  public void setRecipeID(String recipeID) {
    this.recipeID = recipeID;
  }

  public String getTitle() {
    return this.title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getRecipeDetail() {
    return this.recipeDetail;
  }

  public void setRecipeDetail(String recipeDetail) {
    this.recipeDetail = recipeDetail;
  }

  public String getMealType() {
    return this.mealType;
  }

  public void setMealType(String mealType) {
    this.mealType = mealType;
  }

  public String getImgPath() {
    return this.imgPath;
  }

  public void setImgPath(String imgPath) {
    this.imgPath = imgPath;
  }

  public String getIngredients() {
    return this.ingredients;
  }

  public void setIngredients(String ingredients) {
    this.ingredients = ingredients;
  }

  @Override
  public String toString() {
    return (
      recipeID +
      ", " +
      title +
      ", " +
      mealType +
      '\n' +
      ingredients +
      '\n' +
      recipeDetail +
      '\n' +
      imgPath
    );
  }
}
