package client;

import client.CreateAccountScene.*;
import client.CreateRecipeScene.*;
import client.LogInScene.LogInController;
import client.LogInScene.LogInView;
import client.RecipeDetailScene.*;
import client.RecipeListScene.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.util.Collections;
import java.util.Comparator;
import javax.sound.sampled.*;

// Controller class for managing interactions between views and models in the app
public class AppController {

  // Main stage of app
  private Stage stage;
  // All other references in views
  private RecipeListController recipeListController;
  private RecipeListView recipeListView; // => Rlv
  private VBox recipeListContainer;
  private Scene recipeListScene;
  private RecipeDetailView recipeDetailView; // => Rdv
  private Scene recipeDetailScene;
  private CreateRecipeView createRecipeView; // => Crv
  private Scene createRecipeScene;
  private CreateAccountView createAccountView; // => Cav
  private Scene createAccountScene;
  private LogInController logInController;
  private LogInView logInView;
  private Scene logInScene;

  private static final Double windowWidth = 500D;
  private static final Double windowHeight = 500D;

  public AppController(
      RecipeListView recipeListView,
      RecipeDetailView recipeDetailView,
      CreateRecipeView createRecipeView,
      CreateAccountView createAccountView,
      LogInView logInView,
      Stage stage) {
    this.recipeListView = recipeListView;
    this.recipeListContainer = this.recipeListView.getRecipeListContainer();
    this.recipeDetailView = recipeDetailView;
    this.createRecipeView = createRecipeView;
    this.createAccountView = createAccountView;
    this.logInView = logInView;
    // recipeListView.setSortButtonAction(this);

    this.recipeListScene = new Scene(
        new ScrollPane(recipeListView.getBorderPane()),
        windowWidth,
        windowHeight);
    this.recipeDetailScene = new Scene(
        new ScrollPane(recipeDetailView.getBorderPane()),
        windowWidth,
        windowHeight);
    this.createRecipeScene = new Scene(
        new ScrollPane(createRecipeView.getBorderPane()),
        windowWidth,
        windowHeight);
    this.createAccountScene = new Scene(
        new ScrollPane(createAccountView.getBorderPane()),
        windowWidth,
        windowHeight);
    this.logInScene = new Scene(
        new ScrollPane(logInView.getBorderPane()),
        windowWidth,
        windowHeight);
    // this.recipeListScene = new Scene(recipeListView.getBorderPane(), windowWidth,
    // windowHeight);
    // this.recipeDetailScene = new Scene(recipeDetailView.getBorderPane(),
    // windowWidth, windowHeight);
    // this.createRecipeScene = new Scene(createRecipeView.getBorderPane(),
    // windowWidth, windowHeight);

    this.stage = stage;
    this.stage.setScene(logInScene);
    this.stage.setTitle("Log In");
    // init();

  }

  public AppController() {
    // constructor for testing
    this.recipeListView = new RecipeListView(true);
    this.recipeListContainer = recipeListView.getRecipeListContainer();
  }

  // when user successfully logs in, load the recipe list
  public void registerRecipeListController(RecipeListController rlController) {
    recipeListController = rlController;
  }

  public void registerLogInController(LogInController liController) {
    logInController = liController;
  }

  // when user successfully logs in, load the recipe list
  public void loadRecipeList() {
    if (recipeListController == null)
      return;
    recipeListController.readAllRecipesByUID();
  }

  private void init() {
    changeToRecipeListScene();
  }

  public void initRecipeList(List<Recipe> recipeList) {
    recipeListContainer.getChildren().clear();
    for (Recipe recipe : recipeList) {
      RecipeListItem recipeListItem = new RecipeListItem(recipe);
      recipeListItem.setOnMouseClicked(e -> {
        // the next time to render the detail of this recipe, this recipe would be
        // existing
        this.changeToRecipeDetailScene(recipe, false);
      });
      recipeListContainer.getChildren().add(0, recipeListItem);
    }
  }

