package client;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

import org.junit.jupiter.api.Assertions;
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
    }

    // @Test
    // public void editRecipeTest() {
    // // Assume you have a RecipeList class with an editRecipe method
    // Recipe recipe = new Recipe(null, null, null);

    // // Assume you have a Recipe object to add to the list
    // Recipe originalRecipe = new Recipe("Spaghetti Bolognese", "Dinner", null);
    // RecipeListItem.append(originalRecipe);

    // // Assume you have edited details for the recipe
    // String newRecipeName = "Updated Spaghetti Bolognese";
    // originalRecipe.setName(newRecipeName);
    // // Add more edits as needed for your use case

    // // Edit the recipe in the list
    // recipeList.editRecipe(originalRecipe);

    // // Get the edited recipe from the list
    // Recipe editedRecipe = recipeList.getRecipeByName(newRecipeName);

    // // Assert that the recipe in the list has been successfully edited
    // Assertions.assertNotNull(editedRecipe, "Edited recipe should not be null");
    // Assertions.assertEquals(newRecipeName, editedRecipe.getName(), "Recipe name
    // should be updated");
    // // Add more assertions as needed for other edited details

    // // Optionally, you can also assert that the original recipe is no longer in
    // the
    // // list
    // Recipe originalRecipeInList = recipeList.getRecipeByName("Spaghetti
    // Bolognese");
    // Assertions.assertNull(originalRecipeInList, "Original recipe should no longer
    // be in the list");
    // }

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

}
