package client.RecipeDetail;

import client.Recipe;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class RecipeDetail extends VBox {
    // private Recipe recipe;

    public RecipeDetail() {

    }
    public void loadRecipeDetail(Recipe recipe) {
        // convert to text field for updating in the next iteration
        Text recipeTitle = new Text(recipe.getTitle());
        Text recipeMealType = new Text(recipe.getMealType());
        Text recipeDetail = new Text(recipe.getRecipeDetail());

        this.getChildren().clear();
        this.getChildren().addAll(recipeTitle, recipeMealType, recipeDetail);
    }
}
