package client.AddRecipe;

import client.Recipe;
import client.RecipeList.RecipeList;
import client.RecipeList.RecipeListView;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class AddRecipeHeader extends HBox {
    private RecipeListView recipeListView = RecipeListView.getRecipeListView();
    private Button saveButton;
    private Button cancelButton;
    public AddRecipeHeader() {
        this.setStyle("-fx-pref-height: 50px");
        this.saveButton = new Button("save");
        this.cancelButton = new Button("cancel");
        this.getChildren().addAll(this.saveButton, this.cancelButton);
    }

    public void loadButtonsWithHandler(EventHandler<ActionEvent> eventHandler) {
        
        this.saveButton.setOnAction(e -> {
            // save button, onClick -> save to recipe list and return to recipe list
            // get the RecipeList from recipeListView, 
            // get the new Recipe from AddRecipe from AddRecipeView
            recipeListView.addNewRecipe(AddRecipeView.getAddRecipeView().getNewRecipe());
            eventHandler.handle(e);
        });
        this.cancelButton.setOnAction(e -> {
            // cancel button, onClick -> return to recipe list
            eventHandler.handle(e);
        });
    }
}