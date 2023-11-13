package client.RecipeListScene;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

// View class for recipe list
public class RecipeListView {
    // Main layout
    private BorderPane borderPane;
    // Container to hold content (list of recipes and buttons)
    private VBox recipeListContainer;
    // Button to add new recipe
    private Button newRecipeButton;

    // Constructor
    public RecipeListView() {
        borderPane = new BorderPane();
        // creates horizontal box for the button
        HBox buttonGroup = new HBox();
        // create button
        newRecipeButton = new Button("New Recipe");
        // add button to button group
        buttonGroup.getChildren().add(newRecipeButton);
        // set button group up top
        borderPane.setTop(buttonGroup);

        // creates container for recipe list
        recipeListContainer = new VBox();
        // set container to be in the center for layout
        borderPane.setCenter(recipeListContainer);
    }


    // @param test
    public RecipeListView(boolean test) {
        // constructor for testing purposes
        recipeListContainer = new VBox();
    }

    // Gets the main layout of recipe list view
    public BorderPane getBorderPane() {
        return this.borderPane;
    }

    // gets the container for recipe list
    public VBox getRecipeListContainer() {
        return this.recipeListContainer;
    }

    // Setter for action when new recipe button is clicked (set to an event)
    public void setNewRecipeButtonAction(EventHandler<ActionEvent> eventHandler) {
        this.newRecipeButton.setOnAction(eventHandler);
    }
}
