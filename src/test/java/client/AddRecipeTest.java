package client;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.*;

public class AddRecipeTest {
    @Test
    void addRecipeTest() {
        Recipe recipe = new Recipe("Chicken and Rice", "Dinner", "Its Chicken and Rice man...");
        assertEquals(recipe.getTitle(), "Chicken and Rice");
        assertEquals(recipe.getMealType(), "Dinner");
        assertEquals(recipe.getRecipeDetail(), "Its Chicken and Rice man...");
    }

    // RecipeList basic test 
    @Test
    void addStoredInRecipeListTest() {
        AppController appController = new AppController();
        List<Recipe> recipeList = appController.getRecipeList();
        assertEquals(recipeList.size(), 0);
        appController.addNewRecipeToList(new Recipe("recipe 1", "meal type 1", "detail1"));
        recipeList = appController.getRecipeList();
        assertEquals(recipeList.size(), 1);
    }

    // Test if RecipeList's first Item is most Recently added Recipe()
    @Test
    void recipeListFirstItemIsAddedRecipe() {

        AppController appController = new AppController();
        List<Recipe> recipeList;
        Recipe r1 = new Recipe("Title1", "MealType1", "Details1");
        Recipe r2 = new Recipe("Title2", "MealType2", "Details2");
        Recipe r3 = new Recipe("Title3", "MealType3", "Details3");

        appController.addNewRecipeToList(r1);
        appController.addNewRecipeToList(r3);
        recipeList = appController.getRecipeList();
        assertEquals(r3, recipeList.get(0));
        appController.addNewRecipeToList(r2);
        recipeList = appController.getRecipeList();
        assertEquals(r2, recipeList.get(0));
        assertEquals(3, recipeList.size());
    }

    @Test
    public void deleteRecipeFromListTest() {
        // Implement here :) there is a addNewRecipe and a deleteRecipeFromList function
        // in appController
        AppController appController = new AppController();
        List<Recipe> recipeList;
        Recipe r1 = new Recipe("Title1", "MealType1", "Details1");
        Recipe r2 = new Recipe("Title2", "MealType2", "Details2");
        Recipe r3 = new Recipe("Title3", "MealType3", "Details3");  
        
        appController.addNewRecipeToList(r1);
        appController.addNewRecipeToList(r2);
        appController.addNewRecipeToList(r3);
        recipeList = appController.getRecipeList();
        assertEquals(r3, recipeList.get(0));
        appController.removeRecipeFromRecipeList(r3);
        recipeList = appController.getRecipeList();
        assertEquals(r2, recipeList.get(0));
        appController.removeRecipeFromRecipeList(r1);
        recipeList = appController.getRecipeList();
     }

    private AppController appController;

    @BeforeEach
    void setUp() {
        appController = new AppController();
    }

    @Test
    void editRecipeInListTest() {
        // Create an initial recipe
        Recipe originalRecipe = new Recipe("Original Recipe", "Lunch", "Original details");
        appController.addNewRecipeToList(originalRecipe);

        // Create updated details for the recipe
        String updatedTitle = "Updated Recipe";
        String updatedMealType = "Dinner";
        String updatedDetails = "New details for the updated recipe";

        // Create a new recipe with the updated details
        Recipe updatedRecipe = new Recipe(updatedTitle, updatedMealType, updatedDetails);

        // Remove the original recipe from the list
        appController.removeRecipeFromRecipeList(originalRecipe);

        // Add the updated recipe to the list
        appController.addNewRecipeToList(updatedRecipe);

        // Get the edited recipe from the list
        Recipe editedRecipe = appController.getRecipeList().get(0);

        // Assert that the recipe in the list has been successfully edited
        assertEquals(updatedTitle, editedRecipe.getTitle(), "Recipe title should be updated");
        assertEquals(updatedMealType, editedRecipe.getMealType(), "Recipe meal type should be updated");
        assertEquals(updatedDetails, editedRecipe.getRecipeDetail(), "Recipe details should be updated");
    }

    @Test
    void saveRecipeTest() {
        // Create a new recipe
        Recipe newRecipe = new Recipe("New Recipe", "Dinner", "Details for the new recipe");

        // Add the new recipe to the list which essentially means its added n saved
        appController.addNewRecipeToList(newRecipe);

        // Get the list of "saved" recipes
        List<Recipe> recipeList = appController.getRecipeList();

        // test if the new recipe is in the list.
        assertTrue(recipeList.contains(newRecipe), "New recipe should be in the list");
    }

    @Test
    void viewRecipesTest() {
        // Create some sample recipes
        Recipe recipe1 = new Recipe("Recipe 1", "Breakfast", "Details for Recipe 1");
        Recipe recipe2 = new Recipe("Recipe 2", "Lunch", "Details for Recipe 2");
        Recipe recipe3 = new Recipe("Recipe 3", "Dinner", "Details for Recipe 3");

        appController.addNewRecipeToList(recipe1);
        appController.addNewRecipeToList(recipe2);
        appController.addNewRecipeToList(recipe3);
        List<Recipe> recipeList = appController.getRecipeList();

        assertTrue(recipeList.contains(recipe1), "Recipe 1 should be in the list");
        assertTrue(recipeList.contains(recipe2), "Recipe 2 should be in the list");
        assertTrue(recipeList.contains(recipe3), "Recipe 3 should be in the list");
    } // if the recipes are in the recipeList, that means it is already listed and
      // viewable
      // in the list of recipes. Hence, test passed.

      @Test

      void storedRecipes(){
        
        Recipe recipe1 = new Recipe("Recipe 1", "Breakfast", "Details for Recipe 1");
        Recipe recipe2 = new Recipe("Recipe 2", "Lunch", "Details for Recipe 2");
        Recipe recipe3 = new Recipe("Recipe 3", "Dinner", "Details for Recipe 3");
      }
}
