package client.RecipeListScene;

import client.AppController;
import client.HttpResponse.ServerResponse;
import client.Recipe;
import java.util.List;
import javafx.event.ActionEvent;

public class RecipeListController {

  RecipeListView recipeListView;
  RecipeListModel recipeListModel;
  AppController appController;

  public RecipeListController(
    RecipeListView recipeListView,
    RecipeListModel recipeListModel,
    AppController appController
  ) {
    this.recipeListView = recipeListView;
    this.recipeListModel = recipeListModel;
    this.appController = appController;

    recipeListView.setNewRecipeButtonAction(this::handleNewRecipeButtonAction);
  }

  public void readAllRecipesByUID() {
    ServerResponse<List<Recipe>> res = recipeListModel.performGetRecipeListRequest();
    if (res.getStatusCode() != 200) {
      // display error msg, if server down, redirect to log in and display error there
      System.out.println(res.getErrorMsg());
      return;
    }
    List<Recipe> recipeList = res.getResponse();
    appController.initRecipeList(recipeList);
  }

  private void handleNewRecipeButtonAction(ActionEvent event) {
    appController.changeToCreateRecipeScene();
  }
}
