package client.RecipeDetail;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class RecipeDetailHeader extends HBox {
    Button toRecipeListButton;
    // this header is for buttons only

    public RecipeDetailHeader() {
        Label helloWorldLabel = new Label("Recipe Detail View");
        this.toRecipeListButton = new Button("Go back to Recipe List View");
        this.getChildren().add(helloWorldLabel);
        this.getChildren().add(toRecipeListButton);
    }

    public void setToRecipeListButtonAction(EventHandler<ActionEvent> eventHandler) {
        this.toRecipeListButton.setOnAction(eventHandler);
    }
}
