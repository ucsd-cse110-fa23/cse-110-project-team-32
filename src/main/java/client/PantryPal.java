package client;
import client.RecipeListScene.*;
import client.RecipeDetailScene.*;
import client.CreateRecipeScene.*;
import client.CreateAccountScene.*;
import client.LogInScence.*;
import javafx.application.Application;
import javafx.scene.Scene;
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
        CreateAccountView createAccView = new CreateAccountView();
        LogInView logInView = new LogInView();
        
        RecipeDetailModel recipeDetailModel = new RecipeDetailModel();
        RecipeListModel recipeListModel = new RecipeListModel();
        CreateRecipeModel createRecipeModel = new CreateRecipeModel();
        //TODO: add createAccountModel
        //TODO: add logInModel

        AppController appController = new AppController(recipeListView, recipeDetailView, createRecipeView, createAccView, logInView, primaryStage);
        RecipeDetailController rdController = new RecipeDetailController(recipeDetailView, recipeDetailModel, appController);
        RecipeListController rlController = new RecipeListController(recipeListView, recipeListModel, appController);
        CreateRecipeController rcController = new CreateRecipeController(createRecipeView, createRecipeModel, appController);
        //TODO: add createAccountController
        //TODO: add logInController

        primaryStage.show();
    }
}