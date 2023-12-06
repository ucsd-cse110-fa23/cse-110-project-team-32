package client.RecipeListScene;

import client.AppController;
import client.HttpResponse.ServerResponse;
import client.Recipe;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.Action;

import javafx.event.ActionEvent;

public class RecipeListController {

  private Set<String> selectedMealTypes = new HashSet<>();
  public RecipeListView recipeListView;
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
    recipeListView.setReverseSortButtonEventHandler(appController);
    recipeListView.setSortDateButtonEventHandler(appController);
    recipeListView.setSortReverseDateButtonEventHandler(appController);

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
    appController.logOut();
  }

  public void handleSortButton(ActionEvent event) {
    String selectedMealType = recipeListView.getSelectedMealType();
    appController.sortRecipesByTitle(selectedMealType);
  }

  public void handleReverseSortButton(ActionEvent event) {
    String selectedMealType = recipeListView.getSelectedMealType();
    appController.reverseSortRecipesByTitle(selectedMealType);
  }

  public void handleSortDateButton(ActionEvent event) {
    String selectedMealType = recipeListView.getSelectedMealType();
    appController.sortRecipesByDate(selectedMealType);
  }

  public void handleSortReverseDateButton(ActionEvent event) {
    String selectedMealType = recipeListView.getSelectedMealType();
    appController.reverseSortRecipesByDate(selectedMealType);
  }

  public void sortRecipesByTitle() {
    String selectedMealType = recipeListView.getSelectedMealType();
    appController.sortRecipesByTitle(selectedMealType);
    appController.updateRecipeListView(appController.getRecipeList());
  }

  public void sortRecipesByDate() {
    String selectedMealType = recipeListView.getSelectedMealType();
    appController.sortRecipesByDate(selectedMealType);
    appController.updateRecipeListView(appController.getRecipeList());
  }

  public void reverseSortRecipesByDate() {

  }

  public void reverseSortRecipesByTitle() {
    String selectedMealType = recipeListView.getSelectedMealType();
    appController.reverseSortRecipesByTitle(selectedMealType);
    appController.updateRecipeListView(appController.getRecipeList());
  }

  private void handleFilterSelection(ActionEvent event) {
    String selectedMealType = recipeListView.getSelectedMealType();

    if (selectedMealType != null && !selectedMealType.equals("Reset Filter")) {
      List<Recipe> filteredRecipes = appController.handleFilter(selectedMealType);
      appController.updateRecipeListView(filteredRecipes);
    } else if (appController.isSort == true) {
      // System.out.println("HERE");
      appController.updateRecipeListViews2(appController.savedSorted);
    } else if (appController.isReversedSort == true) {
      appController.updateRecipeListViews2(appController.savedReverseSorted);
    } else if (appController.isSortedDate == true) {
      appController.updateRecipeListViews2(appController.savedSortedDate);
    } else if (appController.isReverseSortedDate == true) {
      System.out.println("here");
      appController.updateRecipeListViews2(appController.savedReverseSortedDate);
    } else {
      appController.updateRecipeListView(appController.getRecipeList());
    }
  }
}