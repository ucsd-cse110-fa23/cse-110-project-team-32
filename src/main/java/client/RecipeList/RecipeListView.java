package client.RecipeList;

import client.Recipe;
import client.RecipeDetail.RecipeDetailView;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
// import javafx.scene.layout.GridPane;

public class RecipeListView {
    private static RecipeListView recipeListView = new RecipeListView();
    private BorderPane borderPane;
    private RecipeListHeader header;
    private RecipeList recipeList;

    private RecipeListView() {
        borderPane = new BorderPane();
        this.header = new RecipeListHeader();
        borderPane.setTop(this.header);
        this.recipeList = new RecipeList();
        ScrollPane recipeListScrollPane = new ScrollPane(this.recipeList);
        borderPane.setCenter(recipeListScrollPane);

        borderPane.setPadding(new Insets(20, 20, 20, 20));
        borderPane.setStyle("-fx-background-color: #F0F8FF;");
    }
    
    public static RecipeListView getRecipeListView() {
        return recipeListView;
    }

    public void addNewRecipe(Recipe recipe) {
        this.recipeList.addRecipe(recipe);
    }

    public BorderPane getBorderPane() {
        return this.borderPane;
    }

    public void setToAddRecipeButtonAction(EventHandler<ActionEvent> eventHandler) {
        this.header.getToAddRecipeButton().setOnAction(eventHandler);
    }

    public void setToRecipeDetailButtonAction(EventHandler<MouseEvent> eventHandler) {
        for (RecipeListItem recipeListItem : this.recipeList.getRecipeList()) {
            recipeListItem.setToRecipeDetailButtonAction(eventHandler);
        }
    }
}