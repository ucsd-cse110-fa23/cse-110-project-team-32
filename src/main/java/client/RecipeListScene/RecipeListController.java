package client.RecipeListScene;

import client.AppController;
import client.HttpResponse.ServerResponse;
import client.Recipe;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javafx.event.ActionEvent;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.RadioMenuItem;

public class RecipeListController {

  private Set<String> selectedMealTypes = new HashSet<>();
  RecipeListView recipeListView;
  RecipeListModel recipeListModel;
  AppController appController;

  public RecipeListController(
      RecipeListView recipeListView,
      RecipeListModel recipeListModel,
      AppController appController) {
    this.recipeListView = recipeListView;
    this.recipeListModel = recipeListModel;
    this.appController = appController;
    this.appController.registerRecipeListController(this);

    recipeListView.setLogOutButtonAction(this::handlelogOutButton);
    recipeListView.setNewRecipeButtonAction(this::handleNewRecipeButtonAction);
    recipeListView.setSortButtonEventHandler(appController);

    recipeListView.setFilterAction(this::handleFilterSelection);
    // readAllRecipesByUID();

  }

  public void readAllRecipesByUID() {
    ServerResponse<List<Recipe>> res = recipeListModel.performGetRecipeListRequest();
    System.out.println("Getting user's recipe list..");
    System.out.println(res);
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

  private void handlelogOutButton(ActionEvent event) {
    appController.changeToLogInScene();
  }

  private void handleSortButton(ActionEvent event) {
    // Sort recipes by title
    appController.sortRecipesByTitle();
  }

  public void sortRecipesByTitle() {
    appController.sortRecipesByTitle();
  }

  private void handleFilterSelection(ActionEvent event) {
    String selectedMealType = recipeListView.getSelectedMealType();

    if (selectedMealType != null && !selectedMealType.equals("Reset Filter")) {
      appController.handleFilter(selectedMealType);
    } else {
      appController.updateRecipeListView(appController.getRecipeList());
    }
  }
}
