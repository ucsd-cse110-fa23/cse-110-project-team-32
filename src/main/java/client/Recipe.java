package client;
import java.util.UUID;

public class Recipe {
    private String title;
    private String mealType; 
    private String recipeDetail;
    private String recipeID;

    public Recipe(String title, String mealType, String recipe) {
        this.title = title;
        this.recipeDetail = recipe;
        this.mealType = mealType;
        this.recipeID = "recipe_" + UUID.randomUUID().toString();
    }

    public Recipe(String recipeID, String title, String mealType, String recipe) {
        this.title = title;
        this.recipeDetail = recipe;
        this.mealType = mealType;
        this.recipeID = recipeID;
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

    @Override
    public String toString() {
        return recipeID + ", " + title + ", " + mealType + '\n' + recipeDetail;
    }
}
