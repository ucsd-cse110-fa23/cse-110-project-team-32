package client;

import java.io.Serializable;
import java.util.UUID;

public class Recipe implements Serializable {

  private String title;
  private String mealType;
  private String recipeDetail;
  private String recipeID;
  private String imgURL;

  public Recipe(String title, String mealType, String recipe) {
    this.title = title;
    this.recipeDetail = recipe;
    this.mealType = mealType;
    this.recipeID = "recipe_" + UUID.randomUUID().toString();
  }

  public Recipe(String title, String mealType, String recipe, String imgURL) {
    this.title = title;
    this.recipeDetail = recipe;
    this.mealType = mealType;
    this.recipeID = "recipe_" + UUID.randomUUID().toString();
    this.imgURL = imgURL;
  }

  public Recipe(
    String recipeID,
    String title,
    String mealType,
    String recipe,
    String imgURL
  ) {
    this.title = title;
    this.recipeDetail = recipe;
    this.mealType = mealType;
    this.recipeID = recipeID;
    this.imgURL = imgURL;
  }

  public String getRecipeID() {
    return recipeID;
  }

  public String getTitle() {
    return this.title;
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

  public String getImgURL() {
    return this.imgURL;
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
      recipeDetail +
      '\n' +
      imgURL
    );
  }
}
