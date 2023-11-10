package client.RecipeListScene;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class RecipeListView {
    private BorderPane borderPane;
    private VBox recipeListContainer;
    private Button newRecipeButton;

    public RecipeListView() {
        borderPane = new BorderPane();
        
        HBox buttonGroup = new HBox();
        newRecipeButton = new Button("New Recipe");
        buttonGroup.getChildren().add(newRecipeButton);
        borderPane.setTop(buttonGroup);

        recipeListContainer = new VBox();
        borderPane.setCenter(recipeListContainer);
    }

    public RecipeListView(boolean test) {
        // constructor for testing
        recipeListContainer = new VBox();
    }

    public BorderPane getBorderPane() {
        return this.borderPane;
    }

    public VBox getRecipeListContainer() {
        return this.recipeListContainer;
    }

    public void setNewRecipeButtonAction(EventHandler<ActionEvent> eventHandler) {
        this.newRecipeButton.setOnAction(eventHandler);
    }
}
