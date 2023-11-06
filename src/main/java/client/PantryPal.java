package client;

import client.RecipeList.RecipeListView;
import client.AddRecipe.AddRecipeView;
import client.RecipeDetail.RecipeDetailView;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class PantryPal extends Application {
    private Stage stage;
    private RecipeListView recipeListView;
    private Scene recipeListScene;
    private RecipeDetailView recipeDetailView;
    private Scene recipeDetailScene;
    private AddRecipeView addRecipeView;
    private Scene addRecipeScene;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        this.stage = primaryStage;
        this.recipeListView = RecipeListView.getRecipeListView();
        this.recipeDetailView = RecipeDetailView.getRecipeDetailView();
        this.addRecipeView = AddRecipeView.getAddRecipeView();
        
        recipeListView.setToAddRecipeButtonAction(this::handleToAddRecipeButton);
        recipeListView.setToRecipeDetailButtonAction(this::handleToRecipeDetailButton);
        recipeDetailView.setToRecipeListButtonAction(this::handleToRecipeListButton);
        addRecipeView.setToRecipeListButtonAction(this::handleToRecipeListButton);

        this.recipeListScene = new Scene(recipeListView.getBorderPane(), 500, 500);
        this.recipeDetailScene = new Scene(recipeDetailView.getBorderPane(), 500, 500);
        this.addRecipeScene = new Scene(addRecipeView.getBorderPane(), 500, 500);
        primaryStage.setScene(recipeListScene);
        primaryStage.setTitle("PantryPal");
        primaryStage.show();
    }

    private void handleToAddRecipeButton(ActionEvent event) {
        this.stage.setScene(this.addRecipeScene);
        this.stage.setTitle("PantryPal - Create New Recipe");
    }

    private void handleToRecipeDetailButton(MouseEvent event) {
        this.stage.setScene(this.recipeDetailScene);
        this.stage.setTitle("PantryPal - Recipe Detail");
    }

    private void handleToRecipeListButton(ActionEvent event) {
        this.stage.setScene(this.recipeListScene);
        this.stage.setTitle("PantryPal");
        recipeListView.setToRecipeDetailButtonAction(this::handleToRecipeDetailButton);
    }
}