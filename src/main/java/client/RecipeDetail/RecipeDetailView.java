package client.RecipeDetail;

import client.Recipe;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;

public class RecipeDetailView {
    private static RecipeDetailView recipeDetailView = new RecipeDetailView();
    private BorderPane borderPane;
    private RecipeDetailHeader recipeDetailHeader;
    private RecipeDetail recipeDetail;

    private RecipeDetailView() {
        this.borderPane = new BorderPane();
        borderPane.setPadding(new Insets(20, 20, 20, 20));

        this.recipeDetailHeader = new RecipeDetailHeader();
        this.borderPane.setTop(this.recipeDetailHeader);
        this.recipeDetail = new RecipeDetail();
        this.borderPane.setCenter(this.recipeDetail);
    }

    public static RecipeDetailView getRecipeDetailView() {
        return recipeDetailView;
    }

    public BorderPane getBorderPane() {
        return this.borderPane;
    }

    public void setToRecipeListButtonAction(EventHandler<ActionEvent> eventHandler) {
        this.recipeDetailHeader.setToRecipeListButtonAction(eventHandler);
    }

    public void loadRecipeDetail(Recipe recipe) {
        this.recipeDetail.loadRecipeDetail(recipe);
    }
}