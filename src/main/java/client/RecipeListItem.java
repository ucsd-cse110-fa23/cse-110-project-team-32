package client;

import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

public class RecipeListItem extends HBox {
    private Recipe recipe;

    public RecipeListItem(Recipe recipe) {
        this.recipe = recipe;
        this.getChildren().add(new Text(recipe.getTitle()));
    }

    public Recipe getRecipe() {
        return recipe;
    }
}