  // Filtering feature logic
  public List<Recipe> handleFilter(String mealType) {
    List<Recipe> allRecipes = getRecipeList();
    List<Recipe> filteredRecipes = new ArrayList<>();

    if (mealType != null) {
      for (Recipe recipe : allRecipes) {
        if (mealType.equalsIgnoreCase(recipe.getMealType())) {
          filteredRecipes.add(recipe);
        }
      }
    } else {
      filteredRecipes.addAll(allRecipes); // Display all recipes if no meal type selected
    }

    return filteredRecipes;
  }

  // shows filtered recipes by making others invisible in the display
  public void updateRecipeListView(List<Recipe> recipes) {
    for (Node node : recipeListContainer.getChildren()) {
      if (node instanceof RecipeListItem) {
        RecipeListItem recipeListItem = (RecipeListItem) node;
        Recipe recipe = recipeListItem.getRecipe();
        boolean showRecipe = recipes.contains(recipe);
        recipeListItem.setVisible(showRecipe);
        recipeListItem.setManaged(showRecipe);
        if (showRecipe) {
          recipeListItem.setOnMouseClicked(e -> {
            this.changeToRecipeDetailScene(recipe, false);
          });
        } else {
          recipeListItem.setOnMouseClicked(null);
        }
      }
    }
  }

  public List<Recipe> getRecipeList() {
    // use this function to test
    ObservableList<Node> recipeItemsList = this.recipeListContainer.getChildren();
    List<Recipe> recipeList = new ArrayList<>();
    for (Node c : recipeItemsList) {
      if (c instanceof RecipeListItem) {
        recipeList.add(((RecipeListItem) c).getRecipe());
      }
    }
    return recipeList;
  }

  public void changeToCreateRecipeScene() {
    this.stage.setScene(createRecipeScene);
    this.stage.setTitle("Create New Recipe");
  }

  // Removes recipe from recipe list
  public void removeRecipeFromRecipeList(Recipe recipe) {
    int indexOfRecipeToRemove = 0;
    ObservableList<Node> recipeListItems = recipeListContainer.getChildren();
    for (; indexOfRecipeToRemove < recipeListItems.size(); indexOfRecipeToRemove++) {
      if (recipeListItems.get(indexOfRecipeToRemove) instanceof RecipeListItem) {
        if (((RecipeListItem) recipeListItems.get(indexOfRecipeToRemove)).getRecipe() == recipe)
          break;
      }
    }
    recipeListContainer.getChildren().remove(indexOfRecipeToRemove);
    System.out.println(
        "After deleting a recipe, the size of the recipe list is now " +
            recipeListContainer.getChildren().size());
  }

  // Changes scene to recipe list view scene
  public void changeToRecipeListScene() {
    // Sanity check
    if (stage != null && recipeListScene != null) {
      // Set scene
      stage.setScene(recipeListScene);
      stage.setTitle("Your Recipes");
    }
  }

  // Changes scene to recipe detail view based on the recipe selected
  public void changeToRecipeDetailScene(Recipe recipe, boolean isNewRecipe) {
    // Check if recipe is new
    if (isNewRecipe) {
      this.recipeDetailView.renderNewRecipe(recipe);
    } else { // render existing recipe
      this.recipeDetailView.renderExistingRecipe(recipe);
    }
    this.stage.setScene(recipeDetailScene);
    this.stage.setTitle(recipe.getTitle());
  }

  // Adds new recipe to recipe list view
  public void addNewRecipeToList(Recipe recipe) {
    RecipeListItem recipeListItem = new RecipeListItem(recipe);
    recipeListItem.setOnMouseClicked(e -> {
      // the next time to render the detail of this recipe, this recipe would be
      // existing
      this.changeToRecipeDetailScene(recipe, false);
    });
    recipeListContainer.getChildren().add(0, recipeListItem);
    changeToRecipeListScene();
  }

  public Stage getStage() {
    // for testing purposes
    return stage;
  }

  public void changeToCreateAccountScene() {
    // Sanity check
    if (stage != null && createAccountScene != null) {
      // Set scene
      stage.setScene(createAccountScene);
      stage.setTitle("Create Account");
    }
  }

  public void logOut() {
    // change to log in scene
    logInController.handleLogOut();
    changeToLogInScene();
  }

  public void changeToLogInScene() {
    // Sanity check
    if (stage != null && logInScene != null) {
      // Set scene
      stage.setScene(logInScene);
      stage.setTitle("Log In");
    }
  }
}

