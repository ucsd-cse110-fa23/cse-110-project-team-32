package client;
import client.RecipeListScene.*;
import client.RecipeDetailScene.*;
import client.CreateRecipeScene.*;
import javafx.application.Application;
import javafx.stage.Stage;

public class PantryPal extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        RecipeListView recipeListView = new RecipeListView();
        RecipeDetailView recipeDetailView = new RecipeDetailView();
        CreateRecipeView createRecipeView = new CreateRecipeView();
        
        RecipeDetailModel recipeDetailModel = new RecipeDetailModel();
        RecipeListModel recipeListModel = new RecipeListModel();
        CreateRecipeModel createRecipeModel = new CreateRecipeModel();

        AppController appController = new AppController(recipeListView, recipeDetailView, createRecipeView, primaryStage);
        RecipeDetailController rdController = new RecipeDetailController(recipeDetailView, recipeDetailModel, appController);
        RecipeListController rlController = new RecipeListController(recipeListView, recipeListModel, appController);
        CreateRecipeController rcController = new CreateRecipeController(createRecipeView, createRecipeModel, appController);

        primaryStage.show();
    }
}