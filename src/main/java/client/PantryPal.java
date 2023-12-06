package client;

import client.RecipeListScene.*;
import client.RecipeDetailScene.*;
import client.CreateRecipeScene.*;
import client.LogInScene.*;
import client.CreateAccountScene.*;
import javafx.application.Application;
import javafx.stage.Stage;

public class PantryPal extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    AppController appController;

    @Override
    public void start(Stage primaryStage) {
        RecipeListView recipeListView = new RecipeListView(appController);
        RecipeDetailView recipeDetailView = new RecipeDetailView();
        CreateRecipeView createRecipeView = new CreateRecipeView();
        CreateAccountView createAccView = new CreateAccountView();
        LogInView logInView = new LogInView();

        RecipeDetailModel recipeDetailModel = new RecipeDetailModel();
        RecipeListModel recipeListModel = new RecipeListModel();
        CreateRecipeModel createRecipeModel = new CreateRecipeModel();
        CreateAccountModel createAccountModel = new CreateAccountModel();
        LogInModel logInModel = new LogInModel();

        AppController appController = new AppController(recipeListView, recipeDetailView, createRecipeView,
                createAccView, logInView, primaryStage);
        RecipeDetailController rdController = new RecipeDetailController(recipeDetailView, recipeDetailModel,
                appController);
        RecipeListController rlController = new RecipeListController(recipeListView, recipeListModel, appController);
        CreateRecipeController rcController = new CreateRecipeController(createRecipeView, createRecipeModel,
                appController);
        CreateAccountController caController = new CreateAccountController(createAccView, createAccountModel,
                appController);
        LogInController linController = new LogInController(logInView, logInModel, appController);

        primaryStage.setResizable(false);
        primaryStage.show();
    }
}
