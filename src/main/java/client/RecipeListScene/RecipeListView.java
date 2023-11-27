package client.RecipeListScene;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

// View class for recipe list
public class RecipeListView {
    // Main layout
    private BorderPane borderPane;
    // Container to hold content (list of recipes and buttons)
    private VBox recipeListContainer;
    // Button to add new recipe
    private Button newRecipeButton;
    // Button to log out
    private Button logOutButton;

    // Constructor
    public RecipeListView() {
        borderPane = new BorderPane();
        // create buttons
        newRecipeButton = new Button("New Recipe");
        newRecipeButton.getStyleClass().add("textBox");
        logOutButton = new Button("Log Out");

        Region r = new Region();
        HBox.setHgrow(r, Priority.ALWAYS);

        // creates horizontal box for the buttons and adds buttons to button group
        HBox buttonGroup = new HBox(newRecipeButton, r, logOutButton);
        buttonGroup.setPrefSize(500D,20);
        
        // // add button to button group
        // buttonGroup.getChildren().add(newRecipeButton);
        // buttonGroup.getChildren().add(logOutButton);

        // set button group up top
        borderPane.setTop(buttonGroup);
        // creates container for recipe list
        recipeListContainer = new VBox();
        // set container to be in the center for layout
        // ScrollPane scrollableList = new ScrollPane(recipeListContainer);
        // scrollableList.setFitToWidth(true);
        // scrollableList.setFitToHeight(true);
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

    // setter method for "Log Out" button
    public void setLogOutButtonAction(EventHandler<ActionEvent> eventHandler){
        this.logOutButton.setOnAction(eventHandler);
    }
}
