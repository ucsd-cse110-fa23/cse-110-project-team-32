package client;

import client.RecipeListScene.*;
import client.RecipeDetailScene.*;
import client.CreateRecipeScene.*;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class PantryPal extends Application {
    private Stage stage;
    private Scene recipeListScene;
    private Scene recipeDetailScene;
    private Scene CreateRecipeScene;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        this.stage = primaryStage;
        RecipeListView recipeListView = new RecipeListView();
        RecipeDetailView recipeDetailView = new RecipeDetailView();
        CreateRecipeView createRecipeView = new CreateRecipeView();
        this.recipeListScene = new Scene(recipeListView.getBorderPane(), 500, 500);
        this.recipeDetailScene = new Scene(recipeDetailView.getBorderPane(), 500, 500);
        this.CreateRecipeScene = new Scene(createRecipeView.getBorderPane(), 500, 500);

        AppController controller = new AppController(recipeListView, new RecipeListModel(), recipeListScene,
                recipeDetailView, new RecipeDetailModel(), recipeDetailScene,
                createRecipeView, new CreateRecipeModel(), CreateRecipeScene, stage);
        primaryStage.setScene(recipeListScene);
        primaryStage.setTitle("Your Recipes");
        primaryStage.show();
    }
}