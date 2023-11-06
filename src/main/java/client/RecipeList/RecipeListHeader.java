package client.RecipeList;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class RecipeListHeader extends VBox {
    private Button toAddRecipeButton;

    public RecipeListHeader() {
        // Label helloWorldLabel = new Label("This is the recipe list view");
        this.toAddRecipeButton = new Button("Add Recipe");

        // this.getChildren().add(helloWorldLabel);
        this.getChildren().add(toAddRecipeButton);
    }

    public Button getToAddRecipeButton() {
        return this.toAddRecipeButton;
    }
}
