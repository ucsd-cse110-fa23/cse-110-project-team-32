package client;

public class Recipe {
    private String title;
    private String mealType; 
    private String recipeDetail;

    public Recipe(String title, String mealType, String recipe) {
        this.title = title;
        this.recipeDetail = recipe;
        this.mealType = mealType;
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
        return title + ", " + mealType + '\n' + recipeDetail;
    }
}
