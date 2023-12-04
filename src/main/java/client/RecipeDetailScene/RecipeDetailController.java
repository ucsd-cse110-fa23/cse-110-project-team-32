package client.RecipeDetailScene;

import client.AppController;
import client.HttpResponse.ServerResponse;
import client.Recipe;
import javafx.event.ActionEvent;

public class RecipeDetailController {

  RecipeDetailView view;
  RecipeDetailModel model;
  AppController appController;

  public RecipeDetailController(
    RecipeDetailView recipeDetailView,
    RecipeDetailModel recipeDetailModel,
    AppController appController
  ) {
    view = recipeDetailView;
    model = recipeDetailModel;
    this.appController = appController;
    view.setSaveOrEditButtonAction(this::handleSaveOrEditButtonAction);
    view.setBackButtonAction(this::handleBackButtonAction);
    view.setDeleteButtonAction(this::handleDeleteButtonAction);
    view.setRegenerateButtonAction(this::handleRegenerateButtonAction);
  }

  public void handleSaveOrEditButtonAction(ActionEvent event) {
    // if button says save and isNewRecipe, then run save recipe logic and exit to list view
    // if button says save but not new recipe, then just save recipe without changing scene
    // if button save edit, run allow edit logic
    boolean recipeDetailViewIsEditing = view.isEditing();
    boolean recipeDetailViewIsNewRecipe = view.isNewRecipe();

    if (recipeDetailViewIsEditing) {
      view.updateRecipeDetail();
      view.switchToViewOnlyMode();

      if (recipeDetailViewIsNewRecipe) {
        // MAKE A CALL TO MODEL TO ADD A NEW RECIPE
        // add recipe to list
        Recipe newRecipe = view.getRecipe();
        appController.addNewRecipeToList(newRecipe);
        // POST to server
        if (model != null) {
          // model == null in test mode
          ServerResponse<Boolean> postResponse = model.performPostRecipeRequest(
            newRecipe
          );
          System.out.println(postResponse);
          if (postResponse.getStatusCode() == 503) {
            appController.handleServerDown();
            return;
          }
        }
      } else {
        if (view.hasEdited()) {
          // PUT request to the server to save the changes in the recipe
          Recipe editedRecipe = view.getRecipe();
          if (model != null) {
            // model == null in test mode
            ServerResponse<Boolean> updateResponse = model.performUpdateRecipeRequest(
              editedRecipe
            );
            System.out.println(updateResponse);
            if (updateResponse.getStatusCode() == 503) {
              appController.handleServerDown();
              return;
            }
          }
        }
      }
    } else {
      view.switchToEditMode();
    }
  }

  public void handleBackButtonAction(ActionEvent event) {
    // go back to the recipe list view
    appController.changeToRecipeListScene();
  }

  public void handleDeleteButtonAction(ActionEvent event) {
    // if this is a new recipe, deleting is the same as going back to list,
    // not actually adding the recipe to the recipe list
    if (view.isNewRecipe()) {
      appController.changeToRecipeListScene();
      return;
    }
    Recipe currentRecipe = view.getRecipe();
    // DELETE request to server
    if (model != null) {
      // model == null in test mode
      ServerResponse<Boolean> deleteResponse = model.performDeleteRequest(
        currentRecipe
      );
      System.out.println(deleteResponse);
      if (deleteResponse.getStatusCode() == 503) {
        appController.handleServerDown();
        return;
      }
    }
    appController.removeRecipeFromRecipeList(currentRecipe);
    appController.changeToRecipeListScene();
  }

  private void handleRegenerateButtonAction(ActionEvent event) {
    Recipe curRecipe = view.getRecipe();
    ServerResponse<Recipe> createRecipeResponse = model.generateByChatGPT(
      curRecipe.getMealType(),
      curRecipe.getIngredients()
    );
    System.out.println(createRecipeResponse);
    if (createRecipeResponse.getStatusCode() == 503) {
      appController.handleServerDown();
      // navigate to log in and display error
      return;
    }
    if (createRecipeResponse.getStatusCode() == 501) {
      // display error msg
      System.out.println("There was an error when regenerating another recipe");
      System.out.println(createRecipeResponse.getErrorMsg());
      return;
    }

    // render the newly generate recipe
    view.renderNewRecipe(createRecipeResponse.getResponse());
  }
}
