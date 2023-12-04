package client;

import java.io.Serializable;
import java.util.UUID;

public class Recipe implements Serializable {

  private String title;
  private String mealType;
  private String ingredients;
  private String recipeDetail;
  private String recipeID;
  private String imgBase64Str;
  private int index;

  public Recipe() {}

  public Recipe(String title, String mealType, String recipe) {
    this.title = title;
    this.recipeDetail = recipe;
    this.mealType = mealType;
    this.recipeID = "recipe_" + UUID.randomUUID().toString();
  }

  public Recipe(
    String title,
    String mealType,
    String recipe,
    String imgBase64Str
  ) {
    this.title = title;
    this.recipeDetail = recipe;
    this.mealType = mealType;
    this.recipeID = "recipe_" + UUID.randomUUID().toString();
    this.imgBase64Str = imgBase64Str;
  }

  public Recipe(
    String recipeID,
    String title,
    String mealType,
    String recipe,
    String imgBase64Str
  ) {
    this.title = title;
    this.recipeDetail = recipe;
    this.mealType = mealType;
    this.recipeID = recipeID;
    this.imgBase64Str = imgBase64Str;
  }

  public int getIndex() {
    return this.index;
  }

  public void setIndex(int ind) {
    this.index = ind;
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

  public String getImgBase64Str() {
    return this.imgBase64Str;
  }

  public void setImgBase64Str(String imgBase64Str) {
    this.imgBase64Str = imgBase64Str;
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
      "First 10 chars of img base64 string: " +
      imgBase64Str.substring(0, 10)
    );
  }
}
