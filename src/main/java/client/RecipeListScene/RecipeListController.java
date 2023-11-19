package client.RecipeListScene;

import java.util.List;

import client.AppController;
import client.Recipe;
import javafx.event.ActionEvent;

public class RecipeListController {
    RecipeListView recipeListView;
    RecipeListModel recipeListModel;
    AppController appController;

    public RecipeListController(RecipeListView recipeListView, RecipeListModel recipeListModel, AppController appController) {
        this.recipeListView = recipeListView;
        this.recipeListModel = recipeListModel;
        this.appController = appController;

        recipeListView.setNewRecipeButtonAction(this::handleNewRecipeButtonAction);
        readAllRecipesByUID();
    }

    private void readAllRecipesByUID() {
        List<Recipe> recipeList = recipeListModel.performGetRecipeListRequest();
        appController.initRecipeList(recipeList);
    }

    private void handleNewRecipeButtonAction(ActionEvent event) {
        appController.changeToCreateRecipeScene();
    }
}
