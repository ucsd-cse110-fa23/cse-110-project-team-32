package client.AddRecipe;

import client.Recipe;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.BorderRepeat;
import javafx.scene.layout.GridPane;

public class AddRecipeView {
    private static AddRecipeView addRecipeView = new AddRecipeView();
    private BorderPane borderPane;
    private AddRecipe addRecipe;
    private AddRecipeHeader addRecipeHeader;

    private AddRecipeView() {
        this.borderPane = new BorderPane();
        borderPane.setPadding(new Insets(20, 20, 20, 20));
        
        this.addRecipeHeader = new AddRecipeHeader();
        this.addRecipe = new AddRecipe();
        this.borderPane.setTop(addRecipeHeader);
        this.borderPane.setCenter(addRecipe);
    }

    public static AddRecipeView getAddRecipeView() {
        return addRecipeView;
    }

    public BorderPane getBorderPane() {
        return this.borderPane;
    }

    public Recipe getNewRecipe() {
        return this.addRecipe.createNewRecipe();
    }

    public void setToRecipeListButtonAction(EventHandler<ActionEvent> eventHandler) {
        this.addRecipeHeader.loadButtonsWithHandler(eventHandler);
    }
}