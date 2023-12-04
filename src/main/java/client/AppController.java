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

  public void sortRecipesByTitle(String mealType) {
    mealType = mealType.toLowerCase();
    List<Recipe> recipeList = getRecipeList();
    List<Recipe> sortedRecipes = new ArrayList<>();
    Collections.sort(recipeList, Comparator.comparing(Recipe::getTitle));
    if (mealType != null && mealType != "reset filter") {
      for (Recipe x : recipeList) {
        if (x.getMealType().toLowerCase().equals(mealType.toLowerCase())) {
          System.out.println("Sorted recipe: " + x.getTitle());
          sortedRecipes.add(x);
        }
      }
      updateRecipeListViews(sortedRecipes);
      return;
    } else {
      for (Recipe x : recipeList) {
        System.out.println("Sorted recipe: " + x.getTitle());
        sortedRecipes.add(x);
      }

      updateRecipeListViews(sortedRecipes);
    }
  }

  public void reverseSortRecipesByTitle(String mealType) {
    System.out.println("Sort button clicked!");
    mealType = mealType.toLowerCase();
    List<Recipe> recipeList = getRecipeList();
    List<Recipe> sortedRecipes = new ArrayList<>();
    Collections.sort(recipeList, Comparator.comparing(Recipe::getTitle));

    if (mealType != null && mealType != "reset filter") {
      for (Recipe x : recipeList) {
        if (x.getMealType().toLowerCase().equals(mealType.toLowerCase())) {
          System.out.println("Sorted recipe: " + x.getTitle());
          sortedRecipes.add(x);
        }
      }
      // updateRecipeListViews(sortedRecipes);
      // return;
    } else {
      for (Recipe x : recipeList) {
        System.out.println("Sorted recipe: " + x.getTitle());
        sortedRecipes.add(x);
      }

      // updateRecipeListViews(sortedRecipes);
    }

    Collections.reverse(sortedRecipes);
    for (Recipe x : recipeList) {
      System.out.println("Sorted recipe: " + x.getTitle()); // debug statement only
    }
    updateRecipeListViews(sortedRecipes);

    // updateRecipeListViews(sortedRecipes);
    // updateRecipeListView(sortedRecipes);
    // System.out.println("reached"); //Just a debugging tool

  }

  public void sortRecipesByDate() {
    System.out.println("Sort button clicked!");
    List<Recipe> recipeList = getRecipeList();
    List<Recipe> sortedRecipes = new ArrayList<>();
  }

  public void updateRecipeListViews(List<Recipe> recipes) {
    // Clear existing content
    recipeListContainer.getChildren().clear();

    for (Recipe recipe : recipes) {
      RecipeListItem recipeListItem = new RecipeListItem(recipe);
      recipeListItem.setOnMouseClicked(e -> {
        this.changeToRecipeDetailScene(recipe, false);
      });

      // Add the new recipe to the container
      recipeListContainer.getChildren().add(recipeListItem);
    }
  }

  public void updateRecipeList(List<Recipe> recipes) {
    // Clear the existing content
    recipeListContainer.getChildren().clear();

    // Display the contents of the new list
    for (Recipe recipe : recipes) {
      RecipeListItem recipeListItem = new RecipeListItem(recipe);
      recipeListContainer.getChildren().add(recipeListItem);
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
/*
 * generated recipes are automatically in edit mode
 * Recipe original: null
 * Recipe updating: {values from GPT}
 * 
 * In DetailView:
 * boolean isNewRecipe;
 * boolean inEditMode; init false for existing recipe, true for new recipe
 * if true: edit/save button says save, recipe body is TextField
 * if false: edit/save button says edit, recipe body is Text
 * 
 * Back button actions:
 * if the Recipe original is not null:
 * simply exit to list view
 * if the Recipe original is null:
 * this means the current recipe is newly generated and is placed on the recipe
 * list
 * execute delete logic on that recipe
 * exit to list view
 * Edit button actions:
 * on
 * 
 * https://piazza.com/class/lmy9axhgowe53s/post/174
 * 10. Should the user be able to edit the generated recipe, or does editing
 * only entail making changes to their prompt?
 * The user should be able to edit the generated recipe. They won’t be able to
 * edit their prompt (meal type or ingredients).
 * 
 * take the user from add recipe straight to recipe detail page
 * 
 * From e2e story
 * After editng and clicking save, the app stays in the detail view
 * this means: the app stays in detail view unless user click back or delete
 * 
 * Caitlin clicks the "Save" button to add it to her collection.
 * The new recipe now takes the top spot in her list
 * This means clicking save after new recipe generation exits to the list view
 * 
 * https://piazza.com/class/lmy9axhgowe53s/post/164
 * 1. Once Caitlyn makes a recipe, what happens if she does not want to save it?
 * She can just exit out of the detailed view.
 * 
 * 2.If Caitlyn is editing a recipe and doesn’t want to save her changes, what
 * should she do?
 * She can just exit out of the edit view.
 * 
 * 3. Before recipes are deleted, should there be a confirmation
 * message/interface for the user?
 * Sure, that would be a good way to handle deletion in case the user
 * accidentally clicks on delete.
 */
