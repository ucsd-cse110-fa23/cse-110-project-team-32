package client;

import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

public class RecipeListItem extends HBox {
    private Recipe recipe;

    public RecipeListItem(Recipe recipe) {
        this.recipe = recipe;

        Text titleText = new Text(recipe.getTitle());
        Text mealTypeText = new Text(" - " + recipe.getMealType());

        this.getChildren().addAll(titleText, mealTypeText);
    }

    public Recipe getRecipe() {
        return recipe;
    }

    public String getMealType() {
        return recipe.getMealType();
    }
}
